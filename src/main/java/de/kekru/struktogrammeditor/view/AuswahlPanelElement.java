package de.kekru.struktogrammeditor.view;
import java.awt.Font;

import javax.swing.JLabel;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.other.Helpers;

public class AuswahlPanelElement extends JLabel{
   
   private static final long serialVersionUID = 5455270690460661892L;
   private int typ;
   public static final String iconOrdner = "/icons/";
   private String[] bildpfade = {
      "anweisung.jpg",
      "verzweigung.jpg",
      "fallauswahl.jpg",
      "forschleife.jpg",
      "whileschleife.jpg",
      "dountilschleife.jpg",
      "endlosschleife.jpg",
      "aussprung.jpg",
      "aufruf.jpg"
   };
   
   
   public AuswahlPanelElement(int typ){
      super();
      
      this.typ = typ;
      setIcon(Helpers.getIcon(gibBildpfad()));
      setFont(new Font("monospaced", Font.PLAIN, 15));//Monospaced, damit alle Buchstaben gleich Breit sind; es werden automatisch alle Labels auf dem AuswahlPanel zentriert, damit sie die gleiche x-Position haben, erhalten sie alle die gleiche Anzahl an Zeichen für ihren Text
      setText(GlobalSettings.getCurrentElementBeschriftungsstil()[typ]);
   }
   
   public int gibTyp(){
      return typ;
   }
   
   private String gibBildpfad(){
      if ((typ >= 0) && (typ < bildpfade.length)){
         return iconOrdner + bildpfade[typ];
      }else{
         return "";
      }
   }
}