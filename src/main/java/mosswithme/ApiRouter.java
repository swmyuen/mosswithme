package mosswithme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
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
		
		for(Map.Entry<String, Integer> entry : wordsToBold){
			System.out.println(entry.getValue() + "\t\t" + entry.getKey());
		}
		
		String[] resultStrings = SummaryTool.getTopStrings(matcherInput, 3);
		
		String responseString = "{ \"query\":\"" + input + "\"";
		
		int resultCount = 0;
		for(String str : resultStrings){
			responseString += ",\"result" + resultCount +"\":\"" + str + "\"";
			resultCount++;
		}
		
		responseString += "}";

		
		return Response.status(200).entity(responseString).build();
	}
	
}
