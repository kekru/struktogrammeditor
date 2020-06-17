package de.kekru.struktogrammeditor.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.control.Struktogramm;
import de.kekru.struktogrammeditor.other.JNumberField;
import de.kekru.struktogrammeditor.other.JTextAreaEasy;
import de.kekru.struktogrammeditor.other.SupportedLanguages;

//JDialog, in dem aus dem Struktogramm wahlweise Java oder Delphi Quellcode generiert wird
public class CodeErzeuger extends JDialog {

	private static final long serialVersionUID = 6073577055724789562L;
	// Anfang Attribute
	private ButtonGroup buttongroup = new ButtonGroup();// ButtonGroup für die RadioButtons
	private JRadioButton javaButton = new JRadioButton();// RadioButton zum auswählen von Java-Quellcode
	private JRadioButton delphiButton = new JRadioButton();// RadioButton zum auswählen von Delphi-Quellcode
	private JRadioButton phpButton = new JRadioButton();
	private JTextAreaEasy textarea;// Textarea zum Ausgeben des Quellcodes
	private JCheckBox checkboxKommentare = new JCheckBox();// Checkbox, um festzulegen, ob die Bechriftungen der
															// StruktogrammElemente als Kommentare oder einfach so in
															// den generierten Quellcode eingefügt werden sollen
	private JLabel jLabel1 = new JLabel();// Label für die Beschriftung von numberfieldEinrueckung
	private JNumberField numberfieldEinrueckung = new JNumberField();// gibt an, wie weit die erste Zeile im generierten
	// Quellcode eingerückt werden soll
	private JLabel jLabel2 = new JLabel();// Label für die Beschriftung von numberfieldZeichenzahl
	private JNumberField numberfieldZeichenzahl = new JNumberField();// gibt an, wie viele Leerzeichen pro Einrückung
																		// genutzt werden sollen
	private JButton buttonCodeErzeugen = new JButton();// Button um das Quellcode-Erzeugen zu starten
	private JButton buttonSchliessen = new JButton();// Button zum Schließen
	private Struktogramm str;// das Struktogramm, zu dem Quellcode generiert werden soll
	// Ende Attribute

	public CodeErzeuger(JFrame owner, String title, boolean modal, Struktogramm str) {
		// Vom Java-Editor erzeugter JDialog-Code
		super(owner, title, modal);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		int frameWidth = 498;
		int frameHeight = 437;
		setSize(frameWidth, frameHeight);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);
		Container cp = getContentPane();
		cp.setLayout(null);

		javaButton.setBounds(16, 240, 113, 17);
		javaButton.setText("Java Code");
		cp.add(javaButton);
		delphiButton.setBounds(16, 264, 113, 17);
		delphiButton.setText("Pascal Code");
		cp.add(delphiButton);
		phpButton.setBounds(16, 286, 113, 17);
		phpButton.setText("PHP Code");
		cp.add(phpButton);
		
		checkboxKommentare.setBounds(16, 216, 329, 17);
		checkboxKommentare.setText("Struktogramminhalt als Kommentare ausgeben");
		checkboxKommentare.setSelected(GlobalSettings.isCodeErzeugerAlsKommentar());
		cp.add(checkboxKommentare);
		jLabel1.setBounds(16, 316, 323, 16);
		jLabel1.setText("Erste Zeile soll wie viele Stellen weit eingerückt sein?");
		jLabel1.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
		cp.add(jLabel1);
		numberfieldEinrueckung.setBounds(344, 296, 49, 24);
		numberfieldEinrueckung.setText("" + GlobalSettings.getCodeErzeugerEinrueckungGesamt());
		cp.add(numberfieldEinrueckung);
		jLabel2.setBounds(16, 338, 244, 16);
		jLabel2.setText("Anzahl der Leerzeichen pro Einrückung:");
		jLabel2.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
		cp.add(jLabel2);
		numberfieldZeichenzahl.setBounds(344, 328, 49, 24);
		numberfieldZeichenzahl.setText("" + GlobalSettings.getCodeErzeugerEinrueckungProStufe());
		cp.add(numberfieldZeichenzahl);
		buttonCodeErzeugen.setBounds(16, 368, 147, 25);
		buttonCodeErzeugen.setText("Code erzeugen");
		buttonCodeErzeugen.setMargin(new Insets(2, 2, 2, 2));
		buttonCodeErzeugen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonCodeErzeugen_ActionPerformed(evt);
			}
		});
		cp.add(buttonCodeErzeugen);
		buttonSchliessen.setBounds(296, 368, 91, 25);
		buttonSchliessen.setText("Schließen");
		buttonSchliessen.setMargin(new Insets(2, 2, 2, 2));
		buttonSchliessen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonSchliessen_ActionPerformed(evt);
			}
		});
		cp.add(buttonSchliessen);

		// RadioButtons der ButtonGroup hinzufügen
		buttongroup.add(javaButton);
		buttongroup.add(delphiButton);
		buttongroup.add(phpButton);
		
		if (GlobalSettings.getCodeErzeugerProgrammiersprache() == SupportedLanguages.Java) {
			javaButton.setSelected(true);
		} else if (GlobalSettings.getCodeErzeugerProgrammiersprache() == SupportedLanguages.Delphi) {
			delphiButton.setSelected(true);
		} else {
			phpButton.setSelected(true);
		}

		checkboxKommentare.setSelected(GlobalSettings.isCodeErzeugerAlsKommentar());

		textarea = new JTextAreaEasy(8, 10, 480, 200);
		textarea.setzeFont(new Font("Monospaced", Font.PLAIN, 15));
		textarea.setzeContainer(cp);

		this.str = str;

		setResizable(false);
		setVisible(true);

		// Anfang Komponenten
		// Ende Komponenten
	}

	// Anfang Methoden

	public JRadioButton getSelectedRadioButton(ButtonGroup bg) {// vom Java Editor generiert, ein bisschen modifiziert
		for (Enumeration<AbstractButton> e = bg.getElements(); e.hasMoreElements();) {
			AbstractButton b = e.nextElement();
			if (b.isSelected())
				return (JRadioButton) b;
		}
		return null;
	}

	// Button Code erzeugen wurde angeklickt
	public void buttonCodeErzeugen_ActionPerformed(ActionEvent evt) {
		if (numberfieldEinrueckung.isNumeric() && numberfieldZeichenzahl.isNumeric()) {// Prüfen, ob in den NumberFields
																						// gültige Zahlen stehen

			JRadioButton radioB = getSelectedRadioButton(buttongroup);

			SupportedLanguages typ = SupportedLanguages.Java;

			if (radioB == javaButton) {
				typ = SupportedLanguages.Java;
			} else if (radioB == delphiButton) {
				typ = SupportedLanguages.Delphi;
			} else if (radioB == phpButton) {
				typ = SupportedLanguages.PHP;
			}

			textarea.leeren();

			final int einrueckung = numberfieldEinrueckung.getInt();
			final int einrueckungProStufe = numberfieldZeichenzahl.getInt();
			final boolean alsKommentar = checkboxKommentare.isSelected();
			
			str.gibListe().quellcodeAllerUnterelementeGenerieren(typ, einrueckung, einrueckungProStufe, alsKommentar,
					textarea);// Quellcode generieren

			GlobalSettings.setCodeErzeugerEinrueckungGesamt(einrueckung);
			GlobalSettings.setCodeErzeugerEinrueckungProStufe(einrueckungProStufe);
			GlobalSettings.setCodeErzeugerProgrammiersprache(typ);
			GlobalSettings.setCodeErzeugerAlsKommentar(alsKommentar);
			GlobalSettings.saveSettings();
		} else {
			JOptionPane.showMessageDialog(null, "Bitte Ganzzahlen in die Textfelder eingeben", "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// gibt an, welche Symbole in der jeweiligen Sprache für Kommentare genutzt
	// werden
	public static String gibKommentarZeichen(boolean kommentarStart, SupportedLanguages codeTyp) {
		switch (codeTyp) {
		case Java:
			if (kommentarStart) {
				return "/*";
			} else {
				return "*/";
			}
		case Delphi:
			if (kommentarStart) {
				return "{";
			} else {
				return "}";
			}
		case PHP:
			if (kommentarStart) {
				return "/*";
			} else {
				return "*/";
			}
		}

		return "";
	}

	/** Gibt an, welche Zeichen in der jeweiligen Sprache für Befehlsblockanfang und
	 -ende genutzt werden */
	public static String gibBlockZeichen(boolean blockStart, SupportedLanguages codeTyp) {
		switch (codeTyp) {
		case Java:
			if (blockStart) {
				return "{";
			} else {
				return "}";
			}
		case Delphi:
			if (blockStart) {
				return "begin";
			} else {
				return "end;";
			}
		case PHP:
			if (blockStart) {
				return "{";
			} else {
				return "}";
			}
		}
		return "";
	}

	// bei Delphi setze ich das begin in die nächste Zeile, es muss daher eingerückt
	// werden
	public static boolean mussBlockanfangEinruecken(SupportedLanguages typ) {
		return typ == SupportedLanguages.Delphi;
	}

	public void buttonSchliessen_ActionPerformed(ActionEvent evt) {
		setVisible(false);
	}
	// Ende Methoden

}
