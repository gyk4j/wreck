package gyk4j.wreck.time;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimestampFormatter {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimestampFormatter.class);
	
	public static final String WORD_BOUNDARY = "\\b";
	
	private static final DateTimeFormatter EXIF_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
	private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static Path isValidPath(String path) throws InvalidPathException, IllegalArgumentException {
		Path p = Paths.get(path);
		
		if(!Files.exists(p, LinkOption.NOFOLLOW_LINKS)){
			throw new IllegalArgumentException(p + " does not exist.");
		}
		
		// Files.walkFileTree() works with path to a single file.
		// Disabling the check.
		/*
		if(!Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS)){
			throw new IllegalArgumentException(p + " is not a directory.");
		}
		 */
		
		if(!Files.isReadable(p)) {
			throw new IllegalArgumentException(p + " is not readable.");
		}
		
		if(!Files.isWritable(p)) {
			throw new IllegalArgumentException(p + " is not writable.");
		}
		
		return p;
	}
	
	public static Instant isISO8601(String date) {
		if(date.trim().length() != 10 && 		// 2022-12-23
				date.trim().length() < 19) {	// 2022-12-23T02:37:59.999999Z
			return null;						// 2022-12-23T02:37:59
		}
		
		// check date
		if(date.charAt(4) != '-' ||  
				date.charAt(7) != '-') {
			return null;
		}
		
		// check time. 
		// Date and time must be separated by T.
		// Time must be separated by colons for hours:minutes:seconds
		// May or may not end in UTC Z.
		if (date.length() > 10) {
			if (
					date.charAt(10) != 'T' || 
					date.charAt(13) != ':' || 
					date.charAt(16) != ':' 
					) {
				return null;
			}
		}
		
		// Date only without time, time zone, and time offset
		if(date.length() == 10) {
			LocalDate dt = LocalDate.parse(date);
			return ZonedDateTime.of(dt, LocalTime.MIDNIGHT, ZoneOffset.systemDefault()).toInstant();
		}
		// Date with local time without UTC Z
		else if(date.length() >= 19 && !date.endsWith("Z")) {
			LocalDateTime dt = LocalDateTime.parse(date);
			return ZonedDateTime.of(dt, ZoneOffset.systemDefault()).toInstant();
		}
		else {
			return Instant.parse(date);
		}		
	}
	
	public static Instant isLocalDateTime(String date) {
		Instant i = null;
		
		try {
			LocalDateTime l = LocalDateTime.parse(date, LOCAL_DATE_TIME_FORMATTER);
			ZonedDateTime z = ZonedDateTime.of(l, ZoneOffset.systemDefault());
			ZonedDateTime utc = z.withZoneSameInstant(ZoneOffset.UTC);
			i = utc.toInstant();
		} catch(DateTimeParseException e) {
			i = null;
		}
		return i;
	}
	
	public static Instant hasExif(String date) {
		Instant instant = null;
		try {
			LocalDateTime local= LocalDateTime.parse(date, EXIF_FORMATTER);
			instant = local.atZone(ZoneId.systemDefault()).toInstant();
//			System.out.println("Exif: " + instant);
		}
		catch(DateTimeParseException ex) {
//			System.err.println("Not exif date: " + date);
			instant = null;
		}
		return instant;
	}
	
	public static Instant getMetaDataInstant(Metadata metadata, String key) {
		Instant extracted = null;
		String value = metadata.get(key);
		if(value != null) {
			extracted = TimestampFormatter.isISO8601(value);
			if(extracted == null) {
				LOGGER.warn("{} does not resemble a ISO8601 string.", value);
			}
		}
		else {
//			System.err.println("WARN: " + file.toAbsolutePath() + " does not have " + key);
			for (String name : metadata.names()) {
				extracted = TimestampFormatter.isISO8601(metadata.get(name));
				if(extracted != null) {
					LOGGER.info("Found {} = {}", name, extracted);
				}
			}
		}
		return extracted;
	}
	
	private static final DateTimeFormatter FORMATTER_EXIFTOOL_LOCAL = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy");
	private static final DateTimeFormatter FORMATTER_EXIFTOOL_ENUS = DateTimeFormatter.ofPattern("yyyy[:dd:MM HH:mm:ss[XXX][X]]");
	private static final DateTimeFormatter FORMATTER_EXIFTOOL_ENSG = DateTimeFormatter.ofPattern("yyyy[:MM:dd HH:mm:ss[XXX][X]]");
	public static Instant getExifToolInstant(String value) {
		Instant instant = null;
		
		if(value.length() == 4 && StringUtils.isNumeric(value))
			instant = LocalDateTime.of(Integer.parseInt(value), Month.JANUARY, 1, 0, 0, 0).toInstant(ZoneOffset.UTC);
		else if(value.contains("0000:00:00 00:00:00"))
			instant = null;
		else if(value.contains("1970-01-01 00:00:00"))
			instant = null;
		
		if(instant == null) {
			try {
				instant = LocalDateTime.parse(value, FORMATTER_EXIFTOOL_LOCAL).toInstant(ZoneOffset.UTC);
			}
			catch(DateTimeException e) {
				instant = null;
			}
		}
		
		// No offset time or zone offset
		if(instant == null && value.startsWith("UTC")) {
			String stripped = value.replaceFirst("UTC ", "");
			StringBuilder sb = new StringBuilder(stripped);
			if(sb.charAt(10) == ' ')
				sb.setCharAt(10, 'T');
			
			String mediaInfoValue = sb.toString();
			
			try {
				LocalDateTime ldt = LocalDateTime.parse(mediaInfoValue);
				instant = ZonedDateTime.of(ldt, ZoneOffset.UTC).toInstant();
			} catch(DateTimeParseException e) {
				LOGGER.warn(e.toString());
			}
		}
			
		
		if(instant == null && !value.endsWith("Z") && value.indexOf('+') == -1 && value.indexOf('-') == -1) {
			try {
				instant = ZonedDateTime.of(LocalDateTime.parse(value), ZoneOffset.systemDefault()).toInstant();
			}
			catch(DateTimeException e) {
				instant = null;
			}
		}
		
		// if yyyy-mm-dd
		if(instant == null) {
			try {
				instant = Instant.from(FORMATTER_EXIFTOOL_ENSG.parse(value));
			}
			catch(DateTimeException e) {
				instant = null;
			}
		}
		
		// If yyyy-dd-mm 
		if(instant == null) {
			try {
				instant = LocalDateTime.from(FORMATTER_EXIFTOOL_ENUS.parse(value)).toInstant(ZoneOffset.UTC);
			}
			catch(DateTimeException e) {
				instant = null;
			}
		}
		
		if(instant == null)
			LOGGER.warn("Failed to parse: {}", value);
		
		return instant;
	}
}
