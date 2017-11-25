package cz.muni.fi.pa165.mushrooms.service;


import cz.muni.fi.pa165.mushrooms.dao.ForestDao;
import cz.muni.fi.pa165.mushrooms.dto.ForestDTO;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author bencikpeter
 */
@Service
public class ForestServiceImpl implements ForestService {

    @Inject
    private ForestDao forestDao;

    @Override
    public Forest findForestByName(String name) {
        return forestDao.findByName(name);
    }

    @Override
    public Forest findForestById(Long id) {
        return forestDao.findById(id);
    }

    @Override
    public void deleteForest(Forest forest) {
        forestDao.delete(forest);
    }

    @Override
    public void updateForest(Forest forest) {
        forestDao.update(forest);
    }

    @Override
    public void createForest(Forest forest) {
        forestDao.create(forest);
    }

    @Override
    public List<Forest> findAllForestsWithMushroom(Mushroom mushroomEntity) {
        return null; //TODO: do we need this?
    }
}
