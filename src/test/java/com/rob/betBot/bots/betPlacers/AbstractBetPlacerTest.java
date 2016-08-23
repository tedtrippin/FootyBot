package com.rob.betBot.bots.betPlacers;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.rob.betBot.Bet;
import com.rob.betBot.BetManager;
import com.rob.betBot.Event;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;

public class AbstractBetPlacerTest {

    @Test
    public void test_roundPrice() {

        AbstractBetPlacer testPlacer = new AbstractBetPlacer("test", "test", null, null) {

            @Override
            public Collection<Bet> placeBets(Event race, BetManager betManager)
                throws ExchangeException, BetPlaceException {
                return null;
            }

        };

        assertTrue (100 == testPlacer.roundPrice(101));
        assertTrue (110 == testPlacer.roundPrice(111));

        assertTrue (50 == testPlacer.roundPrice(51));
        assertTrue (55 == testPlacer.roundPrice(59));

        assertTrue (30 == testPlacer.roundPrice(31));
        assertTrue (38 == testPlacer.roundPrice(39));

        assertTrue (21 == testPlacer.roundPrice(21.1));
        assertTrue (29 == testPlacer.roundPrice(29.9));

        assertTrue (11 == testPlacer.roundPrice(11.1));
        assertTrue (19.5 == testPlacer.roundPrice(19.9));

        assertTrue (6.2 == testPlacer.roundPrice(6.21));
        assertTrue (6.8 == testPlacer.roundPrice(6.99));

        assertTrue (4.1 == testPlacer.roundPrice(4.11));
        assertTrue (4.9 == testPlacer.roundPrice(4.99));

        assertTrue (3.05 == testPlacer.roundPrice(3.06));
        assertTrue (3.95 == testPlacer.roundPrice(3.99));

        assertTrue (2.02 == testPlacer.roundPrice(2.03));
        assertTrue (2.98 == testPlacer.roundPrice(2.99));
    }
}
