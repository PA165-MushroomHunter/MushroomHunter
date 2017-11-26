package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.*;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import cz.muni.fi.pa165.mushrooms.facade.VisitFacade;
import cz.muni.fi.pa165.mushrooms.service.BeanMappingService;
import cz.muni.fi.pa165.mushrooms.service.VisitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author Buvko
 */
@Transactional
@Service
public class VisitFacadeImpl implements VisitFacade {

    @Inject
    private VisitService service;

    @Inject
    private BeanMappingService beanMappingService;


    @Override
    public VisitDTO findById(Long id) {
        Visit visit = service.findVisitById(id);
        System.err.println("service found for id=" + id + " visit " + id);
        if (visit == null){
            return null;
        }
        VisitDTO mapped = beanMappingService.mapTo(visit, VisitDTO.class);
        return mapped;
    }

    @Override
    public List<VisitDTO> listAllVisits() {
        return beanMappingService.mapTo(service.findAllVisits(), VisitDTO.class);
    }

    @Override
    public VisitDTO createVisit(VisitCreateDTO visit) {
        if (visit == null){
            throw new IllegalArgumentException("Null VisitDTO cannot be updated");
        }
        Forest forest = beanMappingService.mapTo(visit.getForest(), Forest.class);
        MushroomHunter hunter = beanMappingService.mapTo(visit.getHunter(), MushroomHunter.class);
        List<Mushroom> mushrooms = beanMappingService.mapTo(visit.getMushrooms(), Mushroom.class);

        Visit newVisit = new Visit();
        newVisit.setForest(forest);
        newVisit.setHunter(hunter);
        newVisit.setMushrooms(mushrooms);
        newVisit.setNote(visit.getNote());

        service.createVisit(newVisit);
        // TODO: return proper object
        return null;
    }

    @Override
    public void deleteVisit(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Null id at visit delete.");
        }
        Visit visit = service.findVisitById(id);
        service.deleteVisit(visit);
    }

    @Override
    public void updateVisit(VisitDTO visit) {
        if (visit == null){
            throw new IllegalArgumentException("Null VisitDTO cannot be updated");
        }
        if ((visit.getHunter() == null) || (visit.getForest() == null)){
            throw new IllegalArgumentException("Visit with null forestDTO or HunterDTO cannot be updated");
        }

        Forest forest = beanMappingService.mapTo(visit.getForest(), Forest.class);
        MushroomHunter hunter = beanMappingService.mapTo(visit.getHunter(), MushroomHunter.class);
        List<Mushroom> mushrooms = beanMappingService.mapTo(visit.getMushrooms(), Mushroom.class);

        Visit entityVisit = service.findVisitById(visit.getId());
        if (entityVisit == null){
            //TODO: react to it somehow
        }

        entityVisit.setHunter(hunter);
        entityVisit.setForest(forest);
        entityVisit.setNote(visit.getNote());
        entityVisit.setMushrooms(mushrooms);

        service.updateVisit(entityVisit);
    }

    @Override
    public List<VisitDTO> listAllVisitsForForest(ForestDTO forest) {
        Forest newEntity = new Forest();
        newEntity.setName(forest.getName());
        newEntity.setDescription(forest.getDescription());

        return beanMappingService.mapTo(service.getVisitsByForest(newEntity), VisitDTO.class);
    }

    @Override
    public List<VisitDTO> listAllVisitsForMushroomHunter(MushroomHunterDTO mushroomHunter) {
        MushroomHunter newEntity = new MushroomHunter();
        newEntity.setUserNickname(mushroomHunter.getUserNickname());
        newEntity.setFirstName(mushroomHunter.getFirstName());
        newEntity.setSurname(mushroomHunter.getSurname());
        newEntity.setPersonalInfo(mushroomHunter.getPersonalInfo());
        newEntity.setAdmin(mushroomHunter.isAdmin());

        return beanMappingService.mapTo(service.getVisitsByHunter(newEntity), VisitDTO.class);
    }

    @Override
    public List<VisitDTO> listAllVisitsByMushroom(MushroomDTO mushroom) {
        Mushroom newEntity = new Mushroom();
        newEntity.setName(mushroom.getName());
        newEntity.setType(mushroom.getType());
        newEntity.setIntervalOfOccurrence(mushroom.getIntervalOfOccurrence(),"");

        return beanMappingService.mapTo(service.getVisitsByMushroom(newEntity), VisitDTO.class);
    }
}
