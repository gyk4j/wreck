package gyk4j.wreck.beans;

import java.io.Serializable;

public class MetadataStatisticsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String parser;
	private final String tag;
	private final int count;
	
	public MetadataStatisticsBean(String parser, String tag, int count) {
		super();
		this.parser = parser;
		this.tag = tag;
		this.count = count;
	}

	public String getParser() {
		return parser;
	}
	
	public String getTag() {
		return tag;
	}

	public int getCount() {
		return count;
	}
}
