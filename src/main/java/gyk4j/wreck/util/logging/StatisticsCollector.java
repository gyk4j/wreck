package gyk4j.wreck.util.logging;

import java.util.Map;
import java.util.TreeMap;

public class StatisticsCollector {
	private static StatisticsCollector instance;
	
	private final Map<FileEvent, Integer> statistics = new TreeMap<>();
	private final Map<String, Integer> metadataKeys = new TreeMap<>();
	private final Map<String, ExtensionStatistics> fileExtensions = new TreeMap<>();	
//	private final Map<SelectionEvent, Integer> selection = new TreeMap<>();
	
	private StatisticsCollector() {
		super();
		
	}
	
	public static StatisticsCollector getInstance() {
		if(instance == null) {
			instance = new StatisticsCollector();
		}
		
		return instance;
	}
	
	public Map<FileEvent, Integer> getStatistics() {
		return statistics;
	}
	
	public int get(FileEvent e) {
		return getStatistics().getOrDefault(e, 0);
	}
	
	public int count(FileEvent e) {
		Integer v = getStatistics().merge(e, 1, (ov, nv) -> ++ov);
		return (v == null)? 1: v;
	}
	
	/*
	private float getFileCorrectedCreationPercentage() {
		return ((float) get(LogEvent.CORRECTED_CREATION) / (float) get(LogEvent.FILE_FOUND))*100;
	}
	
	private float getFileCorrectedLastModifiedPercentage() {
		return ((float) get(LogEvent.CORRECTED_MODIFIED) / (float) get(LogEvent.FILE_FOUND))*100;
	}

	private float getFileCorrectedLastAccessedPercentage() {
		return ((float) get(LogEvent.CORRECTED_ACCESSED) / (float) get(LogEvent.FILE_FOUND))*100;
	}
	*/

	public Map<String, Integer> getMetadataKeys() {
		return metadataKeys;
	}
	
	public int get(TagEvent e) {
		return getMetadataKeys().getOrDefault(e.toString(), 0);
	}
	
	public int count(TagEvent e) {
		Integer v = getMetadataKeys().merge(e.toString(), 1, (ov, nv) -> ++ov);
		return (v == null)? 1: v;
	}
	
	public Map<String, ExtensionStatistics> getFileExtensions() {
		return fileExtensions;
	}
	
	public void count(ExtensionEvent e) {
		getFileExtensions().merge(
				e.toString(), 
				new ExtensionStatistics(e.getFile(), e.hasMetadata), 
				(ov, nv) -> {
					ov.addTotal();

					if(nv.getHasMetadata() > 0)
						ov.addMetadata();

					return ov;
				});
	}
	
//	public Map<SelectionEvent, Integer> getSelection() {
//		return selection;
//	}
//	
//	public int count(SelectionEvent e) {
//		Integer v = getSelection().merge(e, 1, (ov, nv) -> ++ov);
//		return (v == null)? 1: v;
//	}
//	
//	public int get(SelectionEvent e) {
//		return getSelection().getOrDefault(e, 0);
//	}
	
	public void reset() {
		getStatistics().clear();
		getMetadataKeys().clear();
		getFileExtensions().clear();
//		getSelection().clear();
	}
}
