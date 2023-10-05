package gyk4j.wreck.io.reader.fs;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.R;

public class DirectoryReader extends AbstractTimestampReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(DirectoryReader.class);
	
	protected static final String[] EARLIEST = {
			R.string.DIR_EARLIEST
	};
	
	protected static final String[] LATEST = {
			R.string.DIR_LATEST
	};
	
	@Override
	public String[] creation() {
		return EARLIEST;
	}

	@Override
	public String[] modified() {
		return LATEST;
	}

	@Override
	public String[] accessed() {
		return LATEST;
	}

	private final Map<Path, Set<FileTime>> fileTimes = new HashMap<>();

	public void add(Path dir, Map<CorrectionEnum, FileTime> suggestions) {
		Set<FileTime> times = fileTimes.get(dir);
		if(times == null) {
			times = new HashSet<FileTime>();
			fileTimes.put(dir, times);
		}
		times.addAll(suggestions.values());
		times.removeIf(t -> t == null);
	}
	
	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {
		Set<FileTime> times = fileTimes.remove(file);
		
		if(times == null || times.isEmpty()) {
			LOG.warn("Empty directory? {}", file);
			return;
		}
		
		FileTime min = times.stream().min(FileTime::compareTo).orElse(null);
		FileTime max = times.stream().max(FileTime::compareTo).orElse(null);
		
		if(min != null)
			add(metadata, EARLIEST[0], min.toString(), min);
		
		if(max != null)
			add(metadata, LATEST[0], max.toString(), max);
	}
}
