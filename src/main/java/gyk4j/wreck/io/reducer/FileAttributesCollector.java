package gyk4j.wreck.io.reducer;

import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.resources.CorrectionEnum;

/**
 * Use inferred/derived sources as backup only if no metadata is available.
 * @author USER
 *
 */
@Deprecated
public class FileAttributesCollector implements ITimestampReducer {
	
//	private static final StatisticsCollector STATS = StatisticsCollector.getInstance();

	@Override
	public void reduce(List<Metadata> metadata, Map<CorrectionEnum, FileTime> corrections) {		
//		Instant lastModified = parse(metadata.get(R.string.NTFS_LAST_MODIFIED_TIME));
		
//		if(lastModified == null)
//			return corrections;
		
		// Fill with extracted metadata
		for(CorrectionEnum c : CorrectionEnum.values()) {
			Optional<Metadata> i = metadata.stream().min(Metadata::compareTo);
//					metadata.get(c).iterator().next();
			
			// Fill with last modified time if no metadata is available.
			if(i.isPresent()) {
				Metadata m = i.get();
				
				if(m.getTime() != null) {
					corrections.put(c, m.getTime());
//					STATS.count(SelectionEvent.LAST_MODIFIED);
				}
			}
		}
	}
}
