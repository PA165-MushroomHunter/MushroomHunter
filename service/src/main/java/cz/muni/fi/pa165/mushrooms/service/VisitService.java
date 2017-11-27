package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Buvko
 */
public interface VisitService {

    /**
     * Finds Visit by its Id.
     *
     * @param id search criteria
     * @return Visit with given Id
     * @throws DataAccessException on invalid access
     */
    Visit findVisitById(Long id) throws DataAccessException;

    /**
     * Lists all stored visits.
     *
     * @return list of all visits
     * @throws DataAccessException on invalid access
     */
    List<Visit> findAllVisits() throws DataAccessException;

    /**
     * Finds Visits by its time interval.
     *
     * @param from time from search criteria
     * @param to   time to search criteria
     * @return Visits within given time interval
     * @throws DataAccessException on invalid access
     */
    List<Visit> findVisitByDate(LocalDate from, LocalDate to) throws DataAccessException;

    /**
     * Creates given Visit.
     *
     * @param visit visit to be created
     * @throws DataAccessException on invalid access
     */
    void createVisit(Visit visit) throws DataAccessException;

    /**
     * Deletes given Visit.
     *
     * @param visit visit to be deleted
     * @throws DataAccessException on invalid access
     */
    void deleteVisit(Visit visit) throws DataAccessException;

    /**
     * Updates given Visit.
     *
     * @param visit visit to be updated
     * @throws DataAccessException on invalid access
     */
    void updateVisit(Visit visit) throws DataAccessException;

    /**
     * Finds Visits done by MushroomHunter.
     *
     * @param mushroomHunter mushroomHunter search criteria
     * @return Visits done by given MushroomHunter
     * @throws DataAccessException on invalid access
     */
    List<Visit> getVisitsByHunter(MushroomHunter mushroomHunter) throws DataAccessException;

    /**
     * Finds Visits done in certain Forest.
     *
     * @param forest forest search criteria
     * @return Visit in given forest
     * @throws DataAccessException on invalid access
     */
    List<Visit> getVisitsByForest(Forest forest) throws DataAccessException;

    /**
     * Finds Visits in which given mushroom was found.
     *
     * @param mushroom mushroom search criteria
     * @return Visit in which given mushroom was found
     * @throws DataAccessException on invalid access
     */
    List<Visit> getVisitsByMushroom(Mushroom mushroom) throws DataAccessException;
}
