package mosswithme;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import nlp.SummaryTool;
import GoogleTest.GoogleSearch;

@Path("/")
public class ApiRouter {
	
	@GET
	@Path("/query")
	@Produces("application/json")
	public Response getResults(@QueryParam("input") String input){
		if(input == null){
			input = "What+are+Hackathons";
		} else {
			input = input.replace(" ", "+");
		}
		
		String[] resultStrings = ApiGetBlurbs(input);
		
		String responseString = "{ \"query\":\"" + input + "\"";
		
		int resultCount = 0;
		for(String str : resultStrings){
			responseString += ",\"result" + resultCount +"\":\"" + str + "\"";
			resultCount++;
		}
		
		responseString += "}";

		
		return Response.status(200).entity(responseString).build();
	}

	private String[] ApiGetBlurbs(String input){
		
		String matcherInput = GoogleSearch.getMatcherInput(input);
		
		System.out.println(matcherInput);
      
		String[] result = SummaryTool.getTopStrings(matcherInput, 3);
      
		return result;
	}
	
}
