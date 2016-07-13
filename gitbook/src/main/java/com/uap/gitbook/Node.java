package com.uap.gitbook;

import java.io.IOException;
import java.util.List;

public class Node {
	private String id;
	private String parentId;
	private Node parent;
	private Item self;
	private List<Node> children;
	private String rootId;
	private int level;

	private boolean isLeaf;

	public Node(String id, String parentId, Item self) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.self = self;
	}

	/**
	 * @return the children
	 */
	public List<Node> getChildren() {
		return children;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @return the rootId
	 */
	public String getRootId() {
		return rootId;
	}

	/**
	 * @return the self
	 */
	public Item getSelf() {
		return self;
	}

	/**
	 * @return the isLeaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}

	public String process() throws IOException {
		StringBuffer li = new StringBuffer("");

		ArgsParser p = new ArgsParser();
		if (self.getMdfile() == null) {
			self.setMdfile(ConstantInterface.ONLY_PARENT);
			p.setMarkdownFile(null);

		} else {
			p.setMarkdownFile(self.getBasedir() + self.getMdfile());
			if (self.getUrl() == null) {
				String mdfile = self.getMdfile();
				self.setUrl(mdfile.toLowerCase().substring(0, mdfile.lastIndexOf(".")) + ".html");
			}

		}

		/*
		 * if (self.getUrl().lastIndexOf("/") <= 0) { } else {
		 * self.getUrl().substring(0, self.getUrl().lastIndexOf("/")) + "/"; }
		 */

		p.setOutputFile(self.getBasedir() + ConstantInterface.HTML + self.getUrl());

		p.setTocTag(ConstantInterface.TOCTAG);
		List<String> toc = null;

		String route = "#/" + ConstantInterface.direct_path + "/" + self.getUrl();
		if (ConstantInterface.WITHTOC) {
			if (self.getMdfile().equals(ConstantInterface.ONLY_PARENT)) {
				li.append("<li class='parent'><a><span class='open-sub'></span>" + self.getShowname() + "</a>");

			} else {
				if (getChildren().size() > 0) {
					li.append("<li class='parent'><a href='" + route + "'><span class='open-sub'></span>" + self.getShowname() + "</a>");
				} else {
					li.append("<li><a href='" + route + "'>" + self.getShowname() + "</a>");

				}

			}
			toc = HtmlTools.process(p);
		} else {
			if (getChildren().size() > 0) {
				if (self.getMdfile().equals(ConstantInterface.ONLY_PARENT)) {
					li.append("<li class='parent'><a>" + self.getShowname() + "</a>");

				} else {
					String css = "";
					if (getChildren().size() > 0) {
						css = "class='parent' ";
					}
					li.append("<li " + css + "><a href='" + route + "'><span class='open-sub'></span>" + self.getShowname() + "</a>");
				}

			} else {
				if (self.getMdfile().equals(ConstantInterface.ONLY_PARENT)) {
					li.append("<li><a>" + self.getShowname() + "</a>");

				} else {
					li.append("<li><a href='" + route + "'>" + self.getShowname() + "</a>");
				}

			}
			toc = HtmlTools.process(p);
			toc = null;
		}
		li.append(HtmlTools.toc2HTML(p, self, toc));

		if (getChildren().size() > 0) {
			li.append("\r\n");
			li.append("<ul class='sub'> ");
			li.append("\r\n");
			for (Node childNode : getChildren()) {
				li.append(childNode.process());
			}
			li.append(" </ul>");
			li.append("\r\n");
		}
		li.append("</li>");
		li.append("\r\n");

		return li.toString();
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<Node> children) {
		this.children = children;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param isLeaf
	 *            the isLeaf to set
	 */
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @param rootId
	 *            the rootId to set
	 */
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	/**
	 * @param self
	 *            the self to set
	 */
	public void setSelf(Item self) {
		this.self = self;
	}
}
