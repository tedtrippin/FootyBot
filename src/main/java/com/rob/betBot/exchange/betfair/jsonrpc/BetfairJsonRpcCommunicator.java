package com.rob.betBot.exchange.betfair.jsonrpc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.exception.CommunicatorException;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exchange.betfair.model.CancelExecutionReport;
import com.rob.betBot.exchange.betfair.model.CancelInstruction;
import com.rob.betBot.exchange.betfair.model.ClearedOrderSummaryReport;
import com.rob.betBot.exchange.betfair.model.CurrentOrderSummaryReport;
import com.rob.betBot.exchange.betfair.model.Error;
import com.rob.betBot.exchange.betfair.model.EventTypeResult;
import com.rob.betBot.exchange.betfair.model.MarketBook;
import com.rob.betBot.exchange.betfair.model.MarketCatalogue;
import com.rob.betBot.exchange.betfair.model.MarketFilter;
import com.rob.betBot.exchange.betfair.model.PlaceExecutionReport;
import com.rob.betBot.exchange.betfair.model.PlaceInstruction;
import com.rob.betBot.http.JsonCommunicator;
import com.rob.betBot.util.JsonUtils;

public class BetfairJsonRpcCommunicator extends JsonCommunicator {

    private static Logger log = LogManager.getLogger(BetfairJsonRpcCommunicator.class);

    private String applicationKey = "";
    private final AtomicInteger requestId = new AtomicInteger(1);
    private final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

    // Param's
    private final String BET_IDS = "betIds";
    private final String CURRENCY_CODE = "currencyCode";
    private final String CUSTOMER_REF = "customerRef";
    private final String FILTER = "filter";
    private final String INSTRUCTIONS = "instructions";
    private final String LOCALE = "locale";
    private final String MAX_RESULT = "maxResults";
    private final String MARKET_IDS = "marketIds";
    private final String MARKET_ID = "marketId";
    private final String MARKET_PROJECTION = "marketProjection";
//    private final String MATCH_PROJECTION = "matchProjection";
//    private final String ORDER_PROJECTION = "orderProjection";
//    private final String PRICE_PROJECTION = "priceProjection";
    private final String SORT = "sort";

    private final String locale = Locale.getDefault().toString();
    private final String currencyCode = "GBP";

    // Methods
    private final String CANCEL_ORDERS = "cancelOrders";
    private final String LIST_CLEARED_ORDERS = "listClearedOrders";
    private final String LIST_CURRENT_ORDERS = "listCurrentOrders";
    private final String LIST_EVENT_TYPES = "listEventTypes";
    private final String LIST_MARKET_CATALOGUE = "listMarketCatalogue";
    private final String LIST_MARKET_BOOK = "listMarketBook";
    private final String PLACE_ORDERS = "placeOrders";

    public BetfairJsonRpcCommunicator(String sessionKey) {

        super(getBetfairUrl());

        applicationKey = Property.getProperty(PropertyKey.BETFAIR_APPLICATION_KEY);
        headers.putSingle("X-Application", applicationKey);
        headers.putSingle("X-Authentication", sessionKey);
    }

    public List<EventTypeResult> listEventTypes(MarketFilter filter)
        throws ExchangeException {

        Map<String, Object> params = new HashMap<>();
        params.put(LOCALE, locale);
        params.put(FILTER, filter);

        ListEventTypesResponse response = post(LIST_EVENT_TYPES, params, ListEventTypesResponse.class);
        return response.getEventTypeResults();
    }

    public List<MarketCatalogue> listMarketCatalogue(MarketFilter filter, int maxResults)
        throws ExchangeException {

        Map<String, Object> params = new HashMap<>();
        params.put(LOCALE, locale);
        params.put(FILTER, filter);
        params.put(SORT, "FIRST_TO_START");
        params.put(MAX_RESULT, String.valueOf(maxResults));
        params.put(MARKET_PROJECTION, new String[] {"EVENT", "MARKET_START_TIME", "RUNNER_DESCRIPTION"});

        ListMarketCatalogueResponse response = post(LIST_MARKET_CATALOGUE, params, ListMarketCatalogueResponse.class);
        return response.getMarketCatalogues();
    }

    public List<MarketBook> listMarketBook(List<String> marketIds)
        throws ExchangeException {

        Map<String, Object> params = new HashMap<>();
        params.put(LOCALE, locale);
        params.put(CURRENCY_CODE, currencyCode);
        params.put(MARKET_IDS, marketIds);

        ListMarketBookResponse response = post(LIST_MARKET_BOOK, params, ListMarketBookResponse.class);
        return response.getMarketBooks();
    }

    public PlaceExecutionReport placeOrders(String marketId , List<PlaceInstruction> instructions, String customerRef)
        throws ExchangeException {

        Map<String, Object> params = new HashMap<>();
        params.put(MARKET_ID, marketId);
        params.put(INSTRUCTIONS, instructions);
        params.put(CUSTOMER_REF, customerRef);

        PlaceExecutionReportResponse response = post(PLACE_ORDERS, params, PlaceExecutionReportResponse.class);
        return response.getPlaceExecutionReport();
    }

    public CurrentOrderSummaryReport listCurrentOrders(Collection<String> betIds)
        throws ExchangeException {

        Map<String, Object> params = new HashMap<>();
        params.put(BET_IDS, betIds);

        ListCurrentOrdersResponse response = post(LIST_CURRENT_ORDERS, params, ListCurrentOrdersResponse.class);
        return response.getCurrentOrderSummaryReport();
    }

    public ClearedOrderSummaryReport listClearedOrders(Collection<String> betIds)
        throws ExchangeException {

        Map<String, Object> params = new HashMap<>();
        params.put(BET_IDS, betIds);

        ListClearedOrdersResponse response = post(LIST_CLEARED_ORDERS, params, ListClearedOrdersResponse.class);
        return response.getClearedOrderSummaryReport();
    }

    /**
     * Sends instructions to cancel bets. Note, all bets must belong to s
     * the same market.
     *
     * @param betIds Bet IDs of bets on a common market
     * @return
     * @throws ExchangeException
     */
    public CancelExecutionReport cancelOrders (Collection<String> betIds, String marketId)
        throws ExchangeException {

        Map<String, Object> params = new HashMap<>();
        List<CancelInstruction> instructions = new ArrayList<>();
        for (String betId: betIds) {
            instructions.add(new CancelInstruction(betId, null));
        }

        params.put(INSTRUCTIONS, instructions);
        params.put(MARKET_ID, marketId);
        params.put(CUSTOMER_REF, UUID.randomUUID().toString().replaceAll("-", ""));

        CancelExecutionReportResponse response = post(CANCEL_ORDERS, params, CancelExecutionReportResponse.class);
        return response.getCancelExecutionReport();
    }

    private <T extends BetfairResponse> T post(String method, Map<String, Object> params, Class<T> responseClass)
        throws ExchangeException {

        try {
            String postDataString = getPostData(method, params);
            if (log.isDebugEnabled())
                log.debug("Posting to method[" + method + "] data[" + postDataString + "]");

            String responseJson = post(postDataString, method, headers);
            if (log.isDebugEnabled())
                log.debug("  response[" + responseJson + "]");

            T response = JsonUtils.readValue(responseJson, responseClass);

            Error error = response.getError();
            if (error != null) {
                if (error.getData() != null && error.getData().getAPINGException() != null)
                    throw new ExchangeException("Error calling[" + method + "]", error.getData().getAPINGException());
                else
                    throw new ExchangeException("Error calling[" + method + "], error[" + error.getMessage() + "]");
            }

            return response;

        } catch (CommunicatorException ex) {
            throw new ExchangeException(ex.getMessage(), ex);
        }
    }

    private String getPostData(String method, Map<String, Object> params) {

        JsonObject json = new JsonObject();
        json.addProperty("jsonrpc", "2.0");
        json.addProperty("method", "SportsAPING/v1.0/" + method);
        json.add("params", JsonUtils.toJsonElement(params));
        json.addProperty("id", String.valueOf(requestId.getAndIncrement()));

        return json.toString();
    }

    private static String getBetfairUrl() {
        return Property.getProperty(PropertyKey.BETFAIR_JSON_RPC_URL);
    }
}
