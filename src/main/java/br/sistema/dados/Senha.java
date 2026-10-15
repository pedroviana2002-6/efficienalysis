package br.sistema.dados;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public final class Senha {
	private static final int ITERACOES = 120_000;
	private static final int BYTES_SALT = 16;
	private static final int BITS_HASH = 256;
	private static final SecureRandom RANDOM = new SecureRandom();

	private Senha() {}

	public static String gerarSalt() {
		byte[] salt = new byte[BYTES_SALT];
		RANDOM.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	public static String hash(String senha, String saltBase64) {
		char[] chars = senha.toCharArray();
		try {
			byte[] salt = Base64.getDecoder().decode(saltBase64);
			PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERACOES, BITS_HASH);
			byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
					.generateSecret(spec).getEncoded();
			return Base64.getEncoder().encodeToString(hash);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("PBKDF2 indisponível nesta JVM", e);
		} finally {
			Arrays.fill(chars, '\0');
		}
	}

	public static boolean confere(String senha, String saltBase64, String hashEsperado) {
		if (senha == null || saltBase64 == null || hashEsperado == null) return false;
		byte[] calculado = hash(senha, saltBase64).getBytes(StandardCharsets.UTF_8);
		byte[] esperado = hashEsperado.getBytes(StandardCharsets.UTF_8);
		return MessageDigest.isEqual(calculado, esperado); 
	}
}
