package de.kekru.struktogrammeditor.control;


public class Main {

	public static void main(String[] args) {

		System.setProperty("com.apple.mrj.application.apple.menu.about.name", GlobalSettings.guiTitel);
		GlobalSettings.init();
		new Controlling(args);
	}
}
