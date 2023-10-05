package gyk4j.wreck.util.logging;

import java.nio.file.Path;

public class ExtensionEvent {
	final Path file;
	final boolean hasMetadata;
	
	public ExtensionEvent(Path file, boolean hasMetadata) {
		this.file = file;
		this.hasMetadata = hasMetadata;
	}
	
	public Path getFile() {
		return file;
	}

	public boolean hasMetadata() {
		return hasMetadata;
	}

	@Override
	public String toString() {
		return ExtensionStatistics.getExtensionKey(file);
	}
}
