package gyk4j.wreck.beans;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

public class MetadataBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Map<String, Instant> tags = new TreeMap<>();

	public Map<String, Instant> getTags() {
		return tags;
	}
	
	public void addTag(String key, Instant value) {
		getTags().put(key, value);
	}
	
	public Instant getEarliest() {
		return getTags().values().stream().min(Instant::compareTo).orElse(null);
	}
	
	public Instant getLatest() {
		return getTags().values().stream().max(Instant::compareTo).orElse(null);
	}
	
}
