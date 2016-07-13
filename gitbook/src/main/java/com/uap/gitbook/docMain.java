package com.uap.gitbook;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

public class docMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream("path.properties");

			prop.load(fis);
			prop.list(System.out);
			System.out.println("\nThe foo property: " + prop.getProperty("path"));
			String path = prop.getProperty("path");

			// FileUtils.deleteDirectory(new File(path +
			// ConstantInterface.HTML));

			SummaryParse parse = new SummaryParse();
			Menu menu = parse.parse(path);

			List<Item> items = menu.getItems();

			for (Item item : items) {
				item.setBasedir(menu.getBasedir());
			}

			List<Node> buildListToTree = new TreeBuilder().buildListToTree(menu.getItems());

			HtmlTools.toHTMLFile(buildListToTree, menu);
			// System.out.print(buildListToTree.toString());

			// 将图片等copy到HTML文件夹
			// HtmlTools.copy2HtmlDir(self.getBasedir(), path);
			//

			// copy all the files except md

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
