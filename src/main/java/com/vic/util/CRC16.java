package com.vic.util;

import java.nio.ByteBuffer;

public class CRC16 {
    private final static int[] POLY = new int[]{0, 0xa001};
    public static byte[] getResult(byte[] bytes) {
        int crc = 0xffff;
        for(int j = 0; j < bytes.length; ++j){
            int ds = bytes[j];
            for(int i = 0; i < 8; ++i){
                crc = (crc >>> 1) ^ POLY[(crc ^ ds) & 1];
            }
        }
        return MyUtil.intToUInt16Bytes(crc);
    }

    public static byte[] calculate(byte[] bytes){
        int crc = 0xFFFFFFFF;
        int mask = 0x000000FF;  // Important!!!, java 异或运算都会自动转换成int类型，所以如果为负数，则会扩展符号位
        for(int i = 0;i < bytes.length; ++i){
            crc ^= (bytes[i] & mask);
            for(int j = 0; j < 8;++j)
                crc = (crc >>> 1) ^ (((crc & 1) == 1) ? 0xedb88320 : 0);
        }
        return MyUtil.intToUInt16Bytes(crc);
    }
}
