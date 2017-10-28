package cz.muni.fi.pa165.mushrooms.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Buvko
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "userNickname")})
public class MushroomHunter {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable=false)
    private String firstName;

    @NotNull
    @Column(nullable=false)
    private String surname;
  
    @OneToMany
    @JoinColumn(name="hunter_visit", nullable=false)
    Set<Visit> visits = new HashSet<>();

    private boolean isAdmin;

    @NotNull
    @Column(nullable=false, unique = true)
    private String userNickname;

    @Column
    private String personalInfo;
  
  

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
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public void visitForest(Visit visit) {
        visits.add(visit);
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    @Override
    public String toString() {
        return "MushroomHunter{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
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
