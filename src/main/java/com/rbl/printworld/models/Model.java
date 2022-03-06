package com.rbl.printworld.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Model implements ResponseModel {
	@Id
	private String id;
	private String name;
	private String description;
	private String nameFile;
	private String extension;
	private int numberElement;
	private int note;
	private long size;
	private int categoryId;
	private List<Integer> subCategoryIds;
	private List<String> imageIds;
	private String userId;
}
