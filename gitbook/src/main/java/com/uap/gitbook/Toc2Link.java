/**
 * 
 */
package com.uap.gitbook;

import java.util.ArrayList;

/**
 * @author Frank
 * 
 */
public class Toc2Link {
	String name;
	String url;
	String index;

	public String getIndex() {
		return index;
	}

	public int getIntIndex() {
		return new Integer(getIndex());
	}

	public void setIndex(String index) {
		this.index = index;
	}

	ArrayList<Toc2Link> children = new ArrayList<Toc2Link>();
	Toc2Link father;

	public Toc2Link getFather() {
		return father;
	}

	public void setFather(Toc2Link father) {
		this.father = father;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ArrayList<Toc2Link> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Toc2Link> children) {
		this.children = children;
	}
}
