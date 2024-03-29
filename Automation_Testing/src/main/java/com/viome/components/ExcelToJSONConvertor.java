package com.viome.components;


import java.io.BufferedWriter;
//import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExcelToJSONConvertor {

	// static XSSFWorkbook excelWorkBook;
	static String Path;

	@SuppressWarnings("unused")
	private static ObjectMapper mapper = new ObjectMapper();

	static int firstRowNum;
	static int lastRowNum;
	static int firstCellNum;
	static int lastCellNum;
	static Sheet sheet;
	static String CurrentsheetName;
	static Iterator itr;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })

	public static ArrayList<Object> CreteJSONFileFromExcel(String filePath, Enum SheetName)
			throws FileNotFoundException, IOException {
		ArrayList<Object> jsonString = null;
		try {

			InputStream fis = new FileInputStream(filePath.trim());
			@SuppressWarnings("resource")
			HSSFWorkbook excelWorkBook = new HSSFWorkbook(fis);
			Path = filePath;
			int totalSheetNumber = excelWorkBook.getNumberOfSheets();
			
			for(int i = 0;i<totalSheetNumber; i++) {
				sheet = excelWorkBook.getSheetAt(i);
				String CurrentsheetName = sheet.getSheetName();
				if (CurrentsheetName.equalsIgnoreCase(SheetName.toString()) && CurrentsheetName != null
						&& CurrentsheetName.length() > 0) {
					List<List<Object>> sheetDataTable = getSheetDataList(sheet);
					jsonString = getJSONStringFromList(sheetDataTable);
					   //itr = (Iterator) jsonString.iterator();

				} else {
					continue;
				}
				i++;
			}

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
		System.out.println("JSON Created from Excel ----->" +jsonString);
		return jsonString;
	}

	private static List<List<Object>> getSheetDataList(Sheet sheet) {
		List<List<Object>> ret = new LinkedList<List<Object>>();

		firstRowNum = sheet.getFirstRowNum();
		lastRowNum = sheet.getLastRowNum();
		int count = 0;
		try {
			if (lastRowNum > 0) {

				for (int i = firstRowNum; i < lastRowNum + 1; i++) {
					// Get current row object.
					Row row = sheet.getRow(i);
					count = 0;
					// Get first and last cell number.
					firstCellNum = row.getFirstCellNum();
					lastCellNum = row.getLastCellNum();

					// Create a String list to save column data in a row.
					List<Object> rowDataList = new LinkedList<Object>();
//					HSSFCell LastCell=(HSSFCell) row.getCell(lastCellNum-1);
//					if(LastCell!=null && LastCell.toString().contains("Test Results") )
//							{
//						row.removeCell(LastCell);
//							}
//					else if (LastCell!=null && LastCell.toString().contains("Exception"))
//					{
//						HSSFCell TestResultCell=(HSSFCell) row.getCell(lastCellNum-2);
//						row.removeCell(LastCell);
//						row.removeCell(TestResultCell);
//					}
					
					
					lastCellNum = row.getLastCellNum();
					// Loop in the row cells.
					for (int j = firstCellNum; j < lastCellNum; j++) {
						// Get current cell.
						HSSFCell cell = (HSSFCell) row.getCell(j);
						count = count + 1;
						// Get cell type.
						int cellType = -1;
						if (cell != null) {
							cellType = cell.getCellType();
						} else {
							cellType = Cell.CELL_TYPE_BLANK;
						}

						if (cellType == Cell.CELL_TYPE_NUMERIC) {
							// long numberValue = ;

							// BigDecimal is used to avoid double value is counted use Scientific counting
							// method.
							// For example the original double variable value is 12345678, but jdk
							// translated the value to 1.2345678E7.
							// String stringCellValue = BigDecimal.valueOf(numberValue).toPlainString();

							rowDataList.add(cell.getNumericCellValue());

						} else if (cellType == Cell.CELL_TYPE_STRING) {
							String cellValue = cell.getStringCellValue();
							if ("null".equals(cellValue)) {
								rowDataList.add(null);
							} else
								rowDataList.add(cellValue);

						} else if (cellType == Cell.CELL_TYPE_BOOLEAN) {
							boolean numberValue = cell.getBooleanCellValue();

							// String stringCellValue = String.valueOf(numberValue);

							rowDataList.add(numberValue);

						} else if (cellType == Cell.CELL_TYPE_BLANK) {
							rowDataList.add("");
						} else if (cellType == Cell.CELL_TYPE_FORMULA) {
							boolean numberValue = cell.getBooleanCellValue();

							// String stringCellValue = String.valueOf(numberValue);

							rowDataList.add(numberValue);

						}

					}

					ret.add(rowDataList);
				}
			}
		} catch (Exception e) {

			System.out.println(count);
		}
		return ret;
	}

	/* Return a JSON string from the string list. */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private static ArrayList getJSONStringFromList(List<List<Object>> dataTable) {
		String ret = "";
		ArrayList<Object> Data = new ArrayList<Object>();
		if (dataTable != null) {
			int rowCount = dataTable.size();

			if (rowCount > 1) {
				// Create a JSONObject to store table data.
				JSONObject tableJsonObject = new JSONObject();

				// The first row is the header row, store each column name.
				List headerRow = dataTable.get(0);

				int columnCount = (headerRow.size());

				// Loop in the row data list.
				for (int i = 1; i < rowCount; i++) {
					// Get current row data.
					List dataRow = dataTable.get(i);

					// Create a JSONObject object to store row data.
					Map map = new HashMap();
					for (int j = 0; j < columnCount; j++) {
						String columnName = String.valueOf(headerRow.get(j));
						Object columnValue = dataRow.get(j);
						if (columnValue instanceof String && columnValue.toString().startsWith("[")
								&& columnValue.toString().endsWith("]")) {
							String tempValue =  columnValue.toString() ;
							JSONArray jsonArray = new JSONArray(tempValue);
							columnValue = jsonArray;
						}
						if (columnValue instanceof String && columnValue.toString().startsWith("{")
								&& columnValue.toString().endsWith("}")) {
							String tempValue =  columnValue.toString() ;
							JSONObject jsonObject = new JSONObject(tempValue);
							columnValue = jsonObject;
						}
						map.put(columnName, columnValue);

					}
					JSONObject rowJsonObject = new JSONObject(map);
					String jsonFormattedString = rowJsonObject.toString().replaceAll("\\\\", "");

					Data.add(jsonFormattedString);

				}

			}
		}
		return Data;
	}

	/* Write string data to a file. */
	@SuppressWarnings("unused")
	private static void writeStringToFile(String data, String fileName) {
		try {
			// Get current executing class working directory.
			String currentWorkingFolder = System.getProperty("user.dir");

			// Get file path separator.
			String filePathSeperator = System.getProperty("file.separator");

			// Get the output file absolute path.
			String filePath = currentWorkingFolder + filePathSeperator + fileName;

			// Create File, FileWriter and BufferedWriter object.
			File file = new File(filePath);

			FileWriter fw = new FileWriter(file);

			BufferedWriter buffWriter = new BufferedWriter(fw);

			// Write string data to the output file, flush and close the buffered writer
			// object.
			buffWriter.write(data);

			buffWriter.flush();

			buffWriter.close();

			System.out.println(filePath + " has been created.");

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public void SetFailureStatus(int RowNumber, Enum SheetName,String message) throws IOException {
		
		FileInputStream fsIP = new FileInputStream(new File(Path));
		@SuppressWarnings("resource")
		HSSFWorkbook excelWorkBook = new HSSFWorkbook(fsIP);
		int totalSheetNumber = excelWorkBook.getNumberOfSheets();
		
		for(int i = 0;i<totalSheetNumber; i++) {
			
			sheet = excelWorkBook.getSheetAt(i);
			String CurrentsheetName = sheet.getSheetName();
			if (CurrentsheetName.equalsIgnoreCase(SheetName.toString()) && CurrentsheetName != null
					&& CurrentsheetName.length() > 0) {
				Row row1 = sheet.getRow(0);	            
				Cell cell1 = row1.createCell(lastCellNum);
				cell1.setCellValue("Status Pass/Fail");
				Row row = sheet.getRow(RowNumber);
				Cell cell = row.createCell(lastCellNum);
				cell.setCellValue("Failed");
				CellStyle style=excelWorkBook.createCellStyle();
	            // Setting Cell Background color  
				style.setFillForegroundColor(IndexedColors.RED.getIndex());
	            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	            cell.setCellStyle(style);
	
				Cell cell11 = row1.createCell(lastCellNum + 1);
				cell11.setCellValue("Exception Message/Failed Due To");
				Row row11 = sheet.getRow(RowNumber);
				Cell cell111 = row11.createCell(lastCellNum + 1);
				if(message!=null)
				cell111.setCellValue(message);
				else
				{cell111.setCellValue("java.lang.AssertionError");}
				fsIP.close();
				FileOutputStream outputStream = new FileOutputStream(Path);
				excelWorkBook.write(outputStream);
				outputStream.close();
				System.out.println("Row Number" + RowNumber + "failed");

			} else {
				continue;
			}
			i++;
		}
		
		}
		

	@SuppressWarnings("rawtypes")
	public void SetPassStatus(int RowNumber, Enum SheetName) throws IOException {
		FileInputStream fsIP = new FileInputStream(new File(Path));
		@SuppressWarnings("resource")
		HSSFWorkbook excelWorkBook = new HSSFWorkbook(fsIP);
		int totalSheetNumber = excelWorkBook.getNumberOfSheets();
		
		for(int i = 0;i<totalSheetNumber; i++) {
			
			sheet = excelWorkBook.getSheetAt(i);
			String CurrentsheetName = sheet.getSheetName();
			if (CurrentsheetName.equalsIgnoreCase(SheetName.toString()) && CurrentsheetName != null
					&& CurrentsheetName.length() > 0) {
				Row row1 = sheet.getRow(0);
				Cell cell1 = row1.createCell(lastCellNum);
				cell1.setCellValue("Test Results");
				Row row = sheet.getRow(RowNumber);
				Cell cell = row.createCell(lastCellNum);
				cell.setCellValue("Passed");
				// Setting Cell Background color 
				CellStyle style=excelWorkBook.createCellStyle(); 
				style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
	            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	            cell.setCellStyle(style);
				fsIP.close();
				FileOutputStream outputStream = new FileOutputStream(Path);
				excelWorkBook.write(outputStream);
				outputStream.close();
				System.out.println("Row Number" + RowNumber + "Passed");

			} else {
				continue;
			}
			i++;
		}
	}




}