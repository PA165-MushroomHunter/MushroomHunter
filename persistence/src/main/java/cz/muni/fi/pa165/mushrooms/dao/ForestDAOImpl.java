package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Forest;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author lindar84
 */
public class ForestDAOImpl implements ForestDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Forest findById(Long id) {
        return em.find(Forest.class, id);
    }

    @Override
    public void create(Forest forest) {
        em.persist(forest);
    }

    @Override
    public void delete(Forest forest) throws IllegalArgumentException {
        em.remove(findById(forest.getId()));
    }

    @Override
    public List<Forest> findAll() {
        return em.createQuery("select f from Forest f", Forest.class).getResultList();
    }

    @Override
    public Forest findByName(String name) {
        try {
            return em.createQuery("select f from Forest f where f.name like :name", Forest.class)
                    .setParameter("name", "%"+name+"%").getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
