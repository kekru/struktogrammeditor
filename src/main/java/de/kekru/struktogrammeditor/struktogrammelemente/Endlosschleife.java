package de.kekru.struktogrammeditor.struktogrammelemente;

import java.awt.Graphics2D;

import org.jdom2.Element;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;
import de.kekru.struktogrammeditor.other.SupportedLanguages;

public class Endlosschleife extends Schleife {//erbt von Schleife

   public Endlosschleife(Graphics2D g){
      super(g);

      setUntererRand(40);//obererRand wird schon in Schleife gesetzt
      setzeText(GlobalSettings.gibElementBeschriftung(AnweisungsTyp.Endlosschleife));
   }
   
   
   //siehe DoUntilSchleife
   @Override
   public void quellcodeGenerieren(SupportedLanguages typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar, JTextAreaEasy textarea){
      String vorher = "";
      String nachher = "";

      switch(typ){
         case Java:
            vorher = "while(true)" + co("zwangkommentar") + co("text") + co("zwangkommentarzu") + "{\n";//zwangkommentar bedeutet, es soll auf jeden Fall ein Kommentarzeichen eingefügt werden, egal was der User ausgewählt hat
            nachher = "}\n";
            break;

         case Delphi:
            vorher = "while true " + co("zwangkommentar") + co("text") + co("zwangkommentarzu") + " do \n"+einruecken("begin",anzahlEingerueckt)+"\n";
            nachher = "end;\n";
            break;
         case PHP:
        	 vorher = "while(true)" + co("zwangkommentar") + co("text") + co("zwangkommentarzu") + "{\n";//zwangkommentar bedeutet, es soll auf jeden Fall ein Kommentarzeichen eingefügt werden, egal was der User ausgewählt hat
             nachher = "}\n";
        	 break;
      }

      textarea.hinzufuegen(wandleZuAusgabe(vorher,typ,anzahlEingerueckt,alsKommentar));
      liste.quellcodeAllerUnterelementeGenerieren(typ,anzahlEingerueckt + anzahlEinzuruecken,anzahlEinzuruecken,alsKommentar,textarea);
      textarea.hinzufuegen(wandleZuAusgabe(nachher,typ,anzahlEingerueckt,alsKommentar));
   }


   	@Override
	public void setzeFaelle(String[] faelle) {}


	@Override
	protected void zusaetzlicheXMLDatenSchreiben(Element aktuelles) {} 
}