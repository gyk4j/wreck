package gyk4j.wreck.view.preview.body.about;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.beans.AboutBean;
import gyk4j.wreck.resources.R;
import gyk4j.wreck.util.ResourceLoader;

public class TabAbout extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(TabAbout.class);
	
	private static final AboutBean[] ATTRIBUTIONS = {
			new AboutBean(
					"on-time.png", 
					"<html><a href=\"#\">On time icons created by Freepik - Flaticon</a></html>", 
					"on time icons",
					URI.create("https://www.flaticon.com/free-icons/on-time")),
			new AboutBean(
					"table.png", 
					"<html><a href=\"#\">Table icons created by Pixel perfect - Flaticon</a></html>", 
					"table icons",
					URI.create("https://www.flaticon.com/free-icons/table")),
			new AboutBean(
					"bar-graph.png", 
					"<html><a href=\"#\">Graph icons created by Pixel perfect - Flaticon</a></html>",
					"graph icons",
					URI.create("https://www.flaticon.com/free-icons/graph")),
			new AboutBean(
					"pie-chart.png", 
					"<html><a href=\"#\">Pie chart icons created by Pixel perfect - Flaticon</a></html>",
					"pie chart icons",
					URI.create("https://www.flaticon.com/free-icons/pie-chart")),
			new AboutBean(
					"file.png", 
					"<html><a href=\"#\">File icons created by Freepik - Flaticon</a></html>",
					"file icons",
					URI.create("https://www.flaticon.com/free-icons/file")),
			new AboutBean(
					"extension.png", 
					"<html><a href=\"#\">Alphabet icons created by bukeicon - Flaticon</a></html>",
					"alphabet icons",
					URI.create("https://www.flaticon.com/free-icons/alphabet")),
			new AboutBean(
					"metadata.png", 
					"<html><a href=\"#\">Metadata icons created by juicy_fish - Flaticon</a></html>",
					"metadata icons",
					URI.create("https://www.flaticon.com/free-icons/metadata")),
			new AboutBean(
					"search.png", 
					"<html><a href=\"#\">Search icons created by Freepik - Flaticon</a></html>",
					"search icons",
					URI.create("https://www.flaticon.com/free-icons/search")),
	};
	
	private static final String ACKNOWLEDGMENT = 
			"<html>" +
			"<p>This software includes software from other open source projects, " +
			"and works licensed under the Creative Commons license and the public domain. " +
			"Special thanks to the Apache Software Foundation, MediaArea, Phil Harvey, JFree, 7-Zip-JBinding (Boris Brodsky), Mark Lee (caprica) and Michael Jeanroy.</p>" +
			"</html>";
	
	private static final AboutBean[] THANKS = {
			new AboutBean(
					"logo-apache.png", 
					"<html><a href=\"#\">Apache Tika</a></html>",
					"Apache Tika project",
					URI.create("https://tika.apache.org")),
			new AboutBean(
					"logo-github.png", 
					"<html><a href=\"#\">ExifTool - Enhanced Java Integration for Phil Harvey's ExifTool by Mickael Jeanroy</a></html>",
					"ExifTool - Enhanced Java Integration for Phil Harvey's ExifTool by Mickael Jeanroy",
					URI.create("https://github.com/mjeanroy/exiftool")),
			new AboutBean(
					"logo-github.png", 
					"<html><a href=\"#\">vlcj-info by caprica</a></html>",
					"vlcj-info by caprica",
					URI.create("https://github.com/caprica/vlcj-info")),
			new AboutBean(
					"logo-7zipjbind.png", 
					"<html><a href=\"#\">7-Zip-JBinding</a></html>", 
					"7-Zip-JBinding",
					URI.create("https://sevenzipjbind.sourceforge.net/")),
			new AboutBean(
					"logo-jfree.png", 
					"<html><a href=\"#\">JFreeChart</a></html>",
					"JFreeChart",
					URI.create("https://www.jfree.org/jfreechart/")),
			
	};
	
	private static final String TRADEMARKS = 
			"<html>" +
			"<p>Oracle and Java are trademarks or registered trademarks of Oracle and/or its affiliates.</p>" +
			"<p></p>" +
			"<p>Other names may be trademarks of their respective owners.</p>" +
			"</html>";
	
	private final JLabel acknowledgement;
	private final JList<AboutBean> thanks;
	private final JLabel trademarks;
	private final JList<AboutBean> attributions;
	private final Box poweredBy;
	
	private static final AboutBean[] POWERED_BY = {
			new AboutBean(
					"logo-exiftool.png", 
					"<html><a href=\"#\">ExifTool by Phil Harvey</a></html>",
					"ExifTool by Phil Harvey",
					URI.create("https://exiftool.org")),
			new AboutBean(
					"logo-tika.png", 
					"<html><a href=\"#\">Apache Tika</a></html>",
					"Apache Tika project",
					URI.create("https://tika.apache.org")),
			new AboutBean(
					"logo-mediainfo.png", 
					"<html><a href=\"#\">MediaInfo</a></html>",
					"MediaInfo by MediaArea",
					URI.create("https://mediaarea.net/MediaInfo")),
			new AboutBean(
					"logo-7zip.png", 
					"<html><a href=\"#\">7-Zip by Igor Pavlov</a></html>",
					"7-Zip by Igor Pavlov",
					URI.create("https://www.7-zip.org")),
	};
	
	public TabAbout() {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(R.style.BORDER_EMPTY_8);
		
		MouseListener mouseClickHandler = new MouseAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void mouseClicked(MouseEvent e) {
				JList<AboutBean> list = (JList<AboutBean>) e.getSource();
				AboutBean value = list.getSelectedValue();
				
				if(Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(value.getHref());
					} catch (IOException e1) {
						LOG.error(e1.toString());
					}
				}
			}
		};
		
		Box acknowledgementBox = Box.createHorizontalBox();
		acknowledgement = new JLabel(ACKNOWLEDGMENT);
		acknowledgementBox.add(acknowledgement);
		acknowledgementBox.add(Box.createHorizontalGlue());
		
		Box thanksBox = Box.createHorizontalBox();
		thanks = new JList<AboutBean>(THANKS);
		thanks.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		thanks.addMouseListener(mouseClickHandler);
		thanks.setCellRenderer(new AttributionRenderer());
		thanks.setOpaque(false);
		thanksBox.add(thanks);
		thanksBox.add(Box.createHorizontalGlue());
		
		Box trademarksBox = Box.createHorizontalBox();
		trademarks = new JLabel(TRADEMARKS);
		trademarksBox.add(trademarks);
		trademarksBox.add(Box.createHorizontalGlue());
		
		Box attributionsBox = Box.createHorizontalBox();
		attributions = new JList<AboutBean>(ATTRIBUTIONS);
		attributions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		attributions.addMouseListener(mouseClickHandler);
		
		attributions.setCellRenderer(new AttributionRenderer());
		attributions.setOpaque(false);
		
		attributionsBox.add(attributions);
		attributionsBox.add(Box.createHorizontalGlue());
		
		poweredBy = Box.createHorizontalBox();
		
		for(AboutBean a : POWERED_BY) {
			Icon icon = ResourceLoader.getIcon(a.getIcon());
			JLabel l = new JLabel(icon);
			l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			l.setBorder(R.style.BORDER_EMPTY_8);
			l.setToolTipText(a.getTooltip());
			l.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(Desktop.isDesktopSupported()) {
						Desktop desktop = Desktop.getDesktop();
						try {
							desktop.browse(a.getHref());
						} catch (IOException e1) {
							LOG.error(e1.toString());
						}
					}
				}
				
			});
			
			poweredBy.add(l);
		}
		
		add(Box.createVerticalStrut(R.integer.BORDER_8));
		add(acknowledgementBox);
		
		add(Box.createVerticalStrut(R.integer.BORDER_8));
		add(poweredBy);
		
		add(Box.createVerticalStrut(R.integer.BORDER_8));
		add(thanksBox);
		
		add(Box.createVerticalStrut(R.integer.BORDER_8));
		add(attributionsBox);
		
		add(Box.createVerticalStrut(R.integer.BORDER_8));
		add(trademarksBox);
		
		add(Box.createVerticalGlue());
	}

	public JLabel getAcknowledgement() {
		return acknowledgement;
	}

	public JList<AboutBean> getAttributions() {
		return attributions;
	}	
}
