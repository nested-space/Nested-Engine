package com.edenrump.math.util;

public class ByteUtils {

    public static byte[] floatToByteArray(float value) {
        int intBits =  Float.floatToIntBits(value);
        return new byte[] {
                (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits) };
    }

    private static String byteArrayToString(byte[] arr){
        StringBuilder str = new StringBuilder();
        for (byte b : arr) {
            str.append(b).append(" ");
        }
        return str.toString().trim();
    }

}
