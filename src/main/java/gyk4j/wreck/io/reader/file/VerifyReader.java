package gyk4j.wreck.io.reader.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.beans.LogEntry;
import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.repository.CsvLogRepository;
import gyk4j.wreck.util.logging.FileEvent;
import gyk4j.wreck.util.logging.StatisticsCollector;

public class VerifyReader extends CsvLogReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(VerifyReader.class);
	private static final StatisticsCollector STATS = StatisticsCollector.getInstance();

	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {
		super.extract(file, attrs, metadata);
		
		LogEntry entry = getRepository().getEntriesByPath().get(file);

		if(entry == null) {
			LOG.error("File not in backup: {}", file);
			STATS.count(FileEvent.UNCORRECTIBLE_CREATION);
			STATS.count(FileEvent.UNCORRECTIBLE_MODIFIED);
			STATS.count(FileEvent.UNCORRECTIBLE_ACCESSED);
		}
		else if(Files.isRegularFile(file)) { 
			if(entry.getCRC() != CsvLogRepository.getCRC32(file)) {
				LOG.error("Mismatched CRC: {}", file);
				STATS.count(FileEvent.UNCORRECTIBLE_CREATION);
				STATS.count(FileEvent.UNCORRECTIBLE_MODIFIED);
				STATS.count(FileEvent.UNCORRECTIBLE_ACCESSED);
			}
			else {
				LOG.info("CRC Passed: {}", file);
			}
		}
		else {
//			STATS.count(FileEvent.CORRECTIBLE_CREATION);
//			STATS.count(FileEvent.CORRECTIBLE_MODIFIED);
//			STATS.count(FileEvent.CORRECTIBLE_ACCESSED);
		}
		
//		if(metadata.isEmpty()) {
//			STATS.count(FileEvent.CORRECTIBLE_CREATION);
//			STATS.count(FileEvent.CORRECTIBLE_MODIFIED);
//			STATS.count(FileEvent.CORRECTIBLE_ACCESSED);
//		}
	}

}
