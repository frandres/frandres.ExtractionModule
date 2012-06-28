package auxiliary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import focalizedExtractor.ExtractionContext;
import focalizedExtractor.FieldInformation;
import focalizedExtractor.FocalizedExtractor;

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


	public void runTest(String configFilePath, int numField) {
		double  correctas[] = new double[getNumFields()];
		
		for (int i = 0; i < correctas.length; i++) {
			correctas[i]=0;
		}
		int numPruebas = getNumTests();
		double total = numPruebas;
		
		for (int testCaseNum = 0; testCaseNum < getNumTests(); testCaseNum++) {
			if (testCaseNum%getNumFields()!=numField){
				continue;
			}
			ExtractionContext extContext = getContext(testCaseNum);
			
			FocalizedExtractor fExt = new FocalizedExtractor(configFilePath, extContext);
			String result = fExt.findFieldValue(getMissingFieldName(testCaseNum));
			
			if (evaluateTest(getCorrectAnswer(testCaseNum),result)){
				System.out.println("Prueba número: " + testCaseNum + " correcta: " + getMissingFieldName(testCaseNum));
				correctas[testCaseNum%getNumFields()]++; 
			} else{
				System.out.println("Prueba número: " + testCaseNum + " incorrecta: " + getMissingFieldName(testCaseNum));
				System.out.println("Resultado correcto:" + getCorrectAnswer(testCaseNum));
				System.out.println("Resultado obtenido:" + result);
			}
			
			System.out.println("");
		}
		
	    System.out.println("RESULTS: ");
	    System.out.println("");
	    
	    for (int i = 0; i < correctas.length; i++) {
	    	System.out.println("Field: " + getMissingFieldName(i)) ;
	    	System.out.println(" Tasa de acierto: " + correctas[i]*getNumFields()*100/total);
	    	System.out.println("");
		}
		
		
	}
	
	private String replaceLastPunctuationSigns(String orig){
		if (orig.endsWith(",")){
			orig= orig.substring(0,orig.lastIndexOf(","));
		}
		
		if (orig.endsWith(".")){
			orig= orig.substring(0,orig.lastIndexOf("."));
		}
		
		return orig;
	}
	
	private boolean evaluateTest(String correct, String answer){

		if (answer == null){
			return correct.compareTo("")==0;
		}
		correct = correct.replace('“','"');
		answer = answer.replace('“','"');
		
		correct = correct.replace('”','"');
		answer = answer.replace('”','"');
		
		if (answer.endsWith(",")){
			answer= answer.substring(0,answer.lastIndexOf(","));
		}
		correct = correct.trim();
		answer = answer.trim();
		
		correct = replaceLastPunctuationSigns(correct);
		answer = replaceLastPunctuationSigns(answer);
		
		return (answer.compareTo(correct)==0); 
	}
	public void runTest(String configFilePath) {
		double  correctas[] = new double[getNumFields()];
		
		for (int i = 0; i < correctas.length; i++) {
			correctas[i]=0;
		}
		int numPruebas = getNumTests();
		double total = numPruebas;
		
		for (int testCaseNum = 0; testCaseNum < getNumTests(); testCaseNum++) {
			ExtractionContext extContext = getContext(testCaseNum);
			
			FocalizedExtractor fExt = new FocalizedExtractor(configFilePath, extContext);
			String result = fExt.findFieldValue(getMissingFieldName(testCaseNum));
			
			if (evaluateTest(getCorrectAnswer(testCaseNum),result)){
				System.out.println("Prueba número: " + testCaseNum + " correcta: " + getMissingFieldName(testCaseNum));
				correctas[testCaseNum%getNumFields()]++; 
			} else{
				System.out.println("Prueba número: " + testCaseNum + " incorrecta: " + getMissingFieldName(testCaseNum));
				System.out.println("Resultado correcto:" + getCorrectAnswer(testCaseNum));
				System.out.println("Resultado obtenido:" + result);
			}
			
			System.out.println("");
		}
		
	    System.out.println("RESULTS: ");
	    System.out.println("");
	    
	    for (int i = 0; i < correctas.length; i++) {
	    	System.out.println("Field: " + getMissingFieldName(i)) ;
	    	System.out.println(" Tasa de acierto: " + correctas[i]*getNumFields()*100/total);
	    	System.out.println("");
		}
		
		
	}
	
}
