package de.kekru.struktogrammeditor.control;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import de.kekru.struktogrammeditor.other.Helpers;
import de.kekru.struktogrammeditor.struktogrammelemente.AnweisungsTyp;
import de.kekru.struktogrammeditor.struktogrammelemente.Fallauswahl;
import de.kekru.struktogrammeditor.struktogrammelemente.Schleife;
import de.kekru.struktogrammeditor.struktogrammelemente.StruktogrammElement;
import de.kekru.struktogrammeditor.struktogrammelemente.StruktogrammElementListe;

//XML erstellen: http://www.ibm.com/developerworks/java/library/j-jdom/
//XML auslesen: http://www.javabeginners.de/XML/XML-Datei_lesen.php

public class XMLLeser {
	private Struktogramm struktogramm;

	/**
	 * Just why?
	 */
	public XMLLeser() {

	}

	/**
	 * Loads a Struktogram from File "pfad"
	 * @param pfad
	 * @param struktogramm
	 */
	public void ladeXLM(File pfad, Struktogramm struktogramm) throws RuntimeException {
		this.ladeXML(pfad, null, struktogramm);
	}

	/**
	 * Loads a Struktogram from Document
	 * @param pfad
	 * @param struktogramm
	 */
	public void ladeXLM(Document document, Struktogramm struktogramm) throws RuntimeException {
		this.ladeXML(null, document, struktogramm);
	}


	//Rekursive Erstellung der StruktogrammElemente (Rekursiver Aufruf dieser Methode passiert dabei erst in listenelementeErstellen(...))
	//http://www.jdom.org/docs/oracle/jdom-part1.pdf
	//http://www.jdom.org/docs/apidocs/org/jdom/Element.html
	private void erstelleElementeRek(Element elem, StruktogrammElement tmp){//elem ist der strelem-Tag zu tmp
		List<?> alleTextzeilen = (List<?>)elem.getChildren("text"); //alle text-Tags auslesen
		String[] textzeilen = new String[alleTextzeilen.size()];

		for(int i=0; i < alleTextzeilen.size(); i++){
			//eine Textzeile besteht aus Zahlen mit ";" getrennt, eine Zahl steht für das entsprechende Unicode-Zeichen
			textzeilen[i] = decodeS(((Element)alleTextzeilen.get(i)).getText());//Zahlen wieder in Zeichen umwandeln
		}

		tmp.setzeText(textzeilen); //alle Textzeilen im StruktogrammElement speichern



		if (tmp instanceof Schleife){ //wenn das StruktogrammElement eine Schleife ist, müssen ihre Unterelemente generiert werden
			listenelementeErstellen(elem.getChild("schleifeninhalt"),((Schleife)tmp).gibListe()); //Unterelemente sind im schleifeninhalt-Tag

		} else if(tmp instanceof Fallauswahl) { //Fallauswahl oder Verzweigung

			List<?> alleFaelle = (List<?>) elem.getChildren("fall");

			Fallauswahl fallauswahl = ((Fallauswahl)tmp);
			fallauswahl.erstelleNeueListen(alleFaelle.size());//so viele Fall-Listen, wie fall-Tags vorhanden sind, werden erstellt

			for (int i=0; i < alleFaelle.size(); i++){
				fallauswahl.gibListe(i).setzeBeschreibung(decodeS(((Element)alleFaelle.get(i)).getAttributeValue("fallname"))); //Beschreibung wird aus dem fall-Tag gelesen und in der Liste gesetzt
				listenelementeErstellen((Element)alleFaelle.get(i), fallauswahl.gibListe(i)); //die Liste i der Fallauswahl erhält den entsprechenden fall-Tag und erstellt ihre Unterelemente
			}
		}

	}



	/*erstellt aus einem Element ein StruktogrammElement mit Unterelementen und gibt es zurück,
     wird gebraucht für die Kopier-Funktion*/
	private StruktogrammElementListe wurzelStruktogrammElementErstellen(Element elem){
		StruktogrammElementListe liste = new StruktogrammElementListe(null);
		
		listenelementeErstellen(elem, liste);
		return liste;
	}


	private void listenelementeErstellen(Element elem, StruktogrammElementListe liste){
		List<?> alleUnterelemente = (List<?>)elem.getChildren("strelem"); //alle strelem-Tags ermitteln
		AnweisungsTyp typ;
		Element tmp;

		for(int i=0; i < alleUnterelemente.size(); i++){
			tmp = (Element)alleUnterelemente.get(i);
			typ = AnweisungsTyp.getByNumber(Integer.parseInt(tmp.getAttributeValue("typ")));
			StruktogrammElement neues = struktogramm.neuesStruktogrammElement(typ); //neues StruktogrammElement anhand des gespeicherten Typs erzeugen...

			setAttribute(neues,tmp);

			liste.hinzufuegen(neues); //...und der übergebenen Liste anhängen

			erstelleElementeRek((Element)alleUnterelemente.get(i),neues); //Text und Unterelemente des StruktogrammElementes setzen, entsprechender strelem-Tag wird übergeben
		}
	}


	private void setAttribute(StruktogrammElement struktogrammelement, Element zugehoerigesKopfelement){
		String s = zugehoerigesKopfelement.getAttributeValue("zx");	   
		if (s != null) {
			struktogrammelement.setXVergroesserung(Integer.parseInt(s));
		}

		s = zugehoerigesKopfelement.getAttributeValue("zy");	   
		if (s != null) {
			struktogrammelement.setYVergroesserung(Integer.parseInt(s));
		}
		
		s = zugehoerigesKopfelement.getAttributeValue("textcolor");	   
		if (s != null) {
			struktogrammelement.setFarbeSchrift(Color.decode(s));
		}
		
		s = zugehoerigesKopfelement.getAttributeValue("bgcolor");	   
		if (s != null) {
			struktogrammelement.setFarbeHintergrund(Color.decode(s));
		}
	}


	private void ladeXML(File file, Document document, Struktogramm struktogramm) throws RuntimeException {

		Document doc = null;

		if (document != null) {
			doc = document;
		} else {
			//Das Document erstellen, aus einer Datei
			doc = Helpers.getSAXParsedDocument(file);
		}
		
		Element element = null;
		
		element = doc.getRootElement(); //WurzelElement ermitteln
		
		this.struktogramm = struktogramm;

		//Schriftart für dieses Struktgramm einlesen
		String fontFamily, fontSize, fontStyle;
		fontFamily = element.getAttributeValue("fontfamily");
		fontSize = element.getAttributeValue("fontsize");
		fontStyle = element.getAttributeValue("fontstyle");
		
		String struktogrammBeschreibung = element.getAttributeValue("caption");

		if(fontFamily != null && fontSize != null && fontStyle != null){				
			struktogramm.setFont(new Font(decodeS(fontFamily),Integer.parseInt(fontStyle),Integer.parseInt(fontSize)));
		}

		if(struktogrammBeschreibung != null){
			struktogramm.setStruktogrammBeschreibung(decodeS(struktogrammBeschreibung));
		}

		struktogramm.gibListe().alleEntfernen(); //Liste des Struktogramms leeren
		listenelementeErstellen(element,struktogramm.gibListe()); //Struktogramm mit neuen Elementen füllen, Ausgangspunkt ist das Wurzelelement der XML-Datei


		if(struktogramm.gibGraphics() != null){
			struktogramm.zeichenbereichAktualisieren();
			struktogramm.zeichne();
		}
	}



	//Für Kopier-Funktion: erstellt aus dem Document ein StruktogrammElement mit Unterelementen
	public StruktogrammElementListe erstelleStruktogrammElementListe(Document document, Struktogramm struktogramm){

		if(document != null){
			this.struktogramm = struktogramm;
			return wurzelStruktogrammElementErstellen(document.getRootElement());
		} 
		return null;
	}



	//wandelt die Zeichen eines Strings in entsprechende Unicode-Zahlen um, mit ";" getrennt, weil es beim Laden von XML-Dateien mit Sonderzeichen und Umlauten zu Problemen kommt
	public static String encodeS(String s){
		String ausgabe = "";
		if (s.equals("")) {
			ausgabe = "-1;"; //-1 markiert eine leere Zeile
		} else {
			for (int i=0; i < s.length(); i++){
				ausgabe += (int)s.charAt(i) +";";
			}
		}
		return ausgabe;
	}



	//Zahlen wieder in Zeichen umwandeln
	private static String decodeS(String codiert) {
		String[] textzeileAlsZahlen = codiert.split(";"); //an allen ; splitten
		String s = "";
		int zeichenNummer;

		for (int i=0; i < textzeileAlsZahlen.length; i++){
			zeichenNummer = Integer.parseInt(textzeileAlsZahlen[i]);//einzelne Arrayeinträge beinhalten die Unicode-Zahlen

			if (zeichenNummer == -1){
				s = ""; //zeichenNummer ist -1, also eine leere Zeile -> s wird zum leeren String
			} else {
				s += (char)zeichenNummer;//Zahl wird zu Unicode-Zeichen umgewandelt
			}
		}
		return s;
	}
}