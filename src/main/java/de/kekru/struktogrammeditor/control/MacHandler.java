package de.kekru.struktogrammeditor.control;

import javax.swing.ImageIcon;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent.AboutEvent;
import com.apple.eawt.AppEvent.QuitEvent;
import com.apple.eawt.Application;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;

public class MacHandler implements AboutHandler, QuitHandler {

	private final Controlling controlling;

	public MacHandler(Controlling controlling){
		
		System.getProperties().put("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", GlobalSettings.guiTitel);
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
		
		this.controlling = controlling;

		final Application application = Application.getApplication(); 
		application.setAboutHandler(this); 
		//application.setPreferencesHandler(this);
		application.setDockIconImage(new ImageIcon(getClass().getResource(GlobalSettings.logoName)).getImage());
	}

	@Override
	public void handleQuitRequestWith(QuitEvent quitEvent, QuitResponse response) {
		if(!controlling.programmBeendenGeklickt()){
			response.cancelQuit();
		}
	}

	@Override
	public void handleAbout(AboutEvent arg0) {
		controlling.showInfo();		
	}

}
