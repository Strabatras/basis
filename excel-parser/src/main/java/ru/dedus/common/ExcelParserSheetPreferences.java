package ru.dedus.common;

public class ExcelParserSheetPreferences {
    private int sheetNumber = 1;
    private int skipFirstLines = 0;

    public int getSheetNumber() {
        return sheetNumber;
    }

    public void setSheetNumber(int sheetNumber) {
        this.sheetNumber = sheetNumber;
    }

    public int getSkipFirstLines() {
        return skipFirstLines;
    }

    public void setSkipFirstLines(int skipFirstLines) {
        this.skipFirstLines = skipFirstLines;
    }
}
