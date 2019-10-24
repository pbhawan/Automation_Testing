package com.viome.components;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacUtil {

    public static final String HMAC_ALGORITHM = "HmacSHA256";

    public static String calculateHMAC(String message, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
        hmac.init(key);
        return Base64.encodeBase64String(hmac.doFinal(message.getBytes()));
    }


    public static boolean checkHMAC(String message, String hmac, String secret) throws InvalidKeyException, NoSuchAlgorithmException {
        return hmac.equals(calculateHMAC(message, secret));
    }

}
