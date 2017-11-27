package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for operations concerning mushrooms.
 *
 * @author Lindar84
 */
@Service
public interface MushroomService {

    /**
     * Lists all stored Mushrooms.
     *
     * @return list of Mushrooms
     * @throws DataAccessException on invalid access
     */
    List<Mushroom> findAllMushrooms() throws DataAccessException;

    /**
     * Finds Mushroom by its Id.
     *
     * @param id search criteria
     * @return Mushroom with given Id
     * @throws DataAccessException on invalid access
     */
    Mushroom findMushroomById(Long id) throws DataAccessException;

    /**
     * Finds Mushroom by its nickname.
     *
     * @param name search criteria
     * @return Mushroom with given nickname
     * @throws DataAccessException on invalid access
     */
    Mushroom findMushroomByName(String name) throws DataAccessException;

    /**
     * Finds Mushroom by its edibility type.
     *
     * @param mushroomType edibility type search criteria
     * @return Mushroom with given edibility type
     * @throws DataAccessException on invalid access
     */
    List<Mushroom> findByMushroomType(MushroomType mushroomType) throws DataAccessException;

    /**
     * Finds Mushrooms by its interval of occurrence.
     *
     * @param fromMonth from search criteria
     * @param toMonth   to search criteria
     * @return Mushrooms with given interval
     * @throws DataAccessException on invalid access
     */
    List<Mushroom> findByIntervalOfOccurrence(String fromMonth, String toMonth) throws DataAccessException;  ///////// TODO String - Date

    /**
     * Creates given Mushroom.
     *
     * @param mushroom mushroom to be created
     * @return Mushroom created mushroom
     * @throws DataAccessException on invalid access
     */
    Mushroom createMushroom(Mushroom mushroom) throws DataAccessException;

    /**
     * Deletes given Mushroom.
     *
     * @param mushroom mushroom to be deleted
     * @throws DataAccessException on invalid access
     */
    void deleteMushroom(Mushroom mushroom) throws DataAccessException;

    /**
     * Updates given Mushroom.
     *
     * @param mushroom mushroom to be updated by
     * @throws DataAccessException on invalid access
     */
    void updateMushroom(Mushroom mushroom) throws DataAccessException;

}
