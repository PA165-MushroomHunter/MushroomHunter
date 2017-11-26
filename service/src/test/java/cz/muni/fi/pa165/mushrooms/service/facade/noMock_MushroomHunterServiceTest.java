package cz.muni.fi.pa165.mushrooms.service.facade;

import cz.muni.fi.pa165.mushrooms.service.MushroomHunterService;
import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import mockit.Deencapsulation;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
import javax.inject.Named;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Matúš on 26.11.2017.
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class noMock_MushroomHunterServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Named("mushroomHunterServiceImpl")
    @Inject
    MushroomHunterService service;

    

    @Before
    public void setUp(){
        // note: for tests here, password hash and password are the same
        hunter1 = createMushroomHunter("Alphonse", "Elric", "theGoodGuy");
        hunter1.setPasswordHash("armor");
        Deencapsulation.setField(hunter1, "id", 1L);
        assertThat(hunter1.getId()).isEqualTo(1L);
        hunter2 = createMushroomHunter("Edward", "Elric", "fullmetal");
        hunter2.setPasswordHash("winry");
        Deencapsulation.setField(hunter2, "id", 2L);
        assertThat(hunter2.getId()).isEqualTo(2L);
        hunter2.setAdmin(true);
    }

}
