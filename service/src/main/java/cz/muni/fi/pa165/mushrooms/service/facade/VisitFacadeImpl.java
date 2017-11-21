package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.ForestDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;
import cz.muni.fi.pa165.mushrooms.dto.VisitDTO;
import cz.muni.fi.pa165.mushrooms.facade.VisitFacade;

import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author Barbora Kompisova
 */
public class VisitFacadeImpl implements VisitFacade {
    @Override
    public VisitDTO findById(Long name) {
        return null;
    }

    @Override
    public void deleteVisit(VisitDTO visit) {

    }

    @Override
    public void updateVisit(VisitDTO visit) {

    }

    @Override
    public void createVisit(VisitDTO visit) {

    }

    @Override
    public List<VisitDTO> listAllVisitsForForest(ForestDTO forest) {
        return null;
    }

    @Override
    public List<VisitDTO> listAllVisitsForMushroomHunter(MushroomHunterDTO mushroomHunter) {
        return null;
    }
}
