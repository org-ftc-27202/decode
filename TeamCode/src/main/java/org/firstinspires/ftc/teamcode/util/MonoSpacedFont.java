package org.firstinspires.ftc.teamcode.util;

// every 2 letters need 1 space to keep same spacing with █
public class MonoSpacedFont {
	public static int toMonospace(StringBuilder stringBuilder, String text) {
		if (text == null) return 0;
		StringBuilder sb = new StringBuilder();

		int amountConverted = 0;
		for (int i = 0; i < text.length(); i++) {
			int codePoint = text.codePointAt(i);

			if (codePoint >= 'A' && codePoint <= 'Z') {
				// A (65) -> 𝙰 (120432)
				sb.appendCodePoint(codePoint - 'A' + 0x1D670);
				amountConverted++;
			} else if (codePoint >= 'a' && codePoint <= 'z') {
				// a (97) -> 𝚊 (120458)
				sb.appendCodePoint(codePoint - 'a' + 0x1D68A);
				amountConverted++;
			} else if (codePoint >= '0' && codePoint <= '9') {
				// 0 (48) -> 𝟶 (120822)
				sb.appendCodePoint(codePoint - '0' + 0x1D7F6);
				amountConverted++;
			} else {
				sb.append((char) codePoint);
			}
		}

		String prefix = "";
		String suffix = "";

		if (amountConverted % 6 == 0) {
			prefix = "";
			suffix = "";
		} else if (amountConverted % 6 == 1) {
			prefix = "";
			suffix = "\u200A";
		} if (amountConverted % 6 == 2) {
			prefix = "\u200A";
			suffix = "\u200A";
		} if (amountConverted % 6 == 3) {
			prefix = "\u200A";
			suffix = " ";
		} if (amountConverted % 6 == 4) {
			prefix = " ";
			suffix = " ";
		} if (amountConverted % 6 == 5) {
			prefix = " ";
			suffix = " \u200A";
		}

		stringBuilder.append(prefix).append(sb).append(suffix);
		return text.length() - (amountConverted / 6);
	}
}
