package gyk4j.wreck.io.reader.metadata;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import com.thebuzzmedia.exiftool.core.UnspecifiedTag;
import com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;

public class ExifToolReader extends AbstractTimestampReader {

	private static final Logger LOG = LoggerFactory.getLogger(ExifToolReader.class);
	
	protected static final String[] CREATION = {
			"CREATE_DATE",
			"CREATION_DATE",
			"DATE_TIME_ORIGINAL",
			"DateTimeOriginal",
			"CreationDate",
			"CreateDate",
	};
	
	protected static final String[] MODIFIED = {
			"UnspecifiedTag{name: \"ModifyDate\"}",
			"ModifyDate",
	};
	
	private static final Pattern REGEXP_VALUE = Pattern.compile("([\\d]{4}):([\\d]{2}):([\\d]{2}) ([\\d]{2}):([\\d]{2}):([\\d]{2}).*");
	
	protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss[.SSS]");
	
	private static final DateTimeFormatter FMT1 = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss");
	private static final DateTimeFormatter FMT2 = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy");
	
	private ExifTool exifTool = null;

	public ExifToolReader() {
		try {
			exifTool = new ExifToolBuilder().enableStayOpen().build();
		} catch (UnsupportedFeatureException ex) {
			// Fallback to simple exiftool instance.
			LOG.warn("Using exiftool with stay-open=off");
			exifTool = new ExifToolBuilder().build();
		}
		
		if(exifTool != null)
			LOG.info("ExifTool library was initialized");
		else
			LOG.error("Failed to initialize ExifTool.");
	}
	
	@Override
	public String[] creation() {
		return CREATION;
	}

	@Override
	public String[] modified() {
		return MODIFIED;
	}

	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {
//		LOGGER.trace(file.toString());
		
		if(exifTool == null)
			return;
		
		try {
			Map<Tag, String> out = exifTool.getImageMeta(
					file.toFile(), 
					Arrays.asList(
							StandardTag.DATE_TIME_ORIGINAL,
							StandardTag.CREATE_DATE,
							StandardTag.CREATION_DATE,
							new UnspecifiedTag("ModifyDate")));
			
			if(!out.isEmpty()) {
				out.forEach((tag, value) -> {
					try {
						add(metadata, tag.getName(), value, parse(value));
					} catch (ParseException e) {
//						LOG.error(e.toString());
					}
				});
			}
//			else
//				LOGGER.warn("No metadata found: {}", file.getFileName());
		} catch (IOException e) {
			LOG.error(e.toString());
		} 
	}
	
	@Override
	public void close() {
		try {
			if(exifTool != null) {
				exifTool.close();
				exifTool = null;
			}
		} catch (Exception e) {
			LOG.error(e.toString());
		}
	}

	private Instant parse(String dateTime) throws ParseException {
		Instant i = null;
		
		if("0".equals(dateTime)) {
			throw new ParseException("Unparsable date time: {}" + dateTime, 0);
		}
		
		if(dateTime.length() == 4) {
			try {
				int yyyy = Integer.parseInt(dateTime);

				if(yyyy >= LocalDate.now().getYear()-100 && 
						yyyy <= LocalDate.now().getYear()) {
					i = ZonedDateTime.of(LocalDate.of(yyyy, 1, 1), LocalTime.NOON, ZoneOffset.UTC).toInstant();
				}
			}
			catch(NumberFormatException e) {
				i = null;
			}
		}
		
		Matcher m = REGEXP_VALUE.matcher(dateTime);
		if(i == null && m.matches() && m.groupCount() == 6) {
			String month = m.group(2), dayOfMonth = m.group(3);
			
			// If US date.
			int mm = Integer.parseInt(month);
			int dd = Integer.parseInt(dayOfMonth);
			if(mm > 12 && mm <= 31 && dd > 0 && dd <= 12) {
				// month looks like a dayOfMonth
				month = m.group(3);
				
				// dayOfMonth looks like a month
				dayOfMonth = m.group(2);				
			}
			
			String temp = String.format(
					"%s-%s-%sT%s:%s:%sZ", 
					m.group(1), 
					month, 
					dayOfMonth, 
					m.group(4), 
					m.group(5), 
					m.group(6));
			
			if(!"0000-00-00T00:00:00Z".equals(temp)) {
				i = Instant.parse(temp);
			}
		}
		
		if(i == null) {
			try {
				LocalDateTime l = LocalDateTime.parse(dateTime, FMT1);
				ZonedDateTime z = ZonedDateTime.of(l, ZoneOffset.systemDefault());
				i = z.withZoneSameLocal(ZoneOffset.UTC).toInstant();
				LOG.info("{} -> {}", dateTime, i.toString());
			} catch(DateTimeParseException e) {
				i = null;
			}
		}
		
		if(i == null) {
			try {
				LocalDateTime l = LocalDateTime.parse(dateTime, FMT2);
				ZonedDateTime z = ZonedDateTime.of(l, ZoneOffset.systemDefault());
				i = z.withZoneSameLocal(ZoneOffset.UTC).toInstant();
				LOG.info("{} -> {}", dateTime, i.toString());
			} catch(DateTimeParseException e) {
				i = null;
			}
		}
		
		if(i == null) {
			throw new ParseException("Unparsable date time: " + dateTime, 0);
		}
		
		return i;
	}
}
