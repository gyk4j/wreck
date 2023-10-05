package gyk4j.wreck.view.preview;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import gyk4j.wreck.resources.R;
import gyk4j.wreck.util.ResourceLoader;
import gyk4j.wreck.view.preview.body.PreviewBody;
import gyk4j.wreck.view.preview.footer.PreviewFooter;
import gyk4j.wreck.view.preview.header.PreviewHeader;

public class PreviewDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final PreviewHeader header;
	private final PreviewBody body;
	private final PreviewFooter footer;

	public PreviewDialog() {
		super();
		
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        ImageIcon icon = (ImageIcon) ResourceLoader.getIcon(R.icon.APP);
        this.setIconImage(icon.getImage());
        setPreferredSize(new Dimension(R.dimen.FRAME_WIDTH, R.dimen.FRAME_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setResizable(false);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().setLayout(new BorderLayout());
        
        header = new PreviewHeader();
        getContentPane().add(header, BorderLayout.PAGE_START);
        
        body = new PreviewBody();
        getContentPane().add(body, BorderLayout.CENTER);
        
        footer = new PreviewFooter();
        getContentPane().add(footer, BorderLayout.PAGE_END);
	}

	public void open() {
		//Display the window.
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	}

	public PreviewBody getBody() {
		return body;
	}

	public PreviewHeader getHeader() {
		return header;
	}

	public PreviewFooter getFooter() {
		return footer;
	}
	
}
