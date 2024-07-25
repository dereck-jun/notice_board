package com.fastcampus.boardserver.utils;

import com.fastcampus.boardserver.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.fastcampus.boardserver.exception.BaseResponseStatus.*;

@Slf4j
public class SHA256 {
    private static final String ENCRYPTION_KEY = "SHA-256";

    public static String encrypt(String str) {
        String SHA = null;

        MessageDigest md;

        try {
            md = MessageDigest.getInstance(ENCRYPTION_KEY);
            md.update(str.getBytes());

            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();

            for (byte byteData: bytes) {
                sb.append(Integer.toString((byteData & 0xff) + 0x100, 16).substring(1));
            }

            SHA = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException: ", e);
            SHA = null;
            throw new BaseException(SHA256_NO_SUCH_SPEC);
        }
        return SHA;
    }
}
