package cz.muni.fi.pa165.mushrooms.dto;

/**
 * Created by Matúš on 25.11.2017.
 */
public class VisitCreateDTO {
    private MushroomHunterDTO hunter;
    private ForestDTO forest;
    //private LocalDate date;
    private String note;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VisitCreateDTO)) return false;

        VisitCreateDTO that = (VisitCreateDTO) o;

        if (!getHunter().equals(that.getHunter())) return false;
        if (!getForest().equals(that.getForest())) return false;
        return getNote() != null ? getNote().equals(that.getNote()) : that.getNote() == null;
    }

    @Override
    public int hashCode() {
        int result = getHunter().hashCode();
        result = 31 * result + getForest().hashCode();
        result = 31 * result + (getNote() != null ? getNote().hashCode() : 0);
        return result;
    }
}
