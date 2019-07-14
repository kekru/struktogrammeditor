package de.kekru.struktogrammeditor.struktogrammelemente;

import java.awt.Graphics2D;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.control.Struktogramm;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;
import de.kekru.struktogrammeditor.view.CodeErzeuger;

public class Endlosschleife extends Schleife {//erbt von Schleife

   public Endlosschleife(Graphics2D g){
      super(g);

      setUntererRand(40);//obererRand wird schon in Schleife gesetzt
      setzeText(GlobalSettings.gibElementBeschriftung(Struktogramm.typEndlosschleife));
   }
   
   
   //siehe DoUntilSchleife
   @Override
   public void quellcodeGenerieren(int typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar, JTextAreaEasy textarea){
      String vorher = "";
      String nachher = "";


      switch(typ){
         case CodeErzeuger.typJava:
            vorher = "while(true)"+co("zwangkommentar")+co("text")+co("zwangkommentarzu")+"{\n";//zwangkommentar bedeutet, es soll auf jeden Fall ein Kommentarzeichen eingefügt werden, egal was der User ausgewählt hat
            nachher = "}\n";
            break;

         case CodeErzeuger.typDelphi:
            vorher = "while true "+co("zwangkommentar")+co("text")+co("zwangkommentarzu")+" do \n"+einruecken("begin",anzahlEingerueckt)+"\n";
            nachher = "end;\n";
            break;
      }

      textarea.hinzufuegen(wandleZuAusgabe(vorher,typ,anzahlEingerueckt,alsKommentar));
      liste.quellcodeAllerUnterelementeGenerieren(typ,anzahlEingerueckt+anzahlEinzuruecken,anzahlEinzuruecken,alsKommentar,textarea);
      textarea.hinzufuegen(wandleZuAusgabe(nachher,typ,anzahlEingerueckt,alsKommentar));
   }
   
}