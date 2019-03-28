package cn.cnbuilder.smallapp.utils;

import org.springframework.beans.factory.annotation.Value;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DESUtil {
    //密码，长度要是8的倍数    注意此处为简单密码  简单应用 要求不高时可用此密码
    private static String key;

    @Value("${des.key}")
    public void setKey(String key) {
        DESUtil.key = key;
    }

    /**
     * 加密
     *
     * @param datasource byte[]
     * @param key        String
     * @return byte[]
     */
    private static byte[] encryptByKey(byte[] datasource, String key) {
        try {
            SecureRandom random = new SecureRandom();

            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作 ecb模式下面的PKCS5Padding
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param data
     * @return
     * @throws Exception
     * @Method: encrypt
     * @Description: 加密数据然后转成Base64
     */
    public static String encrypt(String data) {  //对string进行BASE64Encoder转换
        byte[] bt = encryptByKey(data.getBytes(), key);
        BASE64Encoder base64en = new BASE64Encoder();
        String strs = new String(base64en.encode(bt));
        return strs;
    }

    /**
     * 解密
     *
     * @param src byte[]
     * @param key String
     * @return byte[]
     * @throws Exception
     */
    private static byte[] decrypt(byte[] src, String key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    /**
     * @param data
     * @return
     * @throws Exception
     * @Method: decryptor
     * @Description: 从Base64解密 在Des解密数据
     */
    public static String decryptor(String data) throws Exception {  //对string进行BASE64Encoder转换
        sun.misc.BASE64Decoder base64en = new sun.misc.BASE64Decoder();
        byte[] bt = decrypt(base64en.decodeBuffer(data), key);
        String strs = new String(bt);
        return strs;
    }

    //测试
    public static void main(String args[]) {
        //待加密内容
        String str = "a=a01&&u=sa0keweid&&f=F002&&k=C0283&&t=20180801134532";
        //加密串
        String result = DESUtil.encrypt(str);
        System.out.println("加密后：" + result);
        try {
            //直接将如上内容解密
            String decryResult = DESUtil.decryptor(result);
            System.out.println("解密后：" + new String(decryResult));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
