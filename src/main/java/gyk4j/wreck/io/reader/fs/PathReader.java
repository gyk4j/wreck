package gyk4j.wreck.io.reader.fs;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import gyk4j.wreck.resources.R;

public class PathReader extends AbstractTimestampReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(PathReader.class);
	
	public static final String[] CREATION = {
			R.string.PATH_NAME,
	};
	
	public static final String DATE_PART_SEPARATOR = "[\\\\\\-_\\.:/]?";
	public static final String YEAR_FULL_RANGE = "(?<year>19[8,9][0-9]|20([0-1][0-9]|2[0-3]))";
	public static final String YEAR_SHORT_RANGE = "(?<year>[8,9][0-9]|[0-1][0-9]|2[0-3])";
	public static final String MONTH_RANGE = "(?<month>0[1-9]|1[0-2])";
	public static final String DAY_RANGE = "(?<day>0[1-9]|[1,2][0-9]|3[0,1])";
	
	public static final String TIME_RANGE = "([0,1][0-9]|2[0-3])[0-5][0-9][0-5][0-9]";
	
	private static final String WORD_BOUNDARY = "";
	public static final String FILENAME_DATE_LONG = WORD_BOUNDARY + YEAR_FULL_RANGE + MONTH_RANGE + DAY_RANGE + WORD_BOUNDARY;
	public static final String FILENAME_DATE_SHORT = WORD_BOUNDARY + YEAR_SHORT_RANGE + MONTH_RANGE + DAY_RANGE + WORD_BOUNDARY;
	public static final String FILENAME_DATE_PATH = DATE_PART_SEPARATOR + YEAR_FULL_RANGE + DATE_PART_SEPARATOR + MONTH_RANGE + DATE_PART_SEPARATOR + DAY_RANGE + WORD_BOUNDARY;
	
	public static final String FILENAME_TIME = WORD_BOUNDARY + TIME_RANGE + WORD_BOUNDARY;
	public static final String FILENAME_DATETIME = WORD_BOUNDARY + YEAR_FULL_RANGE + MONTH_RANGE + DAY_RANGE + TIME_RANGE + WORD_BOUNDARY;
	
	public static final Pattern FILENAME_PATTERN_DATE_LONG = Pattern.compile(FILENAME_DATE_LONG);
	public static final Pattern FILENAME_PATTERN_DATE_SHORT = Pattern.compile(FILENAME_DATE_SHORT);
	public static final Pattern FILENAME_PATTERN_DATE_PATH = Pattern.compile(FILENAME_DATE_PATH);
	public static final Pattern FILENAME_PATTERN_TIME = Pattern.compile(FILENAME_TIME);
	
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
	
	public PathReader() {
		super();
	}
	
	@Override
	public String[] creation() {
		return CREATION;
	}

	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {		
		String date = null;
		String time = null;
		LocalDate localDate = null;
		LocalTime localTime = null;
		String filename = file.toString();
		Matcher matcher;

		matcher = FILENAME_PATTERN_DATE_LONG.matcher(filename);
		if (matcher.find()) {
			date = matcher.group();
		}

		if (date == null) {
			matcher = FILENAME_PATTERN_DATE_SHORT.matcher(filename);
			if (matcher.find()) {
				date = matcher.group();
				String prefix = (date.charAt(0) == '8' || date.charAt(0) == '9') ? "19" : "20";
				date = prefix.concat(date);
			}
		}

		if (date == null) {
			matcher = FILENAME_PATTERN_DATE_PATH.matcher(file.toAbsolutePath().toString());
			if (matcher.find()) {
				String year = matcher.group("year");
				String month = matcher.group("month");
				String day = matcher.group("day");
				LOGGER.debug("y = {}, m = {}, d = {}", year, month, day);
				date = String.format("%04d%02d%02d", 
						Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
			}
		}

		// Found a date. Let's try to find the time if available.
		if (date != null) {
			try {
				localDate = LocalDate.parse(date, DATE_FORMATTER);
			} catch (DateTimeParseException e) {
				LOGGER.error(e.getMessage() + ":" + date);
			}

			int end = matcher.end(); // Start searching after the date.
			matcher = FILENAME_PATTERN_TIME.matcher(filename);
			if (matcher.find(end)) {
				time = matcher.group();
				try {
					localTime = LocalTime.parse(time, TIME_FORMATTER);
				} catch (DateTimeParseException e) {
					LOGGER.error(e.getMessage() + ":" + time);
				}
			}
		}

		if (localDate != null) {
			localTime = (localTime == null) ? LocalTime.NOON : localTime;

			ZonedDateTime zdt = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault());
			Instant instant = zdt.toInstant();

			add(metadata, CREATION[0], instant.toString(), instant);
		}
	}

	/*
	@Override
	public Map<CorrectionEnum, Set<Instant>> group(Map<String, String> tags) {
		Map<CorrectionEnum, Set<Instant>> corrections = super.group(tags);
		
		select(tags, CREATION, corrections, 
				CorrectionEnum.CREATION,
				CorrectionEnum.MODIFIED,
				CorrectionEnum.ACCESSED);
		
		return corrections;
	}
	*/
}
