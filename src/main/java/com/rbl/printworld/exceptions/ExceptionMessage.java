package com.rbl.printworld.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {
	private String date;
	private String path;
	private String className;
	private String message;
	private Exception exception;
}
