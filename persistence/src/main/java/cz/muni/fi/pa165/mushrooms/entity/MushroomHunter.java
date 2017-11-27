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
 * @author Buvko
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "userNickname")})
public class MushroomHunter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String firstName;

    @NotNull
    @Column(nullable = false)
    private String surname;

    @Column
    private boolean admin;

    @NotNull
    @Column(nullable = false, unique = true)
    private String userNickname;

    @Column
    private String personalInfo;

    @Column
    private String passwordHash;

    @OneToMany(mappedBy = "hunter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Column(nullable = false)
    private List<Visit> visits = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public List<Visit> getVisits() {
        return Collections.unmodifiableList(visits);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Removes a Visit from this Forest and updates the appropriate MushroomHunter.
     *
     * @param toRemove
     */
    public void removeVisit(Visit toRemove) {
        boolean removedHunter = visits.remove(toRemove);
        if (!removedHunter) {
            throw new IllegalArgumentException("Attempt to remove a visit not registered in hunter.");
        }
        boolean removedForest = toRemove.getForest().removeVisitOnlyHere(toRemove);
        if (!removedForest) {
            throw new IllegalArgumentException("Attempt to remove a visit not registered in forest.");
        }
    }

    // Appropriate reference is set using this method when attributes of Visit are set
    void addVisit(Visit newVisit) {
        boolean added = visits.add(newVisit);
        if (!added) {
            throw new IllegalArgumentException("Attempt to add duplicate visit to hunter.");
        }
    }

    boolean removeVisitOnlyHere(Visit toRemove) {
        return visits.remove(toRemove);
    }

    @Override
    public String toString() {
        return "MushroomHunter{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", personalInfo='" + personalInfo + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserNickname());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof MushroomHunter)) {
            return false;
        }

        MushroomHunter mushroomHunter = (MushroomHunter) o;
        return Objects.equals(getUserNickname(), mushroomHunter.getUserNickname());
    }
}
