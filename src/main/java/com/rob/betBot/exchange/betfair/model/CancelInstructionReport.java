package com.rob.betBot.exchange.betfair.model;


public class CancelInstructionReport {

    private InstructionReportStatusEnum status;
    private InstructionReportErrorCodeEnum errorCode;
    private CancelInstruction instruction;
    private double sizeCancelled;
    private String cancelledDate;

    public CancelInstructionReport() {
    }

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

    public CancelInstruction getInstruction() {
        return instruction;
    }

    public void setInstruction(CancelInstruction instruction) {
        this.instruction = instruction;
    }

    public double getSizeCancelled() {
        return sizeCancelled;
    }

    public void setSizeCancelled(double sizeCancelled) {
        this.sizeCancelled = sizeCancelled;
    }

    public String getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(String cancelledDate) {
        this.cancelledDate = cancelledDate;
    }
}
