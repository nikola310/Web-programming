package utils;

public class StringUtilities {

	public static boolean containsString(String original, String tobeChecked, boolean caseSensitive) {
		if (caseSensitive) {
			return original.contains(tobeChecked);

		} else {
			return original.toLowerCase().contains(tobeChecked.toLowerCase());
		}

	}

}
