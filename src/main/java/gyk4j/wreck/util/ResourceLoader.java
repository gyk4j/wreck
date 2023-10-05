package gyk4j.wreck.util;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLoader {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceLoader.class);
	
	public static Icon getIcon(String path) {
		ImageIcon imageIcon = null;
		URL url = ClassLoader.getSystemResource("res/icon/".concat(path));
		
		if(url != null)
			imageIcon = new ImageIcon(url);
		else 
			LOG.error("Icon not found: {}", path);
		
		return imageIcon;
	}
}
