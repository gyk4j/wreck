package gyk4j.wreck.io.reducer;

import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.resources.CorrectionEnum;

@Deprecated
public class RestoreCollector implements ITimestampReducer {
	
	private static final Logger LOG = LoggerFactory.getLogger(RestoreCollector.class);

	@Override
	public void reduce(List<Metadata> metadata, Map<CorrectionEnum, FileTime> corrections) {
		
		for(CorrectionEnum c: CorrectionEnum.values()) {
//			Set<Instant> instants = metadata.get(c);
			Stream<Metadata> s = metadata.stream().filter(md -> c.equals(md.getGroup()));
			
			if(s.count() > 1) {
				LOG.warn("Unexpected instant set size: {}", s.count());
//				Iterator<Instant> i = instants.iterator();
//				while(i.hasNext()) {
//					LOG.warn("Found {}", i.next().toString());
//				}
				metadata
				.stream()
				.filter(md -> c.equals(md.getGroup()))
				.forEach(md -> LOG.warn("Found {}", md.getValue()));
			}
			else {
				Optional<Metadata> first = s.findFirst();
				corrections.put(c, first.get().getTime());
			}
		}
	}

}
