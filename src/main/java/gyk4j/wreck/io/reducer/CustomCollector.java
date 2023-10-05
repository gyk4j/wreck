package gyk4j.wreck.io.reducer;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.resources.CorrectionEnum;

/**
 * Use user-specified date time as a hard upper limit as last resort.
 * @author USER
 *
 */
@Deprecated
public class CustomCollector implements ITimestampReducer {
	
//	private static final StatisticsCollector STATS = StatisticsCollector.getInstance();

	@Override
	public void reduce(List<Metadata> metadata, Map<CorrectionEnum, FileTime> corrections) {
		FileTime custom = FileTime.from(Instant.EPOCH);
		
		if(custom == null) {
			corrections.clear();
			return;
		}
		
		// Fill with extracted metadata
		for(CorrectionEnum c : CorrectionEnum.values()) {
			FileTime i = corrections.get(c);
			
			// If user-specified limit is earlier than the:
			// - metadata timestamp.
			// - file system's last modified time.
			if(custom.compareTo(i) < 0) {
				corrections.put(c, custom);
//				STATS.count(SelectionEvent.CUSTOM);
			}
		}
	}

}
