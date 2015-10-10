package GoogleTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSearch {
	private static Pattern patternDomainName;
    private Matcher matcher;
    private static final String DOMAIN_NAME_PATTERN
            = "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
    static {
        patternDomainName = Pattern.compile(DOMAIN_NAME_PATTERN);
    }
    public static void main(String[] args) {
    	Set<String> results = new HashSet<String>();
	    String input = "Hackathons";
        GoogleSearch obj = new GoogleSearch();
        Set<String> urls = obj.getDataFromGoogle(input); 
        //result = obj.getSentencesFromLink("https://en.wikipedia.org/wiki/Hackathon%23Types_of_hackathons&sa=U&ved=0CC0Q0gIoAjADahUKEwirouOu57fIAhVD02MKHePFCW4&usg=AFQjCNEkdG2X3GGryEddMtMRBb0VQjCc3g");
        //for each of the urls, go and find all the sentences in that link 
        for(String url : urls) {
        	Set<String> sentences = new HashSet<String>();
			try {
				sentences = obj.getSentencesFromLink(url);
			} catch (MalformedURLException e) {
				continue;
			} catch (HttpStatusException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			}
            for (String sentence : sentences) {
            	results.add(sentence);
            }
        }
         
        //Output results
        obj.outputResults(results);
    }
    
    private Set<String> getDataFromGoogle(String query) {

        Set<String> result = new HashSet<String>();
        String request = "https://www.google.com/search?q=" + query + "&num=20";
        System.out.println("Sending request..." + request);

        try {
            // need http protocol, set this as a Google bot agent :)
            Document doc = Jsoup
                    .connect(request)
                    .userAgent(
                            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .timeout(5000).get();

            // get all links
    		Elements links = doc.select("a[href]");
    		for (Element link : links) {

    			String temp = link.attr("href");		
    			if(temp.startsWith("/url?q=")){
                //use regex to get domain name
    				result.add(temp.replace("/url?q=",""));
    			}

    		}

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    private Set<String> getSentencesFromLink(String request) throws HttpStatusException, MalformedURLException, IllegalArgumentException {
    	Set<String> result = new HashSet<String>();
        //System.out.println("Sending request..." + request);

            // need http protocol, set this as a Google bot agent :)
            Document doc;
			try {
				doc = Jsoup
				        .connect(request)
				        .userAgent(
				                "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
				        .timeout(5000).get();
			} catch (IOException e) {
				return result;
			}

            // get all links
            Elements links = doc.select("p");
            for (Element link : links) {
            	result.add(Jsoup.parse(link.toString()).text());
            }


        return result;
    }
    
    private void outputResults(Set<String> results) {
    	for (String result : results) {
    		System.out.println(result);
    	}
    }
}
