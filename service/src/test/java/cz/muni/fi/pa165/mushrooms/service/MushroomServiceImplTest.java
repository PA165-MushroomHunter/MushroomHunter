package cz.muni.fi.pa165.mushrooms.service;

import cz.muni.fi.pa165.mushrooms.dao.MushroomDao;
import cz.muni.fi.pa165.mushrooms.dao.VisitDao;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for service layer related to Mushroom entity
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


        void create(Mushroom mushroom) {
            validate(mushroom);
            if (mushroom.getId() != null) throw new IllegalArgumentException("already in db");

            mushroom.setId(databaseCounter);
            database.put(databaseCounter++, mushroom);
        }

        void update(Mushroom mushroom) {
            validate(mushroom);
            if (mushroom.getId() == null) throw new IllegalArgumentException("not persisted - cannot be updated");
            if (database.replace(mushroom.getId(),mushroom) == null) throw new IllegalArgumentException("no object with such id in DB - cannot be updated");;
        }

        void delete(Mushroom mushroom){
            validate(mushroom);
            if (mushroom.getId() == null) throw new IllegalArgumentException("no id assigned");
            //TODO: deleting non persisted should be OK or NOK?
            if (database.remove(mushroom.getId()) == null) throw new IllegalArgumentException("object was not in the database");;
        }

        Mushroom findById(Long id) {
            if (id == null){
                throw new IllegalArgumentException("null id");
            }
            return database.get(id);
        }

        List<Mushroom> findByMushroomType(MushroomType mushroomType){
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

        List<Mushroom> findByIntervalOfOccurrence(String fromMonth, String toMonth){
            String intervalOfOccurrence = fromMonth + " - " + toMonth;
            List<Mushroom> typedList = new ArrayList<>();
            List<Mushroom> dumpedDatabase = Collections.unmodifiableList(new ArrayList<>(database.values()));
            for (Mushroom m : dumpedDatabase) {
                if (m.getIntervalOfOccurrence().equals(intervalOfOccurrence)) typedList.add(m);
            }

            return typedList;
        }

        Mushroom findByName(String name) {
            List<Mushroom> typedList = new ArrayList<>();
            List<Mushroom> dumpedDatabase = Collections.unmodifiableList(new ArrayList<>(database.values()));
            for (Mushroom m : dumpedDatabase) {
                if (m.getName().equals(name)) return m;
            }
            return null;
        }

    }

    MockDatabase database;

    @Before
    public void Setup(){}

    @Test
    public void findAllMushrooms(){}

    @Test
    public void findMushroomById(){}

    @Test
    public void FindMushroomByName(){}

    @Test
    public void findByIntervalOfOccurrence(){}

    @Test
    public void createMushroom(){}

    @Test
    public void deleteMushroom(){}

    @Test
    public void updateMushroom(){}

}
