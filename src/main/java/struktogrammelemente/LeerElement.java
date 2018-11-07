package struktogrammelemente;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import control.GlobalSettings;
import control.Struktogramm;

import other.JTextAreaEasy;



public class LeerElement extends Anweisung {//erbt von Anweisung

   public LeerElement(Graphics2D g){
      super(GlobalSettings.gibElementBeschriftung(Struktogramm.typLeerElement),g); //"ø"
   }
   
   /*@Override
   public void setzeText(String[] text){
      //Text kann nicht geändert werden
   }*/

   
   @Override
   public Rectangle gibVorschauRect(Point vorschauPoint){
      return new Rectangle(gibX(),gibY(),gibBreite(),gibHoehe());//Voraschaurect geht über das ganze LeerElement, um zu zeigen, dass es beim Einfügen ersetzt wird
   }
   
   
   @Override
   public void quellcodeGenerieren(int typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar, JTextAreaEasy textarea){
      //LeerElement soll keinen QuellCode generieren
   }
   
}