package gyk4j.wreck.view.preview.body.files;

import javax.swing.JScrollPane;

public class FileListScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private final FileList table;
	
	public FileListScrollPane() {
		super();
		this.table = new FileList();
		super.setViewportView(this.table);
	}

	public FileList getTable() {
		return table;
	}
}
