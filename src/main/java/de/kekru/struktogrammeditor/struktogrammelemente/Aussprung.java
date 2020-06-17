package de.kekru.struktogrammeditor.struktogrammelemente;

import java.awt.Graphics2D;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;
import de.kekru.struktogrammeditor.other.SupportedLanguages;

public class Aussprung extends Anweisung {

	public Aussprung(Graphics2D g) {
		super(g);

		setzeText(GlobalSettings.gibElementBeschriftung(AnweisungsTyp.Aussprung));
	}

	@Override
	public void zeichne() {
		super.zeichne();

		g.drawLine(gibX() + 10, gibY(), gibX(), gibY() + gibHoehe() / 2);
		g.drawLine(gibX() + 10, gibY() + gibHoehe(), gibX(), gibY() + gibHoehe() / 2);
	}

	@Override
	public void quellcodeGenerieren(SupportedLanguages typ, int anzahlEingerueckt, int anzahlEinzuruecken,
			boolean alsKommentar, JTextAreaEasy textarea) {
		String s = "";

		switch (typ) {
		case Java:
			s = co("kommentar") + "break;/return; " + co("text") + co("kommentarzu");
			break;

		case Delphi:
			s = co("kommentar") + "break;/exit; " + co("text") + co("kommentarzu");
			break;
		case PHP:
			s = co("kommentar") + "break;/return; " + co("text") + co("kommentarzu");
			break;
		}

		textarea.hinzufuegen(wandleZuAusgabe(s, typ, anzahlEingerueckt, alsKommentar) + "\n");
	}

}