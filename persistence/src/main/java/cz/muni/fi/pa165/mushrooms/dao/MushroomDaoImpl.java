package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

public class MushroomDaoImpl implements MushroomDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Mushroom findById(Long id) {
        return em.find(Mushroom.class, id);
    }

    @Override
    public List<Mushroom> findByMushroomType(MushroomType mushroomType) {
        if(mushroomType == null) {
            throw new IllegalArgumentException("MushroomType is null");
        }
        try {
            return em.createQuery("select m from Mushroom m " +
                                            "where m.mushroomType= :mushroomType", Mushroom.class)
                    .setParameter("mushroomType", mushroomType)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void create(Mushroom mushroom) {
        if (mushroom == null){
            throw new IllegalArgumentException("Null mushroom parameter was given during create.");
        }
        em.persist(mushroom);
    }

    @Override
    public void update(Mushroom mushroom) {
        if (mushroom == null){
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
        if(name == null) {
            throw new IllegalArgumentException("Mushroom name is null");
        }
        try {
            return em.createQuery("select m from Mushroom m where m.name= :name", Mushroom.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Mushroom> findByIntervalOfOccurrence(String fromMonth, String toMonth) {
        if(fromMonth == null) {
            throw new IllegalArgumentException("Parameter fromMonth is null");
        }
        if(toMonth == null) {
            throw new IllegalArgumentException("Parameter toMonth is null");
        }

        try {
            return em.createQuery("select m from Mushroom m " +
                                            "where m.fromMonth= :fromMonth and m.toMonth= :toMonth", Mushroom.class)
                    .setParameter("fromMonth",fromMonth)
                    .setParameter("toMonth", toMonth)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

}
