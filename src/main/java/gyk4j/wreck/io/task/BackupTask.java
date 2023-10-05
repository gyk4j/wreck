package gyk4j.wreck.io.task;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;

import gyk4j.wreck.io.MetadataBuilder;
import gyk4j.wreck.io.reader.ReaderFactory;
import gyk4j.wreck.io.reader.fs.BackupBasicFileAttributesReader;
import gyk4j.wreck.io.writer.CsvLogWriter;
import gyk4j.wreck.io.writer.WriterFactory;
import gyk4j.wreck.resources.CorrectionEnum;

public class BackupTask implements ITask {
	
	private final BackupBasicFileAttributesReader backupBasicFileAttributesReader;
	private final CsvLogWriter csvLogWriter;
	
	public BackupTask(Path startPath) {
		backupBasicFileAttributesReader = ReaderFactory.getInstance().getBackupBasicFileAttributesReader();
		csvLogWriter = WriterFactory.getInstance().getCsvLogWriter();
		csvLogWriter.getRepository().open(startPath);
	}
	
	public BackupBasicFileAttributesReader getBackupBasicFileAttributesReader() {
		return backupBasicFileAttributesReader;
	}

	public CsvLogWriter getCsvLogWriter() {
		return csvLogWriter;
	}

	private Map<CorrectionEnum, FileTime> backup(Path file, BasicFileAttributes attrs) {
		MetadataBuilder mb = new MetadataBuilder();
		
		mb.addReader(backupBasicFileAttributesReader);
		mb.addWriter(csvLogWriter);
		
		mb.process(file, attrs);
		mb.save(file, attrs);
		
		return mb.getSuggestions();
	}

	@Override
	public void preVisitDirectory(Map<CorrectionEnum, FileTime> suggestions, Path dir, BasicFileAttributes attrs) {
		suggestions.putAll(backup(dir, attrs));
	}

	@Override
	public void visitFile(Map<CorrectionEnum, FileTime> suggestions, Path file, BasicFileAttributes attrs) {
		suggestions.putAll(backup(file, attrs));
	}

	@Override
	public void postVisitDirectory(Map<CorrectionEnum, FileTime> suggestions, Path dir, BasicFileAttributes attrs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFileFailed(Map<CorrectionEnum, FileTime> suggestions, Path file, IOException exc) {
		// TODO Auto-generated method stub
		
	}
}
