package de.kekru.struktogrammeditor.struktogrammelemente;

import java.awt.Graphics2D;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;
import de.kekru.struktogrammeditor.other.SupportedLanguages;

public class WhileSchleife extends Schleife { // erbt von Schleife

	public WhileSchleife(Graphics2D g) {
		super(g);
		setzeText(GlobalSettings.gibElementBeschriftung(AnweisungsTyp.WhileSchleife));
	}

	@Override
	public boolean neuesElementMussOberhalbPlatziertWerden(int y) {
		return y < gibY() + getObererRand() / 2;// hier wird anhand der Position der Maus im Kopfteil entschieden, weil
												// man beim ganzer Betrachtung nicht unterhalb einfügen kann
	}

	@Override // siehe DoUntilSchleife
	public void quellcodeGenerieren(SupportedLanguages typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar,
			JTextAreaEasy textarea) {
		String vorher = "";
		String nachher = "";

		switch (typ) {
		case Java:
			vorher = "while (" + co("kommentar") + co("text") + co("kommentarzu") + ") {\n";
			nachher = "}\n";
			break;

		case Delphi:
			vorher = "while " + co("kommentar") + co("text") + co("kommentarzu") + " do \n"
					+ einruecken("begin", anzahlEingerueckt) + "\n";
			nachher = "end;\n";
			break;
		case PHP:
			vorher = "while (" + co("kommentar") + co("text") + co("kommentarzu") + ") {\n";
			nachher = "}\n";
			break;
		}

		textarea.hinzufuegen(wandleZuAusgabe(vorher, typ, anzahlEingerueckt, alsKommentar));
		liste.quellcodeAllerUnterelementeGenerieren(typ, anzahlEingerueckt + anzahlEinzuruecken, anzahlEinzuruecken,
				alsKommentar, textarea);
		textarea.hinzufuegen(wandleZuAusgabe(nachher, typ, anzahlEingerueckt, alsKommentar));
	}

	@Override
	public void setzeFaelle(String[] faelle) {}

}