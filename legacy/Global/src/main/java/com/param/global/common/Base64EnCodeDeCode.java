package com.param.global.common;

import org.apache.commons.codec.binary.Base64;

public class Base64EnCodeDeCode {

	/**
     * Encodes the byte array into base64 string
     *
     * @param imageByteArray - byte array
     * @return String a {@link java.lang.String}
     */
    public static byte[] encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64(imageByteArray);
    }
 
    /**
     * Decodes the base64 string into byte array
     *
     * @param imageDataString - a {@link java.lang.String}
     * @return byte array
     */
    public static byte[] decodeImage(byte[] imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }
}
