package cz.muni.fi.pa165.mushrooms.facade;

import cz.muni.fi.pa165.mushrooms.dto.MushroomDTO;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;

import java.util.List;

/**
 * @author Barbora Kompisova, Buvko
 */
public interface MushroomFacade {

    /**
     * Returns all Mushroom in the database
     *
     * @return List of all Mushroom, empty List if no Mushroom is found
     */
    List<MushroomDTO> findAllMushrooms();

    /**
     * Takes unique id of Mushroom and returns a corresponding entity if found
     *
     * @param id of Mushroom object, non-null
     * @return Mushroom entity if found, null otherwise
     */
    MushroomDTO findMushroomById(Long id);

    /**
     * Takes unique name of Mushroom and returns a corresponding entity if found
     *
     * @param name non-null string representing name
     * @return mushroom with given name if exists, null otherwise
     */
    MushroomDTO findMushroomByName(String name);

    /**
     * Takes mushroom type and returns a corresponding List of found entities
     *
     * @param mushroomType non-null
     * @return List of Mushroom, empty List if no Mushroom is found
     */
    List<MushroomDTO> findByMushroomType(MushroomType mushroomType);

    /**
     * Takes interval of occurrence and returns all Mushrooms within given range
     *
     * @param fromMonth name of the month, non-null
     * @param toMonth   name of the month, non-null
     * @return List of Mushroom within given range, empty List if no Mushroom is found
     */
    List<MushroomDTO> findByIntervalOfOccurrence(String fromMonth, String toMonth);   /////// TODO String - Date

    /**
     * Takes an object of type Mushroom and creates an entry in a database
     *
     * @param mushroom non-null object to be created in a database
     */
    MushroomDTO createMushroom(MushroomDTO mushroom);

    /**
     * Takes a mushroom object that shall be deleted from database
     *
     * @param id is non-null id of the mushroom to be deleted from the database
     * @return true if the mushroom was successfully deleted
     */
    boolean deleteMushroom(Long id);

    /**
     * Takes an object of type Mushroom and updates an entry in a database
     *
     * @param mushroom non-null object to be updated in a database
     */
    MushroomDTO updateMushroom(MushroomDTO mushroom);

}
