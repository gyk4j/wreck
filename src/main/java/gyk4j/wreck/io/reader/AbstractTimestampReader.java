package gyk4j.wreck.io.reader;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.resources.CorrectionEnum;

public abstract class AbstractTimestampReader implements ITimestampReader {

	private static final String[] NONE = new String[] {};
	
	private final Map<String, CorrectionEnum> keys;
	
	public AbstractTimestampReader() {
		super();
		keys = new HashMap<>();
		
		for(String s: creation()) {
			keys.put(s, CorrectionEnum.CREATION);
		}
		
		for(String s: modified()) {
			keys.put(s, CorrectionEnum.MODIFIED);
		}
		
		for(String s: accessed()) {
			keys.put(s, CorrectionEnum.ACCESSED);
		}
	}

	@Override
	public String[] creation() {
		return NONE;
	}

	@Override
	public String[] modified() {
		return NONE;
	}

	@Override
	public String[] accessed() {
		return NONE;
	}
	
	protected Map<String, CorrectionEnum> getKeys() {
		return keys;
	}
	
	protected void add(
			List<Metadata> metadata, 
			String key, 
			String value, 
			FileTime time) {
		Metadata m = new Metadata(
				key,
				value,
				time,
				getKeys().getOrDefault(key, null));
		metadata.add(m);
	}
	
	protected void add(
			List<Metadata> metadata, 
			String key, 
			String value, 
			Instant time) {
		FileTime ft = (time != null)? FileTime.from(time) : null;
		add(metadata, key, value, ft);
	}
	
	protected void add(
			List<Metadata> metadata, 
			String key, 
			String value, 
			String time) {
		Instant it = (time != null)? Instant.parse(time): null;
		add(metadata, key, value, it);
	}

	@Override
	public void close() throws Exception {		
	}

}
