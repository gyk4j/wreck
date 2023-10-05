package gyk4j.wreck.resources;

public enum CorrectionEnum {
	CREATION("Creation", "Apply correction to file creation time attribute"),
	MODIFIED("Last modified", "Apply correction to last modified time attribute"),
	ACCESSED("Last accessed", "Apply correction to last accessed time attribute");
	
	String name;
	String description;
	
	CorrectionEnum(String name, String description) {
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
