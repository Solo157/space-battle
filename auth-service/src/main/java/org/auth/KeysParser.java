package org.auth;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

public class KeysParser {

    public static RSAPrivateKey getPrivateKeyFromString(String keyStr) {
        try {
            // Удаляем заголовки и переносы строк
            String privateKeyPEM = keyStr;
            // Декодируем Base64 в байты
            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

            // Создаём спецификацию ключа
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

            // Получаем фабрику для RSA
            KeyFactory kf = KeyFactory.getInstance("RSA");

            // Генерируем приватный ключ
            PrivateKey privateKey = kf.generatePrivate(keySpec);

            return (RSAPrivateKey) privateKey;
        } catch (Exception e) {
            throw new RuntimeException("Не удалось преобразовать ключ", e);
        }
    }

}
