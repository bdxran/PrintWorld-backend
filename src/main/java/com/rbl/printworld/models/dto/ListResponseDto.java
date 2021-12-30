package com.rbl.printworld.models.dto;

import com.rbl.printworld.models.ResponseModel;
import com.rbl.printworld.models.enums.RequestStatus;

import java.util.List;

public class ListResponseDto<T extends ResponseModel> extends ResponseDto {

	private Long count;
	private Integer totalPages;
	private List<T> data;

	public ListResponseDto(RequestStatus requestStatus, Long totalElements, Integer totalPages, List<T> elements) {
		super(requestStatus);
		this.count = totalElements;
		this.totalPages = totalPages;
		this.data = elements;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
