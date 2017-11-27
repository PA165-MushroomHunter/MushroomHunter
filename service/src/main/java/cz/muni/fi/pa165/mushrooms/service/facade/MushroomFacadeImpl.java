package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.MushroomDTO;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;
import cz.muni.fi.pa165.mushrooms.facade.MushroomFacade;
import cz.muni.fi.pa165.mushrooms.service.MushroomService;
import cz.muni.fi.pa165.mushrooms.service.BeanMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link MushroomFacade} interface.
 *
 * @author Lindar84
 */
@Transactional
@Service
public class MushroomFacadeImpl implements MushroomFacade {

    @Inject
    private MushroomService service;

    @Autowired
    private BeanMappingService beanMappingService;


    @Override
    public List<MushroomDTO> findAllMushrooms() {
        return beanMappingService.mapTo(service.findAllMushrooms(), MushroomDTO.class);
    }

    @Override
    public MushroomDTO findMushroomById(Long id) {
        Mushroom mushroom = service.findMushroomById(id);
        return (mushroom == null) ? null : beanMappingService.mapTo(mushroom, MushroomDTO.class);
    }

    @Override
    public MushroomDTO findMushroomByName(String name) {
        Mushroom mushroom = service.findMushroomByName(name);
        return (mushroom == null) ? null : beanMappingService.mapTo(mushroom, MushroomDTO.class);
    }

    @Override
    public List<MushroomDTO> findByMushroomType(MushroomType mushroomType) {
        if (mushroomType == null) {
            throw new IllegalArgumentException("No mushroom type at finding mushroom by type.");
        }
        List<Mushroom> mushrooms = service.findByMushroomType(mushroomType);
        return beanMappingService.mapTo(mushrooms, MushroomDTO.class);
    }

    @Override
    public List<MushroomDTO> findByIntervalOfOccurrence(String fromMonth, String toMonth) {   ////// TODO String - Date
        if (fromMonth == null) {
            throw new IllegalArgumentException("Parameter fromMonth is null");
        }
        if (toMonth == null) {
            throw new IllegalArgumentException("Parameter toMonth is null");
        }
        List<Mushroom> mushrooms = service.findByIntervalOfOccurrence(fromMonth, toMonth);
        return beanMappingService.mapTo(mushrooms, MushroomDTO.class);
    }

    @Override
    public MushroomDTO createMushroom(MushroomDTO mushroom) {
        if (mushroom == null) {
            throw new IllegalArgumentException("No mushroom at mushroom create.");
        }
        Mushroom mappedMushroom = beanMappingService.mapTo(mushroom, Mushroom.class);
        //set mushroom
        mappedMushroom.setName(mushroom.getName());
        mappedMushroom.setType(mushroom.getType());
        String[] interval = mushroom.getIntervalOfOccurrence().split(" - ");
        mappedMushroom.setIntervalOfOccurrence(interval[0], interval[1]);
        //save mushroom
        Mushroom newMushroom = service.createMushroom(mappedMushroom);
        return findMushroomById(newMushroom.getId());
    }

    @Override   //InvocationTargetException
    public boolean deleteMushroom(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Null id at mushroom delete.");
        }
        Mushroom mushroom = beanMappingService.mapTo(findMushroomById(id), Mushroom.class);
        service.deleteMushroom(mushroom);
        return true;
    }

    @Override
    public MushroomDTO updateMushroom(MushroomDTO mushroom) {
        if (mushroom == null) {
            throw new IllegalArgumentException("No mushroom at mushroom update.");
        }
        Mushroom entity = service.findMushroomById(mushroom.getId());
        entity.setName(mushroom.getName());
        entity.setType(mushroom.getType());
        String[] interval = mushroom.getIntervalOfOccurrence().split(" - ");
        entity.setIntervalOfOccurrence(interval[0], interval[1]);

        service.updateMushroom(entity);
        return findMushroomById(mushroom.getId());
    }

}
