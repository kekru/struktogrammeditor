package de.kekru.struktogrammeditor.other;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Helpers {

	public static final void openWebsite(String uri) {
		try {
			Desktop.getDesktop().browse(new URI(uri));
		} catch (IOException e) {
			throw new IllegalStateException("failed to open uri " + uri, e);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("failed to open uri " + uri, e);
		}
	}

	public static List<String> readTextFile(File file) {
		//BufferedReader reader = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			
			String line;
			List<String> lines = new ArrayList<>();

			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}

			return lines;
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("could not read file " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new IllegalStateException("could not read file " + file.getAbsolutePath(), e);
		}
	}
	
	public static Icon getIcon(String resourcePath) {
		 return new ImageIcon(Helpers.class.getResource(resourcePath));
	}
	
	public static Document getSAXParsedDocument(File file) {
	    SAXBuilder builder = new SAXBuilder(); 
	    Document document = null;
	    try {
	        document = builder.build(file);
	    } 
	    catch (JDOMException | IOException e) {
	        e.printStackTrace();
	    }
	    return document;
	}

}
