package gyk4j.wreck.view.preview.body.settings;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gyk4j.wreck.resources.SourceEnum;

public class SettingsSource extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JCheckBox[] sources;
	private final SettingsSourceCustomDateTime customDateTime;
	
	public SettingsSource() {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createTitledBorder("Timestamp Sources"));
		
		customDateTime = new SettingsSourceCustomDateTime();
		customDateTime.setOpaque(false);
		
		sources = new JCheckBox[SourceEnum.values().length];
		int i = 0;
		for(SourceEnum action: SourceEnum.values()) {
			JCheckBox option = new JCheckBox();
//			String text = "<html><p><strong>"
//					.concat(action.getName())
//					.concat("</strong><br/> ")
//					.concat(action.getDescription())
//					.concat("</p></html>");
			
			option.setOpaque(false);
			option.setHorizontalAlignment(SwingConstants.LEADING);
			option.setHorizontalTextPosition(SwingConstants.TRAILING);
			option.setVerticalAlignment(SwingConstants.TOP);
			option.setVerticalTextPosition(SwingConstants.TOP);
			option.setText(action.getName());
			option.setName(action.toString());
			option.setToolTipText(action.getDescription());

			Box b = Box.createHorizontalBox();
			b.add(option);
//			if(action == RepairActionEnum.CUSTOM)
//				b.add(customDateTime);

			b.add(Box.createHorizontalGlue());
			add(b);
			
			sources[i] = option;
			i++;
		}
		add(customDateTime);
	}
	
	public JCheckBox[] getSources() {
		return sources;
	}

	public SettingsSourceCustomDateTime getCustomDateTime() {
		return customDateTime;
	}
}
