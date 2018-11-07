package de.kekru.struktogrammeditor.other;
import java.awt.Container;
import java.awt.Font;

//Quelle: http://www.dpunkt.de/java/Referenz/Das_Paket_javax.swing/28.html
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JTextAreaEasy extends JTextArea {
    
	private static final long serialVersionUID = -4009874987394271922L;
	private JScrollPane scrollpane;

    public JTextAreaEasy(){
       super();
       setLineWrap(false);
       setWrapStyleWord(false);
       //setBounds(0, 0, 400, 200);
    }
    
    public JTextAreaEasy(int xpos, int ypos, int breite, int hoehe){
       super();
       setLineWrap(false);
       setWrapStyleWord(false);
       setBounds(xpos, ypos, breite, hoehe);
    }
    
    public void setzeContainer(Container cp){
       scrollpane = new JScrollPane(this);//http://www.dpunkt.de/java/Programmieren_mit_Java/Oberflaechenprogrammierung/14.html
       scrollpane.setBounds(getBounds());
       cp.add(scrollpane);
    }
    
    public void hinzufuegen(String s){
       append(s);
    }
    
    public void einfuegenAnStelle(String s, int stelle){
       insert(s,stelle);
    }
    
    public void zeilenumbruch(){
       hinzufuegen("\n");
    }
    
    public void zeilenumbruchAnStelle(int stelle){
       einfuegenAnStelle("\n",stelle);
    }
    
    public void leeren(){
       setText("");
    }
    
    public void setzeNurLesen(boolean nurLesen){
       setEditable(!nurLesen);
    }
    
    public String gibText(){
       return getText();
    }
    
    public void setzeText(String text){
       setText(text);
    }
    
    public String[] gibTextzeilenArray(){
       return getText().split("\n");//http://download.oracle.com/javase/1.4.2/docs/api/java/lang/String.html#split%28java.lang.String%29
    }
    
    public void setzeFont(Font font){
       setFont(font);
    }
}