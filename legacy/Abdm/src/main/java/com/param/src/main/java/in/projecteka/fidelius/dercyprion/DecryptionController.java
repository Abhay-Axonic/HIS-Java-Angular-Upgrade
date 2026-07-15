package com.param.src.main.java.in.projecteka.fidelius.dercyprion;

import java.security.Security;
import java.util.Arrays;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.mgrm.fidelius.Utils;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.Arrays;



public class DecryptionController {

private static final Logger logger = Logger.getLogger(DecryptionController.class);

static {
    try {
        Security.addProvider(new BouncyCastleProvider());
    } catch (Exception e) {
        logger.error(e.getMessage(), e);
        throw new ServiceException(e.getMessage());
    }
}

public DecryptionResponse decrypt(DecryptionRequest decryptionRequest) throws Exception {
    byte[] xorOfNonces = Utils.calculateXorOfBytes(
            Utils.decodeBase64ToBytes(decryptionRequest.getSenderNonce()),
            Utils.decodeBase64ToBytes(decryptionRequest.getRequesterNonce())
    );
    byte[] iv = Arrays.copyOfRange(xorOfNonces, xorOfNonces.length - 12, xorOfNonces.length);
    byte[] salt = Arrays.copyOfRange(xorOfNonces, 0, 20);

    String decryptedData = decrypt(iv, salt, decryptionRequest.getRequesterPrivateKey(),
            decryptionRequest.getSenderPublicKey(), decryptionRequest.getEncryptedData());

    return new DecryptionResponse(decryptedData);
}

private String decrypt(byte[] iv, byte[] salt, String requesterPrivateKey,
                       String senderPublicKey, String encryptedDataAsBase64Str) throws Exception {
    String sharedSecret = Utils.computeSharedSecret(requesterPrivateKey, senderPublicKey);
    byte[] aesEncryptionKey = Utils.sha256Hkdf(salt, sharedSecret, 32);

    String decryptedData = "";
    try {
        byte[] encryptedBytes = Utils.decodeBase64ToBytes(encryptedDataAsBase64Str);

        GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
        AEADParameters parameters = new AEADParameters(new KeyParameter(aesEncryptionKey), 128, iv, null);

        cipher.init(false, parameters);  // 'false' for decryption

        byte[] cipherBytes = new byte[cipher.getOutputSize(encryptedBytes.length)];
        int encryptedBytesLength = cipher.processBytes(encryptedBytes, 0, encryptedBytes.length, cipherBytes, 0);

        // DoFinal - where mac check occurs
        try {
            cipher.doFinal(cipherBytes, encryptedBytesLength);
        } catch (InvalidCipherTextException e) {
            // Catch specific InvalidCipherTextException for MAC failure
            System.err.println("Decryption failed: MAC check in GCM failed.");
            e.printStackTrace();
            throw new Exception("MAC check failed during decryption. Data integrity might have been compromised.");
        }

        decryptedData = new String(cipherBytes).trim();  // Ensure proper string conversion
    } catch (InvalidCipherTextException e) {
        System.err.println("Decryption error due to ciphertext issues: " + e.getMessage());
        e.printStackTrace();
        throw e;
    } catch (Exception e) {
        System.err.println("General decryption error: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }

    return decryptedData;
}}