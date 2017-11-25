package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.ForestDTO;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.facade.ForestFacade;
import cz.muni.fi.pa165.mushrooms.service.BeanMappingService;
import cz.muni.fi.pa165.mushrooms.service.ForestService;
import cz.muni.fi.pa165.mushrooms.service.MushroomService;
import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import javax.inject.Inject;

/**
 * Tests for ForestFacadeImpl not using mock but relying on real service implementation
 *
 * @author bencikpeter
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class ForestFacadeImplNoMockTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Inject
    ForestService service;

    @Inject
    ForestFacade facade;

    @Inject
    BeanMappingService mappingService;

    private Forest forest1, forest2;
    private ForestDTO forestDTO1, forestDTO2;

    @Before
    public void setUp() {

        forest1 = new Forest();
        forest1.setName("Nice forest");
        forest1.setDescription("Nice forest in Slovakia");

        forest2 = new Forest();
        forest2.setName("Ugly forest");
        forest2.setDescription("Ugly forest in ugly country");

        service.createForest(forest1);
        service.createForest(forest2);

        assertThat(forest1.getId()).isNotNull();
        assertThat(forest2.getId()).isNotNull();

        forestDTO1 = mappingService.mapTo(forest1, ForestDTO.class);
        forestDTO2 = mappingService.mapTo(forest2, ForestDTO.class);
    }

    @Test
    public void findById(){
        assertThat(facade.findById(forest1.getId())).isEqualToComparingFieldByField(forestDTO1);
        assertThat(facade.findById(forest2.getId())).isEqualToComparingFieldByField(forestDTO2);
        assertThat(facade.findById(123L)).isNull();
    }

    @Test
    public void findByName(){
        assertThat(facade.findByName(forest1.getName())).isEqualToComparingFieldByField(forestDTO1);
        assertThat(facade.findByName(forest2.getName())).isEqualToComparingFieldByField(forestDTO2);
        assertThat(facade.findByName("unknown")).isNull();
    }

    @Test
    public void delete(){
        facade.deleteForest(forestDTO1.getId());
        assertThat(facade.findById(forestDTO1.getId())).isNull();
    }

    @Test
    public void update(){
        String desc = "New description";
        forestDTO1.setDescription(desc);
        facade.updateForest(forestDTO1);

        Forest updated = service.findForestById(forestDTO1.getId());
        assertThat(updated).isNotNull();
        assertThat(updated.getDescription()).isEqualTo(desc);
    }

    @Test
    public void create(){
        ForestDTO newForest = new ForestDTO();
        newForest.setDescription("Elves everywhere");
        newForest.setName("Lothlórien");

        assertThat(newForest.getId()).isNull();

        facade.createForest(newForest);

        assertThat(newForest.getId()).isNotNull();

        Forest forestEntity = service.findForestById(newForest.getId());
        assertThat(forestEntity).isNotNull();
        assertThat(forestEntity.getDescription()).isEqualTo(newForest.getDescription());
        assertThat(forestEntity.getName()).isEqualTo(newForest.getName());

    }

    //Functionality not implemented yet
    //@Test
    //public void listAllForestsWithMushroom(){}


}
