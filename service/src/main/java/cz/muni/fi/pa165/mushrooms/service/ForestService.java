package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author bencikpeter
 */
public interface ForestService {

    Forest findForestByName(String name);
    Forest findForestById(Long id);
    void deleteForest(Forest forest);
    void updateForest(Forest forest);
    void createForest(Forest forest);

    List<Forest> findAllForestsWithMushroom(Mushroom mushroomEntity);
}
