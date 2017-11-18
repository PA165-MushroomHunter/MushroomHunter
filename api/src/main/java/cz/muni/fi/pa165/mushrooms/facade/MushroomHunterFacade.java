package cz.muni.fi.pa165.mushrooms.facade;

import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;
import java.util.List;

/**
 *
 * @author bencikpeter, bohdancvejn, bkompis, Lindar84, Buvko
 */
public interface MushroomHunterFacade {

    MushroomHunterDTO findHunterById(Long userId); // TODO: do we want ID's in DTO? o.O

    MushroomHunterDTO findHunterByNickname(String userNickname);

    void registerHunter(MushroomHunterDTO hunter, String unencryptedPassword);

    void deleteHunter(MushroomHunterDTO hunter);

    void updateHunter(MushroomHunterDTO hunter);

    void updatePassword(MushroomHunterDTO hunter, String oldUnencryptedPassword,
                        String newUnencryptedPassword);

    List<MushroomHunterDTO> findAllHunters();

    boolean authenticate(MushroomHunterDTO user);

    boolean isAdmin(MushroomHunterDTO user);
}
