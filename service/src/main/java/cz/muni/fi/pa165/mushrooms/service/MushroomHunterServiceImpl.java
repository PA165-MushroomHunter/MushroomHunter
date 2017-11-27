package cz.muni.fi.pa165.mushrooms.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;

import cz.muni.fi.pa165.mushrooms.dao.MushroomHunterDao;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.service.exceptions.EntityFindServiceException;
import cz.muni.fi.pa165.mushrooms.service.exceptions.EntityOperationServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * Implementation of business logic for MushroomHunter.
 *
 * @author bkompis
 */
@Service
public class MushroomHunterServiceImpl implements MushroomHunterService {
    @Inject
    private MushroomHunterDao hunterDao;

    @Override
    public List<MushroomHunter> findAllHunters() throws DataAccessException {
        try {
            return hunterDao.findAll();
        } catch (Throwable e) {
            throw new EntityFindServiceException("all hunters", e);
        }
    }

    @Override
    public MushroomHunter findHunterById(Long id) throws DataAccessException {
        try {
            return hunterDao.findById(id);
        } catch (Throwable e) {
            throw new EntityFindServiceException("hunter", "id", id, e);
        }
    }

    @Override
    public MushroomHunter findHunterByNickname(String nickname) throws DataAccessException {
        try {
            return hunterDao.findByNickname(nickname);
        } catch (Throwable e) {
            throw new EntityFindServiceException("hunter", "nickname", nickname, e);
        }
    }

    @Override
    public void registerHunter(MushroomHunter hunter, String password) throws DataAccessException {
        try {
            hunter.setPasswordHash(createHash(password));
            hunterDao.create(hunter);
        } catch (Throwable e) {
            throw new EntityOperationServiceException("hunter", "register", hunter, e);
        }
    }

    @Override
    public void deleteHunter(MushroomHunter hunter) throws DataAccessException {
        try {
            hunterDao.delete(hunter);
        } catch (Throwable e) {
            throw new EntityOperationServiceException("hunter", "delete", hunter, e);
        }
    }

    @Override
    public void updateHunter(MushroomHunter hunter) throws DataAccessException {
        try {
            hunterDao.update(hunter);
        } catch (Throwable e) {
            throw new EntityOperationServiceException("hunter", "update", hunter, e);
        }
    }

    @Override
    public boolean updatePassword(MushroomHunter hunter, String oldPassword, String newPassword) throws DataAccessException {
        try {
            MushroomHunter toUpdate = hunterDao.findById(hunter.getId());
            if (validatePassword(oldPassword, toUpdate.getPasswordHash())) {
                toUpdate.setPasswordHash(createHash(newPassword));
                hunterDao.update(toUpdate);
                return true;
            }
            return false;
        } catch (Throwable e) {
            throw new EntityOperationServiceException("hunter", "update password", hunter, e);
        }
    }

    @Override
    public boolean authenticate(MushroomHunter hunter, String password) throws DataAccessException {
        try {
            MushroomHunter mh = findHunterById(hunter.getId()); // fresh entity to be safe
            return validatePassword(password, mh.getPasswordHash());
        } catch (Throwable e) {
            throw new EntityOperationServiceException("hunter", "authenticate", hunter, e);
        }
    }

    @Override
    public boolean isAdmin(MushroomHunter hunter) throws DataAccessException {
        return findHunterById(hunter.getId()).isAdmin();
    }

    // taken from the example project
    //see  https://crackstation.net/hashing-security.htm#javasourcecode
    private static String createHash(String password) {
        final int SALT_BYTE_SIZE = 24;
        final int HASH_BYTE_SIZE = 24;
        final int PBKDF2_ITERATIONS = 1000;
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        // Hash the password
        byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        // format iterations:salt:hash
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validatePassword(String password, String correctHash) {
        if (password == null) return false;
        if (correctHash == null) throw new IllegalArgumentException("password hash is null");
        String[] params = correctHash.split(":");
        int iterations = Integer.parseInt(params[0]);
        byte[] salt = fromHex(params[1]);
        byte[] hash = fromHex(params[2]);
        byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length);
        return slowEquals(hash, testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        return paddingLength > 0 ? String.format("%0" + paddingLength + "d", 0) + hex : hex;
    }
}
