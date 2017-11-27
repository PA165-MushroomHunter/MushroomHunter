package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.dao.MushroomHunterDao;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import cz.muni.fi.pa165.mushrooms.service.exceptions.EntityFindServiceException;
import cz.muni.fi.pa165.mushrooms.service.exceptions.EntityOperationServiceException;
import cz.muni.fi.pa165.mushrooms.validation.PersistenceSampleApplicationContext;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.*;

import static cz.muni.fi.pa165.mushrooms.service.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for MushroomHunterService
 *
 * @author Buvko
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class MushroomHunterServiceTest extends AbstractTransactionalJUnit4SpringContextTests  {

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
        nonPersistedHunter = createHunter("Alphonse", "Elric", "theGoodGuy" , "smt5udp", false);
        nonPersistedHunter.setPasswordHash("armor");

        persistedHunter = createHunter("Edward", "Elric", "fullmetal", "buw9fww", true);
        persistedHunter.setPasswordHash("winry");

        persistedHunter2 = createHunter("Vlad", "Third", "theImpaler", "thu3hhbm",false);
        persistedHunter2.setPasswordHash("badGuy");

        new Expectations() {{
            mushroomHunterDao.create((MushroomHunter) any);
            result = new Delegate() {
                void foo(MushroomHunter mushroomHunter) {
                    if (mushroomHunter == null){
                        throw new IllegalArgumentException("null mushroom hunter on creating");
                    }
                    if (mushroomHunter.getId() != null){
                        throw new IllegalArgumentException("id already in db on creating mushroom hunter");
                    }
                    if (mushroomHunter.getFirstName() == null){
                        throw new IllegalArgumentException("null first name on creating mushroom hunter");
                    }
                    if (mushroomHunter.getSurname() == null){
                        throw new IllegalArgumentException("null surname on creating mushroom hunter");
                    }
                    if (mushroomHunter.getUserNickname() == null){
                        throw new IllegalArgumentException("null nickname on creating mushroom hunter");
                    }
                    if (checkMushroomHunterDuplicity(persistedMushroomHunter, mushroomHunter)){
                        throw new IllegalArgumentException("duplicate mushroom hunter on creating");
                    }

                    mushroomHunter.setId(dbCounter);
                    persistedMushroomHunter.put(dbCounter, mushroomHunter);
                    dbCounter ++;
                }
            };
            minTimes = 0;

            mushroomHunterDao.findById(anyLong);
            result = new Delegate() {
                MushroomHunter foo(Long id) {
                    if (id == null){
                        throw new IllegalArgumentException("null id");
                    }
                    return persistedMushroomHunter.get(id);
                }
            };
            minTimes = 0;

            mushroomHunterDao.findByNickname(anyString);
            result = new Delegate() {
                MushroomHunter foo(String nickname) {
                    if (nickname == null){
                        throw new IllegalArgumentException("null id");
                    }
                    for (MushroomHunter hunter: persistedMushroomHunter.values()) {
                        if (hunter.getUserNickname().equals(nickname)){
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
                    if (mushroomHunter == null || mushroomHunter.getId() == null ){
                        throw new IllegalArgumentException("invalid entity");
                    }
                    if (persistedMushroomHunter.get(mushroomHunter.getId()) == null){
                        throw new IllegalArgumentException("not in db");
                    }
                    persistedMushroomHunter.remove(mushroomHunter.getId());
                }
            };
            minTimes = 0;

            mushroomHunterDao.update((MushroomHunter) any);
            result = new Delegate() {
                void foo(MushroomHunter mushroomHunter) {
                    if (mushroomHunter == null){
                        throw new IllegalArgumentException("null mushroom hunter on mushroom hunter update");
                    }
                    if (mushroomHunter.getId() == null){
                        throw new IllegalArgumentException("null id on mushroom hunter update");
                    }
                    if (mushroomHunter.getFirstName() == null){
                        throw new IllegalArgumentException("null first name on mushroom hunter update");
                    }
                    if (mushroomHunter.getSurname() == null){
                        throw new IllegalArgumentException("null surname on mushroom hunter update");
                    }
                    if (mushroomHunter.getUserNickname() == null){
                        throw new IllegalArgumentException("null nickname on mushroom hunter update");
                    }
                    if (checkMushroomHunterDuplicity(persistedMushroomHunter, mushroomHunter)){
                        throw new IllegalArgumentException("duplicating unique mushroom hunter attribute on updating");
                    }
                    persistedMushroomHunter.replace(mushroomHunter.getId(), mushroomHunter);
                }
            };
            minTimes = 0;

        }};

        mushroomHunterDao.create(persistedHunter);
        mushroomHunterDao.create(persistedHunter2);
        persistedMushroomHunterPreTest =  persistedMushroomHunter.size();
    }

    @Test
    public void findMushroomHunterById() throws Exception {
        assertThat(service.findHunterById(persistedHunter.getId())).isEqualTo(persistedHunter);
    }

    @Test
    public void findMushroomHunterByNullId() throws Exception {
        assertThatThrownBy(() -> service.findHunterById(null)).isInstanceOf(EntityFindServiceException.class);
    }

    @Test
    public void findMushroomHunterWithNonExistingId() throws Exception {
        assertThat(service.findHunterById(-1L)).isNull();
    }

    @Test
    public void findAllMushroomHunters() {
        assertThat(service.findAllHunters()).containsExactlyInAnyOrder(persistedHunter,persistedHunter2);
    }

    @Test
    public void findMushroomHunterByNickname() throws Exception {
        String existingNickname = persistedHunter.getUserNickname();

        assertThat(service.findHunterByNickname(existingNickname)).isNotNull();
        assertThat(service.findHunterByNickname(existingNickname)).isEqualTo(persistedHunter);
    }

    @Test
    public void findByMushroomHunterNullNickname() throws Exception {
        assertThatThrownBy(() -> service.findHunterByNickname(null)).isInstanceOf(EntityFindServiceException.class);
    }

    @Test
    public void findByMushroomHunterNonExistingNickname() {
        String nonPersistedNickname = "nickname";
        assertThat(service.findHunterByNickname(nonPersistedNickname)).isNull();
    }

    @Test
    public void createMushroomHunter() {
        newHunter = createHunter("Jan", "Jakub", "J2J", "95gh3kew", false);
        int oldSize = persistedMushroomHunter.size();

        service.registerHunter(newHunter, newHunter.getPasswordHash());
        assertThat(persistedMushroomHunter.size()).isEqualTo(oldSize + 1);
    }

    @Test
    public void createNullMushroomHunter() throws Exception {
        assertThatThrownBy(() -> service.registerHunter(null, "abcd")).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    public void createNullFirstNameMushroomHunter() throws Exception {
        newHunter = createHunter(null, "Jakub", "J2J", "95gh3kew", false);
        assertThatThrownBy(() -> service.registerHunter(newHunter, newHunter.getPasswordHash())).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    public void createNullLSurnameMushroomHunter() throws Exception {
        newHunter = createHunter("Jan", null, "J2J", "95gh3kew", false);
        assertThatThrownBy(() -> service.registerHunter(newHunter, newHunter.getPasswordHash())).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    public void createNullNicknameMushroomHunter() throws Exception {
        newHunter = createHunter("Jan", "Jakub", null, "95gh3kew", false);
        assertThatThrownBy(() -> service.registerHunter(newHunter, newHunter.getPasswordHash())).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    public void createDuplicateMushroomHunter() throws Exception {
        assertThatThrownBy(() -> service.registerHunter(persistedHunter, persistedHunter.getPasswordHash())).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    public void deleteMushroomHunter() throws Exception {
        assertThat(persistedMushroomHunter.values()).containsExactlyInAnyOrder(persistedHunter,persistedHunter2);
        service.deleteHunter(persistedHunter);
        assertThat(persistedMushroomHunter.values()).containsExactlyInAnyOrder(persistedHunter2);
    }

    @Test
    public void deleteNonExistingMushroomHunter() throws Exception {
        assertThat(service.findAllHunters()).doesNotContain(nonPersistedHunter);
        assertThatThrownBy(() -> service.deleteHunter(nonPersistedHunter)).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    public void updateMushroomHuntersNickname() throws Exception {
        String newUpdateValue = "New Nickname";

        assertThat(persistedHunter.getUserNickname()).isNotEqualTo(newUpdateValue);

        persistedHunter.setUserNickname(newUpdateValue);
        service.updateHunter(persistedHunter);

        assertThat(persistedHunter.getUserNickname()).isEqualTo(newUpdateValue);
    }

    @Test
    public void updateMushroomHuntersInfo() throws Exception {
        String newUpdateValue = "New Info";

        assertThat(persistedHunter.getPersonalInfo()).isNotEqualTo(newUpdateValue);

        persistedHunter.setPersonalInfo(newUpdateValue);
        service.updateHunter(persistedHunter);

        assertThat(persistedHunter.getPersonalInfo()).isEqualTo(newUpdateValue);
    }

    @Test
    public void updateMushroomHuntersNicknameToNull() throws Exception {
        String newUpdateValue = null;
        persistedHunter.setUserNickname(newUpdateValue);

        assertThatThrownBy(() -> service.updateHunter(persistedHunter)).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    public void updateMushroomHuntersToNicknameDuplicate() throws Exception {
        String newUpdateValue = persistedHunter2.getUserNickname();
        persistedHunter.setUserNickname(newUpdateValue);

        assertThatThrownBy(() -> service.updateHunter(persistedHunter)).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    public void updateMushroomHuntersFirstNameToNull() throws Exception {
        String newUpdateValue = null;
        persistedHunter.setFirstName(newUpdateValue);

        assertThatThrownBy(() -> service.updateHunter(persistedHunter)).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    public void updateMushroomHuntersSurnameToNull() throws Exception {
        String newUpdateValue = null;
        persistedHunter.setSurname(newUpdateValue);

        assertThatThrownBy(() -> service.updateHunter(persistedHunter)).isInstanceOf(EntityOperationServiceException.class);
    }


    @Test
    @Ignore
    public void updatePassword() throws Exception {
        assertThat(service.authenticate(persistedHunter, "winry")).isTrue();
        service.updatePassword(persistedHunter, "winry", "hudsonsoft");
        assertThat(service.authenticate(persistedHunter, "winry")).isFalse();
        assertThat(service.authenticate(persistedHunter, "hudsonsoft")).isTrue();
    }

    @Test
    @Ignore
    public void updatePasswordToNull() throws Exception {
        assertThatThrownBy(() -> service.updatePassword(persistedHunter, "winry", null)).isInstanceOf(EntityOperationServiceException.class);
    }

    @Test
    @Ignore
    public void authenticate() throws Exception {
        assertThat(service.authenticate(persistedHunter, "winry")).isTrue();
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
