package ru.dedus.exceptions;

public class ExcelParserException extends RuntimeException {
    public ExcelParserException() {
        super();
    }

    public ExcelParserException(String message) {
        super(message);
    }

    public ExcelParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelParserException(Throwable cause) {
        super(cause);
    }
}
