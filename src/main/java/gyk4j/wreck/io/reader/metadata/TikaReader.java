package gyk4j.wreck.io.reader.metadata;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import gyk4j.wreck.security.PasswordProvider;

public class TikaReader extends AbstractTimestampReader {
	private static final String TIKA_CONFIG_XML = "/tika-config.xml";

	private static final Logger LOG = LoggerFactory.getLogger(TikaReader.class);
	
	private static final Pattern PATTERN_BLACKLIST = Pattern.compile("(creator|producer|File Modified Date)", CASE_INSENSITIVE);
	
	public static final String[] CREATION = {
			"Exif IFD0:Date/Time",
			"Exif SubIFD0:Date/Time Digitized",
			"Exif SubIFD0:Date/Time Original",
			"Exif SubIFD:Date/Time Digitized",
			"Exif SubIFD:Date/Time Original",
			"creation_time",
			"dcterms:created",
			"exif:DateTimeOriginal",
			"pdf:docinfo:created",
			"xmp:CreateDate",
			"xmpDM:releaseDate",	
	};
	
	public static final String[] MODIFIED = {
			"dcterms:modified",
			"meta:print-date",
			"pdf:docinfo:modified",
			"xmp:MetadataDate",
			"xmp:ModifyDate",
			"xmp:MM:History:When",
			"xmpMM:History:When",
	};
	
	private static final Pattern REGEXP_VALUE = Pattern.compile(
			"([\\d]{4})-([\\d]{2})-([\\d]{2})T([\\d]{2}):([\\d]{2}):([\\d]{2})Z?");
	
	private Tika tika = null;
	private char[] buffer = new char[4096];
	
	public TikaReader() {
		ParseContext context = new ParseContext();
		context.set(PasswordProvider.class, PasswordProvider.getInstance());
		
		URL url = TikaReader.class.getResource(TIKA_CONFIG_XML);
		
		try {
			TikaConfig config = new TikaConfig(url);
			tika = new Tika(config);
		} catch (TikaException e) {
			LOG.error(e.toString());
		} catch (IOException e) {			
			LOG.error(e.toString());
		} catch (SAXException e) {			
			LOG.error(e.toString());
		}
		
		LOG.info("Apache Tika initialized");
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
		org.apache.tika.metadata.Metadata tikaMetadata = 
				new org.apache.tika.metadata.Metadata();

		try (Reader reader = tika.parse(file, tikaMetadata)){
			
			while(reader.read(buffer) > 0);
//			buffer = null;
			
			for (String name : tikaMetadata.names()) {
				if(!PATTERN_BLACKLIST.matcher(name).find()) {
					try {
						String value = tikaMetadata.get(name);						
						add(metadata, name, value, parse(value));
					}
					catch (ParseException ex) {
//						LOG.error("{}: {}", file.getFileName(), ex.toString());
					}
				}				
			}
		} catch (FileNotFoundException e) {
			LOG.error("{}: {}", file.getFileName(), e.toString());
		} catch (IOException e) {
			LOG.error("{}: {}", file.getFileName(), e.toString());
		} finally {
			metadata = null;
		}
	}

	private Instant parse(String dateTime) throws ParseException {
		Instant i = null;
		Matcher m = REGEXP_VALUE.matcher(dateTime);
		if(m.matches() && !"0000-00-00T00:00:00Z".equals(dateTime)) {
			String temp = dateTime;
			if(!temp.endsWith("Z"))
				temp = temp.concat("Z");
			i = Instant.parse(temp);
		}
		
		if(i == null) {
			throw new ParseException("Unparsable date time: " + dateTime, 0);
		}
		
		return i;
	}
}
