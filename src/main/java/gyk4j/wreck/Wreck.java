package gyk4j.wreck;

import java.awt.EventQueue;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.controller.PreviewController;
import gyk4j.wreck.model.PreviewModel;
import gyk4j.wreck.view.preview.PreviewView;

public class Wreck {
	private static final Logger LOGGER = LoggerFactory.getLogger(Wreck.class);
	
	private final PreviewModel model;
	private final PreviewView view;
	private final PreviewController controller;
	
	public Wreck() {
		super();
		
		this.model = new PreviewModel();
		this.view = new PreviewView();
		this.controller = new PreviewController(model, view);
	}

	public static void main(String[] args) {		
		if(args.length == 1) {
			Wreck app = new Wreck();
			app.getController().start(args[0]);
		}
		else {
			try {
				Attributes attr = getManifest();
				String title = attr.getValue("Specification-Title");
				String version = attr.getValue("Implementation-Version");
				String jar = String.format("%s-%s.jar", title, version);
				LOGGER.error("java -jar {} <path>", jar);
				
				EventQueue.invokeAndWait(
						() -> JOptionPane.showMessageDialog(
								null, 
								String.format("java -jar %s <path>", jar), 
								"Error", 
								JOptionPane.ERROR_MESSAGE)
						);
			} catch (InvocationTargetException e) {
				LOGGER.error(e.toString());
			} catch (InterruptedException e) {
				LOGGER.error(e.toString());
			} catch (IOException e) {
				LOGGER.error(e.toString());
			}
		}
	}
	
	private static Attributes getManifest() throws IOException {
        URLClassLoader cl = (URLClassLoader) Wreck.class.getClassLoader();
        URL url = cl.findResource("META-INF/MANIFEST.MF");
        Manifest manifest = new Manifest(url.openStream());
        Attributes attr = manifest.getMainAttributes();
 
        return attr;
    }

	public PreviewModel getModel() {
		return model;
	}

	public PreviewView getView() {
		return view;
	}

	public PreviewController getController() {
		return controller;
	}
}
