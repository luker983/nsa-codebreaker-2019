package com.badguy.terrortime;

import android.content.*;
import org.json.*;
import java.net.*;
import java.security.cert.*;
import javax.net.ssl.*;
import java.security.*;
import android.util.*;

public class ClientCredentialTokenRequest
{
    private Context appContext;
    private String audience;
    private String authorization;
    private String clientId;
    private String grantType;
    private JSONObject jsonTokenResponse;
    private int port;
    private JSONObject postNameValues;
    private String scope;
    private String secret;
    private URL site;
    private Long tokenExpiration;
    private String tokenValue;
    
    public ClientCredentialTokenRequest(final Context appContext, final String s, final String clientId, final String secret, final String grantType, final String scope, final String audience, final int port) throws Exception {
        this.postNameValues = new JSONObject();
        this.port = 443;
        Label_0261: {
            if (s == null) {
                break Label_0261;
            }
            Label_0249: {
                if (clientId == null) {
                    break Label_0249;
                }
                Label_0237: {
                    if (secret == null) {
                        break Label_0237;
                    }
                    Label_0225: {
                        if (scope == null) {
                            break Label_0225;
                        }
                        Label_0213: {
                            if (grantType == null) {
                                break Label_0213;
                            }
                            Label_0201: {
                                if (audience == null) {
                                    break Label_0201;
                                }
                                Label_0189: {
                                    if (appContext == null) {
                                        break Label_0189;
                                    }
                                    try {
                                        this.appContext = appContext;
                                        this.site = new URL(s);
                                        this.clientId = clientId;
                                        this.secret = secret;
                                        this.grantType = grantType;
                                        this.scope = scope;
                                        this.audience = audience;
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("Basic ");
                                        sb.append(this.getBase64AuthorizationString(this.clientId, this.secret));
                                        this.authorization = sb.toString();
                                        this.port = port;
                                        this.postNameValues.put("audience", (Object)"");
                                        this.postNameValues.put("grant_type", (Object)this.grantType);
                                        this.postNameValues.put("scope", (Object)this.scope);
                                        return;
                                        throw new RuntimeException("context was NULL");
                                        throw new RuntimeException("scope String was NULL");
                                        throw new RuntimeException("URL site String was NULL");
                                        throw new RuntimeException("audience String was NULL");
                                        throw new RuntimeException("client id String was NULL");
                                        throw new RuntimeException("grant type String was NULL");
                                        throw new RuntimeException("secret String was NULL");
                                    }
                                    catch (Exception ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private SSLContext createAcceptAllCertsContext() throws NoSuchAlgorithmException, NullPointerException, KeyManagementException {
        final X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(final X509Certificate[] array, final String s) {
            }
            
            @Override
            public void checkServerTrusted(final X509Certificate[] array, final String s) {
            }
            
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        try {
            final SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, new TrustManager[] { x509TrustManager }, null);
            return instance;
        }
        catch (KeyManagementException ex) {
            Log.e("EXCEPTION LOG", ex.getMessage());
            throw new KeyManagementException(ex);
        }
        catch (NullPointerException ex2) {
            Log.e("EXCEPTION LOG", ex2.getMessage());
            throw new NullPointerException();
        }
        catch (NoSuchAlgorithmException ex3) {
            Log.e("EXCEPTION LOG", ex3.getMessage());
            throw new NoSuchAlgorithmException(ex3);
        }
    }
    
    private String getBase64AuthorizationString(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(":");
        sb.append(s2);
        return Base64.encodeToString(sb.toString().getBytes(), 10);
    }
    
    private boolean isValidResponse() throws Exception {
        final boolean b = false;
        final JSONObject jsonTokenResponse = this.jsonTokenResponse;
        if (jsonTokenResponse != null) {
            try {
                final String string = jsonTokenResponse.getString("access_token");
                final Integer value = this.jsonTokenResponse.getInt("expires_in");
                final String string2 = this.jsonTokenResponse.getString("scope");
                final String string3 = this.jsonTokenResponse.getString("token_type");
                boolean b2 = b;
                if (string != null) {
                    b2 = b;
                    if (string.length() > 0) {
                        b2 = b;
                        if (value != null) {
                            b2 = b;
                            if (value > 0) {
                                b2 = b;
                                if (string2 != null) {
                                    b2 = b;
                                    if (string2.equals(this.scope)) {
                                        b2 = b;
                                        if (string3 != null) {
                                            final boolean equals = string3.equals("bearer");
                                            b2 = b;
                                            if (equals) {
                                                b2 = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return b2;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        throw new RuntimeException("Unknown token response error.");
    }
    
    private tokenHelper requestAccessToken() throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: new             Ljava/net/URL;
        //     5: dup            
        //     6: aload_0        
        //     7: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.site:Ljava/net/URL;
        //    10: invokevirtual   java/net/URL.getProtocol:()Ljava/lang/String;
        //    13: aload_0        
        //    14: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.site:Ljava/net/URL;
        //    17: invokevirtual   java/net/URL.getHost:()Ljava/lang/String;
        //    20: aload_0        
        //    21: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.port:I
        //    24: aload_0        
        //    25: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.site:Ljava/net/URL;
        //    28: invokevirtual   java/net/URL.getFile:()Ljava/lang/String;
        //    31: invokespecial   java/net/URL.<init>:(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V
        //    34: astore_2       
        //    35: aload_0        
        //    36: invokespecial   com/badguy/terrortime/ClientCredentialTokenRequest.createAcceptAllCertsContext:()Ljavax/net/ssl/SSLContext;
        //    39: astore_3       
        //    40: aload_2        
        //    41: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    44: checkcast       Ljavax/net/ssl/HttpsURLConnection;
        //    47: astore          4
        //    49: aload           4
        //    51: aload_3        
        //    52: invokevirtual   javax/net/ssl/SSLContext.getSocketFactory:()Ljavax/net/ssl/SSLSocketFactory;
        //    55: invokevirtual   javax/net/ssl/HttpsURLConnection.setSSLSocketFactory:(Ljavax/net/ssl/SSLSocketFactory;)V
        //    58: aload           4
        //    60: new             Lcom/badguy/terrortime/ClientCredentialTokenRequest$2;
        //    63: dup            
        //    64: aload_0        
        //    65: invokespecial   com/badguy/terrortime/ClientCredentialTokenRequest$2.<init>:(Lcom/badguy/terrortime/ClientCredentialTokenRequest;)V
        //    68: invokevirtual   javax/net/ssl/HttpsURLConnection.setHostnameVerifier:(Ljavax/net/ssl/HostnameVerifier;)V
        //    71: aload           4
        //    73: ldc             "POST"
        //    75: invokevirtual   javax/net/ssl/HttpsURLConnection.setRequestMethod:(Ljava/lang/String;)V
        //    78: aload           4
        //    80: ldc             "Content-Type"
        //    82: ldc             "application/x-www-form-urlencoded"
        //    84: invokevirtual   javax/net/ssl/HttpsURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    87: aload           4
        //    89: ldc_w           "Authorization"
        //    92: aload_0        
        //    93: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.authorization:Ljava/lang/String;
        //    96: invokevirtual   javax/net/ssl/HttpsURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    99: aload           4
        //   101: ldc_w           "X-Server-Select"
        //   104: ldc_w           "oauth"
        //   107: invokevirtual   javax/net/ssl/HttpsURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   110: aload           4
        //   112: iconst_1       
        //   113: invokevirtual   javax/net/ssl/HttpsURLConnection.setDoInput:(Z)V
        //   116: aload           4
        //   118: iconst_1       
        //   119: invokevirtual   javax/net/ssl/HttpsURLConnection.setDoOutput:(Z)V
        //   122: aload           4
        //   124: iconst_0       
        //   125: invokevirtual   javax/net/ssl/HttpsURLConnection.setUseCaches:(Z)V
        //   128: aload           4
        //   130: sipush          30000
        //   133: invokevirtual   javax/net/ssl/HttpsURLConnection.setReadTimeout:(I)V
        //   136: aload           4
        //   138: sipush          30000
        //   141: invokevirtual   javax/net/ssl/HttpsURLConnection.setConnectTimeout:(I)V
        //   144: aload_0        
        //   145: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.postNameValues:Lorg/json/JSONObject;
        //   148: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   151: ldc_w           "UTF-8"
        //   154: invokevirtual   java/lang/String.getBytes:(Ljava/lang/String;)[B
        //   157: pop            
        //   158: aload_0        
        //   159: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.postNameValues:Lorg/json/JSONObject;
        //   162: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   165: pop            
        //   166: aload_0        
        //   167: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.postNameValues:Lorg/json/JSONObject;
        //   170: invokevirtual   org/json/JSONObject.keys:()Ljava/util/Iterator;
        //   173: astore_2       
        //   174: new             Ljava/lang/StringBuilder;
        //   177: astore_3       
        //   178: aload_3        
        //   179: invokespecial   java/lang/StringBuilder.<init>:()V
        //   182: aload_2        
        //   183: invokeinterface java/util/Iterator.hasNext:()Z
        //   188: istore          5
        //   190: iload           5
        //   192: ifeq            290
        //   195: aload_2        
        //   196: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   201: checkcast       Ljava/lang/String;
        //   204: astore          6
        //   206: aload_0        
        //   207: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.postNameValues:Lorg/json/JSONObject;
        //   210: aload           6
        //   212: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   215: astore          7
        //   217: new             Ljava/lang/StringBuilder;
        //   220: astore          8
        //   222: aload           8
        //   224: invokespecial   java/lang/StringBuilder.<init>:()V
        //   227: aload           8
        //   229: aload           6
        //   231: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   234: pop            
        //   235: aload           8
        //   237: ldc_w           "="
        //   240: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   243: pop            
        //   244: aload           8
        //   246: aload           7
        //   248: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   251: pop            
        //   252: aload_3        
        //   253: aload           8
        //   255: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   258: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   261: pop            
        //   262: aload_2        
        //   263: invokeinterface java/util/Iterator.hasNext:()Z
        //   268: ifeq            279
        //   271: aload_3        
        //   272: ldc_w           "&"
        //   275: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   278: pop            
        //   279: goto            182
        //   282: astore_3       
        //   283: goto            983
        //   286: astore_3       
        //   287: goto            962
        //   290: invokestatic    java/lang/System.currentTimeMillis:()J
        //   293: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   296: astore          7
        //   298: new             Ljava/lang/StringBuilder;
        //   301: astore_2       
        //   302: aload_2        
        //   303: invokespecial   java/lang/StringBuilder.<init>:()V
        //   306: aload_2        
        //   307: ldc_w           "Current systemtime: "
        //   310: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   313: pop            
        //   314: aload_2        
        //   315: aload           7
        //   317: invokevirtual   java/lang/Long.longValue:()J
        //   320: ldc2_w          1000
        //   323: ldiv           
        //   324: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   327: pop            
        //   328: ldc_w           "TOKENREQUEST"
        //   331: aload_2        
        //   332: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   335: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   338: pop            
        //   339: aload           7
        //   341: ifnull          934
        //   344: new             Ljava/io/BufferedOutputStream;
        //   347: astore          6
        //   349: aload           6
        //   351: aload           4
        //   353: invokevirtual   javax/net/ssl/HttpsURLConnection.getOutputStream:()Ljava/io/OutputStream;
        //   356: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   359: aload_3        
        //   360: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   363: ldc_w           "UTF-8"
        //   366: invokevirtual   java/lang/String.getBytes:(Ljava/lang/String;)[B
        //   369: astore          8
        //   371: new             Ljava/lang/StringBuilder;
        //   374: astore_2       
        //   375: aload_2        
        //   376: invokespecial   java/lang/StringBuilder.<init>:()V
        //   379: aload_2        
        //   380: ldc_w           "Requesting token. Destination: "
        //   383: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   386: pop            
        //   387: aload_2        
        //   388: aload_0        
        //   389: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.site:Ljava/net/URL;
        //   392: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   395: pop            
        //   396: aload_2        
        //   397: ldc_w           ". Authorization property: "
        //   400: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   403: pop            
        //   404: aload_2        
        //   405: aload_0        
        //   406: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.authorization:Ljava/lang/String;
        //   409: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   412: pop            
        //   413: aload_2        
        //   414: ldc_w           ". Request: "
        //   417: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   420: pop            
        //   421: aload_2        
        //   422: aload_3        
        //   423: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   426: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   429: pop            
        //   430: ldc_w           "TOKENREQUEST"
        //   433: aload_2        
        //   434: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   437: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   440: pop            
        //   441: aload           6
        //   443: aload           8
        //   445: iconst_0       
        //   446: aload           8
        //   448: arraylength    
        //   449: invokevirtual   java/io/OutputStream.write:([BII)V
        //   452: aload           6
        //   454: invokevirtual   java/io/OutputStream.close:()V
        //   457: new             Ljava/lang/StringBuffer;
        //   460: astore_2       
        //   461: aload_2        
        //   462: invokespecial   java/lang/StringBuffer.<init>:()V
        //   465: aload           4
        //   467: invokevirtual   javax/net/ssl/HttpsURLConnection.getInputStream:()Ljava/io/InputStream;
        //   470: astore          6
        //   472: new             Ljava/io/InputStreamReader;
        //   475: astore          8
        //   477: aload           8
        //   479: aload           6
        //   481: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   484: new             Ljava/io/BufferedReader;
        //   487: astore_3       
        //   488: aload_3        
        //   489: aload           8
        //   491: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   494: aload_3        
        //   495: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   498: astore          9
        //   500: aload           9
        //   502: ifnull          515
        //   505: aload_2        
        //   506: aload           9
        //   508: invokevirtual   java/lang/StringBuffer.append:(Ljava/lang/String;)Ljava/lang/StringBuffer;
        //   511: pop            
        //   512: goto            494
        //   515: new             Ljava/lang/StringBuilder;
        //   518: astore          9
        //   520: aload           9
        //   522: invokespecial   java/lang/StringBuilder.<init>:()V
        //   525: aconst_null    
        //   526: astore_3       
        //   527: aload           9
        //   529: ldc_w           "Received token: "
        //   532: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   535: pop            
        //   536: aload           9
        //   538: aload_2        
        //   539: invokevirtual   java/lang/StringBuffer.toString:()Ljava/lang/String;
        //   542: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   545: pop            
        //   546: ldc_w           "TOKENREQUEST"
        //   549: aload           9
        //   551: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   554: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   557: pop            
        //   558: aload_2        
        //   559: invokevirtual   java/lang/StringBuffer.toString:()Ljava/lang/String;
        //   562: invokevirtual   java/lang/String.length:()I
        //   565: ifeq            907
        //   568: aload_2        
        //   569: invokevirtual   java/lang/StringBuffer.toString:()Ljava/lang/String;
        //   572: astore_2       
        //   573: new             Lorg/json/JSONObject;
        //   576: astore_1       
        //   577: aload_1        
        //   578: aload_2        
        //   579: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //   582: aload_0        
        //   583: aload_1        
        //   584: putfield        com/badguy/terrortime/ClientCredentialTokenRequest.jsonTokenResponse:Lorg/json/JSONObject;
        //   587: aload_0        
        //   588: invokespecial   com/badguy/terrortime/ClientCredentialTokenRequest.isValidResponse:()Z
        //   591: istore          5
        //   593: iload           5
        //   595: ifeq            884
        //   598: aload_0        
        //   599: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.jsonTokenResponse:Lorg/json/JSONObject;
        //   602: astore_1       
        //   603: aload_0        
        //   604: aload_1        
        //   605: ldc             "expires_in"
        //   607: invokevirtual   org/json/JSONObject.getLong:(Ljava/lang/String;)J
        //   610: ldc2_w          1000
        //   613: lmul           
        //   614: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   617: putfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenExpiration:Ljava/lang/Long;
        //   620: aload_0        
        //   621: aload_0        
        //   622: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenExpiration:Ljava/lang/Long;
        //   625: invokevirtual   java/lang/Long.longValue:()J
        //   628: aload           7
        //   630: invokevirtual   java/lang/Long.longValue:()J
        //   633: ladd           
        //   634: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   637: putfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenExpiration:Ljava/lang/Long;
        //   640: aload_0        
        //   641: aload_0        
        //   642: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenExpiration:Ljava/lang/Long;
        //   645: invokevirtual   java/lang/Long.longValue:()J
        //   648: ldc2_w          1000
        //   651: ldiv           
        //   652: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   655: putfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenExpiration:Ljava/lang/Long;
        //   658: aload_0        
        //   659: aload_0        
        //   660: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.jsonTokenResponse:Lorg/json/JSONObject;
        //   663: ldc             "access_token"
        //   665: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   668: putfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenValue:Ljava/lang/String;
        //   671: new             Lcom/badguy/terrortime/ClientCredentialTokenRequest$tokenHelper;
        //   674: astore_1       
        //   675: aload_0        
        //   676: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenExpiration:Ljava/lang/Long;
        //   679: invokevirtual   java/lang/Long.intValue:()I
        //   682: istore          10
        //   684: aload_1        
        //   685: iload           10
        //   687: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   690: aload_0        
        //   691: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenValue:Ljava/lang/String;
        //   694: invokevirtual   java/lang/String.getBytes:()[B
        //   697: invokespecial   com/badguy/terrortime/ClientCredentialTokenRequest$tokenHelper.<init>:(Ljava/lang/Integer;[B)V
        //   700: aload_1        
        //   701: astore_2       
        //   702: new             Ljava/lang/StringBuilder;
        //   705: astore_3       
        //   706: aload_1        
        //   707: astore_2       
        //   708: aload_3        
        //   709: invokespecial   java/lang/StringBuilder.<init>:()V
        //   712: aload_1        
        //   713: astore_2       
        //   714: aload_3        
        //   715: ldc_w           "Token: "
        //   718: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   721: pop            
        //   722: aload_1        
        //   723: astore_2       
        //   724: aload_3        
        //   725: aload_0        
        //   726: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenValue:Ljava/lang/String;
        //   729: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   732: pop            
        //   733: aload_1        
        //   734: astore_2       
        //   735: aload_3        
        //   736: ldc_w           ". Expiration: "
        //   739: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   742: pop            
        //   743: aload_1        
        //   744: astore_2       
        //   745: aload_3        
        //   746: aload_0        
        //   747: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenExpiration:Ljava/lang/Long;
        //   750: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   753: pop            
        //   754: aload_1        
        //   755: astore_2       
        //   756: ldc_w           "TOKENREQUEST"
        //   759: aload_3        
        //   760: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   763: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   766: pop            
        //   767: aload_1        
        //   768: astore_2       
        //   769: aload_0        
        //   770: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenExpiration:Ljava/lang/Long;
        //   773: invokevirtual   java/lang/Long.intValue:()I
        //   776: i2l            
        //   777: aload           7
        //   779: invokevirtual   java/lang/Long.longValue:()J
        //   782: ldc2_w          1000
        //   785: ldiv           
        //   786: lcmp           
        //   787: iflt            837
        //   790: aload_1        
        //   791: astore_2       
        //   792: aload_0        
        //   793: getfield        com/badguy/terrortime/ClientCredentialTokenRequest.tokenExpiration:Ljava/lang/Long;
        //   796: invokevirtual   java/lang/Long.longValue:()J
        //   799: ldc2_w          4294967295
        //   802: lcmp           
        //   803: ifgt            837
        //   806: aload_1        
        //   807: astore_2       
        //   808: aload           8
        //   810: invokevirtual   java/io/InputStreamReader.close:()V
        //   813: aload           6
        //   815: ifnull          825
        //   818: aload_1        
        //   819: astore_2       
        //   820: aload           6
        //   822: invokevirtual   java/io/InputStream.close:()V
        //   825: aload           4
        //   827: ifnull          835
        //   830: aload           4
        //   832: invokevirtual   javax/net/ssl/HttpsURLConnection.disconnect:()V
        //   835: aload_1        
        //   836: areturn        
        //   837: aload_1        
        //   838: astore_2       
        //   839: ldc_w           "EXCEPTION"
        //   842: ldc_w           "Invalid Token Expiration."
        //   845: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   848: pop            
        //   849: aload_1        
        //   850: astore_2       
        //   851: new             Ljava/lang/RuntimeException;
        //   854: astore_3       
        //   855: aload_1        
        //   856: astore_2       
        //   857: aload_3        
        //   858: ldc_w           "CLIENTCREDENTIALSTOKENREQUEST: Invalid token expiration"
        //   861: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   864: aload_1        
        //   865: astore_2       
        //   866: aload_3        
        //   867: athrow         
        //   868: astore_3       
        //   869: goto            962
        //   872: astore_3       
        //   873: goto            983
        //   876: astore_2       
        //   877: aload_3        
        //   878: astore_1       
        //   879: aload_2        
        //   880: astore_3       
        //   881: goto            962
        //   884: ldc_w           "EXCEPTION"
        //   887: ldc_w           "Response not valid."
        //   890: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   893: pop            
        //   894: new             Ljava/lang/RuntimeException;
        //   897: astore_3       
        //   898: aload_3        
        //   899: ldc_w           "Did not receive valid response to token request"
        //   902: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   905: aload_3        
        //   906: athrow         
        //   907: new             Ljava/lang/RuntimeException;
        //   910: astore_3       
        //   911: aload_3        
        //   912: ldc_w           "Did not receive response to token request"
        //   915: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   918: aload_3        
        //   919: athrow         
        //   920: astore_3       
        //   921: goto            983
        //   924: astore_1       
        //   925: aload_3        
        //   926: astore_2       
        //   927: aload_1        
        //   928: astore_3       
        //   929: aload_2        
        //   930: astore_1       
        //   931: goto            962
        //   934: new             Ljava/lang/RuntimeException;
        //   937: astore_3       
        //   938: aload_3        
        //   939: ldc_w           "Failed to acquire system time."
        //   942: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   945: aload_3        
        //   946: athrow         
        //   947: astore_3       
        //   948: goto            983
        //   951: astore_3       
        //   952: aconst_null    
        //   953: astore_1       
        //   954: goto            962
        //   957: astore_3       
        //   958: goto            983
        //   961: astore_3       
        //   962: aload_1        
        //   963: astore_2       
        //   964: new             Ljava/lang/RuntimeException;
        //   967: astore          7
        //   969: aload_1        
        //   970: astore_2       
        //   971: aload           7
        //   973: aload_3        
        //   974: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //   977: aload_1        
        //   978: astore_2       
        //   979: aload           7
        //   981: athrow         
        //   982: astore_3       
        //   983: aload           4
        //   985: ifnull          993
        //   988: aload           4
        //   990: invokevirtual   javax/net/ssl/HttpsURLConnection.disconnect:()V
        //   993: aload_3        
        //   994: athrow         
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  144    182    961    962    Ljava/lang/Exception;
        //  144    182    957    961    Any
        //  182    190    961    962    Ljava/lang/Exception;
        //  182    190    957    961    Any
        //  195    279    286    290    Ljava/lang/Exception;
        //  195    279    282    286    Any
        //  290    339    961    962    Ljava/lang/Exception;
        //  290    339    957    961    Any
        //  344    441    961    962    Ljava/lang/Exception;
        //  344    441    957    961    Any
        //  441    494    961    962    Ljava/lang/Exception;
        //  441    494    957    961    Any
        //  494    500    961    962    Ljava/lang/Exception;
        //  494    500    957    961    Any
        //  505    512    286    290    Ljava/lang/Exception;
        //  505    512    282    286    Any
        //  515    525    961    962    Ljava/lang/Exception;
        //  515    525    957    961    Any
        //  527    593    924    934    Ljava/lang/Exception;
        //  527    593    920    924    Any
        //  598    603    924    934    Ljava/lang/Exception;
        //  598    603    920    924    Any
        //  603    684    876    884    Ljava/lang/Exception;
        //  603    684    872    876    Any
        //  684    700    951    957    Ljava/lang/Exception;
        //  684    700    947    951    Any
        //  702    706    868    872    Ljava/lang/Exception;
        //  702    706    982    983    Any
        //  708    712    868    872    Ljava/lang/Exception;
        //  708    712    982    983    Any
        //  714    722    868    872    Ljava/lang/Exception;
        //  714    722    982    983    Any
        //  724    733    868    872    Ljava/lang/Exception;
        //  724    733    982    983    Any
        //  735    743    868    872    Ljava/lang/Exception;
        //  735    743    982    983    Any
        //  745    754    868    872    Ljava/lang/Exception;
        //  745    754    982    983    Any
        //  756    767    868    872    Ljava/lang/Exception;
        //  756    767    982    983    Any
        //  769    790    868    872    Ljava/lang/Exception;
        //  769    790    982    983    Any
        //  792    806    868    872    Ljava/lang/Exception;
        //  792    806    982    983    Any
        //  808    813    868    872    Ljava/lang/Exception;
        //  808    813    982    983    Any
        //  820    825    868    872    Ljava/lang/Exception;
        //  820    825    982    983    Any
        //  839    849    868    872    Ljava/lang/Exception;
        //  839    849    982    983    Any
        //  851    855    868    872    Ljava/lang/Exception;
        //  851    855    982    983    Any
        //  857    864    868    872    Ljava/lang/Exception;
        //  857    864    982    983    Any
        //  866    868    868    872    Ljava/lang/Exception;
        //  866    868    982    983    Any
        //  884    907    951    957    Ljava/lang/Exception;
        //  884    907    947    951    Any
        //  907    920    951    957    Ljava/lang/Exception;
        //  907    920    947    951    Any
        //  934    947    951    957    Ljava/lang/Exception;
        //  934    947    947    951    Any
        //  964    969    982    983    Any
        //  971    977    982    983    Any
        //  979    982    982    983    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0825:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Thread.java:748)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public byte[] getValidTokenAsByteArray(final Client client, final Context context) throws Exception {
        final long n = System.currentTimeMillis() / 1000L;
        final long longValue = 60L;
        if (client != null && client.getEncryptPin() != null) {
            Label_0147: {
                if (client.getOAuth2AccessToken(client.getEncryptPin()) != null && client.getOAuth2AccessTokenExpiration() != 0) {
                    if (client.getOAuth2AccessTokenExpiration() >= n + longValue) {
                        break Label_0147;
                    }
                }
                try {
                    final tokenHelper requestAccessToken = this.requestAccessToken();
                    if (requestAccessToken == null || requestAccessToken.expiration == null || requestAccessToken.value == null) {
                        throw new RuntimeException("Token request failed.");
                    }
                    client.setOAuth2AccessToken(client.getEncryptPin(), requestAccessToken.value);
                    client.setOAuth2AccessTokenExpiration(requestAccessToken.expiration);
                    final ClientDBHandlerClass instance = ClientDBHandlerClass.getInstance(client.getEncryptPin(), context.getApplicationContext());
                    if (instance != null) {
                        instance.addOrUpdateClient(client);
                        instance.close();
                        return client.getOAuth2AccessToken(client.getEncryptPin());
                    }
                    throw new RuntimeException("Failed to connect to database");
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        throw new RuntimeException("Null client or null encryptPin");
    }
    
    private static class tokenHelper
    {
        Integer expiration;
        byte[] value;
        
        public tokenHelper(final Integer expiration, final byte[] value) {
            this.expiration = expiration;
            this.value = value;
        }
    }
}
