package ui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class ID3 {

	public ID3() {
		super();
	}
	
	
	public static LinkedList<LinkedList<String>> getExamplesTrain(String path) {
		LinkedList<String> features = new LinkedList<>();
		LinkedList<LinkedList<String>> examplesAll = new LinkedList<>();
		try (BufferedReader sc = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
			String a;
			int cnt = 0;
			LinkedList<String> example = new LinkedList<>();

			while ((a = sc.readLine()) != null) {
				a = a.trim();
				if (cnt == 0) {
					Collections.addAll(features, a.split(","));
				} else {
					Collections.addAll(example, a.split(","));
					examplesAll.add(new LinkedList<>(example));
					example.clear();
				}
				cnt++;
			}
			String y = features.getLast();
			features.removeLast();
			return examplesAll;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Node fit(String path, int debth) {
		LinkedList<String> features = new LinkedList<>();
		LinkedList<LinkedList<String>> examplesAll = new LinkedList<>();
		try (BufferedReader sc = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
			String a;
			int cnt = 0;
			LinkedList<String> example = new LinkedList<>();

			while ((a = sc.readLine()) != null) {
				a = a.trim();
				if (cnt == 0) {
					Collections.addAll(features, a.split(","));
				} else {
					Collections.addAll(example, a.split(","));
					examplesAll.add(new LinkedList<>(example));
					example.clear();
				}
				cnt++;
			}
			String y = features.getLast();
			features.removeLast();
			if(debth <= -1) {
			return (Node) id3(examplesAll, examplesAll, features, y, features);
			} else {
				return (Node) id3WithConstraint(examplesAll, examplesAll, features, y, features, debth);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static LinkedList<String> predictions(String path, TreePart tree, LinkedList<LinkedList<String>> examplesTrainAll) {
		LinkedList<String> features = new LinkedList<>();
		LinkedList<LinkedList<String>> examplesAll = new LinkedList<>();
		try (BufferedReader sc = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
			String a;
			int cnt = 0;
			LinkedList<String> example = new LinkedList<>();

			while ((a = sc.readLine()) != null) {
				a = a.trim();
				if (cnt == 0) {
					Collections.addAll(features, a.split(","));
				} else {
					Collections.addAll(example, a.split(","));
					examplesAll.add(new LinkedList<>(example));
					example.clear();
				}
				cnt++;
			}
			String y = features.getLast();
			features.removeLast();
			LinkedList<String> results = new LinkedList<>();
			for (LinkedList<String> example2 : examplesAll) {
				results.add(goThroughNode(tree, example2, features, examplesTrainAll));
			}
			return results;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static String goThroughNode(TreePart tree, LinkedList<String> example, LinkedList<String> features, LinkedList<LinkedList<String>> examplesTrainAll) {
		if (tree instanceof Node) {
			int index = findIndexOfFeature(((Node) tree).getFeature(), features);
			LinkedList<SubTree> help = new LinkedList<>();
			help.addAll(((Node) tree).getSubTree());
			for (SubTree subTree : help) {
				if (subTree.getfeatureEx().compareTo(example.get(index)) == 0) {
					return goThroughNode(subTree.getPart(), example, features, removeFeatures(examplesTrainAll, subTree.getfeatureEx(), index));
				}
			}
			return mostCommonLabel(examplesTrainAll);
			
		} else if (tree instanceof Leaf) {
			return ((Leaf) tree).getLabel();
		}
		return null;
	}

	public static double log2(double N) {

		double result = (Math.log(N) / Math.log(2));

		return result;
	}
	
	public static TreePart id3(LinkedList<LinkedList<String>> D, LinkedList<LinkedList<String>> Dp,
			LinkedList<String> X, String y, LinkedList<String> features) {
		String v = "", x = "";
		if (D.isEmpty()) {
			v = mostCommonLabel(Dp);
			return new Leaf(v);
		}
		v = mostCommonLabel(D);
		if (X.isEmpty() || sameLabel(v, D)) {
			return new Leaf(v);
		}
		x = biggestIG(D, X, features);
		LinkedList<SubTree> subtrees = new LinkedList<>();
		int index = findIndexOfFeature(x, features);
		Map<String, Integer> map = mapLabel(D, index);
		LinkedList<String> Xnew = new LinkedList<>();
		for (String s : X) {
			if (s.compareTo(x) != 0) {
				Xnew.add(s);
			}
		}
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			subtrees.add(
					new SubTree(entry.getKey(), id3(removeFeatures(D, entry.getKey(), index), D, Xnew, y, features)));
		}

		return new Node(subtrees, x);
	}
	
	public static TreePart id3WithConstraint(LinkedList<LinkedList<String>> D, LinkedList<LinkedList<String>> Dp,
			LinkedList<String> X, String y, LinkedList<String> features, int constraint) {
		
		String v = "", x = "";
		if(constraint == 0) {
			v = mostCommonLabel(D);
			return new Leaf(v);
		}
		if (D.isEmpty()) {
			v = mostCommonLabel(Dp);
			return new Leaf(v);
		}
		v = mostCommonLabel(D);
		if (X.isEmpty() || sameLabel(v, D)) {
			return new Leaf(v);
		}
		x = biggestIG(D, X, features);
		LinkedList<SubTree> subtrees = new LinkedList<>();
		int index = findIndexOfFeature(x, features);
		Map<String, Integer> map = mapLabel(D, index);
		LinkedList<String> Xnew = new LinkedList<>();
		for (String s : X) {
			if (s.compareTo(x) != 0) {
				Xnew.add(s);
			}
		}
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			subtrees.add(
					new SubTree(entry.getKey(), id3WithConstraint(removeFeatures(D, entry.getKey(), index), D, Xnew, y, features, constraint - 1)));
		}

		return new Node(subtrees, x);
	}
	
	public static Map<String, Integer> mapLabel(LinkedList<LinkedList<String>> D, int index) {
		Map<String, Integer> map = new HashMap<>();
		for (LinkedList<String> example : D) {
			Integer help = map.get(example.get(index));
			if (help == null) {
				map.put(example.get(index), 1);
			} else {
				map.put(example.get(index), help + 1);
			}
		}
		return map;
	}

	public static String mostCommonLabel(LinkedList<LinkedList<String>> D) {
		return mostCommonLabelOfIndex(D, D.get(0).size() - 1);
	}
	
	public static String mostCommonLabelOfIndex(LinkedList<LinkedList<String>> D, int index) {
		Map<String, Integer> map = mapLabel(D, index);
		int biggest = 0;
		String biggestName = "";
		int help = 0;
		String helpName = "";
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			help = entry.getValue();
			helpName = entry.getKey();
			if (help > biggest || (help == biggest && helpName.compareTo(biggestName) < 0)) {
				biggest = help;
				biggestName = helpName;
			}
		}
		return biggestName;
	}
	
	public static boolean sameLabel(String v, LinkedList<LinkedList<String>> D) {
		for (LinkedList<String> example : D) {
			if (example.getLast().compareTo(v) != 0) {
				return false;
			}
		}
		return true;

	}

	public static int findIndexOfFeature(String x, LinkedList<String> features) {
		int cnt = 0;
		for (String feature : features) {
			if (feature.compareTo(x) == 0) {
				return cnt;
			}
			cnt++;
		}

		return -1;
	}

	public static double entropy(LinkedList<LinkedList<String>> D) {
		Map<String, Integer> map = mapLabel(D, D.get(0).size() - 1);
		double sumAll = 0, sumEntropy = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			sumAll += entry.getValue();
		}
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			sumEntropy += ((entry.getValue() / sumAll) * log2((entry.getValue() / sumAll)));
		}
		return -sumEntropy;
	}

	public static LinkedList<LinkedList<String>> removeFeatures(LinkedList<LinkedList<String>> D, String feature,
			int index) {
		LinkedList<LinkedList<String>> help = new LinkedList<LinkedList<String>>();
		for (int i = 0; i < D.size(); i++) {
			if (D.get(i).get(index).compareTo(feature) == 0) {
				help.add(D.get(i));
			}
		}
		return help;
	}

	public static double IG(LinkedList<LinkedList<String>> D, String x, LinkedList<String> features) {
		double entropy = entropy(D);
		int index = findIndexOfFeature(x, features);
		Map<String, Integer> map = mapLabel(D, index);
		double sum = 0, sumAll = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			sumAll += entry.getValue();
		}
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			sum += ((entry.getValue() / sumAll) * entropy(removeFeatures(D, entry.getKey(), index)));
		}

		return entropy - sum;
	}

	public static String biggestIG(LinkedList<LinkedList<String>> D, LinkedList<String> X,
			LinkedList<String> features) {
		double biggest = 0;
		String biggestName = "";
		double help = 0;
		String helpName = "";
		for (int i = 0; i < X.size(); i++) {
			help = IG(D, X.get(i), features);
			helpName = X.get(i);
			System.out.println("IG(" + helpName + ")= " + help);
			if (help > biggest || (help == biggest && helpName.compareTo(biggestName) < 0) || biggestName.compareTo("") == 0) {
				biggest = help;
				biggestName = helpName;
			}
		}
		return biggestName;
	}




}
