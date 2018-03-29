package com.rob.betBot.exchange.betfair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.Bet;
import com.rob.betBot.BetRequest;
import com.rob.betBot.BetTypeEnum;
import com.rob.betBot.exception.BetCancelException;
import com.rob.betBot.exception.BetNotFoundException;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.InsufficientFundsException;
import com.rob.betBot.exchange.AbstractBetManager;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonRpcCommunicator;
import com.rob.betBot.exchange.betfair.model.CancelExecutionReport;
import com.rob.betBot.exchange.betfair.model.CancelInstructionReport;
import com.rob.betBot.exchange.betfair.model.ClearedOrderSummary;
import com.rob.betBot.exchange.betfair.model.ClearedOrderSummaryReport;
import com.rob.betBot.exchange.betfair.model.CurrentOrderSummary;
import com.rob.betBot.exchange.betfair.model.CurrentOrderSummaryReport;
import com.rob.betBot.exchange.betfair.model.ExecutionReportErrorCodeEnum;
import com.rob.betBot.exchange.betfair.model.InstructionReportErrorCodeEnum;
import com.rob.betBot.exchange.betfair.model.InstructionReportStatusEnum;
import com.rob.betBot.exchange.betfair.model.LimitOrder;
import com.rob.betBot.exchange.betfair.model.OrderTypeEnum;
import com.rob.betBot.exchange.betfair.model.PersistenceTypeEnum;
import com.rob.betBot.exchange.betfair.model.PlaceExecutionReport;
import com.rob.betBot.exchange.betfair.model.PlaceInstruction;
import com.rob.betBot.exchange.betfair.model.PlaceInstructionReport;
import com.rob.betBot.exchange.betfair.model.SideEnum;

public class BetfairBetManager extends AbstractBetManager {

    private static Logger log = LogManager.getLogger(BetfairBetManager.class);
    private static Logger journal = LogManager.getLogger("journal");

    protected BetfairJsonRpcCommunicator betfair;

    public BetfairBetManager(BetfairJsonRpcCommunicator betfair) {
        this.betfair = betfair;
    }

    @Override
    public void updateBets(Collection<Bet> bets)
        throws ExchangeException, BetNotFoundException {

        Map<String, Bet> betsMap = new HashMap<>();
        Collection<String> betIds = new ArrayList<>();
        bets.forEach(b -> { betsMap.put(b.getBetId(), b); betIds.add(b.getBetId()); });

        //  See if bet is current
        CurrentOrderSummaryReport report = betfair.listCurrentOrders(betIds);
        List<CurrentOrderSummary> summaries = report.getCurrentOrders();
        if (summaries == null || summaries.size() == 0)
            throw new BetNotFoundException("");

        for (CurrentOrderSummary summary : summaries) {
            Bet bet = betsMap.get(summary.getBetId());
            ((BetfairBet)bet).update(summary);
            betIds.remove(summary.getBetId());
        }

        // couldn't find it? Lets see if its cleared
        ClearedOrderSummaryReport clearedReport = betfair.listClearedOrders(betIds);
        List<ClearedOrderSummary> clearedSummaries = clearedReport.getClearedOrders();
        if (clearedSummaries == null || clearedSummaries.size() == 0)
            throw new BetNotFoundException("");

        for (ClearedOrderSummary clearedSummary : clearedSummaries) {
            Bet bet = betsMap.get(clearedSummary.getBetId());
            ((BetfairBet)bet).update(clearedSummary);
        }
    }

    @Override
    protected Collection<Bet> doPlaceBets(Collection<BetRequest> betRequests)
        throws ExchangeException, BetPlaceException {

        String s = "Placing " + betRequests.size() + " bets on betfair";
        journal.info(s);
        log.debug(s);

        if (betRequests.size() == 0)
            return Collections.emptyList();

        List<PlaceInstruction> instructions = new ArrayList<>();
        String marketId = null;
        for (BetRequest request : betRequests) {
            String s2 = "  placing[" + request + "]";
            journal.info(s2);
            log.debug(s2);

            marketId = request.getMarketId();

            LimitOrder limitOrder = new LimitOrder();
            limitOrder.setPersistenceType(PersistenceTypeEnum.LAPSE);
            limitOrder.setPrice(request.getPrice());
            limitOrder.setSize(request.getAmount());

            PlaceInstruction placeInstruction = new PlaceInstruction();
            placeInstruction.setLimitOrder(limitOrder);
            placeInstruction.setSide(request.getBetType() == BetTypeEnum.BET ? SideEnum.BACK : SideEnum.LAY);
            placeInstruction.setSelectionId(request.getRunnerId());
            placeInstruction.setOrderType(OrderTypeEnum.LIMIT);

            instructions.add(placeInstruction);
        }

        String betReference = UUID.randomUUID().toString().replace("-", "");
        PlaceExecutionReport report = betfair.placeOrders(marketId, instructions, betReference);

        if (report.getErrorCode() == ExecutionReportErrorCodeEnum.MARKET_SUSPENDED) {
            log.debug("Problem placing bet, market suspended. Waiting 2 seconds then trying again");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
            return doPlaceBets(betRequests);
        }

        List<PlaceInstructionReport> reports = report.getInstructionReports();

        if (reports == null || reports.size() == 0)
            throw new BetPlaceException("Betfair.placeOrders: returned no report");

        Map<Long, BetRequest> betRequestsMap = new HashMap<>();
        betRequests.forEach(b -> betRequestsMap.put(b.getRunnerId(), b));

        List<Bet> bets = new ArrayList<>();
        for (PlaceInstructionReport confirmation : reports) {
            log.debug("  finished, betId[" + confirmation.getBetId() + "] status[" + confirmation.getStatus() + "]");
            switch (confirmation.getStatus()) {
                case SUCCESS :

                    journal.info(new StringBuilder("  bet placed[betId: ").append(confirmation.getBetId())
                        .append(", avg price: ").append(confirmation.getAveragePriceMatched())
                        .append("]").toString());

                    long selectionId = confirmation.getInstruction().getSelectionId();
                    BetRequest request = betRequestsMap.get(Long.valueOf(selectionId));
                    BetfairBet bet = new BetfairBet(confirmation.getBetId(), request);
                    bet.setPrice(confirmation.getInstruction().getLimitOrder().getPrice());
                    bet.setMatchedSoFar(confirmation.getSizeMatched());
                    bets.add(bet);
                    break;

                case FAILURE :
                    handleFailure(confirmation);

                case TIMEOUT :
                    journal.info("  bet timed out");
                    throw new BetPlaceException("Betfair.placeOrders: timeout");

                default :
                    journal.info("  bet failed cus of an unknown exception");
                    throw new BetPlaceException("Betfair.placeOrders: unknown error");
            }
        }

        return bets;
    }

    @Override
    protected Collection<Bet> doCancelBets(Collection<Bet> bets)
        throws ExchangeException, BetCancelException {

        journal.info("Attempting to cancel bets");
        if (bets.isEmpty()) {
            log.debug("  no bets");
            return Collections.emptyList();
        }

        List<String> betIds = new ArrayList<>();
        bets.forEach(b -> betIds.add(b.getBetId()));

        String marketId = bets.iterator().next().getMarketId();

        // TODO - what to do about this betfair market ID??? should really have a WHBetfairBet that
        // transposes market/runner IDs between calls to exchanges
        CancelExecutionReport cancelReport = betfair.cancelOrders(betIds, marketId);

        List<CancelInstructionReport> reports = cancelReport.getInstructionReports();
        if (reports == null || reports.size() == 0) {
            journal.info("  error - no reports received from betfair");
            throw new BetCancelException("all", cancelReport.getErrorCode().toString());
        }

        Map<String, Bet> betsMap = new HashMap<>();
        bets.forEach(b -> betsMap.put(b.getBetId(), b));

        Collection<Bet> partMatchedBets = new ArrayList<>();
        for (CancelInstructionReport report : reports) {
            if (report.getStatus() != InstructionReportStatusEnum.SUCCESS) {
                journal.warn("  betfair error[" + report.getStatus() + "]");
            }

            Bet bet = betsMap.get(report.getInstruction().getBetId());
            double amountMatched = bet.getAmount() - report.getSizeCancelled();
            if (amountMatched == 0) {
                journal.info("  " + report.getInstruction().getBetId() + " cancelled");
                continue;
            }

            journal.info("  " + report.getInstruction().getBetId()
                + " part cancelled, amount matched[" + amountMatched + "]");

            // If whole bet wasn't cancelled we return a bet with how much was matched
            BetfairBet partMatchedBet = new BetfairBet(bet.getBetId(), bet);
            partMatchedBet.setPricesInfo(bet.getPricesInfo());
            partMatchedBet.setMatchedSoFar(amountMatched);
        }

        return partMatchedBets;
    }


    private void handleFailure(PlaceInstructionReport confirmation)
        throws BetPlaceException {

        InstructionReportErrorCodeEnum error = confirmation.getErrorCode();
        journal.info("  bet failed - betfair error[" + error + "]");
        switch (error) {
            case INSUFFICIENT_FUNDS :
                throw new InsufficientFundsException(confirmation.getInstruction().getLimitOrder().getSize());
            default :
                throw new BetPlaceException("Betfair.placeOrders: betfair error[" + error + "]");
        }
    }
}
