package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author bencikpeter
 */
public interface ForestService {

    /**
     * Lists all stored Forests.
     *
     * @return list of Forests
     * @throws DataAccessException on invalid access
     */
    List<Forest> findAllForests() throws DataAccessException;

    /**
     * Finds Forest by its name.
     *
     * @param name search criteria
     * @return Forest with given nickname
     * @throws DataAccessException on invalid access
     */
    Forest findForestByName(String name) throws DataAccessException;

    /**
     * Finds Forest by its Id.
     *
     * @param id search criteria
     * @return Forest with given Id
     * @throws DataAccessException on invalid access
     */
    Forest findForestById(Long id) throws DataAccessException;

    /**
     * Deletes given Forest.
     *
     * @param forest forest to be deleted
     * @throws DataAccessException on invalid access
     */
    void deleteForest(Forest forest) throws DataAccessException;

    /**
     * Updates given Forest.
     *
     * @param forest forest to be updated
     * @throws DataAccessException on invalid access
     */
    void updateForest(Forest forest) throws DataAccessException;

    /**
     * Creates given Forest.
     *
     * @param forest forest to be created
     * @throws DataAccessException on invalid access
     */
    void createForest(Forest forest) throws DataAccessException;

    /**
     * The function takes a mushroom and returns a list of forests where the given mushroom has been found
     * along with the forest, the function also returns a number of occurrences of given mushroom in each forest
     * The resulting list is sorted according to the number of occurrences in descending order.
     *
     * @param mushroomEntity to be found in forests
     * @return list of touples of Forest and number of visits that found a particular mushroom sorted in descending order
     * @throws DataAccessException when any database access failure is encountered
     */
    List<Map.Entry<Forest, Integer>> findAllForestsWithMushroom(Mushroom mushroomEntity) throws DataAccessException;
}
