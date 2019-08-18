package com.sbsft.wslapi.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CipherUtil {

    private static byte[] ivBytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static String binToHex(byte[] value) {
        return String.format("%0" + value.length * 2 + 'x', new BigInteger(1, value));
    }

    private static byte[] generateRawKey(String key) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes("UTF-8"));
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256, random);
        return generator.generateKey().getEncoded();
    }

    public static String aesEncode(String value, String key, boolean isGenerate) {
        String result = null;
        if (!StringUtil.isEmpty(value) && !StringUtil.isEmpty(key)) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                if (isGenerate) {
                    cipher.init(1, new SecretKeySpec(generateRawKey(key), "AES"));
                } else {
                    cipher.init(1, new SecretKeySpec(key.getBytes(), "AES"), new IvParameterSpec(ivBytes));
                }

                byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
                result = Base64.getEncoder().encodeToString(encrypted);
            } catch (Exception var6) {
                //logger.error(var6.getMessage(), var6);
            }

            return result;
        } else {
            return result;
        }
    }

    public static String e1Encode(String value) {
        if (value == null) {
            return null;
        } else {
            String result = null;
            String randomText = "DRTR";
            String saltText = "eclaptIMe";

            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashedBytes = digest.digest((randomText + saltText).getBytes());
                String key = binToHex(hashedBytes).substring(0, 32);
                result = aesEncode(value, key, false).replace('/', '_').replace('+', '-');
                result = result.substring(0, 2) + randomText + result.substring(2);
                result = result.replace("\r\n", "").replace("\n", "");
                result = URLEncoder.encode(result, "UTF-8");
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException var7) {
                //logger.error(var7.getMessage(), var7);
            }

            return result;
        }
    }

    public static String e1Decode(String value) {
        if (value == null) {
            return null;
        } else {
            String result = null;

            try {
                result = URLDecoder.decode(value, "UTF-8");
                String randomText = result.substring(2, 6);
                String saltText = "eclaptIMe";
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashedBytes = digest.digest((randomText + saltText).getBytes());
                String key = binToHex(hashedBytes).substring(0, 32);
                result = result.substring(0, 2) + result.substring(6);
                result = result.replace('_', '/').replace('-', '+');
                result = aesDecode(result, key, false);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException var7) {
                //logger.error(var7.getMessage(), var7);
            }

            return result;
        }
    }

    public static String aesDecode(String value, String key) {
        return aesDecode(value, key, false);
    }

    public static String aesDecode(String value, String key, boolean isGenerate) {
        String result = null;
        if (!StringUtil.isEmpty(value) && !StringUtil.isEmpty(key)) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                if (isGenerate) {
                    cipher.init(2, new SecretKeySpec(generateRawKey(key), "AES"));
                } else {
                    cipher.init(2, new SecretKeySpec(key.getBytes("UTF-8"), "AES"), new IvParameterSpec(ivBytes));
                }

                byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(value));
                result = new String(decrypted, "UTF-8");
            } catch (Exception var6) {
                //logger.error(var6.getMessage(), var6);
            }

            return result;
        } else {
            return result;
        }
    }
}
