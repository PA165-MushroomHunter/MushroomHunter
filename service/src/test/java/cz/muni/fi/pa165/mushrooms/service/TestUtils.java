package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;

import java.time.LocalDate;
import java.util.Map;

/**
 * Utilities used in tests.
 *
 * @author bkompis
 */
public class TestUtils {

    public static void validateForest(Forest forest) {
        if (forest == null) throw new IllegalArgumentException("null");
        if (forest.getName() == null) throw new IllegalArgumentException("nameIsNull");
    }

    public static Forest createForest(String name, String description){
        Forest forest = new Forest();
        forest.setName(name);
        forest.setDescription(description);
        return forest;
    }

    public static MushroomHunter createHunter(String firstName, String surname, String userNickname, boolean admin) {
        MushroomHunter hunter = new MushroomHunter();
        hunter.setFirstName(firstName);
        hunter.setSurname(surname);
        hunter.setUserNickname(userNickname);
        hunter.setAdmin(admin);
        hunter.setPersonalInfo("Mushroom hunter " + userNickname + " - " + firstName + " " + surname);
        return hunter;
    }

    public static MushroomHunter createHunter(String firstName, String surname, String userNickname, String passHash ,boolean admin) {
        MushroomHunter hunter = createHunter(firstName, surname, userNickname, admin);
        hunter.setPasswordHash(passHash);
        return hunter;
    }

    public static Mushroom createMushroom(String name, MushroomType type, String from, String to){
        Mushroom mushroom = new Mushroom();
        mushroom.setName(name);
        mushroom.setType(type);
        mushroom.setIntervalOfOccurrence(from, to);
        return mushroom;
    }

    public static Visit createVisit(MushroomHunter hunter, Forest forest, LocalDate date){
        Visit visit = new Visit();
        visit.setHunter(hunter);
        visit.setForest(forest);
        visit.setDate(date);
        visit.setNote("A visit note.");
        // todo: shrooms
        return visit;
    }

    public static boolean checkVisitDuplicity(Map<Long, Visit> visits, Visit newVisit) {
        for (Visit v : visits.values()) {
            if (!v.getId().equals(newVisit.getId())
                    && v.getHunter().equals(newVisit.getHunter())
                    && v.getForest().equals(newVisit.getForest())
                    && v.getDate().equals(newVisit.getDate())) {
                return true;
            }
        }
        return false;
    }


    public static boolean checkVisitValidity(Visit visit){
        if (visit == null || visit.getDate() == null || visit.getHunter() == null || visit.getForest() == null){
            return false;
        }
        return true;
    }

    public static void validateMushroom(Mushroom mushroom){
        if (mushroom == null) throw new IllegalArgumentException("null");
        if (mushroom.getName() == null) throw new IllegalArgumentException("nameIsNull");
        if (mushroom.getIntervalOfOccurrence() == null) throw new IllegalArgumentException("interval of occurence is null");
        if (mushroom.getType() == null) throw new IllegalArgumentException("type is null");

    }

    static boolean checkMushroomHunterDuplicity(Map<Long, MushroomHunter> hunterMap, MushroomHunter newHunter) {
        for (MushroomHunter hunter : hunterMap.values()) {
            if (!hunter.getId().equals(newHunter.getId())
                    && hunter.getUserNickname().equals(newHunter.getUserNickname())) {
                return true;
            }
        }
        return false;
    }
}