package cz.muni.fi.pa165.mushrooms.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author BohdanCvejn
 */

@Entity
public class Mushroom {

    public enum MushroomType {
        EDIBLE,
        UNEDIBLE,
        POISONOUS;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private MushroomType mushroomType;

    @NotNull
    @Column(nullable = false, unique = true)
    private Date intervalOfOccurence;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MushroomType getMushroomType() {
        return mushroomType;
    }

    public Date getIntervalOfOccurence() {
        return intervalOfOccurence;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMushroomType(MushroomType mushroomType) {
        this.mushroomType = mushroomType;
    }

    public void setIntervalOfOccurence(Date intervalOfOccurence) {
        this.intervalOfOccurence = intervalOfOccurence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mushroom)) return false;

        Mushroom mushroom = (Mushroom) o;

        if (!getId().equals(mushroom.getId())) return false;
        if (!getName().equals(mushroom.getName())) return false;
        if (getMushroomType() != mushroom.getMushroomType()) return false;
        return getIntervalOfOccurence().equals(mushroom.getIntervalOfOccurence());
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
