package com.rob.betBot.exchange.betfair;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Sets;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonRpcCommunicator;
import com.rob.betBot.exchange.betfair.model.MarketCatalogue;
import com.rob.betBot.exchange.betfair.model.MarketFilter;
import com.rob.betBot.exchange.betfair.model.TimeRange;

public class BetfairEventLoader {

    private final BetfairHelper betfairHelper = new BetfairHelper();
    private final BetfairJsonRpcCommunicator betfair;
    private final BetfairHorseRacingConverter horseRacingConverter;

    public BetfairEventLoader(BetfairJsonRpcCommunicator betfair, BetfairHorseRacingConverter horseRacingConverter) {
        this.betfair = betfair;
        this.horseRacingConverter = horseRacingConverter;
    }

    public void loadHorses(String countryCode)
        throws ExchangeException {

        Calendar c = Calendar.getInstance();
        Date from = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 2);
        Date to = c.getTime();

        TimeRange timeRange = new TimeRange();
        timeRange.setFrom(betfairHelper.toBetfairDateString(from));
        timeRange.setTo(betfairHelper.toBetfairDateString(to));

        MarketFilter filter = betfairHelper.getHorseFilter();
        filter.setMarketCountries(Sets.newHashSet(countryCode.toUpperCase()));
        filter.setMarketStartTime(timeRange);
        List<MarketCatalogue> betfairMarkets = betfair.listMarketCatalogue(filter, 99);

        if (betfairMarkets == null || betfairMarkets.size() == 0)
            return;

        horseRacingConverter.convertEvents(betfairMarkets);
    }
}