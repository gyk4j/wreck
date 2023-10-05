package gyk4j.wreck.io.task;

import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

import gyk4j.wreck.io.writer.ITimestampWriter;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.SourceEnum;

public class CorrectTask extends AnalyzeTask {
	
	public CorrectTask(
			Path startPath, 
			Map<SourceEnum, Boolean> sources, 
			Date customDateTime,
			Map<CorrectionEnum, Boolean> corrections) {
		super(startPath, sources, customDateTime, corrections);
	}

	@Override
	protected ITimestampWriter[] getWriters() {
		return new ITimestampWriter[] {
				getWriterFactory().getBasicFileAttributesWriter(),
		};
	}
}
