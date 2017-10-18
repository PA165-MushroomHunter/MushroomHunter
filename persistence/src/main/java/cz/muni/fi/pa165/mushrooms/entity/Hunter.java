package cz.muni.fi.pa165.mushrooms.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Matúš on 18.10.2017.
 */
@Entity
public class Hunter {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable=false,unique=true)
    private String firstName;

    @NotNull
    @Column(nullable=false,unique=true)
    private String surName;

    private String personalInfo;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }

    @Override
    public String toString() {
        return "Hunter{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", surName='" + surName + '\'' +
                ", personalInfo='" + personalInfo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hunter)) return false;

        Hunter hunter = (Hunter) o;

        if (!getFirstName().equals(hunter.getFirstName())) return false;
        if (!getSurName().equals(hunter.getSurName())) return false;
        return getPersonalInfo() != null ? getPersonalInfo().equals(hunter.getPersonalInfo()) : hunter.getPersonalInfo() == null;
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getSurName().hashCode();
        result = 31 * result + (getPersonalInfo() != null ? getPersonalInfo().hashCode() : 0);
        return result;
    }
}
