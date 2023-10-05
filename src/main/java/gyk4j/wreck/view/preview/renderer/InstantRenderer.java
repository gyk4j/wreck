package gyk4j.wreck.view.preview.renderer;

import java.awt.Component;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class InstantRenderer extends GenericRenderer {
	
	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		Instant instant = (Instant) value;
		setText((instant != null)? FORMATTER.format(instant): "");
		setHorizontalAlignment(SwingConstants.CENTER);
		return this;
	}

}
