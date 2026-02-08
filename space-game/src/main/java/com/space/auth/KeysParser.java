package com.space.auth;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class KeysParser {

    public static PublicKey getPublicKeyFromString(String key) {
        try {
        // убираем "ssh-rsa" и пробелы
        String publicKeyPEM = key
                .replace("ssh-rsa", "")
                .replaceAll("\\s+", "");

        // декодируем из Base64
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

        // В Java обычно публичные ключи в формате X.509
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

            KeyFactory kf = null;
            try {
                kf = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            return kf.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
