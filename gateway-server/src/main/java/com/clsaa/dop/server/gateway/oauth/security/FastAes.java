package com.clsaa.dop.server.gateway.oauth.security;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;

/**
 * AES算法类,内部会使用SHA-256加密key
 *
 * @author 任贵杰 812022339@qq.com
 * @summary AES算法类
 */
public final class FastAes {

	private static final String AES = "AES";
	private static final int IV_LEN = 16;

	private FastAes() {
		throw new UnsupportedOperationException();
	}

	/**
	 * AES128位加密,加密后内容会被BASE64URL编码
	 *
	 * @param AESKey  加密密匙
	 * @param content 加密内容
	 * @return {@link CryptoResult}对象,加密后内容会被BASE64URL编码
	 */
	public static CryptoResult encrypt(byte[] AESKey, String content) {
		try {
			//对AESKey做SHA-256加密
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			AESKey = Arrays.copyOfRange(sha.digest(AESKey),0,16);

			//根据SHA-256加密生成的字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(AESKey, AES);

			//根据指定算法AES生成密码器
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			//初始化密码器
			byte[] bytesIv = new byte[IV_LEN];
			SecureRandom ivRandom = new SecureRandom();
			ivRandom.nextBytes(bytesIv);
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(bytesIv));

			//获取加密内容的字节数组
			byte[] byteEncode = content.getBytes(StandardCharsets.UTF_8);

			//加密
			byte[] byteAES = cipher.doFinal(byteEncode);
			byte[] byteResult = Bytes.concat(bytesIv, byteAES);
			//将加密后的数据转换为字符串
			String AESEncode = BaseEncoding.base64Url().encode(byteResult);
			//将加密结果返回
			return new CryptoResult(CryptoResult.Status.OK, AESEncode);
		} catch (NoSuchAlgorithmException ignored) {
			return new CryptoResult(CryptoResult.Status.NoSuchAlgorithmException);
		} catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ignored) {
			return new CryptoResult(CryptoResult.Status.Exception);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException ignored) {
			return new CryptoResult(CryptoResult.Status.InvalidKeyException);
		}
	}

	/**
	 * AES128位解密,解密内容必须为BASE64URL编码
	 *
	 * @param AESKey  加密密匙
	 * @param content 加密内容
	 * @return {@link CryptoResult}
	 */
	public static CryptoResult decrypt(byte[] AESKey, String content) {
		try {
			//将加密并编码后的内容解码成字节数组,并获取
			byte[] byteContent = BaseEncoding.base64Url().decode(content);
			byte[] byteIv = Arrays.copyOfRange(byteContent, 0, IV_LEN);
			byte[] byteChiper = Arrays.copyOfRange(byteContent, IV_LEN, byteContent.length);

			//对AESKey做SHA-256加密
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			AESKey = Arrays.copyOfRange(sha.digest(AESKey),0,16);

			//根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(AESKey, AES);

			//根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			//初始化密码器
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(byteIv));
			//解密
			byte[] byteDecode = cipher.doFinal(byteChiper);
			String AESDecode = new String(byteDecode, StandardCharsets.UTF_8);
			//将加密结果返回
			return new CryptoResult(CryptoResult.Status.OK, AESDecode);
		} catch (NoSuchAlgorithmException ignored) {
			return new CryptoResult(CryptoResult.Status.NoSuchAlgorithmException);
		} catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException ignored) {
			return new CryptoResult(CryptoResult.Status.Exception);
		} catch (IllegalArgumentException | InvalidKeyException | InvalidAlgorithmParameterException ignored) {
			return new CryptoResult(CryptoResult.Status.InvalidKeyException);
		}
	}
}
