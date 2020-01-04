# Task 7 - Distrust

## Prompt

The arrested terrorist (see Task 3) was not cooperative during initial questioning. He claimed we’d never defeat the underlying cryptography implemented in TerrorTime and the only way to read encrypted messages was if you were one of the communicants. After additional questioning, he revealed that he is actually the lead software developer for TerrorTime and the organization leader directed him to provide a secret way of decrypting and reading everyone's messages. He did not divulge how this was possible, but claimed to have engineered another, more subtle weakness as an insurance policy in case of his capture. After receiving this information, the analysts who found TerrorTime on the suspect’s mobile device mentioned seeing an executable called keygen on his laptop. The terrorist confirmed it is an executable version of the library included with TerrorTime. They have shared a copy of the keygen executable for you to reverse engineer and look for potential vulnerabilities. As expected from the terrorist's statement, the chats stored on the server are all encrypted. Based on your analysis of keygen, develop an attack that can decrypt any TerrorTime message, including those sent in the past, and use this capability to decrypt messages from the organization leader to other cell leaders. Completing task 4 and task 5 are recommended before beginning this task. To prove task completion, submit the following information:

1. Plaintext version of the latest encrypted message from the organization leader
2. Enter the future action (i.e., beyond the current one) they are planning
3. The target (of the terrorist action’s) identity (First and Last Name)
4. The location where the action is to take place
5. Enter the action planned by the terrorists

## Provided Files

* `keygen`

## Solution


### Reverse Engineering

Now we know why the `crypto/Keygen.java` class doesn't use traditional RSA key generation! We have to reverse engineer the `keygen` binary to figure out how to take advantage of this backdoor. I'm not going to go into the whole reverse engineering process because that would take forever, but I will try to summarize my process in solving this task. 

In terms of tools, I used [Ghidra](https://github.com/NationalSecurityAgency/ghidra) and [GDB](https://www.gnu.org/software/gdb/). Ghidra is really nice because it has a decompiler that usually does a decent job of converting the low level instructions into something that looks like C, but it is completely static. GDB on the other hand is dynamic, allowing for interaction with the program while it's running. I've seen a few tools out there that bridge the two to allow them to work together, but I haven't tried any of them out yet. 

Anyway, our goal is to figure out how keys are being generated so we can take advantage of the backdoor with the ultimate goal of being able to decrypt ALL TerrorTIme messages past and  present. A good place to start is running the provided `keygen` utility:

![Keygen](images/keygen.png)

As expected, this produces a key pair. It seems to be legit. Feel free to throw it into `openssl` to get the primes, exponents, and modulus. It doesn't reveal much. The next step is to set up our tools to look at the binary. In Ghidra it's as simple as creating a new project and dragging the file into the project folder. When we open up the binary for the first time it will ask us if we want to analyze it, then it will go through the analysis. It should look like this:

![Ghidra](images/ghidra.png)

This might be overwhelming, but I promise if you stare at it long enough it starts to make sense. I'm by no means a skilled reverse engineer, but my advice would be to not get bogged down with details. Try to filter out the big important function calls and ignore the rest. 

To load `keygen` into GDB we simply need to run:

```
$ gdb keygen
```

And we've successfully attached a debugger to the process. At DEFCON this year I did a few little workshops in the Red Team Village that suggested an add-on for GDB called [PEDA](https://github.com/longld/peda). It automatically follows pointers, updates register values, and guesses character encodings. It made this task so much more manageable.

Now it's your job to go learn how to reverse engineer this binary using these tools. Here are some tips:

1. Rename functions with weird names that you come across. 
2. Try to follow along in Ghidra as you step through the program in GDB.
3. Take notes on what values are used where and by what functions
4. Map out the whole keygen process

Good luck! Here were my results:

![Keygen Flowchart](images/flow.png)

This took me a very very long time to work out so don't be discouraged if it doesn't come easy to you. The elipses on the right side leads into a repeat of most steps with some weird math in the middle, but I was able to reverse all of my keys without going down that rabbit hole. However, I've talked to some people that were not able to complete this task without reversing that portion. If proper prime factors are not found in the first 1000 iterations, it 'permutes' the rkeys and then does it all over again.  

In words, the `keygen` program contains three keys that are used in the backdoor process. Two keys are randomly generated (R1 and R2) and one is a pregenerated RSA public key. The final result is a backdoored private/public key pair with the scheme:

```
N = concat((R2 + i) xor Enc(R1 xor p), random_1024_bits)
```

Where `i` is incremented until `N / p` is also prime. We have the public keys for each and every user so we know the modulus of the RSA key. We just need to extract the embedded prime to be able to build the private key. To do that, we take the first 1024 bits, `xor` them with every possible R2 value, decrypt the result, and `xor` with R1. That should give us `p`. But wait, how do we decrypt something that was encrypted with an RSA public key??? We need that private key too. That means more RE :(

Searching through the binary some more (or maybe you've already discovered this) reveals some hidden options within the script. The `generate_params` section of the code is particularly interesting. Here you can see how the backdoor keys get made! 

There are three sizes that the `keygen` script allows: 512 bit, 1024 bit, and 2048 bit. Each one of these options needs a different sized backdoor key that is half the size of the resulting key. So, `generate_params` creates three different RSA key pairs: 256, 512, and 1024 bit keys. And if you look carefully, you can see that only the 256 bit key is generated legitimately using a safe algorithm! The other two use the backdoor method with the smaller key as the input! 256 bit keys are not that hard to crack, so if we could crack the small key then we could get the 512 bit key through the backdoor method all the way up to the TerrorTime user keys!

### Walking Through The Back Door

Getting the small key is pretty easy, databases like [FactorDB](http://factordb.com/) have factored keys if you provide the modulus. We're going to need it anyway so it's time to start writing code that interacts with RSA keys. First, get the 256 bit key from the `keygen` script. I used `strings` to get it:

![Strings](images/strings.png)

`recover_small_key.py` contains some code that will read in the 256 bit key and print out the modulus as an integer. That can be put into a site like FactorDB:

![Factors](images/factors.png)

The resulting `p` and `q` values go back into the `recover_small_key.py` script to generate the private key. Success!

Now we have to write a script that will take in the three backdoor keys and the backdoored public key, then spit out a corresponding private key. `reverse1.py` and `reverse2.py` are basically the same version of this, just with different paths. We need to use it to get the 512 bit backdoor private key using our new 256 bit key. A successful key reversal looks like this:

![512](images/512.png)

Great! The 1024 bit key can be reversed now too! And because they use the same algorithm, we don't even need to write a new script! Using this technique finally leads us to the organization leader's private key:

```
-----BEGIN RSA PRIVATE KEY-----
MIIEoQIBAAKCAQA9joHKtDi4MZ3Dv6xqBKep9oSVbzoliKFJzC3Bi5+VhbHjnLfk
yUsYfpoFuugBAdL5NtmZQh0Tc5QGxIWMQkiTTyYBhei8ytbGGV3Mpsf+l6f8ps8E
BKJWYXG6/A2sPU8M3GmuLwJWD/+cd2QeFSKM8MrfWrhZaFLiMDlM0gVJxLi1x7nv
Ze2CXtIK4ps/ikUCFvJH20EAAeqUH8vnKVzJYaWTVhvsI6mSfX154J6hbg4y4dmB
Nf2xG4O2c+A6YxglryvHOlGBxaSpqzlx/0SfMkHHUKWKQRiqnYLL74OXD+00sTgZ
oylZByaBrIH4KamKbcjFX5NVwcLJ9prt9rPpAgMBAAECggEAEFgbPDAX80gxRH1L
ESdFQUqUL31F6htZ/9c3uvEQoUnLTs0YwqVzzAApcyYKXeIXG1al9hxozfZdMmLR
tskOrhZQ2AyvHBmbZyPrFOCfr4ii8OF1i/KTC+55nRU5qCbrK1YGtVoD+8fprajB
q38fGl8A7scOtaaOg0IcpLoK72fwBUAkv3FHZmh/wYq0SovOJpcsb/1UeU5e+fQV
o7lTUHxONZf39s3fcWEfuPcV7vyRpbBJhkSsBkWOMWAe0GIdQv8bqv0Jm7siLMkE
jsF1Hh4HtDYRuPjO9ikwyAxPyrYEEuy8M9y+9NYhiQu3F76TfhPdfTmXsaPXY+EN
RIGXZQKBgDTOQPawDPxaC6mcQwhL2JdRC4YlAhA3vmd7Pw5ABhW0pmjxrQF94gcl
gUBum2AwX9g8x9esa79zhNHepBfaXqjaYDerVob5dzXqbs+nLHSJGlUzQ/kS3GlF
s2NGXTdlzeWPpp+edg/CWAIvFytIPs1AymkDfrq+zioXQOH7t4ALAoGBASpsqnBW
fRsew36sDZOcOyKhPUfAXvzPk+htYa5XOUz37mix3s7M+SzcKzJvfXWNC2+GWgti
vcNiGET2RnH+pBenhDrbR6knn84Q4jXN2pjk1g/iEctcTRLcchclsX0ur6pUIRpR
ljBiId+su8sQwV6na656lW8/8CdJ+FXW4pBbAoGAMiOA1XqV6epJ1pMTpsX1cr+2
NHnhOqrjNHh2h75MyMFNnKmUfq0bN3PqBNbA2xUzfCtCCUuixxcVjvxaTHfAl405
/MHIF8K46U2eR7bQ5BnmEe3qZY3+EErpKKMAEYCtfoHmUPrx2MbML86sriEb2GHj
jtCNvR08vgR0S95o/r8CgYEA/82v96WbjdC8AOwYoywrYI7YCZYzlf+VKIoCTkBE
RN7NhP7bH2eCO9rqSExbPZVOdSW0L8hPI5kFdq+oSPTe0cvtQ8pNTglCKZA9Z6Ny
OtcFSw4c9LXE92LxhTfxQX3wZE/DJF1FIWos4R+exf0x1CIs4p3/VCJ5iCQW8/XD
tbsCgYArdBL+Uer+8lIt2ulBdJuN8pzxdPaU1n7MnPR2q+LX1uNMQrKM8UO0c1yz
HsGfGfnvhj5ErRimlhKUXaE4MBzRw+bZEZxIcD4PLZeH0L4pEsPRQ/dIQQWMqY5V
PAUDFDZj/7b9aO9VYsvAMsUCnzEsnvAq+onP09cxju+b6hjooQ==
-----END RSA PRIVATE KEY-----
```

Hmmm....that's great but we still don't know what the terrorists are up to. We have a couple of options:

1. Create a custom `clientDB.db` file with our new private key and masquerade as the organization leader
2. Write a decryption script that reads messages from our intercepted packets

I think both are probably valid, but I know for sure that the second option works because that's what I did. I was too tired at this point to automate anything so I manually copied and pasted all of the messages into a decryption script. It takes in the encrypted symmetric message key, decrypts it with the private key, then uses the AES key and IV to decrypt the message. And we're pretty much done! Malia's messages talk about a kidnapping and hostage situation involving Jasiah Brown, all to fund an assassination:

![Malia](images/malia.png)

 That's almost all the information we need, but we still don't have a location (it's not Yellow Cloud Tech Services, I tried). Masquerading as our cell leader, Aden, reveals the following:

![Aden](images/aden.png)

With how obvious the other answers were, I was surprised that the solution here was `home`, but it worked! We can now foil the terrorists' plans and hopefully never have to reverse engineer anything ever again.

### The End
