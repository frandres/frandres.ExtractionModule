package textPreProcessor;
import java.util.List;

import textPreProcessor.extractor.Extractor;
import textPreProcessor.files.FileSource;
import textPreProcessor.formatReaders.XMLReader;

public class TextProcessor {
	 
	public static void main(String[] args) {
		
		 if(args.length < 1) {
	        	System.out.println("usage: java TextProcessor <files>");
	        	System.exit(0);
	        }
		 
		 XMLReader xmlReader = new XMLReader(args[0]);		 
		 List<FileSource> fSources = xmlReader.getFSources();
		 
		 Extractor.processFiles(fSources);
		 
	}
}
