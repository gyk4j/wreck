package gyk4j.wreck.view.preview.body.settings;

import java.awt.Component;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gyk4j.wreck.resources.CorrectionEnum;

public class SettingsCorrections extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final Map<CorrectionEnum, JCheckBox>  corrections;
	

	public SettingsCorrections() {
		super();
		
		corrections = new EnumMap<>(CorrectionEnum.class);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		setBorder(BorderFactory.createTitledBorder("Correction Targets"));
		
		for(CorrectionEnum c : CorrectionEnum.values()) {
			JCheckBox cb = createCheckBox(c.getName(), c.getDescription());
			corrections.put(c, cb);
			add(createBox(cb, SwingConstants.LEFT));
		}
	}
	
	private JCheckBox createCheckBox(String text, String tooltip) {
//		String html = "<html><p><strong>"
//				.concat(text)
//				.concat("</strong><br/>")
//				.concat(tooltip)
//				.concat("</p></html>");
		
		JCheckBox checkbox = new JCheckBox(text);
		checkbox.setOpaque(false);
		checkbox.setHorizontalAlignment(SwingConstants.LEADING);
		checkbox.setHorizontalTextPosition(SwingConstants.TRAILING);
		checkbox.setVerticalAlignment(SwingConstants.TOP);
		checkbox.setVerticalTextPosition(SwingConstants.TOP);
		checkbox.setToolTipText(tooltip);
		return checkbox;
	}
	
	private Box createBox(Component c, int align) {
		Box b = Box.createHorizontalBox();
		if(align == SwingConstants.CENTER ||
				align == SwingConstants.RIGHT || 
				align == SwingConstants.TRAILING)
			b.add(Box.createHorizontalGlue());
		
		b.add(c);
		
		if(align == SwingConstants.CENTER ||
				align == SwingConstants.LEFT || 
				align == SwingConstants.LEADING)
			b.add(Box.createHorizontalGlue());
		return b;
	}
	
	public Map<CorrectionEnum, JCheckBox> getCorrections() {
		return corrections;
	}
}
