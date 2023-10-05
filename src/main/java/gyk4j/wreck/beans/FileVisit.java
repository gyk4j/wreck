package gyk4j.wreck.beans;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FileVisit {
	private Path file;
	private BasicFileAttributes attributes;
	private int progress;
	
	public FileVisit(Path file, BasicFileAttributes attributes) {
		super();
		this.file = file;
		this.attributes = attributes;
	}
	public Path getFile() {
		return file;
	}
	public void setFile(Path file) {
		this.file = file;
	}
	public BasicFileAttributes getAttributes() {
		return attributes;
	}
	public void setAttributes(BasicFileAttributes attributes) {
		this.attributes = attributes;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
}
