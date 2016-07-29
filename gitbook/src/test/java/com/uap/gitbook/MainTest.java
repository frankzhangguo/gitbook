/**
 * 
 */
package com.uap.gitbook;

import java.nio.file.Paths;

/**
 * @author Frank
 * 
 */
public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		String p =	Paths.get("D:\\GIT\\iuap-content").resolve("..\\ss\\new.txt").getParent().toString();
			System.out.println(p);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
