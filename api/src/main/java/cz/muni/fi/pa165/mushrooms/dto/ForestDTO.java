package cz.muni.fi.pa165.mushrooms.dto;

import java.util.Objects;

/**
 * The basic DTO class for the Forest entity.
 *
 * @author BohdanCvejn
 */
public class ForestDTO {

    private Long id;
    private String name;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForestDTO)) return false;

        ForestDTO forestDTO = (ForestDTO) o;
        return Objects.equals(getName(), forestDTO.getName());
    }
}
