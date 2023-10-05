package gyk4j.wreck.io.task;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;

import gyk4j.wreck.resources.CorrectionEnum;

public interface ITask {
	void preVisitDirectory(
			Map<CorrectionEnum, FileTime> suggestions, 
			Path dir, 
			BasicFileAttributes attrs);
	
	void postVisitDirectory(
			Map<CorrectionEnum, FileTime> suggestions, 
			Path dir, 
			BasicFileAttributes attrs);
	
	void visitFile(
			Map<CorrectionEnum, FileTime> suggestions, 
			Path file, 
			BasicFileAttributes attrs);
	
	void visitFileFailed(
			Map<CorrectionEnum, FileTime> suggestions,
			Path file, 
			IOException exc);
}
