package com.rob.betBot.exchange.williamHill;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rob.betBot.Event;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.MarketType;
import com.rob.betBot.PricesManager;
import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.dao.MarketDao;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.ExpiredException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.williamHill.model.WHEvent;
import com.rob.betBot.exchange.williamHill.model.WHMarket;
import com.rob.betBot.exchange.williamHill.model.WHSelection;
import com.rob.betBot.model.MarketData;
import com.rob.betBot.model.PricesData;

@Component
public class WilliamHillPricesManager implements PricesManager {

    private final static Logger journal = LogManager.getLogger("journal");
    private final static Logger log = LogManager.getLogger(WilliamHillPricesManager.class);
    private final static String LIVE_SCORE_MARKET = "Live Score";

    @Autowired
    private MarketDao marketDao;

    long timeOfNextCall = 0;

    @Override
    public boolean updateEvent(Event event, long currentTime)
        throws ExchangeException {

        if (timeOfNextCall > currentTime)
            return false;

        journal.error("Updating...");

        int randomInterval = (int)(Math.random()*10)-5;
        timeOfNextCall = currentTime + getPricesInterval() + (randomInterval*1000);

        if (log.isDebugEnabled()) {
            StringBuilder msg = new StringBuilder("Updating event[")
                .append(event.getEventData().getEventName()).append("], next update after[")
                .append(new Date(timeOfNextCall)).append("]");
            log.debug(msg);
            journal.debug(msg);
        }

        log.debug("Loading prices for event[" + event.getEventData().getEventName() + "]");

        WHEvent whEvent = null;
        String eventId = event.getEventData().getExchangeEventId();
        WilliamHillScraper scraper = new WilliamHillScraper(getPricesUrlPrefix() + eventId);
        try {
            whEvent = scraper.scrape(getMarketsEndString(), getEventsExpiredString(), WHEvent.class, getMarketsStartString());
        } catch (ExpiredException ex) {
            event.finished(currentTime);
            return true;
        } catch (IOException ex) {
            log.error("Couldnt scrape event[" + event.getEventData().getEventName() + "]", ex);
        }

        if (whEvent == null) {
            log.warn("Couldn't scrape event, assuming finished");
            journal.warn("Couldn't scrape event, assuming finished");
            event.setWarning("Couldn't scrape event, assuming finished");
            event.finished(currentTime);
            return true;
        }

        event.setWarning(null);

        // Build map of our current market IDs
        Map<String, MarketData> marketMap = new HashMap<>();
        event.getMarkets().forEach(m -> marketMap.put(m.getMarketData().getExchangeMarketId(), m.getMarketData()));

        if (!event.isInPlay()) {
            // Check if we've gone in-play. We do this by looking for the 'Live Score' market
            for (WHMarket market : whEvent.getMarkets()) {
                if (!market.getMkt_name().equalsIgnoreCase(LIVE_SCORE_MARKET))
                    continue;

                event.setInPlay(true);
                udpateMarketIds(whEvent, marketMap);
                break;
            }
        }

        for (WHMarket market : whEvent.getMarkets()) {

            String marketId = market.getEv_mkt_id();
            if (!marketMap.containsKey(marketId))
                continue;

            if (market.getStatus() == "C") {
                event.finished(currentTime);
                return true;
            }

            Map<Long, Double> runnerPriceMap = new HashMap<Long, Double>();
            for (WHSelection selection : whEvent.getSelections()) {
                if (!marketId.equals(selection.getEv_mkt_id()))
                    continue;

                // Work out decimal price
                int numerator = Integer.valueOf(selection.getLp_num());
                int denumerator = Integer.valueOf(selection.getLp_den());
                double price = (double)numerator/denumerator;
                price += 1;
                if (log.isDebugEnabled())
                    log.debug("  price: " + selection.getName() + " - " + price);

                // get runner ID
                long runnerId = Long.valueOf(selection.getEv_oc_id());

                runnerPriceMap.put(runnerId, price);
            }

            MarketData marketData = marketMap.get(marketId);
            PricesData pricesData = new PricesData(Exchange.WILLIAM_HILL_EXCHANGE_ID, event.getEventData().getId(),
                marketData.getId(), currentTime, runnerPriceMap);
            MarketPrices prices = new MarketPrices(pricesData);
            event.getMarket(marketId).setLatestPrices(prices);
        }

        return true;
    }

    private void udpateMarketIds(WHEvent whEvent, Map<String, MarketData> marketMap) {

        Collection<MarketData> marketDatas = marketMap.values();
        for (WHMarket market : whEvent.getMarkets()) {
            MarketType marketType = WilliamHillHelper.getMarketType(market.getMkt_name());
            if (marketType == null)
                continue;

            for (MarketData data : marketDatas) {
                if (data.getMarketType() != marketType)
                    continue;

                data.setExchangeMarketId(market.getEv_mkt_id());

                // Persist the new market ID
                try {
                    marketDao.saveOrUpdate(data);
                } catch (Exception ex) {
                    log.error("Unable to update market with in-play market ID");
                }

                break;
            }
        }
    }

    private String getPricesUrlPrefix() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_PRICES_URL_PREFIX);
    }

    private String getMarketsStartString() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_MARKETS_START_STRING);
    }

    private String getMarketsEndString() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_MARKETS_END_STRING);
    }

    private String getEventsExpiredString() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_EVENTS_EXPIRED_STRING);
    }

    private long getPricesInterval() {
        return Property.getPropertyAsLong(PropertyKey.WILLIAM_HILL_PRICES_INTERVAL) * 1000;
    }
}
