package cz.muni.fi.pa165.mushrooms.entity;

import com.sun.javafx.UnmodifiableArrayList;
import cz.muni.fi.pa165.mushrooms.utils.LocalDateAttributeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author bkompis
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"visit_hunter", "visit_forest", "date"})})
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "visit_hunter", nullable = false)
    private MushroomHunter hunter;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "visit_forest", nullable = false)
    private Forest forest;

    @ManyToMany
    @JoinColumn(name = "visit_mushroom", nullable = true)
    private List<Mushroom> mushrooms;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = LocalDateAttributeConverter.class)
    //@Temporal(TemporalType.DATE) should not be used
    // solution from https://www.thoughts-on-java.org/persist-localdate-localdatetime-jpa/
    private LocalDate date;

    @Column
    private String note;

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

    public List<Mushroom> getMushrooms() {
        return mushrooms;
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

    public void setMushrooms(List<Mushroom> mushrooms) {
        this.mushrooms = mushrooms;
    }

    public void addMushroom(Mushroom mushroom) {
        this.mushrooms.add(mushroom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHunter(), getForest(), getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visit)) return false;

        Visit visit = (Visit) o;

        if (!getHunter().equals(visit.getHunter())) return false;
        if (!getForest().equals(visit.getForest())) return false;
        if (getMushrooms() != null ? !getMushrooms().equals(visit.getMushrooms()) : visit.getMushrooms() != null)
            return false;
        if (!getDate().equals(visit.getDate())) return false;
        return getNote() != null ? getNote().equals(visit.getNote()) : visit.getNote() == null;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "hunter=" + hunter +
                ", forest=" + forest +
                ", mushrooms=" + mushrooms +
                ", date=" + date +
                ", note='" + note + '\'' +
                '}';
    }
}
