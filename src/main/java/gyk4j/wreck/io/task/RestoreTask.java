package gyk4j.wreck.io.task;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;

import gyk4j.wreck.io.MetadataBuilder;
import gyk4j.wreck.io.reader.ReaderFactory;
import gyk4j.wreck.io.reader.file.CsvLogReader;
import gyk4j.wreck.io.writer.RestoreBasicFileAttributesWriter;
import gyk4j.wreck.io.writer.WriterFactory;
import gyk4j.wreck.resources.CorrectionEnum;

public class RestoreTask implements ITask {
	private final Path startPath;
	private final CsvLogReader csvLogReader;
	private final RestoreBasicFileAttributesWriter restoreBasicFileAttributesWriter;
	
	public RestoreTask(Path startPath) {
		this.startPath = startPath;
		
		csvLogReader = ReaderFactory.getInstance().getCsvLogReader();
		csvLogReader.getRepository().load(startPath);
		restoreBasicFileAttributesWriter = WriterFactory.getInstance().getRestoreBasicFileAttributesWriter();
	}
	
	public Path getStartPath() {
		return startPath;
	}

	public CsvLogReader getCsvLogReader() {
		return csvLogReader;
	}

	public RestoreBasicFileAttributesWriter getRestoreBasicFileAttributesWriter() {
		return restoreBasicFileAttributesWriter;
	}

	public Map<CorrectionEnum, FileTime> restore(Path file, BasicFileAttributes attrs) {
		MetadataBuilder mb = new MetadataBuilder();
		
		mb.addReader(csvLogReader);
		mb.addWriter(restoreBasicFileAttributesWriter);
		
		mb.process(file, attrs);
		mb.save(file, attrs);
		
		return mb.getSuggestions();
	}

	@Override
	public void postVisitDirectory(Map<CorrectionEnum, FileTime> suggestions, Path dir, BasicFileAttributes attrs) {
		suggestions.putAll(restore(dir, attrs));
		
		done(dir);
	}

	@Override
	public void visitFile(Map<CorrectionEnum, FileTime> suggestions, Path file, BasicFileAttributes attrs) {
		suggestions.putAll(restore(file, attrs));
		
		done(file);
	}

	@Override
	public void preVisitDirectory(Map<CorrectionEnum, FileTime> suggestions, Path dir, BasicFileAttributes attrs) {
		
	}

	@Override
	public void visitFileFailed(Map<CorrectionEnum, FileTime> suggestions, Path file, IOException exc) {
		done(file);
	}
	
	private void done(Path file) {
		if(file.equals(getStartPath()))
			csvLogReader.getRepository().clear();
	}
}
