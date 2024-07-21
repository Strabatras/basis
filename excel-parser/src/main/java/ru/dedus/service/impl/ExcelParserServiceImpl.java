package ru.dedus.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import ru.dedus.common.ExcelParserSheetPreferences;
import ru.dedus.common.impl.ExcelParserImpl;
import ru.dedus.example.ExampleDate;
import ru.dedus.exceptions.ExcelParserException;
import ru.dedus.service.ExcelParserService;

@Service
public class ExcelParserServiceImpl implements ExcelParserService {

    @Override
    public void execute(String filePath) {

        final File file = file(filePath);
        final FileInputStream fileInputStream = fileInputStream(file);
        final ExcelParserSheetPreferences preferences = excelParserSheetPreferences();

        ExcelParserImpl excelParser = new ExcelParserImpl(fileInputStream);
        List<ExampleDate> exampleDateList = excelParser.execute(preferences, ExampleDate.class);

        for (ExampleDate row : exampleDateList) {
            System.out.println(row.getRowNumber() + " | "
                    + (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")).format(row.getColumnFour()));
        }
    }

    private ExcelParserSheetPreferences excelParserSheetPreferences() {
        ExcelParserSheetPreferences preferences = new ExcelParserSheetPreferences();
        preferences.setSheetNumber(1);
        preferences.setSkipFirstLines(1);
        return preferences;
    }

    private File file(String pathToFile) {
        final File file;
        try {
            file = ResourceUtils.getFile("classpath:" + pathToFile);

        } catch (Exception e) {
            throw new ExcelParserException(e.getMessage(), e);
        }
        return file;
    }

    private FileInputStream fileInputStream(File file) {
        final FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new ExcelParserException(e.getMessage(), e);
        }
        return fileInputStream;
    }
}
