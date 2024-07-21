package ru.dedus.common;

import java.util.List;

public interface ExcelParser {
    <T extends ExcelParserRowOutput> List<T> execute(ExcelParserSheetPreferences preferences, Class<T> pojo);
}
