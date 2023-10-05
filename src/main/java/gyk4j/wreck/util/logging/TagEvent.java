package gyk4j.wreck.util.logging;

import gyk4j.wreck.io.reader.ITimestampReader;

public class TagEvent {
	final Class<? extends ITimestampReader> reader;
	final String name;
	
	public TagEvent(Class<? extends ITimestampReader> reader, String name) {
		super();
		this.reader = reader;
		this.name = name;
	}

	public Class<? extends ITimestampReader> getReader() {
		return reader;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return reader.getSimpleName().concat(":").concat(name);
	}
}
