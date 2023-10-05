package gyk4j.wreck.view.preview.body.statistics;

import java.awt.Color;

public enum ChartSeriesEnum {
	DONE(0, "done", new Color(0x66, 0xBB, 0x6A)),
	SKIPPED(1, "skipped", new Color(0xF4, 0x43, 0x36));
	
	private final int index;
	private final String name;
	private final Color color;
	
	ChartSeriesEnum(int index, String name, Color color) {
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
