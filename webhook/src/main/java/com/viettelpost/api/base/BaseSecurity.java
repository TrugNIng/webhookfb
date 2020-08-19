package com.viettelpost.api.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.viettelpost.api.business.models.UserToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;

public class BaseSecurity {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    final static ObjectMapper mapper = new ObjectMapper();
//    final static String rootFolder = "/home/trungdq10/vtpplus/";
    final static String rootFolder = "G:\\Project VTP\\allproject\\vtpplus\\";

    public static String md5(String input) throws Exception {
        return DigestUtils.md5Hex(input.getBytes(StandardCharsets.UTF_8));
    }

    public static UserToken validateToken(String token) throws Exception {
        ECPublicKey publicKey = getPublicKey(rootFolder + "KeyPair/vtp.private");
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new ECDSAVerifier(publicKey);
        UserToken response = null;
        if (signedJWT.verify(verifier)) {
            JWTClaimsSet set = signedJWT.getJWTClaimsSet();
            String phone = set.getSubject();
            Long userId = (Long) set.getClaim(Constants.userId);
            Long partner = (Long) set.getClaim(Constants.partner);
            String sToken = (String) set.getClaim(Constants.token);
            Integer from = 0;
            if (set.getClaim(Constants.from) != null) {
                from = Integer.valueOf(set.getClaim(Constants.from).toString());
            }
            Long expired = set.getExpirationTime().getTime();
            response = new UserToken(userId, sToken, partner, phone, from == null ? 0 : from);
            response.setExpired(expired);
            response.setEncrypted(token);
        }
        if (response != null && response.getToken() != null && !response.getToken().isEmpty() && response.getUserId() != null) {
            long sys = new Date().getTime();
            if (response.getExpired() - sys > 0) {
                return response;
            }
        }
        return null;
    }

    public static String getToken(UserToken userToken) {
        try {
            ECPrivateKey privateKey = getPrivateKey(rootFolder + "KeyPair/vtp.private");
            JWSSigner signer = new ECDSASigner(privateKey);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userToken.getPhone())
                    .expirationTime(new Date(userToken.getExpired()))
                    .claim(Constants.userId, userToken.getUserId())
                    .claim(Constants.partner, userToken.getPartner())
                    .claim(Constants.token, userToken.getToken())
                    .claim(Constants.from, userToken.getSource())
                    .build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.ES256), claimsSet);

            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ECPublicKey getPublicKey(String fileName) throws Exception {
        PEMParser pemParser = null;
        try {
            pemParser = new PEMParser(new InputStreamReader(new FileInputStream(fileName)));
            PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            KeyPair keyPair = converter.getKeyPair(pemKeyPair);
            pemParser.close();

            return (ECPublicKey) keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pemParser != null) {
                pemParser.close();
            }
        }
        return null;
    }

    public static ECPrivateKey getPrivateKey(String fileName) throws Exception {
        PEMParser pemParser = null;
        try {
            pemParser = new PEMParser(new InputStreamReader(new FileInputStream(fileName)));
            PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            KeyPair keyPair = converter.getKeyPair(pemKeyPair);
            pemParser.close();

            return (ECPrivateKey) keyPair.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pemParser != null) {
                pemParser.close();
            }
        }
        return null;
    }

    private static void generateKey(String header, String privateName, String publicName) throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair pair = gen.genKeyPair();

        PublicKey publicKey = pair.getPublic();
        File f = new File("KeyPair/" + header + "/" + publicName);
        f.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(publicKey.getEncoded());
        fos.flush();
        fos.close();

        PrivateKey privateKey = pair.getPrivate();
        File f1 = new File("KeyPair/" + header + "/" + privateName);
        f1.getParentFile().mkdirs();
        FileOutputStream fos1 = new FileOutputStream(f1);
        fos1.write(privateKey.getEncoded());
        fos1.flush();
        fos1.close();

    }


    public static void allowAllSSL() throws Exception {
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
        javax.net.ssl.SSLContext context = null;
        TrustManager[] trustManagers = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new _FakeX509TrustManager()};
        }

        try {
            context = javax.net.ssl.SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            throw e;
        } catch (KeyManagementException e) {
            throw e;
        }
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    private static class _FakeX509TrustManager implements javax.net.ssl.X509TrustManager {
        private final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
        }

        public boolean isClientTrusted(X509Certificate[] chain) {
            return (true);
        }

        public boolean isServerTrusted(X509Certificate[] chain) {
            return (true);
        }

        public X509Certificate[] getAcceptedIssuers() {
            return (_AcceptedIssuers);
        }
    }


    public static void main(String[] args) {
        try {
            getPrivateKey("E:/Work/Java/e-commerce/KeyPair/local.private");
//            generateKey("local", "local.private", "local.public");
            System.out.println(md5("Zk78@134@#"));
//            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//            encryptor.setAlgorithm("PBEWithMD5AndDES");
//            encryptor.setPassword("CNTT@1234897A#@");
//            String enc = encryptor.decrypt("r0nescWPuu0xhju2GzPJt1nW4rdoiuu33h9OOyGW2fC0hSHGu4QUdhJokw5OGrQwRPwLBIawjKno/WLr+y+VafVMMkC0FRLzauhZHMMwGzLl//TRuKIXseTineqiz8PrtV7kjO/uU8Cxf5lnQrwi/w52f0aj2GepCMV8Byc4kLNC7lz+Hidh8o/DYCz+U6FzHXCauS7ddOjymlRlVmjshAPWCEzwIDPQiPalwkXZmPnp2e6fbdwv19Kf8lIiMG5Dqyh03dFqa+c2/g/FAgCA0ApynH7SeKEw47j6R/wEkr6qDb/mXgLeywRJ9bPK3r/YId20KglrJxb3zDFOYOPpom4v9/eQ11Xr");
//            System.out.println(enc);

            UserToken userToken = new UserToken();
            userToken.setUserId(2083760L);
            userToken.setPartner(2083760L);
            userToken.setPhone("0347612033");
            userToken.setToken("56F40EEBC5BD5C0BA34A9841DD618093");
            userToken.setExpired(System.currentTimeMillis() + 1000L * 60 * 30);

            String s = getToken(userToken);
            System.out.println(s);


            String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOjUyNzIwNCwibmFtZSI6Ik5ndXnhu4VuIFRy4buNbmcgTHXDom4iLCJtYV9idXVjdWMiOiJDREciLCJpc3MiOiJldnRwIiwicm9sZXMiOlt7ImlzX3N1cGVyX3VzZXIiOmZhbHNlLCJyb2xlX2lkIjoxLCJyb2xlX25hbWUiOiJSZWdpc3RlcmVkIFVzZXJzIn0seyJpc19zdXBlcl91c2VyIjpmYWxzZSwicm9sZV9pZCI6MTY1LCJyb2xlX25hbWUiOiJWX1RDVF9DTlRUIn0seyJpc19zdXBlcl91c2VyIjpmYWxzZSwicm9sZV9pZCI6MTcyLCJyb2xlX25hbWUiOiJWX1RDVF9DU0tIIn1dLCJpYXQiOjE1Mzg3OTk0MTN9.cOaa6WmslkXgH1DDEE3_NYD6XshJYG1TU6k88ueVpSPWZ830an5g_LI7CoqcqRwZhScWYym4Xs-gIS0y9U15ZF4YoCx1pnAAdaiJSW5T2pD6HA252kxaJJKGx_qHZvZQPrEFiQFJxu_jKofvSbBQNXzoa_TILtVfQy4z6u2u8AGNGGc7sjtFOoY-j9pnCE1Z0Oft4MxYlHkXYNYHKdel3573QAeuWtIGP3nNQrvzSqZKxuEGXnxACbuSmXVbR493pDB3NpnIzMv1oLV2a8twurtGiUJsEReslzG9RMxf20zZA3_dWOa7r_nJx4YGlPp48zeQQ3w3yfZSQYnfu299Ig";
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWTClaimsSet set = signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
