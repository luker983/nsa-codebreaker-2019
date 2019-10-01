# Task 6b - Future Message Decryption

## Prompt

Though we might be unable to decrypt messages sent and received in the past without a user's private key, it may still be possible to view future messages in the clear. For this task generate a new public/private key pair and make whatever changes are necessary such that all future messages sent/received within TerrorTime may be decrypted with this private key. Critically, you can not disrupt future legitimate conversations between users.

## Solution

In the first half of this task we were able to cover our tracks by deleting entries from vCards. Now we have to take the opposite approach and edit all of the vCards to include our Public Key Pair. First we need some keys though! `crypto/Keygen.java` is generating 2048 bit RSA keys so we will do the same.

```
$ openssl genrsa -out rsa.private 2046
$ openssl rsa -in rsa.private -out rsa.public -pubout -outform PEM
```

Now, our top-level organization leader only has three contacts, likely meaning three terrorist cells. If each terrortist cell only has two members like all of the cells we've seen so far, then that's a total of 10 users. We can login as all of Malia's contacts to work out the name, and if you look at the login traffic in Burp you can even see which group each terrortist belongs to.

![Group](images/group.png)

Doing this for all of the cell leaders reveals the following users:

```
* management
    * malia
    * aden
    * leila
    * savannah
    
* cell-0
    * jason
    * jazmin

* cell-1
    * isaac
    * georgia

* cell-2
    * ava
    * emmett
```

This doesn't seem too bad to just brute force, and a lot easier than making our own client to auth into each user and reset the vCard. So, logging into each user and sending the vCard reset request with only our Public Key should do the trick! The request looks like this:

```
<iq id='0maP3-360' type='set'><vCard xmlns='vcard-temp'><DESC>
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0Z9xykmNjTOY05dwyE34
xhduKMO3DEo2rL74zXrbMh9hxS89qteA5m9OAHPOXo9/YlF1YKwpnejkGgfC4Ozg
NyZoB5QrJ1Yis9Gb4GMK4fK/4P2PW99FaDUjtx3YYSEEkOyHj/QukgzfGtUw3vIt
ImO9VEqUKXdcQEgIUzhFIBQLF7f9WcqYUFGsdWVpX+gvDmio0XOpNJ93chFqvjkl
9LbXiWdxhrnN8MS26PKytKBZOZIIA3Qcmp5rAYinWGW1XeQB8BqaLD30Ujl4wT+Z
kGEzTbqmQyc7iWMuoI6RfLM5JernsvJ/PJXmOysjIBMqLOBn5hNCNgWc9CH3Xwcu
ZwIDAQAB
-----END PUBLIC KEY-----
</DESC></vCard></iq>
```

That was not fun, but after changing the vCard for all 10 users we should be able to read all future messages without interfering in legitimate conversations. Except that when I tried to submit my keys it said that future users wouldn't be able to read messages! That didn't make any sense to me because everytime a user logs in they add their keys to the vCard, allowing for conversation.
