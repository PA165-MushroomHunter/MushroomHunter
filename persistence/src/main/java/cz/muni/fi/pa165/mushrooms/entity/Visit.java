package cz.muni.fi.pa165.mushrooms.entity;


import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Objects;

/**
 *
 * @author bkompis
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"hunter", "forest", "date"})}) // combination of 3 attributes must be unique
public class Visit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne //todo: other side, maybe add @JoinColumn
    @Column(nullable=false)
    private MushroomHunter hunter;

    @NotNull
    @ManyToOne //todo: other side, maybe add @JoinColumn
    @Column(nullable=false)
    private Forest forest;

    @NotNull
    @Temporal(TemporalType.DATE) // necessary? TODO
    @Convert(converter = LocalDateAttributeConverter.class) // explicit assignment of attribute converter
    @Column(nullable=false)
    private LocalDate date;

    @Column
    private String note;

    @Converter//(autoApply = true) // converts all LocalDates to java.sql.Date
    private class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

        @Override
        public Date convertToDatabaseColumn(LocalDate localDate) {
            return (localDate == null ? null : Date.valueOf(localDate));
        }

        @Override
        public LocalDate convertToEntityAttribute(Date sqlDate) {
            return (sqlDate == null ? null : sqlDate.toLocalDate());
        }
    }

    // getters
    public Long getId() {
        return id;
    }

    public MushroomHunter getHunter() {
        return hunter;
    }

    public Forest getForest() {
        return forest;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    //setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setHunter(MushroomHunter hunter) {
        this.hunter = hunter;
    }

    public void setForest(Forest forest) {
        this.forest = forest;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Visit)) {
            return false;
        }
        Visit visit = (Visit) o;
        return Objects.equals(getHunter(), visit.getHunter()) &&
                Objects.equals(getForest(), visit.getForest()) &&
                Objects.equals(getDate(), visit.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHunter(), getForest(), getDate());
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", hunter=" + hunter +
                ", forest=" + forest +
                ", date=" + date +
                ", note='" + note + '\'' +
                '}';
    }
}
