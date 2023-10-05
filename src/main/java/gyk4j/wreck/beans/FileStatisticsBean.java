package gyk4j.wreck.beans;

import java.io.Serializable;

public class FileStatisticsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String statistic;
	private final int count;
	
	public FileStatisticsBean(String statistic, int count) {
		super();
		this.statistic = statistic;
		this.count = count;
	}

	public String getStatistic() {
		return statistic;
	}

	public int getCount() {
		return count;
	}
}
