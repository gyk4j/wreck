package gyk4j.wreck.io.writer;

import static gyk4j.wreck.time.TimeUtils.isLaterThan;
import static gyk4j.wreck.time.TimeUtils.isSameTime;

import java.nio.file.attribute.FileTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.util.logging.FileEvent;
import gyk4j.wreck.util.logging.StatisticsCollector;

public abstract class AbstractTimestampWriter implements ITimestampWriter {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractTimestampWriter.class);
	private static final StatisticsCollector STATS = StatisticsCollector.getInstance();
	
	protected FileTime writeAttribute(
			FileEvent required, 
			FileEvent none, 
			FileTime fileSystem, 
			FileTime metadata) {
		FileTime value = null;
		
		FileTime correct = metadata;
		
		if (correct == null){
			STATS.count(none);
		}
		else if(isLaterThan(fileSystem, correct)) {
			STATS.count(required);
			value = correct;
		}
		return value;
	}
	
	protected FileTime restoreAttribute(
			FileEvent required, 
			FileEvent none, 
			FileTime fileSystem,
			FileTime metadata) {
		
		if (metadata == null){
			STATS.count(none);
			LOG.warn("Missing correct value");
		}
		else if(!isSameTime(fileSystem, metadata)){
			STATS.count(required);
		}
		
		return metadata;
	}

}
