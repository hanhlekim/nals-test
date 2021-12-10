package com.nals.work.util;

import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Convert data for datetime.
 *
 * @author HanhLe
 */
public class DateTimeConverter {

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Parse LocalDate to String
     *
     * @param date   the LocalDate
     * @param format the format date
     * @return String
     */
    public static String parseDateToString(LocalDate date, String format) {
        if (Objects.isNull(date))
            return null;
        if (Strings.isBlank(format)) {
            format = DEFAULT_DATE_FORMAT;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }

    /**
     * Parse string to LocalDate
     *
     * @param date   the LocalDate
     * @param format the format date
     * @return LocalDate
     */
    public static LocalDate parseStringToDate(String date, String format) {
        if (Strings.isBlank(format)) {
            format = DEFAULT_DATE_FORMAT;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);//"yyyy-MM-dd HH:mm"
        return LocalDate.parse(date, formatter);
    }

}
