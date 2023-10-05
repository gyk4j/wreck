package gyk4j.wreck.io.reader.fs;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import gyk4j.wreck.resources.R;

public class BackupBasicFileAttributesReader extends AbstractTimestampReader {
	
	protected static final String[] CREATION = {
			R.string.BACKUP_CREATION_TIME
	};
	
	protected static final String[] MODIFIED = {
			R.string.BACKUP_LAST_MODIFIED_TIME
			
	};
	
	protected static final String[] ACCESSED = {
			R.string.BACKUP_LAST_ACCESSED_TIME
	};

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
		return ACCESSED;
	}

	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {		
		add(metadata, CREATION[0], attrs.creationTime().toString(), attrs.creationTime());
		add(metadata, MODIFIED[0], attrs.lastModifiedTime().toString(), attrs.lastModifiedTime());
		add(metadata, ACCESSED[0], attrs.lastAccessTime().toString(), attrs.lastAccessTime());
	}
}
