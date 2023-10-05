package gyk4j.wreck.io.task;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.MetadataBuilder;
import gyk4j.wreck.io.reader.ITimestampReader;
import gyk4j.wreck.io.reader.ReaderFactory;
import gyk4j.wreck.io.reader.user.CustomDateTimeReader;
import gyk4j.wreck.io.writer.ITimestampWriter;
import gyk4j.wreck.io.writer.WriterFactory;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.SourceEnum;

public class AnalyzeTask implements ITask {
	
	private static final Logger LOG = LoggerFactory.getLogger(AnalyzeTask.class);
	
	// Sources
	private final Map<SourceEnum, Boolean> sources;
	private final Date customDateTime;
	
	// Targets
	private final Map<CorrectionEnum, Boolean> corrections;
	
	private final ReaderFactory readerFactory;
	private final CustomDateTimeReader customReader;
	
	private final WriterFactory writerFactory;
	
	public AnalyzeTask(
			Path startPath,
			Map<SourceEnum, Boolean> sources, 
			Date customDateTime,
			Map<CorrectionEnum, Boolean> corrections) {
		
		this.sources = sources;
		this.customDateTime = customDateTime;
		
		this.corrections = corrections;
		
		readerFactory = ReaderFactory.getInstance();
		customReader = readerFactory.getCustomDateTimeReader();
		customReader.setCustom(getCustomDateTime());
		
		writerFactory = WriterFactory.getInstance();
	}

	protected Map<SourceEnum, Boolean> getSources() {
		return sources;
	}
	
	protected Date getCustomDateTime() {
		return customDateTime;
	}

	protected Map<CorrectionEnum, Boolean> getCorrections() {
		return corrections;
	}
	
	protected ReaderFactory getReaderFactory() {
		return readerFactory;
	}
	
	protected WriterFactory getWriterFactory() {
		return writerFactory;
	}
	
	protected ITimestampWriter[] getWriters() {
		return new ITimestampWriter[] {
				getWriterFactory().getAnalyzeWriter()
		};
	}
	
	protected ITimestampReader[] getFileReaders() {
		List<ITimestampReader> readers = new ArrayList<>();
		if(sources.get(SourceEnum.METADATA)) {
			readers.add(readerFactory.getExifToolReader());
			readers.add(readerFactory.getMediaInfoReader());
			readers.add(readerFactory.getSevenZipReader());
			readers.add(readerFactory.getTikaReader());
			readers.add(readerFactory.getPathReader());
		}
		
		// Use fallback option.
		if(sources.get(SourceEnum.FILE_SYSTEM))
			readers.add(readerFactory.getFileSystemReader());

		if(sources.get(SourceEnum.CUSTOM))
			readers.add(customReader);
		
		return readers.toArray(new ITimestampReader[0]);
	}
	
	protected ITimestampReader[] getDirectoryReaders() {
		List<ITimestampReader> readers = new ArrayList<>();
		
		if(sources.get(SourceEnum.METADATA))
			readers.add(readerFactory.getDirectoryReader());
		
		// Use fallback option.
		if(sources.get(SourceEnum.FILE_SYSTEM))
			readers.add(readerFactory.getFileSystemReader());

		if(sources.get(SourceEnum.CUSTOM))
			readers.add(customReader);
		
		return readers.toArray(new ITimestampReader[0]);
	}
	
	private Map<CorrectionEnum, FileTime> process(
			Path file, 
			BasicFileAttributes attrs,
			ITimestampReader[] readers,
			ITimestampWriter[] writers) {
		
		MetadataBuilder mb = new MetadataBuilder();
		
		for(ITimestampReader reader : readers) {
			mb.addReader(reader);
		}
		
		for(ITimestampWriter writer : writers) {
			mb.addWriter(writer);
		}
		
		mb.process(file, attrs);
		
		mb.save(file, attrs);
		
		return mb.getSuggestions();
	}
	
	private Map<CorrectionEnum, FileTime> analyze(Path path, BasicFileAttributes attrs) {
		Map<CorrectionEnum, FileTime> suggestions = null;
		
		ITimestampWriter[] writers = getWriters();
		
		// Use embedded or inferred metadata as primary source.
		ITimestampReader[] readers;
		
		if(attrs.isRegularFile())
			readers = getFileReaders();
		else if(attrs.isDirectory())
			readers = getDirectoryReaders();
		else
			readers = new ITimestampReader[0];
		
		suggestions = process(
				path, 
				attrs, 
				readers,
				writers);
		
		readerFactory.getDirectoryReader().add(path.getParent(), suggestions);
		
		LOG.info("{} C: {}, M: {}, A: {} {}", 
				(attrs.isRegularFile())? "<F>": "[D]",
				suggestions.get(CorrectionEnum.CREATION),
				suggestions.get(CorrectionEnum.MODIFIED),
				suggestions.get(CorrectionEnum.ACCESSED),
				path.getFileName());

		return suggestions;
	}

	@Override
	public void postVisitDirectory(Map<CorrectionEnum, FileTime> suggestions, Path dir, BasicFileAttributes attrs) {
		suggestions.putAll(analyze(dir, attrs));	
	}

	@Override
	public void visitFile(Map<CorrectionEnum, FileTime> suggestions, Path file, BasicFileAttributes attrs) {
		suggestions.putAll(analyze(file, attrs));
	}

	@Override
	public void preVisitDirectory(Map<CorrectionEnum, FileTime> suggestions, Path dir, BasicFileAttributes attrs) {
		
	}

	@Override
	public void visitFileFailed(Map<CorrectionEnum, FileTime> suggestions, Path file, IOException exc) {
		
	}
}
