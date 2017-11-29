package cz.muni.fi.pa165.mushrooms.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cz.muni.fi.pa165.mushrooms.dao.VisitDao;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import cz.muni.fi.pa165.mushrooms.service.exceptions.EntityFindServiceException;
import cz.muni.fi.pa165.mushrooms.service.exceptions.EntityOperationServiceException;
import cz.muni.fi.pa165.mushrooms.service.exceptions.MushroomHunterServiceDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * @author Buvko
 */
@Service
public class VisitServiceImpl implements VisitService {

    @Inject
    private VisitDao visitDao;

    @Override
    public Visit findVisitById(Long id) throws DataAccessException {
        try {
            return visitDao.findById(id);
        } catch (Throwable e) {
            throw new EntityFindServiceException("visit", "id", id, e);
        }
    }

    @Override
    public List<Visit> findAllVisits() throws DataAccessException {
        try {
            return visitDao.findAll();
        } catch (Throwable e) {
            throw new EntityFindServiceException("all visits", e);
        }
    }

    @Override
    public List<Visit> findVisitByDate(LocalDate from, LocalDate to) throws DataAccessException {
        try {
            return visitDao.findByDate(from, to);
        } catch (Throwable e) {
            throw new EntityFindServiceException("visit", "date", from.toString() + " - " + to, e);
        }
    }

    @Override
    public void createVisit(Visit visit) throws DataAccessException {
        try {
            visitDao.create(visit);
        } catch (Throwable e) {
            throw new EntityOperationServiceException("visit", "create", visit, e);
        }
    }

    @Override
    public void deleteVisit(Visit visit) throws DataAccessException {
        try {
            Visit v = findVisitById(visit.getId());
            v.getHunter().removeVisit(v); // otherwise, the visit won't be deleted
            visitDao.delete(visit);
        } catch (Throwable e) {
            throw new EntityOperationServiceException("visit", "delete", visit, e);
        }
    }

    @Override
    public void updateVisit(Visit visit) throws DataAccessException {
        try {
            visitDao.update(visit);
        } catch (Throwable e) {
            throw new EntityOperationServiceException("visit", "update", visit, e);
        }
    }

    @Override
    public List<Visit> getVisitsByHunter(MushroomHunter mushroomHunter) throws DataAccessException {
        if (mushroomHunter == null) {
            throw new MushroomHunterServiceDataAccessException("Null hunter at finding visits.");
        }

        List<Visit> visitsByHunter = new ArrayList<>();
        List<Visit> visits = findAllVisits();// takes care of exceptions

        for (Visit v : visits) {
            if (v.getHunter().equals(mushroomHunter)) {
                visitsByHunter.add(v);
            }
        }

        return visitsByHunter;
    }

    @Override
    public List<Visit> getVisitsByForest(Forest forest) throws DataAccessException {
        if (forest == null) {
            throw new MushroomHunterServiceDataAccessException("Null forest at finding visits.");
        }

        List<Visit> visitsByForest = new ArrayList<>();
        List<Visit> visits = findAllVisits(); // takes care of exceptions

        for (Visit v : visits) {
            if (v.getForest().equals(forest)) {
                visitsByForest.add(v);
            }
        }

        return visitsByForest;
    }

    @Override
    public List<Visit> getVisitsByMushroom(Mushroom mushroom) throws DataAccessException {
        if (mushroom == null) {
            throw new MushroomHunterServiceDataAccessException("Null mushroom at finding visits.");
        }

        List<Visit> visitsByMushroom = new ArrayList<>();
        List<Visit> visits = findAllVisits(); // takes care of exceptions
        List<Mushroom> mushrooms;

        for (Visit v : visits) {
            mushrooms = v.getMushrooms();
            for (Mushroom m : mushrooms) {
                if (m.equals(mushroom)) {
                    visitsByMushroom.add(v);
                }
            }
        }

        return visitsByMushroom;
    }

}
