package com.uap.gitbook;

import javax.xml.bind.annotation.XmlAttribute;

public class Item {
	String showname;
	String mdfile;
	String url;
	String id;
	String basedir;

	public String getBasedir() {
		return basedir;
	}

	/**
	 * @return the id
	 */
	@XmlAttribute(required = true)
	public String getId() {
		return id;
	}

	/**
	 * @return the mdfile
	 */
	public String getMdfile() {
		return mdfile;
	}

	/**
	 * @return the showname
	 */
	public String getShowname() {
		return showname;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param mdfile
	 *            the mdfile to set
	 */
	public void setMdfile(String mdfile) {
		this.mdfile = mdfile;
	}

	/**
	 * @param showname
	 *            the showname to set
	 */
	public void setShowname(String showname) {
		this.showname = showname;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
