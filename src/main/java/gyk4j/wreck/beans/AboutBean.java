package gyk4j.wreck.beans;

import java.net.URI;

public class AboutBean {
	private final String icon;
	private final String attribution;
	private final String tooltip;
	private final URI href;
	
	public AboutBean(String icon, String attribution, String tooltip, URI href) {
		super();
		this.icon = icon;
		this.attribution = attribution;
		this.tooltip = tooltip;
		this.href = href;
	}

	public String getIcon() {
		return icon;
	}

	public String getAttribution() {
		return attribution;
	}
	
	public String getTooltip() {
		return tooltip;
	}
	
	public URI getHref() {
		return href;
	}
}
