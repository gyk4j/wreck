package gyk4j.wreck.resources;

import java.awt.Color;

public enum CorrectionsChartSeriesEnum {
	REQUIRED(0, "Required", R.color.CORRECTION_REQUIRED), // new Color(0xFF, 0x6F, 0x00)
	NO_METADATA(1, "No metadata", R.color.CORRECTION_NO_METADATA), // new Color(0xB7, 0x1C, 0x1C)
	NON_REQUIRED(2, "Non-required", R.color.CORRECTION_NON_REQUIRED); // new Color(0x1E, 0x5B, 0x20)
	
	private final int index;
	private final String name;
	private final Color color;
	
	CorrectionsChartSeriesEnum(int index, String name, Color color) {
		this.index = index;
		this.name = name;
		this.color = color;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}
}
