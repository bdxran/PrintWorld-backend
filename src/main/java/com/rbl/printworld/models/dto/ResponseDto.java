package com.rbl.printworld.models.dto;

import com.rbl.printworld.models.enums.RequestStatus;

public class ResponseDto {
	private RequestStatus requestStatus;

	public ResponseDto(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public RequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}
}
