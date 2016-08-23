import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.collect.Lists;
import com.rob.betBot.AppConfig;
import com.rob.betBot.Event;
import com.rob.betBot.EventLoader;
import com.rob.betBot.conf.Property;
import com.rob.betBot.exchange.betfair.BetfairHorseRacingConverter;
import com.rob.betBot.exchange.betfair.model.MarketCatalogue;
import com.rob.betBot.mvc.MvcConfig;
import com.rob.betBot.util.JsonUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class, MvcConfig.class})
public class HibernateTests {

    @Autowired
    BetfairHorseRacingConverter betfairHorseRacingConverter;

    @Autowired
    EventLoader eventLoader;

    @BeforeClass
    public static void init()
        throws IOException {

        Properties properties = new Properties();
        InputStream propertiesStream = BotRandomTests.class.getClassLoader().getResourceAsStream("betbot.properties");
        properties.load(propertiesStream);
        new Property().onLoad(properties);
    }

    @Test
    public void test_loadBetfairEvent()
        throws FileNotFoundException {

        MarketCatalogue toBePlaced = JsonUtils.readValue(new FileReader("c:/tmp/toBePlaced.json"), MarketCatalogue.class);
        MarketCatalogue winner = JsonUtils.readValue(new FileReader("c:/tmp/winner.json"), MarketCatalogue.class);

        List<MarketCatalogue> marketCatalogueList = Lists.newArrayList(toBePlaced, winner);

        betfairHorseRacingConverter.convertEvents(marketCatalogueList);
    }

    @Test
    public void test_loadEvents() {

        List<Event> events = eventLoader.loadEvents(2, 0);
        System.out.println("Done - " + events.size());
    }
}
