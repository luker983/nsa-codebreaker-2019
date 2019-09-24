package com.badguy.terrortime.crypto;

import android.support.v4.util.*;

public class Keygen
{
    public static Pair<String, String> generatePublicPrivateKeys() {
        final String[] split = generateRsaKeyPair("alg1", 2048).split("\n\\s+");
        return new Pair<String, String>(split[0], split[1]);
    }
    
    private static native String generateRsaKeyPair(final String p0, final int p1);
}
