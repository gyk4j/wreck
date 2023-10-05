package gyk4j.wreck.controller.action.statistics;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import gyk4j.wreck.util.ResourceLoader;
import gyk4j.wreck.view.preview.body.statistics.PreviewStatisticsGraphStack;

public class RawTableToggleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private final PreviewStatisticsGraphStack stack;
	
	public RawTableToggleAction(PreviewStatisticsGraphStack stack) {
		super(null, ResourceLoader.getIcon("table.png"));
		this.putValue(Action.SHORT_DESCRIPTION, "Show table");
		this.putValue(Action.LONG_DESCRIPTION, "Show table");
		this.stack = stack;
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		CardLayout cl = (CardLayout) stack.getLayout();
		cl.show(stack, PreviewStatisticsGraphStack.CARD_TEXT);
	}

}
