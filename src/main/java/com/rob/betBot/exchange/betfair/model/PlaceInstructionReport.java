package com.rob.betBot.exchange.betfair.model;


public class PlaceInstructionReport {

	private InstructionReportStatusEnum status;
	private InstructionReportErrorCodeEnum errorCode;
	private PlaceInstruction instruction;
	private String betId;
	private String placedDate;
	private double averagePriceMatched;
	private double sizeMatched;

	public InstructionReportStatusEnum getStatus() {
		return status;
	}

	public void setStatus(InstructionReportStatusEnum status) {
		this.status = status;
	}

	public InstructionReportErrorCodeEnum getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(InstructionReportErrorCodeEnum errorCode) {
		this.errorCode = errorCode;
	}

	public PlaceInstruction getInstruction() {
		return instruction;
	}

	public void setInstruction(PlaceInstruction instruction) {
		this.instruction = instruction;
	}

	public String getBetId() {
		return betId;
	}

	public void setBetId(String betId) {
		this.betId = betId;
	}

	public String getPlacedDate() {
		return placedDate;
	}

	public void setPlacedDate(String placedDate) {
		this.placedDate = placedDate;
	}

	public double getAveragePriceMatched() {
		return averagePriceMatched;
	}

	public void setAveragePriceMatched(double averagePriceMatched) {
		this.averagePriceMatched = averagePriceMatched;
	}

	public double getSizeMatched() {
		return sizeMatched;
	}

	public void setSizeMatched(double sizeMatched) {
		this.sizeMatched = sizeMatched;
	}

}
