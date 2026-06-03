package com.itesm.infrastructure.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import com.itesm.domain.exceptions.EncryptionException;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.microprofile.config.ConfigProvider;

@Converter
@ApplicationScoped
public class EncryptorConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String ENCRYPTED_PREFIX = "ENC:";

    // tamaños estandar
    private static final int IV_LENGTH_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;

    private final SecureRandom secureRandom = new SecureRandom();

    private String getEncryptionKey() {
        String key = ConfigProvider.getConfig().getValue("decision360.db.encryption-key", String.class);
        return key != null ? key.trim() : null;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) return attribute;

        try {
            String encryptionKey = getEncryptionKey();
            byte[] keyBytes = encryptionKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            // Vector de inicialización aleatorio único para este registro
            byte[] iv = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] encryptedBytes = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));

            // juntar el IV y el texto cifrado en un solo arreglo para poder guardarlo en la BD
            byte[] combinedBytes = new byte[IV_LENGTH_BYTES + encryptedBytes.length];
            System.arraycopy(iv, 0, combinedBytes, 0, IV_LENGTH_BYTES);
            System.arraycopy(encryptedBytes, 0, combinedBytes, IV_LENGTH_BYTES, encryptedBytes.length);

            return ENCRYPTED_PREFIX + Base64.getEncoder().encodeToString(combinedBytes);
        } catch (Exception e) {
            throw new EncryptionException("Error al cifrar dato en BD", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || !dbData.startsWith(ENCRYPTED_PREFIX)) return dbData;

        try {
            String encryptionKey = getEncryptionKey();
            byte[] keyBytes = encryptionKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            String base64Content = dbData.substring(ENCRYPTED_PREFIX.length());
            byte[] combinedBytes = Base64.getDecoder().decode(base64Content);

            // separar el IV del texto cifrado
            byte[] iv = new byte[IV_LENGTH_BYTES];
            System.arraycopy(combinedBytes, 0, iv, 0, IV_LENGTH_BYTES);

            int encryptedSize = combinedBytes.length - IV_LENGTH_BYTES;
            byte[] encryptedBytes = new byte[encryptedSize];
            System.arraycopy(combinedBytes, IV_LENGTH_BYTES, encryptedBytes, 0, encryptedSize);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptionException("Error al descifrar dato de BD", e);
        }
    }
}
