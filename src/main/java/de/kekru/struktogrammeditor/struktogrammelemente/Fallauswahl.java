package de.kekru.struktogrammeditor.struktogrammelemente;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.jdom.Element;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.control.Struktogramm;
import de.kekru.struktogrammeditor.control.XMLLeser;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;
import de.kekru.struktogrammeditor.view.CodeErzeuger;

public class Fallauswahl extends StruktogrammElement { //erbt von StruktogrammElement
	//private Struktogramm str; //wird in hier gebraucht, um auf die Einstellung zugreifen zu können, ob die letzten Elemente bei Bedarf gestreckt werden sollen
	protected int xVerschiebungFuerTrennlinie; //legt fest, wie weit vom linken Rand der Fallauswahl die Trennlinie zwischen Vorletztem- und Sonst-Fall sein soll
	protected int yVerschiebungFuerTrennLinie; //legt fest, wie hoch die oben genannte Trennlinie sein soll
	protected ArrayList<StruktogrammElementListe> listen; //Liste von StruktogrammElementListen, für die einzelnen Fälle; als Generische ArrayList: http://www.theserverside.de/java-generics-generische-methoden-klassen-und-interfaces/

	public Fallauswahl(Graphics2D g){
		this(g,3);//anderen Konstruktor aufrufen
	}


	public Fallauswahl(Graphics2D g, int anzahlListen){
		super(g);

		//this.str = str;

		erstelleNeueListen(anzahlListen);

		yVerschiebungFuerTrennLinie = -20; //Trennlinie zwischen Vorletztem und Sonst-Fall geht 20 Pixel in den Kopfteil hinein

		listen.get(listen.size() -1).setzeBeschreibung("Sonst");

		obererRandZusatz = 40; //der Kopfteil soll 40 Pixel plus die Höhe des Textes sein

		setzeText(GlobalSettings.gibElementBeschriftung(Struktogramm.typFallauswahl));
	}





	@Override
	public void quellcodeGenerieren(int typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar, JTextAreaEasy textarea){
		String vorher = "";
		String nachher = "";
		String fall = "";
		String fallEnde = "";


		switch(typ){
		case CodeErzeuger.typJava:
			vorher = "switch("+co("kommentar")+co("text")+co("kommentarzu")+"){\n";//Kopfteil besteht aus "switch(" plus eventuellem Kommentarzeichen plus die Textzeilen plus eventuellenm Kommentar-Zu Zeichen plus "){"
			nachher = "}\n";
			break;

		case CodeErzeuger.typDelphi:
			vorher = "case "+co("kommentar")+co("text")+co("kommentarzu")+" of\n";//+einruecken("begin",anzahlEingerueckt)+"\n";
			nachher = "end;\n";
			break;
		}

		textarea.hinzufuegen(wandleZuAusgabe(vorher,typ,anzahlEingerueckt,alsKommentar)); //Kopfteil ausgeben, alles passend eingerückt und Kommentare bei Bedarf eingefügt

		//einzelnen Fälle ausgeben
		for(int i=0; i < listen.size(); i++){//Listen durchgehen
			switch(typ){
			case CodeErzeuger.typJava:
				if(i < listen.size()-1){
					fall = "case "+co("kommentar")+listen.get(i).gibBeschreibung()+co("kommentarzu")+":\n";//case und dann der Fallname
				}else{
					fall = "default: "+co("zwangkommentar")+listen.get(i).gibBeschreibung()+co("zwangkommentarzu")+"\n";//Sonsts-Fall erhält default, Beschriftung ist nicht relevant für fertigen Code, also auf jeden Fall Kommentare setzen
				}
				fallEnde = einruecken("break;\n",anzahlEinzuruecken);
				break;

			case CodeErzeuger.typDelphi:
				if(i < listen.size()-1){
					fall = co("kommentar")+listen.get(i).gibBeschreibung()+co("kommentarzu")+":\n";
				}else{
					fall = "else "+co("zwangkommentar")+listen.get(i).gibBeschreibung()+co("zwangkommentarzu")+"\n";
				}
				fall += einruecken("begin",anzahlEingerueckt+anzahlEinzuruecken)+"\n";
				fallEnde = "end;\n";

				break;
			}

			textarea.hinzufuegen(wandleZuAusgabe(fall,typ,anzahlEingerueckt+anzahlEinzuruecken,alsKommentar));//Anfang für den aktuellen Fall ausgeben
			listen.get(i).quellcodeAllerUnterelementeGenerieren(typ,anzahlEingerueckt+anzahlEinzuruecken*2,anzahlEinzuruecken,alsKommentar,textarea);//Unterelemente ausgeben
			textarea.hinzufuegen(wandleZuAusgabe(fallEnde,typ,anzahlEingerueckt+anzahlEinzuruecken,alsKommentar));//Ende für den Fall ausgeben
		}

		textarea.hinzufuegen(wandleZuAusgabe(nachher,typ,anzahlEingerueckt,alsKommentar));//Ende der Fallauswahl ausgeben

	}



	//alle Listen werden gelöscht und Neue werden erzeugt
	public void erstelleNeueListen(int anzahlListen){
		listen = new ArrayList<StruktogrammElementListe>();

		for(int i=0; i < anzahlListen; i++){
			listen.add(new StruktogrammElementListe(g));
			listen.get(i).setzeBeschreibung(""+(i+1));
		}
	}



	//neue Spalte links neben der Sonst-Spalte wird erstellt
	public void erstelleNeueSpalte(){
		int listennummer = listen.size() -1;
		listen.add(listennummer, new StruktogrammElementListe(g));

		listen.get(listennummer).setzeBeschreibung(""+(listennummer+1));
	}


	//die Spalte mit der Nummer spaltenIndex wird nach links oder nach rechts verschoben, je nach Parameter-Wert
	public void spalteVerschieben(boolean nachLinks, int spaltenIndex){
		if (nachLinks){
			if (spaltenIndex > 0)
				listenTauschen(spaltenIndex, spaltenIndex -1);
		}else{
			if (spaltenIndex <= listen.size() -2)
				listenTauschen(spaltenIndex, spaltenIndex +1);
		}
	}


	//Zwei Spalten (Listen) werden getauscht
	protected void listenTauschen(int index1, int index2){
		StruktogrammElementListe tmp = listen.get(index1);
		listen.set(index1,listen.get(index2));
		listen.set(index2,tmp);
	}


	public void entferneSpalte(int index){
		if ((index >= 0) && (index < listen.size()) && (listen.size() > 2)){//wenn mehr als 2 Spalten da sind, darf eine gelöscht werden
			listen.remove(index);
		}
	}



	//zusätzliche XML Daten sind die Daten zu den Fall-Listen
	@Override
	protected void zusaetzlicheXMLDatenSchreiben(Element aktuelles){
		Element unterelement;

		for (int i=0; i < listen.size(); i++){
			unterelement = new Element("fall").setAttribute("fallname",XMLLeser.encodeS(listen.get(i).gibBeschreibung()));//für jeden Fall wird ein neuer fall-Tag generiert mit dem Fallnamen als Attribut (codiert)

			listen.get(i).schreibeXMLDatenAllerUnterElemente(unterelement);//in den neuen fall-Tag werden die xml-Daten der Unterelemente geschrieben
			aktuelles.addContent(unterelement);//fall-Tag wird in den strelem-Tag geschrieben, der zu dieser Fallauswahl gehört
		}

	}




	//prüft, ob eventuellesUnterelement irgendwo innerhalb einer der Fall-Listen steht
	@Override
	public boolean istUnterelement(StruktogrammElement eventuellesUnterelement){
		for (int i=0; i < listen.size(); i++){//Fall-Listen durchgehen
			if(listen.get(i).istUnterelement(eventuellesUnterelement)){//einzelne Listen fragen
				return true;
			}
		}

		return false;
	}



	//gibt die Fallbeschreibungen als String-Array zurück
	@Override
	public String[] gibFaelle(){
		String[] faelle = new String[listen.size()];
		for (int i=0; i < listen.size(); i++){
			faelle[i] = listen.get(i).gibBeschreibung();
		}

		return faelle;
	}


	//setzt die Fallbeschreibungen
	@Override
	public void setzeFaelle(String[] faelle){
		for (int i=0; i < listen.size(); i++){
			listen.get(i).setzeBeschreibung(faelle[i]);
		}
	}



	@Override
	public int gibAnzahlListen(){
		return listen.size();
	}



	public StruktogrammElementListe gibListe(int index){
		return listen.get(index);
	}





	@Override
	public boolean neuesElementMussOberhalbPlatziertWerden(int y){
		return y < gibY() + getObererRand()/2;//ist die Maus im oberen Teil des Kopfteils, so soll ein neues StruktogrammElement oberhalb eingefügt werden, sonst unterhalb
	}


	@Override
	protected void setzeGraphics(Graphics2D g){
		super.setzeGraphics(g);

		for (int i=0; i < listen.size(); i++){
			listen.get(i).graphicsAllerUnterlementeSetzen(g);
		}
	}


	@Override
	public void setzeBreite(int neueBreite){//FallAuswahl wird horizontal getreckt, einzelne Fall-Listen müssen mitgestreckt werden

		int gesamtbreiteDerListen = 0;
		int neueSpaltenbreite = 0;

		for (int i=0; i < listen.size(); i++){

			listen.get(i).xPosAllerUnterelementeSetzen(gibX() + gesamtbreiteDerListen);//x-Position der Unterelemente neu setzen

			if (i <= listen.size() -2){

				//Rechnung: neueGesamtbreite / alteGesamtbreite = neueSpaltenbreite / alteSpaltenbreite //-> Verhältnisse sollen gleich bleiben
				//<=> neueSpaltenbreite = neueGesamtbreite * alteSpaltenbreite / alte Gesamtbreite
				neueSpaltenbreite = neueBreite * listen.get(i).gibBreite() / gibBreite();

				listen.get(i).breiteDerUnterelementeSetzen(neueSpaltenbreite);

			}else{//das rechteste Element
				neueSpaltenbreite = neueBreite - gesamtbreiteDerListen;

				listen.get(i).breiteDerUnterelementeSetzen(neueSpaltenbreite);
				xVerschiebungFuerTrennlinie = neueBreite - neueSpaltenbreite;
			}


			gesamtbreiteDerListen += neueSpaltenbreite;

		}


		bereich.width = neueBreite;

	}



	@Override
	public void setzeHoehe(int neueHoehe){

		for(int i=0; i < listen.size(); i++){
			listen.get(i).gesamtHoeheSetzen(neueHoehe -getObererRand());
		}

		bereich.height = neueHoehe;
	}




	@Override
	protected Object gibElementAnPos(int x, int y, boolean nurListe){
		Object tmp;

		if (bereich.contains(x,y)){ //der angegebene Punkt ist in meinem Bereich


			for (int i=0; i < listen.size(); i++){

				tmp = listen.get(i).gibElementAnPos(x,y,nurListe);
				if (objGesetzt(tmp)){
					return tmp; //eine Liste oder ein StruktogrammElement wurde gefunden, welches den Punkt enthält
				}

			}

			if (!nurListe){
				return this; //der Punkt ist nicht in einem der Fall-Listen, also, wenn nicht nur nach Listen gefragt ist, diese Fallauswahl zurückgeben
			}
		}

		return null; //der Punkt ist nicht auf dieser Fallauswahl, oder er ist auf dem Kopfteil und nurListe ist true
	}




	@Override
	public StruktogrammElementListe gibListeDieDasElementHat(StruktogrammElement element){
		StruktogrammElementListe tmp;

		for (int i=0; i < listen.size(); i++){//Fall-Listen werden gefragt, ob sie, oder deren Unterlisten, das Element haben
			tmp = listen.get(i).gibListeDieDasElementHat(element);
			if (tmp != null){
				return tmp;
			}
		}

		return null;//keine Unterliste dieser Fallauswahl hat das gesuchte Element
	}




	private int gibPassendeYKoordFuerLinie(int x){
		//herausfinden, bei welcher y-Koordinate die senkrechten Linien der Fallauswahl oben enden müssen
		//y = m * x + b
		//m = (y2 - y1)/(x2-x1)
		double m = (double)(gibY() - (gibY()+getObererRand()+yVerschiebungFuerTrennLinie)) / (double)(gibX() - (gibX() + xVerschiebungFuerTrennlinie));

		//b = y - m * x
		double b = gibY() - m * gibX();

		//y = m * x + b
		return (int)(m * x + b);
	}


	@Override
	public void zeichne(){
		eigenenBereichZeichnen();//Umrandung und eventuell gelbe Unterlegung zeichnen

		//Fall-Listen zeichnen
		for (int i=0; i < listen.size(); i++){
			if (!listen.get(i).isEmpty()){
				listen.get(i).alleZeichnen();
			}
		}


		//die beiden Schrägen Linien zeichnen
		g.drawLine(gibX(),gibY(),gibX() + xVerschiebungFuerTrennlinie, gibY() +getObererRand() +yVerschiebungFuerTrennLinie);
		g.drawLine(gibX() + xVerschiebungFuerTrennlinie, gibY() +getObererRand() +yVerschiebungFuerTrennLinie, gibX() + gibBreite(), gibY());


		//senkrechte Striche im Kopfteil der Fallauswahl und Fallbeschriftungen zeichnen
		int x;
		StruktogrammElementListe tmp;

		for (int i=0; i < listen.size(); i++){
			tmp = listen.get(i);

			x = tmp.gibRechterRand();

			if (i != listen.size() -1){//senkrechte Striche zeichnen, die Sonstliste (die Letzte) braucht keinen senkrechten Strich
				g.setColor(Color.black);
				g.drawLine(x, gibY()+gibHoehe(), x, gibPassendeYKoordFuerLinie(x));
			}

			//Fallbeschreibungen zeichnen
			if (this instanceof Verzweigung){//ein bischen rumtricksen... bei der Verzweigung soll Ja und Nein ja ganz an den Rändern stehen
				if (i==0){
					x = gibX() + 5; //5 Pixel von linken Rand entfernt
				}else{
					x = tmp.gibRechterRand() - 5 - gibTextbreite(tmp.gibBeschreibung()); //5 Pixel vom rechten Rand entfernt
				}
			}else{ //bei der Fallauswahl sollen die Überschriften mittig über den Zeilen stehen
				x = tmp.gibX() + gibXVerschiebungFuerMittig(tmp.gibBeschreibung(), tmp.gibRechterRand() -tmp.gibX());
			}

			g.setColor(getFarbeSchrift());
			g.drawString(tmp.gibBeschreibung(), x, gibY() + getObererRand() - 5); //Beschreibung zeichnen
		}


		textZeichnen(); //Textzeilen zeichnen

	}




	@Override
	public Rectangle zeichenbereichAktualisieren(int x, int y){

		int gesamtbreiteDerListen = 0;
		int groessteHoeheDerListen = 0;

		for (int i=0; i < listen.size(); i++){

			Rectangle rectListe = listen.get(i).zeichenbereichAllerElementeAktualisieren(x + gesamtbreiteDerListen, y + getObererRand());//Zeichenbereich der einzelnen Listen aktualisieren, die x-Position ist immer der rechte Rand der vorherigen Liste
			gesamtbreiteDerListen += rectListe.width;

			//Höhe der größten Liste ermitteln
			if (rectListe.height > groessteHoeheDerListen){
				groessteHoeheDerListen = rectListe.height;
			}
		}



		xVerschiebungFuerTrennlinie = gesamtbreiteDerListen - listen.get(listen.size()-1).gibBreite(); //xVerschiebung der Trennlinie ist Breite der Listen minus Breite der Sonst-Liste




		if (gesamtbreiteDerListen < gibMindestbreite()){
			gesamtbreiteDerListen = gibMindestbreite();
		}


		if(GlobalSettings.gibLetzteElementeStrecken()){//wenn der User diese Einstellung gewählt hat...
			setzeHoehe(getObererRand() + groessteHoeheDerListen);//...Höhe setzen und somit das letzte Element jeder Liste strecken
		}


		bereich.setBounds(x, y, gesamtbreiteDerListen, getObererRand() + groessteHoeheDerListen);//eigenen Bereich festhalten
		

		return bereich;
	}




	@Override
	public void setzeXPos(int x){
		bereich.x = x;

		int xVerschiebung = 0;

		for (int i=0; i < listen.size(); i++){
			listen.get(i).xPosAllerUnterelementeSetzen(x + xVerschiebung);
			xVerschiebung += listen.get(i).gibBreite();
		}
	}
	
	
	@Override
	public void zoomsZuruecksetzen(){
		super.zoomsZuruecksetzen();
		
		for(int i=0; i < listen.size(); i++){
			listen.get(i).zoomsAllerElementeZuruecksetzen();
		}
	}
	
	
	@Override
	public int getObererRand(){
		return super.getObererRand() + getYVergroesserung();
	}

}