package com.rob.betBot.exchange.combined;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonLoginCommunicator;

public class WilliamHillBetfairFootballBetManagerTest extends WilliamHillBetfairFootballBetManager {

    public WilliamHillBetfairFootballBetManagerTest() {
        super (createMockCommunicator(), null);
    }

    @Test
    public void test_HomeRunner() {

        String betfairRunner = "2 - 1";
        String whRunner = "Aston Villa 2-1";
        String eventName = "Aston Villa v Manchester utd";

        String result = normalizeCorrectScore(whRunner, eventName);

        assertTrue("Expected " + betfairRunner, betfairRunner.equals(result));
    }

    @Test
    public void test_AwayRunner() {

        String betfairRunner = "1 - 2";
        String whRunner = "Manchester utd 2-1";
        String eventName = "Aston Villa v Manchester utd";

        String result = normalizeCorrectScore(whRunner, eventName);

        assertTrue("Expected " + betfairRunner, betfairRunner.equals(result));
    }

    private static BetfairJsonLoginCommunicator createMockCommunicator() {

        BetfairJsonLoginCommunicator mock = mock(BetfairJsonLoginCommunicator.class);
        when(mock.getSessionToken()).thenReturn("wibble");
        return mock;
    }
}
