package com.l2jfrozen.utils;

import java.util.Base64;

public class DeamonStringConverter {

	public static void main(String[] args) {
		
		DeamonStringConverter.encodeString("java -cp ");
		DeamonStringConverter.encodeString(" a.a.d ");
		DeamonStringConverter.encodeString(" lib/l2jfrozen-netcore.jar");
		//DeamonStringConverter.encodeString("https://localhost:8443/l2jfrozen-manager/ManagerService/updateNetcore");
	}
	
	public static String encodeString(String value){
		
		System.out.println("Value to be encoded in Base64: "+value);
		
		String output = Base64.getEncoder().encodeToString(value.getBytes());
		
		System.out.println("Value encoded in Base64: "+output);
		
		return output;
		
	}
	
	public static String decodeString(String value){
		
		System.out.println("Value to be decoded in Base64: "+value);
		
		String output = new String(Base64.getDecoder().decode(value.getBytes()));
		
		System.out.println("Value decoded in Base64: "+output);
		
		return output;
		
	}


}
