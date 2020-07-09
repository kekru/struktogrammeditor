package de.kekru.struktogrammeditor.view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import de.kekru.struktogrammeditor.control.Controlling;
import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.control.Struktogramm;


public class FontChooser extends JDialog {	
	//siehe: http://www.java2s.com/Code/Java/Tiny-Application/FontChooser.htm

	private static final long serialVersionUID = -7360108534182191037L;
	private String[] schriftarten;  
	private JComboBox<String> schriftartenChooser;
	private JComboBox<String> styleChooser = new JComboBox<String>(styleNames);
	private JComboBox<String> sizeChooser = new JComboBox<String>(sizeNames);
	private JButton buttonOK = new JButton();
	private JButton buttonAbbrechen = new JButton();
	private JButton buttonZuruecksetzen = new JButton();
	private Controlling controlling;


	// The names to appear in the "Style" menu
	static final String[] styleNames = new String[] { "Normal", "Kursiv",
		"Fett", "Fett und Kursiv" };

	// The style values that correspond to those names
	static final Integer[] styleValues = new Integer[] {
		Font.PLAIN, Font.ITALIC, Font.BOLD, Font.BOLD + Font.ITALIC};

	// The size "names" to appear in the size menu
	static final String[] sizeNames = new String[] { "8", "10", "12", "14", "15",
		"18", "20", "24", "28", "32", "40", "48", "56", "64", "72" };

	public FontChooser(Controlling controlling, boolean modal) {

		super(controlling.getGUI(), "Schriftart ändern", modal);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);   
		setSize(465, 120);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();    
		setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		//Container cp = getContentPane();
		//cp.setLayout(null);
		setLayout(new BorderLayout());

		this.controlling = controlling;


		schriftarten = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		schriftartenChooser = new JComboBox<String>(schriftarten);
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		{
			// Set initial values for the properties
			//schriftart = "serif";
			//style = Font.PLAIN;
			//size = 15;

			// Create ItemChooser objects that allow the user to select font
			// family, style, and size.
			schriftartenChooser.setBounds(10,10,250,20);
			panel.add(schriftartenChooser);
			styleChooser.setBounds(270,10,120,20);
			panel.add(styleChooser);
			sizeChooser.setBounds(400,10,50,20);
			panel.add(sizeChooser);
		}
		add(panel, BorderLayout.NORTH);

		fontAufChooser(controlling.gibAktuellesStruktogramm().getFont());


		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		{
			buttonOK.setBounds(10, 40, 91, 25);
			buttonOK.setText("OK");
			buttonOK.setMargin(new Insets(2, 2, 2, 2));
			buttonOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					buttonOK_ActionPerformed(evt);
				}
			});

			panel.add(buttonOK);


			buttonAbbrechen.setBounds(115, 40, 91, 25);
			buttonAbbrechen.setText("Abbrechen");
			buttonAbbrechen.setMargin(new Insets(2, 2, 2, 2));
			buttonAbbrechen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					buttonAbbrechen_ActionPerformed(evt);
				}
			});

			panel.add(buttonAbbrechen);


			buttonZuruecksetzen.setBounds(220, 40, 91, 25);
			buttonZuruecksetzen.setText("Zurücksetzen");
			buttonZuruecksetzen.setMargin(new Insets(2, 2, 2, 2));
			buttonZuruecksetzen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					buttonZuruecksetzen_ActionPerformed(evt);
				}
			});

			panel.add(buttonZuruecksetzen);
		}
		add(panel, BorderLayout.SOUTH);

		setVisible(true); //alles nach setVisible(true) wird erst beim Schließen des Dialogs gemacht
	}


	private void fontAufChooser(Font font){

		int pos = 0;
		String schriftart = font.getFamily().toLowerCase();
		for(int i=0; i < schriftarten.length; i++){
			if(schriftarten[i].toLowerCase().equals(schriftart)){
				pos = i;
				break;
			}
		}

		schriftartenChooser.setSelectedIndex(pos);

		int a = font.getStyle();
		for(int i=0; i < styleValues.length; i++){
			if(styleValues[i] == a) {
				pos = i;
				break;
			}
		}

		styleChooser.setSelectedIndex(pos);

		a = font.getSize();
		for(int i=0; i < sizeNames.length; i++) {
			if(sizeNames[i].equals("" + a)){
				pos = i;
				break;
			}
		}

		sizeChooser.setSelectedIndex(pos);
	}


	public void buttonOK_ActionPerformed(ActionEvent evt) {
		Struktogramm str = controlling.gibAktuellesStruktogramm();
		str.setFont(new Font(schriftartenChooser.getSelectedItem().toString(),styleValues[styleChooser.getSelectedIndex()],Integer.parseInt(sizeNames[sizeChooser.getSelectedIndex()])));
		str.rueckgaengigPunktSetzen(true);
		str.graphicsInitialisieren();   
		str.zeichenbereichAktualisieren();
		str.zeichne();
		setVisible(false);
	}


	public void buttonAbbrechen_ActionPerformed(ActionEvent evt) {
		setVisible(false); 
	} 


	public void buttonZuruecksetzen_ActionPerformed(ActionEvent evt) {
		fontAufChooser(GlobalSettings.fontStandard);
	}
}
