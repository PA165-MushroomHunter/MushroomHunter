package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;

import java.time.LocalDate;
import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author Barbora Kompisova
 */
public interface VisitService {
    Visit findVisitById(Long id);
    List<Visit> findAllVisits();
    List<Visit> findVisitByDate(LocalDate from, LocalDate to);
    void createVisit(Visit visit);
    void deleteVisit(Visit visit);
    void updateVisit(Visit visit);
    List<Visit> getVisitsByHunter(MushroomHunter mushroomHunter);
    List<Visit> getVisitsByForest(Forest forest);
    List<Visit> getVisitsByMushroom(Mushroom mushroom);
}
