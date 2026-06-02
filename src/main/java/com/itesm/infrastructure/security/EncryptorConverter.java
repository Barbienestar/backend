package com.itesm.infrastructure.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.eclipse.microprofile.config.ConfigProvider; // CAMBIO AQUÍ
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Converter
@ApplicationScoped
public class EncryptorConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String ENCRYPTED_PREFIX = "ENC:";

    private String getEncryptionKey() {
        String key = ConfigProvider.getConfig()
                .getValue("decision360.db.encryption-key", String.class);
        return key != null ? key.trim() : null;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) return attribute;

        try {
            String encryptionKey = getEncryptionKey();
            byte[] keyBytes = encryptionKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            return ENCRYPTED_PREFIX + Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar dato en BD", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return dbData;

        if (!dbData.startsWith(ENCRYPTED_PREFIX)) {
            return dbData;
        }

        try {
            String cleanData = dbData.substring(ENCRYPTED_PREFIX.length());

            String encryptionKey = getEncryptionKey();
            byte[] keyBytes = encryptionKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cleanData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar dato de BD", e);
        }
    }
}