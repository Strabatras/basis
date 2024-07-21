package ru.dedus.util;

import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.ERROR;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import static org.apache.poi.ss.usermodel.CellType._NONE;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import ru.dedus.exceptions.ExcelParserException;

public final class ExcelParserCellUtil {

    private ExcelParserCellUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String cellValueToString(Cell cell) {
        if (cell.getCellType() == NUMERIC) {
            return cellTypeNumericToString(cell);
        }
        if (cell.getCellType() == STRING) {
            return cellTypeStringToString(cell);
        }
        if (cell.getCellType() == FORMULA) {
            return cellTypeFormulaToString(cell);
        }
        if (cell.getCellType() == BOOLEAN) {
            return cellTypeBooleanToString(cell);
        }
        if (cell.getCellType() == _NONE) {
            return "";
        }
        if (cell.getCellType() == BLANK) {
            return "";
        }
        if (cell.getCellType() == ERROR) {
            return "";
        }
        throw new ExcelParserException("Unsupported cell type '%s' for cell '%s'"
                .formatted(cell.getCellType().toString(), cell.getAddress()));
    }

    private static String cellTypeStringToString(Cell cell) {
        return cell.getRichStringCellValue().getString();
    }

    private static String cellTypeNumericToString(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue().toString();
        }
        return String.valueOf(cell.getNumericCellValue());
    }

    private static String cellTypeBooleanToString(Cell cell) {
        return cell.getBooleanCellValue() ? "1" : "0";
    }

    private static String cellTypeFormulaToString(Cell cell) {
        if (cell.getCachedFormulaResultType() == BOOLEAN) {
            return cellTypeBooleanToString(cell);
        }
        if (cell.getCachedFormulaResultType() == NUMERIC) {
            return cellTypeNumericToString(cell);
        }
        if (cell.getCachedFormulaResultType() == STRING) {
            return cellTypeStringToString(cell);
        }
        throw new ExcelParserException("Unsupported formula '%s' for cell '%s'"
                .formatted(cell.getCachedFormulaResultType().toString(), cell.getAddress()));
    }
}
