package gyk4j.wreck.io.reducer;

import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.resources.CorrectionEnum;

public interface ITimestampReducer {
	public void reduce(
			List<Metadata> metadata, 
			Map<CorrectionEnum, FileTime> corrections);
}
