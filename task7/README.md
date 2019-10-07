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

Now we know why the `crypto/Keygen.java` class doesn't use traditional Java key generation. My guess is that somehow the keys are generated in a way that makes it possible to derive the private key from the public key. 

Backdoor is generated using pre-built pub/priv key pair and some base64 keys to encrypt/obfuscate one of the primes inside of the resulting pub/priv key pair. The key needs to be recovered using the 256 bit key.

Production of public key:

```
n = Enc(k1 xor p) xor k2
q = n / p
x = is_prime(q)
if x:
    done
else:
    k2 += 1
```
