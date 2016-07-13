package com.uap.gitbook;

/**
 * 
 * @author Frank
 * 
 */
public class ArgsParser {
	private String markdownFile = null;

	private String outputFile = null;

	private String headerFile = null;

	private String footerFile = null;
	private String tocTag = null;

	public String getFooterFile() {
		return footerFile;
	}

	public String getHeaderFile() {
		return headerFile;
	}

	public String getMarkdownFile() {
		return markdownFile;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public String getTocTag() {
		return tocTag;
	}

	public void setFooterFile(String footerFile) {
		this.footerFile = footerFile;
	}

	public void setHeaderFile(String headerFile) {
		this.headerFile = headerFile;
	}

	public void setMarkdownFile(String markdownFile) {
		this.markdownFile = markdownFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public void setTocTag(String tocTag) {
		this.tocTag = tocTag;
	}
}
