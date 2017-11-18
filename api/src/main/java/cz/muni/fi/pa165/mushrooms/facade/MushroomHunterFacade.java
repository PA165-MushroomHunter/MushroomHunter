package cz.muni.fi.pa165.mushrooms.facade;

import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;
import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author bencikpeter, bohdancvejn, bkompis, Lindar84, Buvko
 */
public interface MushroomHunterFacade {

    MushroomHunterDTO findHunterById(Long userId); // TODO: do we want ID's in DTO? o.O

    MushroomHunterDTO findHunterByNickname(String nickname);

    /**
     * Register the given user with the given unencrypted password.
     */
    void registerHunter(MushroomHunterDTO hunter, String unencryptedPassword);

    void deleteHunter(MushroomHunterDTO hunter);

    void updateHunter(MushroomHunterDTO hunter);

    void updatePassword(MushroomHunterDTO hunter, String oldUnencryptedPassword,
                        String newUnencryptedPassword);

    /**
     * Find all registered users
     */
    List<MushroomHunterDTO> findAllHunters();

    /**
     * Try to authenticate a mushroomHunter. Return true only if the hashed password matches the records.
     */

    boolean authenticate(MushroomHunterDTO u);

    /**
     * Check if the given mushroom hunter has administrator privileges.
     */
    boolean isAdmin(MushroomHunterDTO u);

}