package com.uap.gitbook;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class TreeBuilder {

	public List<Node> buildListToTree(List<Item> items) {
		List<Node> dirs = new ArrayList<Node>();
		for (Item item : items) {

			String id = item.getId();

			int index = id.lastIndexOf(".");
			String parent = "0";
			;
			if (index > 0) {
				parent = id.substring(0, index);
			}

			Node node = new Node(item.getId(), parent, item);
			dirs.add(node);
		}

		List<Node> roots = findRoots(dirs);
		@SuppressWarnings("unchecked")
		List<Node> notRoots = (List<Node>) CollectionUtils.subtract(dirs, roots);
		for (Node root : roots) {
			root.setChildren(findChildren(root, notRoots));
		}
		return roots;
	}

	public List<Node> findRoots(List<Node> allNodes) {
		List<Node> results = new ArrayList<Node>();
		for (Node node : allNodes) {
			boolean isRoot = true;
			for (Node comparedOne : allNodes) {
				if (node.getParentId().equals(comparedOne.getId())) {
					isRoot = false;
					break;
				}
			}
			if (isRoot) {
				node.setLevel(0);
				results.add(node);
				node.setRootId(node.getId());
			}
		}
		return results;
	}

	private List<Node> findChildren(Node root, List<Node> allNodes) {
		List<Node> children = new ArrayList<Node>();

		for (Node comparedOne : allNodes) {
			if (comparedOne.getParentId().equals(root.getId())) {
				comparedOne.setParent(root);
				comparedOne.setLevel(root.getLevel() + 1);
				children.add(comparedOne);
			}
		}
		@SuppressWarnings("unchecked")
		List<Node> notChildren = (List<Node>) CollectionUtils.subtract(allNodes, children);
		for (Node child : children) {
			List<Node> tmpChildren = findChildren(child, notChildren);
			if (tmpChildren == null || tmpChildren.size() < 1) {
				child.setLeaf(true);
			} else {
				child.setLeaf(false);
			}
			child.setChildren(tmpChildren);
		}
		return children;
	}
}
