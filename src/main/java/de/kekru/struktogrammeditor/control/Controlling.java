package de.kekru.struktogrammeditor.control;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.sun.java.swing.plaf.motif.MotifLookAndFeel;

import de.kekru.struktogrammeditor.other.Helpers;
import de.kekru.struktogrammeditor.other.XActionCommands;
import de.kekru.struktogrammeditor.struktogrammelemente.AnweisungsTyp;
import de.kekru.struktogrammeditor.struktogrammelemente.StruktogrammElement;
import de.kekru.struktogrammeditor.view.CodeErzeuger;
import de.kekru.struktogrammeditor.view.EinstellungsDialog;
import de.kekru.struktogrammeditor.view.FontChooser;
import de.kekru.struktogrammeditor.view.GUI;
import de.kekru.struktogrammeditor.view.ZoomEinstellungen;

//Necessary because NotifyLookAndFeel is now restricted 
@SuppressWarnings("restriction")
public class Controlling implements Konstanten, ActionListener, WindowListener, KeyListener {

	private GUI gui;

	public Controlling(String[] params) {
		handleOSSettingsAndLookAndFeel();

		gui = new GUI(this);
		neuesStruktogramm();

		if (params != null && params.length > 0) {
			Stream.of(params).filter((f) -> new File(f).exists()).forEach((s) -> openStruktogramm(new File(s)));
		}
	}

	public void handleOSSettingsAndLookAndFeel() {
		try {
			LookAndFeel lookAndFeel = null;

			switch (GlobalSettings.getLookAndFeelAktuell()) {
			case lookAndFeelOSStandard:
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				break;

			case lookAndFeelNimbus:
				lookAndFeel = new NimbusLookAndFeel();
				break;

			case lookAndFeelMotif:
				lookAndFeel = new MotifLookAndFeel();
				break;
			}

			if (lookAndFeel != null) {
				try {
					UIManager.setLookAndFeel(lookAndFeel);// Standard Swing Look And Feel
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
			}

			if (isMac()) {
				new MacHandler(this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the current Computer is a Mac
	 * 
	 * @return
	 */
	private static boolean isMac() {
		return OS.mac;
	}

	public Struktogramm gibAktuellesStruktogramm() {
		return gui.gibTabbedpane().gibAktuellesStruktogramm();
	}

	public Struktogramm neuesStruktogramm() {
		Struktogramm str = gui.gibTabbedpane().struktogrammHinzufuegen();
		str.graphicsInitialisieren();// erst nach setVisible(true); sonst gibt es Probleme in
										// Struktogramm.graphicsInitialisieren()
		str.zeichenbereichAktualisieren();
		str.zeichne();
		return str;
	}

	public void speichern(boolean neuenSpeicherpfadAuswaehlenLassen) {
		Struktogramm str = gibAktuellesStruktogramm();
		if (str != null) {
			GlobalSettings.setzeSpeicherpfad(
					str.speichern(neuenSpeicherpfadAuswaehlenLassen, GlobalSettings.getZuletztGenutzterSpeicherpfad()));// Struktogramm
																														// wird
																														// gespeichert
																														// (zuletztGenutzterSpeicherpfad
																														// wird
																														// dabei
																														// übergeben,
																														// damit
																														// der
																														// JFileChooser,
																														// sofern
																														// er
																														// genutzt
																														// wird,
																														// dort
																														// startet)
																														// und
																														// der
																														// neue
																														// Speicherpfad
																														// wird
																														// gesichert
			GlobalSettings.saveSettings();
			titelleisteAktualisieren();
		}
	}

	/**
	 * Loads a Struktogramm
	 */
	public void laden() {
		// Parameter ist der Startordner für den OpenDialog
		File pfad = Struktogramm.oeffnenDialog(GlobalSettings.getZuletztGenutzterSpeicherpfad(), gui);
		if (pfad != null && !pfad.getName().isEmpty()) {
			openStruktogramm(pfad);
		}
	}

	private void openStruktogramm(File pfad) {
		Struktogramm str = neuesStruktogramm();
		str.graphicsInitialisieren();
		str.laden(pfad);
		GlobalSettings.setzeSpeicherpfad(pfad);
		GlobalSettings.saveSettings();
		titelleisteAktualisieren();
	}

	public void bildSpeichern() {
		JFileChooser file = new JFileChooser();
		file.setFileFilter(new FileNameExtensionFilter("PNG Bild", "png"));
		int ret = file.showSaveDialog(gui);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File f = file.getSelectedFile();
			gibAktuellesStruktogramm().printToPngFile(f);
		} else {
			JOptionPane.showMessageDialog(gui, "Keine Datei ausgewählt oder abgebrochen", "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void titelleisteAktualisieren() {
		File pfad = null;

		Struktogramm str = gibAktuellesStruktogramm();
		if (str != null) {
			pfad = str.gibAktuellenSpeicherpfad();
			if (pfad != null) {
				// wenn das aktuelle Struktogramm gespeichert oder geladen wurde, so wird sein Speicherpfad in der Titelleiste angezeigt
				gui.setTitle(GlobalSettings.guiTitel + " [" + pfad.getAbsolutePath() + "]"); 
			} else {
				gui.setTitle(GlobalSettings.guiTitel + " [Ungespeichertes Struktogram]");
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (XActionCommands.valueOf(e.getActionCommand())) {
		case neu:
			neuesStruktogramm();
			break;

		case oeffnen:
			laden();
			break;

		case speichern:
			speichern(false);
			break;

		case speicherUnter:
			speichern(true);
			break;

		case bildSpeichern:
			bildSpeichern();
			break;

		case bildInZwischenAblage:
			copyImagetoClipBoard(gibAktuellesStruktogramm().generateImage(false));
			break;

		case quellcodeErzeugen:
			new CodeErzeuger(gui, "Quellcode erzeugen", true, gibAktuellesStruktogramm());
			break;

		case struktogrammSchliessen:
			gui.gibTabbedpane().aktuellesStruktogrammschliessen();
			break;

		case programmBeenden:
			programmBeendenGeklickt();
			break;

		case rueckgaengig:
			gibAktuellesStruktogramm().schrittZurueck();
			break;

		case widerrufen:
			gibAktuellesStruktogramm().schrittNachVorne();
			break;

		case ganzesStruktogrammKopieren:
			gui.gibAuswahlPanel().kopiereGanzesStruktogramm();
			break;

		case elementUnterDerMausKopieren:
			kopiereElement();
			break;

		case elementEinfuegen:
			gibAktuellesStruktogramm().elementAusKopierFeldEinfuegenAnMausPos();
			break;

		case letztesElementStrecken:
			letzteElementeStreckenGeklickt(e.getSource());
			break;

		case startbeschriftungAendern:
			new EinstellungsDialog(gui, true);
			break;

		case schriftartAendern:
			new FontChooser(this, true);
			break;

		case groesseAendernMitMausrad:
			mitMausradElementeVergroessernGeklickt(e.getSource());
			break;

		case zoomeinstellungen:
			new ZoomEinstellungen(gui);
			break;

		case vergroesserungenRuckgaengigMachen:
			gibAktuellesStruktogramm().zoomsZuruecksetzen();
			break;

		case elementShortcutsVerwenden:
			elementEinfuegenShortcutsVerwendenGeklickt(e.getSource());
			break;

		case kantenglaettungVerwenden:
			kantenglaettungVerwendenGeklickt(e.getSource());
			break;

		case homepage:
			Helpers.openWebsite("http://whiledo.de/index.php?p=struktogrammeditor");
			break;

		case changelog:
			Helpers.openWebsite("http://strukt.whiledo.de/changelog.html");
			break;

		case hilfe:
			Helpers.openWebsite("http://strukt.whiledo.de/hilfe.html");
			break;

		case sourceCode:
			Helpers.openWebsite("https://github.com/kekru/struktogrammeditor/tree/" + GlobalSettings.buildInfoGitHash);
			break;

		case info:
			showInfo();
			break;

		case beschriftungsStilJava:
			changeBeschriftungsStil(beschriftungsStilJava);
			break;

		case beschriftungsStilFormal:
			changeBeschriftungsStil(beschriftungsStilFormal);
			break;

		case beschriftungsStilKeineBeschriftungen:
			changeBeschriftungsStil(beschriftungsStilKeineBeschriftungen);
			break;

		case lookAndFeelOSStandard:
			changeLookAndFeel(lookAndFeelOSStandard);
			break;

		case lookAndFeelSwingStandard:
			changeLookAndFeel(lookAndFeelSwingStandard);
			break;

		case lookAndFeelNimbus:
			changeLookAndFeel(lookAndFeelNimbus);
			break;

		case lookAndFeelMotif:
			changeLookAndFeel(lookAndFeelMotif);
			break;

		case struktogrammbeschreibungHinzufuegen:
			addStruktogrammbeschriftung();
			break;
		case drucken:
			PrinterJob p = PrinterJob.getPrinterJob();
			BufferedImage img = gibAktuellesStruktogramm().generateImage(true);
			ImagePrintable printable = new ImagePrintable(p.defaultPage(), img);

			p.setPrintable(printable);
			if (p.printDialog()) {
				try {
					p.print();
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(gui, "Drucken abgebrochen", "Drucken", JOptionPane.INFORMATION_MESSAGE);
			}
			break;

		}
	}

	private void addStruktogrammbeschriftung() {
		Struktogramm str = gibAktuellesStruktogramm();
		String s = JOptionPane.showInputDialog("Beschriftung", str.getStruktogrammBeschreibung());
		if (s == null) {
			return;
		}
		str.setStruktogrammBeschreibung(s);
		str.rueckgaengigPunktSetzen();
		str.zeichenbereichAktualisieren();
		str.zeichne();
	}

	private void kopiereElement() {
		StruktogrammElement element = gibAktuellesStruktogramm().getElementUnterMaus();

		if (element != null) {
			getGUI().gibAuswahlPanel().setzeKopiertesStrElement(gibAktuellesStruktogramm().xmlErstellen(element));
		}
	}

	private void changeLookAndFeel(int beschriftungsStilIndex) {
		GlobalSettings.setLookAndFeelAktuell(beschriftungsStilIndex);
		GlobalSettings.saveSettings();
		JOptionPane.showMessageDialog(gui, "Die Änderungen werden beim Neustart des Programms wirksam.",
				"Look And Feel Änderung", JOptionPane.INFORMATION_MESSAGE);
	}

	private void changeBeschriftungsStil(int beschriftungsStilIndex) {
		GlobalSettings.setBeschriftungsStilAktuell(beschriftungsStilIndex);
		GlobalSettings.saveSettings();
		gui.gibAuswahlPanel().aktualisiereBeschriftungen();
	}

	public void showInfo() {
		final String separator = System.getProperty("line.separator");

		JOptionPane.showMessageDialog(gui,
				"Struktogrammeditor " + GlobalSettings.versionsString + separator + "Kevin Krummenauer - 2011-2012"
						+ separator + "Informatik Projekt Stufe 13.2, AMG Beckum, Februar/März 2011" + separator
						+ separator + "Git Hash: " + GlobalSettings.buildInfoGitHash + separator + "Build-Zeit: "
						+ GlobalSettings.buildInfoBuildTime + separator +
						// "Updates:"+separator+
						// datumsfolge+separator+
						separator
						+ "This product includes software developed by the JDOM Project (http://www.jdom.org/).",
				"Information - Struktogrammeditor " + GlobalSettings.versionsString, JOptionPane.INFORMATION_MESSAGE);
	}

	private void letzteElementeStreckenGeklickt(Object source) {
		GlobalSettings.setzeLetzteElementeStrecken(((JCheckBoxMenuItem) source).isSelected());
		GlobalSettings.saveSettings();
		gibAktuellesStruktogramm().zeichenbereichAktualisieren();
		gibAktuellesStruktogramm().zeichne();
	}

	private void mitMausradElementeVergroessernGeklickt(Object source) {
		boolean einOderAus = ((JCheckBoxMenuItem) source).isSelected();
		GlobalSettings.setBeiMausradGroesseAendern(einOderAus);
		GlobalSettings.saveSettings();
		gibAktuellesStruktogramm().mausradScrollEinOderAusschalten(einOderAus);
		gibAktuellesStruktogramm().zeichenbereichAktualisieren();
		gibAktuellesStruktogramm().zeichne();
	}

	private void elementEinfuegenShortcutsVerwendenGeklickt(Object source) {
		boolean einOderAus = ((JCheckBoxMenuItem) source).isSelected();
		GlobalSettings.setElementShortcutsVerwenden(einOderAus);
		GlobalSettings.saveSettings();
	}

	private void kantenglaettungVerwendenGeklickt(Object source) {
		GlobalSettings.setKantenglaettungVerwenden(((JCheckBoxMenuItem) source).isSelected());
		GlobalSettings.saveSettings();
		gibAktuellesStruktogramm().graphicsInitialisieren();
		gibAktuellesStruktogramm().zeichenbereichAktualisieren();
		gibAktuellesStruktogramm().zeichne();
	}

	public GUI getGUI() {
		return gui;
	}

	public void programmBeendenGeklickt() {
		if (gui.gibTabbedpane().einOderMehrereStruktogrammeNichtGespeichert()) {
			Object[] options = { "Ja", "Nein" }; // http://download.oracle.com/javase/1.4.2/docs/api/javax/swing/JOptionPane.html
			if (0 == JOptionPane.showOptionDialog(gui,
					"Ein oder mehrere Struktogramme wurden noch nicht gespeichert.\nWirklich Beenden ohne zu speichern?",
					"Noch nicht gespeichert", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
					options[1])) {
				System.exit(0);
			}
		} else {
			System.exit(0);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		programmBeendenGeklickt();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == gui.gibTabbedpane() && e.getModifiersEx() != GlobalSettings.strgOderApfelMask) {

			switch (e.getKeyChar()) {
			case '+':
				gibAktuellesStruktogramm().zoomAktuellesElement(true);
				break;

			case '-':
				gibAktuellesStruktogramm().zoomAktuellesElement(false);
				break;
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == gui.gibTabbedpane() && e.getModifiersEx() != GlobalSettings.strgOderApfelMask) {

			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				if (GlobalSettings.isElementShortcutsVerwenden()) {// AnweisungsTyp.Verzweigung
					gibAktuellesStruktogramm().neuesElementAnAktuellerStelleEinfuegen(AnweisungsTyp.Anweisung);
				}
				break;

			case KeyEvent.VK_I:
				if (GlobalSettings.isElementShortcutsVerwenden()) {
					gibAktuellesStruktogramm().neuesElementAnAktuellerStelleEinfuegen(AnweisungsTyp.Verzweigung);
				}
				break;

			case KeyEvent.VK_S:
				if (GlobalSettings.isElementShortcutsVerwenden()) {
					gibAktuellesStruktogramm().neuesElementAnAktuellerStelleEinfuegen(AnweisungsTyp.Fallauswahl);
				}
				break;

			case KeyEvent.VK_F:
				if (GlobalSettings.isElementShortcutsVerwenden()) {
					gibAktuellesStruktogramm().neuesElementAnAktuellerStelleEinfuegen(AnweisungsTyp.ForSchleife);
				}
				break;

			case KeyEvent.VK_W:
				if (GlobalSettings.isElementShortcutsVerwenden()) {
					gibAktuellesStruktogramm().neuesElementAnAktuellerStelleEinfuegen(AnweisungsTyp.WhileSchleife);
				}
				break;

			case KeyEvent.VK_D:
				if (GlobalSettings.isElementShortcutsVerwenden()) {
					gibAktuellesStruktogramm().neuesElementAnAktuellerStelleEinfuegen(AnweisungsTyp.DoUntilSchleife);
				}
				break;

			case KeyEvent.VK_E:
				if (GlobalSettings.isElementShortcutsVerwenden()) {
					gibAktuellesStruktogramm().neuesElementAnAktuellerStelleEinfuegen(AnweisungsTyp.Endlosschleife);
				}
				break;

			case KeyEvent.VK_B:
				if (GlobalSettings.isElementShortcutsVerwenden()) {
					gibAktuellesStruktogramm().neuesElementAnAktuellerStelleEinfuegen(AnweisungsTyp.Aussprung);
				}
				break;

			case KeyEvent.VK_M:
				if (GlobalSettings.isElementShortcutsVerwenden()) {
					gibAktuellesStruktogramm().neuesElementAnAktuellerStelleEinfuegen(AnweisungsTyp.Aufruf);
				}
				break;

			case KeyEvent.VK_DELETE:
				gibAktuellesStruktogramm().elementAnAktuellerStelleLoeschen();
				break;
			}
		}

	}

	// http://stackoverflow.com/questions/4552045/copy-bufferedimage-to-clipboard
	public static void copyImagetoClipBoard(final BufferedImage image) {

		Transferable transferable = new Transferable() {

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (flavor.equals(DataFlavor.imageFlavor) && image != null) {
					return image;
				} else {
					throw new UnsupportedFlavorException(flavor);
				}
			}

			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				DataFlavor[] flavors = getTransferDataFlavors();
				for (DataFlavor f : flavors) {
					if (flavor.equals(f)) {
						return true;
					}
				}

				return false;
			}
		};

		ClipboardOwner clipboardOwner = new ClipboardOwner() {
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {

			}
		};

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, clipboardOwner);

	}
}