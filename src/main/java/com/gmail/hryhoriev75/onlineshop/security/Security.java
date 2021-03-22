package com.gmail.hryhoriev75.onlineshop.security;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.validator.routines.EmailValidator;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

public class Security {

    private static final int ITERATIONS = 200000;
    private static final int KEY_LENGTH = 512;
    private static final int SALT_LENGTH = 128;

    public static String hashPassword(final String password) throws Exception {
        byte[] salt = new byte[SALT_LENGTH / 8];
        (new SecureRandom()).nextBytes(salt);
        byte[] hashedBytes = hashPassword(password.toCharArray(), salt);
        return Hex.encodeHexString(hashedBytes) + Hex.encodeHexString(salt);
    }

    public static boolean isPasswordCorrect(final String password, final String passwordSaltHexed) throws Exception {
        if (password == null)
            return false;
        String passwordHexed = passwordSaltHexed.substring(0, KEY_LENGTH / 8 * 2);
        byte[] salt = Hex.decodeHex(passwordSaltHexed.substring(KEY_LENGTH / 8 * 2).toCharArray());
        byte[] hashedBytes = hashPassword(password.toCharArray(), salt);
        return Hex.encodeHexString(hashedBytes).equals(passwordHexed);
    }

    private static byte[] hashPassword(final char[] password, final byte[] salt) throws Exception {
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKey key = skf.generateSecret(spec);
        return key.getEncoded();
    }

    public static boolean isEmailValid(final String email)  {
        return email != null && EmailValidator.getInstance().isValid(email);
    }

    public static boolean isPasswordValid(final String password)  {
        return password != null && password.length() >= 8 && password.length() <= 64;
    }

}
