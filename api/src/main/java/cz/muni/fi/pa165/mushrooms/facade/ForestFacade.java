package cz.muni.fi.pa165.mushrooms.facade;

import cz.muni.fi.pa165.mushrooms.dto.AddEditForestDTO;
import cz.muni.fi.pa165.mushrooms.dto.ForestDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomDTO;

import java.util.List;

/**
 * Facade layer of the Forest type objects.
 *
 * @author bencikpeter, bohdancvejn, bkompis, Lindar84, Buvko
 */
public interface ForestFacade {

    /**
     * Takes unique name of Forest and returns a corresponding entity.
     *
     * @param name is a name of Forest object
     * @return Forest entity if found, null otherwise
     * @throws IllegalArgumentException on null name given as a parameter
     */
    ForestDTO findByName(String name);

    /**
     * Takes unique id of Forest and returns a corresponding entity.
     *
     * @param id is an id of Forest object
     * @return Forest entity if found, null otherwise
     * @throws IllegalArgumentException on null id given as a parameter
     */
    ForestDTO findById(Long id);

    /**
     * Deletes a forest with a given ID from database
     *
     * @param id of a Forest which you want to delete
     * @throws IllegalArgumentException on null Forest given as a parameter
     *                                  or if the forest given as a parameter doesn't exist in the database
     */
    void deleteForest(Long id);

    /**
     * Find all Forests.
     *
     * @return a list of all found Forests, empty list if none are found.
     */
    List<ForestDTO> findAllForests();

    /**
     * Takes an object of type Forest and update this in a database.
     * You are not able to update an id of the forest.
     *
     * @param forest is a Forest to be updated in a database
     * @throws IllegalArgumentException on null forest given as a parameter
     *                                  or if the forest given as a parameter doesn't exist in the database
     */
    ForestDTO updateForest(AddEditForestDTO forest);

    /**
     * Takes an object of type Forest and creates an entry in a database.
     *
     * @param forest is a Forest to be created in a database
     * @throws IllegalArgumentException on null forest given as a parameter
     */
    ForestDTO createForest(AddEditForestDTO forest);

    /**
     * Takes an object of type Mushroom and returns list of Forests where you can find this mushroom.
     *
     * @param mushroom is a Mushroom for which you are looking for a forest
     * @return List of all Forests with an occurrence of the mushroom, empty List if there is any such Forest
     * @throws IllegalArgumentException on null Mushroom given as a parameter
     */
    List<ForestDTO> listAllForestsWithMushroom(MushroomDTO mushroom); //complicated business function

}
