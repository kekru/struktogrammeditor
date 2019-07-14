package de.kekru.struktogrammeditor.view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import de.kekru.struktogrammeditor.control.GlobalSettings;


public class EinstellungsDialog extends JDialog{

	private static final long serialVersionUID = -6402017961524470279L;
	// Anfang Attribute
	public static final int anzahlStruktogrammElemente = 10; 
	//private GUI gui;
	//private JLabel labelUeberschrift = new JLabel();
	private JLabel[] labels = new JLabel[anzahlStruktogrammElemente];
	private JTextField[] textfields = new JTextField[anzahlStruktogrammElemente];
	private JButton buttonOK = new JButton();
	private JButton buttonAbbrechen = new JButton();
	private JButton buttonStandardWerte = new JButton();
	private final String[] beschreibungen = GlobalSettings.getCurrentElementBeschriftungsstil();
	public static final String[] standardWerte = {"Anweisung",
		"Verzweigung",
		"Fallauswahl",
		"0 < i < anzahl",
		"While Schleife",
		"Do-While Schleife",
		"Endlosschleife",
		"Aussprung",
		"Aufruf",
	"ø"};


	// Ende Attribute



	public EinstellungsDialog(GUI gui, boolean modal) {

		super(gui, "Einstellungsdialog", modal);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);   
		setSize(300, 440);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();    
		setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		//Container cp = getContentPane();
		//cp.setLayout(null);
		setLayout(new BorderLayout());
		//this.gui = gui;

		//labelUeberschrift.setBounds(10,10,280,20);
		//labelUeberschrift.setText("Beschriftungen von neu eingefügten Elementen:");
		add(new JLabel("Beschriftungen von neu eingefügten Elementen:"), BorderLayout.NORTH);

		JPanel panel = new JPanel(new GridLayout(anzahlStruktogrammElemente, 2, 4, 4));
		{

			for(int i=0; i < anzahlStruktogrammElemente; i++){
				labels[i] = new JLabel();
				labels[i].setBounds(10, 40+30*i,100,20);
				labels[i].setText(beschreibungen[i]);
				panel.add(labels[i]);

				textfields[i] = new JTextField();
				textfields[i].setBounds(120, 40+30*i,150,20);
				textfields[i].setText(GlobalSettings.gibElementBeschriftung(i));
				panel.add(textfields[i]);
			}
		}
		add(panel, BorderLayout.CENTER);
		

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		{

			buttonOK.setBounds(10, 340, 91, 25);
			buttonOK.setText("OK");
			buttonOK.setMargin(new Insets(2, 2, 2, 2));
			buttonOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					buttonOK_ActionPerformed(evt);
				}
			});

			panel.add(buttonOK);


			buttonAbbrechen.setBounds(115, 340, 91, 25);
			buttonAbbrechen.setText("Abbrechen");
			buttonAbbrechen.setMargin(new Insets(2, 2, 2, 2));
			buttonAbbrechen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					buttonAbbrechen_ActionPerformed(evt);
				}
			});

			panel.add(buttonAbbrechen);


			buttonStandardWerte.setBounds(10, 380, 115, 25);
			buttonStandardWerte.setText("Standardwerte");
			buttonStandardWerte.setMargin(new Insets(2, 2, 2, 2));
			buttonStandardWerte.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					buttonStandardWerte_ActionPerformed(evt);
				}
			});

			panel.add(buttonStandardWerte);
		}
		add(panel, BorderLayout.SOUTH);


		
		setVisible(true); //alles nach setVisible(true) wird erst beim Schließen des Dialogs gemacht


	}



	public void buttonOK_ActionPerformed(ActionEvent evt) {
		String[] neueWerte = new String[anzahlStruktogrammElemente];

		for(int i=0; i < anzahlStruktogrammElemente; i++){
			neueWerte[i] = textfields[i].getText();	 
		}

		GlobalSettings.setzeElementBeschriftungen(neueWerte); 
		GlobalSettings.saveSettings();
		setVisible(false);
	}


	public void buttonAbbrechen_ActionPerformed(ActionEvent evt) {
		setVisible(false); 
	}

	public void buttonStandardWerte_ActionPerformed(ActionEvent evt) {	 
		for(int i=0; i < anzahlStruktogrammElemente; i++){
			textfields[i].setText(standardWerte[i]);	        
		}
	}






}
