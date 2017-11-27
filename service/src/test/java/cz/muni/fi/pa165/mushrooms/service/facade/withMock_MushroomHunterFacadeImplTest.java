package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterAuthenticateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterCreateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterUpdateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterUpdatePasswordDTO;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.service.BeanMappingService;
import cz.muni.fi.pa165.mushrooms.service.MushroomHunterService;
import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import mockit.Deencapsulation;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Basic tests for facade implementations using a mock of the service layer.
 *
 * @author bkompis
 */
//TODO: ex throwing lower; DataAccessException (from facade impl)
// TODO: constraints, exceptions in javadoc
// TODO: equals()in DTOs vs. equals on all attributes
// TODO: exception handling
// TODO: more tests, + tests without mock (integration)
@ContextConfiguration(classes = ServiceConfiguration.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class) // TODO: necessary?
public class withMock_MushroomHunterFacadeImplTest extends AbstractTransactionalJUnit4SpringContextTests { //should be transactional?

    private final Logger logger = LoggerFactory.getLogger(withMock_MushroomHunterFacadeImplTest.class);

    private MushroomHunter hunter1;
    private MushroomHunter hunter2;

    private MushroomHunterDTO hunter1DTO;
    private MushroomHunterDTO hunter2DTO;

    @Injectable
    private MushroomHunterService service;
    @Inject @Tested // both annotations are necessary
    private Mapper dozer;
    @Inject @Tested
    private BeanMappingService mapping;
    @Tested(fullyInitialized = true)
    private MushroomHunterFacadeImpl facade;


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
        hunter2 = createMushroomHunter("Edward", "Elric", "fullmetal");
        hunter2.setPasswordHash("winry");
        Deencapsulation.setField(hunter2, "id", 2L);
        assertThat(hunter2.getId()).isEqualTo(2L);
        hunter2.setAdmin(true);

        hunter1DTO = mapping.mapTo(hunter1, MushroomHunterDTO.class);
        hunter2DTO = mapping.mapTo(hunter2, MushroomHunterDTO.class);

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
                    if (id.equals(1L)){
                        return hunter1;
                    }
                    if (id.equals(2L)){
                        return hunter2;
                    }
                    return null;
                }
            }; minTimes = 0;

            service.updateHunter((MushroomHunter) any);
            result = new Delegate(){
                void foo(){}
                // do nothing
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
    public void findHunterById() {
        assertThat(facade.findHunterById(1L)).isEqualToComparingFieldByField(hunter1DTO);
        assertThat(facade.findHunterById(2L)).isEqualToComparingFieldByField(hunter2DTO);
        assertThat(facade.findHunterById(243L)).isNull();
    }

    @Test
    public void findHunterByNickname() {
        assertThat(facade.findHunterByNickname("theGoodGuy")).isEqualToComparingFieldByField(hunter1DTO);
        assertThat(facade.findHunterByNickname("fullmetal")).isEqualToComparingFieldByField(hunter2DTO);
        assertThat(facade.findHunterByNickname("foo")).isNull();
    }

    @Test
    public void registerHunter() { // maybe use BeanMappingService here
        MushroomHunterCreateDTO createDTO1 = new MushroomHunterCreateDTO();
        createDTO1.setAdmin(hunter1.isAdmin());
        createDTO1.setFirstName(hunter1.getFirstName());
        createDTO1.setSurname(hunter1.getSurname());
        createDTO1.setPersonalInfo(hunter1.getPersonalInfo());
        createDTO1.setUserNickname(hunter1.getUserNickname());
        createDTO1.setUnencryptedPassword("armor");

        assertThat(facade.registerHunter(createDTO1)).isEqualTo(hunter1DTO);
    }

    @Test
    public void deleteHunter() { // meh tests
        assertThat(facade.deleteHunter(1L)).isTrue();
        assertThatThrownBy(() -> facade.deleteHunter(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateHunter() {
        MushroomHunterUpdateDTO update = new MushroomHunterUpdateDTO();
        update.setAdmin(true);
        update.setFirstName("Haganeno");
        update.setSurname("Renkinjitsushi");
        update.setId(hunter1.getId());
        update.setPersonalInfo("Nanii?");
        update.setUserNickname("chibi");

        MushroomHunterDTO updated = facade.updateHunter(update);
        assertThat(updated.getId()).isEqualTo(hunter1.getId());
        assertThat(updated.getUserNickname()).isEqualTo("chibi");
        assertThat(updated.getPersonalInfo()).isEqualTo("Nanii?");
        assertThat(updated.getSurname()).isEqualTo("Renkinjitsushi");
        assertThat(updated.getFirstName()).isEqualTo("Haganeno");
        assertThat(updated.isAdmin()).isTrue();

    }

    @Test
    public void updatePassword() {
        MushroomHunterUpdatePasswordDTO update = new MushroomHunterUpdatePasswordDTO();
        update.setNewPassword("new");
        update.setOldPassword(hunter1.getPasswordHash());
        update.setId(hunter1.getId());

        MushroomHunterDTO updated = facade.updatePassword(update);
        assertThat(updated.getId()).isEqualTo(hunter1.getId());
        //assertThat(updated.getPasswordHash()).isEqualTo("new");
    }

    @Test
    public void findAllHunters() {
        assertThat(facade.findAllHunters()).containsExactlyInAnyOrder(hunter1DTO, hunter2DTO);
    }

    @Test
    public void authenticate() {
        MushroomHunterAuthenticateDTO auth = new MushroomHunterAuthenticateDTO();
        auth.setNickname(hunter1.getUserNickname());
        auth.setPassword(hunter1.getPasswordHash());
        assertThat(facade.authenticate(auth)).isTrue();
    }

    @Test
    public void isAdmin() {
        assertThat(facade.isAdmin(hunter1DTO)).isFalse();
        assertThat(facade.isAdmin(hunter2DTO)).isTrue();
    }

}