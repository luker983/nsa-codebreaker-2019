package com.badguy.terrortime;

import java.util.function.*;
import org.jivesoftware.smack.*;
import android.util.*;
import org.jxmpp.jid.*;
import org.jivesoftware.smackx.vcardtemp.*;
import com.badguy.terrortime.crypto.*;
import org.jivesoftware.smackx.vcardtemp.packet.*;
import java.util.*;
import java.security.*;
import java.io.*;

public class VCardHelper
{
    public static Set<PublicKey> getPublicKeys(final String s) {
        final TerrorTimeApplication instance = TerrorTimeApplication.getInstance();
        try {
            return getPublicKeys(Optional.ofNullable(instance.getContactList().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$VCardHelper$P6PWv0GkVlERoGvnHZoELRpWgaA.INSTANCE).getJidFromString(s).orElseGet(new _$$Lambda$VCardHelper$xN8H2Y8k9I2MDew5p91JYgbazz8(instance.getXMPPTCPConnection().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$VCardHelper$V_ab9BKXwxWILxMuqSWCPrS7qA8.INSTANCE), s))).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$VCardHelper$b5R_GKcmB5jnxOAbd20Qar3Rppk.INSTANCE).asEntityBareJidIfPossible());
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Unable to get public key", t);
            return Collections.emptySet();
        }
    }
    
    public static Set<PublicKey> getPublicKeys(final EntityBareJid entityBareJid) {
        final TerrorTimeApplication instance = TerrorTimeApplication.getInstance();
        try {
            final VCard loadVCard = instance.getVCardManager().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$VCardHelper$ceF_VjXeHgtag_1LCjQSmFTNzmU.INSTANCE).loadVCard(entityBareJid);
            if (loadVCard == null) {
                return Collections.emptySet();
            }
            final String field = loadVCard.getField("DESC");
            if (field == null) {
                return Collections.emptySet();
            }
            final String[] split = field.split(":");
            final HashSet<PublicKey> set = new HashSet<PublicKey>();
            for (int length = split.length, i = 0; i < length; ++i) {
                set.add(CryptHelper.convertPublicPEMtoPublicKey(split[i]).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$VCardHelper$e79pltK3ySt8jVR8Oyuz_Ft4Mo0.INSTANCE));
            }
            return set;
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Error retrieving public key", t);
            return Collections.emptySet();
        }
    }
    
    public static boolean savePublicKey(final String s) {
        final TerrorTimeApplication instance = TerrorTimeApplication.getInstance();
        try {
            final VCardManager vCardManager = instance.getVCardManager().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$VCardHelper$MFz_lOa40tqBYrCyHPzQ0thn4IQ.INSTANCE);
            VCard loadVCard;
            if ((loadVCard = vCardManager.loadVCard(instance.getXMPPTCPConnection().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$VCardHelper$a8pL86RXMnQJ05gZCNGN1wTSYWI.INSTANCE).getUser().asEntityBareJid())) == null) {
                loadVCard = new VCard();
            }
            final String field = loadVCard.getField("DESC");
            if (field != null) {
                final String[] split = field.split(":");
                for (int length = split.length, i = 0; i < length; ++i) {
                    if (Arrays.equals(split[i].getBytes(), s.getBytes())) {
                        return true;
                    }
                }
                final StringBuilder sb = new StringBuilder(s);
                sb.append(":");
                sb.append(field);
                loadVCard.setField("DESC", sb.toString());
            }
            else {
                loadVCard.setField("DESC", s);
            }
            vCardManager.saveVCard(loadVCard);
            return true;
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Error saving public key", t);
            return false;
        }
    }
    
    public static boolean savePublicKey(final PublicKey publicKey) {
        try {
            return savePublicKey(CryptHelper.convertKeyToPEM(publicKey));
        }
        catch (IOException ex) {
            Log.e("EXCEPTION", "Failed to convert public key to PEM", (Throwable)ex);
            return false;
        }
    }
}
