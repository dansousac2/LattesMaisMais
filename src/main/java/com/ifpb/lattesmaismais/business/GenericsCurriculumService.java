package com.ifpb.lattesmaismais.business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenericsCurriculumService {
	
	public static String createVersionName() {
		LocalDateTime ldt = LocalDateTime.now();
		String ldtString = ldt.format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss"));
		return "V_" + ldtString;
	}

}
