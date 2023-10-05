package gyk4j.wreck.io.reader;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import gyk4j.wreck.io.Metadata;

public interface ITimestampReader extends AutoCloseable {	
	void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata);
	String[] creation();
	String[] modified();
	String[] accessed();
}
