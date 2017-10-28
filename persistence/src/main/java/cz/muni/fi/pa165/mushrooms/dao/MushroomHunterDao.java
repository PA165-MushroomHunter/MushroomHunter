package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;

import java.util.List;

/**
 * @author Buvko
 */
public interface MushroomHunterDao {

    /**
     * Takes unique id of MushroomHunter and returns entity with this id
     *
     * @param id of Forest object, non-null
     * @return Forest entity if found, null otherwise
     */
    public MushroomHunter findById(Long id);

    /**
     * Stores the MushroomHunter entity into persistent database
     *
     * @param c MushroomHunter object
     */
    public void create(MushroomHunter c);

    /**
     * Deletes the MushroomHunter entry from persistent database
     *
     * @param c MushroomHunter object
     */
    public void delete(MushroomHunter c);

    /**
     * Returns the list of all MushroomHunter entities stored in database
     */
    public List<MushroomHunter> findAll();

    /**
     * Returns the list all MushroomHunter entities with given first name
     *
     * @param firstName String object, search criteria for MushroomHunter entity
     */
    public List<MushroomHunter> findByFirstName(String firstName);

    /**
     * Returns the list all MushroomHunter entities with given surname
     *
     * @param surname String object, search criteria for MushroomHunter entity
     */
    public List<MushroomHunter> findBySurname(String surname);
}

