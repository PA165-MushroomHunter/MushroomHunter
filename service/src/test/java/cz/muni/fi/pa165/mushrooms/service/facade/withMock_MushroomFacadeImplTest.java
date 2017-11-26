package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.MushroomDTO;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;
import cz.muni.fi.pa165.mushrooms.service.BeanMappingService;
import cz.muni.fi.pa165.mushrooms.service.MushroomService;
import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import mockit.*;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Basic tests for mushroomFacade implementations using a mock of the service layer.
 *
 * @author bohdancvejn, Lindar84
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class withMock_MushroomFacadeImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    private Mushroom mushroom1;
    private Mushroom mushroom2;

    private MushroomDTO mushroom1DTO;
    private MushroomDTO mushroom2DTO;

    @Injectable
    private MushroomService service;
    @Inject @Tested
    private Mapper dozer;
    @Inject @Tested
    private BeanMappingService mapping;
    @Tested(fullyInitialized = true)
    private MushroomFacadeImpl facade;

    @Before
    public void setUp() {
        mushroom1 = new Mushroom();
        mushroom1.setName("Puffball");
        mushroom1.setType(MushroomType.EDIBLE);
        mushroom1.setIntervalOfOccurrence("January", "April");
        Deencapsulation.setField(mushroom1, "id", 1L);

        mushroom2 = new Mushroom();
        mushroom2.setName("Toadstool");
        mushroom2.setType(MushroomType.POISONOUS);
        mushroom2.setIntervalOfOccurrence("April", "September");
        Deencapsulation.setField(mushroom2, "id", 2L);

        mushroom1DTO = mapping.mapTo(mushroom1, MushroomDTO.class);
        mushroom2DTO = mapping.mapTo(mushroom2, MushroomDTO.class);

        new Expectations() {{
            service.findMushroomById(anyLong);
            result = new Delegate() {
                Mushroom foo(Long id) {
                    if (id.equals(1L)) return mushroom1;
                    if (id.equals(2L)) return mushroom2;
                    return null;
                }
            };
            minTimes = 0;

            service.findMushroomByName(anyString);
            result = new Delegate() {
                Mushroom foo(String name) {
                    if (name.equals("Puffball")) return mushroom1;
                    if (name.equals("Toadstool")) return mushroom2;
                    return null;
                }
            };
            minTimes = 0;

            service.findByIntervalOfOccurrence(anyString, anyString);
            result = new Delegate() {
                List<Mushroom> foo(String monthFrom, String monthTo) {
                    //always returns mushroom1, mushroom2
                    //correct list is not a point of this test

                    List<Mushroom> list = new ArrayList<>();
                    list.add(mushroom1);
                    list.add(mushroom2);
                    return list;
                }
            };
            minTimes = 0;

            service.findByMushroomType((MushroomType) any);
            result = new Delegate() {
                List<Mushroom> foo(MushroomType type) {
                    //always returns mushroom1, mushroom2
                    //correct list is not a point of this test

                    List<Mushroom> list = new ArrayList<>();
                    if (type.equals(MushroomType.EDIBLE)) {
                        list.add(mushroom1);
                    }
                    if (type.equals(MushroomType.POISONOUS)) {
                        list.add(mushroom2);
                    }

                    return list;
                }
            };
            minTimes = 0;

            service.findAllMushrooms();
            result = new Delegate() {
                List<Mushroom> foo() {
                    List<Mushroom> list = new ArrayList<>();
                    list.add(mushroom1);
                    list.add(mushroom2);
                    return list;
                }
            };
            minTimes = 0;

            service.deleteMushroom((Mushroom) any);
            result = new Delegate() {
                void foo(Mushroom mushroom) {
                    //no action performed
                }
            };
            minTimes = 0;

            service.updateMushroom((Mushroom) any);
            result = new Delegate() {
                void foo(Mushroom mushroom) {
                    //no action performed
                }
            };
            minTimes = 0;

            service.createMushroom((Mushroom) any);
            result = new Delegate() {
                void foo(Mushroom mushroom) {
                    //no action performed
                }
            };
            minTimes = 0;

        }};
    }

    @Test
    public void findAllMushroomsTest() {
        assertThat(facade.findAllMushrooms()).containsExactlyInAnyOrder(mushroom1DTO,mushroom2DTO);
    }

    @Test
    public void findMushroomByIdTest(){
        assertThat(facade.findMushroomById(1L)).isEqualToComparingFieldByField(mushroom1DTO);
        assertThat(facade.findMushroomById(2L)).isEqualToComparingFieldByField(mushroom2DTO);
        assertThat(facade.findMushroomById(123L)).isNull();
    }

    @Test
    public void findMushroomByNameTest(){
        assertThat(facade.findMushroomByName(mushroom1.getName())).isEqualToComparingFieldByField(mushroom1DTO);
        assertThat(facade.findMushroomByName(mushroom2.getName())).isEqualToComparingFieldByField(mushroom2DTO);
        assertThat(facade.findMushroomByName("lalala")).isNull();
    }

    @Test
    public void findMushroomByTypeTest(){
        assertThat(facade.findByMushroomType(mushroom1.getType())).containsExactly(mushroom1DTO);
        assertThat(facade.findByMushroomType(mushroom2.getType())).containsExactly(mushroom2DTO);
        assertThat(facade.findByMushroomType(MushroomType.UNEDIBLE)).isEmpty();
    }

    @Test
    public void findByMushroomTypeTest() {
        assertThatThrownBy(() -> facade.findByMushroomType(null)).isInstanceOf(IllegalArgumentException.class);
        MushroomDTO mushroomDTO = new MushroomDTO();
        mushroomDTO.setId(1L);
        assertThat(facade.findByMushroomType(MushroomType.EDIBLE)).containsExactlyInAnyOrder(mushroom1DTO);
    }

    @Test
    public void findByMushroomIntervalOfOccurrenceTest() {      ///// TODO String - Date
        assertThatThrownBy(() -> facade.findByIntervalOfOccurrence(null,null)).isInstanceOf(IllegalArgumentException.class);

        assertThat(facade.findByIntervalOfOccurrence(mushroom1.getIntervalOfOccurrence(),
                mushroom2.getIntervalOfOccurrence())).containsExactlyInAnyOrder(mushroom1DTO, mushroom2DTO);
    }

    //Delete, update, create not testable very well by mock - no access to database,
    //testing only error handling
    @Test
    public void deleteMushroomTest(){
        assertThatThrownBy(() -> facade.deleteMushroom(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateMushroomTest(){
        assertThatThrownBy(() -> facade.updateMushroom(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createMushroom(){

        assertThatThrownBy(() -> facade.createMushroom(null)).isInstanceOf(IllegalArgumentException.class);

        MushroomDTO createDTO1 = new MushroomDTO();
        createDTO1.setName(mushroom1.getName());
        createDTO1.setType(mushroom1.getType());
        createDTO1.setIntervalOfOccurrence(mushroom1.getIntervalOfOccurrence());
        createDTO1.setId(mushroom1.getId());

        assertThat(createDTO1).isEqualTo(mushroom1DTO);
    }

}