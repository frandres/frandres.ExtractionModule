package focalizedExtractor;

import java.util.ArrayList;
import java.util.List;

public class FieldDescriptor implements Comparable<FieldDescriptor>{

	private String fieldName;
//	private String documentRegExp;
	private List <String> specificRegExp;
	private double weight;
	private boolean isDate;
	
	public FieldDescriptor(String fieldName,
			List <String> specificRegExp, double weight, boolean isDate) {
		super();
		this.fieldName = fieldName;
		this.specificRegExp = specificRegExp;
		this.weight = weight;
		this.isDate = isDate;
	}
	
	public FieldDescriptor(String fieldName) {
		super();
		this.fieldName = fieldName;
		this.specificRegExp = null;
		this.weight = 0;
		this.isDate = false;
	}

	protected String getFieldName() {
		return fieldName;
	}

//	protected String getDocumentRegExp() {
//		return documentRegExp;
//	}

	protected List <String> getSpecificRegExp() {
		return specificRegExp;
	}

	public boolean isDate(){
		return this.isDate;
	}
	
	public double getWeight() {
		return weight;
	}

	@Override
	public int compareTo(FieldDescriptor o) {
		return fieldName.compareTo(o.getFieldName());
	}

	public List<String> getPossibleValues(String fieldValue) {
		
		
		if (isDate()){
			DateManipulator dMan = new DateManipulator(fieldValue);
			return dMan.getEquivalentDates();
			
		} else {
			ArrayList<String> possibleValues = new ArrayList<String>();
			possibleValues.add(fieldValue);
			return possibleValues;
		}
		
	}

}
