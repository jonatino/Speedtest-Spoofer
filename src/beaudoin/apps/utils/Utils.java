package beaudoin.apps.utils;

import java.security.MessageDigest;

public class Utils {

	public static String convertToMd5(final String md5) throws Exception {
		final java.security.MessageDigest md = MessageDigest.getInstance("MD5");
		final byte[] array = md.digest(md5.getBytes("UTF-8"));
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	public static int getCountOf(String text, String c) {
		return (text.length() - text.replace(c, "").length());
	}

}
