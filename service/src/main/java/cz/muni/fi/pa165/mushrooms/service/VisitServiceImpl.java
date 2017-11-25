package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.dao.MushroomHunterDao;
import cz.muni.fi.pa165.mushrooms.dao.VisitDao;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author Barbora Kompisova
 */
@Service
public class VisitServiceImpl implements VisitService {

    @Inject
    private MushroomHunterDao mushroomHunterDao;

    @Inject
    private VisitDao visitDao;

    @Override
    public Visit findVisitById(Long id) {
        return visitDao.findById(id);
    }

    @Override
    public List<Visit> findAllVisits() {
        return visitDao.findAll();
    }

    @Override
    public List<Visit> findVisitByDate(LocalDate from, LocalDate to) {
        return visitDao.findByDate(from, to);
    }

    @Override
    public void createVisit(Visit visit) {
        visitDao.create(visit);
    }

    @Override
    public void deleteVisit(Visit visit) {
        visitDao.delete(visit);
    }

    @Override
    public void updateVisit(Visit visit) {
        visitDao.update(visit);
    }

    @Override
    public List<Visit> getVisitsByHunter(MushroomHunter mushroomHunter) {
        List<Visit> visitsByHunter = new ArrayList<>();
        List<Visit> visits = visitDao.findAll();

        for (Visit v: visits) {
            if (v.getHunter().equals(mushroomHunter)){
                visitsByHunter.add(v);
            }
        }
        return visitsByHunter;
    }

    @Override
    public List<Visit> getVisitsByForest(Forest forest) {
        return null;
    }

    @Override
    public List<Visit> getVisitsByMushroom(Mushroom mushroom) {
        return null;
    }

}
