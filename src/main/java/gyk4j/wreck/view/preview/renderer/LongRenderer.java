package gyk4j.wreck.view.preview.renderer;

import java.awt.Component;
import java.text.MessageFormat;

import javax.swing.JTable;
import javax.swing.SwingConstants;

public class LongRenderer extends GenericRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setText(MessageFormat.format("{0,number,integer}", value));
		setHorizontalAlignment(SwingConstants.TRAILING);

		return this;
	}

}
