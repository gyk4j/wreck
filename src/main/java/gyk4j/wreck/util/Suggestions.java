package gyk4j.wreck.util;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import gyk4j.wreck.resources.CorrectionEnum;

public class Suggestions implements Comparable<Suggestions> {
	private FileTime creationTime;
	private FileTime lastModifiedTime;
	private FileTime lastAccessedTime;
	
	public static Suggestions from(Map<CorrectionEnum, Instant> metadata) {
		return new Suggestions(
				metadata.get(CorrectionEnum.CREATION),
				metadata.get(CorrectionEnum.MODIFIED),
				metadata.get(CorrectionEnum.ACCESSED));
	}
	
	protected Suggestions() {
		super();
		creationTime = null;
		lastModifiedTime = null;
		lastAccessedTime = null;
	}
	
	protected Suggestions(FileTime creationTime, FileTime lastModifiedTime, FileTime lastAccessedTime) {
		super();
		this.creationTime = creationTime;
		this.lastModifiedTime = lastModifiedTime;
		this.lastAccessedTime = lastAccessedTime;
	}

	protected Suggestions(Instant creationTime, Instant lastModifiedTime, Instant lastAccessedTime) {
		this(
				(creationTime != null)? FileTime.from(creationTime): null,
				(lastModifiedTime != null)? FileTime.from(lastModifiedTime): null,
				(lastAccessedTime != null)? FileTime.from(lastAccessedTime): null);
	}
	
	public FileTime getCreationTime() {
		return creationTime;
	}
	
	public FileTime getLastModifiedTime() {
		return lastModifiedTime;
	}
	
	public FileTime getLastAccessedTime() {
		return lastAccessedTime;
	}

	@Override
	public String toString() {
		
		return String.format(
				System.lineSeparator() +
				"  Creation: %-19s" + System.lineSeparator() +
				"  Modified: %-19s" + System.lineSeparator() +
				"  Accessed: %-19s" + System.lineSeparator(), 
				toLocalDateTimeString(getCreationTime()), 
				toLocalDateTimeString(getLastModifiedTime()), 
				toLocalDateTimeString(getLastAccessedTime()));
	}
	
	private String toLocalDateTimeString(FileTime fileTime) {
		return (fileTime != null) ? 
				LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault()).toString():
					"-";
	}
	/*
	private Suggestions merge(Suggestions other) {
		FileTime creation = (getCreationTime().toInstant().isBefore(other.getCreationTime().toInstant()))? 
				getCreationTime(): other.getCreationTime();
		FileTime modified = (getLastModifiedTime().toInstant().isAfter(other.getLastModifiedTime().toInstant()))? 
				getLastModifiedTime(): other.getLastModifiedTime();
		FileTime accessed = (getLastAccessedTime().toInstant().isAfter(other.getLastAccessedTime().toInstant()))? 
				getLastAccessedTime(): other.getLastAccessedTime();
		return new Suggestions(creation, modified, accessed);
	}
	*/

	@Override
	public int compareTo(Suggestions other) {
		int creation = getCreationTime().compareTo(other.getCreationTime());
		int modified = getLastModifiedTime().compareTo(other.getLastModifiedTime());
		int accessed = getLastAccessedTime().compareTo(other.getLastAccessedTime());
		return creation * modified * accessed;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (this.getClass() != other.getClass()) {
			return false;
		}
		
		if (compareTo((Suggestions) other) != 0) {
			return false;
		}
		
		return true;
	}
}
