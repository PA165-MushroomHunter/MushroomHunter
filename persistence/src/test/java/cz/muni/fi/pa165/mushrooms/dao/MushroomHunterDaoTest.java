package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.validation.PersistenceSampleApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for the MushroomHunterDao with prepopulated database.
 *
 * @author bkompis
 */
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class MushroomHunterDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MushroomHunterDao mushroomHunterDao;

    @PersistenceContext
    private EntityManager em;

    private MushroomHunter hunter1;
    private MushroomHunter hunter2;

    @Before
    public void setUp() {
        // create hunters
        hunter1 = createMushroomHunter("John", "Doe", "Johnny");
        hunter2 = createMushroomHunter("Jack", "Daniels", "Dan");

        // persist hunters
        em.persist(hunter1);
        em.persist(hunter2);
        // flush is necessary to 'save' changes, particularly in collections stored in the entity (here, Visits)
        em.flush();
    }

    private static MushroomHunter createMushroomHunter(String firstName, String surname, String userNickname) {
        MushroomHunter hunter = new MushroomHunter();
        hunter.setFirstName(firstName);
        hunter.setSurname(surname);
        hunter.setUserNickname(userNickname);
        hunter.setPersonalInfo("The mushroom hunter " + userNickname + " - " + firstName + " " + surname);
        return hunter;
    }

    @Test
    public void findById_validId() throws Exception {
        MushroomHunter hunter = mushroomHunterDao.findById(hunter1.getId());
        assertThat(hunter).isNotNull();
        assertThat(hunter).isEqualTo(hunter1);
    }

    @Test
    public void findById_nullId() throws Exception {
        assertThatThrownBy(() -> mushroomHunterDao.findById(null)).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findById_nonexistentId() throws Exception {
        Long nonexistentId = hunter1.getId() + hunter2.getId();
        assertThat(mushroomHunterDao.findById(nonexistentId)).isNull();
    }

    @Test
    public void findAll_valid() throws Exception {
        assertThat(mushroomHunterDao.findAll()).containsExactlyInAnyOrder(hunter1, hunter2);
    }

    @Test
    public void findByFirstName_valid() throws Exception {
        List<MushroomHunter> found = mushroomHunterDao.findByFirstName("John");
        assertThat(found).containsExactly(hunter1);
    }

    @Test
    public void findByFirstName_unknownName() throws Exception {
        List<MushroomHunter> found = mushroomHunterDao.findByFirstName("Nonexistent");
        assertThat(found).isEmpty();
    }

    @Test
    public void findByFirstName_nullName() throws Exception {
        assertThatThrownBy(() -> mushroomHunterDao.findByFirstName(null))
                .hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findBySurname_validName() throws Exception {
        List<MushroomHunter> found = mushroomHunterDao.findBySurname("Doe");
        assertThat(found).containsExactly(hunter1);
    }

    @Test
    public void findBySurname_unknownName() throws Exception {
        List<MushroomHunter> found = mushroomHunterDao.findBySurname("Nonexistent");
        assertThat(found).isEmpty();
    }

    @Test
    public void findBySurname_nullName() throws Exception {
        assertThatThrownBy(() -> mushroomHunterDao.findBySurname(null))
                .hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findByNickname_valid() throws Exception {
        List<MushroomHunter> found = mushroomHunterDao.findByNickame("Johnny");
        assertThat(found).containsExactly(hunter1);
    }

    @Test
    public void findByNickname_unknownNickname() throws Exception {
        List<MushroomHunter> found = mushroomHunterDao.findByNickame("Nonexistent");
        assertThat(found).isEmpty();
    }

    @Test
    public void findByNickname_nullName() throws Exception {
        assertThatThrownBy(() -> mushroomHunterDao.findByNickame(null))
                .hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    // note: create flushes automatically
    @Test
    public void create_valid() throws Exception {
        MushroomHunter newHunter = createMushroomHunter("Sylvanas", "Windrunner", "banshee");
        assertThat(newHunter.getId()).isNull();

        mushroomHunterDao.create(newHunter);
        assertThat(newHunter.getId()).isNotNull();

        //compare the the newly created entity with the one stored in the database
        assertThat(newHunter).isEqualTo(mushroomHunterDao.findById(newHunter.getId()));
    }

    @Test
    public void create_valid_similarEntity() throws Exception {
        MushroomHunter newHunter = createMushroomHunter(hunter1.getFirstName(), hunter1.getSurname(), "different");
        assertThat(newHunter.getId()).isNull();
        mushroomHunterDao.create(newHunter);
        assertThat(newHunter.getId()).isNotNull();

        //compare the the newly created entity with the one stored in the database
        assertThat(newHunter).isEqualTo(mushroomHunterDao.findById(newHunter.getId()));
    }

    @Test
    public void create_duplicateEntity() throws Exception {
        MushroomHunter h = createMushroomHunter("Unused", "Name", hunter1.getUserNickname());
        assertThatThrownBy(() -> mushroomHunterDao.create(h)).isInstanceOf(JpaSystemException.class);
    }

    @Test
    public void create_nullFirstName() throws Exception {
        MushroomHunter h = createMushroomHunter(null, "Name", "im_awesome");
        assertThatThrownBy(() -> mushroomHunterDao.create(h)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void create_nullSurname() throws Exception {
        MushroomHunter h = createMushroomHunter("Unused", null, "im_awesome");
        assertThatThrownBy(() -> mushroomHunterDao.create(h)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void create_nullNickname() throws Exception {
        MushroomHunter h = createMushroomHunter("Unused", "Name", null);
        assertThatThrownBy(() -> mushroomHunterDao.create(h)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void create_nullEntity() throws Exception {
        assertThatThrownBy(() -> mushroomHunterDao.create(null))
                .hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    //note: update needs to be flushed; tests will pass without flush, but do not behave as expected (false positives)
    @Test
    public void update_valid() throws Exception {
        hunter1.setFirstName("Changeling");
        hunter1.setSurname("Skinchanger");
        hunter1.setUserNickname("Unused");
        hunter1.setAdmin(true);
        hunter1.setPersonalInfo("Someone else now.");

        mushroomHunterDao.update(hunter1);
        em.flush();
        // get the updated entity from the database and compare
        MushroomHunter found = mushroomHunterDao.findById(hunter1.getId());
        assertThat(found).isEqualTo(hunter1);
    }

    @Test
    public void update_valid_managedEntity() throws Exception {
        MushroomHunter hunter = mushroomHunterDao.findById(hunter1.getId());
        hunter.setFirstName("Changeling");
        hunter.setSurname("Skinchanger");
        hunter.setUserNickname("Unused");
        hunter.setAdmin(true);
        hunter.setPersonalInfo("Someone else now.");

        mushroomHunterDao.update(hunter);
        em.flush();

        // get the updated entity from the database and compare
        MushroomHunter found = mushroomHunterDao.findById(hunter.getId());
        assertThat(found).isEqualTo(hunter);
        assertThat(found.getFirstName()).isEqualTo("Changeling");
        assertThat(found.getSurname()).isEqualTo("Skinchanger");
        assertThat(found.getUserNickname()).isEqualTo("Unused");
        assertThat(found.isAdmin()).isTrue();
    }

    @Test
    public void update_duplicateNickname() throws Exception {
        MushroomHunter hunter = mushroomHunterDao.findById(hunter1.getId());
        hunter.setUserNickname(hunter2.getUserNickname());
        // constraint violation wrapped in PersistenceException
        mushroomHunterDao.update(hunter); //does not flush
        assertThatThrownBy(() -> em.flush()).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void update_nullNickname() throws Exception {
        MushroomHunter hunter = mushroomHunterDao.findById(hunter1.getId());
        hunter.setUserNickname(null);
        assertThat(hunter.getUserNickname()).isNull();
        mushroomHunterDao.update(hunter); // does not flush
        assertThatThrownBy(() -> em.flush()).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void update_nullFirstName() throws Exception {
        MushroomHunter hunter = mushroomHunterDao.findById(hunter1.getId());
        hunter.setFirstName(null);
        assertThat(hunter.getFirstName()).isNull();
        mushroomHunterDao.update(hunter); // does not flush
        assertThatThrownBy(() -> em.flush()).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void update_nullSurname() throws Exception {
        MushroomHunter hunter = mushroomHunterDao.findById(hunter1.getId());
        hunter.setSurname(null);
        assertThat(hunter.getSurname()).isNull();
        mushroomHunterDao.update(hunter); // does not flush
        assertThatThrownBy(() -> em.flush()).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void update_nullEntity() throws Exception {
        // exception happens before the database is even accessed == before flush
        assertThatThrownBy(() -> mushroomHunterDao.update(null)).hasRootCauseInstanceOf(IllegalArgumentException.class);
    }

    // note: delete does not flush automatically
    @Test
    public void delete_valid() throws Exception {
        assertThat(mushroomHunterDao.findAll()).containsExactlyInAnyOrder(hunter1, hunter2);

        mushroomHunterDao.delete(hunter1);
        em.flush();

        assertThat(mushroomHunterDao.findAll()).containsExactly(hunter2);
    }

    @Test
    public void delete_entityNotPersisted() throws Exception {
        MushroomHunter newHunter = createMushroomHunter("Sylvanas", "Windrunner", "banshee");
        mushroomHunterDao.delete(newHunter); // should not do anything
        em.flush();
        assertThat(mushroomHunterDao.findAll()).containsExactlyInAnyOrder(hunter1, hunter2);
    }

    @Test
    public void delete_nullEntity() throws Exception {
        assertThatThrownBy(() -> mushroomHunterDao.delete(null)).hasRootCauseInstanceOf(IllegalArgumentException.class);
    }

    //TODO: cascading delete tests (Visit set)

}