package com.rob.betBot;

import com.rob.betBot.model.RunnerData;

public class Runner  {

    public RunnerData runnerData;

    public Runner(RunnerData runnerData) {
        this.runnerData = runnerData;
    }

    public RunnerData getRunnerData() {
        return runnerData;
    }

    public void setRunnerData(RunnerData runnerData) {
        this.runnerData = runnerData;
    }
}
