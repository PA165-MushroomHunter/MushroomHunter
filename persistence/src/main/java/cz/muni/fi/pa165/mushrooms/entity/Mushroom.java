package cz.muni.fi.pa165.mushrooms.entity;

import cz.muni.fi.pa165.mushrooms.enums.MushroomType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author Lindar84, BohdanCvejn
 */
@Entity
public class Mushroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique=true)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(nullable = false)
    private MushroomType type;

    @NotNull
    @Column(nullable = false)
    private String intervalOfOccurrence;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MushroomType getType() {
        return type;
    }

    public void setType(MushroomType type) {
        this.type = type;
    }

    public String getIntervalOfOccurrence() {
        return intervalOfOccurrence;
    }

    // "... in the following string format: "June - July" (month - month)"
    // later we can change it to Date type to use the dates for sorting of mushrooms
    public void setIntervalOfOccurrence(String startMonth, String endMonth) {
        this.intervalOfOccurrence = startMonth + " - " + endMonth;
    }

    // Do we want to write ID?
    // ID is in every Entity, so I would stay consistent
    @Override
    public String toString() {
        return "Mushroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", intervalOfOccurrence='" + intervalOfOccurrence + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Mushroom)) return false;

        Mushroom mushroom = (Mushroom) o;

        return name.equals(mushroom.name) && type.equals(mushroom.type) && intervalOfOccurrence.equals(mushroom.intervalOfOccurrence);
    }

    @Override
    public int hashCode() {

        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getIntervalOfOccurrence().hashCode();
        return result;
    }

}
