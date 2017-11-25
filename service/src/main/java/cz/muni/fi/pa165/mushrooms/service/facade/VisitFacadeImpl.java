package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.ForestDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;
import cz.muni.fi.pa165.mushrooms.dto.VisitCreateDTO;
import cz.muni.fi.pa165.mushrooms.dto.VisitDTO;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
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
 * @author Barbora Kompisova
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
    public VisitDTO createVisit(VisitCreateDTO visit) {
        Forest forest = beanMappingService.mapTo(visit.getForest(), Forest.class);
        MushroomHunter hunter = beanMappingService.mapTo(visit.getHunter(), MushroomHunter.class);

        Visit newVisit = new Visit();
        newVisit.setForest(forest);
        newVisit.setHunter(hunter);

        System.err.println(forest.getId());
        System.err.println(hunter.getId());

        service.createVisit(newVisit);

        System.err.println(service.findAllVisits().size());

        System.err.println("asdbiasnbd + " + newVisit);

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
//        //TODO: check Dozer behaviour
//        MushroomHunter entity = service.findHunterById(hunter.getId());
//        System.err.println("service found:" + entity);
//        entity.setSurname(hunter.getSurname());
//        entity.setFirstName(hunter.getFirstName());
//        entity.setUserNickname(hunter.getUserNickname());
//        entity.setPersonalInfo(hunter.getPersonalInfo());
//        entity.setAdmin(hunter.isAdmin());
//
//        service.updateHunter(entity);
//        System.err.println("service updated to: " + entity);
//        return findHunterById(entity.getId());
    }



    @Override
    public List<VisitDTO> listAllVisitsForForest(ForestDTO forest) {
        return null;
    }

    @Override
    public List<VisitDTO> listAllVisitsForMushroomHunter(MushroomHunterDTO mushroomHunter) {
        MushroomHunter newEntity = new MushroomHunter();
        newEntity.setUserNickname(mushroomHunter.getUserNickname());
        newEntity.setFirstName(mushroomHunter.getFirstName());
        newEntity.setSurname(mushroomHunter.getSurname());
        newEntity.setPersonalInfo(mushroomHunter.getPersonalInfo());
        newEntity.setAdmin(mushroomHunter.isAdmin());

        service.getVisitsByHunter(newEntity);

        return null;
    }
}
