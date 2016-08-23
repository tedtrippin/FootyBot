package com.rob.fakeBetfair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.rob.betBot.exchange.betfair.jsonrpc.ListClearedOrdersResponse;
import com.rob.betBot.exchange.betfair.jsonrpc.ListCurrentOrdersResponse;
import com.rob.betBot.exchange.betfair.jsonrpc.ListMarketBookResponse;
import com.rob.betBot.exchange.betfair.jsonrpc.ListMarketCatalogueResponse;
import com.rob.betBot.exchange.betfair.jsonrpc.PlaceExecutionReportResponse;
import com.rob.betBot.exchange.betfair.model.Competition;
import com.rob.betBot.exchange.betfair.model.Event;
import com.rob.betBot.exchange.betfair.model.ExchangePrices;
import com.rob.betBot.exchange.betfair.model.ExecutionReportStatusEnum;
import com.rob.betBot.exchange.betfair.model.InstructionReportStatusEnum;
import com.rob.betBot.exchange.betfair.model.LimitOrder;
import com.rob.betBot.exchange.betfair.model.MarketBook;
import com.rob.betBot.exchange.betfair.model.MarketCatalogue;
import com.rob.betBot.exchange.betfair.model.MarketDescription;
import com.rob.betBot.exchange.betfair.model.MarketStatusEnum;
import com.rob.betBot.exchange.betfair.model.PlaceExecutionReport;
import com.rob.betBot.exchange.betfair.model.PlaceInstruction;
import com.rob.betBot.exchange.betfair.model.PlaceInstructionReport;
import com.rob.betBot.exchange.betfair.model.PriceSize;
import com.rob.betBot.exchange.betfair.model.Runner;
import com.rob.betBot.exchange.betfair.model.RunnerCatalog;

@Path ("/fakebetfair")
public class FakeBetfair {

    private final String MARKET_ID = "FakeMarketID";
    private final String TEAM_A = "Preston";
    private final String TEAM_B = "Swindon";
    private final String EVENT_NAME = TEAM_A + " v " + TEAM_B;
    private int numberOfCallsUntilClosed = 10;
    private int callCounter = 0;
    private Gson gson = new Gson();

    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public String getDefault() {
        return "{\"hello\":\"world\"}";
    }

    @POST
    @Path ("listClearedOrders")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public ListClearedOrdersResponse listClearedOrders() {

        ListClearedOrdersResponse listClearedOrdersResponse = new ListClearedOrdersResponse();
        return listClearedOrdersResponse;
    }

    @POST
    @Path ("listCurrentOrders")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public ListCurrentOrdersResponse listCurrentOrders() {

        ListCurrentOrdersResponse listCurrentOrdersResponse = new ListCurrentOrdersResponse();
        return listCurrentOrdersResponse;
    }

    @POST
    @Path ("listMarketCatalogue")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public ListMarketCatalogueResponse listMarketCatalogue() {

        Competition competition = new Competition();
        competition.setId("competitionId");
        competition.setName("FakeBetfair competition");

        MarketDescription marketDescription = new MarketDescription();

        Event event = new Event();
        event.setId(UUID.fromString("00000000-0000-0000-0000-000000000001").toString());
        event.setName(EVENT_NAME);
        event.setVenue("MyArse stadium");

        RunnerCatalog teamA = new RunnerCatalog();
        teamA.setRunnerName(TEAM_A);
        teamA.setSelectionId(1L);

        RunnerCatalog teamB = new RunnerCatalog();
        teamB.setRunnerName(TEAM_B);
        teamB.setSelectionId(2L);

        RunnerCatalog drawRunner = new RunnerCatalog();
        drawRunner.setRunnerName("draw");
        drawRunner.setSelectionId(3L);

        List<RunnerCatalog> runners = new ArrayList<>();
        runners.add(teamA);
        runners.add(teamB);
        runners.add(drawRunner);

        MarketCatalogue marketCatalogue = new MarketCatalogue();
        marketCatalogue.setCompetition(competition);
        marketCatalogue.setDescription(marketDescription);
        marketCatalogue.setEvent(event);
        marketCatalogue.setMarketId(MARKET_ID);
        marketCatalogue.setMarketName("winner");
        marketCatalogue.setRunners(runners);

        List<MarketCatalogue> marketCatalogues = new ArrayList<>();
        marketCatalogues.add(marketCatalogue);

        ListMarketCatalogueResponse listMarketCatalogueResponse = new ListMarketCatalogueResponse();
        listMarketCatalogueResponse.setResult(marketCatalogues);
        return listMarketCatalogueResponse;
    }

    @POST
    @Path ("listMarketBook")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public ListMarketBookResponse listMarketBook() {

        PriceSize price = new PriceSize();
        price.setPrice(5d);
        price.setSize(100d);
        ExchangePrices prices = new ExchangePrices();
        prices.setAvailableToBack(Lists.newArrayList(price));

        Runner teamA = new Runner(1L);
        teamA.setEx(prices);
        Runner teamB = new Runner(2L);
        teamB.setEx(prices);
        Runner drawRunner = new Runner(3L);
        drawRunner.setEx(prices);

        List<Runner> runners = new ArrayList<>();
        runners.add(teamA);
        runners.add(teamB);
        runners.add(drawRunner);

        MarketBook marketBook = new MarketBook();
        marketBook.setMarketId(MARKET_ID);
        marketBook.setRunners(runners);

        if (callCounter++ > numberOfCallsUntilClosed)
            marketBook.setStatus(MarketStatusEnum.CLOSED);

        List<MarketBook> marketBooks = new ArrayList<>();

        ListMarketBookResponse listMarketBookResponse = new ListMarketBookResponse();
        listMarketBookResponse.setResult(marketBooks);
        return listMarketBookResponse;
    }

    @POST
    @Path ("placeOrders")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public PlaceExecutionReportResponse placeOrders(BetfairRequest betFairRequest) {

        List<HashMap> stringMap = (List<HashMap>) betFairRequest.getParams().get("instructions");
        String s = stringMap.get(0).toString();
        PlaceInstruction instruction = gson.fromJson(s, PlaceInstruction .class);
        LimitOrder limitOrder = instruction.getLimitOrder();

        PlaceInstructionReport instructionReport = new PlaceInstructionReport();
        instructionReport.setStatus(InstructionReportStatusEnum.SUCCESS);
        instructionReport.setAveragePriceMatched(limitOrder.getPrice());
        instructionReport.setBetId(UUID.randomUUID().toString());
        instructionReport.setSizeMatched(limitOrder.getSize());
        List<PlaceInstructionReport> instructionReports = Lists.newArrayList(instructionReport);

        PlaceExecutionReport placeExecutionReport = new PlaceExecutionReport();
        placeExecutionReport.setMarketId(MARKET_ID);
        placeExecutionReport.setStatus(ExecutionReportStatusEnum.SUCCESS);
        placeExecutionReport.setInstructionReports(instructionReports);

        PlaceExecutionReportResponse placeExecutionReportResponse = new PlaceExecutionReportResponse();
        placeExecutionReportResponse.setResult(placeExecutionReport);

        return placeExecutionReportResponse;
    }

    static class BetfairRequest {

        private String jsonrpc;
        private String method;
        private Map<Object, Object> params;
        private String id;

        public String getJsonrpc() {
            return jsonrpc;
        }

        public void setJsonrpc(String jsonrpc) {
            this.jsonrpc = jsonrpc;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Map<Object, Object> getParams() {
            return params;
        }

        public void setParams(Map<Object, Object> params) {
            this.params = params;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
