package cz.muni.fi.pa165.mushrooms.utils;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the LocalDateAttribute converter.
 *
 * @author bkompis
 */
public class LocalDateAttributeConverterTest {
    private Date sqlDate;
    private LocalDate localDate;
    private LocalDateAttributeConverter converter;

    @Before
    public void setUp() {
        localDate = LocalDate.of(2017, Month.SEPTEMBER, 1);
        sqlDate = Date.valueOf("2017-09-01");
        converter = new LocalDateAttributeConverter();
    }

    @Test
    public void convertToDatabaseColumn() {
        assertThat(converter.convertToDatabaseColumn(localDate)).hasSameTimeAs(sqlDate);
    }

    @Test
    public void convertToDatabaseColumnNullInput() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    public void convertToEntityAttribute() {
        assertThat(converter.convertToEntityAttribute(sqlDate)).isEqualTo(localDate);
    }

    @Test
    public void convertToEntityAttributeNullInput() {
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }
}