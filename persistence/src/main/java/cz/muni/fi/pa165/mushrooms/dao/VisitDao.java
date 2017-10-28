package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Visit;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author bkompis
 */
public interface VisitDao {

    /**
     * Create a visit.
     * @param visit the Visit to create in the database.
     */
    void create(Visit visit);

    /**
     * Delete a visit.
     * @param visit the Visit to delete from the database.
     */
    void delete(Visit visit);

    /**
     * Update a visit in the database with new values.
     * @param visit the Visit to update
     */
    void update(Visit visit);

    /**
     * Searches for a visit in the database.
     * @param id the id of the requested Visit. May not be null.
     * @return the found Visit or null if it could not be found
     */
    Visit findById(Long id);

    /**
     * Lists all stored Visits.
     * @return list of Visits
     */
    List<Visit> findAll();

    /**
     * Searches for visits whose date is between 'from' and 'to'.
     * @param from beginning of the date interval
     * @param to end of the date interval
     * @return list of Visits
     */
    List<Visit> findByDate(LocalDate from, LocalDate to);
    // TODO: maybe add findByHunter and findByForest
}
