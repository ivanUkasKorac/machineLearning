package ui;

public class SubTree {
	String featureEx;
	TreePart part;

	public SubTree() {
		this.featureEx = null;
		this.part = null;
	}

	public SubTree(String featureEx, TreePart part) {
		this.featureEx = featureEx;
		this.part = part;
	} // constructor

	public String getfeatureEx() {
		return this.featureEx;
	}

	public TreePart getPart() {
		return this.part;
	}
}
