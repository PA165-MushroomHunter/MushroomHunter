package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.dao.ForestDao;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for service layer related to Forest entity
 *
 * @author bohdancvejn
 */

public class ForestServiceImplTest {

    @Injectable
    private ForestDao forestDao;

    @Tested(fullyInitialized = true)
    private ForestServiceImpl service;

    private class MockDatabase {
        private Map<Long, Forest> database = new HashMap<>();
        private long databaseCounter = 1L;


        public void create(Forest forest) {
            validateForest(forest);
            if (forest.getId() != null) throw new IllegalArgumentException("already in db");

            forest.setId(databaseCounter);
            database.put(databaseCounter++, forest);
        }

        public void update(Forest forest) {
            validateForest(forest);
            if (forest.getId() == null) throw new IllegalArgumentException("not persisted - cannot be updated");
            if (database.replace(forest.getId(), forest) == null)
                throw new IllegalArgumentException("no object with such id in DB - cannot be updated");
            ;
        }

        public void delete(Forest forest) {
            validateForest(forest);
            if (forest.getId() == null) throw new IllegalArgumentException("no id assigned");
            database.remove(forest.getId());
        }

        public Forest findById(Long id) {
            if (id == null) {
                throw new IllegalArgumentException("null id");
            }
            return database.get(id);
        }

        public Forest findByName(String name) {
            if (name == null) throw new IllegalArgumentException("Name is null");
            List<Forest> typedList = new ArrayList<>();
            List<Forest> dumpedDatabase = Collections.unmodifiableList(new ArrayList<>(database.values()));
            for (Forest m : dumpedDatabase) {
                if (m.getName().equals(name)) return m;
            }
            return null;
        }

        public List<Forest> findAll() {
            return Collections.unmodifiableList(new ArrayList<>(database.values()));
        }

        public List<Forest> findAllForestsWithMushroom(Mushroom mushroomEntity) throws DataAccessException {
            return null; //TODO: not decided if we want this functionality
        }


    }

    MockDatabase database;
    Forest forest1, forest2, forest3;

    private static Forest setupForest(String name, String description) {
        Forest f = new Forest();
        f.setName(name);
        f.setDescription(description);
        return f;
    }

    //please move it to TestUtils
    public static void validateForest(Forest forest) {
        if (forest == null) throw new IllegalArgumentException("null");
        if (forest.getName() == null) throw new IllegalArgumentException("nameIsNull");
    }

    @Before
    public void setUp() {

        database = new MockDatabase();
        forest1 = setupForest("Evergreen", "nice and always green");
        forest2 = setupForest("Negativos", "dirty but full of mushrooms");
        forest3 = setupForest("Lohor", "never visited by man");


        new Expectations() {{

            forestDao.create((Forest) any);
            result = new Delegate() {
                void foo(Forest f) {
                    database.create(f);
                }
            };
            minTimes = 0;

            forestDao.update((Forest) any);
            result = new Delegate() {
                void foo(Forest f) {
                    database.update(f);
                }
            };
            minTimes = 0;

            forestDao.delete((Forest) any);
            result = new Delegate() {
                void foo(Forest f) {
                    database.delete(f);
                }
            };
            minTimes = 0;

            forestDao.findById(anyLong);
            result = new Delegate() {
                Forest foo(Long id) {
                    return database.findById(id);
                }
            };
            minTimes = 0;

            forestDao.findByName(anyString);
            result = new Delegate() {
                Forest foo(String name) {
                    return database.findByName(name);
                }
            };
            minTimes = 0;

            forestDao.findAll();
            result = new Delegate() {
                List<Forest> foo() {
                    return database.findAll();
                }
            };
            minTimes = 0;

        }};
    }

    @Test
    public void findAllForests() {

        assertThat(service.findAllForests()).isEmpty();

        database.create(forest1);
        database.create(forest2);
        database.create(forest3);

        assertThat(service.findAllForests()).containsExactlyInAnyOrder(forest1, forest2, forest3);
    }

    @Test
    public void findForestById_valid() {
        database.create(forest1);
        database.create(forest2);

        assertThat(service.findForestById(forest1.getId())).isEqualToComparingFieldByField(forest1);
    }

    @Test
    public void findForestById_invalid() {
        database.create(forest1);
        database.create(forest2);

        assertThat(service.findForestById(forest1.getId() + forest2.getId())).isNull();
    }

    @Test
    public void findForestById_null() {
        database.create(forest1);
        database.create(forest2);

        assertThatThrownBy(() -> service.findForestById(null)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void FindForestByName_null() {

        database.create(forest1);
        database.create(forest2);

        assertThatThrownBy(() -> service.findForestByName(null)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void FindForestByName_nonexistent() {

        database.create(forest1);
        database.create(forest2);

        assertThat(service.findForestByName(forest3.getName())).isNull();
    }

    @Test
    public void FindForestByName_valid() {

        database.create(forest1);
        database.create(forest2);
        database.create(forest3);

        assertThat(service.findForestByName(forest3.getName())).isEqualToComparingFieldByField(forest3);
    }

    @Test
    public void FindForestByName_emptyString() {

        database.create(forest1);
        database.create(forest3);

        assertThat(service.findForestByName("")).isNull();
    }

    @Test
    public void createForest_valid() {

        assertThat(forest1.getId()).isNull();
        service.createForest(forest1);
        assertThat(forest1.getId()).isNotNull();
        assertThat(database.findById(forest1.getId())).isEqualTo(forest1);
    }

    @Test
    public void createForest_preexisting() {

        forest2.setId(2L);
        assertThatThrownBy(() -> service.createForest(forest2)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void createForest_conflicting() {

        database.create(forest1);

        forest2.setId(forest1.getId());
        assertThatThrownBy(() -> service.createForest(forest2)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void createForest_null() {

        assertThatThrownBy(() -> service.createForest(null)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void deleteForest_noID() {

        database.create(forest1);

        assertThatThrownBy(() -> service.deleteForest(forest2)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void deleteForest_nonexistentID() {

        database.create(forest1);

        //delete nonexistent with ID
        forest2.setId(2L);
        service.deleteForest(forest2);
        assertThat(database.findAll()).containsExactlyInAnyOrder(forest1); //checks that invalid delete does not modify database
    }

    @Test
    public void deleteForest_correct() {

        database.create(forest1);


        service.deleteForest(forest1);
        assertThat(database.findAll()).isEmpty();
    }

    @Test
    public void deleteForest_null() {

        database.create(forest1);

        assertThatThrownBy(() -> service.deleteForest(null)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void updateForest_nullID() {

        database.create(forest1);
        database.create(forest3);

        assertThatThrownBy(() -> service.updateForest(forest2)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void updateForest_nonexistentID() {

        database.create(forest1);
        database.create(forest3);

        forest2.setId(1234L);
        assertThatThrownBy(() -> service.updateForest(forest2)).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void updateForest_valid() {

        database.create(forest1);
        database.create(forest3);

        String newName = "New updated name";
        forest1.setName(newName);
        service.updateForest(forest1);
        Forest tmp = database.findById(forest1.getId());
        assertThat(tmp.getName().equals(newName));
    }

    @Test
    public void updateForest_null() {

        database.create(forest1);
        database.create(forest3);

        assertThatThrownBy(() -> service.updateForest(null)).isInstanceOf(DataAccessException.class);
    }
}

