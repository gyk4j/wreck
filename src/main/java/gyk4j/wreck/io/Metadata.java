package gyk4j.wreck.io;

import java.nio.file.attribute.FileTime;

import gyk4j.wreck.resources.CorrectionEnum;

public class Metadata implements Comparable<Metadata> {
	private final String key;
	private final String value;
	private final FileTime time;
	private final CorrectionEnum group;
	
	/**
	 * 
	 * @param key Metadata Tag Key
	 * @param value Raw tag value as string 
	 * @param time Parsed value as {@link java.nio.file.attribute.FileTime FileTime}
	 * @param group Attribute as {@link gyk4j.wreck.resources.ConnectionEnum ConnectionEnum}
	 */
	public Metadata(String key, String value, FileTime time, CorrectionEnum group) {
		super();
		this.key = key;
		this.value = value;
		this.time = time;
		this.group = group;
	}
	
	public Metadata clone(FileTime ft) {
		Metadata newInstance = new Metadata(key, ft.toString(), ft, group);
		return newInstance;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public FileTime getTime() {
		return time;
	}
	
	public CorrectionEnum getGroup() {
		return group;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
            return true;
		
		Metadata m = (Metadata) obj;
		
		return getKey().equals(m.getKey()) 
				&& getValue().equals(m.getValue())
				&& getTime().equals(m.getTime())
				&& getGroup().equals(m.getGroup());
	}
	@Override
	public int compareTo(Metadata o) {
		int result = 0;
		if (getTime() != null && o.getTime() != null)
			result = getTime().compareTo(o.getTime());
		else if(getTime() == null)
			result = 1;
		else if(o.getTime() == null)
			result = -1;
		else
			result = 0;
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", 
				key, 
				value, 
				group.toString());
	}
}
