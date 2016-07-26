/**
 * 
 */
package com.uap.gitbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.markdown4j.Markdown4jProcessor;

import info.monitorenter.cpdetector.CharsetPrinter;

/**
 * @author Frank
 * 
 */
public class HtmlTools implements ConstantInterface {

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static java.util.List<String> findAllFiles(String path, String subfix) {
		java.util.List<String> list = new ArrayList<>();
		File file = new File(path);
		File[] files = file.listFiles();

		if (files == null) {
			return null;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				java.util.List<String> subList = findAllFiles(files[i].getAbsolutePath(), subfix);
				if (subList != null && subList.size() > 0) {
					for (String string : subList) {

						list.add(string);
					}
				}
			} else {
				String filePath = files[i].getAbsolutePath().toLowerCase();
				if (filePath.endsWith(subfix)) {
					list.add(filePath);
				}
			}
		}
		return list;

	}

	public static boolean needToCopy(String path) {

		// 表达式对象
		Pattern p = Pattern.compile("\\\\\\.[\\w]*");
		// 创建 Matcher 对象
		Matcher m = p.matcher(path);
		// 是否匹配
		if (m.find()) {// .开头 如：.git
			return false;
		}

		// 表达式对象
		p = Pattern.compile("\\.md\\b", Pattern.CASE_INSENSITIVE);
		// 创建 Matcher 对象
		m = p.matcher(path);
		// 是否匹配
		if (m.find()) {// .md 结尾
			return false;
		}
		return true;
	}

	public static void copyAllFiles(String path, String distpath) throws IOException {
		path = path.toLowerCase();
		distpath = distpath.toLowerCase();
		File file = new File(path);
		File[] files = file.listFiles();

		if (files == null) {
			return;
		}
		for (int i = 0; i < files.length; i++) {

			if (needToCopy(files[i].getAbsolutePath())) {
				if (files[i].isDirectory()) {
					copyAllFiles(files[i].getAbsolutePath(), distpath);

				} else {
					String filePath = files[i].getAbsolutePath().toLowerCase();

					String distFile = filePath;
					distFile = distFile.replace(distpath, distpath + ConstantInterface.HTML);
					FileUtils.copyFile(new File(filePath), new File(distFile));
				}
			}

		}

	}

	public static String formatDoc(Document doc) {

		Elements links = doc.getElementsByTag("pre");
		boolean prewithcode = false;
		for (int i = 0; i < links.size(); i++) {
			Element element = links.get(i);

			Elements codes = element.getElementsByTag("code");
			if (codes.size() > 0) {
				prewithcode = true;
				for (Element code : codes) {
					code.addClass("brush:java");
				}

			}

		}

		// md文档中存在 img 标签
		links = doc.getElementsByTag("img");

		for (int i = 0; i < links.size(); i++) {
			Element element = links.get(i);
			String piclink = element.attr("src").trim();

			if (piclink.startsWith("/")) {
				piclink = piclink.substring(1);
			} else if (piclink.startsWith("./")) {
				piclink = piclink.substring(2);
			}
			element.attr("src", piclink);

		}

		String out = doc.toString();
		if (prewithcode) {

			String regex = "(?<=\\<|\\<\\/)pre(?=\\>)";

			out = out.replaceAll(regex, "div");

		}
		return out;
	}

	public static String getFileContent(String filePath) throws FileNotFoundException {
		if (filePath != null) {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new FileNotFoundException(filePath);
			}

			try {
				return FileUtils.readFileToString(file, ENCODING);
			} catch (IOException e) {
				System.err.println(String.format("Error parsing file: %s", filePath));
			}
		}
		return "";
	}

	/**
	 * Converts the file in HTML depending on the options sent in parameter.
	 * 
	 * @param args
	 *            an ArgsParser object containing the program's options.
	 * @throws FileNotFoundException
	 *             if the file which should be converted doesn't exist.
	 */
	public static List<String> process(ArgsParser args) throws FileNotFoundException {

		if (args.getMarkdownFile() == null) {
			return null;
		}
		File file = new File(args.getMarkdownFile());
		if (!file.exists()) {
			System.err.println("java.io.FileNotFoundException:" + args.getMarkdownFile());
			// throw new FileNotFoundException();
			return null;
		}
		List<String> list = new ArrayList<String>();

		try {
			String fileContent = FileUtils.readFileToString(file, ENCODING);
			fileContent = delHTMLStyle(fileContent);

			Markdown4jProcessor processor = new Markdown4jProcessor();
			String html = processor.process(fileContent);
			// System.out.println("文件名称：" + args.getMarkdownFile() + " 字数：" +
			// fileContent.length());

			// html = getFileContent(args.getHeaderFile()) + html +
			// getFileContent(args.getFooterFile());

			html = getFileContent(ConstantInterface.HEADER) + html + getFileContent(ConstantInterface.FOOTER);

			Document doc = Jsoup.parse(html);

			/*
			 * if (args.getTocTag() != null) { Elements links =
			 * doc.getElementsByTag(args.getTocTag()); for (int i = 0; i <
			 * links.size(); i++) { Element element = links.get(i);
			 * element.attr("id", "toc" + i); list.add(element.text());
			 * System.out.println(element.text()); } }
			 */

			ArrayList<Toc2Link> arrayList = generateID(doc);

			String nav = getAnchorsNavbar(arrayList);
			// System.out.println(nav);

			doc.body().append(nav);

			String htmlContent = formatDoc(doc);

			if (args.getOutputFile() == null) {
				// Display to console
				// System.out.println(htmlContent);
			} else {
				// Save to file
				File output = new File(args.getOutputFile());
				FileUtils.write(output, htmlContent, ENCODING);

			}
		} catch (IOException e) {
			System.err.println("ERROR");
			e.printStackTrace();
		}
		return list;
	}

	public static String delHTMLStyle(String htmlStr) {
		String regEx_style = "\\s+style\\s*=\\s*(\"([^\"]|\\\\\")+\"|'[^']+')";
		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // ����style��ǩ
		return htmlStr.trim(); // �����ı��ַ�
	}

	public static ArrayList<Toc2Link> generateID(Document document) {
		Elements links = document.select("h1, h2, h3");

		ArrayList<Toc2Link> toc2LinkList = new ArrayList<Toc2Link>();
		Toc2Link currentLink = null;
		for (int i = 0; i < links.size(); i++) {
			Element element = links.get(i);
			element.attr("id", element.text());

			if (element.tagName().equals("h1")) {
				currentLink = new Toc2Link();

				currentLink.setName(element.text());
				currentLink.setUrl(element.attr("id"));
				currentLink.setIndex("1");

				toc2LinkList.add(currentLink);

			} else if (element.tagName().equals("h2")) {

				Toc2Link link = new Toc2Link();
				link.setName(element.text());
				link.setUrl(element.attr("id"));
				link.setIndex("2");

				if (link.getIntIndex() > currentLink.getIntIndex()) {
					currentLink.getChildren().add(link);
					link.setFather(currentLink);
				} else {
					Toc2Link fatherLink = currentLink.getFather();
					while (fatherLink.getIntIndex() >= link.getIntIndex()) {
						fatherLink = fatherLink.getFather();
					}
					fatherLink.getChildren().add(link);
					link.setFather(fatherLink);
				}

				currentLink = link;

			} else if (element.tagName().equals("h3")) {
				Toc2Link link = new Toc2Link();
				link.setName(element.text());
				link.setUrl(element.attr("id"));
				link.setIndex("3");

				if (link.getIntIndex() > currentLink.getIntIndex()) {
					currentLink.getChildren().add(link);
					link.setFather(currentLink);

				} else {
					Toc2Link fatherLink = currentLink.getFather();
					while (fatherLink.getIntIndex() >= link.getIntIndex()) {
						fatherLink = fatherLink.getFather();
					}
					fatherLink.getChildren().add(link);
					link.setFather(fatherLink);

				}
				currentLink = link;
			}

			// System.out.println(element.text());
		}

		return toc2LinkList;
	}

	public static String toc2HTML(ArgsParser p, Item item, List<String> tocs) {

		StringBuffer li = new StringBuffer("");
		if (tocs != null && tocs.size() > 0) {
			li.append("\r\n");
			li.append("<ul class='sub'> ");
			li.append("\r\n");
			for (String toc : tocs) {
				li.append("<li><a bookref='" + item.getUrl() + "#toc" + tocs.indexOf(toc) + "'>" + toc + "</a></li>");
				li.append("\r\n");
			}
			li.append(" </ul>");
			li.append("\r\n");
		}
		return li.toString();
	}

	public static void toHTMLFile(List<Node> nodes, Menu menu, String indexName) throws IOException {
		if (nodes.size() <= 0) {
			return;
		}
		StringBuffer html = new StringBuffer("");
		if (nodes.size() > 0) {
			html.append("<ul> ");
			html.append("\r\n");
			for (Node item : nodes) {
				html.append(item.process());
			}
			html.append("<li><a href='/webpage/developer/ieop/views/docIndex.html#/pic/webpage/developer/ieop/doc/quickdev/startup_setup.html'>开发平台历史版本</a></li>");

			html.append(" </ul>");
			html.append("\r\n");
		}
		// Save to file

		Document doc = Jsoup.parse(getFileContent(TEMPLATE));
		doc.getElementById("nava-name").html(menu.getName());
		doc.getElementById("nava-title").html(menu.getName());

		// System.out.println(html.toString());
		doc.getElementById("left-menu").html(html.toString());

		if(indexName != null && indexName.length()>0){
			File output = new File(menu.getBasedir() + ConstantInterface.HTML + indexName);
			FileUtils.write(output, doc.toString(), ENCODING);
		}
		

	}

	/**
	 * 
	 * @param links
	 * @return
	 */

	public static String getAnchorsNavbar(ArrayList<Toc2Link> links) {
		if (links != null && links.size() > 0) {

			Toc2Link[] array = links.toArray(new Toc2Link[links.size()]);

			String html = "<div id='anchors-navbar'><i class='fa fa-anchor'></i><ul>";

			for (int i = 0; i < array.length; i++) {
				html += "<li><a href='#" + array[i].url + "'>" + array[i].name + "</a></li>";
				if (array[i].children.size() > 0) {
					html += "<ul>";
					for (int j = 0; j < array[i].children.size(); j++) {
						html += "<li><a href='#" + array[i].children.get(j).url + "'>" + array[i].children.get(j).name + "</a></li>";
						if (array[i].children.get(j).children.size() > 0) {
							html += "<ul>";
							for (int k = 0; k < array[i].children.get(j).children.size(); k++) {
								html += "<li><a href='#" + array[i].children.get(j).children.get(k).url + "'>" + array[i].children.get(j).children.get(k).name + "</a></li>";
							}
							html += "</ul>";
						}
					}
					html += "</ul>";
				}
			}

			html += "</ul></div>"
			// + "<a href='#" + array[0].url +
			// "' id='goTop'><i class='fa fa-arrow-up'></i></a>"

			;

			return html;

		} else {
			return "";
		}

	}

	public static String guessEncoding(String filename) {
		try {
			CharsetPrinter charsetPrinter = new CharsetPrinter();
			String encode = charsetPrinter.guessEncoding(new File(filename));
			return encode;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void checkEncode(String path) throws Exception {
		List<String> filePathList = findAllFiles(path, ".md");
		for (String filePath : filePathList) {
			String encode = guessEncoding(filePath);
			if (!encode.equalsIgnoreCase("UTF-8")) {
				if (avaiable(filePath)) {
					System.err.println("文件编码错误, 文件编码为" + encode + "非UTF-8：   " + filePath);
				}
			}
		}
	}

	public static boolean avaiable(String filePath) throws Exception {
		FileInputStream fin = new FileInputStream(filePath);
		int size = fin.available();

		fin.close();

		if (size == 0) {
			return false;
		} else {
			return true;
		}
	}

}
