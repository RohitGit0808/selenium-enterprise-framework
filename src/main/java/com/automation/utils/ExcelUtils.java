package com.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    private static final Logger log = LogManager.getLogger(ExcelUtils.class);

    private ExcelUtils() {}

    public static List<Map<String, String>> readSheet(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in: " + filePath);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return data;

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(j), getCellValue(cell));
                }
                data.add(rowData);
            }

            log.info("Read {} rows from sheet '{}' in file: {}", data.size(), sheetName, filePath);
        } catch (IOException e) {
            log.error("Failed to read Excel file {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Cannot read Excel file: " + filePath, e);
        }

        return data;
    }

    public static Object[][] readSheetAsArray(String filePath, String sheetName) {
        List<Map<String, String>> data = readSheet(filePath, sheetName);
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        return result;
    }

    public static String getCellData(String filePath, String sheetName, int rowIndex, int colIndex) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowIndex);
            if (row == null) return "";

            Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            return getCellValue(cell);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read cell data", e);
        }
    }

    public static int getRowCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            return sheet != null ? sheet.getLastRowNum() : 0;
        } catch (IOException e) {
            throw new RuntimeException("Cannot get row count", e);
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        return switch (cell.getCellType()) {
            case FORMULA -> formatter.formatCellValue(cell, evaluator);
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                ? cell.getLocalDateTimeCellValue().toString()
                : formatter.formatCellValue(cell);
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case BLANK -> "";
            default -> cell.getStringCellValue().trim();
        };
    }
}
