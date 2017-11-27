package cz.muni.fi.pa165.mushrooms.facade;

import cz.muni.fi.pa165.mushrooms.dto.*;

import java.util.List;

/**
 * Facade layer of the Visit type objects.
 *
 * @author bencikpeter, bohdancvejn, bkompis, Lindar84, Buvko
 */
public interface VisitFacade {

    /**
     * Takes unique id of Visit and returns a corresponding object.
     *
     * @param id is an id of Visit object
     * @return Visit entity if found, null otherwise
     * @throws IllegalArgumentException on null id given as a parameter
     */
    VisitDTO findById(Long id);

    /**
     * Returns list of all visits.
     *
     * @return List of all visits
     */
    List<VisitDTO> listAllVisits();

    /**
     * Takes a Visit object to be deleted from the database by it's id.
     *
     * @param id is primary key for a Visit object
     * @throws IllegalArgumentException on null visit given as a parameter
     *                                  or if the visit given as a parameter doesn't exist in the database
     */
    void deleteVisit(Long id);

    /**
     * Takes a Visit object to be updated in the database.
     *
     * @param visit is a Visit object
     * @throws IllegalArgumentException on null visit given as a parameter
     *                                  or if the visit given as a parameter doesn't exist in the database
     */
    void updateVisit(VisitDTO visit);

    /**
     * Takes a Visit object to be added to the database.
     *
     * @param visit is a Visit object
     * @throws IllegalArgumentException on null visit given as a parameter
     */
    VisitDTO createVisit(VisitCreateDTO visit);

    /**
     * Takes an object of type Forest and returns list of visits carried out in this forest.
     *
     * @param forest is a Forest where you are looking for visits
     * @return List of all visits in the forest, empty List if there nobody enter
     * @throws IllegalArgumentException on null Forest given as a parameter
     *                                  or if the Forest given as a parameter doesn't exist in the database
     */
    List<VisitDTO> listAllVisitsForForest(ForestDTO forest);

    /**
     * Takes an object of type MushroomHunter and returns list of visits carried out by this user.
     *
     * @param mushroomHunter is a user of whom we want to know his visits in any forest
     * @return List of all visits in forests carried out by the mushroomHunter, empty List if he hasn't been to a forest yet
     * @throws IllegalArgumentException on null MushroomHunter given as a parameter
     *                                  or if the MushroomHunter given as a parameter doesn't exist in the database
     */
    List<VisitDTO> listAllVisitsForMushroomHunter(MushroomHunterDTO mushroomHunter);


    List<VisitDTO> listAllVisitsByMushroom(MushroomDTO mushroom);

}
