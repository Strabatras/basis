package ru.dedus.example;

import lombok.Getter;
import lombok.Setter;
import ru.dedus.annotations.ExcelParserCell;
import ru.dedus.common.ExcelParserRowOutput;

@Getter
@Setter
public class ExampleSheetOne implements ExcelParserRowOutput {
    private long rowNumber = 0;

    @ExcelParserCell(index = 0, name = "Натуральное число")
    private boolean naturalNumber;

    @ExcelParserCell(index = 1, name = "Натуральное число как текст")
    private Long naturalNumberAsString;

    @ExcelParserCell(index = 2, name = "Число с двойной точностью")
    private float doubleNumber;

    @ExcelParserCell(index = 3, name = "Число с двойной точностью как текст")
    private String doubleNumberAsString;
}
