package gyk4j.wreck.view.preview.renderer;

import java.awt.Component;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;

public class DateRenderer extends GenericRenderer {
	
	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		Date d = (Date) value;
		Instant instant = Instant.ofEpochMilli(d.getTime());
		setText(FORMATTER.format(instant));
		setHorizontalAlignment(SwingConstants.CENTER);
		return this;
	}

}
