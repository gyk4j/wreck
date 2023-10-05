package gyk4j.wreck.view.preview.body.files;

import java.awt.Color;
import java.nio.file.Path;
import java.time.Instant;
import java.time.Period;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import gyk4j.wreck.view.preview.renderer.DateRenderer;
import gyk4j.wreck.view.preview.renderer.InstantRenderer;
import gyk4j.wreck.view.preview.renderer.IntegerRenderer;
import gyk4j.wreck.view.preview.renderer.LongRenderer;
import gyk4j.wreck.view.preview.renderer.PathRenderer;
import gyk4j.wreck.view.preview.renderer.PeriodRenderer;

public class FileList extends JTable {

	private static final int TABLE_ROW_HEIGHT = 20;
	private static final Color TABLE_GRID_COLOR = new Color(0xF0, 0xF0, 0xF0);

	private static final long serialVersionUID = 1L;
	
	private final FileListPopupMenu popupMenu;

	public FileList() {
		super();
		
		setAutoCreateRowSorter(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        setGridColor(TABLE_GRID_COLOR);
        setShowGrid(true);
        setRowHeight(TABLE_ROW_HEIGHT);
        
        setDefaultRenderer(Path.class, new PathRenderer());
        setDefaultRenderer(Integer.class, new IntegerRenderer());
        setDefaultRenderer(Long.class, new LongRenderer());
        setDefaultRenderer(Date.class, new DateRenderer());
        setDefaultRenderer(Instant.class, new InstantRenderer());
        setDefaultRenderer(Period.class, new PeriodRenderer());
        
//        ListSelectionModel selectionModel = new DefaultListSelectionModel();
//        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        contentsTable.setSelectionModel(selectionModel);
        
        // Add context menu
        popupMenu = new FileListPopupMenu(this);
        
        // sets the popup menu for the table
        setComponentPopupMenu(popupMenu);
        
        setFillsViewportHeight(true);
	}

	public FileListPopupMenu getPopupMenu() {
		return popupMenu;
	}
	
	public void resizeColumns() {
		// Size the columns
        
        TableColumnModel columnModel = getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(512);
        for(int i=1; i < columnModel.getColumnCount(); i++) {
        	columnModel.getColumn(i).setPreferredWidth(128);
        }        
	}
	
}
