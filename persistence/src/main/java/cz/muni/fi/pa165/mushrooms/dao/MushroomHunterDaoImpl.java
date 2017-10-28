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
        return em.createQuery("select c from MushroomHunter c", MushroomHunter.class)
                .getResultList();
    }

    @Override
    public void create(MushroomHunter c) {
        em.persist(c);
    }

    @Override
    public void delete(MushroomHunter c) {
        em.remove(c);
    }

    @Override
    public List<MushroomHunter> findByFirstName(String firstName) {
        try {
            return em
                    .createQuery("select c from MushroomHunter c where firstName = :firstName",
                            MushroomHunter.class).setParameter("firstName", firstName)
                    .getResultList();
        } catch (NoResultException nrf) {
            return null;
        }
    }

    @Override
    public List<MushroomHunter> findBySurname(String surname) {
        try {
            return em
                    .createQuery("select c from MushroomHunter c where surname = :surname",
                            MushroomHunter.class).setParameter("surname", surname)
                    .getResultList();
        } catch (NoResultException nrf) {
            return null;
        }
    }
}
