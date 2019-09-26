# Task 5 - Masquerade

## Prompt

The app uses a bespoke application of the OAUTH protocol to authorize and authenticate TerrorTime users to the chat service. Our intelligence indicates that individual terrorists are provided phones with TerrorTime installed and pre-registered to them. They simply need to enter their username and secret PIN to access the chat service, which uses OAUTH behind the scenes to generate a unique token that is used for authentication. This is a non-standard way of using the protocol, but they believe it to be superior to normal password-based authentication since a unique token is used per login vs. a static password. Whether that is indeed the case is up to you to analyze and assess for possible vulnerabilities. Our forensics team recovered a deleted file from the terrorist's hard drive that may aid in your analysis.

Through other intelligence means, we know that the arrested terrorist is a member of one of many cells in a larger organization. He has shown no signs of someone who is acting in a leadership role -- he simply carries out orders given to him from his cell leader, who is likely relaying information from the top-level organizational leader. To uncover information from the cell leader’s conversations, we need access to their account. The messages are end-to-end encrypted, so without the leader's private key we won't be able to decrypt his messages, but we may be able to learn more about the members of the cell and the organization's structure. Analyze the client and server-side components of the authentication process and find a way to masquerade as arbitrary users without knowing their credentials. Take advantage of this vulnerability and masquerade as the cell leader. Access and review the cell leader’s relevant information stored on the server. Use this information to identify and submit the top-level organizational leader’s username and go a step first and submit a copy of the last (still encrypted) message in the organization leader’s chat history. It’s suggested to complete task 4 before attempting this task as task 4 aids in discovering the cell leader’s identity.

## Provided Files

* `auth_verify.pyc`

## Solution

### OAUTH Exploitation

To be able to exploit the token exchange, we need to have a good understanding of how OAUTH2 works in this application. Whenever a user logs in, the Client Secret and ID is sent to the authentication server which returns a token if the credentials are valid. The token is then used to create an XMPP session with the chat server. 

![Diagram](images/diagram.png)

Proxying the login process through a tool like BurpSuite can be used to prove this and see how the token request is formatted. I told my emulator to proxy through `127.0.0.1:8080`, the default Burp listening port, and logged in as my Jason user. 

![Burp](images/burp.png)

Using this request, we can figure out how the credentials are being passed through. The authorization type is Basic, a simple base64 encoding of credentials. Let's reverse the encoding and see what's there.

```
$ echo -n "amFzb24tLXZob3N0LTEwQHRlcnJvcnRpbWUuYXBwOjBtOXdDQkRwU3JqT0ls" | base64 -D
jason--vhost-10@terrortime.app:0m9wCBDpSrjOIl
```

Great! We could make our own token requests if we wanted and experiment with different combinations of usernames and secrets and might find an exploit, but let's keep following the token for now. What does the reponse look like?

![Response](images/response.png)

It's pretty much just the token and expiration date. Not too interesting. So now that token should be sent with the XMPP session request to the chat server in order to authenticate a user. Let's take a look at that `auth_verify,pyc` file to figure out how the XMPP server handles authentication.

`.pyc` files are compiled Python, so we should probably decompile it if we can using a tool like [uncompyle](https://pypi.org/project/uncompyle6/). `uncompyle6 auth_verify.pyc` reveals the authentication code. It takes in a token and an authenication server to verify that the token is correct. See anything weird here? Wouldn't a verification tool need to know the user it's verifying?? Apparently not. 

That's a serious flaw in the authentication system. We can use ANY user's token to authenticate as ANY OTHER USER we want! Now, that doesn't solve the problem of knowing the keys to be able to encrypt/decrypt messages but it's a great step in the right direction. It's time to test it out.

First, we have to register a user with the Client ID of the user we want to masquerade as (in this case `aden--vhost-10@terrortime.app`). Then we need to use the Client Secret for any user we have access to (`jason--vhost-10@terrortime.app`).

![Masq](images/masq.png)

If we tried to log in right now, the app would send a token request with the credentials `aden--vhost-10@terrortime.app:0m9wCBDpSrjOIl`. Aden's ID with Jason's secret. This will fail, so how do we get it to succeed? The magic of BurpSuite. Burp can search a request for a certain expression and replace it automatically with whatever we want using the Match and Replace functionality. We want to replace the `Authorization` header with the correct base64 encoded credentials of Jason: `amFzb24tLXZob3N0LTEwQHRlcnJvcnRpbWUuYXBwOjBtOXdDQkRwU3JqT0ls`. 

![MaR](images/mar.png)

Now we can test it by logging in. But first, know that Burp cannot proxy protocols like XMPP, so we will get a token but not be able to log in completely. The solution is to do one log in attempt with the proxy to save the token to the database, then turn off the proxy and log in again to connect to XMPP with the saved token. This will need to be done every hour. Then you should be able to access Aden's contacts.

![Contact](images/contact.png)

Finally. Now we can take a guess as to which one is the top-level organization leader. I just tried them in order and this one ended up working:

```
malia--vhost-10@terrortime.app
```


### XMPP Intercept

Set XMPP server IP to a MITM relay on my machine and then log in as aden to see encrypted messages.
