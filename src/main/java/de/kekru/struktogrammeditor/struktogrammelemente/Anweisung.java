package de.kekru.struktogrammeditor.struktogrammelemente;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.control.Struktogramm;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;


public class Anweisung extends StruktogrammElement {

   public Anweisung(String text, Graphics2D g){
      super(g);
      obererRandZusatz = 10;
      setzeText(text);
   }

   public Anweisung(Graphics2D g){
      this(GlobalSettings.gibElementBeschriftung(Struktogramm.typAnweisung),g);
   }
   
   
   
   
   @Override
   public void quellcodeGenerieren(int typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar, JTextAreaEasy textarea){



      textarea.hinzufuegen(wandleZuAusgabe( co("kommentar")+co("text")+co("kommentarzu"),
                                            typ,anzahlEingerueckt,alsKommentar)+"\n");
   }
   
   

   
   
   @Override
   protected int gibMindestbreite(){
      return gibBreiteDerBreitestenTextzeile() + getXVergroesserung() + 30;
   }
   
   @Override
   public Rectangle zeichenbereichAktualisieren(int x, int y){
      
      bereich.setBounds(x,y, gibMindestbreite() , getObererRand() + getYVergroesserung());

      return bereich;
   }
   
}