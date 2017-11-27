package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * TODO: create  javadoc
 *
 * @author bencikpeter
 */
public interface ForestService {

    List<Forest> findAllForests() throws DataAccessException;

    Forest findForestByName(String name) throws DataAccessException;

    Forest findForestById(Long id) throws DataAccessException;

    void deleteForest(Forest forest) throws DataAccessException;

    void updateForest(Forest forest) throws DataAccessException;

    void createForest(Forest forest) throws DataAccessException;

    /**
     * The function takes a mushroom and returns a list of forests where the given mushroom has been found
     * along with the forest, the function also returns a number of occurrences of given mushroom in each forest
     * The resulting list is sorted according to the number of occurrences in ascending order.
     *
     * @param mushroomEntity to be found in forests
     * @return list of touples of Forest and number of visits that found a particular mushroom sorted in ascending order
     * @throws DataAccessException when any database access failure is encountered
     */
    List<Map.Entry<Forest,Integer>> findAllForestsWithMushroom(Mushroom mushroomEntity) throws DataAccessException;
}
