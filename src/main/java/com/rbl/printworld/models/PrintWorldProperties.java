package com.rbl.printworld.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrintWorldProperties {
	private String tmp;
	private String repositoryData;
	private String metaCounter;
	private String environment;

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public String getRepositoryData() {
		return repositoryData;
	}

	public void setRepositoryData(String repositoryData) {
		this.repositoryData = repositoryData;
	}

	public String getMetaCounter() {
		return metaCounter;
	}

	public void setMetaCounter(String metaCounter) {
		this.metaCounter = metaCounter;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	@Override
	public String toString() {
		return "PrintWorldProperties{" +
				"tmp='" + tmp + '\'' +
				", repositoryData='" + repositoryData + '\'' +
				", metaCounter='" + metaCounter + '\'' +
				", environment='" + environment + '\'' +
				'}';
	}
}
