package textPreProcessor.formatReaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class WordReader {
	 static Logger log = Logger.getLogger(WordReader.class.getName());  
	 
	/* Receive a filename with the complete path. Return the contents of the
	 * file
	 */
	public static String getTextContent (String filename){
		
		InputStream input = null;
		
		try {
			input = new FileInputStream(new File(filename));
		} catch (FileNotFoundException e) {
			log.log(Level.INFO,"Problem finding the file: " + filename);
			return null;
		}
		

		if (filename.matches(".*docx")){
			XWPFWordExtractor extractor = null;
			
			try {
				extractor = new XWPFWordExtractor(new XWPFDocument(input));
				input.close();
			} catch (IOException e) {
				log.log(Level.WARN,"Problem reading the file" + filename + ". Error: " + e.getMessage());
				return null;
			}
			
			return extractor.getText();
			
		} else{
			WordExtractor extractor =null;
			try {
				extractor = new WordExtractor(input);
				input.close();
			} catch (IOException e) {
				log.log(Level.WARN,"Problem reading the file" + filename + ". Error: " + e.getMessage());
				return null;
			}
			
			return extractor.getText();
		}
		
	}
}
