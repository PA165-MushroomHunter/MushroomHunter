package cz.muni.fi.pa165.mushrooms.dto;

import java.time.LocalDate;

/**
 * TODO: create  javadoc
 *
 * @author Barbora Kompisova
 */
public class VisitDTO {
    private Long id;
    private MushroomHunterDTO hunter;
    private ForestDTO forest;
    //private LocalDate date;
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MushroomHunterDTO getHunter() {
        return hunter;
    }

    public void setHunter(MushroomHunterDTO hunter) {
        this.hunter = hunter;
    }

    public ForestDTO getForest() {
        return forest;
    }

    public void setForest(ForestDTO forest) {
        this.forest = forest;
    }

//    public LocalDate getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VisitDTO)) return false;

        VisitDTO visitDTO = (VisitDTO) o;

        if (!getHunter().equals(visitDTO.getHunter())) return false;
        if (!getForest().equals(visitDTO.getForest())) return false;
      //  if (!getDate().equals(visitDTO.getDate())) return false;
        return getNote() != null ? getNote().equals(visitDTO.getNote()) : visitDTO.getNote() == null;
    }

    @Override
    public int hashCode() {
        int result = getHunter().hashCode();
        result = 31 * result + getForest().hashCode();
      //  result = 31 * result + getDate().hashCode();
        result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VisitDTO{" +
                "id=" + id +
                ", hunter=" + hunter +
                ", forest=" + forest +
                '}';
    }
}
