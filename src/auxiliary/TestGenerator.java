package auxiliary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import focalizedExtractor.ExtractionContext;
import focalizedExtractor.FieldInformation;

public class TestGenerator {

	static Logger log = Logger.getLogger(TestGenerator.class.getName());
	
	private String contentFilePath;
	private final static String delimiter = "---";
	private final static String fieldNameRegExp = "(.*)\\|.*";
	private final static String fieldValueRegExp = ".*\\|(.*)";
	
	public TestGenerator(String contentFilePath) {
		super();
		this.contentFilePath = contentFilePath;
	}

	public String getContentFilePath() {
		return contentFilePath;
	}

	public List<ExtractionContext> getFieldIinfos(){
	
		List<ExtractionContext> extractionContexts = new ArrayList<ExtractionContext>();
		List<FieldInformation>  fieldInformations  = new ArrayList<FieldInformation>();
		String thisLine;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(contentFilePath));
			while ((thisLine = br.readLine()) != null) { 
				if (thisLine.compareTo(delimiter)==0){
					extractionContexts.add(new ExtractionContext(fieldInformations));
					fieldInformations  = new ArrayList<FieldInformation>();
				} else{
					fieldInformations.add(parseLine(thisLine));
				}
			}  
		} catch (Exception e) {
			log.log(Level.ERROR, "Problem reading file:" +contentFilePath + " Error:" + e.getMessage());
		}
		
		return extractionContexts;
		
	}

	private FieldInformation parseLine(String thisLine) {
		
		String fieldName = thisLine;
		String fieldValue = thisLine;
		
		Pattern pattern = Pattern.compile(
	                fieldNameRegExp,
	                Pattern.MULTILINE
	    );

		Matcher matcher = pattern.matcher(thisLine);

		if (matcher.find()){

			try{
				fieldName = matcher.group(1);
			}catch (IllegalStateException e) {
				log.log(Level.ERROR, "Problema encontrando nombre de campo: " + thisLine + ", error:" + e.getMessage());
			}		
		}
		
		pattern = Pattern.compile(
                fieldValueRegExp,
                Pattern.MULTILINE
	    );
	
		matcher = pattern.matcher(thisLine);
	
		if (matcher.find()){
	
			try{
				fieldValue = matcher.group(1);
			}catch (IllegalStateException e) {
				log.log(Level.ERROR, "Problema encontrando valor de campo: " + thisLine + ", error:" + e.getMessage());
			}		
		}
		
		
		return new FieldInformation(fieldName, fieldValue);
	}
}
