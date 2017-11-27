package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Buvko
 */
@Repository
public class MushroomHunterDaoImpl implements MushroomHunterDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public MushroomHunter findById(Long id) {
        return em.find(MushroomHunter.class, id);
    }

    @Override
    public List<MushroomHunter> findAll() {
        return em.createQuery("select mh from MushroomHunter mh", MushroomHunter.class)
                .getResultList();
    }

    @Override
    public void create(MushroomHunter mh) {
        if (mh == null) {
            throw new IllegalArgumentException("Null mushroom hunter at create.");
        }
        em.persist(mh);
    }

    @Override
    public void delete(MushroomHunter mh) {
        em.remove(em.contains(mh) ? mh : em.merge(mh));
    }

    @Override
    public void update(MushroomHunter mh) {
        if (mh == null) {
            throw new IllegalArgumentException("Null mushroom hunter at update.");
        }
        em.merge(mh);
    }

    @Override
    public List<MushroomHunter> findByFirstName(String firstName) {
        if (firstName == null) {
            throw new IllegalArgumentException("firstName is null");
        }

        try {
            return em
                    .createQuery("select mh from MushroomHunter mh where mh.firstName = :firstName",
                            MushroomHunter.class).setParameter("firstName", firstName)
                    .getResultList();
        } catch (NoResultException nrf) {
            return null;
        }
    }

    @Override
    public List<MushroomHunter> findBySurname(String surname) {
        if (surname == null) {
            throw new IllegalArgumentException("surname is null");
        }
        try {
            return em
                    .createQuery("select mh from MushroomHunter mh where mh.surname = :surname",
                            MushroomHunter.class).setParameter("surname", surname)
                    .getResultList();
        } catch (NoResultException nrf) {
            return null;
        }
    }

    @Override
    public MushroomHunter findByNickname(String userNickname) {
        if (userNickname == null) {
            throw new IllegalArgumentException("user nickname is null");
        }
        try {
            return em.createQuery("select mh from MushroomHunter mh where mh.userNickname like :userNickname", MushroomHunter.class)
                    .setParameter("userNickname", userNickname).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}