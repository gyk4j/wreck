package gyk4j.wreck.beans;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Map;

import gyk4j.wreck.resources.CorrectionEnum;

public class LogEntry implements Comparable<LogEntry> {
	private final Path path;
	private final long size;
	private final long crc;
	private final Map<CorrectionEnum, FileTime> original;
	
	public LogEntry(
			Path path, 
			long size,
			long crc,
			Map<CorrectionEnum, FileTime> original) {
		super();
		this.path = path;
		this.size = size;
		this.crc = crc;
		this.original = original;
	}
	public Path getPath() {
		return path;
	}
	public long getSize() {
		return size;
	}
	public long getCRC() {
		return crc;
	}
	public Map<CorrectionEnum, FileTime> getOriginal() {
		return original;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj instanceof LogEntry) {
			LogEntry o = (LogEntry) obj;
			return getPath().equals(o.getPath())
					&& getCRC() == o.getCRC() 
					&& getSize() == o.getSize()
					&& isFileTimeEquals(CorrectionEnum.CREATION, o)
					&& isFileTimeEquals(CorrectionEnum.MODIFIED, o)
					&& isFileTimeEquals(CorrectionEnum.ACCESSED, o);
		}
		else {
			return false;
		}
	}
	
	private boolean isFileTimeEquals(CorrectionEnum c, LogEntry o) {
		FileTime o1 = getOriginal().get(c);
		FileTime o2 = o.getOriginal().get(c);
		
		return (o1 == null && o2 == null) || o1.equals(o2);
	}
	
	@Override
	public int compareTo(LogEntry o) {
		int result;
		if(getCRC() == o.getCRC())
			result = 0;
		else if(getCRC() < o.getCRC())
			result = -1;
		else
			result = 1;
		return result;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPath());
		sb.append(',');
		sb.append(getSize());
		sb.append(',');
		sb.append(getCRC());
		sb.append(',');
		sb.append(getOriginal().get(CorrectionEnum.CREATION));
		sb.append(',');
		sb.append(getOriginal().get(CorrectionEnum.MODIFIED));
		sb.append(',');
		sb.append(getOriginal().get(CorrectionEnum.ACCESSED));
		
		return sb.toString();
	}
}
