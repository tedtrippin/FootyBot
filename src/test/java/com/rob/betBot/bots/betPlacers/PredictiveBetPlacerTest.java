package com.rob.betBot.bots.betPlacers;

import static org.mockito.Mockito.mock;

import org.junit.Ignore;
import org.junit.Test;

import com.rob.betBot.Event;
import com.rob.betBot.TestRaceFactory;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.engine.BetSettler;
import com.rob.betBot.engine.EventBot;
import com.rob.betBot.engine.EventThread;
import com.rob.betBot.engine.EventThreadListener;
import com.rob.betBot.exchange.emulated.TestExchange;

public class PredictiveBetPlacerTest {

    @Ignore
    @Test
    public void test_SimpleTest() {

        PredictiveBetPlacer placer = new PredictiveBetPlacer();
        Bot testBot = new Bot("testBot");
        testBot.addBetPlacer(placer);
        EventBot testRaceBot = new EventBot(testBot, new TestExchange());

        BetSettler mockBetSettler = mock(BetSettler.class);
        Event testRace = TestRaceFactory.createRace();

        // Set start time to <tt>now-ESTIMATED_RACE_DURATION_S</tt>
        testRace.started(System.currentTimeMillis() - testRace.getEstimatedDurationMS());

        EventThread testRaceThread = new EventThread(testRace, testRaceBot, mockBetSettler, null, null,
            new EventThreadListener() {
                @Override
                public void eventThreadFinished(EventThread thread) {}
        });

        testRaceThread.run();
    }
}
