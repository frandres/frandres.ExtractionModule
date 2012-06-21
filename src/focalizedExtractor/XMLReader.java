package focalizedExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import textPreProcessor.files.FileInformation;

public class XMLReader {

	//No generics
	//String filename;
	Document dom;
	
	static Logger log = Logger.getLogger(FocalizedExtractor.class.getName());
	
	public XMLReader(String fname){

		dom = parseXmlFile(fname);	
		
		if (dom == null){
			log.log(Level.ERROR, "Unable to read XML: " + fname);	
		}
	}

	/*
	 * Open the file and parse the XML File, returning a Documents with the parsed
	 * informatoin. 
	 */
	private Document parseXmlFile(String filename){

		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {
			
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(filename);
			
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {		
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}

		return dom; 
	}

	/*
	 * Given a document already ready to be parsed, iterates through
	 * the existing FieldDescriptors to return a list of those present
	 * in the XML. 
	 */
	public List<FieldDescriptor> getFDescriptors(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();
		
		List<FieldDescriptor> fDescriptors = new ArrayList<FieldDescriptor>();
		
		//get a nodelist of <FieldDescriptor> elements
		NodeList nl = docEle.getElementsByTagName("FieldDescriptor");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				
				//get the element
				Element el = (Element)nl.item(i);
				
				//get the FileSource object
				FieldDescriptor f = getFieldDescriptor(el);
				
				//add it to list
				fDescriptors.add(f);
			}
		}
		
		return fDescriptors;
	}


	/**
	 * Given the list of FileSources, extracts them and return
	 * a list of them. 
	 * @param filSEl
	 * @return
	 */
	private FieldDescriptor getFieldDescriptor(Element filSEl) {
		
		String fieldName = getTextValue(filSEl,"FieldName");
		List <String> specificRegExp = getSpecificRegExps(filSEl);
		double weight = Double.parseDouble(getTextValue(filSEl,"Weight"));
		String date = getTextValue(filSEl,"isDate");
		boolean isDate= (date!= null && date.compareTo("1")==0);
		
		return new FieldDescriptor(fieldName,
								   specificRegExp, 
								   weight,
								   isDate);
	}
	
	private List<String> getSpecificRegExps (Element filSEl){
		//SpecificRegExp
		
		List<String> regExps = new ArrayList<String>();
		
		//get a nodelist of <employee> elements\
		
		NodeList nl = filSEl.getElementsByTagName("SpecificRegExp");
		
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the employee element
				Element el = (Element)nl.item(i);

				//add it to list
				regExps.add(el.getTextContent());
			}
		}
		
		return regExps;
	}
	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is name I will return John  
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	public String getUnitRegExp() {
		Element docEle = dom.getDocumentElement();

		return getTextValue(docEle,"UnitRegExp");
	}

	public double getMinimumHitRatio() {
		Element docEle = dom.getDocumentElement();
		return Double.parseDouble(getTextValue(docEle,"MinimumHitRatio"));
	}

	public String getDocumentsFilePath() {
		Element docEle = dom.getDocumentElement();

		return getTextValue(docEle,"DocumentsFilePath");
	}

}
	