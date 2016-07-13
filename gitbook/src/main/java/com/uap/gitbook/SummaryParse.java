package com.uap.gitbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class SummaryParse {
	//
	List<Item> items = new ArrayList<Item>();

	public Menu parse(String summaryPath) throws IOException {

		ArrayList<Toc2Link> arrayList = new ArrayList<Toc2Link>();

		Toc2Link lastLink = null;

		for (String line : FileUtils.readLines(new File(summaryPath + "SUMMARY.MD"), ConstantInterface.ENCODING)) {

			String[] twoparts = line.split("\\*");
			if (twoparts.length >= 2) {
				Toc2Link toc = new Toc2Link();
				String index = twoparts[0];
				// System.out.println("index:" + index);

				String name = StringUtils.substringBetween(twoparts[1], "[", "]");

				// System.out.println("name:" + name);

				String link = StringUtils.substringBetween(twoparts[1], "(", ")");

				// System.out.println("link:" + link);

				if (name.length() == 0 || link.length() == 0) {
					continue;
				}
				toc.setIndex(index);
				toc.setName(name);
				toc.setUrl(link);

				if (lastLink == null) {
					arrayList.add(toc);
					lastLink = toc;
				} else {
					if (toc.getIndex().length() > lastLink.getIndex().length()) {
						toc.setFather(lastLink);
						lastLink.getChildren().add(toc);
						lastLink = toc;

					} else {
						while (lastLink.getIndex().length() >= toc.getIndex().length()) {
							lastLink = lastLink.getFather();
							if (lastLink == null) {
								break;
							}
						}
						if (lastLink == null) {
							arrayList.add(toc);
							lastLink = toc;
						} else {
							lastLink.getChildren().add(toc);
							toc.setFather(lastLink);
							lastLink = toc;
						}
					}
				}
			}
		}

		Menu menu = new Menu();
		menu.setName("开发文档");
		menu.setBasedir(summaryPath);
		createItemList(arrayList, "");
		menu.setItems(items);

		return menu;
	}

	public void createItemList(ArrayList<Toc2Link> arrayList, String index) {
		Integer id = 0;
		for (Toc2Link toc2Link : arrayList) {
			Item item = new Item();
			item.setShowname(toc2Link.getName());
			item.setMdfile(toc2Link.getUrl());
			id++;
			item.setId(index + (id) + "");

			items.add(item);

			createItemList(toc2Link.getChildren(), index + (id) + ".");

		}
	}

}
