package de.kekru.struktogrammeditor.struktogrammelemente;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.jdom.Element;


public abstract class Schleife extends StruktogrammElement { //abstrakte Klasse -> kann nicht erzeugt werden; erbt von Struktogrammelement
	protected StruktogrammElementListe liste; //Liste mit den innerhalb der Schleife befindlichen StruktogrammElementen
	private static final int linkerRand = 20;
	private int untererRand;


	public Schleife(Graphics2D g){
		super(g);
		liste = new StruktogrammElementListe(g);
		setObererRand(0);
		setUntererRand(0);
		setzeText("Schleife");
	}



	public StruktogrammElementListe gibListe(){
		return liste;
	}


	@Override
	protected void zusaetzlicheXMLDatenSchreiben(Element aktuelles){
		Element neues = new Element("schleifeninhalt"); //neuer schleifeninhalt-Tag...
		liste.schreibeXMLDatenAllerUnterElemente(neues); //...in den die einzelnen Unterelemente gespeichert werden
		aktuelles.addContent(neues); //schleifeninhalt-Tag in eigenen strelem-Tag einfügen
	}




	@Override
	public boolean istUnterelement(StruktogrammElement eventuellesUnterelement){
		return liste.istUnterelement(eventuellesUnterelement);
	}



	@Override
	protected void setzeGraphics(Graphics2D g){
		super.setzeGraphics(g);

		liste.graphicsAllerUnterlementeSetzen(g);
	}



	@Override
	public void setzeBreite(int neueBreite){

		liste.breiteDerUnterelementeSetzen(neueBreite - linkerRand);

		liste.xPosAllerUnterelementeSetzen(gibX() + linkerRand);

		bereich.width = neueBreite;
	}


	@Override
	public void setzeHoehe(int neueHoehe){
		liste.gesamtHoeheSetzen(neueHoehe -getObererRand() -getUntererRand());
		bereich.height = neueHoehe;
	}



	@Override
	protected Object gibElementAnPos(int x, int y, boolean nurListe){
		Object tmp;

		if (bereich.contains(x,y)){ //der angegebene Punkt ist in meinem Bereich

			tmp = liste.gibElementAnPos(x,y,nurListe);
			if (objGesetzt(tmp)){ //ist der Punkt in der Liste?
				return tmp;
			}

			if (!nurListe){
				return this; //der Punkt ist nicht in der Liste, also im Kopfteil der Schleife
			}
		}

		return null; //der Punkt ist nicht auf dieser Schleife, oder er ist auf dem Kopfteil der Schleife und nurListe ist true
	}



	@Override
	public StruktogrammElementListe gibListeDieDasElementHat(StruktogrammElement element){
		return liste.gibListeDieDasElementHat(element);
	}




	@Override
	public void zeichne(){
		super.zeichne();


		if (!liste.isEmpty()){
			liste.alleZeichnen();
		}
	}





	@Override
	public Rectangle zeichenbereichAktualisieren(int x, int y){

		Rectangle rectListe = liste.zeichenbereichAllerElementeAktualisieren(x + linkerRand, y + getObererRand() + (! (this instanceof DoUntilSchleife) ? 0/*getYVergroesserung()*/ : 0));//Zeichenbereich der Liste aktualisieren


		int gesamtbreite;
		//Prüfen, ob die Breite der Liste größer oder gleich der Mindestbreite ist...
		if (rectListe.width >= gibMindestbreite()){
			gesamtbreite = rectListe.width;
		}else{
			gesamtbreite = gibMindestbreite();//...wenn nicht, dann Mindestbreite als Gesamtbreite nehmen
		}

		gesamtbreite += linkerRand;//linken Rand dazunehmen


		bereich.setBounds(x, y, gesamtbreite, getObererRand() + getUntererRand() + rectListe.height);//Bereich speichern

		
		
		return bereich;
	}





	@Override
	public void setzeXPos(int x){
		bereich.x = x;

		liste.xPosAllerUnterelementeSetzen(x + linkerRand);
	}
	
	
	@Override
	public void zoomsZuruecksetzen(){
		super.zoomsZuruecksetzen();
		liste.zoomsAllerElementeZuruecksetzen();
	}

	
	@Override
	public int getObererRand(){
		return super.getObererRand() + getYVergroesserung();
	}



	public void setUntererRand(int untererRand) {
		this.untererRand = untererRand;
	}



	public int getUntererRand() {
		return untererRand;
	}
}