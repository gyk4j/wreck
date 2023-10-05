package gyk4j.wreck.io.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.util.logging.FileEvent;

public class RestoreBasicFileAttributesWriter extends AbstractTimestampWriter {
	
	private static final Logger LOG = LoggerFactory.getLogger(RestoreBasicFileAttributesWriter.class);
	
	@Override
	public void write(
			Path file, 
			BasicFileAttributes attrs, 
			Map<CorrectionEnum, FileTime> values) {
		
		FileTime creation = restoreAttribute(
				FileEvent.CORRECTIBLE_CREATION,
				FileEvent.UNCORRECTIBLE_CREATION,
				(attrs != null)? attrs.creationTime() : null,
				values.get(CorrectionEnum.CREATION));
		
		FileTime modified = restoreAttribute(
				FileEvent.CORRECTIBLE_MODIFIED,
				FileEvent.UNCORRECTIBLE_MODIFIED,
				(attrs != null)? attrs.lastModifiedTime() : null,
				values.get(CorrectionEnum.MODIFIED));
		
		FileTime accessed = restoreAttribute(
				FileEvent.CORRECTIBLE_ACCESSED,
				FileEvent.UNCORRECTIBLE_ACCESSED,
				(attrs != null)? attrs.lastAccessTime() : null,
				values.get(CorrectionEnum.ACCESSED));
		
		BasicFileAttributeView basicView = Files.getFileAttributeView(file, BasicFileAttributeView.class);
		try {
			basicView.setTimes(modified, accessed, creation);
			LOG.info("Restored {}: {}, {}, {}", 
					file.toString(),
					creation,
					modified,
					accessed);
		} catch (IOException e) {
			LOG.error("Restore failed: {} {}", file.toString(), e.toString());
		}
	}
}
