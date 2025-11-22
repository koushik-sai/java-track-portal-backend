package com.javaportal.config;

import java.security.SecureRandom;

public class PasswordGenerator {
	private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWZYX";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL_CHARACTERS = "!@#$%^&*";

	private static final String ALL_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;

	private static final Integer PASSWORD_LENGTH = 16;
	
	private PasswordGenerator() {}

	public static String generateStrongPassword() {
		SecureRandom random = new SecureRandom();

		StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

		// Ensure at least one character from each category is present in the password
		password.append(randomCharFrom(LOWERCASE, random));
		password.append(randomCharFrom(UPPERCASE, random));
		password.append(randomCharFrom(DIGITS, random));
		password.append(randomCharFrom(SPECIAL_CHARACTERS, random));

		// Fill the remaining characters with a mix of all categories
		for (int i = password.length(); i < PASSWORD_LENGTH; i++) {
			password.append(randomCharFrom(ALL_CHARACTERS, random));
		}

		// Shuffle the characters to ensure randomness
		return shuffleString(password.toString(), random);
	}

	// Helper method to get a random character from a given string
	private static char randomCharFrom(String source, SecureRandom random) {
		int index = random.nextInt(source.length());
		return source.charAt(index);
	}

	// Shuffle the password string to ensure the characters are randomly distributed
	private static String shuffleString(String str, SecureRandom random) {
		char[] characters = str.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			int j = random.nextInt(characters.length);
			char temp = characters[i];
			characters[i] = characters[j];
			characters[j] = temp;
		}
		return new String(characters);
	}
}
