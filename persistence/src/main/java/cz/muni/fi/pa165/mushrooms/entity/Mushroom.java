package cz.muni.fi.pa165.mushrooms.entity;

import cz.muni.fi.pa165.mushrooms.enums.MushroomType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Lindar84, BohdanCvejn
 */
@Entity
public class Mushroom {
  
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable=false, unique = true)
    private String name;

    @NotNull
    @Column(nullable=false)
    private MushroomType type;

    @NotNull
    @Column(nullable=false)
    private String intervalOfOccurence;


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

    public String getIntervalOfOccurence() {
        return intervalOfOccurence;
    }

    // "... in the following string format: "June - July" (month - month)"
    // later we can change it to Date type to use the dates for sorting of mushrooms
    public void setIntervalOfOccurence(String startMonth, String endMonth) {
        this.intervalOfOccurence = startMonth + " - " + endMonth;
    }

    // Do we want to write ID?
    @Override
    public String toString() {
        return "Mushroom{" +
                "name = '" + name + '\'' +
                ", type = " + type +
                ", interval of occurence = '" + intervalOfOccurence + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Mushroom)) return false;

        Mushroom mushroom = (Mushroom) o;

        return name.equals(mushroom.name) && type.equals(mushroom.type) && intervalOfOccurence.equals(mushroom.intervalOfOccurence);
    }

    @Override
    public int hashCode() {

        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getMushroomType().hashCode();
        result = 31 * result + getIntervalOfOccurence().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Mushroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mushroomType=" + mushroomType +
                ", intervalOfOccurence=" + intervalOfOccurence +
                '}';
    }
}
