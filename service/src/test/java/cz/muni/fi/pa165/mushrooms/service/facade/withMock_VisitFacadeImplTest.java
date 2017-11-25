package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.*;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import cz.muni.fi.pa165.mushrooms.service.*;
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

    private Visit visit1;
    private Visit visit2;

    private VisitDTO visit1DTO;
    private VisitDTO visit2DTO;

    @Injectable
    private MushroomHunterService service;

    @Injectable
    private ForestService forestService;

    @Injectable
    private MushroomService mushroomService;

    @Injectable
    private VisitService visitService; // ak je ma sa injectnut

    @Inject @Tested // both annotations are necessary
    private Mapper dozer;
    @Inject @Tested
    private BeanMappingService mapping;

    @Tested(fullyInitialized = true)
    private MushroomHunterFacadeImpl hunterFacade;

    @Tested(fullyInitialized = true) // pouzivaj realnu impl tohoto
    private VisitFacadeImpl visitFacade;

    @Tested(fullyInitialized = true)
    private ForestFacadeImpl forestFacade;


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
        forest1.setName("Listnany");
        Deencapsulation.setField(forest1, "id", 1L);
        forest1DTO = mapping.mapTo(forest1, ForestDTO.class);

        forest2 = new Forest();
        forest2.setName("Ihlicnaty");
        Deencapsulation.setField(forest2, "id", 2L);
        forest2DTO = mapping.mapTo(forest2, ForestDTO.class);

        visit1 = new Visit();
        visit1.setDate(LocalDate.now());
        visit1.setHunter(hunter1);
        visit1.setForest(forest1);
        Deencapsulation.setField(visit1, "id", 1L);
        visit1DTO = mapping.mapTo(visit1, VisitDTO.class);

        visit2 = new Visit();
        visit2.setDate(LocalDate.now());
        visit2.setHunter(hunter2);
        visit2.setForest(forest2);
        Deencapsulation.setField(visit2, "id", 2L);
        visit2DTO = mapping.mapTo(visit2, VisitDTO.class);


        // anonymna trieda?

        new Expectations(){{
            service.authenticate((MushroomHunter) any, anyString);
            result = new Delegate() {
                boolean foo(MushroomHunter hunter, String pass) {
                    return hunter != null && pass != null && hunter.getPasswordHash().equals(pass);
                }
            }; minTimes = 0;

            service.findHunterById(anyLong);
            result = new Delegate() {
                MushroomHunter foo(Long id) {
                    System.err.println("looking for hunter with id " + id);
                    if (id.equals(1L)){
                        return hunter1;
                    }
                    if (id.equals(2L)){
                        return hunter2;
                    }
                    return null;
                }
            }; minTimes = 0;
/*******************************************************************************/
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

/*******************************************************************************/
            service.updateHunter((MushroomHunter) any);
            result = new Delegate(){
                void foo(){}
                // do nothing
            }; minTimes = 0;

            forestService.createForest((Forest) any);
            result = new Delegate() {
                void foo(Forest forest){
                    //no action performed
                }
            }; minTimes = 0;

            service.deleteHunter((MushroomHunter) any);
            result = new Delegate() {
                void foo(){}
                // do nothing
            }; minTimes = 0;

            service.findAllHunters();
            result = new Delegate(){
                List<MushroomHunter> foo(){
                    List<MushroomHunter> res = new ArrayList<>();
                    res.add(hunter1);
                    res.add(hunter2);
                    return res;
                }
            }; minTimes = 0;

            service.findHunterByNickname(anyString);
            result = new Delegate() {
                MushroomHunter foo(String nickname){
                    switch (nickname){
                        case "theGoodGuy":
                            return hunter1;
                        case "fullmetal":
                            return hunter2;
                        default:
                            return null;
                    }
                }
            }; minTimes = 0;

            service.isAdmin((MushroomHunter) any);
            result = new Delegate(){
                boolean foo(MushroomHunter hunter) {
                    return hunter != null && hunter.isAdmin();
                }
            }; minTimes = 0;

            service.registerHunter((MushroomHunter) any, anyString);
            result = new Delegate(){
                void foo (MushroomHunter hunter, String password){
                    hunter.setPasswordHash(password);
                }
            }; minTimes = 0;

            service.updatePassword((MushroomHunter) any, anyString, anyString);
            result = new Delegate(){
                boolean foo(MushroomHunter hunter, String oldPassword, String newPassword) {
                    if (!oldPassword.equals(hunter.getPasswordHash())) {
                        return false;
                    }
                    hunter.setPasswordHash(newPassword);
                    return true;
                }
            }; minTimes = 0;

        }};
    }

    @Test
    public void findVisitById() {
        assertThat(visitFacade.findById(1L)).isEqualToComparingFieldByField(visit1DTO);
        assertThat(visitFacade.findById(2L)).isEqualToComparingFieldByField(visit2DTO);
        assertThat(visitFacade.findById(3L)).isNull();
    }

    @Test
    public void createVisit() { // maybe use BeanMappingService here
        VisitCreateDTO createDTO1 = new VisitCreateDTO();

        forestFacade.createForest(forest1DTO);

        MushroomHunterCreateDTO createDTO2 = new MushroomHunterCreateDTO();
        createDTO2.setAdmin(hunter1.isAdmin());
        createDTO2.setFirstName(hunter1.getFirstName());
        createDTO2.setSurname(hunter1.getSurname());
        createDTO2.setPersonalInfo(hunter1.getPersonalInfo());
        createDTO2.setUserNickname(hunter1.getUserNickname());
        createDTO2.setUnencryptedPassword("armor");

        createDTO1.setForest(forest1DTO);
        createDTO1.setHunter(hunter1DTO);

        System.err.println(forest1.getId() + " " + hunter1.getId());

        assertThat(visitFacade.createVisit(createDTO1)).isEqualTo(visit1DTO);
    }
}