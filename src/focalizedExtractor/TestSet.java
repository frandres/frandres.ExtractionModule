package focalizedExtractor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestSet {
	
	private int numFields;
	private List<ExtractionContext> testCases;
	
	

	public TestSet(List<ExtractionContext> testCases) {
		super();
		
		this.testCases = testCases;
		
		if (testCases!= null && testCases.size()>0){
			this.numFields = testCases.get(0).getFieldsInformation().size();
		} else{
			System.out.println("Lista vacia");
		}
		
	}
	
	
	public int getNumFields() {
		return numFields;
	}


	public List<ExtractionContext> getTestCases() {
		return testCases;
	}


	private int getContextNum(int num){
		return num/numFields;
	}
	
	private int getFieldNum(int num){
		return num%numFields;
	}
	
	public ExtractionContext getContext(int num){
		
		List<FieldInformation> fInfo = new ArrayList<FieldInformation>(getTestCases().get(getContextNum(num)).getFieldsInformation());
		
		fInfo.remove(getFieldNum(num));
		
		return new ExtractionContext(fInfo);
	}
	
	public String getMissingFieldName(int num){
		return getTestCases().get(getContextNum(num)).getFieldsInformation().get(getFieldNum(num)).getFieldName();
	}
	
	public String getCorrectAnswer(int num){
		return getTestCases().get(getContextNum(num)).getFieldsInformation().get(getFieldNum(num)).getFieldValue();
	}


	public int getNumTests() {
		return numFields*testCases.size();
	}
	
}
