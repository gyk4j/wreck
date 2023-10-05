package gyk4j.wreck.io.writer;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;

import gyk4j.wreck.repository.CsvLogRepository;
import gyk4j.wreck.resources.CorrectionEnum;

public class CsvLogWriter extends AbstractTimestampWriter implements AutoCloseable {
	
	private final CsvLogRepository repository;
	
	public CsvLogWriter() {
		super();
		repository = CsvLogRepository.getInstance();
	}
	
	public CsvLogWriter(Path startingPath) {
		this();
		getRepository().open(startingPath);
	}
	
	public CsvLogRepository getRepository() {
		return repository;
	}
	
	@Override
	public void write(
			Path file, 
			BasicFileAttributes attrs, 
			Map<CorrectionEnum, FileTime> values) {
		getRepository().save(file, attrs);
	}

	@Override
	public void close() throws Exception {
		getRepository().close();
	}
}
