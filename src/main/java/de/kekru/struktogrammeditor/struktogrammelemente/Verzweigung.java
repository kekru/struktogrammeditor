package de.kekru.struktogrammeditor.struktogrammelemente;

import java.awt.Graphics2D;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.control.Struktogramm;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;
import de.kekru.struktogrammeditor.view.CodeErzeuger;

public class Verzweigung extends Fallauswahl { //erbt von Fallauswahl
   private boolean seitenSindVertauscht;
   private static final String jaText = "Ja";
   private static final String neinText = "Nein";

   public Verzweigung(Graphics2D g){
      super(g,2);
      gibLinkeSeite().setzeBeschreibung(jaText);
      gibRechteSeite().setzeBeschreibung(neinText);
      xVerschiebungFuerTrennlinie = 0;
      yVerschiebungFuerTrennLinie = 0; //die schrägen Linien sollen bis zum Boden des Kopfteils gehen
      obererRandZusatz = 20;
      seitenSindVertauscht = false;
      setzeText(GlobalSettings.gibElementBeschriftung(Struktogramm.typVerzweigung));
   }
   
   
   @Override  //siehe Fallauswahl
   public void quellcodeGenerieren(int typ, int anzahlEingerueckt, int anzahlEinzuruecken, boolean alsKommentar, JTextAreaEasy textarea){
      String vorher = "";
      String nachher = "";
      String zwischenStueck = "";


      switch(typ){
         case CodeErzeuger.typJava:
            vorher = "if("+co("kommentar")+co("text")+co("kommentarzu")+"){\n";
            zwischenStueck = "}else{\n";
            nachher = "}\n";
            break;

         case CodeErzeuger.typDelphi:
            vorher = "if "+co("kommentar")+co("text")+co("kommentarzu")+" then \n"+einruecken("begin\n",anzahlEingerueckt);
            zwischenStueck = "end\n"+einruecken("else\n",anzahlEingerueckt)+einruecken("begin\n",anzahlEingerueckt);
            nachher = "end;\n";
            break;
      }

      textarea.hinzufuegen(wandleZuAusgabe(vorher,typ,anzahlEingerueckt,alsKommentar));

      StruktogrammElementListe jaSeite;
      StruktogrammElementListe neinSeite;
      if (seitenSindVertauscht){
         jaSeite = gibRechteSeite();
         neinSeite = gibLinkeSeite();
      }else{
         jaSeite = gibLinkeSeite();
         neinSeite = gibRechteSeite();
      }

      jaSeite.quellcodeAllerUnterelementeGenerieren(typ,anzahlEingerueckt+anzahlEinzuruecken,anzahlEinzuruecken,alsKommentar,textarea);
      textarea.hinzufuegen(wandleZuAusgabe(zwischenStueck,typ,anzahlEingerueckt,alsKommentar));
      neinSeite.quellcodeAllerUnterelementeGenerieren(typ,anzahlEingerueckt+anzahlEinzuruecken,anzahlEinzuruecken,alsKommentar,textarea);

      textarea.hinzufuegen(wandleZuAusgabe(nachher,typ,anzahlEingerueckt,alsKommentar));


   }
   
   
   
   
   
   private StruktogrammElementListe gibLinkeSeite(){
      return listen.get(0);
   }
   
   private StruktogrammElementListe gibRechteSeite(){
      return listen.get(1);
   }

   
   
   public void seitenVertauschen(){
      listenTauschen(0,1);
      seitenSindVertauscht = !seitenSindVertauscht;
   }
   
   
   
   @Override
   public void erstelleNeueSpalte(){

   }

}
