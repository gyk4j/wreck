package gyk4j.wreck.util.logging;

import java.nio.file.Path;

public class ExtensionStatistics implements Comparable<ExtensionStatistics> {
	private String id;
	private int total;
	private int hasMetadata;
	
	public static String getExtensionKey(Path file) {
		String filename = file.getFileName().toString();
		int separatorPos = filename.lastIndexOf('.');
		
		if(separatorPos < 0 || separatorPos == (filename.length()-1)) {
			return "";
		}
		
		String extension = filename.substring(separatorPos+1).trim();
		
		if(extension.isEmpty()) {
			return "";
		}
		
		extension = extension.toLowerCase();
		return extension;
	}
	
	public ExtensionStatistics(String id, boolean hasMetadata) {
		super();
		this.id = id;
		total = 1;
		this.hasMetadata = (hasMetadata)? 1: 0;
	}
	
	public ExtensionStatistics(Path file, boolean hasMetadata) {
		this(getExtensionKey(file), hasMetadata);
	}

	public String getId() {
		return id;
	}

	public int getTotal() {
		return total;
	}
	
	public int addTotal() {
		return ++total;
	}

	public int getHasMetadata() {
		return hasMetadata;
	}
	
	public int addMetadata() {
		return ++hasMetadata;
	}

	@Override
	public String toString() {
		return String.format("%-10s %6d %6d %6d", id, total, hasMetadata, total-hasMetadata);
	}

	@Override
	public int compareTo(ExtensionStatistics o) {
		if(getTotal() > o.getTotal())
			return -1;
		else if(getTotal() == o.getTotal())
			return 0;
		else
			return 1;
	}
	
	
}
