package gyk4j.wreck.io.reader.metadata;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import uk.co.caprica.vlcjinfo.MediaInfo;
import uk.co.caprica.vlcjinfo.MediaInfoParseException;
import uk.co.caprica.vlcjinfo.Section;

public class MediaInfoReader extends AbstractTimestampReader {
	private static final String SECTION_GENERAL = "General";

	private static final Logger LOG = LoggerFactory.getLogger(MediaInfoReader.class);
	
	protected static final String[] CREATION = {
			"Encoded date",
			"Recorded date",
			"creation_time",
			"Encoded_Date",
			"Tagged_Date",
			"Recorded_Date",
	};
	
	private static final Pattern REGEXP_VALUE = Pattern.compile(
			"(UTC )?([\\d]{4})-([\\d]{2})-([\\d]{2}) ([\\d]{2}):([\\d]{2}):([\\d]{2})");
	
	protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("[z ]yyyy-MM-dd HH:mm:ss[.SSS]");
	
	public MediaInfoReader(){
		super();
	}
	
	@Override
	public String[] creation() {
		return CREATION;
	}

	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {		
		try {
			MediaInfo mediaInfo = MediaInfo.mediaInfo(file.toAbsolutePath().toString());
			
			if(mediaInfo != null) {
				Section general = mediaInfo.first(SECTION_GENERAL);
				
				if(general != null) {
					// Loop through the metadata tags we are aiming for.
					for(int i = 0; i < CREATION.length; i++) {
						String value = general.value(CREATION[i]);
						
						if(value != null) {
							try {
								add(metadata, CREATION[i], value, parse(value));
							}
							catch (ParseException e) {
								LOG.error(e.toString());
							}
						}
					}
				}
			}
		}
		catch(MediaInfoParseException e) {
			LOG.error(e.toString());
		}		
	}

	private Instant parse(String dateTime) throws ParseException {
		Instant i = null;
		
		Matcher m = REGEXP_VALUE.matcher(dateTime);
		if(m.matches()) {
			String temp = dateTime;

			if(temp.startsWith("UTC")) {
				temp = temp.replaceAll("UTC", "").trim();
			}

			if(!temp.endsWith("Z")) {
				temp = temp.concat("Z");
			}

			if(temp.charAt(10) == ' ') {
				temp = temp.replace(' ', 'T');
			}

			if(!"0000-00-00T00:00:00Z".equals(temp))
				i = Instant.parse(temp);
		}
		else if(dateTime.length() == 4) {
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
		
		if(i == null) {			
			throw new ParseException("Unparsable date time: " + dateTime, 0);
		}

		return i;
	}
}
