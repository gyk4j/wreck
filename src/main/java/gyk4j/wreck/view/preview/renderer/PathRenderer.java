package gyk4j.wreck.view.preview.renderer;

import java.awt.Component;
import java.nio.file.Path;

import javax.swing.JTable;
import javax.swing.SwingConstants;

public class PathRenderer extends GenericRenderer {

	private static final long serialVersionUID = 1L;
	
//	private static final Logger LOG = LoggerFactory.getLogger(PathRenderer.class);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		Path path = (Path) value;
		setToolTipText((path != null)? path.toString(): "");
		setText((path != null && path.getFileName() != null)? path.getFileName().toString(): "");
		setHorizontalAlignment(SwingConstants.LEADING);
		return this;
	}

}
