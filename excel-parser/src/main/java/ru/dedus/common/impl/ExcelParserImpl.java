package ru.dedus.common.impl;

import static ru.dedus.util.ExcelParserCellUtil.cellValueToString;
import static ru.dedus.util.ExcelParserRowOutputUtil.ANNOTATION_CLASS;
import static ru.dedus.util.ExcelParserRowOutputUtil.annotatedFields;
import static ru.dedus.util.ExcelParserRowOutputUtil.cellValueToClazzField;
import static ru.dedus.util.ExcelParserRowOutputUtil.checkAnnotatedFields;
import static ru.dedus.util.ExcelParserRowOutputUtil.clazzNewInstance;
import static ru.dedus.util.ExcelParserRowOutputUtil.setClazzInstanceRowNumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import ru.dedus.common.ExcelParser;
import ru.dedus.common.ExcelParserRowOutput;
import ru.dedus.common.ExcelParserSheetPreferences;
import ru.dedus.exceptions.ExcelParserException;

public class ExcelParserImpl implements ExcelParser {
    private final FileInputStream fileInputStream;

    public ExcelParserImpl(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    @Override
    public <T extends ExcelParserRowOutput> List<T> execute(ExcelParserSheetPreferences preferences, Class<T> clazz) {
        final List<T> output = new ArrayList<>();
        final var annotatedFieldList = annotatedFields(clazz);
        checkAnnotatedFields(annotatedFieldList);
        final Workbook workbook = workbook(fileInputStream);
        checkSheetNumber(preferences.getSheetNumber(), workbook.getNumberOfSheets());
        final Sheet sheet = workbook.getSheetAt(preferences.getSheetNumber() - 1);

        for (int i = preferences.getSkipFirstLines(); i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            T clazzInstance = clazzNewInstance(clazz);
            sheetRow(row, annotatedFieldList, clazzInstance);
            output.add(clazzInstance);
        }
        return output;
    }

    private <T extends ExcelParserRowOutput> void sheetRow(Row row, List<Field> fieldList, T clazzInstance) {
        setClazzInstanceRowNumber(row.getRowNum(), clazzInstance);
        for (Field field : fieldList) {
            var annotation = field.getAnnotation(ANNOTATION_CLASS);
            var cell = row.getCell(annotation.index());
            if (cell != null) {
                sheetCell(cell, field.getName(), clazzInstance);
            }
        }
    }

    private <T> void sheetCell(Cell cell, String fieldName, T clazzInstance) {
        String cellValue = cellValueToString(cell);
        if (!cellValue.isEmpty()) {
            cellValueToClazzField(fieldName, clazzInstance, cellValue);
        }
    }

    private Workbook workbook(FileInputStream fileInputStream) {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(fileInputStream);
        } catch (IOException e) {
            throw new ExcelParserException(e.getMessage(), e);
        }
        return workbook;
    }

    private void checkSheetNumber(int sheetNumber, int workbookSheetsQuantity) {
        if (sheetNumber < 1) {
            throw new ExcelParserException("Number of requested workbook sheet cannot be less than '1'");
        }
        if (sheetNumber > workbookSheetsQuantity) {
            throw new ExcelParserException(
                    "Number of requested workbook sheet '%d', but there are only '%d' sheets in this workbook"
                            .formatted(sheetNumber, workbookSheetsQuantity));
        }
    }
}
