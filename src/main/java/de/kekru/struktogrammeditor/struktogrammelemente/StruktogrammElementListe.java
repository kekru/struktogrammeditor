package de.kekru.struktogrammeditor.struktogrammelemente;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.jdom.Element;

import de.kekru.struktogrammeditor.other.JTextAreaEasy;



public class StruktogrammElementListe extends ArrayList<StruktogrammElement> {//erbt von generischer ArrayList

	private static final long serialVersionUID = -122818269830027765L;
	private Rectangle bereich;
	private Graphics2D g;
	private String beschreibung = ""; //hier wird bei einer Fallauswahl und bei einer Verzweigung gespeichert, was über der jeweiligen Spalte stehen soll

	public StruktogrammElementListe(Graphics2D g){
		super();
		bereich = new Rectangle();
		this.g = g;

		add(new LeerElement(g)); //Am Anfang kommt ein Leerelement in die Liste
	}



	public void setzeBeschreibung(String beschr){
		beschreibung = beschr;
	}

	public String gibBeschreibung(){
		return beschreibung;
	}


	public int gibAnzahlUnterelemente(){
		return size();
	}



	public void quellcodeAllerUnterelementeGenerieren(int typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar, JTextAreaEasy textarea){
		for (int i=0; i < size(); i++){
			get(i).quellcodeGenerieren(typ, anzahlEingerueckt, anzahlEinzuruecken, alsKommentar, textarea);
		}
	}



	public void schreibeXMLDatenAllerUnterElemente(Element parent){
		//if (!(get(0) instanceof LeerElement)){//alle Struktogrammelemente, außer LeerElement, müssen XML-Daten schreiben
		for (int i=0; i < size(); i++){
			get(i).schreibeXMLDaten(parent);
		}
		//}
	}




	private int gibTextbreite(String s){
		return g != null ? (int) g.getFontMetrics().getStringBounds(s, g).getBounds().getWidth() : s.length() * 4;//http://www.tutorials.de/java/288641-textlaenge-pixel.html
	}



	public void alleZeichnen(){
		for (int i=0; i < size(); i++){
			get(i).zeichne();
		}
	}


	public void graphicsAllerUnterlementeSetzen(Graphics2D g){
		this.g = g;

		for (int i=0; i < size(); i++){
			get(i).setzeGraphics(g);
		}
	}


	public boolean istUnterelement(StruktogrammElement eventuellesUnterelement){

		for (int i=0; i < size(); i++){
			if((get(i) == eventuellesUnterelement) || (get(i).istUnterelement(eventuellesUnterelement))){//wenn get(i) das Gesuchte ist, oder wenn get(i) das gesuchte Unterelement beinhaltet, wird true zurückgegeben
				return true;
			}
		}

		return false;
	}



	public void hinzufuegen(StruktogrammElement neues){//am Ende einfügen
		hinzufuegen(neues,null,true);
	}


	public void hinzufuegen(StruktogrammElement neues, StruktogrammElement naechstesOderVorheriges, boolean vorDemAltenEinfuegen){
		ArrayList<StruktogrammElement> list = new ArrayList<StruktogrammElement>();
		list.add(neues);
		hinzufuegen(list, naechstesOderVorheriges, vorDemAltenEinfuegen);
	}

	public void hinzufuegen(ArrayList<StruktogrammElement> neue, StruktogrammElement naechstesOderVorheriges, boolean vorDemAltenEinfuegen){
		Object leeres = null;

		if (!isEmpty() && (get(0) instanceof LeerElement)){
			leeres = get(0); //das LeerElement merken
		}




		if (naechstesOderVorheriges != null){

			int position = indexOf(naechstesOderVorheriges); //Position des Elementes hinter oder vor dem das Neue eingefügt werden soll

			if (!vorDemAltenEinfuegen){//wenn nach dem alten Element eingefügt werden soll, wird die Position um 1 inkrementiert
				position++;
			}

			for(StruktogrammElement strElem : neue){
				add(position, strElem);//Einfügen an der Stelle position; das Element, das vorher an dieser Position war, wird um eine Stelle weiter geschoben
				position++;
			}

		}else{

			for(StruktogrammElement strElem : neue){ 
				add(strElem); //Am Ende einfügen
			}
		}




		if (leeres != null){
			remove(indexOf(leeres)); //das LeerElement entfernen
		}
	}




	public void entfernen(StruktogrammElement zuLoeschen){
		remove(indexOf(zuLoeschen));

		if (size() == 0){
			add(new LeerElement(g)); //wenn die Liste jetzt leer ist, ein LeerElement einfügen
		}
	}


	public void alleEntfernen(){
		clear();
		add(new LeerElement(g));
	}




	public Object gibElementAnPos(int x, int y, boolean nurListe){
		if (bereich.contains(x,y)){
			//Punkt ist innerhalb dieser Liste

			Object tmp;

			for (int i=0; i < size(); i++){
				tmp = get(i).gibElementAnPos(x,y,nurListe);
				if (tmp != null){
					return tmp; //das Listen-Element mit dem Index i beinhaltet den Punkt, dieses oder eines seiner Unterelemente wird zurückgegeben
				}
			}


			if (nurListe){ //es wurde noch kein Element zurückgegeben (auch keine weiter innere Liste), wenn also nach einer Liste gefragt ist...
				return this; //...wird diese zurückgeliefert
			}

		}

		return null; //Punkt ist nicht innerhalb dieser Liste
	}



	public StruktogrammElementListe gibListeDieDasElementHat(StruktogrammElement element){
		StruktogrammElementListe tmp;

		for (int i=0; i < size(); i++){
			if(get(i) == element){
				return this; //ich habe das Element

			}else{

				tmp = get(i).gibListeDieDasElementHat(element);
				if (tmp != null){
					return tmp; //eine der Listen meiner Unterelemente hat das Element
				}
			}
		}

		return null; //ich und auch keine meiner Unterelemente hat das gesucht Element
	}



	//gibt die Breite des breitesten Unterelementes und die addierte Höhe aller Unterelemente an
	public Dimension gibDimensionDerUnterelemente(){
		int breite = 0;
		int hoehe = 0;
		Rectangle rect;

		for (int i=0; i < size(); i++){
			rect = get(i).gibRectangle();

			if (rect.width > breite){
				breite = rect.width;
			}

			hoehe += rect.height;
		}



		return new Dimension(breite, hoehe);
	}



	public void xPosAllerUnterelementeSetzen(int x){
		for (int i=0; i < size(); i++){
			get(i).setzeXPos(x);
		}

		bereich.x = x;
	}


	public void breiteDerUnterelementeSetzen(int neueBreite){
		for (int i=0; i < size(); i++){
			get(i).setzeBreite(neueBreite);
		}

		bereich.width = neueBreite;
	}



	public void gesamtHoeheSetzen(int neueHoehe){//Höhe des letzten Elementes wird verändert
		if(size() > 0){
			int hoeheVorher = get(size() -1).gibHoehe(); //Höhe des letzten Elementes ermitteln

			get(size() -1).setzeHoehe(neueHoehe - (bereich.height - hoeheVorher)); //neue Höhe des letzten Elementes setzen
		}

		bereich.height = neueHoehe;
	}




	public Rectangle zeichenbereichAllerElementeAktualisieren(int x, int y){
		int neueYPos = y;

		for (int i = 0; i < size(); i++){
			neueYPos += get(i).zeichenbereichAktualisieren(x,neueYPos).getHeight();//Zeichenbereich von Element get(i) wird aktualisiert mit den Koordinaten x und neueYPos; neueYPos wird dabei für das nächste Element um die Höhe des aktuellen Elementes vergrößert
		}

		Dimension dim = gibDimensionDerUnterelemente();//größte Breite eines Unterlementes und Gesamthöhe aller Unterelemente

		if (dim.width < gibTextbreite(gibBeschreibung() +8)){//Beschreibung ist bei Verzweigung und Fallauswahl kein leerer String (wird gesetzt in setzeFaelle(...) durch den EingabeDialog)
			dim.width = gibTextbreite(gibBeschreibung()) +8;
		}

		breiteDerUnterelementeSetzen(dim.width);//alle Unterelemente auf die gleiche Breite bringen

		bereich.setSize(dim);
		bereich.setLocation(x,y);

		return bereich;
	}


	public int gibBreite(){
		return bereich.width;
	}

	public int gibHoehe(){
		return bereich.height;
	}

	public int gibRechterRand(){
		return bereich.x + bereich.width;
	}

	public int gibX(){
		return bereich.x;
	}

	public void zoomsAllerElementeZuruecksetzen(){
		for(int i=0; i < size(); i++){
			get(i).zoomsZuruecksetzen();
		}
	}

}