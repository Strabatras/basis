package ru.dedus.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import ru.dedus.annotations.ExcelParserCell;
import ru.dedus.common.ExcelParserRowOutput;
import ru.dedus.exceptions.ExcelParserException;

public final class ExcelParserRowOutputUtil {

    public static final Class<ExcelParserCell> ANNOTATION_CLASS = ExcelParserCell.class;

    private ExcelParserRowOutputUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <T> void cellValueToClazzField(String fieldName, T clazzInstance, String cellValue) {
        Field field = declaredField(fieldName, clazzInstance);
        field.setAccessible(true);
        valueToClazzField(field, clazzInstance, cellValue);
    }

    public static <T> T clazzNewInstance(Class<T> clazz) {
        T clazzNewInstance;
        try {
            clazzNewInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            throw new ExcelParserException(e);
        }
        return clazzNewInstance;
    }

    public static <T extends ExcelParserRowOutput> void setClazzInstanceRowNumber(int rowNum, T clazzInstance) {
        clazzInstance.setRowNumber(++rowNum);
    }

    public static <T extends ExcelParserRowOutput> List<Field> annotatedFields(Class<T> clazz) {
        final List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ANNOTATION_CLASS)) {
                validateExcelParserCellFieldAnnotation(field.getAnnotation(ANNOTATION_CLASS));
                fields.add(field);
            }
        }
        return fields;
    }

    public static void checkAnnotatedFields(List<Field> annotatedFields) {
        if (annotatedFields.isEmpty()) {
            throw new ExcelParserException("Класс предназначенный для хранения разобранной строки excel "
                    + "не содержит полей помеченных аннотацией 'ExcelParserCell'");
        }
    }

    private static void validateExcelParserCellFieldAnnotation(ExcelParserCell annotation) {
        if (annotation.index() < 0) {
            throw new ExcelParserException(
                    ("Value of field 'index' of annotation 'ExcelParserCell' must be >= 0. " + "Current value is '%d'")
                            .formatted(annotation.index()));
        }
    }

    private static <T> Field declaredField(String fieldName, T clazzNewInstance) {
        Field declaredField;
        try {
            declaredField = clazzNewInstance.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new ExcelParserException(e);
        }
        return declaredField;
    }

    private static Boolean stringValueToBoolean(String value) {
        if ("1".equals(value) || "1.0".equals(value)) {
            return true;
        }
        if ("0".equals(value) || "0.0".equals(value)) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    public static <T> void valueToClazzField(Field field, T clazzInstance, String value) {

        if (String.class.equals(field.getType())) {
            stringValueToClazzField(field, clazzInstance, value);
            return;
        }

        if (Integer.class.equals(field.getType()) || int.class.equals(field.getType())) {
            integerValueToClazzField(field, clazzInstance, value);
            return;
        }

        if (Long.class.equals(field.getType()) || long.class.equals(field.getType())) {
            longValueToClazzField(field, clazzInstance, value);
            return;
        }

        if (Double.class.equals(field.getType()) || double.class.equals(field.getType())) {
            doubleValueToClazzField(field, clazzInstance, value);
            return;
        }

        if (Float.class.equals(field.getType()) || float.class.equals(field.getType())) {
            floatValueToClazzField(field, clazzInstance, value);
            return;
        }

        if (Byte.class.equals(field.getType()) || byte.class.equals(field.getType())) {
            byteValueToClazzField(field, clazzInstance, value);
            return;
        }

        if (Short.class.equals(field.getType()) || short.class.equals(field.getType())) {
            shortValueToClazzField(field, clazzInstance, value);
            return;
        }

        if (Character.class.equals(field.getType()) || char.class.equals(field.getType())) {
            charValueToClazzField(field, clazzInstance, value);
            return;
        }

        if (Boolean.class.equals(field.getType()) || boolean.class.equals(field.getType())) {
            booleanValueToClazzField(field, clazzInstance, value);
            return;
        }

        if (Date.class.equals(field.getType())) {
            dateValueToClazzField(field, clazzInstance, value);
            return;
        }

        throw new ExcelParserException(
                "Unsupported field type '%s'. Field '%s'".formatted(field.getType(), field.getName()));
    }

    private static <T> void stringValueToClazzField(Field field, T clazzInstance, String value) {
        try {
            field.set(clazzInstance, value);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException("Can't set value in 'String' field '%s'".formatted(field.getName()), e);
        }
    }

    private static <T> void integerValueToClazzField(Field field, T clazzInstance, String value) {
        Integer integerValue = Double.valueOf(value).intValue();
        String fieldTypeName = "Integer";
        try {
            if (field.getType().isPrimitive()) {
                fieldTypeName = "int";
                field.setInt(clazzInstance, integerValue);
                return;
            }
            field.set(clazzInstance, integerValue);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException(
                    "Can't set value '%d' in '%s' field '%s'".formatted(integerValue, fieldTypeName, field.getName()),
                    e);
        }
    }

    private static <T> void longValueToClazzField(Field field, T clazzInstance, String value) {
        Long longValue = Double.valueOf(value).longValue();
        String fieldTypeName = "Long";
        try {
            if (field.getType().isPrimitive()) {
                fieldTypeName = "long";
                field.setLong(clazzInstance, longValue);
                return;
            }
            field.set(clazzInstance, longValue);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException(
                    "Can't set value '%d' in '%s' field '%s'".formatted(longValue, fieldTypeName, field.getName()), e);
        }
    }

    private static <T> void doubleValueToClazzField(Field field, T clazzInstance, String value) {
        Double doubleValue = Double.valueOf(value);
        String fieldTypeName = "Double";
        try {
            if (field.getType().isPrimitive()) {
                fieldTypeName = "double";
                field.setDouble(clazzInstance, doubleValue);
                return;
            }
            field.set(clazzInstance, doubleValue);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException(
                    "Can't set value '%f' in '%s' field '%s'".formatted(doubleValue, fieldTypeName, field.getName()),
                    e);
        }
    }

    private static <T> void floatValueToClazzField(Field field, T clazzInstance, String value) {
        Float floatValue = Float.valueOf(value);
        String fieldTypeName = "Float";
        try {
            if (field.getType().isPrimitive()) {
                fieldTypeName = "float";
                field.setFloat(clazzInstance, floatValue);
                return;
            }
            field.set(clazzInstance, floatValue);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException(
                    "Can't set value '%f' in '%s' field '%s'".formatted(floatValue, fieldTypeName, field.getName()), e);
        }
    }

    private static <T> void byteValueToClazzField(Field field, T clazzInstance, String value) {
        Byte byteValue = Double.valueOf(value).byteValue();
        String fieldTypeName = "Byte";
        try {
            if (field.getType().isPrimitive()) {
                fieldTypeName = "byte";
                field.setByte(clazzInstance, byteValue);
                return;
            }
            field.set(clazzInstance, byteValue);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException(
                    "Can't set value '%f' in '%s' field '%s'".formatted(byteValue, fieldTypeName, field.getName()), e);
        }
    }

    private static <T> void shortValueToClazzField(Field field, T clazzInstance, String value) {
        Short shortValue = Double.valueOf(value).shortValue();
        String fieldTypeName = "Short";
        try {
            if (field.getType().isPrimitive()) {
                fieldTypeName = "short";
                field.setShort(clazzInstance, shortValue);
                return;
            }
            field.set(clazzInstance, shortValue);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException(
                    "Can't set value '%f' in '%s' field '%s'".formatted(shortValue, fieldTypeName, field.getName()), e);
        }
    }

    private static <T> void charValueToClazzField(Field field, T clazzInstance, String value) {
        Character charValue = value.charAt(0);
        String fieldTypeName = "Character";
        try {
            if (field.getType().isPrimitive()) {
                fieldTypeName = "char";
                field.setChar(clazzInstance, charValue);
                return;
            }
            field.set(clazzInstance, charValue);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException(
                    "Can't set value '%c' in '%s' field '%s'".formatted(charValue, fieldTypeName, field.getName()), e);
        }
    }

    private static <T> void booleanValueToClazzField(Field field, T clazzInstance, String value) {
        Boolean booleanValue = stringValueToBoolean(value);
        String fieldTypeName = "Boolean";
        try {
            if (field.getType().isPrimitive()) {
                fieldTypeName = "boolean";
                field.setBoolean(clazzInstance, booleanValue);
                return;
            }
            field.set(clazzInstance, booleanValue);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException(
                    "Can't set value '%b' in '%s' field '%s'".formatted(booleanValue, fieldTypeName, field.getName()),
                    e);
        }
    }

    private static <T> void dateValueToClazzField(Field field, T clazzInstance, String value) {
        ExcelParserCell annotation = field.getAnnotation(ANNOTATION_CLASS);
        Date dateValue = null;
        try {
            dateValue = DateUtils.parseDate(value, annotation.dateFormat());
            field.set(clazzInstance, dateValue);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new ExcelParserException(
                    "Can't set value '%t' in '%s' field '%s'".formatted(dateValue, "Date", field.getName()), e);
        }
    }
}
