package gyk4j.wreck.view.preview.renderer;

import java.awt.Component;

import javax.swing.JTable;

public class StringRenderer extends GenericRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		String text = (String) value;
		setText(text);
		return this;
	}
}
