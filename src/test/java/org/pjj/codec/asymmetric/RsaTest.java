package org.pjj.codec.asymmetric;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

/**
 * @author PengJiaJun
 * @Date 2022/09/09 11:05
 */
public class RsaTest {

    private static final String ALGORITHM = "RSA";
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static String publicKeyPath;
    private static String privateKeyPath;
    /**
     * rsa单次最大加密的明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * rsa单次最大解密的密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    static {
        publicKeyPath = RsaTest.class.getClassLoader().getResource("rsa.pub").getPath();
        privateKeyPath = RsaTest.class.getClassLoader().getResource("rsa.pri").getPath();
    }

    @Test
    public void testWriteKey2File() throws NoSuchAlgorithmException, IOException {
        writeKey2File();
    }

    /**
     * 测试使用
     *      公钥加密 -- 私钥解密
     *      私钥加密 -- 公钥解密
     */
    @Test
    public void testRsa() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String str = "今天是2022年9月9日";
        // 公钥加密 -- 私钥解密
        String encryptStr = encrypt(str, getPublicKey());//公钥加密
        String decryptStr = decrypt(encryptStr, getPrivateKey());//私钥解密
        System.out.println("公钥加密结果: " + encryptStr);
        System.out.println("私钥解密结果: " + decryptStr);

        System.out.println("\n======================================================================\n");
        // 私钥加密 -- 公钥解密
        encryptStr = encrypt(str, getPrivateKey());//私钥加密
        decryptStr = decrypt(encryptStr, getPublicKey());//公钥解密
        System.out.println("私钥加密结果: " + encryptStr);
        System.out.println("公钥解密结果: " + decryptStr);

    }

    /**
     * 加密
     *
     * @param originalCont 原始内容
     * @param key 公钥 或 私钥 (PublicKey 与 PrivateKey 都是继承至 Key, 所以这里接收 Key 就可以同时接收 公钥 或 私钥)
     * @return base64编码后的加密内容
     */
    private String encrypt(String originalCont, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = doCodec(cipher, originalCont.getBytes(UTF8), MAX_ENCRYPT_BLOCK);
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 解密
     * @param encryptedStr 加密后的内容
     * @param key 公钥 或 私钥
     * @return 返回原始内容
     */
    private String decrypt(String encryptedStr, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] decodeBase64 = Base64.decodeBase64(encryptedStr);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = doCodec(cipher, decodeBase64, MAX_DECRYPT_BLOCK);
        return new String(decryptedBytes, UTF8);
    }

    /**
     * 执行加密 或者 解密
     * @param cipher Cipher.ENCRYPT_MODE为加密模式, Cipher.DECRYPT_MODE为解密模式
     * @param bytes
     * @param maxBlockSize
     * @return
     */
    private byte[] doCodec(Cipher cipher, byte[] bytes, int maxBlockSize) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int inputLen = bytes.length;// 最开始表示 数组长度, 之后表示 数组剩余长度
        int offset = 0;//起始位置, 从 0 开始, 之后就是 offset = i * maxBlockSize; i 表示第几段, offset的变化为 0 ->128 ->256
        byte[] cache;
        int i = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 循环分段处理
        while((inputLen - offset) > 0) {
            if((inputLen - offset) > maxBlockSize) {
                // 第三个参数是要处理多长
                cache = cipher.doFinal(bytes, offset, maxBlockSize);
            }else {
                // 该 else 处理, 最后一段, 即最后一段不满128的长度, 即用 inputLen - offset 表示最后的剩余长度
                cache = cipher.doFinal(bytes, offset, inputLen - offset);
            }
            // 把当前cache的内容存起来
            bos.write(cache, 0, cache.length);

            i++;
            offset = i * maxBlockSize;// 下一段
        }

        // 加密或者解密的结果
        byte[] codecBytes = bos.toByteArray();

        bos.close();
        return codecBytes;
    }

    /**
     * 从生成好的公钥文件rsa.pub(经过base64编码后存储的)中 获取公钥
     * @return
     */
    private PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyBase64Str = FileUtils.readFileToString(new File(publicKeyPath), UTF8);
        byte[] decodeBase64 = Base64.decodeBase64(publicKeyBase64Str);

        // 公钥的规则就是 x509
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decodeBase64);

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    /**
     * 从生成好的私钥文件rsa.pri(经过base64编码后存储的)中 获取私钥
     * @return
     */
    private PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyBase64Str = FileUtils.readFileToString(new File(privateKeyPath), UTF8);
        byte[] decodeBase64 = Base64.decodeBase64(privateKeyBase64Str);

        // 私钥的规则就是 PKCS8
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decodeBase64);

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
     * 生成经过 base64编码后的密钥对(公钥 / 私钥) 并存储在文件中
     * 密钥对每次执行生成的都是不一样的, 所以执行一次之后保存起来, 其他时候就不需要执行这个了
     * (不知为何生成的文件不在 resources 中 而是在 target/test-classes/中)
     */
    private void writeKey2File() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(1024);
        // 通过 KeyPair 生成器生成KeyPair
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        // 把对象转为字节数组
        byte[] publicKeyEncoded = publicKey.getEncoded();
        // 使用base64编码
        String publicKeyBase64Str = Base64.encodeBase64String(publicKeyEncoded);

        // 与公钥相同方式获取私钥
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] privateKeyEncoded = privateKey.getEncoded();
        String privateKeyBase64Str = Base64.encodeBase64String(privateKeyEncoded);

        // 分别把公钥字符串 与 私钥字符串 写入文件
        FileUtils.writeStringToFile(new File(publicKeyPath), publicKeyBase64Str, UTF8);
        FileUtils.writeStringToFile(new File(privateKeyPath), privateKeyBase64Str, UTF8);
    }

}
