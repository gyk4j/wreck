package gyk4j.wreck.io.writer;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;

import gyk4j.wreck.resources.CorrectionEnum;

public interface ITimestampWriter {

	void write(
			Path file, 
			BasicFileAttributes attrs, 
			Map<CorrectionEnum, FileTime> values);
}