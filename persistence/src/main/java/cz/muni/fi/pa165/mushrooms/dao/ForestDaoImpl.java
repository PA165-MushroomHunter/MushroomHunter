package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author lindar84
 */
@Repository
public class ForestDaoImpl implements ForestDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Forest findById(Long id) {
        return em.find(Forest.class, id);
    }

    @Override
    public void create(Forest forest) {
        if (forest == null) {
            throw new IllegalArgumentException("Null forest at create.");
        }
        em.persist(forest);
    }

    @Override
    public void update(Forest forest) {
        if (forest == null) {
            throw new IllegalArgumentException("Null forest at update.");
        }
        em.merge(forest);
    }

    @Override
    public void delete(Forest forest) {
        em.remove(em.contains(forest) ? forest : em.merge(forest));
    }

    @Override
    public List<Forest> findAll() {
        return em.createQuery("select f from Forest f", Forest.class).getResultList();
    }

    @Override
    public Forest findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        try {
            return em.createQuery("select f from Forest f where f.name like :name", Forest.class)
                    .setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
