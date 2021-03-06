package com.rob.betBot.exchange.betfair.model;

import java.util.List;

public class PlaceExecutionReport {

	private String customerRef;
	private ExecutionReportStatusEnum status;
	private ExecutionReportErrorCodeEnum errorCode;
	private String marketId;
	private List<PlaceInstructionReport> instructionReports;

	public String getCustomerRef() {
		return customerRef;
	}

	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}

	public ExecutionReportStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ExecutionReportStatusEnum status) {
		this.status = status;
	}

	public ExecutionReportErrorCodeEnum getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ExecutionReportErrorCodeEnum errorCode) {
		this.errorCode = errorCode;
	}

	public String getMarketId() {
		return marketId;
	}

	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	public List<PlaceInstructionReport> getInstructionReports() {
		return instructionReports;
	}

	public void setInstructionReports(
			List<PlaceInstructionReport> instructionReports) {
		this.instructionReports = instructionReports;
	}
}
