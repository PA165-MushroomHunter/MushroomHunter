package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.dao.MushroomHunterDao;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.*;

import static cz.muni.fi.pa165.mushrooms.service.TestUtils.checkMushroomHunterDuplicity;
import static cz.muni.fi.pa165.mushrooms.service.TestUtils.createHunter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test class for MushroomHunterService
 *
 * @author Buvko
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class MushroomHunterServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Injectable
    private MushroomHunterDao mushroomHunterDao;

    @Tested(fullyInitialized = true)
    private MushroomHunterServiceImpl service;

    private MushroomHunter nonPersistedHunter;
    private MushroomHunter persistedHunter;
    private MushroomHunter persistedHunter2;
    private MushroomHunter newHunter;

    private Map<Long, MushroomHunter> persistedMushroomHunter = new HashMap<>();
    // stores 'database' size
    private long dbCounter = 1;
    int persistedMushroomHunterPreTest;


    @Before
    public void setUp() {
        // note: for tests here, password hash and password are the same
        nonPersistedHunter = createHunter("Alphonse", "Elric", "theGoodGuy", "smt5udp", false);
        nonPersistedHunter.setPasswordHash("armor");

        persistedHunter = createHunter("Edward", "Elric", "fullmetal", "buw9fww", true);

        persistedHunter2 = createHunter("Vlad", "Third", "theImpaler", "thu3hhbm", false);

        new Expectations() {{
            mushroomHunterDao.create((MushroomHunter) any);
            result = new Delegate() {
                void foo(MushroomHunter mushroomHunter) {
                    if (mushroomHunter == null) {
                        throw new IllegalArgumentException("null mushroom hunter on creating");
                    }
                    if (mushroomHunter.getId() != null) {
                        throw new IllegalArgumentException("id already in db on creating mushroom hunter");
                    }
                    if (mushroomHunter.getFirstName() == null) {
                        throw new IllegalArgumentException("null first name on creating mushroom hunter");
                    }
                    if (mushroomHunter.getSurname() == null) {
                        throw new IllegalArgumentException("null surname on creating mushroom hunter");
                    }
                    if (mushroomHunter.getUserNickname() == null) {
                        throw new IllegalArgumentException("null nickname on creating mushroom hunter");
                    }
                    if (checkMushroomHunterDuplicity(persistedMushroomHunter, mushroomHunter)) {
                        throw new IllegalArgumentException("duplicate mushroom hunter on creating");
                    }

                    mushroomHunter.setId(dbCounter);
                    persistedMushroomHunter.put(dbCounter, mushroomHunter);
                    dbCounter++;
                }
            };
            minTimes = 0;

            mushroomHunterDao.findById(anyLong);
            result = new Delegate() {
                MushroomHunter foo(Long id) {
                    if (id == null) {
                        throw new IllegalArgumentException("null id");
                    }
                    return persistedMushroomHunter.get(id);
                }
            };
            minTimes = 0;

            mushroomHunterDao.findByNickname(anyString);
            result = new Delegate() {
                MushroomHunter foo(String nickname) {
                    if (nickname == null) {
                        throw new IllegalArgumentException("null id");
                    }
                    for (MushroomHunter hunter : persistedMushroomHunter.values()) {
                        if (hunter.getUserNickname().equals(nickname)) {
                            return hunter;
                        }
                    }
                    return null;
                }
            };
            minTimes = 0;

            mushroomHunterDao.findAll();
            result = new Delegate() {
                List<MushroomHunter> foo() {
                    return Collections.unmodifiableList(new ArrayList<>(persistedMushroomHunter.values()));
                }
            };
            minTimes = 0;


            mushroomHunterDao.delete((MushroomHunter) any);
            result = new Delegate() {
                void foo(MushroomHunter mushroomHunter) {
                    if (mushroomHunter == null || mushroomHunter.getId() == null) {
                        throw new IllegalArgumentException("invalid entity");
                    }
                    if (persistedMushroomHunter.get(mushroomHunter.getId()) == null) {
                        throw new IllegalArgumentException("not in db");
                    }
                    persistedMushroomHunter.remove(mushroomHunter.getId());
                }
            };
            minTimes = 0;

            mushroomHunterDao.update((MushroomHunter) any);
            result = new Delegate() {
                void foo(MushroomHunter mushroomHunter) {
                    if (mushroomHunter == null) {
                        throw new IllegalArgumentException("null mushroom hunter on mushroom hunter update");
                    }
                    if (mushroomHunter.getId() == null) {
                        throw new IllegalArgumentException("null id on mushroom hunter update");
                    }
                    if (mushroomHunter.getFirstName() == null) {
                        throw new IllegalArgumentException("null first name on mushroom hunter update");
                    }
                    if (mushroomHunter.getSurname() == null) {
                        throw new IllegalArgumentException("null surname on mushroom hunter update");
                    }
                    if (mushroomHunter.getUserNickname() == null) {
                        throw new IllegalArgumentException("null nickname on mushroom hunter update");
                    }
                    if (checkMushroomHunterDuplicity(persistedMushroomHunter, mushroomHunter)) {
                        throw new IllegalArgumentException("duplicating unique mushroom hunter attribute on updating");
                    }
                    persistedMushroomHunter.replace(mushroomHunter.getId(), mushroomHunter);
                }
            };
            minTimes = 0;

        }};

        mushroomHunterDao.create(persistedHunter);
        mushroomHunterDao.create(persistedHunter2);
        persistedMushroomHunterPreTest = persistedMushroomHunter.size();
    }

    @Test
    public void findById() throws Exception {
        assertThat(service.findHunterById(persistedHunter.getId())).isEqualTo(persistedHunter);
    }

    @Test
    public void findByNullId() throws Exception {
        assertThatThrownBy(() -> service.findHunterById(null)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void findWithNonExistingId() throws Exception {
        assertThat(service.findHunterById(-1L)).isNull();
    }

    @Test
    public void findAll() {
        assertThat(service.findAllHunters()).containsExactlyInAnyOrder(persistedHunter, persistedHunter2);
    }

    @Test
    public void findByNickname() throws Exception {
        String existingNickname = persistedHunter.getUserNickname();

        assertThat(service.findHunterByNickname(existingNickname)).isNotNull();
        assertThat(service.findHunterByNickname(existingNickname)).isEqualTo(persistedHunter);
    }

    @Test
    public void findByNullNickname() throws Exception {
        assertThatThrownBy(() -> service.findHunterByNickname(null)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void findByNonExistingNickname() {
        String nonPersistedNickname = "nickname";
        assertThat(service.findHunterByNickname(nonPersistedNickname)).isNull();
    }

    @Test
    public void create() {
        newHunter = createHunter("Jan", "Jakub", "J2J", "95gh3kew", false);
        int oldSize = persistedMushroomHunter.size();

        service.registerHunter(newHunter, newHunter.getPasswordHash());
        assertThat(persistedMushroomHunter.size()).isEqualTo(oldSize + 1);
    }

    @Test
    public void createNull() throws Exception {
        assertThatThrownBy(() -> service.registerHunter(null, "abcd")).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void createNullFirstName() throws Exception {
        newHunter = createHunter(null, "Jakub", "J2J", "95gh3kew", false);
        assertThatThrownBy(() -> service.registerHunter(newHunter, newHunter.getPasswordHash())).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void createNullLSurname() throws Exception {
        newHunter = createHunter("Jan", null, "J2J", "95gh3kew", false);
        assertThatThrownBy(() -> service.registerHunter(newHunter, newHunter.getPasswordHash())).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void createNullNickname() throws Exception {
        newHunter = createHunter("Jan", "Jakub", null, "95gh3kew", false);
        assertThatThrownBy(() -> service.registerHunter(newHunter, newHunter.getPasswordHash())).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void createDuplicate() throws Exception {
        assertThatThrownBy(() -> service.registerHunter(persistedHunter, persistedHunter.getPasswordHash())).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void delete() throws Exception {
        assertThat(persistedMushroomHunter.values()).containsExactlyInAnyOrder(persistedHunter, persistedHunter2);
        service.deleteHunter(persistedHunter);
        assertThat(persistedMushroomHunter.values()).containsExactlyInAnyOrder(persistedHunter2);
    }

    @Test
    public void deleteNonExisting() throws Exception {
        assertThat(service.findAllHunters()).doesNotContain(nonPersistedHunter);
        assertThatThrownBy(() -> service.deleteHunter(nonPersistedHunter)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void updateNickname() throws Exception {
        String newUpdateValue = "New Nickname";

        assertThat(persistedHunter.getUserNickname()).isNotEqualTo(newUpdateValue);

        persistedHunter.setUserNickname(newUpdateValue);
        service.updateHunter(persistedHunter);

        assertThat(persistedHunter.getUserNickname()).isEqualTo(newUpdateValue);
    }

    @Test
    public void updateInfo() throws Exception {
        String newUpdateValue = "New Info";

        assertThat(persistedHunter.getPersonalInfo()).isNotEqualTo(newUpdateValue);

        persistedHunter.setPersonalInfo(newUpdateValue);
        service.updateHunter(persistedHunter);

        assertThat(persistedHunter.getPersonalInfo()).isEqualTo(newUpdateValue);
    }

    @Test
    public void updateNicknameToNull() throws Exception {
        String newUpdateValue = null;
        persistedHunter.setUserNickname(newUpdateValue);

        assertThatThrownBy(() -> service.updateHunter(persistedHunter)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void updateToNicknameDuplicate() throws Exception {
        String newUpdateValue = persistedHunter2.getUserNickname();
        persistedHunter.setUserNickname(newUpdateValue);

        assertThatThrownBy(() -> service.updateHunter(persistedHunter)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void updateFirstNameToNull() throws Exception {
        String newUpdateValue = null;
        persistedHunter.setFirstName(newUpdateValue);

        assertThatThrownBy(() -> service.updateHunter(persistedHunter)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void updateSurnameToNull() throws Exception {
        String newUpdateValue = null;
        persistedHunter.setSurname(newUpdateValue);

        assertThatThrownBy(() -> service.updateHunter(persistedHunter)).isInstanceOf(DataAccessException.class);
    }


    @Test
    public void updatePassword() {
        String oldPassword = "skrra8pa";
        String newPassword = "ka2kapum";
        newHunter = createHunter("Jan", "Jakub", "BMPRS", oldPassword, false);
        service.registerHunter(newHunter, newHunter.getPasswordHash());

        assertThat(service.updatePassword(newHunter, oldPassword, newPassword)).isTrue();
    }

    @Test
    public void updatePasswordWithIncorrectOldPassword() {
        String oldPassword = "skrra8pa";
        String newPassword = "ka2kapum";
        newHunter = createHunter("Jan", "Jakub", "BMPRS", oldPassword, false);
        service.registerHunter(newHunter, newHunter.getPasswordHash());

        assertThat(service.updatePassword(newHunter, "wrongOldPassword", newPassword)).isFalse();
    }

    @Test
    public void updatePasswordToNull() throws Exception {
        String password = "skrra8pa";
        newHunter = createHunter("Jan", "Jakub", "BMPRS", password, false);
        service.registerHunter(newHunter, newHunter.getPasswordHash());

        assertThatThrownBy(() -> service.updatePassword(newHunter, password, null)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void authenticate() {
        String password = "95gh3kew";
        newHunter = createHunter("Jan", "Jakub", "BMPRS", password, false);
        service.registerHunter(newHunter, newHunter.getPasswordHash());

        assertThat(service.authenticate(newHunter, password)).isTrue();
    }

    @Test
    public void invalidAuthenticateWithWrongPassword() {
        String password = "95gh3kew";
        newHunter = createHunter("Jan", "Jakub", "BMPRS", password, false);
        service.registerHunter(newHunter, newHunter.getPasswordHash());

        assertThat(service.authenticate(newHunter, "wrongPassword")).isFalse();
    }

    @Test
    public void authenticateWithNullPassword() throws Exception {
        assertThat(service.authenticate(persistedHunter, null)).isFalse();
    }

    @Test
    public void adminCheckOnAdmin() throws Exception {
        assertThat(service.isAdmin(persistedHunter)).isTrue();
    }

    @Test
    public void adminCheckOnNonAdmin() throws Exception {
        newHunter = createHunter("Jan", "Jakub", "J2J", "95gh3kew", false);
        mushroomHunterDao.create(newHunter);
        assertThat(service.isAdmin(newHunter)).isFalse();
    }

}
