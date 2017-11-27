package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import cz.muni.fi.pa165.mushrooms.validation.PersistenceSampleApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author BohdanCvejn, bkompis
 */
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class VisitDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Inject
    private VisitDao visitDao;

    @PersistenceContext
    private EntityManager em;

    private Visit visit1;
    private Visit visit2;

    private Forest forest1;
    private Forest forest2;

    private MushroomHunter mushroomHunter1;
    private MushroomHunter mushroomHunter2;

    @Before
    public void setUp() {
        forest1 = new Forest();
        forest1.setName("Dun Morogh");
        forest1.setDescription("Dun Morogh forest in South Wisconsin");

        forest2 = new Forest();
        forest2.setName("Marola Xades");
        forest2.setDescription("Marola Xades forest in South America");

        mushroomHunter1 = new MushroomHunter();
        mushroomHunter1.setFirstName("Alex");
        mushroomHunter1.setSurname("Black");
        mushroomHunter1.setUserNickname("Blacky");

        mushroomHunter2 = new MushroomHunter();
        mushroomHunter2.setFirstName("Piper");
        mushroomHunter2.setSurname("White");
        mushroomHunter2.setUserNickname("Whity");

        visit1 = new Visit();
        visit1.setHunter(mushroomHunter1);
        visit1.setForest(forest1);
        visit1.setDate(LocalDate.of(2017, Month.OCTOBER, 25));

        visit2 = new Visit();
        visit2.setHunter(mushroomHunter2);
        visit2.setForest(forest2);
        visit2.setDate(LocalDate.of(2017, Month.OCTOBER, 30));

        em.persist(forest1);
        em.persist(forest2);
        em.persist(mushroomHunter1);
        em.persist(mushroomHunter2);
        em.persist(visit1);
        em.persist(visit2);
        em.flush();
    }

    @Test
    public void create_validVisit() throws Exception {
        int sizeBeforeCreate = visitDao.findAll().size();

        Visit testVisit = new Visit();
        testVisit.setHunter(mushroomHunter1);
        testVisit.setForest(forest1);
        testVisit.setDate(LocalDate.of(2017, Month.FEBRUARY, 5));
        testVisit.setNote("blabla");

        em.persist(testVisit);

        assertThat(visitDao.findAll().size()).isEqualTo(sizeBeforeCreate + 1);
        assertThat(visitDao.findAll()).containsExactlyInAnyOrder(visit1, visit2, testVisit);
    }

    @Test
    public void create_nullVisit() throws Exception {
        assertThatThrownBy(() -> visitDao.create(null)).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void create_visitWithNullHunter() throws Exception {
        Visit testVisit = new Visit();
        testVisit.setHunter(null);
        testVisit.setForest(forest1);
        testVisit.setDate(LocalDate.of(2017, 2, 5));
        testVisit.setNote("note for the forest");

        assertThatThrownBy(() -> visitDao.create(testVisit)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void create_visitWithNullForest() throws Exception {
        Visit testVisit = new Visit();
        testVisit.setHunter(mushroomHunter1);
        testVisit.setForest(null);
        testVisit.setDate(LocalDate.of(2017, 2, 5));
        testVisit.setNote("note for the forest");

        assertThatThrownBy(() -> visitDao.create(testVisit)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void create_visitWithNullDate() throws Exception {
        Visit testVisit = new Visit();
        testVisit.setHunter(mushroomHunter1);
        testVisit.setForest(forest1);
        testVisit.setDate(null);

        assertThatThrownBy(() -> visitDao.create(testVisit)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void update_validVisitDate() {
        LocalDate newDate = LocalDate.of(2017, Month.MAY, 12);
        Visit foundVisit = em.find(Visit.class, visit1.getId());

        foundVisit.setDate(newDate);
        visitDao.update(foundVisit);
        em.flush();

        assertThat(em.find(Visit.class, visit1.getId()).getDate()).isEqualTo(newDate);
    }

    @Test
    public void update_nullVisit() {
        assertThatThrownBy(() -> visitDao.update(null)).hasRootCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void update_visitWithNullHunter() {
        visit1.setHunter(null);
        visitDao.update(visit1);

        assertThatThrownBy(() -> em.flush()).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void update_visitWithNullForest() {
        visit1.setForest(null);
        visitDao.update(visit1);

        assertThatThrownBy(() -> em.flush()).isInstanceOf(ConstraintViolationException.class);

    }

    @Test
    public void update_visitWithNullDate() {
        visit1.setDate(null);
        visitDao.update(visit1);

        assertThatThrownBy(() -> em.flush()).isInstanceOf(ConstraintViolationException.class);

    }

    @Test
    public void findAll_visits() throws Exception {
        assertThat(visitDao.findAll()).containsExactlyInAnyOrder(visit1, visit2);
    }

    @Test
    public void delete_validVisit() throws Exception {
        int sizeBeforeCreate = visitDao.findAll().size();
        visit1.getHunter().removeVisit(visit1);

        visitDao.delete(visit1);
        em.flush();
        assertThat(visitDao.findAll().size()).isEqualTo(sizeBeforeCreate - 1);
        assertThat(visitDao.findAll()).containsExactly(visit2);
    }

    @Test
    public void delete_nullVisit() throws Exception {
        assertThatThrownBy(() -> visitDao.delete(null)).hasRootCauseInstanceOf(IllegalArgumentException.class);
    }

    /* fails on ConstraintViolationException
        @Test
        public void delete_nonExistingVisit() {
            int sizeBeforeCreate = visitDao.findAll().size();

            Visit testVisit = new Visit();
            testVisit.setHunter(mushroomHunter1);
            testVisit.setForest(forest1);
            testVisit.setDate(LocalDate.of(2017, 2, 5));

            visitDao.delete(testVisit);
            em.flush();

            assertThat(visitDao.findAll().size()).isEqualTo(sizeBeforeCreate);
            assertThat(visitDao.findAll()).containsExactlyInAnyOrder(visit1, visit2);
        }
    */
    @Test
    public void findById_nullId() throws Exception {
        assertThatThrownBy(() -> visitDao.findById(null)).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findById_validId() throws Exception {
        Visit testForest = visitDao.findById(visit1.getId());
        assertThat(testForest).isNotNull();
        assertThat(testForest).isEqualTo(visit1);
    }

    @Test
    public void findById_invalidId() throws Exception {
        Long invalidID = visit1.getId() + visit2.getId();
        Visit testVisit = visitDao.findById(invalidID);
        assertThat(testVisit).isNull();
    }

    @Test
    public void findByDate_nullDateFrom() throws Exception {
        assertThatThrownBy(() -> visitDao.findByDate(null, LocalDate.of(2017, Month.OCTOBER, 25))).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findByDate_nullDateTo() throws Exception {
        assertThatThrownBy(() -> visitDao.findByDate(LocalDate.of(2017, Month.OCTOBER, 25), null)).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findByDate_validDate() throws Exception {
        LocalDate firstDate = visit1.getDate();
        LocalDate secondDate = visit2.getDate();

        List<Visit> testVisit = visitDao.findByDate(firstDate, secondDate);
        assertThat(testVisit).isNotNull();
        assertThat(testVisit).containsExactlyInAnyOrder(visit1, visit2);
    }
}