package cz.muni.fi.pa165.mushrooms.dao;

import cz.muni.fi.pa165.mushrooms.validation.PersistenceSampleApplicationContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the MushroomHunterDao with an empty database.
 *
 * @author bkompis
 */
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class MushroomHunterDaoTestNoSetup extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private MushroomHunterDao mushroomHunterDao;

    @Test
    public void findById_nonexistentId() throws Exception {
        // the database contains no entries, and thus no ID's
        assertThat(mushroomHunterDao.findById(1L)).isNull();
    }

    @Test
    public void findAll_emptyDatabase() throws Exception {
        assertThat(mushroomHunterDao.findAll()).isEmpty();
    }

    @Test
    public void findByFirstName_emptyDatabase() throws Exception {
        assertThat(mushroomHunterDao.findByFirstName("anything")).isEmpty();
    }

    @Test
    public void findBySurname() throws Exception {
        assertThat(mushroomHunterDao.findBySurname("anything")).isEmpty();
    }

    @Test
    public void findByNickname() throws Exception {
        assertThat(mushroomHunterDao.findByNickame("anything")).isEmpty();
    }
}
