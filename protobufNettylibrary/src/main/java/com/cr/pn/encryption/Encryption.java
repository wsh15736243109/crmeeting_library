package com.cr.pn.encryption;

import com.cr.pn.encryption.AES.misc.BASE64Decoder;
import com.cr.pn.encryption.AES.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zy on 2018/7/31.
 */

public class Encryption {

    private byte[] VIPARA;

    private String salt = "A_EncryptDecrypt";

    public Encryption() {
        VIPARA = new byte[salt.length()];
        for (int i = 0; i < VIPARA.length; i++) {
            VIPARA[i] = 0;
        }
    }

    public Encryption(String salt) {
        this.salt = salt;
        VIPARA = new byte[salt.length()];
        for (int i = 0; i < VIPARA.length; i++) {
            VIPARA[i] = 0;
        }
    }

    /**
     * 加密
     *
     * @param content
     * @return
     */
    public String encrypt(String content) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA);
            SecretKeySpec key = new SecretKeySpec(salt.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] byte_encode = content.getBytes("utf-8");
            byte[] byte_AES = cipher.doFinal(byte_encode);
            String AES_encode = new String(new BASE64Encoder().encode(byte_AES));
            return AES_encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content
     * @return
     */
    public String decrypt(String content) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA);
            SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            return AES_decode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

}
