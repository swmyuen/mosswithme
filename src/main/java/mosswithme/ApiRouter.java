package mosswithme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import nlp.SummaryTool;
import GoogleTest.GoogleSearch;

@Path("/")
public class ApiRouter {
	
	@GET
	@Path("/query")
	@Produces("application/json")
	public Response getResults(@QueryParam("input") String input) throws IOException{
		if(input == null){
			input = "What+are+Hackathons";
		} else {
			input = input.replace(" ", "+");
		}
		
		String matcherInput = GoogleSearch.getMatcherInput(input);
	
		List<Map.Entry<String, Integer>> wordsToBold = SummaryTool.getWordsToBold(matcherInput);
		
		String[] resultStrings = SummaryTool.getTopStrings(matcherInput, 3);
		
		String[] optimizedResultStrings = optimizeResults(resultStrings, wordsToBold);
		
		String responseString = "{ \"query\":\"" + input + "\"";
		
		int resultCount = 0;
		for(String str : resultStrings){
			responseString += ",\"result" + resultCount +"\":\"" + str + "\"";
			resultCount++;
		}
		
		int boldCount = 0;
		
		responseString += ",\"boldWords\":[";
		int x = 0;
		for(Map.Entry<String, Integer> entry : wordsToBold){
			System.out.println(entry.getValue() + "\t\t" + entry.getKey());
			
			if(x == 0){
				responseString += "{\"boldWord" + boldCount +"\":\"" + entry.getKey() + "\"}";
				x++;
			} else {
				responseString += ",{\"boldWord" + boldCount +"\":\"" + entry.getKey() + "\"}";
			}
			boldCount++;
		}
		
		responseString += "]\n}";

		
		return Response.status(200).entity(responseString).build();
	}
	
	private String[] optimizeResults(String[] resultStrings, List<Map.Entry<String, Integer>> wordsToBold){
		String[] optResultString = new String[3];
		Map<String, Double> sentenceScore = new HashMap<String,Double>();
		Map<String, Integer> boldWords = new HashMap<String, Integer>();
		
		for(Map.Entry<String, Integer> word : wordsToBold){
			boldWords.put(word.getKey(), word.getValue());
		}
		
		for(String str : resultStrings){
			double sentScore = 0;
			String[] words = str.split(" ");
			
			for(String word : words){
				if(boldWords.keySet().contains(word)){
					sentScore += 1;
				}
			}
			
			sentScore = sentScore / words.length;
			sentenceScore.put(str, sentScore);
		}
		
		Set<Entry<String, Double>> scores = sentenceScore.entrySet();
		List<Entry<String,Double>> list = new ArrayList<Entry<String, Double>>(scores);
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>(){

			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				// TODO Auto-generated method stub
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		
		for(Entry<String,Double> entry : list){
			System.out.println("ASDF: " + entry.getValue() + "\t\t" + entry.getKey());
		}
		
		for(int i = 0; i < optResultString.length; i++){
			optResultString[i] = list.get(i).getKey();
		}
		
		return optResultString;
	}
	
}
