package de.kekru.struktogrammeditor.other;
import java.awt.Container;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class JListEasy extends JList {
   
   private static final long serialVersionUID = -5302981897521724160L;
   private DefaultListModel model;
   private JScrollPane scrollpane;
   
   public JListEasy(){
      super(new DefaultListModel());
      model = (DefaultListModel)getModel();
      //setBounds(8, 16, 225, 137);
   }

   public JListEasy(int xpos, int ypos, int breite, int hoehe){
      super(new DefaultListModel());
      model = (DefaultListModel)getModel();
      setBounds(xpos, ypos, breite, hoehe);
   }
   
   
   public void setzeContainer(Container cp){
      scrollpane = new JScrollPane(this);//http://www.dpunkt.de/java/Programmieren_mit_Java/Oberflaechenprogrammierung/14.html
      scrollpane.setBounds(getBounds());
      cp.add(scrollpane);
     // scrollpane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
     // scrollpane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
   }
   
   public int gibIndex(){
      return getSelectedIndex();
   }
   
   public void setzeIndex(int neuerIndex){
      setSelectedIndex(neuerIndex);
   }
   
   public void setzeText(String s, int index){
      if ((index >= 0) && (index < gibAnzahl())){
         model.set(index,s);
      }
   }
   
   public String gibMarkiertenInhalt(){
      if (gibIndex() >= 0){
         return ""+getModel().getElementAt(gibIndex());
      }else{
         return "";
      }
   }
   
   public String gibInhalt(int indexDesInhalt){
      if (indexIstVorhanden(indexDesInhalt)){
         return ""+getModel().getElementAt(indexDesInhalt);
      }else{
         return "";
      }
   }
   
   public void fuegeHinzu(String s){
      model.add(gibAnzahl(),s);
   }
   
   public void fuegeHinzuAmAnfang(String s){
      model.add(0,s);
   }
   
   public void fuegeHinzuAnStelle(String s, int stelle){
      if (stelle > gibAnzahl()){
        stelle = gibAnzahl();
      }
      model.add(stelle,s);
   }
   
   public void entferne(int indexDesEintrages){
      if (indexIstVorhanden(indexDesEintrages)){
         model.remove(indexDesEintrages);
      }
   }
   
   public void entferneLetztenEintrag(){
      entferne(gibAnzahl()-1);
   }
   
   public void entferneAlle(){
      for(int i=gibAnzahl()-1; i >= 0 ; i--){
         entferne(0);
      }
   }
   
   private boolean indexIstVorhanden(int indexDesEintrages){
      return (indexDesEintrages >= 0) && (indexDesEintrages < gibAnzahl());
   }
   
   public int gibAnzahl(){
      return model.getSize();
   }
}