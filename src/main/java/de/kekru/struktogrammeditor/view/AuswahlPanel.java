package de.kekru.struktogrammeditor.view;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom.Document;

import de.kekru.struktogrammeditor.control.Controlling;
import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.control.Struktogramm;
import de.kekru.struktogrammeditor.other.Helpers;


public class AuswahlPanel extends JPanel implements DropTargetListener, DragGestureListener, DragSourceListener{

	private static final long serialVersionUID = 3619714917985247680L;
	private AuswahlPanelElement[] panelElemente = new AuswahlPanelElement[9]; //9 StruktogrammElemente stehen zur Auswahl
	private DragSource dragSource;
	//private DropTarget dropTarget;
	private JLabel muelleimer;
	private JLabel kopierFeld;
	private boolean muelleimerIstAuf;
	private boolean kopierFeldIstAuf;
	private Controlling controlling;
	private Document kopiertesStrElement;

	public AuswahlPanel(Controlling controlling){

		this.controlling = controlling;

		setLayout(new GridBagLayout());
		
		
		GridBagConstraints c = new GridBagConstraints();			
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.ipadx = 1;
		c.ipady = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0,0,4,0);


		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;



		for (int i = 0; i < panelElemente.length; i++){//Label für mit den einzelnen Elementen erzeugen
			panelElemente[i] = new AuswahlPanelElement(i);
			//panelElemente[i].setLocation(20,10+20*i);
			add(panelElemente[i], c);
			c.gridy++;
		}




		//Mülleimer erzeugen
		muelleimer = new JLabel();
		muelleimerIstAuf = true;   //erst true setzen...
		muelleimerAuf(!muelleimerIstAuf); //dann mit ! einfuegen, weil in der Methode überprüft wird, ob er Parameter != muelleimerIstAuf ist
		muelleimer.setFont(new Font("monospaced", Font.PLAIN, 15));
		muelleimer.setText("Wegschmeißen");
		add(muelleimer, c);

		c.gridy++;
		c.weighty = 1000;
		
		//Kopier-Box erzeugen
		kopiertesStrElement = null;
		kopierFeld = new JLabel();
		kopierFeldIstAuf = true;
		kopierFeldAuf(!kopierFeldIstAuf);
		kopierFeld.setFont(new Font("monospaced", Font.PLAIN, 15));
		kopierFeld.setText("Kopie");
		add(kopierFeld, c);


		//für Drag & Drop Aktionen vorbereiten
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

		//dropTarget = 
		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE,this, true, null);
	}

	
	
	public void aktualisiereBeschriftungen(){
		for(int i=0; i < panelElemente.length; i++){
			panelElemente[i].setText(GlobalSettings.getCurrentElementBeschriftungsstil()[i]);
		}
	}


	public void setzeKopiertesStrElement(Document doc){
		kopiertesStrElement = doc;
	}

	public Document gibKopiertesStrElement(){
		return kopiertesStrElement;
	}



	private void muelleimerAuf(boolean oeffnen){
		if(muelleimerIstAuf != oeffnen){//damit nicht ständig das Icon neu geladen wird, kommt hier diese Sperre rein
			String bildname;

			if (oeffnen){
				bildname = "muelleimer2.png";
				muelleimerIstAuf = true;
			}else{
				bildname = "muelleimer1.png";
				muelleimerIstAuf = false;
			}

			muelleimer.setIcon(Helpers.getIcon(AuswahlPanelElement.iconOrdner + bildname));
		}
	}


	private void kopierFeldAuf(boolean oeffnen){
		if(kopierFeldIstAuf != oeffnen){//damit nicht ständig das Icon neu geladen wird, kommt hier diese Sperre rein
			String bildname;

			if (oeffnen){
				bildname = "kopiebox2.png";
				kopierFeldIstAuf = true;
			}else{
				bildname = "kopiebox1.png";
				kopierFeldIstAuf = false;
			}

			kopierFeld.setIcon(Helpers.getIcon(AuswahlPanelElement.iconOrdner + bildname));
		}
	}
	
	
	
	public void kopiereGanzesStruktogramm(){
		setzeKopiertesStrElement(controlling.gibAktuellesStruktogramm().xmlErstellen());
	}




	//Methoden Drag ausgelöst
	//http://www.java2s.com/Code/Java/Swing-JFC/MakingaComponentDraggable.htm
	public void dragGestureRecognized(DragGestureEvent evt){//User hat angefangen ein Objekt zu ziehen

		Point mausPos = bildschirmKoordZuLokalenKoord(evt.getDragOrigin());//Kordinaten der Maus auf dem AuswahlPanel

		Object element = getComponentAt(mausPos);//Komponente ermitteln, über der die Maus ist

		if (element instanceof AuswahlPanelElement){//ist die Komponente ein AuswahlPanelElement (der User will ein neues StruktogrammElement einfügen)...

			int typ = ((AuswahlPanelElement)element).gibTyp();//Typnummer für das entsprechende StruktogrammElement ermitteln


			Transferable t = new StringSelection("n"+typ);//Drag-Daten setzen, n steht für neues Element, und typ ist der Typ des neuen Elementes

			dragSource.startDrag(evt, DragSource.DefaultCopyDrop, t, this);//Drag wird ausgelöst

		}else if((element == kopierFeld) && (kopiertesStrElement != null)){//ist die Komponente das kopierFeld und ist ein Document in kopiertesStrElement abgelegt...

			Transferable t = new StringSelection("k");//Drag-Daten setzen, k für Element aus dem kopierFeld

			dragSource.startDrag(evt, DragSource.DefaultCopyDrop, t, this);//Drag wird ausgelöst
		}

	}

	public void dragEnter(DragSourceDragEvent evt){

	}

	public void dragOver(DragSourceDragEvent evt){

	}

	public void dragExit(DragSourceEvent evt){

	}

	public void dropActionChanged(DragSourceDragEvent evt){

	}

	public void dragDropEnd(DragSourceDropEvent evt){

	}











	//Drop empfangen
	//http://www.java2s.com/Code/Java/Swing-JFC/PanelDropTarget.htm
	public void drop(DropTargetDropEvent event){


		try{
			event.acceptDrop(event.getSourceActions());


			Transferable tr = event.getTransferable();
			String dragTyp = (String)tr.getTransferData(tr.getTransferDataFlavors()[0]);//Drag & Drop Transferdaten ermitteln (das ist in diesem Programm ein Buchstabe und eventuell eine Zahl dahinter (siehe: Struktogramm.drop(...))

			Component dropUeberComponent = getComponentAt(bildschirmKoordZuLokalenKoord(event.getLocation()));
			Struktogramm str = controlling.gibAktuellesStruktogramm();

			if (dragTyp.charAt(0) == 'z'){//z -> ein Drag wurde ausgelöst, indem ein StruktogrammElement aus dem Struktogramm gezogen wurde

				if(dropUeberComponent == muelleimer){
					//ein Element wurde aus dem aktuellen Struktogramm auf den Mülleimer gezogen -> aus dem Zwischenlager des Struktogramms entfernen

					str.elementAusZwischenlagerGanzEntfernen();
					str.zeichenbereichAktualisieren();
					str.zeichne();
					str.rueckgaengigPunktSetzen();

				}else if(dropUeberComponent == kopierFeld){

					//ein Element aus dem aktuellen Struktogramm wurde auf die Kopier-Box gezogen -> xml-Abbild generieren und behalten
					setzeKopiertesStrElement(str.xmlErstellen(str.gibZwischenlagerElement()));
				}
			}

			muelleimerAuf(false);
			kopierFeldAuf(false);
			event.dropComplete(true);
		}catch (Exception e){
			e.printStackTrace();
			event.rejectDrop();
		}
	}

	public void dragExit(DropTargetEvent evt){
		muelleimerAuf(false);
		kopierFeldAuf(false);
	}

	public void dropActionChanged(DropTargetDragEvent evt){

	}

	public void dragEnter(DropTargetDragEvent evt){

	}

	public void dragOver(DropTargetDragEvent evt){
		Component tmp = getComponentAt(bildschirmKoordZuLokalenKoord(evt.getLocation())); //Komponente ermitteln, die unter der Maus ist

		muelleimerAuf(tmp == muelleimer); //wenn die Komponente unter der Maus das Mülleimer-Label ist, dann geöffneten Mülleimer zeigen, sonst den Geschlossenen
		kopierFeldAuf(tmp == kopierFeld); //wie beim Mülleimer
	}







	//die Drag & Drop Methoden liefern Mauskoordinaten für den ganzen Bildschirm, hier werden sie zu Koordinaten des AuswahlPanel konvertiert
	//siehe: http://www.tutego.de/java/articles/Absolute-Koordinaten-Swing-Element.html
	public Point bildschirmKoordZuLokalenKoord(Point bildschirmKoord){
		return new Point(bildschirmKoord.x - getX(), bildschirmKoord.y - getY());
	}



}