package cz.muni.fi.pa165.mushrooms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author bencikpeter
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Forest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "forest", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Column(nullable = false)
    private List<Visit> visits = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Visit> getVisits() {
        return Collections.unmodifiableList(visits);
    }

    public void removeVisit(Visit toRemove) {
        boolean removedForest = visits.remove(toRemove);
        if (!removedForest) {
            throw new IllegalArgumentException("Attempt to remove a visit not registered in forest.");
        }
        boolean removedHunter = toRemove.getHunter().removeVisitOnlyHere(toRemove);
        if (!removedHunter) {
            throw new IllegalArgumentException("Attempt to remove a visit not registered in hunter.");
        }
    }

    // Appropriate reference is set using this method when attributes of Visit are set
    void addVisit(Visit newVisit) {
        boolean added = visits.add(newVisit);
        if (!added) {
            throw new IllegalArgumentException("Attempt to add duplicate visit to Forest.");
        }
    }

    boolean removeVisitOnlyHere(Visit toRemove) {
        return visits.remove(toRemove);
    }

    @Override
    public String toString() {
        return "Forest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Forest)) {
            return false;
        }

        Forest forest = (Forest) o;
        return Objects.equals(getName(), forest.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}