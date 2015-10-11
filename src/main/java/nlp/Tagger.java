package nlp;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class Tagger {
	
	private final Set<String> unwantedTags = new HashSet<String>(
			Arrays.asList("DT", "VBZ", "WRB", "VBP", "IN", "WDT", "TO", "EX", "CC", "VB", "MD"));
	
	public static Map<String, String> getTags(String input) throws IOException {
		POSModel model = new POSModelLoader().load(new File("./src/resources/en-pos-maxent.bin"));
		POSTaggerME tagger = new POSTaggerME(model);
		
		ObjectStream<String> lineStream = new PlainTextByLineStream(new StringReader(input));
		
		String line;
		Map<String, String> tagMap = new HashMap<String, String>();
		while ((line = lineStream.read()) != null) {
	 
			String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
					.tokenize(line);
			String[] tags = tagger.tag(whitespaceTokenizerLine);
			
			//Turn the two arrays into a map
			
			for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
				tagMap.put(whitespaceTokenizerLine[i], tags[i]);
			}
			
//			This is for debugging purposes.
//			for (Map.Entry<String, String> entry : tagMap.entrySet())
//			{
//			    System.out.println(entry.getKey() + "/" + entry.getValue());
//			}
			
			
		}
		
		lineStream.close();
		
		return tagMap;
	}
}
