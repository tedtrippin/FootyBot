package com.rob.betBot.exchange.betfair.model;

public class BetfairJsonRpcException extends Exception {

    private static final long serialVersionUID = 1L;

    private String errorDetails;
	private String errorCode;
	private String requestUUID;

    public BetfairJsonRpcException() {
        super();
    }

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getRequestUUID() {
		return requestUUID;
	}

	public void setRequestUUID(String requestUUID) {
		this.requestUUID = requestUUID;
	}

	@Override
	public String toString() {
		return new StringBuilder()
		    .append("ErrorCode=").append(errorCode)
		    .append(",ErrorDetails=").append(errorDetails)
		    .append(",RequestUUID=").append(requestUUID).toString();
	}
}
