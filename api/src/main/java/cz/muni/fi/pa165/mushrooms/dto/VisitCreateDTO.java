package cz.muni.fi.pa165.mushrooms.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author Buvko
 */
public class VisitCreateDTO {
    @NotNull
    private MushroomHunterDTO hunter;
    @NotNull
    private ForestDTO forest;
    private LocalDate date;
    private String note;

    private List<MushroomDTO> mushrooms;

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

    public List<MushroomDTO> getMushrooms() {
        return mushrooms;
    }

    public void setMushrooms(List<MushroomDTO> mushrooms) {
        this.mushrooms = mushrooms;
    }
}
