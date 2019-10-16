package com.viome.components;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

public class CSVFileHandling {

	// static XSSFWorkbook excelWorkBook;
	String Path;

	int firstRowNum;
	int lastRowNum;
	int firstCellNum;
	 int lastCellNum;
	public List<String> CreteJSONFileFromExcel(String filePath) {
		List<String> jsonString = null;
		try {

			InputStream fis = new FileInputStream(filePath.trim());
			HSSFWorkbook excelWorkBook = new HSSFWorkbook(fis);
			Path = filePath;
			// Get all excel sheet count.
			int totalSheetNumber = excelWorkBook.getNumberOfSheets();

			// Loop in all excel sheet.
			for (int i = 0; i < totalSheetNumber; i++) {
				// Get current sheet.
				Sheet sheet = excelWorkBook.getSheetAt(i);

				// Get sheet name.
				String sheetName = sheet.getSheetName();

				if (sheetName != null && sheetName.length() > 0) {
					// Get current sheet data in a list table.
					List<List<Object>> sheetDataTable = getSheetDataList(sheet);

					// Generate JSON format of above sheet data and write to a JSON file.
					jsonString = getJSONStringFromList(sheetDataTable);
					String jsonFileName = sheet.getSheetName() + ".json";
					/* writeStringToFile(jsonString, jsonFileName); */

				}
			}
			// Close excel work book object.
			//((Closeable) excelWorkBook).close();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		return jsonString;
	}

	/*
	 * Return sheet data in a two dimensional list. Each element in the outer list
	 * is represent a row, each element in the inner list represent a column. The
	 * first row is the column name row.
	 */
	private List<List<Object>> getSheetDataList(Sheet sheet) {
		List<List<Object>> ret = new LinkedList<List<Object>>();

		// Get the first and last sheet row number.
		firstRowNum = sheet.getFirstRowNum();
		lastRowNum = sheet.getLastRowNum();

		if (lastRowNum > 0) {
			// Loop in sheet rows.
			for (int i = firstRowNum; i < lastRowNum + 1; i++) {
				// Get current row object.
				Row row = sheet.getRow(i);

				// Get first and last cell number.
				 firstCellNum = row.getFirstCellNum();
				 lastCellNum = row.getLastCellNum() - 1;

				// Create a String list to save column data in a row.
				List<Object> rowDataList = new LinkedList<Object>();

				// Loop in the row cells.
				for (int j = firstCellNum; j < lastCellNum; j++) {
					// Get current cell.
					Cell cell = row.getCell(j);

					// Get cell type.
					int cellType = cell.getCellType();

					if (cellType == cell.CELL_TYPE_NUMERIC) {
						// long numberValue = ;

						// BigDecimal is used to avoid double value is counted use Scientific counting
						// method.
						// For example the original double variable value is 12345678, but jdk
						// translated the value to 1.2345678E7.
						// String stringCellValue = BigDecimal.valueOf(numberValue).toPlainString();

						rowDataList.add(cell.getNumericCellValue());

					} else if (cellType == cell.CELL_TYPE_STRING) {
						String cellValue = cell.getStringCellValue();
						if ("null".equals(cellValue)) {
							rowDataList.add(null);
						} else
							rowDataList.add(cellValue);
					} else if (cellType == cell.CELL_TYPE_BOOLEAN) {
						boolean numberValue = cell.getBooleanCellValue();

						// String stringCellValue = String.valueOf(numberValue);

						rowDataList.add(numberValue);

					} else if (cellType == cell.CELL_TYPE_BLANK) {
						rowDataList.add("");
					}

				}

				// Add current row data list in the return list.
				ret.add(rowDataList);
			}
		}
		return ret;
	}

	/* Return a JSON string from the string list. */
	private static List getJSONStringFromList(List<List<Object>> dataTable) {
		String ret = "";
		List<String> Data = new ArrayList<String>();
		if (dataTable != null) {
			int rowCount = dataTable.size();

			if (rowCount > 1) {
				// Create a JSONObject to store table data.
				JSONObject tableJsonObject = new JSONObject();

				// The first row is the header row, store each column name.
				List headerRow = dataTable.get(0);

				int columnCount = (headerRow.size()) - 1;

				// Loop in the row data list.
				for (int i = 1; i < rowCount; i++) {
					// Get current row data.
					List dataRow = dataTable.get(i);

					// Create a JSONObject object to store row data.
					JSONObject rowJsonObject = new JSONObject();

					for (int j = 0; j < columnCount; j++) {
						String columnName = String.valueOf(headerRow.get(j));
						Object columnValue = dataRow.get(j);

						rowJsonObject.put(columnName, columnValue);

					}

					Data.add(rowJsonObject.toString());
					/*
					 * String Data = rowJsonObject.toString(); JSONArray jsonArr = new
					 * JSONArray(Data);
					 * 
					 * tableJsonObject.put("Row " + i, rowJsonObject);
					 */

				}

				// Return string format data of JSONObject object.
				/*
				 * ret = tableJsonObject.toString();
				 */
			}
		}
		return Data;
	}

	/* Write string data to a file. */
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

	public void SetFailureStatus(int RowNumber) throws IOException {
		FileInputStream fsIP = new FileInputStream(new File(Path)); // Read the spreadsheet that needs to be updated

		XSSFWorkbook excelWorkBook = new XSSFWorkbook(fsIP);
		/*CellStyle borderHeaderStyle = excelWorkBook.createCellStyle();
		borderHeaderStyle.setBorderBottom(CellStyle.BORDER_THICK);*/
		Sheet sheet = excelWorkBook.getSheetAt(0);
		Row row1 = sheet.getRow(0);
		Cell cell1 = row1.createCell(lastCellNum+1);
		cell1.setCellValue("Results");
		//cell1.setCellStyle(borderHeaderStyle);
		/*CellStyle backgroundStyle = excelWorkBook.createCellStyle();
		backgroundStyle.setFillBackgroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
		backgroundStyle.setFillPattern(CellStyle.BORDER_HAIR);*/
		Row row = sheet.getRow(RowNumber);
		Cell cell = row.createCell(lastCellNum+1);
		cell.setCellValue("Failed");
		fsIP.close();
		FileOutputStream outputStream = new FileOutputStream(Path);
		excelWorkBook.write(outputStream);
		outputStream.close();
		System.out.println("Row Number"+RowNumber+"failed");
	}

	public void SetPassStatus(int RowNumber) throws IOException {
		FileInputStream fsIP = new FileInputStream(new File(Path)); // Read the spreadsheet that needs to be updated

		XSSFWorkbook excelWorkBook = new XSSFWorkbook(fsIP);
		/*CellStyle borderHeaderStyle = excelWorkBook.createCellStyle();
		borderHeaderStyle.setBorderBottom(CellStyle.BORDER_THICK);*/
		Sheet sheet = excelWorkBook.getSheetAt(0);
		Row row1 = sheet.getRow(0);
		Cell cell1 = row1.createCell(lastCellNum+1);
		cell1.setCellValue("Results");
		//cell1.setCellStyle(borderHeaderStyle);
		/*CellStyle backgroundStyle = excelWorkBook.createCellStyle();
		backgroundStyle.setFillBackgroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
		backgroundStyle.setFillPattern(CellStyle.BORDER_HAIR);*/
		Row row = sheet.getRow(RowNumber);
		Cell cell = row.createCell(lastCellNum+1);
		cell.setCellValue("Passed");
		fsIP.close();
		FileOutputStream outputStream = new FileOutputStream(Path);
		excelWorkBook.write(outputStream);
		outputStream.close();
		System.out.println("Row Number"+RowNumber+"Passed");
	}

}