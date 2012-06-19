package focalizedExtractor;

public class FieldInformation implements Comparable<FieldInformation>{

	private String fieldName;
	private String fieldValue;

	public FieldInformation(String fieldName, String fieldValue) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	protected String getFieldName() {
		return fieldName;
	}
	protected String getFieldValue() {
		return fieldValue;
	}

	@Override
	public int compareTo(FieldInformation o) {
		return getFieldName().compareTo(o.getFieldName());
	}

	
	
	
}
