package cz.muni.fi.pa165.mushrooms.facade;

import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;

import java.util.List;

/**
 * Facade layer of the MushroomHunter type objects.
 *
 * @author bencikpeter, bohdancvejn, bkompis, Lindar84, Buvko
 */
public interface MushroomHunterFacade {

    /**
     * Takes unique id of MushroomHunter and returns a corresponding user.
     *
     * @param userId is an id of MushroomHunter object
     * @return MushroomHunter entity if found, null otherwise
     * @throws IllegalArgumentException on null userId given as a parameter
     */
    MushroomHunterDTO findHunterById(Long userId); // TODO: do we want ID's in DTO? o.O

    /**
     * Takes unique nickname of MushroomHunter and returns a corresponding user.
     *
     * @param userNickname is an nickname of MushroomHunter object
     * @return MushroomHunter entity if found, null otherwise
     * @throws IllegalArgumentException on null userNickname given as a parameter
     */
    MushroomHunterDTO findHunterByNickname(String userNickname);

    /**
     * Register the given user with the given unencrypted password.
     *
     * @param hunter is a new user for registration
     * @param unencryptedPassword is user's password
     *
     * @throws IllegalArgumentException if the hunter is already registered
     * @throws NullPointerException on null hunter or password given as a parameter
     */
//////// co jeste jednou heslo pro potvrzeni spravnosti? /////////////////
    void registerHunter(MushroomHunterDTO hunter, String unencryptedPassword);

    /**
     * Deletes the MushroomHunter from the database.
     *
     * @param hunter is a user which you want to delete
     *
     * @throws IllegalArgumentException on null hunter as a parameter
     * @throws TypeNotPresentException if the user given as a parameter doesn't exist in the database
     */
    void deleteHunter(MushroomHunterDTO hunter);

    /**
     * Takes an object of type MushroomHunter and updates this in a database.
     * You are not able to update an id of the user and his password.
     *
     * @param hunter is a MushroomHunter to be updated in a database
     * @throws IllegalArgumentException on null hunter given as a parameter
     * @throws TypeNotPresentException if the user given as a parameter doesn't exist in the database
     */
    void updateHunter(MushroomHunterDTO hunter);

    /**
     * Takes an object of type MushroomHunter and updates his password in a database.
     *
     * @param hunter is a MushroomHunter whose password you want to update in a database
     * @param oldUnencryptedPassword is user's old password
     * @param newUnencryptedPassword is user's new password
     *
     * @throws NullPointerException on null hunter, new or old password given as a parameter
     * @throws SecurityException when old password is not the same as oldUnencryptedPassword
     */
    void updatePassword(MushroomHunterDTO hunter, String oldUnencryptedPassword,
                                                    String newUnencryptedPassword);
    /**
     * Finds all registered users.
     *
     * @return List of registered MushroomHunters, empty List if there is no-one registered
     */
    List<MushroomHunterDTO> findAllHunters();

    /**
     * Try to authenticate a mushroomHunter.
     *
     * @param user is a MushroomHunter for authentication
     * @return true only if the hashed password matches the records, false otherwise
     * @throws IllegalArgumentException on null user given as a parameter
     */
///// mluvi se tu o heslu - nemelo by byt v parametrech? //////
    boolean authenticate(MushroomHunterDTO user);

    /**
     * Check if the given mushroom hunter has administrator privileges.
     *
     * @param user is a MushroomHunter for checking his privileges
     * @return true if the user is administrator, false otherwise
     *
     * @throws IllegalArgumentException on null user given as a parameter
     * @throws TypeNotPresentException if the user given as a parameter doesn't exist in the database
     */
    boolean isAdmin(MushroomHunterDTO user);
}
