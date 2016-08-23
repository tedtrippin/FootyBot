package com.rob.betBot.exchange.betfair.jsonrpc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.LoginFailedException;
import com.rob.betBot.exchange.betfair.BetfairHelper;
import com.rob.betBot.exchange.betfair.model.EventTypeResult;
import com.rob.betBot.exchange.betfair.model.MarketCatalogue;
import com.rob.betBot.exchange.betfair.model.MarketFilter;

@Ignore
public class ListMarketTest {

    @Test
    public void testListEventTypes()
        throws ExchangeException, LoginFailedException {

        String sessionToken = "PumhA65JiA1depsFFJoa4JC+khVP5vpXsNaVZjLjM4s=";

        MarketFilter filter = new MarketFilter();
        Set<String> footballMarketTypes = new HashSet<>();
        footballMarketTypes.add("CORRECT_SCORE");

        BetfairJsonRpcCommunicator betfair = new BetfairJsonRpcCommunicator(sessionToken);
        List<EventTypeResult> eventTypes = betfair.listEventTypes(filter);

        if (eventTypes == null) {
            System.out.println("No eventTypes found");
            return;
        }

        System.out.println("Found " + eventTypes.size() + " eventTypess");
        for (EventTypeResult eventType: eventTypes) {
            System.out.println(" eventType: " + eventType);
        }
    }

    @Test
    public void testListMarket()
        throws ExchangeException, LoginFailedException {

        String sessionToken = "PumhA65JiA1depsFFJoa4JC+khVP5vpXsNaVZjLjM4s=";

        BetfairHelper betfairHelper = new BetfairHelper();
        MarketFilter filter = betfairHelper.getFootballFilter();
        filter.setTextQuery("Middlesbrough v Norwich");

//        BetfairJsonRpcCommunicator betfair = new BetfairJsonRpcCommunicator(betfairSso.getSessionToken());
        BetfairJsonRpcCommunicator betfair = new BetfairJsonRpcCommunicator(sessionToken);
        List<MarketCatalogue> markets = betfair.listMarketCatalogue(filter, 9);

        if (markets == null) {
            System.out.println("No markets found");
            return;
        }

        System.out.println("Found " + markets.size() + " markets");
        for (MarketCatalogue market : markets) {
            System.out.println(" event: " + market.getEvent().getName() + ", market: " + market.getMarketName());
        }
    }

    @Test
    public void testLogin() throws ExchangeException, LoginFailedException {

        BetfairJsonLoginCommunicator betfairSso = new BetfairJsonLoginCommunicator();
        betfairSso.login("TedTrippin", "bW188leee");
        System.out.println("SessionToken: " + betfairSso.getSessionToken());
        System.out.println("app key: " + Property.getProperty(PropertyKey.BETFAIR_APPLICATION_KEY));
    }

    @Test
    public void testReplace() {
        String s = "draw 3-3";
        System.out.println(s.replaceAll("draw ", ""));
    }
}
