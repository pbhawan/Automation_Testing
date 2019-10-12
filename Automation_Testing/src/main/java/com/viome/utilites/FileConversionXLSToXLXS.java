package com.viome.utilites;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileConversionXLSToXLXS {

	/*public static void main(String[] args) throws FileNotFoundException {

		FileConversionXLSToXLXS fileConversionXLSToXLXS = new FileConversionXLSToXLXS();
		String xlsFilePath = "./src/test/resources/DemoSheet.xls";//Source File Path
        String xlsxFilePath = fileConversionXLSToXLXS.convertXLS2XLSX(xlsFilePath); // Destination/Converted XLSX file Path
	}*/

	public String convertXLS2XLSX(String xlsFilePath) {
		
		Map cellStyleMap = new HashMap();
		String xlsxFilePath = null;
		Workbook workbookIn = null;
		File xlsxFile = null;
		Workbook workbookOut = null;
		OutputStream out = null;
		String XLSX = ".xlsx";
		try {
			InputStream inputStream = new FileInputStream(xlsFilePath);
			xlsxFilePath = xlsFilePath.substring(0, xlsFilePath.lastIndexOf('.')) + XLSX;
			workbookIn = new HSSFWorkbook(inputStream);
			xlsxFile = new File(xlsxFilePath);
			if (xlsxFile.exists())
				xlsxFile.delete();
			workbookOut = new XSSFWorkbook();
			int sheetCnt = workbookIn.getNumberOfSheets();

			for (int i = 0; i < sheetCnt; i++) {
				Sheet sheetIn = workbookIn.getSheetAt(i);
				Sheet sheetOut = workbookOut.createSheet(sheetIn.getSheetName());
				Iterator rowIt = sheetIn.rowIterator();
				while (rowIt.hasNext()) {
					Row rowIn = (Row) rowIt.next();
					Row rowOut = sheetOut.createRow(rowIn.getRowNum());
					copyRowProperties(rowOut, rowIn,cellStyleMap);
				}
			}
			out = new BufferedOutputStream(new FileOutputStream(xlsxFile));
			workbookOut.write(out);
		} catch (Exception ex) {
			System.err.println("Exception Occured inside transFormXLS2XLSX :: file Name :: " + xlsFilePath
					+ ":: reason ::" + ex.getMessage());
			ex.printStackTrace();
			xlsxFilePath = null;
		} finally {
			try {
				if (workbookOut != null)
					((FilterOutputStream) workbookOut).close();
				if (workbookIn != null)
					((FilterOutputStream) workbookIn).close();
				if (out != null)
					out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return xlsxFilePath;
	}

	private void copyRowProperties(Row rowOut, Row rowIn, Map cellStyleMap) {
		rowOut.setRowNum(rowIn.getRowNum());
		rowOut.setHeight(rowIn.getHeight());
		rowOut.setHeightInPoints(rowIn.getHeightInPoints());
		rowOut.setZeroHeight(rowIn.getZeroHeight());
		Iterator cellIt = rowIn.cellIterator();
		while (cellIt.hasNext()) {
			Cell cellIn = (Cell) cellIt.next();
			Cell cellOut = rowOut.createCell(cellIn.getColumnIndex(), cellIn.getCellType());
			rowOut.getSheet().setColumnWidth(cellOut.getColumnIndex(),
					rowIn.getSheet().getColumnWidth(cellIn.getColumnIndex()));
			copyCellProperties(cellOut, cellIn, cellStyleMap);
		}

	}

	private void copyCellProperties(Cell cellOut, Cell cellIn, Map cellStyleMap) {

		Workbook wbOut = cellOut.getSheet().getWorkbook();
		HSSFPalette hssfPalette = ((HSSFWorkbook) cellIn.getSheet().getWorkbook()).getCustomPalette();
		switch (cellIn.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			break;

		case Cell.CELL_TYPE_BOOLEAN:
			cellOut.setCellValue(cellIn.getBooleanCellValue());
			break;

		case Cell.CELL_TYPE_ERROR:
			cellOut.setCellValue(cellIn.getErrorCellValue());
			break;

		case Cell.CELL_TYPE_FORMULA:
			cellOut.setCellFormula(cellIn.getCellFormula());
			break;

		case Cell.CELL_TYPE_NUMERIC:
			cellOut.setCellValue(cellIn.getNumericCellValue());
			break;

		case Cell.CELL_TYPE_STRING:
			cellOut.setCellValue(cellIn.getStringCellValue());
			break;
		}
		HSSFCellStyle styleIn = (HSSFCellStyle) cellIn.getCellStyle();
		XSSFCellStyle styleOut = null;
		if (cellStyleMap.get(styleIn.getIndex()) != null) {
			styleOut = (XSSFCellStyle) cellStyleMap.get(styleIn.getIndex());
		} else {
			styleOut = (XSSFCellStyle) wbOut.createCellStyle();
			styleOut.setAlignment(styleIn.getAlignment());
			DataFormat format = wbOut.createDataFormat();
			styleOut.setDataFormat(format.getFormat(styleIn.getDataFormatString()));
			HSSFColor forgroundColor = styleIn.getFillForegroundColorColor();
			if (forgroundColor != null) {
				short[] foregroundColorValues = forgroundColor.getTriplet();
				styleOut.setFillForegroundColor(new XSSFColor(new java.awt.Color(foregroundColorValues[0],
						foregroundColorValues[1], foregroundColorValues[2])));
				styleOut.setFillPattern(styleIn.getFillPattern());
			}
			styleOut.setFillPattern(styleIn.getFillPattern());
			styleOut.setBorderBottom(styleIn.getBorderBottom());
			styleOut.setBorderLeft(styleIn.getBorderLeft());
			styleOut.setBorderRight(styleIn.getBorderRight());
			styleOut.setBorderTop(styleIn.getBorderTop());
			HSSFColor bottom = hssfPalette.getColor(styleIn.getBottomBorderColor());
			if (bottom != null) {
				short[] bottomColorArray = bottom.getTriplet();
				styleOut.setBottomBorderColor(new XSSFColor(new java.awt.Color(bottomColorArray[0],
						bottomColorArray[1], bottomColorArray[2])));
			}
			HSSFColor top = hssfPalette.getColor(styleIn.getTopBorderColor());
			if (top != null) {
				short[] topColorArray = top.getTriplet();
				styleOut.setTopBorderColor(new XSSFColor(new java.awt.Color(topColorArray[0], topColorArray[1],
						topColorArray[2])));
			}
			HSSFColor left = hssfPalette.getColor(styleIn.getLeftBorderColor());
			if (left != null) {
				short[] leftColorArray = left.getTriplet();
				styleOut.setLeftBorderColor(new XSSFColor(new java.awt.Color(leftColorArray[0], leftColorArray[1],
						leftColorArray[2])));
			}
			HSSFColor right = hssfPalette.getColor(styleIn.getRightBorderColor());
			if (right != null) {
				short[] rightColorArray = right.getTriplet();
				styleOut.setRightBorderColor(new XSSFColor(new java.awt.Color(rightColorArray[0], rightColorArray[1],
						rightColorArray[2])));
			}
			styleOut.setVerticalAlignment(styleIn.getVerticalAlignment());
			styleOut.setHidden(styleIn.getHidden());
			styleOut.setIndention(styleIn.getIndention());
			styleOut.setLocked(styleIn.getLocked());
			styleOut.setRotation(styleIn.getRotation());
			//styleOut.setShrinkToFit(styleIn.getShrinkToFit());
			styleOut.setVerticalAlignment(styleIn.getVerticalAlignment());
			styleOut.setWrapText(styleIn.getWrapText());
			cellOut.setCellComment(cellIn.getCellComment());
			cellStyleMap.put(styleIn.getIndex(), styleOut);
		}
		cellOut.setCellStyle(styleOut);
	}
}