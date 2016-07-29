/**
 * 
 */
package com.uap.gitbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author Frank
 * 
 */
public class MainPath {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String path = "D:\\workspace\\part1";
		try {
			/*
			 * Properties prop = new Properties(); FileInputStream fis = new
			 * FileInputStream("path.properties"); prop.load(fis);
			 * prop.list(System.out); System.out.println("\nThe foo property: "
			 * + prop.getProperty("path")); path = prop.getProperty("path");
			 */
			process(path);
			// reformat(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ����md�ļ�
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 */

	public static void process(String path) throws FileNotFoundException {

		List<String> files = HtmlTools.findAllFiles(path, ".md");
		for (String filePath : files) {
			ArgsParser p = new ArgsParser();

			p.setMarkdownFile(filePath);

			p.setOutputFile(filePath.toLowerCase().substring(0, filePath.lastIndexOf(".")) + ".html");

			p.setTocTag(ConstantInterface.TOCTAG);

			HtmlTools.process(p);
		}

	}

	/**
	 * ��html���и�ʽ���� ���ڴ������
	 * 
	 * @param path
	 * @throws IOException
	 */
	public static void reformat(String path) throws IOException {
		List<String> files = HtmlTools.findAllFiles(path, ".html");
		for (String filePath : files) {
			String html = HtmlTools.getFileContent(filePath);

			Document doc = Jsoup.parse(html);
			html = HtmlTools.formatDoc(doc, null);

			// Save to file
			File output = new File(filePath);
			FileUtils.write(output, html, ConstantInterface.ENCODING);
		}
	}

}
