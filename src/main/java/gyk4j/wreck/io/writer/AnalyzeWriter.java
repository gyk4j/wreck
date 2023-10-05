package gyk4j.wreck.io.writer;

import java.nio.file.attribute.FileTime;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.util.logging.FileEvent;

public class AnalyzeWriter extends AbstractTimestampWriter {
	
	@Override
	public void write(
			Path file, 
			BasicFileAttributes attrs, 
			Map<CorrectionEnum, FileTime> values) {
		
		writeAttribute(
				FileEvent.CORRECTIBLE_CREATION,
				FileEvent.UNCORRECTIBLE_CREATION,
				(attrs != null)? attrs.creationTime() : null, 
				values.get(CorrectionEnum.CREATION));
		
		writeAttribute(
				FileEvent.CORRECTIBLE_MODIFIED,
				FileEvent.UNCORRECTIBLE_MODIFIED,
				(attrs != null)? attrs.lastModifiedTime() : null, 
				values.get(CorrectionEnum.MODIFIED));
		
		writeAttribute(
				FileEvent.CORRECTIBLE_ACCESSED,
				FileEvent.UNCORRECTIBLE_ACCESSED,
				(attrs != null)? attrs.lastAccessTime() : null, 
				values.get(CorrectionEnum.ACCESSED));
	}
}
