package gyk4j.wreck.io.task;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;

import gyk4j.wreck.io.MetadataBuilder;
import gyk4j.wreck.io.reader.ReaderFactory;
import gyk4j.wreck.io.reader.file.VerifyReader;
import gyk4j.wreck.resources.CorrectionEnum;

public class VerifyTask implements ITask {
	private final Path startPath;
	private final VerifyReader verifyReader;
	
	public VerifyTask(Path startPath) {
		this.startPath = startPath;
		
		verifyReader = ReaderFactory.getInstance().getVerifyReader();
		verifyReader.getRepository().load(startPath);
	}
	
	public Path getStartPath() {
		return startPath;
	}

	public VerifyReader getVerifyReader() {
		return verifyReader;
	}

	public Map<CorrectionEnum, FileTime> verify(Path file, BasicFileAttributes attrs) {
		MetadataBuilder mb = new MetadataBuilder();
		
		mb.addReader(verifyReader);
//		mb.addWriter(analyzeWriter);;
		
		mb.process(file, attrs);
//		mb.save(file, attrs);
		
		return mb.getSuggestions();
	}

	@Override
	public void postVisitDirectory(Map<CorrectionEnum, FileTime> suggestions, Path dir, BasicFileAttributes attrs) {
		suggestions.putAll(verify(dir, attrs));
	}

	@Override
	public void visitFile(Map<CorrectionEnum, FileTime> suggestions, Path file, BasicFileAttributes attrs) {
		suggestions.putAll(verify(file, attrs));
	}

	@Override
	public void preVisitDirectory(Map<CorrectionEnum, FileTime> suggestions, Path dir, BasicFileAttributes attrs) {
		
	}

	@Override
	public void visitFileFailed(Map<CorrectionEnum, FileTime> suggestions, Path file, IOException exc) {

	}
}
