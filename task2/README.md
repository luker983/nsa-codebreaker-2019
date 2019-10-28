# Task 2 - Permissions

## Prompt

The TerrorTime APK file contains metadata that describes various security properties of the application that we want to know. Since we now have a copy of the APK thanks to the military operation described in Task 1, we need you to identify and submit the following:

1. App Permissions
2. The SHA256 hash of the Code Signing Certificate
3. The Common Name of the Certificate Signer

## Provided Files

* `terrortime.pcapng`

## Solution

### App Permissions

This challenge is a good time to get Android Studio up and running so the app can be run on an emulator. Make sure that when you load the APK, you choose the *Profile or debug APK* option. If you don't, you might have issues later with logging and the virtual device file explorer. Just in case you've already loaded the APK, you can use *File>Profile or debug APK* to get into the correct state.   

Once the APK is imported into Android Studio we can start looking for permissions. The `AndroidManifest.xml` file contains a lot of the metadata so it's a good place to start. Searching for `permission` reveals the permissions needed:

![Permissions](images/perm.png)

```
INTERNET
ACCESS_NETWORK_STATE
```

### Certificate Information

The Android SDK includes many tools for working with apps, including a tool to work with certificates. `apksigner` can be used to print out the certificate information for an APK. My `apksigner` binary was located under `sdk/build-tools/28.0.3/`. 

```
$ apksigner verify --print-certs terror_time.apk
Signer #1 certificate DN: CN=dev_terrorTime_928908, OU=TSuite
Signer #1 certificate SHA-256 digest: 15dc9416dc4a9e2d8e3d833b448da081b463ca910fdaa5195676bbc48496d2aa
Signer #1 certificate SHA-1 digest: 36d15a486ac4b2c652810ee5af1f98c79f86b3c6
Signer #1 certificate MD5 digest: e1acb777b06d8d12154dfb10e93e6c4b
```

We need the SHA-256 hash and the Common Name (CN):

```
Signer #1 certificate SHA-256 digest: 15dc9416dc4a9e2d8e3d833b448da081b463ca910fdaa5195676bbc48496d2aa
CN=dev_terrorTime_928908
```

That's it! We have access to a more stable version of `terrorTime.apk` now that should be used for the rest of the tasks. **MAKE SURE YOU USE THE NEW VERSION**. Many people have had login issues using the first version of the APK. 
