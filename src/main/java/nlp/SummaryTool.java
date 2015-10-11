package nlp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Sets;

public class SummaryTool {
	
	public static String[] getTopStrings(String input, int returnNumber){
		return getTopStrings(splitContentToSentences(input), returnNumber);
	}
	
	public static String[] getTopStrings(String[] input, int returnNumber) {
		int numberOfSentencesReturned = returnNumber;
		Map<String, Double> sentences = new HashMap<String,Double>();
		String [] result = new String[numberOfSentencesReturned];

		for(String str : input){
			sentences.put(str, 0.0);
		}
		
		Double intersectionValues[][] = new Double[input.length][input.length];
		
		// Calculate the intersection of sentences 
		for(int i = 0; i < input.length; ++i) {
			for(int j = 0; j < input.length; ++j) {
				intersectionValues[i][j] = normalizeIntersection(input[i], input[j]);
			}
		}
		
		// Build dictionary and create score which is the sum of intersection
		for(int i = 0; i < input.length; i++) {
			double score = 0;
			for(int j = 0; j < input.length; j++) {
				if (i == j){
					continue;
				}
				score += intersectionValues[i][j];
			}
			sentences.put(input[i], score);
		}
		
		// Sort by highest score
		Set<Entry<String,Double>> set = sentences.entrySet();
		List<Entry<String,Double>> list = new ArrayList<Entry<String, Double>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>(){

			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				// TODO Auto-generated method stub
				return (o2.getValue()).compareTo(o1.getValue());
			}
			
		});
		
		// Get top results
		int returnCount = 0;
		for(Map.Entry<String, Double> entry:list){
			result[returnCount] = entry.getKey();
			
			returnCount++;
			
			if(returnCount > numberOfSentencesReturned - 1){
				break;
			}
		}
		
		return result;
	}
	
	private static String[] splitContentToSentences(String input) {
		input = input.replace("\n\n", ". ");
		input = input.replace("\n", ". ");
		input = input.replace("!", ".");
		
		return input.split("(?<=[a-z])\\.\\s+");
	}
	
	private static double normalizeIntersection(String sent1, String sent2){
		Set<String> s1 = new HashSet<String>();
		Set<String> s2 = new HashSet<String>();
		Set<String> intersect = new HashSet<String>(s1);
		
		for (String word : sent1.split(" ")){
			s1.add(word);
		}
		for(String word : sent2.split(" ")){
			s2.add(word);
		}
		
		intersect = Sets.intersection(s1, s2);
		
		if(intersect.size() == 0){
			return 0;
		}
		
		return (double)intersect.size() / (((double) s1.size() + (double) s2.size()) / 2);
	}
	
	public static List<Map.Entry<String, Integer>> getWordFreq (String input) {
		Map<String, Integer> wordFreq = new HashMap<String,Integer>();
		
		input = input.replace("!", "");
		input = input.replace("?", "");
		input = input.replace(".", "");
		input = input.replace(",", "");
		input = input.replace(":", "");
		input = input.replace(";", "");
		input = input.replace("|", "");
		
		for(String str : input.split(" ")) {
			if(wordFreq.keySet().contains(str.toLowerCase())){
				wordFreq.replace(str.toLowerCase(), wordFreq.get(str.toLowerCase()) + 1);
			} else {
				wordFreq.put(str.toLowerCase(), 1);
			}
		}
		
		Set<Entry<String,Integer>> set = wordFreq.entrySet();
		List<Entry<String,Integer>> list = new ArrayList<Entry<String, Integer>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>(){

			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		
		return list;
	}
}
