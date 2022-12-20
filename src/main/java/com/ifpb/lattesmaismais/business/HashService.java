package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.HashException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashService {
	public String hashingSHA256(String text) throws Exception {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

			messageDigest.reset();

			// Passando o valor desejado:
			messageDigest.update(text.getBytes("UTF-8"));

			// "Digerindo" o valor passado e gerando o hash:
			String hashText = String.format("%064x", new BigInteger(1, messageDigest.digest()));

			return hashText;

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new HashException("Ocorreu um problema durante a geração de hash: " + e.getMessage());
		}
	}
}
