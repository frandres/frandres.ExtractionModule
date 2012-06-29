package auxiliary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import focalizedExtractor.ExtractionContext;
import focalizedExtractor.FieldInformation;
import focalizedExtractor.FocalizedExtractor;

public class TestSet {
	static Logger log = Logger.getLogger(TestSet.class.getName());
	private int numFields;
	private List<ExtractionContext> testCases;
	private double probability;
	private Random rGenerator;

	public TestSet(List<ExtractionContext> testCases, double probability) {
		super();
		
		this.testCases = testCases;
		this.probability = probability;
		rGenerator = new Random();
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
		
		for (Iterator <FieldInformation> iterator = fInfo.iterator(); iterator.hasNext();) {
			FieldInformation fieldInformation = (FieldInformation) iterator
					.next();
			if (!haveTheField()){
				iterator.remove();
			}
			
		}

		return new ExtractionContext(fInfo);
	}
	
	private boolean haveTheField(){
		return (rGenerator.nextDouble()>probability);
		 
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
		double  results[][] = new double[getNumFields()][4];
		
		for (int i = 0; i < getNumFields(); i++) {
			for (int j = 0; j < 4; j++) {
				results[i][j]=0;
			}
		}
		
		int numPruebas = getNumTests();
		double total = numPruebas;
		
		FocalizedExtractor fExt = new FocalizedExtractor();
		fExt.initialize(configFilePath);
		
		for (double minimumHitMeasure = 0.5; minimumHitMeasure <= 1; minimumHitMeasure+=.25) {
			fExt.setMinimumHitRadio(minimumHitMeasure);
			System.out.println("MINIMUM HIT MEASURE: " + minimumHitMeasure);
			for (int testCaseNum = 0; testCaseNum < getNumTests(); testCaseNum++) {
				if (testCaseNum%getNumFields()!=numField&&numField>=0){
					continue;
				}
				ExtractionContext extContext = getContext(testCaseNum);
				fExt.seteContext(extContext);
				
				String result = fExt.findFieldValue(getMissingFieldName(testCaseNum));
				
				int resultEvaluation = evaluateTest(getCorrectAnswer(testCaseNum),result);
				switch (resultEvaluation) {
				case CORRECT:
					log.log(Level.INFO,"Prueba número: " + testCaseNum + " correcta: " + getMissingFieldName(testCaseNum));
					break;
				
				case INCOMPLETE:
					log.log(Level.INFO,"Prueba número: " + testCaseNum + " incompleta: " + getMissingFieldName(testCaseNum));			
					break;
								
				case INCORRECT:
					log.log(Level.INFO,"Prueba número: " + testCaseNum + " incorrecta: " + getMissingFieldName(testCaseNum));
					break;
				case EXCESS:
					log.log(Level.INFO,"Prueba número: " + testCaseNum + " excess: " + getMissingFieldName(testCaseNum));
					break;
					
				default:
					break;
				}
				log.log(Level.INFO,"Resultado correcto:" + getCorrectAnswer(testCaseNum));
				log.log(Level.INFO,"Resultado obtenido:" + result);
				//System.out.println("");
				
				results[testCaseNum%getNumFields()][resultEvaluation]++;
							
			}
			
			System.out.println("RESULTS: ");
		    System.out.println("With probability of: " + probability);
		    
		    for (int i = 0; i < getNumFields(); i++) {
		    	System.out.println("Field: " + getMissingFieldName(i)) ;
		    	System.out.println(" CORRECTAS: " + results[i][CORRECT]*getNumFields()*100/total);
		    	System.out.println(" EXCESS: " + results[i][EXCESS]*getNumFields()*100/total);
		    	System.out.println(" INCOMPLETE: " + results[i][INCOMPLETE]*getNumFields()*100/total);
		    	System.out.println(" INCORRECT: " + results[i][INCORRECT]*getNumFields()*100/total);
		    	
		    	results[i][CORRECT]=0;
		    	results[i][EXCESS]=0;
		    	results[i][INCOMPLETE]=0;
		    	results[i][INCORRECT]=0;
			}	
		    System.out.println("");
		    System.out.println("-----------------------");
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
	
	private final static int INCORRECT = 3;
	private final static int INCOMPLETE = 2;
	private final static int CORRECT = 0;
	private final static int EXCESS = 1;
	
	private int evaluateTest(String correct, String answer){

		if (answer == null){
			if (correct.compareTo("")==0 || correct ==null){
				return CORRECT;
			}
			else{
				return INCORRECT;
			}
		}
		
		if (correct ==null || correct == ""){
			return INCORRECT;
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
		
		
		// ATENCION: EL ORDEN DE ESTOS IFS AFECTA CONSIDERABLEMENTE. 
		
		if (answer.compareTo(correct)==0){
			// ATENCION: EL ORDEN DE ESTOS IFS AFECTA CONSIDERABLEMENTE.
			return CORRECT;
		}
		
		if (answer.contains(correct)){
			// ATENCION: EL ORDEN DE ESTOS IFS AFECTA CONSIDERABLEMENTE.
			return INCOMPLETE;
		}
		
		if (correct.contains(answer)){
			// ATENCION: EL ORDEN DE ESTOS IFS AFECTA CONSIDERABLEMENTE.
			return EXCESS;
		}
		
		return INCORRECT;
		
	}
	public void runTest(String configFilePath) {
		runTest (configFilePath,-1);
		
	}
	
}
