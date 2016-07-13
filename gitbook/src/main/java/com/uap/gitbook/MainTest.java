/**
 * 
 */
package com.uap.gitbook;

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

			/*
			 * Menu menu = Xml2Java.xmltojava(Menu.class,
			 * "D:/webbase/iuap/menu.xml"); List<Item> items = menu.getItems();
			 * 
			 * for (Item item : items) { item.setBasedir(menu.getBasedir()); }
			 * 
			 * List<Node> buildListToTree = new
			 * TreeBuilder().buildListToTree(menu.getItems());
			 * 
			 * HtmlTools.toHTMLFile(buildListToTree, menu); //
			 * System.out.print(buildListToTree.toString());
			 */

			HtmlTools.copyAllFiles("D:/GIT/iuap-quickstart/", ".jpg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
