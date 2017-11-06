package com.example.rjq.myapplication.util;

import java.util.UUID;

public class CommonUtils {

	public static String GenerateUUID() {
		UUID uuid = UUID.randomUUID();
		String Uuid;
		Uuid = uuid.toString().replace('-', '_');
		return Uuid;
	}

}
