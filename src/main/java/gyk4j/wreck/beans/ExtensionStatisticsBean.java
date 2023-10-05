package gyk4j.wreck.beans;

import java.io.Serializable;

public class ExtensionStatisticsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String extension;
	private final int total;
	private final int hasMetadata;
	
	public ExtensionStatisticsBean(String extension, int total, int hasMetadata) {
		super();
		this.extension = extension;
		this.total = total;
		this.hasMetadata = hasMetadata;
	}

	public String getExtension() {
		return extension;
	}

	public int getTotal() {
		return total;
	}

	public int getHasMetadata() {
		return hasMetadata;
	}
	
}
