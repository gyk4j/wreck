package gyk4j.wreck.io.reducer;

import static gyk4j.wreck.time.TimeUtils.VALID_PERIOD_MAX;
import static gyk4j.wreck.time.TimeUtils.VALID_PERIOD_MIN;

import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.R;

public class TimestampReducer implements ITimestampReducer {
	
	private static final Logger LOG = LoggerFactory.getLogger(TimestampReducer.class);
	
	/**
	 * 
	 * @param t Found FileTime.
	 * @param s Stream of metadata with {@link java.nio.file.attribute.FileTime FileTime}
	 * @param isEarliest Get earliest time if true, or latest time if false.
	 * @return
	 */
	private FileTime get(FileTime t, Stream<Metadata> s, boolean isEarliest) {
		if(t == null) {
			Optional<Metadata> attr = (isEarliest)? s.min(Metadata::compareTo) : s.max(Metadata::compareTo);

			if(attr.isPresent())
				t = attr.get().getTime();
		}
		return t;
	}
	
	/**
	 * 
	 * @param metadata Full list of extracted metadata for file
	 * @param c Correction enum for specific attribute
	 * @param isEarliest Get the minimum/earliest (true), or maximum/latest (false) file time
	 * @return
	 */
	private FileTime get(List<Metadata> metadata, CorrectionEnum c, boolean isEarliest) {
		FileTime t = null;
		Stream<Metadata> s;
		
		if(metadata.isEmpty())
			return t;
		
		Optional<Metadata> custom = metadata.stream()
				.filter(md -> 
				R.string.USER_CUSTOM_DATE_TIME.equals(md.getKey()))
				.findAny();
		
//		metadata.forEach(md -> LOG.trace("{} = {} {}", md.getKey(), md.getValue(), md.getTime()));
		
		List<Metadata> embedded = metadata
				.stream()
				.filter(md -> 
				!R.string.USER_CUSTOM_DATE_TIME.equals(md.getKey()) &&
				!R.string.FS_CREATION.equals(md.getKey()) &&
				!R.string.FS_MODIFIED.equals(md.getKey()) &&
				!R.string.BACKUP_CREATION_TIME.equals(md.getKey()) &&
				!R.string.BACKUP_LAST_MODIFIED_TIME.equals(md.getKey()) &&
				!R.string.BACKUP_LAST_ACCESSED_TIME.equals(md.getKey()))
				.collect(Collectors.toList());
		
		List<Metadata> fileSystem = metadata
				.stream()
				.filter(md ->
				R.string.FS_CREATION.equals(md.getKey()) ||
				R.string.FS_MODIFIED.equals(md.getKey()))
				.collect(Collectors.toList());
		
		// High precision by attribute group (creation, modified, accessed).
		s = embedded.stream().filter(md -> c.equals(md.getGroup()));
		t = get(t, s, isEarliest);
		
		// Any embedded metadata
		s = embedded.stream();
		t = get(t, s, isEarliest);

		// Use backup options between file system or custom user-defined time.
		s = fileSystem.stream();
		t = get(t, s, isEarliest);
		
		// Use any metadata found.
		s = metadata.stream();
		t = get(t, s, isEarliest);
		
		if(t != null) {
			// If custom limit is set, and time found is later than the limit, use the
			// user set limit instead.  
			if(custom.isPresent() && custom.get().getTime().compareTo(t) < 0)
				t = custom.get().getTime();
			
			// Last check: Between 1980-01-01 and current date time.
			if(t.compareTo(VALID_PERIOD_MIN) < 0)
				t = VALID_PERIOD_MIN;
			else if(t.compareTo(VALID_PERIOD_MAX) > 0)
				t = VALID_PERIOD_MAX;
		}
		
		embedded.clear();
		fileSystem.clear();
		
		return t;
	}
	
	@Override
	public void reduce(List<Metadata> metadata, Map<CorrectionEnum, FileTime> corrections) {		
		corrections.clear();
		// Metadata
		for(CorrectionEnum c: CorrectionEnum.values()) {
			FileTime t = null;
			switch(c) {
			case CREATION:
				t = get(metadata, c, true);
				break;
			case MODIFIED:
				t = get(metadata, c, false);
				break;
			case ACCESSED:
				t = get(metadata, c, false);
				break;
			default:
				LOG.error("Unknown enum correction: {}", c.toString());
				break;
			}
			corrections.put(c, t);
		}
	}
}
