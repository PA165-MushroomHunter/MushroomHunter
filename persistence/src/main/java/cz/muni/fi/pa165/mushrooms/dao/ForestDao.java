package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Forest;

import java.util.List;

/**
 * @author bencikpeter
 */

public interface ForestDao {

    /**
     * Takes unique id of Forest and returns a corresponding entity if found
     *
     * @param id of Forest object, non-null
     * @return forest entity if found, null otherwise
     */
    Forest findById(Long id);

    /**
     * Takes an object of type Forest and creates an entry in a database
     *
     * @param forest non-null object to be created in a database
     * @throws IllegalArgumentException on null forest given as an parameter
     */
    void create(Forest forest);

    /**
     * Takes an object of type Forest and update an object in a database
     *
     * @param forest non-null object to be updated in a database
     * @throws IllegalArgumentException on null forest given as an parameter
     */
    void update(Forest forest);

    /**
     * Takes a forest object that shall be deleted from database
     *
     * @param forest non-null object to be deleted from the database
     */
    void delete(Forest forest);

    /**
     * Returns all Forests in the database
     *
     * @return List of all Forests, empty List if no Forest is found
     */
    List<Forest> findAll();

    /**
     * Takes unique name of Forest and returns a corresponding entity if found
     *
     * @param name non-null string representing name
     * @return forest with given name if exists, null otherwise
     * @throws IllegalArgumentException on null forest name given as an parameter
     */
    Forest findByName(String name);

}
