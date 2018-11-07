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
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

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

	public static String[] readTextFile(String path) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
			String line;
			List<String> lines = new LinkedList<String>();

			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}

			return lines.toArray(new String[lines.size()]);

		} catch (FileNotFoundException e) {
			throw new IllegalStateException("could not read file " + path, e);
			
		} catch (IOException e) {
			throw new IllegalStateException("could not read file " + path, e);
			
		} finally {
			
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new IllegalStateException("failed to close reader", e);
				}
			}
		}

	}
	
	public static Icon getIcon(String resourcePath) {
		 return new ImageIcon(Helpers.class.getResource(resourcePath));
	}

}
