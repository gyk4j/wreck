package gyk4j.wreck.resources;

public enum SourceEnum {
	METADATA("Metadata tags", "Use metadata tag values if available."),
	FILE_SYSTEM("File system attributes", "Use the earliest file system attribute time"),
	CUSTOM("Custom", "Use a user-specified date");
	
	String name;
	String description;
	
	SourceEnum(String name, String description) {
		this.name = name;
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
}
