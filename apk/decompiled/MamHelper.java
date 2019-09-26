package com.badguy.terrortime;

import org.jivesoftware.smackx.mam.*;
import android.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.jxmpp.jid.*;
import java.util.*;

public class MamHelper
{
    public static List<Message> getMessageArchive() {
        final TerrorTimeApplication instance = TerrorTimeApplication.getInstance();
        try {
            final MamManager mamManager = instance.getMamManager().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$MamHelper$TzcYVUR15MlXmeFc7Ce_u9xlcSI.INSTANCE);
            final Jid jid = instance.getContactList().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$MamHelper$0ioGksqsduZg6APM76XVutM6p20.INSTANCE).getUserJid().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$MamHelper$IujRZy8pipkcFtIBvyXzqpoCoq8.INSTANCE);
            final MamManager.MamQueryArgs.Builder builder = MamManager.MamQueryArgs.builder();
            builder.setResultPageSizeTo(10000);
            return mamManager.queryArchive(builder.build()).getMessages().stream().map((Function<? super Object, ?>)new _$$Lambda$MamHelper$qw8unbVl0Vef_LM3CMfYKStNRIo(jid)).collect(Collectors.toCollection((Supplier<List<Message>>)_$$Lambda$MamHelper$OGSS2qx6njxlnp0dnKb4lA3jnw8.INSTANCE));
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Failed to get message archive", t);
            return Collections.emptyList();
        }
    }
}
