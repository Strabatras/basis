package ru.dedus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.dedus.service.ExcelParserService;
import ru.dedus.service.impl.ExcelParserServiceImpl;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);

        ExcelParserService service = context.getBean(ExcelParserServiceImpl.class);
        service.execute("files/Test-dates-xlsx.xlsx");
    }
}
