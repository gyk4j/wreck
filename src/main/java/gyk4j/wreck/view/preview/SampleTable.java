package gyk4j.wreck.view.preview;

import javax.swing.JTable;

import gyk4j.wreck.resources.R;
import gyk4j.wreck.view.preview.renderer.IntegerRenderer;
import gyk4j.wreck.view.preview.renderer.StringRenderer;

public class SampleTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	public SampleTable() {
		super();
		
		setAutoCreateRowSorter(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        setGridColor(R.color.TABLE_GRID_COLOR);
        setShowGrid(true);
        setRowHeight(R.dimen.TABLE_ROW_HEIGHT);
        
        setDefaultRenderer(String.class, new StringRenderer());
        setDefaultRenderer(int.class, new IntegerRenderer());
		
		setFillsViewportHeight(true);
	}
}