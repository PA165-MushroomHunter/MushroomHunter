package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.dao.MushroomDao;
import cz.muni.fi.pa165.mushrooms.dao.VisitDao;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for service layer related to Mushroom entity
 * Not much to test here actually, service is just relaying to DAO layer
 *
 * @author bencikpeter
 */

public class MushroomServiceImplTest {

    @Injectable
    private MushroomDao mushroomDao;

    @Tested(fullyInitialized = true)
    private MushroomServiceImpl service;

    private class MockDatabase {
        private Map<Long, Mushroom> database = new HashMap<>();
        private long databaseCounter = 0;

        private void validate(Mushroom mushroom){
            if (mushroom == null) throw new IllegalArgumentException("null");
            if (mushroom.getName() == null) throw new IllegalArgumentException("nameIsNull");
            if (mushroom.getIntervalOfOccurrence() == null) throw new IllegalArgumentException("interval of occurence is null");
            if (mushroom.getType() == null) throw new IllegalArgumentException("type is null");
        }


        public void create(Mushroom mushroom) {
            validate(mushroom);
            if (mushroom.getId() != null) throw new IllegalArgumentException("already in db");

            mushroom.setId(databaseCounter);
            database.put(databaseCounter++, mushroom);
        }

        public void update(Mushroom mushroom) {
            validate(mushroom);
            if (mushroom.getId() == null) throw new IllegalArgumentException("not persisted - cannot be updated");
            if (database.replace(mushroom.getId(),mushroom) == null) throw new IllegalArgumentException("no object with such id in DB - cannot be updated");;
        }

        public void delete(Mushroom mushroom){
            validate(mushroom);
            if (mushroom.getId() == null) throw new IllegalArgumentException("no id assigned");
            //TODO: deleting non persisted should be OK or NOK?
            if (database.remove(mushroom.getId()) == null) throw new IllegalArgumentException("object was not in the database");;
        }

        public Mushroom findById(Long id) {
            if (id == null){
                throw new IllegalArgumentException("null id");
            }
            return database.get(id);
        }

        public List<Mushroom> findByMushroomType(MushroomType mushroomType){
            List<Mushroom> typedList = new ArrayList<>();
            List<Mushroom> dumpedDatabase = Collections.unmodifiableList(new ArrayList<>(database.values()));
            for (Mushroom m : dumpedDatabase) {
                if (m.getType().equals(mushroomType)) typedList.add(m);
            }

            return typedList;
        }

        public List<Mushroom> findAll(){
            return Collections.unmodifiableList(new ArrayList<>(database.values()));
        }

        public List<Mushroom> findByIntervalOfOccurrence(String fromMonth, String toMonth){
            String intervalOfOccurrence = fromMonth + " - " + toMonth;
            List<Mushroom> typedList = new ArrayList<>();
            List<Mushroom> dumpedDatabase = Collections.unmodifiableList(new ArrayList<>(database.values()));
            for (Mushroom m : dumpedDatabase) {
                if (m.getIntervalOfOccurrence().equals(intervalOfOccurrence)) typedList.add(m);
            }

            return typedList;
        }

        public Mushroom findByName(String name) {
            if (name == null) throw new IllegalArgumentException("Name is null");
            List<Mushroom> typedList = new ArrayList<>();
            List<Mushroom> dumpedDatabase = Collections.unmodifiableList(new ArrayList<>(database.values()));
            for (Mushroom m : dumpedDatabase) {
                if (m.getName().equals(name)) return m;
            }
            return null;
        }

    }

    MockDatabase database;
    Mushroom mushroom1, mushroom2, mushroom3;

    private static Mushroom setupMushroom(String name, MushroomType type, String fromM, String toM){
        Mushroom m = new Mushroom();
        m.setName(name);
        m.setIntervalOfOccurrence(fromM,toM);
        m.setType(type);
        return m;
    }

    @Before
    public void Setup(){
        //TODO: actual setup

        database = new MockDatabase();
        mushroom1 = setupMushroom("some",MushroomType.UNEDIBLE,"june","july");
        mushroom2 = setupMushroom("other",MushroomType.POISONOUS,"june","july");
        mushroom3 = setupMushroom("different",MushroomType.UNEDIBLE,"may","september");



        new Expectations(){{
            mushroomDao.findById(anyLong);
            result = new Delegate() {
                Mushroom foo(Long id){
                    return database.findById(id);
                }
            }; minTimes = 0;

            mushroomDao.findByMushroomType((MushroomType) any);
            result = new Delegate() {
                List<Mushroom> foo(MushroomType type){
                    return database.findByMushroomType(type);
                }
            }; minTimes = 0;

            mushroomDao.create((Mushroom) any);
            result = new Delegate() {
                void foo(Mushroom m){
                    database.create(m);
                }
            }; minTimes = 0;

            mushroomDao.update((Mushroom) any);
            result = new Delegate() {
                void foo(Mushroom m){
                    database.update(m);
                }
            }; minTimes = 0;

            mushroomDao.delete((Mushroom) any);
            result = new Delegate() {
                void foo(Mushroom m){
                    database.delete(m);
                }
            }; minTimes = 0;

            mushroomDao.findAll();
            result = new Delegate() {
                List<Mushroom> foo(){
                    return database.findAll();
                }
            }; minTimes = 0;

            mushroomDao.findByName(anyString);
            result = new Delegate() {
                Mushroom foo(String name){
                    return database.findByName(name);
                }
            }; minTimes = 0;

            mushroomDao.findByIntervalOfOccurrence(anyString, anyString);
            result = new Delegate() {
                List<Mushroom> foo(String fromMonth, String toMonth){
                    return database.findByIntervalOfOccurrence(fromMonth,toMonth);
                }
            }; minTimes = 0;
        }};
    }

    @Test
    public void findAllMushrooms(){

        assertThat(service.findAllMushrooms()).isEmpty();

        database.create(mushroom1);
        database.create(mushroom2);
        database.create(mushroom3);

        assertThat(service.findAllMushrooms()).containsExactlyInAnyOrder(mushroom1,mushroom2,mushroom3);
    }

    @Test
    public void findMushroomById(){
        database.create(mushroom1);
        database.create(mushroom2);
        //null
        assertThatThrownBy(() -> service.findMushroomById(null)).isInstanceOf(IllegalArgumentException.class);
        //valid
        assertThat(service.findMushroomById(mushroom1.getId())).isEqualToComparingFieldByField(mushroom1);
        //invalid
        assertThat(service.findMushroomById(999L)).isNull();
    }

    @Test
    public void FindMushroomByName(){

        database.create(mushroom1);
        database.create(mushroom2);

        //null name
        assertThatThrownBy(() -> service.findMushroomByName(null)).isInstanceOf(IllegalArgumentException.class);
        //nonexistent name
        assertThat(service.findMushroomByName(mushroom3.getName())).isNull();
        //existing name
        database.create(mushroom3);
        assertThat(service.findMushroomByName(mushroom3.getName())).isEqualToComparingFieldByField(mushroom3);
        //empty string
        assertThat(service.findMushroomByName("")).isNull();

    }

    @Test
    public void findByIntervalOfOccurrence(){}

    @Test
    public void createMushroom(){

        assertThat(mushroom1.getId()).isNull();
        service.createMushroom(mushroom1);
        assertThat(mushroom1.getId()).isNotNull();


        //entity with preexisting ID
        mushroom2.setId(2L);
        assertThatThrownBy(()->service.createMushroom(mushroom2)).isInstanceOf(IllegalArgumentException.class);

        //entity with conflicting ID
        mushroom2.setId(mushroom1.getId());
        assertThatThrownBy(()->service.createMushroom(mushroom2)).isInstanceOf(IllegalArgumentException.class);

        //null
        assertThatThrownBy(()->service.createMushroom(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void deleteMushroom(){

        database.create(mushroom1);

        //delete nonexistent without ID
        assertThatThrownBy(()->service.deleteMushroom(mushroom2)).isInstanceOf(IllegalArgumentException.class);
        //delete nonexistent with ID
        mushroom2.setId(2L);
        assertThatThrownBy(()->service.deleteMushroom(mushroom2)).isInstanceOf(IllegalArgumentException.class);
        //correct delete
        service.deleteMushroom(mushroom1);
        assertThat(database.findAll()).isEmpty();
        //delete null
        assertThatThrownBy(()->service.deleteMushroom(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateMushroom(){

        database.create(mushroom1);
        database.create(mushroom3);

        //update no id
        assertThatThrownBy(()->service.updateMushroom(mushroom2)).isInstanceOf(IllegalArgumentException.class);
        //update nonexistent ID
        mushroom2.setId(1234L);
        assertThatThrownBy(()->service.updateMushroom(mushroom2)).isInstanceOf(IllegalArgumentException.class);
        //correct update
        String newName = "Totaly new name";
        mushroom1.setName(newName);
        service.updateMushroom(mushroom1);
        Mushroom tmpMush = database.findById(mushroom1.getId());
        assertThat(tmpMush.getName().equals(newName));
        //update null
        assertThatThrownBy(()->service.updateMushroom(null)).isInstanceOf(IllegalArgumentException.class);
    }

}
