package cz.muni.fi.pa165.mushrooms.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Converter between LocalDate and java.sql.Date.
 *
 * @author bkompis
 */
// source: https://www.thoughts-on-java.org/persist-localdate-localdatetime-jpa/
@Converter//(autoApply = true) // converts all LocalDates to java.sql.Date
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    public LocalDateAttributeConverter() {
    }

    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return (localDate == null ? null : Date.valueOf(localDate));
    }

    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate) {
        return (sqlDate == null ? null : sqlDate.toLocalDate());
    }
}
