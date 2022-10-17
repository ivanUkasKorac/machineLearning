package ui;

import java.util.LinkedList;


public class Node extends TreePart{
	LinkedList<SubTree> tree;
	String feature;

	public Node(LinkedList<SubTree> tree, String feature) {
		super();
		this.tree = tree;
		this.feature = feature;
	} // constructor

	public String getFeature() {
		return this.feature;
	}

	public LinkedList<SubTree> getSubTree() {
		return this.tree;
	}
}
