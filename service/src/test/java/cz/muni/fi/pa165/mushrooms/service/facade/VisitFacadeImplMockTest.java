package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.dto.*;
import cz.muni.fi.pa165.mushrooms.entity.Forest;
import cz.muni.fi.pa165.mushrooms.entity.Mushroom;
import cz.muni.fi.pa165.mushrooms.entity.MushroomHunter;
import cz.muni.fi.pa165.mushrooms.entity.Visit;
import cz.muni.fi.pa165.mushrooms.enums.MushroomType;
import cz.muni.fi.pa165.mushrooms.service.BeanMappingService;
import cz.muni.fi.pa165.mushrooms.service.TestUtils;
import cz.muni.fi.pa165.mushrooms.service.VisitService;
import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import mockit.Deencapsulation;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static cz.muni.fi.pa165.mushrooms.service.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Basic tests for hunterFacade implementations using a mock of the service layer.
 *
 * @author bkompis
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class VisitFacadeImplMockTest extends AbstractTransactionalJUnit4SpringContextTests {

    private MushroomHunter hunter1;
    private MushroomHunter hunter2;

    private MushroomHunterDTO hunter1DTO;
    private MushroomHunterDTO hunter2DTO;

    private Forest forest1;
    private Forest forest2;

    private ForestDTO forest1DTO;
    private ForestDTO forest2DTO;

    private Mushroom mushroom1;
    private Mushroom mushroom2;

    private MushroomDTO mushroom1DTO;
    private MushroomDTO mushroom2DTO;

    private Visit visit1;
    private Visit visit2;

    private VisitDTO visit1DTO;
    private VisitDTO visit2DTO;

    @Injectable
    private VisitService visitService;

    @Inject
    @Tested
    private Mapper dozer;
    @Inject
    @Tested
    private BeanMappingService mapping;

    @Tested(fullyInitialized = true)
    private VisitFacadeImpl visitFacade;


    @Before
    public void setUp() {
        // note: for tests here, password hash and password are the same
        hunter1 = createHunter("Alphonse", "Elric", "theGoodGuy", false);
        hunter1.setPasswordHash("armor");
        hunter1.setId(1L);
        hunter1DTO = mapping.mapTo(hunter1, MushroomHunterDTO.class);

        hunter2 = createHunter("Edward", "Elric", "fullmetal", true);
        hunter2.setPasswordHash("winry");
        hunter2.setId(2L);
        hunter2DTO = mapping.mapTo(hunter2, MushroomHunterDTO.class);

        forest1 = createForest("Listnaty", "Listnaty les");
        forest1.setId(1L);
        forest1DTO = mapping.mapTo(forest1, ForestDTO.class);

        forest2 = createForest("Ihlicnaty", "Ihlicnaty les");
        forest2.setId(2L);
        forest2DTO = mapping.mapTo(forest2, ForestDTO.class);

        mushroom1 = createMushroom("Hrib Dubovy", MushroomType.EDIBLE, "June", "July");
        mushroom1.setId(1L);
        mushroom1DTO = mapping.mapTo(mushroom1, MushroomDTO.class);

        mushroom2 = createMushroom("Hrib Satan", MushroomType.POISONOUS, "July", "September");
        mushroom2.setId(2L);
        mushroom2DTO = mapping.mapTo(mushroom2, MushroomDTO.class);

        visit1 = TestUtils.createVisit(hunter1, forest1, LocalDate.now());
        visit1.addMushroom(mushroom1);
        visit1.setId(1L);
        visit1DTO = mapping.mapTo(visit1, VisitDTO.class);

        visit2 = TestUtils.createVisit(hunter2, forest2, LocalDate.now());
        visit2.setId(2L);
        visit2DTO = mapping.mapTo(visit2, VisitDTO.class);

        new Expectations() {{
            visitService.findVisitById(anyLong);
            result = new Delegate() {
                Visit foo(Long id) {
                    if (id.equals(1L)) {
                        return visit1;
                    }
                    if (id.equals(2L)) {
                        return visit2;
                    }
                    return null;
                }
            };
            minTimes = 0;

            visitService.createVisit((Visit) any);
            result = new Delegate() {
                void foo() {
                }
            };
            minTimes = 0;

            visitService.deleteVisit((Visit) any);
            result = new Delegate() {
                void foo() {
                }
                // do nothing
            };
            minTimes = 0;

            visitService.updateVisit((Visit) any);
            result = new Delegate() {
                void foo() {
                }
                // do nothing
            };
            minTimes = 0;

            visitService.findAllVisits();
            result = new Delegate() {
                List<Visit> foo() {
                    List<Visit> res = new ArrayList<>();
                    res.add(visit1);
                    res.add(visit2);
                    return res;
                }
            };
            minTimes = 0;

            visitService.getVisitsByHunter((MushroomHunter) any);
            result = new Delegate() {
                List<Visit> foo(MushroomHunter hunter) {
                    if (hunter.getUserNickname().equals("theGoodGuy")) {
                        List<Visit> visitsByHunter1 = new ArrayList<>();
                        visitsByHunter1.add(visit1);
                        return visitsByHunter1;
                    } else if (hunter.getUserNickname().equals("fullmetal")) {
                        List<Visit> visitsByHunter2 = new ArrayList<>();
                        visitsByHunter2.add(visit2);
                        return visitsByHunter2;
                    } else
                        return null;
                }
            };
            minTimes = 0;

            visitService.getVisitsByForest((Forest) any);
            result = new Delegate() {
                List<Visit> foo(Forest forest) {
                    if (forest.getName().equals("Listnaty")) {
                        List<Visit> visitsByForest1 = new ArrayList<>();
                        visitsByForest1.add(visit1);
                        return visitsByForest1;
                    } else if (forest.getName().equals("Ihlicnaty")) {
                        List<Visit> visitsByForest2 = new ArrayList<>();
                        visitsByForest2.add(visit2);
                        return visitsByForest2;
                    } else
                        return null;
                }
            };
            minTimes = 0;

            visitService.getVisitsByMushroom((Mushroom) any);
            result = new Delegate() {
                List<Visit> foo(Mushroom mushroom) {
                    if (mushroom.getName().equals("Hrib Dubovy")) {
                        List<Visit> visitsByHunter1 = new ArrayList<>();
                        visitsByHunter1.add(visit1);
                        return visitsByHunter1;
                    } else if (mushroom.getName().equals("Hrib Satan")) {
                        List<Visit> visitsByHunter2 = new ArrayList<>();
                        visitsByHunter2.add(visit2);
                        return visitsByHunter2;
                    } else
                        return null;
                }
            };
            minTimes = 0;

        }};
    }

    @Test
    public void findVisitsByHunter() {
        assertThat(visitFacade.listAllVisitsForMushroomHunter(hunter1DTO)).containsExactlyInAnyOrder(visit1DTO);
        assertThat(visitFacade.listAllVisitsForMushroomHunter(hunter2DTO)).containsExactlyInAnyOrder(visit2DTO);
    }

    @Test
    public void findVisitsByForest() {
        assertThat(visitFacade.listAllVisitsForForest(forest1DTO)).containsExactlyInAnyOrder(visit1DTO);
        assertThat(visitFacade.listAllVisitsForForest(forest2DTO)).containsExactlyInAnyOrder(visit2DTO);
    }

    @Test
    public void findVisitsByMushroom() {
        assertThat(visitFacade.listAllVisitsByMushroom(mushroom1DTO)).containsExactlyInAnyOrder(visit1DTO);
        assertThat(visitFacade.listAllVisitsByMushroom(mushroom2DTO)).containsExactlyInAnyOrder(visit2DTO);
    }

    @Test
    public void findVisitById() {
        assertThat(visitFacade.findById(1L)).isEqualToComparingFieldByField(visit1DTO);
        assertThat(visitFacade.findById(2L)).isEqualToComparingFieldByField(visit2DTO);
        assertThat(visitFacade.findById(3L)).isNull();
    }

    @Test
    public void findAllVisits() {
        assertThat(visitFacade.listAllVisits()).containsExactlyInAnyOrder(visit1DTO, visit2DTO);
    }

    @Test
    public void deleteVisit() {
        assertThatThrownBy(() -> visitFacade.deleteVisit(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateVisit() {
        assertThatThrownBy(() -> visitFacade.updateVisit(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createVisit() {
        assertThatThrownBy(() -> visitFacade.createVisit(null)).isInstanceOf(IllegalArgumentException.class);
    }
}