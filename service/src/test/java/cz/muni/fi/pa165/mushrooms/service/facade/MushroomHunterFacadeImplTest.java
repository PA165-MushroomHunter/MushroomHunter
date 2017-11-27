package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterAuthenticateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterCreateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterUpdateDTO;
import cz.muni.fi.pa165.mushrooms.dto.MushroomHunterUpdatePasswordDTO;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.facade.MushroomHunterFacade;
import cz.muni.fi.pa165.mushrooms.service.BeanMappingService;
import cz.muni.fi.pa165.mushrooms.service.MushroomHunterService;
import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

/**
 * Tests for facade functionality without mock - using the real service implementation.
 *
 * @author bkompis
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class MushroomHunterFacadeImplTest extends AbstractTransactionalJUnit4SpringContextTests {
    // relies on correct implementation of service
    // used in setup
    @Inject
    MushroomHunterService service;
    @Inject
    MushroomHunterFacade facade;
    @Inject
    private BeanMappingService mapping;

    private MushroomHunter hunter1;
    private MushroomHunter hunter2;

    private MushroomHunterDTO hunter1DTO;
    private MushroomHunterDTO hunter2DTO;

    private static MushroomHunter createMushroomHunter(String firstName, String surname, String userNickname) {
        MushroomHunter hunter = new MushroomHunter();
        hunter.setFirstName(firstName);
        hunter.setSurname(surname);
        hunter.setUserNickname(userNickname);
        hunter.setPersonalInfo("Hunter " + userNickname + " - " + firstName + " " + surname);
        return hunter;
    }

    @Before
    public void setUp() {
        // hunters don't have an ID set in this setup
        // note: for tests here, password hash and password are the same
        hunter1 = createMushroomHunter("Alphonse", "Elric", "theGoodGuy");
        service.registerHunter(hunter1, "armor");
        assertNotNull(hunter1.getId());
        hunter2 = createMushroomHunter("Edward", "Elric", "fullmetal");
        hunter2.setAdmin(true);
        service.registerHunter(hunter2, "winry");
       assertNotNull(hunter2.getId());

        hunter1DTO = mapping.mapTo(hunter1, MushroomHunterDTO.class);
        assertNotNull(hunter1DTO);
        hunter2DTO = mapping.mapTo(hunter2, MushroomHunterDTO.class);
        assertNotNull(hunter2DTO);
    }

    @Test
    public void findHunterById() {
        assertThat(facade.findHunterById(hunter1.getId())).isEqualToComparingFieldByField(hunter1DTO);
        assertThat(facade.findHunterById(hunter2.getId())).isEqualToComparingFieldByField(hunter2DTO);
        assertThat(facade.findHunterById(243L)).isNull();
    }

    @Test
    public void findHunterByNickname() {
        assertThat(facade.findHunterByNickname("theGoodGuy")).isEqualToComparingFieldByField(hunter1DTO);
        assertThat(facade.findHunterByNickname("fullmetal")).isEqualToComparingFieldByField(hunter2DTO);
        assertThat(facade.findHunterByNickname("foo")).isNull();
    }

    @Test
    public void registerHunter() {
        MushroomHunterCreateDTO createDTO1 = new MushroomHunterCreateDTO();
        createDTO1.setAdmin(true);
        createDTO1.setFirstName("Winry");
        createDTO1.setSurname("Rockbell");
        createDTO1.setPersonalInfo("It's me!");
        createDTO1.setUserNickname("mechanic");
        createDTO1.setUnencryptedPassword("automail");

        MushroomHunterDTO expected = new MushroomHunterDTO();
        expected.setAdmin(true);
        expected.setFirstName("Winry");
        expected.setSurname("Rockbell");
        expected.setPersonalInfo("It's me!");
        expected.setUserNickname("mechanic");

        assertThat(facade.registerHunter(createDTO1)).isEqualTo(expected);
    }

    @Test
    public void deleteHunter() { // meh tests
        assertThat(facade.deleteHunter(hunter1.getId())).isTrue();
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
    }

    @Test
    public void findAllHunters() {
        assertThat(facade.findAllHunters()).containsExactlyInAnyOrder(hunter1DTO, hunter2DTO);
    }

    @Test
    public void authenticate() {
        MushroomHunterAuthenticateDTO auth = new MushroomHunterAuthenticateDTO();
        auth.setNickname(hunter1.getUserNickname());
        auth.setPassword("armor");
        assertThat(facade.authenticate(auth)).isTrue();
    }

    @Test
    public void isAdmin() {
        assertThat(facade.isAdmin(hunter1DTO)).isFalse();
        assertThat(facade.isAdmin(hunter2DTO)).isTrue();
    }

}