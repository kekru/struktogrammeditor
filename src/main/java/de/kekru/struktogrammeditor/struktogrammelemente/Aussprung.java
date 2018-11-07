package de.kekru.struktogrammeditor.struktogrammelemente;

import java.awt.Graphics2D;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.control.Struktogramm;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;
import de.kekru.struktogrammeditor.view.CodeErzeuger;

public class Aussprung extends Anweisung {

	public Aussprung(Graphics2D g){
		super(g);

		setzeText(GlobalSettings.gibElementBeschriftung(Struktogramm.typAussprung));
	}


	@Override
	public void zeichne(){
		super.zeichne();

		g.drawLine(gibX() +10,gibY(),gibX(),gibY()+gibHoehe()/2);
		g.drawLine(gibX() +10,gibY()+gibHoehe(),gibX(),gibY()+gibHoehe()/2);
	}



	@Override
	public void quellcodeGenerieren(int typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar, JTextAreaEasy textarea){
		String s = "";

		switch(typ){
		case CodeErzeuger.typJava:
			s = co("kommentar")+"break;/return; "+co("text")+co("kommentarzu");
			break;

		case CodeErzeuger.typDelphi:
			s = co("kommentar")+"break;/exit; "+co("text")+co("kommentarzu");
			break;
		}

		textarea.hinzufuegen(wandleZuAusgabe(s, typ,anzahlEingerueckt,alsKommentar)+"\n");
	}

}