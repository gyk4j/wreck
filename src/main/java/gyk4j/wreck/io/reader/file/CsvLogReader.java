package gyk4j.wreck.io.reader.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.beans.LogEntry;
import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import gyk4j.wreck.repository.CsvLogRepository;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.R;

public class CsvLogReader extends AbstractTimestampReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(CsvLogReader.class);
	
	public static final String[] CREATION = {
			R.string.CSV_CREATION,
	};
	
	public static final String[] MODIFIED = {
			R.string.CSV_MODIFIED,
	};
	
	public static final String[] ACCESSED = {
			R.string.CSV_ACCESSED,
	};
	
	private final CsvLogRepository repository;
	
	public CsvLogReader() {
		super();
		repository = CsvLogRepository.getInstance();
	}
	
	public CsvLogReader(Path startingPath) {
		this();
		getRepository().open(startingPath);
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
		return ACCESSED;
	}

	public CsvLogRepository getRepository() {
		return repository;
	}

	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {		
		LogEntry entry = getRepository().getEntriesByPath().get(file);
		
		if(entry == null && Files.isRegularFile(file)) {
			LOG.info("Using fallback CRC: {}", file);
			long crc = CsvLogRepository.getCRC32(file);
			entry = getRepository().getEntriesByCrc().get(crc);
		}
		
		if(entry == null) {
			LOG.warn("Non-existent file: {}. Skipped extract.", file);
			return;
		}
		
		Map<CorrectionEnum, FileTime> original = entry.getOriginal();
		
		if(original != null) {
			add(	metadata,  
					CREATION[0], 
					original.get(CorrectionEnum.CREATION).toString(),
					original.get(CorrectionEnum.CREATION));
			
			add(	metadata, 
					MODIFIED[0], 
					original.get(CorrectionEnum.MODIFIED).toString(),
					original.get(CorrectionEnum.MODIFIED));
			
			add(	metadata, 
					ACCESSED[0], 
					original.get(CorrectionEnum.ACCESSED).toString(),
					original.get(CorrectionEnum.ACCESSED));
		}
	}
}
