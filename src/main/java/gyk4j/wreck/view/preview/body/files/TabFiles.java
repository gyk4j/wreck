package gyk4j.wreck.view.preview.body.files;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import gyk4j.wreck.resources.R;

public class TabFiles extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final FileListScrollPane fileList;

	public TabFiles() {
		super();
		
		fileList = new FileListScrollPane();
		setLayout(new BorderLayout());
		setBorder(R.style.BORDER_EMPTY_8);
		add(fileList, BorderLayout.CENTER);
	}
	
	public FileListScrollPane getFileList() {
		return fileList;
	}
}
