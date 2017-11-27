package cz.muni.fi.pa165.mushrooms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.inject.Inject;

import cz.muni.fi.pa165.mushrooms.dao.ForestDao;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import cz.muni.fi.pa165.mushrooms.service.exceptions.EntityFindServiceException;
import cz.muni.fi.pa165.mushrooms.service.exceptions.EntityOperationServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * @author bencikpeter
 */
@Service
public class ForestServiceImpl implements ForestService {

    @Inject
    private ForestDao forestDao;

    @Inject
    private VisitService visitService;

    public List<Forest> findAllForests() throws DataAccessException {
        try {
            return forestDao.findAll();
        } catch (Throwable e) {
            throw new EntityFindServiceException("all forests", e);
        }
    }

    @Override
    public Forest findForestByName(String name) throws DataAccessException {
        try {
            return forestDao.findByName(name);
        } catch (Throwable e) {
            throw new EntityFindServiceException("forest", "name", name, e);
        }
    }

    @Override
    public Forest findForestById(Long id) throws DataAccessException {
        try {
            return forestDao.findById(id);
        } catch (Throwable e) {
            throw new EntityFindServiceException("forest", "id", id, e);
        }
    }

    @Override
    public void deleteForest(Forest forest) throws DataAccessException {
        try {
            forestDao.delete(forest);
        } catch (Throwable e) {
            throw new EntityOperationServiceException("forest", "delete", forest, e);
        }
    }

    @Override
    public void updateForest(Forest forest) throws DataAccessException {
        try {
            forestDao.update(forest);
        } catch (Throwable e) {
            throw new EntityOperationServiceException("forest", "update", forest, e);
        }
    }

    @Override
    public void createForest(Forest forest) throws DataAccessException {
        try {
            forestDao.create(forest);
        } catch (Throwable e) {
            throw new EntityOperationServiceException("forest", "create", forest, e);
        }
    }

    private static class ForestCountComparator implements Comparator<Map.Entry<Forest, Integer>> {
        public int compare(Map.Entry<Forest, Integer> e1, Map.Entry<Forest, Integer> e2) {

            return e1.getValue().compareTo(e2.getValue());
        }
    }

    @Override
    public List<Map.Entry<Forest, Integer>> findAllForestsWithMushroom(Mushroom mushroomEntity) throws DataAccessException {
        //this is not data manipulation problem, not throwing DataAccessException is OK
        if (mushroomEntity == null) throw new IllegalArgumentException("null mushroom entity");
        List<Visit> visits = visitService.findAllVisits();
        List<Visit> visitsWithMushroom = new ArrayList<>();
        for (Visit visit : visits) {
            if (visit.getMushrooms().contains(mushroomEntity)) {
                visitsWithMushroom.add(visit);
            }
        }

        //Simulating multiset behavior
        // forest is the key, integer tells how many times the forest occurred in visits
        Map<Forest, Integer> forestCount = new HashMap<>();
        for (Visit visit : visitsWithMushroom) {
            Forest forest = visit.getForest();
            if (forestCount.containsKey(forest)) {
                Integer count = forestCount.get(forest);
                count++;
                forestCount.replace(forest, count);
            } else {
                forestCount.put(forest, 1);
            }
        }

        List<Map.Entry<Forest, Integer>> sortedForests = new ArrayList<>();

        if (forestCount.size() == 0) return sortedForests;

        //priority queue encapsulates sorting
        Queue<Map.Entry<Forest, Integer>> queue = new PriorityQueue<>(forestCount.size(), new ForestCountComparator());
        queue.addAll(forestCount.entrySet());


        for (Map.Entry<Forest, Integer> entry; (entry = queue.poll()) != null; ) {
            sortedForests.add(entry);
        }

        Collections.reverse(sortedForests);
        return sortedForests;
    }

}
