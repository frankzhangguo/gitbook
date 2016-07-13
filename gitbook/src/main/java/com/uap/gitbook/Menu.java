
package com.uap.gitbook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Frank
 *
 */
@XmlRootElement(name = "menu")
public class Menu {
	String name;
	String basedir;
	List<Item> items;

	/**
	 * @return the name
	 */
	public String getName() {

		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the basedir
	 */
	public String getBasedir() {
		return basedir;
	}

	/**
	 * @param basedir
	 *            the basedir to set
	 */
	public void setBasedir(String basedir) {
		this.basedir = basedir;

	}

	/**
	 * @return the items
	 */
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	public List<Item> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}

}
