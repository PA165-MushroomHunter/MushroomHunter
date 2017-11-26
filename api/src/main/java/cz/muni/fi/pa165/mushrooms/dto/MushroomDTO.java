package cz.muni.fi.pa165.mushrooms.dto;

import cz.muni.fi.pa165.mushrooms.enums.MushroomType;

/**
 * TODO: create  javadoc
 *
 * @author Buvko
 */
public class MushroomDTO {
    private Long id;
    private String name;
    private MushroomType type;
    private String intervalOfOccurrence;    ///////// TODO String - Date

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setIntervalOfOccurrence(String intervalOfOccurrence) {
        this.intervalOfOccurrence = intervalOfOccurrence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MushroomDTO)) return false;

        MushroomDTO that = (MushroomDTO) o;

        if (!getName().equals(that.getName())) return false;
        if (getType() != that.getType()) return false;
        return getIntervalOfOccurrence().equals(that.getIntervalOfOccurrence());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getIntervalOfOccurrence().hashCode();
        return result;
    }
}
