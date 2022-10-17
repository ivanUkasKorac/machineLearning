package ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class Solution {

	
	public static void printTree(TreePart tree, int debth, String before) {
		String help2 = "";
		if (tree instanceof Node) {
			LinkedList<SubTree> help = new LinkedList<>();
			help.addAll(((Node) tree).getSubTree());
			for (SubTree subTree : help) {
				help2 = before + debth + ":" + ((Node) tree).getFeature() + "=" + subTree.getfeatureEx() + " ";
				if (subTree.getPart() instanceof Leaf) {
					System.out.print(
							before + debth + ":" + ((Node) tree).getFeature() + "=" + subTree.getfeatureEx() + " ");
				}
				printTree(subTree.getPart(), debth + 1, help2);

			}
		} else if (tree instanceof Leaf) {
			System.out.println(((Leaf) tree).getLabel());
		}
	}
	
	public static double accuracy(LinkedList<LinkedList<String>> examplesTestAll, LinkedList<String> predictions) {
		double cnt = 0;
		for(int i = 0; i < examplesTestAll.size(); i++) {
			if(examplesTestAll.get(i).getLast().compareTo(predictions.get(i)) == 0) {
				cnt++;
			}
		}
		return cnt / examplesTestAll.size();
	}
	
	public static Map<String, Integer> accuracyMatrix(LinkedList<LinkedList<String>> examplesTestAll, LinkedList<String> predictions, Map<String, Integer> map3) {
		Map<String, Integer> result = new HashMap<>(map3);
		for(int i = 0; i < examplesTestAll.size(); i++) {
			int help = result.get(examplesTestAll.get(i).getLast() + predictions.get(i));
			result.put(examplesTestAll.get(i).getLast() + predictions.get(i), help + 1);
		}
		return result;
	}

	public static void main(String... args) {
		String pathTrain = "", pathTest = "";
		int debth = -1;
		if(args.length < 2) {
			System.out.println("Wrong number of arguments");
			return;
		}
		for (int i = 0; i < args.length; i++) {
			if (i == 0) {
				pathTrain = args[i];
			} else if (i == 1) {
				pathTest = args[i];
			} else {
				debth = Integer.parseInt(args[i]);
			}
		}
		

		ID3 model = new ID3();
		Node tree = model.fit(pathTrain, debth);
		LinkedList<LinkedList<String>> examplesTrainAll = new LinkedList<>(model.getExamplesTrain(pathTrain));
		LinkedList<LinkedList<String>> examplesTestAll = new LinkedList<>(model.getExamplesTrain(pathTest));
		System.out.println("[BRANCHES]:");
		printTree(tree, 1, "");
		LinkedList<String> predictions = model.predictions(pathTest, tree, examplesTrainAll);
		System.out.print("[PREDICTIONS]: ");
		for (String p : predictions) {
			System.out.print(p + " ");
		}
		System.out.println();
		
		System.out.print("[ACCURACY]: ");
		double acc = accuracy(examplesTestAll, predictions);
		System.out.printf("%s %n", String.format(Locale.ROOT, "%.5f", acc));
		
		Map<String, Integer> map1 = new TreeMap<>(ID3.mapLabel(examplesTestAll, examplesTestAll.get(0).size() - 1));
		Map<String, Integer> map2 = new TreeMap<>(ID3.mapLabel(examplesTestAll, examplesTestAll.get(0).size() - 1));
		Map<String, Integer> map3 = new TreeMap<>();

		for (Map.Entry<String, Integer> entry1 : map1.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : map2.entrySet()) {
				map3.put(entry1.getKey() + entry2.getKey(), 0);
			}
		}
		Map<String, Integer> matrixFinal = accuracyMatrix(examplesTestAll, predictions, map3);
		System.out.println("[CONFUSION_MATRIX]:");
		for (Map.Entry<String, Integer> entry1 : map1.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : map2.entrySet()) {
				System.out.print(matrixFinal.get(entry1.getKey() + entry2.getKey()) + " ");
			}
			System.out.println();
		}
		

	}

}
