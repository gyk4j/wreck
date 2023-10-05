package gyk4j.wreck.io.reader.fs;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import gyk4j.wreck.resources.R;

public class FileSystemReader extends AbstractTimestampReader {
	
	protected static final String[] CREATION = {
			R.string.FS_CREATION
	};
	
	protected static final String[] MODIFIED = {
			R.string.FS_MODIFIED
	};
	
	public FileSystemReader() {
		super();
	}

	@Override
	public String[] creation() {
		return CREATION;
	}
	
	@Override
	public String[] modified() {
		return MODIFIED;
	}
	
	@Override
	public String[] accessed() {
		return MODIFIED;
	}

	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {
		int order = attrs.creationTime().compareTo(attrs.lastModifiedTime());
		FileTime earlier = (order < 0)? attrs.creationTime(): attrs.lastModifiedTime();
		FileTime later = attrs.lastModifiedTime();
		
		add(metadata, CREATION[0], earlier.toString(), earlier);
		add(metadata, MODIFIED[0], later.toString(), later);
	}
}
