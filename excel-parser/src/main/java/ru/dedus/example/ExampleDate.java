package ru.dedus.example;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import ru.dedus.annotations.ExcelParserCell;
import ru.dedus.common.ExcelParserRowOutput;

@Getter
@Setter
public class ExampleDate implements ExcelParserRowOutput {
    private long rowNumber = 0;

    @ExcelParserCell(index = 3, name = "Date 20.02.37 (dd.mm.YY)")
    private Date columnFour;
}
