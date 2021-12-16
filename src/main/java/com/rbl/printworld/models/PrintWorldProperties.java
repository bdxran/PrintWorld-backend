package com.rbl.printworld.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PrintWorldProperties {
	private String tmp;
	private String repositoryData;

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public String getRepertoryData() {
		return repositoryData;
	}

	public void setRepertoryData(String repositoryData) {
		this.repositoryData = repositoryData;
	}

	@Override
	public String toString() {
		return "PrintWorld{" +
				"tmp='" + tmp + '\'' +
				", repertoryDate='" + repositoryData + '\'' +
				'}';
	}
}
