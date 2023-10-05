package gyk4j.wreck.io;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.reader.ITimestampReader;
import gyk4j.wreck.io.reducer.ITimestampReducer;
import gyk4j.wreck.io.reducer.TimestampReducer;
import gyk4j.wreck.io.writer.ITimestampWriter;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.util.logging.ExtensionEvent;
import gyk4j.wreck.util.logging.StatisticsCollector;
import gyk4j.wreck.util.logging.TagEvent;

public class MetadataBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(MetadataBuilder.class);
	private static final StatisticsCollector STATS = StatisticsCollector.getInstance();

	private final List<ITimestampReader> readers;
	private final List<ITimestampWriter> writers;
	private final ITimestampReducer reducer;
	private final List<Metadata> metadata;
	private final Map<CorrectionEnum, FileTime> suggestions;
	
	public MetadataBuilder(){		
		readers = new ArrayList<>();
		writers = new ArrayList<>();
		metadata = new ArrayList<>();
		reducer = new TimestampReducer();
		suggestions = new EnumMap<>(CorrectionEnum.class);
	}
	
	private List<ITimestampReader> getReaders() {
		return readers;
	}
	
	private List<ITimestampWriter> getWriters() {
		return writers;
	}
	
	private ITimestampReducer getReducer() {
		return reducer;
	}
	
	public List<Metadata> getMetadata() {
		return metadata;
	}
	
	public Map<CorrectionEnum, FileTime> getSuggestions() {
		return suggestions;
	}
	
	public boolean isIncomplete() {
		if(suggestions.isEmpty())
			return true;
		
		for(CorrectionEnum c: CorrectionEnum.values()) {
			if(suggestions.get(c) == null)
				return true;
		}
		
		return false;
	}
	
	public void addReader(ITimestampReader reader) {
		getReaders().add(reader);
	}
	
	public void addWriter(ITimestampWriter writer) {
		getWriters().add(writer);
	}
	
	public void process(Path file, BasicFileAttributes attrs) {
		
		for(ITimestampReader reader : getReaders()) {
			int prev = metadata.size();
			reader.extract(file, attrs, metadata);
			
			for(int t=prev; t < metadata.size(); t++)
				STATS.count(new TagEvent(reader.getClass(), metadata.get(t).getKey()));
		}
	}
	
	public void save(Path file, BasicFileAttributes attrs) {
		if(getMetadata().isEmpty())
			LOG.warn("Metadata empty!!! {}...", file);
		
		getReducer().reduce(getMetadata(), getSuggestions());
		for(ITimestampWriter writer: getWriters()) {
			writer.write(file, attrs, getSuggestions());
		}
		
		STATS.count(new ExtensionEvent(file, !isIncomplete()));
	}
}
