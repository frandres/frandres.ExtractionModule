package focalizedExtractor;

import java.util.ArrayList;
import java.util.List;

public class TestSet {

	public static ExtractionContext getContext(int num){
		List<FieldInformation> infoCampos = new ArrayList<FieldInformation>();
		
		ExtractionContext eContext = new ExtractionContext(infoCampos);
		
		switch (num) {
		case 0:
			infoCampos.add(new FieldInformation("EsAsignado.calificacion", " Jefa del Laboratorio \"E\""));
			infoCampos.add(new FieldInformation("EsAsignado.fechaAsignacion", "15-1-2002"));
			break;
		case 1:
			infoCampos.add(new FieldInformation("Profesor.Nombre", "Alexander Bueno"));
			infoCampos.add(new FieldInformation("EsAsignado.calificacion", "Director Encargado del Núcleo del Litoral"));
			break;

		default:
			break;
		}
		
		return eContext;
	}
	
	public static String getMissingFieldName(int num){
		switch (num) {
		case 0:
			return "Profesor.Nombre";
		case 1:
			return "EsAsignado.fechaAsignacion";

		default:
			break;
		}
		
		return null;
	}
	
	public static String getCorrectAnswer(int num){
		switch (num) {
		case 0:
			return "Carolina Payares";

		case 1:
			return "4-11-";

		default:
			break;
		}
		
		return null;
	}
	
}
