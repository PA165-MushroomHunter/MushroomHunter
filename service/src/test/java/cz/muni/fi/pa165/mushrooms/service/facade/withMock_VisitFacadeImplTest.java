package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.*;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;
import cz.muni.fi.pa165.mushrooms.service.BeanMappingService;
import cz.muni.fi.pa165.mushrooms.service.ForestService;
import cz.muni.fi.pa165.mushrooms.service.MushroomHunterService;
import cz.muni.fi.pa165.mushrooms.service.MushroomService;
import cz.muni.fi.pa165.mushrooms.service.VisitService;
import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import mockit.Deencapsulation;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Basic tests for hunterFacade implementations using a mock of the service layer.
 *
 * @author bkompis
 */
//TODO: ex throwing lower; DataAccessException (from hunterFacade impl)
// TODO: constraints, exceptions in javadoc
// TODO: equals()in DTOs vs. equals on all attributes
// TODO: exception handling
// TODO: more tests, + tests without mock (integration)
@ContextConfiguration(classes = ServiceConfiguration.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class) // TODO: necessary?
public class withMock_VisitFacadeImplTest extends AbstractTransactionalJUnit4SpringContextTests { //should be transactional?

    //private final Logger logger = LoggerFactory.getLogger(MushroomHunterFacadeImplTest_withMock.class);

    private MushroomHunter hunter1;
    private MushroomHunter hunter2;

    private MushroomHunterDTO hunter1DTO;
    private MushroomHunterDTO hunter2DTO;

    private Forest forest1;
    private Forest forest2;

    private ForestDTO forest1DTO;
    private ForestDTO forest2DTO;

    private Mushroom mushroom1;
    private Mushroom mushroom2;

    private MushroomDTO mushroom1DTO;
    private MushroomDTO mushroom2DTO;

    private Visit visit1;
    private Visit visit2;

    private VisitDTO visit1DTO;
    private VisitDTO visit2DTO;

    @Injectable
    private VisitService visitService;

    @Inject @Tested
    private Mapper dozer;
    @Inject @Tested
    private BeanMappingService mapping;

    @Tested(fullyInitialized = true)
    private VisitFacadeImpl visitFacade;

    private static MushroomHunter createMushroomHunter(String firstName, String surname, String userNickname) {
        MushroomHunter hunter = new MushroomHunter();
        hunter.setFirstName(firstName);
        hunter.setSurname(surname);
        hunter.setUserNickname(userNickname);
        hunter.setPersonalInfo("Mushroom hunter " + userNickname + " - " + firstName + " " + surname);
        return hunter;
    }

    @Before
    public void setUp(){
        // note: for tests here, password hash and password are the same
        hunter1 = createMushroomHunter("Alphonse", "Elric", "theGoodGuy");
        hunter1.setPasswordHash("armor");
        Deencapsulation.setField(hunter1, "id", 1L);
        assertThat(hunter1.getId()).isEqualTo(1L);
        hunter1DTO = mapping.mapTo(hunter1, MushroomHunterDTO.class);

        hunter2 = createMushroomHunter("Edward", "Elric", "fullmetal");
        hunter2.setPasswordHash("winry");
        Deencapsulation.setField(hunter2, "id", 2L);
        assertThat(hunter2.getId()).isEqualTo(2L);
        hunter2.setAdmin(true);
        hunter2DTO = mapping.mapTo(hunter2, MushroomHunterDTO.class);

        forest1 = new Forest();
        forest1.setName("Listnaty");
        Deencapsulation.setField(forest1, "id", 1L);
        forest1DTO = mapping.mapTo(forest1, ForestDTO.class);

        forest2 = new Forest();
        forest2.setName("Ihlicnaty");
        Deencapsulation.setField(forest2, "id", 2L);
        forest2DTO = mapping.mapTo(forest2, ForestDTO.class);

        mushroom1 = new Mushroom();
        mushroom1.setName("Hrib Dubovy");
        mushroom1.setType(MushroomType.EDIBLE);
        mushroom1.setIntervalOfOccurrence("June","July");
        Deencapsulation.setField(mushroom1, "id", 1L);
        mushroom1DTO = mapping.mapTo(mushroom1, MushroomDTO.class);

        mushroom2 = new Mushroom();
        mushroom2.setName("Hrib Satan");
        mushroom2.setType(MushroomType.POISONOUS);
        mushroom2.setIntervalOfOccurrence("July","September");
        Deencapsulation.setField(mushroom2, "id", 2L);
        mushroom2DTO = mapping.mapTo(mushroom2, MushroomDTO.class);

        visit1 = new Visit();
        visit1.setDate(LocalDate.now());
        visit1.setHunter(hunter1);
        visit1.setForest(forest1);
        visit1.setMushrooms(new ArrayList<Mushroom>());
        Deencapsulation.setField(visit1, "id", 1L);
        visit1DTO = mapping.mapTo(visit1, VisitDTO.class);

        visit2 = new Visit();
        visit2.setDate(LocalDate.now());
        visit2.setHunter(hunter2);
        visit2.setForest(forest2);
        Deencapsulation.setField(visit2, "id", 2L);
        visit2DTO = mapping.mapTo(visit2, VisitDTO.class);

        new Expectations(){{
            visitService.findVisitById(anyLong);
            result = new Delegate() {
                Visit foo(Long id) {
                    System.err.println("looking for visit with id " + id);
                    if (id.equals(1L)){
                        return visit1;
                    }
                    if (id.equals(2L)){
                        return visit2;
                    }
                    return null;
                }
            }; minTimes = 0;

            visitService.createVisit((Visit) any);
            result = new Delegate(){
                void foo (){
                }
            }; minTimes = 0;

            visitService.deleteVisit((Visit) any);
            result = new Delegate() {
                void foo(){}
                // do nothing
            }; minTimes = 0;

            visitService.updateVisit((Visit) any);
            result = new Delegate() {
                void foo(){}
                // do nothing
            }; minTimes = 0;

            visitService.findAllVisits();
            result = new Delegate(){
                List<Visit> foo(){
                    List<Visit> res = new ArrayList<>();
                    res.add(visit1);
                    res.add(visit2);
                    return res;
                }
            }; minTimes = 0;

            visitService.getVisitsByHunter((MushroomHunter) any);
            result = new Delegate() {
                List<Visit> foo(MushroomHunter hunter){
                    if (hunter.getUserNickname().equals("theGoodGuy")){
                        List<Visit> visitsByHunter1 =  new ArrayList<>();
                        visitsByHunter1.add(visit1);
                        return visitsByHunter1;
                    }
                    else if (hunter.getUserNickname().equals("fullmetal")){
                        List<Visit> visitsByHunter2 =  new ArrayList<>();
                        visitsByHunter2.add(visit2);
                        return visitsByHunter2;
                    }
                    else
                        return null;
                }
            }; minTimes = 0;

            visitService.getVisitsByForest((Forest) any);
            result = new Delegate() {
                List<Visit> foo(Forest forest){
                    if (forest.getName().equals("Listnaty")){
                        List<Visit> visitsByForest1 =  new ArrayList<>();
                        visitsByForest1.add(visit1);
                        return visitsByForest1;
                    }
                    else if (forest.getName().equals("Ihlicnaty")){
                        List<Visit> visitsByForest2 =  new ArrayList<>();
                        visitsByForest2.add(visit2);
                        return visitsByForest2;
                    }
                    else
                        return null;
                }
            }; minTimes = 0;

            visitService.getVisitsByMushroom((Mushroom) any);
            result = new Delegate() {
                List<Visit> foo(Mushroom mushroom){
                    if (mushroom.getName().equals("Hrib Dubovy")){
                        List<Visit> visitsByHunter1 =  new ArrayList<>();
                        visitsByHunter1.add(visit1);
                        return visitsByHunter1;
                    }
                    else if (mushroom.getName().equals("Hrib Satan")){
                        List<Visit> visitsByHunter2 =  new ArrayList<>();
                        visitsByHunter2.add(visit2);
                        return visitsByHunter2;
                    }
                    else
                        return null;
                }
            }; minTimes = 0;

        }};
    }

    @Test
    public void findVisitsByHunter() {
        assertThat(visitFacade.listAllVisitsForMushroomHunter(hunter1DTO)).containsExactlyInAnyOrder(visit1DTO);
        assertThat(visitFacade.listAllVisitsForMushroomHunter(hunter2DTO)).containsExactlyInAnyOrder(visit2DTO);
    }

    @Test
    public void findVisitsByForest() {
        assertThat(visitFacade.listAllVisitsForForest(forest1DTO)).containsExactlyInAnyOrder(visit1DTO);
        assertThat(visitFacade.listAllVisitsForForest(forest2DTO)).containsExactlyInAnyOrder(visit2DTO);
    }

    @Test
    public void findVisitsByMushroom() {
        assertThat(visitFacade.listAllVisitsByMushroom(mushroom1DTO)).containsExactlyInAnyOrder(visit1DTO);
        assertThat(visitFacade.listAllVisitsByMushroom(mushroom2DTO)).containsExactlyInAnyOrder(visit2DTO);
    }

    @Test
    public void findVisitById() {
        assertThat(visitFacade.findById(1L)).isEqualToComparingFieldByField(visit1DTO);
        assertThat(visitFacade.findById(2L)).isEqualToComparingFieldByField(visit2DTO);
        assertThat(visitFacade.findById(3L)).isNull();
    }

    @Test
    public void findAllVisits() {
        assertThat(visitFacade.listAllVisits()).containsExactlyInAnyOrder(visit1DTO,visit2DTO);
    }

    @Test
    public void deleteVisit() {
        assertThatThrownBy(() -> visitFacade.deleteVisit(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateVisit() {
        assertThatThrownBy(() -> visitFacade.updateVisit(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createVisit() {
        assertThatThrownBy(() -> visitFacade.createVisit(null)).isInstanceOf(IllegalArgumentException.class);
    }
}