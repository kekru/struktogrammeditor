package de.kekru.struktogrammeditor.view;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.kekru.struktogrammeditor.control.Controlling;
import de.kekru.struktogrammeditor.control.Struktogramm;

public class StrTabbedPane extends JTabbedPane implements ChangeListener{
   
   private static final long serialVersionUID = 1L;
   private Controlling controlling;
   //private boolean stateChangedFreigegeben;

   public StrTabbedPane(Controlling controlling){
      super();
      this.controlling = controlling;
      
      addChangeListener(this);
      addKeyListener(controlling);
   }
   
   
   public GUI gibGUI(){
	   return controlling.getGUI();
   }
   
//   public void changeListenerAktivieren(){
//      addChangeListener(this);
//      stateChangedFreigegeben = true;
//   }
   
   
   public Struktogramm struktogrammHinzufuegen(){
      Struktogramm str = new Struktogramm(this);
      add("Unbenannt", new JScrollPane(str));
      //stateChangedFreigegeben = false; //changeListener kurz deaktivieren...
      setSelectedIndex(getTabCount() -1); //...weil es sonst in graphicsInitialisieren Probleme gibt
      //stateChangedFreigegeben = true;
      return str;
   }
   
   
   public void titelDerAktuellenSeiteSetzen(String titel){
      setTitleAt(getSelectedIndex(),titel);
   }
   
   public void titelDerAktuellenSeiteAlsBearbeitetOderAlsGespespeichertMarkieren(boolean bearbeitet){
      if (getSelectedIndex() > -1){

         String titel = getTitleAt(getSelectedIndex());
      
         if (bearbeitet){//"*" anhängen
            if (titel.charAt(titel.length() -1) != '*'){
               titel += "*";
            }
         }else{//"*" entfernen
            if (titel.charAt(titel.length() -1) == '*'){
               titel = titel.substring(0,titel.length() -1);
            }
         }
      
         setTitleAt(getSelectedIndex(),titel);
      }
   }
   
   
   public void aktuellesStruktogrammschliessen(){
      switch(JOptionPane.showConfirmDialog(controlling.getGUI(), "Vor dem Schließen aktuelles Struktogramm speichern?", "Vorher speichern?", JOptionPane.YES_NO_CANCEL_OPTION)){
         case JOptionPane.YES_OPTION:
            controlling.speichern(false);
            remove(getSelectedIndex());
            break;
         case JOptionPane.NO_OPTION:
            remove(getSelectedIndex());
            break;
      }
   }
   
   
   public Struktogramm gibAktuellesStruktogramm(){
      if(getTabCount() > 0){
         /*getSelectedComponent() liefert das JScrollPane,
           dieses liefert mit getComponents()[0] seinen JViewPort,
           dieser liefert mit getComponents()[0] dann das Struktogramm*/
         return (Struktogramm)(((JViewport)(((JScrollPane)getSelectedComponent()).getComponents())[0]).getComponents())[0];
      }else{
         return null;
      }
   }
   
   
   public void stateChanged(ChangeEvent e){
//      if (stateChangedFreigegeben && (getTabCount() > 1)){//wenn es das erste ist (nach Schließen), wird diese Methode aufgerufen und graphicsInitialisieren klappt noch nicht, darum nicht zulassen
//
//
//         Struktogramm str = gibAktuellesStruktogramm();
//         if (str != null){
//            str.graphicsInitialisieren();
//            str.zeichenbereichAktualisieren();
//            str.zeichne();
//            gui.titelleisteAktualisieren();
//         }
//      }
	   
	   controlling.titelleisteAktualisieren();
   }
   
   
   public boolean einOderMehrereStruktogrammeNichtGespeichert(){
      for(int i=0; i < getTabCount(); i++){
         if(getTitleAt(i).charAt(getTitleAt(i).length() -1) == '*'){
            return true;	 
         }
      }
      
      return false;
   }
}