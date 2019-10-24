package com.viome.components;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviderClass {

	@DataProvider(name="CustomerJson")
	public static Iterator<String> CustomerJson() throws FileNotFoundException, IOException
	{
		List<String> JsonString = ExcelToJSONConvertor.CreteJSONFileFromExcel("./src/test/resources/DataSheet.xls", "Customer");
		
		//return (JsonString);
		return(JsonString.iterator());
	}
	
}
