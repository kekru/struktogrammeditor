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
		
		this.controlling = controlling;

		final Application application = Application.getApplication(); 
		application.setAboutHandler(this); 
		//application.setPreferencesHandler(this);
		application.setDockIconImage(new ImageIcon(getClass().getResource(GlobalSettings.logoName)).getImage());
	}

	@Override
	public void handleQuitRequestWith(QuitEvent quitEvent, QuitResponse response) {
		controlling.programmBeendenGeklickt(); //When this method returns, then the user clicked on cancel
		response.cancelQuit();
	}

	@Override
	public void handleAbout(AboutEvent arg0) {
		controlling.showInfo();		
	}
}
