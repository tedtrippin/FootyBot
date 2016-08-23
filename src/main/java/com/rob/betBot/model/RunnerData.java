package com.rob.betBot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "runner")
public class RunnerData extends DataObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "exchange_id")
    private int exchangeId;

    @Column (name = "exchange_runner_id")
    private long exchangeRunnerId;

    @Column (name = "runner_name")
    private String runnerName;

    public RunnerData() {
    }

    public RunnerData(long id, int exchangeId, long exchangeRunnerId, String runnerName) {

        super(id);

        this.exchangeId = exchangeId;
        this.exchangeRunnerId = exchangeRunnerId;
        this.runnerName = runnerName;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public long getExchangeRunnerId() {
        return exchangeRunnerId;
    }

    public void setExchangeRunnerId(long exchangeRunnerId) {
        this.exchangeRunnerId = exchangeRunnerId;
    }

    public String getRunnerName() {
        return runnerName;
    }

    public void setRunnerName(String runnerName) {
        this.runnerName = runnerName;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof RunnerData))
            return false;

        return id == ((RunnerData)o).id;
    }

    @Override
    public int hashCode() {
        return (int) (exchangeId ^ id);
    }

    @Override
    public String toString() {
        return new StringBuilder("runnerName:").append(runnerName)
            .append(",exchangeRunnerId:").append(exchangeRunnerId).toString();
    }
}
