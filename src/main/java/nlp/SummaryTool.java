package nlp;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.Sets;

public class SummaryTool {
	
	public static String[] getTopStrings(String input){
		return getTopStrings(splitContentToSentences(input));
	}
	
	public static String[] getTopStrings(String[] input) {
		int numberOfSentencesReturned = 3;
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
		
		// Build dictionary and create score which is the sum of intersectiosn
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
		TreeMap<String, Double> sortedSentences = SortByValue((HashMap<String, Double>) sentences);
		
		int count = 0;
		for(Map.Entry<String, Double> entry : sortedSentences.entrySet()){
			if (count >= numberOfSentencesReturned){
				break;
			}
			
			result[count] = entry.getKey();
			count++;
		}
		
		return result;
	}
	
	private static String[] splitContentToSentences(String input) {
//		input = input.replace("\n\n", ". ");
//		input = input.replace("\n", ". ");

		
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
		
//		intersect.retainAll(s2);
		
		intersect = Sets.intersection(s1, s2);
		
		if(intersect.size() == 0){
			return 0;
		}
		
		return (double)intersect.size() / (((double) s1.size() + (double) s2.size()) / 2);
	}
	
	private static TreeMap<String, Double> SortByValue (HashMap<String, Double> map) {
		ValueComparator vc =  new ValueComparator(map);
		TreeMap<String,Double> sortedMap = new TreeMap<String,Double>(vc);
		sortedMap.putAll(map);
		return sortedMap;
	}
	
	private static class ValueComparator implements Comparator<String> {
		 
	    Map<String, Double> map;
	 
	    public ValueComparator(Map<String, Double> base) {
	        this.map = base;
	    }
	 
	    public int compare(String a, String b) {
	        if (map.get(a) >= map.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys 
	    }
	}
	

}
