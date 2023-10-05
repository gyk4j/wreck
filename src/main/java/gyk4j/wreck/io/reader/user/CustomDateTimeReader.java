package gyk4j.wreck.io.reader.user;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import gyk4j.wreck.resources.R;

public class CustomDateTimeReader extends AbstractTimestampReader {

//	private static final Logger LOG = LoggerFactory.getLogger(CustomDateTimeReader.class);

	protected static final String[] USER = {
			R.string.USER_CUSTOM_DATE_TIME
	};
	
	private Date custom;
	
	public CustomDateTimeReader() {
		super();
		custom = new Date();
	}
	
	@Override
	public String[] creation() {
		return USER;
	}
	
	public Date getCustom() {
		return custom;
	}

	public void setCustom(Date custom) {
		this.custom = custom;
	}
	
	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {
		Instant i = (custom != null)? custom.toInstant() : null;
		String value = (i != null)? i.toString() : null;
		add(	metadata,
				USER[0],
				value,
				i);
	}
	
	/*
	@Override
	public Map<CorrectionEnum, Set<Instant>> group(Map<String, String> tags) {
		Map<CorrectionEnum, Set<Instant>> corrections = super.group(tags);
		
		select(tags, USER, corrections, 
			CorrectionEnum.CREATION,
			CorrectionEnum.MODIFIED,
			CorrectionEnum.ACCESSED);
		
		return corrections;
	}
	*/
}
