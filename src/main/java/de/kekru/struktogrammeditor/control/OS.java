package de.kekru.struktogrammeditor.control;

public class OS {

	public static boolean windows = false, linux = false, mac = false;
	
	static {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			windows = true;
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			mac = true;
		} else {
			linux = true;
		}
	}
}
