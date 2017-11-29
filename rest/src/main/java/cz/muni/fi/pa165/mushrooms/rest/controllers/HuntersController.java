package cz.muni.fi.pa165.mushrooms.rest.controllers;

import java.util.Collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterCreateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterUpdateDTO;
import cz.muni.fi.pa165.mushrooms.facade.MushroomHunterFacade;
import cz.muni.fi.pa165.mushrooms.rest.exceptions.ResourceAlreadyExistsException;
import cz.muni.fi.pa165.mushrooms.rest.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.mushrooms.rest.tools.ApiUris;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping(ApiUris.ROOT_URI_HUNTERS)
public class HuntersController {
    private final static Logger logger = LoggerFactory.getLogger(HuntersController.class);

    @Inject
    private MushroomHunterFacade hunterFacade;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final Collection<MushroomHunterDTO> getUsers() throws JsonProcessingException {
        logger.debug("rest getUsers()");
        return hunterFacade.findAllHunters();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final MushroomHunterDTO getUser(@PathVariable("id") long id) throws Exception {
        logger.debug("rest getHunter({})", id);
        MushroomHunterDTO userDTO = hunterFacade.findHunterById(id);
        if (userDTO == null){
            throw new ResourceNotFoundException();
        }
        return userDTO;
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void deleteHunter(@PathVariable("id") long id) throws Exception {
        logger.debug("rest deleteHunter({})", id);
        try {
            hunterFacade.deleteHunter(id);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public final MushroomHunterDTO createHunter(@RequestBody MushroomHunterCreateDTO hunter) throws Exception {

        logger.debug("rest createHunter()");

        try {
            return hunterFacade.registerHunter(hunter);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            throw new ResourceAlreadyExistsException();
        }
    }



    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public final MushroomHunterDTO updateHunter(@RequestBody MushroomHunterUpdateDTO hunter) throws Exception {

        logger.debug("rest updateHunter()");

        try {
            return hunterFacade.updateHunter(hunter);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            throw new ResourceAlreadyExistsException();
        }
    }

}
