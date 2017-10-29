package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
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
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by Matúš on 28.10.2017.
 */
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class ForestDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ForestDao forestDao;

    @PersistenceContext
    private EntityManager em;

    private Forest forest;

    @Before
    public void setUp(){
        forest = new Forest();
        forest.setName("Dun Morogh");
        forest.setDescription("Dun Morogh forest in South Wisconsin");

        em.persist(forest);
        em.flush();
    }

    @Test
    public void findById_validId() throws Exception {
        Forest testForest = forestDao.findById(forest.getId());
        assertThat(testForest).isNotNull();
        assertThat(testForest).isEqualToComparingFieldByField(forest);
    }

    @Test
    public void findById_nullId() throws Exception {
        assertThatThrownBy(() -> forestDao.findById(null)).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void create_validForest() throws Exception {
        int sizeBeforeCreate = forestDao.findAll().size();
        Forest testForest = new Forest();
        testForest.setName("Duskwood");

        forestDao.create(testForest);
        Forest foundForest = em.find(Forest.class, testForest.getId());

        assertThat(foundForest).isNotNull();
        assertThat(forestDao.findAll().size()).isEqualTo(sizeBeforeCreate + 1);
    }

    @Test
    public void create_nullForest() throws Exception {
        assertThatThrownBy(() -> forestDao.create(null)).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void create_ForestWithNullName() throws Exception {
        Forest testForest = new Forest();
        testForest.setName(null);

        assertThatThrownBy(() -> forestDao.create(testForest)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void create_ForestWithNonUniqueName() throws Exception {
        Forest testForest = new Forest();
        testForest.setName("Dun Morogh");

       assertThatThrownBy(() -> forestDao.create(testForest)).isInstanceOf(JpaSystemException.class);
    }

    @Test
    public void create_ExistingForest() {
        int sizeBeforeCreate = forestDao.findAll().size();
        forestDao.create(forest);

        // Inserting same object will not raise the number of database entities
        assertThat(forestDao.findAll().size()).isEqualTo(sizeBeforeCreate);
    }

    @Test
    public void create_ForestWithExistingId() {
        int sizeBeforeCreate = forestDao.findAll().size();
        Forest testForest = new Forest();
        testForest.setId(forest.getId());

        // Inserting same object will not raise the number of database entities
        assertThat(forestDao.findAll().size()).isEqualTo(sizeBeforeCreate);
    }

    @Test
    public void delete_validForest() throws Exception {
        int sizeBeforeCreate = forestDao.findAll().size();

        forestDao.delete(forest);
        em.flush();

        assertThat(forestDao.findAll().size()).isEqualTo(sizeBeforeCreate - 1);
    }

    @Test
    public void delete_nullForest() throws Exception {
        assertThatThrownBy(() -> forestDao.delete(null)).hasRootCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void delete_nonExistingForest() {
        int sizeBeforeCreate = forestDao.findAll().size();
        Forest testForest = new Forest();
        testForest.setName("Ashenvale");

        forestDao.delete(testForest);
        em.flush();

        assertThat(forestDao.findAll().size()).isEqualTo(sizeBeforeCreate);
    }

    @Test
    public void findAll() throws Exception {
        Forest testForest = new Forest();
        testForest.setName("Silverpine Forest");
        forestDao.create(testForest);

        List<Forest> forests = forestDao.findAll();
        // TODO
    }

    @Test
    public void findByFirstName() throws Exception {
    }

    @Test
    public void findBySurname() throws Exception {
    }

}
