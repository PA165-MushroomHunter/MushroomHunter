package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Mushroom;

import java.util.Date;
import java.util.List;

/**
 * @author BohdanCvejn
 */

public interface MushroomDao {

    /**
     * Takes unique id of Mushroom and returns a corresponding entity if found
     *
     * @param id of Mushroom object, non-null
     * @return Mushroom entity if found, null otherwise
     */
    public Mushroom findById(Long id);

    /**
     * Takes an object of type Mushroom and creates an entry in a database
     *
     * @param mushroom non-null object to be created in a database
     */
    public void create(Mushroom mushroom);

    /**
     * Takes an object of type Mushroom and updates an entry in a database
     *
     * @param mushroom non-null object to be updated in a database
     */
    public void update(Mushroom mushroom);
    
    /**
     * Takes a mushroom object that shall be deleted from database
     *
     * @param mushroom non-null object to be deleted from the database
     */
    public void delete(Mushroom mushroom);

    /**
     * Returns all Mushroom in the database
     *
     * @return List of all Mushroom, empty List if no Mushroom is found
     */
    public List<Mushroom> findAll();

    /**
     * Takes unique name of Mushroom and returns a corresponding entity if found
     *
     * @param name non-null string representing name
     * @return mushroom with given name if exists, null otherwise
     */
    public Mushroom findByName(String name);

    /**
     * Takes interval of occurrence and returns all Mushrooms within given range
     *
     * @param fromMonth name of the month, non-null
     * @param toMonth   name of the month, non-null
     * @return List of Mushroom within given range, empty List if no Mushroom is found
     */
    public Mushroom findByIntervalOfOccurrence(Date fromMonth, Date toMonth);
}
