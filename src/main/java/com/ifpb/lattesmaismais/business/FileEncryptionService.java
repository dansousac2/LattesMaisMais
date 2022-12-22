package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.DecryptionException;
import com.ifpb.lattesmaismais.presentation.exception.EncryptionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Service
public class FileEncryptionService {

    private static final String encryptionPassword = "thisisonlyatest";

    public byte[] encryptData(byte[] data) throws EncryptionException {

        try {
            // Gerando o iv:
            byte[] iv = generateIV();

            // Gerando a secret key (senha + iv):
            SecretKey secretKey = getSecretKey(encryptionPassword, iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

            // Encriptando dados
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            byte[] encryptedData = cipher.doFinal(data);

            // Concatenando dados criptografados com o iv (será necessário para a hora de descriptografar)
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + encryptedData.length);
            // Armazenando tamanho de iv (para validação na hora de descriptografar)
            byteBuffer.putInt(iv.length);
            byteBuffer.put(iv);
            byteBuffer.put(encryptedData);

            // Retornando resultado
            return byteBuffer.array();
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }

    }

    public byte[] decryptData(byte[] encryptedData) throws DecryptionException, IllegalArgumentException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);

        // Validando se os dados estão criptografados em AES:
        int ivSize = byteBuffer.getInt();
        if (ivSize != 12) {
            throw new IllegalArgumentException("Tamanho de IV incorreto, verifique se os dados estão criptografados em AES/GCM");
        }

        // Recuperando o valor do iv:
        byte[] iv = new byte[ivSize];
        byteBuffer.get(iv);

        try {
            // Gerando a secret key (senha + iv):
            SecretKey secretKey = getSecretKey(encryptionPassword, iv);

            // Recuperando o restante dos dados:
            byte[] remainingData = new byte[byteBuffer.remaining()];
            byteBuffer.get(remainingData);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

            // Descriptografando os dados:
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            return cipher.doFinal(remainingData);

        } catch (Exception e) {
            throw new DecryptionException(e.getMessage());
        }
    }

    public SecretKey getSecretKey(String password, byte [] iv) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Gerando uma chave de 128 bits
        KeySpec spec = new PBEKeySpec(password.toCharArray(), iv, 65536, 128);

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, "AES");
    }

    public byte[] generateIV () {
        SecureRandom secureRandom = new SecureRandom();

        // Gerando um iv (vetor de inicialização) de 12 bytes
        byte[] iv = new byte[12];

        // Preenchendo vetor com valores aleatórios (recomendado para o modo GCM)
        secureRandom.nextBytes(iv);

        return iv;
    }
}
