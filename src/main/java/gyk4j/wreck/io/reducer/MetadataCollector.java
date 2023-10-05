package gyk4j.wreck.io.reducer;

import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.resources.CorrectionEnum;

@Deprecated
public class MetadataCollector implements ITimestampReducer {
	
//	private static final StatisticsCollector STATS = StatisticsCollector.getInstance();
	
	@Override
	public void reduce(List<Metadata> metadata, Map<CorrectionEnum, FileTime> corrections) {		
//		LOG.trace("{} tag-value pairs", metadata.size());
		
		// Always use embedded metadata if available for accuracy.
		Map<CorrectionEnum, Set<FileTime>> instants = new HashMap<>();
		for(CorrectionEnum c : CorrectionEnum.values()) {
			instants.put(c, new TreeSet<FileTime>());
		}
		
		// Fill with extracted metadata
		for(CorrectionEnum c : CorrectionEnum.values()) {
//			for(String key: TAG_KEYS.get(c)) {
			Optional<Metadata> m = metadata.stream().filter(md -> c.equals(md.getGroup())).min(Metadata::compareTo);

			if(m.isPresent()) {
				FileTime i = m.get().getTime();
				if(i != null) {
					instants.get(c).add(i);
//					LOG.trace("Added {} to {}", i.toString(), c.toString());
				}
			}
//			}
			
			if(instants.get(c).size() > 0) {
				corrections.put(c, instants.get(c).iterator().next());
//				STATS.count(SelectionEvent.METADATA);
			}
		}
		
		if(corrections.get(CorrectionEnum.CREATION) == null) {
			FileTime modified = corrections.get(CorrectionEnum.MODIFIED);
			if(modified != null) {
				corrections.put(CorrectionEnum.CREATION, modified);
//				STATS.count(SelectionEvent.METADATA);
			}
		}
		
		if(corrections.get(CorrectionEnum.MODIFIED) == null) {
			FileTime creation = corrections.get(CorrectionEnum.CREATION);
			if(creation != null) {
				corrections.put(CorrectionEnum.MODIFIED, creation);
//				STATS.count(SelectionEvent.METADATA);
			}
		}
		
		if(corrections.get(CorrectionEnum.ACCESSED) == null) {
			FileTime creation = corrections.get(CorrectionEnum.CREATION);
			FileTime modified = corrections.get(CorrectionEnum.MODIFIED);
			
			if(creation != null && modified != null) {
				corrections.put(CorrectionEnum.ACCESSED, 
						modified.compareTo(creation) > 0? modified: creation);
//				STATS.count(SelectionEvent.METADATA);
			}
			else if(creation != null) {
				corrections.put(CorrectionEnum.ACCESSED, creation);
//				STATS.count(SelectionEvent.METADATA);
			}
			else if(modified != null) {
				corrections.put(CorrectionEnum.ACCESSED, modified);
//				STATS.count(SelectionEvent.METADATA);
			}
				
		}
	}
}
