package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

/*
 * @author bohdancvejn
 */
@Repository
public class MushroomDaoImpl implements MushroomDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Mushroom findById(Long id) {
        return em.find(Mushroom.class, id);
    }

    @Override
    public List<Mushroom> findByMushroomType(MushroomType mushroomType) {
        if (mushroomType == null) {
            throw new IllegalArgumentException("MushroomType is null");
        }
        try {
            return em.createQuery("select m from Mushroom m " +
                    "where m.type= :mushroomType", Mushroom.class)
                    .setParameter("mushroomType", mushroomType)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void create(Mushroom mushroom) {
        if (mushroom == null) {
            throw new IllegalArgumentException("Null mushroom parameter was given during create.");
        }
        em.persist(mushroom);
    }

    @Override
    public void update(Mushroom mushroom) {
        if (mushroom == null) {
            throw new IllegalArgumentException("Null mushroom parameter was given during update.");
        }
        em.merge(mushroom);
    }

    @Override
    public void delete(Mushroom mushroom) {
        em.remove(em.contains(mushroom) ? mushroom : em.merge(mushroom));
    }

    @Override
    public List<Mushroom> findAll() {
        return em.createQuery("select m from Mushroom m", Mushroom.class)
                .getResultList();
    }

    @Override
    public Mushroom findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Mushroom name is null");
        }
        try {
            return em.createQuery("select m from Mushroom m where m.name= :name", Mushroom.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Mushroom> findByIntervalOfOccurrence(String fromMonth, String toMonth) {    //// TODO String - Date
        if (fromMonth == null) {
            throw new IllegalArgumentException("Parameter fromMonth is null");
        }
        if (toMonth == null) {
            throw new IllegalArgumentException("Parameter toMonth is null");
        }

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int indexFrom = Arrays.asList(months).indexOf(fromMonth);
        int indexTo = Arrays.asList(months).indexOf(toMonth);

        String[] monthInterval = new String[12];
        int j = 0;
        for(int i=0; i<months.length; i++){
            if (indexFrom<=i && indexTo>=i){
                monthInterval[j++] = months[i];
            }
        }

        try {
            return em.createQuery("select m from Mushroom m " +
                    "where m.intervalOfOccurrence in (:monthInterval)", Mushroom.class)
                    .setParameter("monthInterval", monthInterval).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}
