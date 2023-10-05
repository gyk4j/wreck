package gyk4j.wreck.beans;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Period;

public class FileBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Path path;
	protected FileTime creation;
	protected FileTime modified;
	protected FileTime metadata;
	private Period period;
	
	public FileBean(Path path, FileTime creation, FileTime modified, FileTime metadata, Period period) {
		super();
		this.path = path;
		this.creation = creation;
		this.modified = modified;
		this.metadata = metadata;
		this.period = period;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public FileTime getCreation() {
		return creation;
	}

	public void setCreation(FileTime creation) {
		this.creation = creation;
	}

	public FileTime getModified() {
		return modified;
	}

	public void setModified(FileTime modified) {
		this.modified = modified;
	}

	public FileTime getMetadata() {
		return metadata;
	}

	public void setMetadata(FileTime metadata) {
		this.metadata = metadata;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}
	
}
