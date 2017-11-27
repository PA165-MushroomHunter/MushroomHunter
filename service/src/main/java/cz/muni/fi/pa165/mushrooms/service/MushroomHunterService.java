package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for operations concerning mushroom hunters, users of the system.
 *
 * @author bkompis
 */
@Service
public interface MushroomHunterService {

    /**
     * Finds Mushroom Hunter by its Id.
     *
     * @param id search criteria
     * @return Mushroom Hunter with given Id
     * @throws DataAccessException on invalid access
     */
    MushroomHunter findHunterById(Long id) throws DataAccessException;

    /**
     * Finds Mushroom Hunter by its nickname.
     *
     * @param nickname search criteria
     * @return Mushroom Hunter with given nickname
     * @throws DataAccessException on invalid access
     */
    MushroomHunter findHunterByNickname(String nickname) throws DataAccessException;

    /**
     * Registers new Mushroom Hunter with given password.
     *
     * @param hunter   hunter to be registered
     * @param password hunters new password
     * @throws DataAccessException on invalid access
     */
    void registerHunter(MushroomHunter hunter, String password) throws DataAccessException;

    /**
     * Deletes given Mushroom Hunter.
     *
     * @param hunter hunter to be deleted
     * @throws DataAccessException on invalid access
     */
    void deleteHunter(MushroomHunter hunter) throws DataAccessException;

    /**
     * Updates given Mushroom Hunter.
     *
     * @param hunter hunter to be updated by
     * @throws DataAccessException on invalid access
     */
    void updateHunter(MushroomHunter hunter) throws DataAccessException;

    /**
     * Updates given Mushroom Hunters password and validates of old one is correct.
     *
     * @param hunter      hunter to have password changed
     * @param oldPassword old password to be validated
     * @return true if validation passed and password was successfully changed false otherwise
     * @throws DataAccessException on invalid access
     */
    boolean updatePassword(MushroomHunter hunter, String oldPassword, String newPassword) throws DataAccessException;

    /**
     * Lists all stored Mushroom Hunters.
     *
     * @return list of Mushroom Hunters
     * @throws DataAccessException on invalid access
     */
    List<MushroomHunter> findAllHunters() throws DataAccessException;

    /**
     * Authenticates given Mushroom Hunter by given password.
     *
     * @param hunter   hunter to be authenticated
     * @param password password for authentication
     * @throws DataAccessException on invalid access
     */
    boolean authenticate(MushroomHunter hunter, String password) throws DataAccessException;

    /**
     * Returns true if given Mushroom Hunter is administrator false otherwise.
     *
     * @param hunter hunter to be checked for administrator
     * @throws DataAccessException on invalid access
     */
    boolean isAdmin(MushroomHunter hunter) throws DataAccessException;
}
