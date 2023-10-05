package gyk4j.wreck.time;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public class TimeUtils {
	// Windows Explorer will not show any date time before 1980-01-01T00:00:00.
	// Safe to assume that any legit file time should be after that.
	// Anything other than that are likely some placeholder or fake values.
	public static final FileTime VALID_PERIOD_MIN = 
			FileTime.from(ZonedDateTime.of(
					LocalDate.of(1980, Month.JANUARY, 1), 
					LocalTime.MIDNIGHT, 
					ZoneOffset.UTC)
					.toInstant());

	// No file should be created, modified or accessed beyond the current latest time.
	// Any future date time values found are likely placeholder values.
	public static final FileTime VALID_PERIOD_MAX = 
			FileTime.from(ZonedDateTime.of(
			LocalDateTime.now(), 
			ZoneOffset.UTC)
			.toInstant());
	
	public static Instant getInstant(TimeOrder order, Instant instant, FileTime filetime) throws Exception {
		Instant fileInstance = filetime.toInstant();
		if(order == TimeOrder.BEFORE) {
			return instant.isBefore(fileInstance)? instant: fileInstance;
		}
		else if(order == TimeOrder.AFTER) {
			return instant.isAfter(fileInstance)? instant: fileInstance;
		}
		else {
			throw new Exception("");
		}
	}
	
	public static boolean isLaterThan(FileTime a, FileTime b) {
		if(a == null || b == null)
			return false;
		
		LocalDateTime ldtA = LocalDateTime.ofInstant(a.toInstant(), ZoneOffset.systemDefault());
		LocalDateTime ldtB = LocalDateTime.ofInstant(b.toInstant(), ZoneOffset.systemDefault());
		
		return ldtA.toLocalDate().isAfter(ldtB.toLocalDate());
	}
	
	public static boolean isSameTime(FileTime a, FileTime b) {
		if(a == null || b == null)
			return false;
		
		return a.equals(b);
	}
	
	public static Instant updateDateTime(Instant dateTimeStamp, boolean before, FileTime time, long epochTime) {
		Instant instant = null;
		
		// Use zip extended attributes as first choice for better accuracy.
		if(time != null) {
			instant = time.toInstant();
		}
		// Fallback for older format zip archive without the extended attributes.
		else if(epochTime > 0) {
			instant = new Date(epochTime).toInstant();			
		}
		else {
			instant = null;
		}
		
		if(instant != null) {
			int compare = instant.compareTo(dateTimeStamp);
			if(compare == 0) {
				// Expected == Actual. No need to fix.
//				System.out.println("Same timestamp. Ignored.");
			}
			else if((before && compare < 0) || (!before && compare > 0)) {
				// Update the last found timestamp with an earlier or later one.
				dateTimeStamp = instant;
			}
			else {
//				System.out.println("Worse timestamp. Ignored.");
			}
		}
		else {
			// Unlikely. 
			// For standard zip32, at least one time (usually last modified) should be available.
			// For zip64, there are Creation, Modified and Accessed time that may or may not be filled.
//			System.out.println("No metadata timestamp found.");
			dateTimeStamp = null;
		}
		
		return dateTimeStamp;
	}
}
