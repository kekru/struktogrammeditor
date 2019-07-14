package de.kekru.struktogrammeditor.struktogrammelemente;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.jdom.Element;

import de.kekru.struktogrammeditor.control.Struktogramm;
import de.kekru.struktogrammeditor.control.XMLLeser;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;
import de.kekru.struktogrammeditor.view.CodeErzeuger;

public abstract class StruktogrammElement { //abstrakte Klasse -> keine Objekte davon erzeugbar
	protected String[] text; //Textzeilen, die im Kopfbereich des jeweiligen StruktogrammElementes angzeigt werden
	protected Rectangle bereich; //Koordinaten, Breite und Hoehe des jeweiligen StruktogrammElemente
	protected boolean markiert; //speichert, ob dieses StruktogrammElement gelb unterlegt gezeichnet werden soll
	protected Graphics2D g; //Graphics-Kontext des BufferedImage bild des Struktogramms
	protected static final int vorschauHoehe = 20; //Höhe des roten Vorschau-Rechteckes
	private int obererRand; //verändert sich je nach Anzahl der Textzeilen
	protected int obererRandZusatz; //wird pro von StruktogrammElement abgeleitete Klasse einmal gesetzt; beschreibt zusätzliche Pixelzahl zum für den oberen Rand
	private int xVergroesserung, yVergroesserung;
	private Color farbeSchrift = Color.black, farbeHintergrund = Color.white;

	public StruktogrammElement(Graphics2D g){
		this.g = g;

		obererRandZusatz = 20;

		bereich = new Rectangle();
		markiert = false;
		setzeText("");
		
		xVergroesserung = 0;
		yVergroesserung = 0;
	}




	//wandelt codierten String für die Quelltextgenerierung in einen auszugebenen String um
	protected String wandleZuAusgabe(String codierung, int typ, int anzahlEinzuruecken, boolean alsKommentare){
		codierung = einruecken(codierung,anzahlEinzuruecken);

		if(alsKommentare){
			codierung = codierung.replaceAll(co("kommentar"),CodeErzeuger.gibKommentarZeichen(true,typ))//Codierung von "kommentar" wird zum entsprechenden Zeichen umgewandelt
			.replaceAll(co("kommentarzu"),CodeErzeuger.gibKommentarZeichen(false,typ));//Codierung von "kommentarzu" wird zum entsprechenden Zeichen umgewandelt
		}else{
			codierung = codierung.replaceAll(co("kommentar"),"") //Codierung der Kommentare wird entfernt, weil der User den Quelltext ohne Kommentare will
			.replaceAll(co("kommentarzu"),"");
		}

		int x = codierung.indexOf(co("text")); //x ist die Position, wo das erste Zeichen des Textes erscheinen soll
		if(x > -1){
			x = x - codierung.substring(0,x).lastIndexOf("\n") -1; // x minus die Position des letzten Zeilenumbruchs im Bereich 0 bis x ist der Abstand (in Zeichen) des ersten Textzeichens vom linken Rand; alle weiteren Zeilen sollen um x Leerzeichen eingerückt werden, um einen linksbündigen Textblock zu erhalten
		}else{
			x = 0; //co("text") war nicht im Eingabestring
		}



		return codierung.replace(co("text"),textzeilenAusgeben(anzahlEinzuruecken, x)) //co("text") wird durch die eingerückten Textzeilen ersetzt
		.replaceAll(co("zwangkommentar"),CodeErzeuger.gibKommentarZeichen(true,typ)) //Zwangkommentare werden eingefügt
		.replaceAll(co("zwangkommentarzu"),CodeErzeuger.gibKommentarZeichen(false,typ));
	}


	protected String co(String s){ //codiert s, damit es sehr unwahrscheinlich wird, dass der User zufällig den Schlüsselstring eingibt
		return "%%"+s+"SbGRXEJUz4ZbvaaN%%";
	}


	//gibt einen String zurück, in dem alle Textzeilen mit Zeilenumbrüchen eingerückt enthalten sind
	protected String textzeilenAusgeben(int anzahlEinzuruecken, int xPosErsteZeile){
		String rueckgabe = "";

		for (int i=0; i < text.length; i++){

			if (i > 0){
				rueckgabe += einruecken(text[i], xPosErsteZeile);
			}else{
				rueckgabe += text[i];//erste Zeile nicht einrücken, damit z.B. zwischen while( und der Zeile keine Leerschritte sind
			}


			if(i < text.length -1)//nach der letzten Zeile keinen Zeilenumbruch, damit ein eventuelles Kommentar-Zu Zeichen direkt dahinter gehangen werden kann
				rueckgabe += "\n";
		}

		return rueckgabe;
	}






	/*wird von den abgeleiteten Klassen überschrieben;
     typ gibt an, welche Sprache erzeugt werden soll, anzahlEingerueckt, wie weit bisher eingerückt worden ist,
     anzahlEinzuruecken, wie weit pro Einrückung eingerückt werden soll,
     alsKommentar, ob die Textzeilen und Fallnamen auskommentiert erscheinen sollen und
     textarea ist die JTextAreaEasy, in die der Code eingefügt werden soll*/
	public void quellcodeGenerieren(int typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar, JTextAreaEasy textarea){

	}

	protected String einruecken(String codeZeile, int anzahlStellen){
		for (int i=0; i < anzahlStellen; i++){ //gewünschte Anzahl an Leerzeichen vorne anhängen
			codeZeile = " "+codeZeile;
		}
		return codeZeile;
	}




	public void schreibeXMLDaten(Element elem){
		Element neues = new Element("strelem")//strelem-Tag mit dem Attribut typ, welches die Typnummer für das StruktogrammElement angibt, wird eingefügt
			.setAttribute("typ",""+Struktogramm.strElementZuTypnummer(this))
			.setAttribute("zx",""+xVergroesserung)
			.setAttribute("zy",""+yVergroesserung)
			.setAttribute("textcolor",""+getFarbeSchrift().getRGB())
			.setAttribute("bgcolor",""+getFarbeHintergrund().getRGB());

		for (int i=0; i < text.length; i++){
			neues.addContent(new Element("text").addContent(XMLLeser.encodeS(text[i])));//in den strelem-Tag wird pro Textzeile ein text-Tag eingefügt, mit der Textzeile als Inhalt, die Textzeile ist dabei codiert, weil es beim laden später Probleme u.a. mit Umlauten gibt
		}

		zusaetzlicheXMLDatenSchreiben(neues);

		elem.addContent(neues); //strelem-Tag wird in den übergeordneten Tag eingefügt
	}


	//wird von Schleife und von Fallauswahl überschrieben
	protected void zusaetzlicheXMLDatenSchreiben(Element aktuelles){

	}




	public boolean istUnterelement(StruktogrammElement eventuellesUnterelement){
		return false;//eventuellesUnterelement ist nicht Unterelement von this
	}



	//wird in Fallauswahl überschrieben
	public String[] gibFaelle(){
		String[] faelle = new String[1];
		faelle[0] = "";
		return faelle;
	}


	public void setzeFaelle(String[] faelle){
		//nichts, wird nur in Verzweigung und Fallauswahl gebraucht
	}







	public int gibAnzahlListen(){
		return 0;
	}



	protected void setzeGraphics(Graphics2D g){
		this.g = g;
		randGroesseSetzen();
	}


	public void setzeMarkiert(boolean markiert){
		this.markiert = markiert;
	}



	protected boolean objGesetzt(Object obj){
		return obj != null;
	}



	public boolean neuesElementMussOberhalbPlatziertWerden(int y){
		return y < gibY() + gibHoehe()/2;
	}


	//gibt den Bereich zurück, der die Größe und Position des roten Vorschaurechteckes angibt, wenn die Maus an der Position vorschauPoint ist
	public Rectangle gibVorschauRect(Point vorschauPoint){
		int anYPos;

		if (neuesElementMussOberhalbPlatziertWerden(vorschauPoint.y)){
			//der Point ist auf der obere Hälfte -> Vorschau über diesem Element zeichnen
			anYPos = gibY() - vorschauHoehe / 2;
		}else{
			//Vorschau muss unter diesem Element gezeichnet werden
			anYPos = gibY() + gibHoehe() - vorschauHoehe / 2;
		}


		return new Rectangle(gibX(),anYPos,gibBreite(),vorschauHoehe);
	}


	public Rectangle gibRectangle(){
		return bereich;
	}

	protected int gibMindestbreite(){
		return gibBreiteDerBreitestenTextzeile() + xVergroesserung + 80;
	}
	
	
	protected int getXVergroesserung(){
		return xVergroesserung;
	}
	
	protected int getYVergroesserung(){
		return yVergroesserung;
	}


	public void setXVergroesserung(int xVergroesserung) {
		this.xVergroesserung = xVergroesserung;
	}




	public void setYVergroesserung(int yVergroesserung) {
		this.yVergroesserung = yVergroesserung;
	}




	protected int gibBreiteDerBreitestenTextzeile(){
		int groessteBreite = 0;
		int breite;

		for (String s : text){//text-Array durchgehen
			breite = gibTextbreite(s);
			if (breite > groessteBreite){
				groessteBreite = breite;
			}
		}

		return groessteBreite;
	}


	protected Object gibElementAnPos(int x, int y, boolean nurListe){//Elemente mit UnterElementen überschreiben diese Methode

		if (nurListe){
			return null;//Ich bin keine StruktogrammElementListe
		}

		if (bereich.contains(x,y)){
			return this;//der angegebene Punkt ist in meinem Bereich
		}else{
			return null;//der angegebene Punkt ist nicht in meinem Bereich
		}
	}


	public StruktogrammElementListe gibListeDieDasElementHat(StruktogrammElement element){
		return null; //ich habe keine Liste
	}


	public abstract Rectangle zeichenbereichAktualisieren(int x, int y);//wird in den Tochterklassen überschrieben
	

	public void setzeText(String[] text){
		this.text = text;
		randGroesseSetzen();
	}

	protected void randGroesseSetzen(){
		setObererRand(obererRandZusatz + text.length * gibTexthoehe(text[0]));
	}

	protected void setzeText(String textEineZeile){
		text = new String[1];
		text[0] = textEineZeile;
		randGroesseSetzen();
	}

	public String[] gibText(){
		return text;
	}


	public void zeichne(){ //wird überschrieben, aber mit super.zeichne(); aufgerufen
		eigenenBereichZeichnen();
		textZeichnen();
	}


	protected void textZeichnen(){
		int texthoehe = gibTexthoehe(text[0]);
		int yVerschiebungAktuell = texthoehe - 5;
		
		g.setColor(farbeSchrift);

		for (String s : text){
			g.drawString(s, gibX() + gibXVerschiebungFuerTextInMitte(s), gibY() + yVerschiebungAktuell);//Textzeilen untereinander zeichnen
			yVerschiebungAktuell += texthoehe;
		}
		
		zeichneBeideGroessenaenderungskaestchen(0);
	}
	
	protected void zeichneBeideGroessenaenderungskaestchen(int yVerschiebung){
		/*if(markiert){    	  
			final Font fontVorher = g.getFont();
			g.setFont(new Font("serif", Font.PLAIN, 15));
			
			zeichneGroessenaenderungskaestchen("+", gibX() + gibBreite() - 26, gibY() + 2 + yVerschiebung, 10);
			zeichneGroessenaenderungskaestchen("-", gibX() + gibBreite() - 12, gibY() + 2 + yVerschiebung, 10);

			g.setFont(fontVorher);
		}*/
		
		//TODO Vielleicht später wieder einbauen, das Anzeigen der Kästchen geht mit obigem Code bereits
	}


	protected void eigenenBereichZeichnen(){
		if (!markiert){
			g.setColor(farbeHintergrund);//für Rechteck mit eingestellter Farbe
		}else{
			g.setColor(Color.yellow);//für gelbes Rechteck als Markierung
		}
		g.fillRect(gibX(), gibY(), gibBreite(), gibHoehe());//ausgefülltes Rechteck zeichnen
		g.setColor(Color.black);
		g.drawRect(gibX(), gibY(), gibBreite(), gibHoehe());//schwarzer Rand
	}

	//Nicht löschen, wird vielleicht später noch genutzt
	/*private void zeichneGroessenaenderungskaestchen(final String text, final int x, final int y, final int kantenlaenge){
		g.setColor(Color.white);
		g.fillRect(x, y, kantenlaenge, kantenlaenge);
		g.setColor(Color.black);
		g.drawRect(x, y, kantenlaenge, kantenlaenge);
		g.drawString(text, x + 1 + gibXVerschiebungFuerMittig(text, kantenlaenge), y + 1 + kantenlaenge);// + gibYVerschiebungFuerMittig(text, kantenlaenge));
	}*/

	protected int gibX(){
		return bereich.x;
	}

	protected int gibY(){
		return bereich.y;
	}

	protected int gibBreite(){
		return bereich.width;
	}

	protected int gibHoehe(){
		return bereich.height;
	}


	public void setzeBreite(int neueBreite){
		bereich.width = neueBreite;
	}

	public void setzeHoehe(int neueHoehe){
		bereich.height = neueHoehe;
	}


	protected int gibTextbreite(String s){
		return (g != null) ? (int) g.getFontMetrics().getStringBounds(s, g).getBounds().getWidth() : s.length() * 4;//http://www.tutorials.de/java/288641-textlaenge-pixel.html
	}

	protected int gibTexthoehe(String s){
		if (objGesetzt(g)){ //sonst gib es einen Fehler beim erzeugen des Struktogramm, weil dort eine StruktogrammElementListe mit null als Graphics2D erzeugt wird und darin dann ein LeerElement
			return (int) g.getFontMetrics().getStringBounds(s, g).getBounds().getHeight();//http://www.tutorials.de/java/288641-textlaenge-pixel.html
		}else{
			return 20;
		}
	}


	protected int gibXVerschiebungFuerTextInMitte(String textzeile){
		return gibXVerschiebungFuerMittig(textzeile, gibBreite());
	}

	//gibt die x-Verschiebung zurück, damit der Text s mittig in einem Bereich der Breite breiteUntergrund dargestellt wird
	protected int gibXVerschiebungFuerMittig(String s, int breiteUntergrund){
		return (int)((breiteUntergrund - gibTextbreite(s)) / 2);//breiteUntergrund - gibTextbreite(s) ist, wie weit der Untergrund übersteht gegenüber der Textbreite; durch 2 dividieren des Überstehenden und eine Hälfe nach links packen, dann ist der Text in der Mitte
	}
	
	/*private int gibYVerschiebungFuerMittig(String s, int hoeheUntergrund){
		return (int)((hoeheUntergrund - gibTexthoehe(s)) / 2);//breiteUntergrund - gibTextbreite(s) ist, wie weit der Untergrund übersteht gegenüber der Textbreite; durch 2 dividieren des Überstehenden und eine Hälfe nach links packen, dann ist der Text in der Mitte
	}*/


	public void setzeXPos(int x){
		bereich.x = x;
	}
	
	public void zoomX(int erhoeheXUm){
		if(xVergroesserung + erhoeheXUm >= 0){
			xVergroesserung += erhoeheXUm;
		}
	}
	
	public void zoomY(int erhoeheYUm){
		if(yVergroesserung + erhoeheYUm >= 0){
			yVergroesserung += erhoeheYUm;
		}
	}
	
	public void zoomsZuruecksetzen(){
		xVergroesserung = 0;
		yVergroesserung = 0;
	}


	public void setObererRand(int obererRand) {
		this.obererRand = obererRand;
	}


	public int getObererRand() {
		return obererRand;
	}


	public Color getFarbeSchrift() {
		return farbeSchrift;
	}

	public void setFarbeSchrift(Color farbeSchrift) {
		this.farbeSchrift = farbeSchrift;
	}

	public Color getFarbeHintergrund() {
		return farbeHintergrund;
	}

	public void setFarbeHintergrund(Color farbeHintergrund) {
		this.farbeHintergrund = farbeHintergrund;
	}

}