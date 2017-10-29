package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.validation.PersistenceSampleApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Matúš on 28.10.2017.
 */
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ForestDaoTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ForestDao forestDao;

    @PersistenceContext
    private EntityManager em;

    @PersistenceUnit
    private EntityManagerFactory emf;

    private Forest forest;

    @Before
    public void setUp(){
        forest = new Forest();
        forest.setName("Sorrowful");
        forest.setDescription("Sorrowful forest in South Wisconsin");

        em.persist(forest);
    }

    @Test
    public void findById_validId() throws Exception {
        Forest testForest = forestDao.findById(forest.getId());
        assertThat(testForest).isNotNull();
        assertThat(testForest).isEqualToComparingFieldByField(forest);
    }

    @Test
    public void findById_nullId() throws Exception {
        forestDao.findById(null);
    }

    @Test
    public void create() throws Exception {
        Forest testForest = new Forest();
        testForest.setName("Dark");
        forestDao.create(testForest);
        Forest foundForest = em.find(Forest.class, testForest.getId());
        Forest foundForest1 = em.find(Forest.class, forest.getId());

        System.out.println("\n\n"+ foundForest + " " + foundForest1);
    }

    @Test
    public void delete() throws Exception {
        Forest testForest = new Forest();
        testForest.setName("Dark");
        forestDao.create(testForest);
        Forest foundForest = em.find(Forest.class, testForest.getId());
        System.out.println("\n\n"+ foundForest);
        forestDao.delete(foundForest);
        foundForest = em.find(Forest.class, testForest.getId());
        System.out.println("\n\n"+ foundForest);
    }

    @Test
    public void findAll() throws Exception {
        Forest testForest = new Forest();
        testForest.setName("Dark");
        forestDao.create(testForest);

        List<Forest> forests = forestDao.findAll();
        System.out.println("\n\n" + forests.toString());
    }

    @Test
    public void findByFirstName() throws Exception {
    }

    @Test
    public void findBySurname() throws Exception {
    }

}
