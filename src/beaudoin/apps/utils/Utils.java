/**
 *  The MIT License (MIT)
 * 	
 *  Copyright (c) 2014 Jonathan Beaudoin
 *	
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *	
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *	
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 */
package beaudoin.apps.utils;

import java.security.MessageDigest;

/**
 * 
 * @author Jonathan
 * @since March 14th, 2014
 */
public class Utils {

	/**
	 * Used to convert a simple string to an MD5 hash. Web related scripts generally
	 * use MD5 hash(s).
	 * @param text
	 * @return the MD5 hash
	 * @throws Exception
	 */
	public static String convertToMd5(String text) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] data = md5.digest(text.getBytes("UTF-8"));
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; ++i) {
			sb.append(Integer.toHexString((data[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	/**
	 * Used to count a number of occurences of one characted in a string.
	 * @param text
	 * @param c
	 * @return the number of occurrences of the specified character
	 */
	public static int getCountOf(String text, String c) {
		return (text.length() - text.replace(c, "").length());
	}

}
