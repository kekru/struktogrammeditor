package de.kekru.struktogrammeditor.view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import de.kekru.struktogrammeditor.control.Controlling;
import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.control.Konstanten;
import de.kekru.struktogrammeditor.other.XActionCommands;

/**
 *
 * Struktogrammeditor
 *
 * @version 1.0 vom 27.03.2011
 * @author Kevin Krummenauer
 */

public class GUI extends JFrame implements Konstanten{

	private static final long serialVersionUID = -3526840402506170333L;
	private AuswahlPanel auswahlPanel; //Panel an der linken Seite, wo die Labels zu finden sind, von denen man neue StruktogrammElemente in das Struktogramm ziehen kann
	private StrTabbedPane tabbedpane; //TabbedPane, in dem die Struktogramme sind  
	private Controlling controlling;
	private final JMenuBar menubar;

	public GUI(Controlling controlling) {

		super(GlobalSettings.guiTitel);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		int frameWidth = 1016;
		int frameHeight = 522;
		setSize(frameWidth, frameHeight);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);


		this.controlling = controlling;

		//Container cp = getContentPane();
		//cp.setLayout(null);
		setLayout(new BorderLayout());

		setIconImage(new ImageIcon(getClass().getResource(GlobalSettings.logoName)).getImage());


		//setJMenuBar(new MenueLeiste(this));

		tabbedpane = new StrTabbedPane(controlling);
		//tabbedpane.setBounds(xPosTabbedPane, 0, 671, 401);
		//cp.add(tabbedpane);

		auswahlPanel = new AuswahlPanel(controlling);
		//auswahlPanel.setBounds(0,0,200,500);
		//add(auswahlPanel);

		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(auswahlPanel), tabbedpane);
		splitpane.setOneTouchExpandable(true);
		add(splitpane, BorderLayout.CENTER);

		//Struktogramm str = neuesStruktogramm();


		menubar = new JMenuBar();
		{
			JMenu menu = createMenu("Datei", KeyEvent.VK_D);
			{
				menu.add(createMenuItem("Neu", XActionCommands.neu, KeyEvent.VK_N, KeyEvent.VK_N));
				menu.add(createMenuItem("Öffnen...", XActionCommands.oeffnen, KeyEvent.VK_F, KeyEvent.VK_O));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Speichern", XActionCommands.speichern, KeyEvent.VK_S, KeyEvent.VK_S));
				menu.add(createMenuItem("Speichern unter...", XActionCommands.speicherUnter, KeyEvent.VK_U));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Als Bild speichern", XActionCommands.bildSpeichern, KeyEvent.VK_A));
				menu.add(createMenuItem("Bild in Zwischenablage kopieren", XActionCommands.bildInZwischenAblage, KeyEvent.VK_Z, KeyEvent.VK_K));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Quellcode erzeugen", XActionCommands.quellcodeErzeugen, KeyEvent.VK_Q));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Struktogramm Schließen", XActionCommands.struktogrammSchliessen, KeyEvent.VK_K));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Programm beenden",	XActionCommands.programmBeenden, KeyEvent.VK_P));
			}
			menubar.add(menu);

			menu = createMenu("Bearbeiten", KeyEvent.VK_B);
			{
				menu.add(createMenuItem("Rückgängig", XActionCommands.rueckgaengig, KeyEvent.VK_R, KeyEvent.VK_Z));
				menu.add(createMenuItem("Widerrufen", XActionCommands.widerrufen, KeyEvent.VK_W, KeyEvent.VK_Y));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Struktogrammname hinzufügen", XActionCommands.struktogrammbeschreibungHinzufuegen, KeyEvent.VK_S));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Ganzes Struktogramm kopieren", XActionCommands.ganzesStruktogrammKopieren, KeyEvent.VK_G));
				menu.add(createMenuItem("Element unter der Maus kopieren", XActionCommands.elementUnterDerMausKopieren, KeyEvent.VK_K, KeyEvent.VK_C));
				menu.add(createMenuItem("Kopiertes Element an Mausposition einfügen", XActionCommands.elementEinfuegen, KeyEvent.VK_E, KeyEvent.VK_V));
			}
			menubar.add(menu);

			menu = createMenu("Einstellungen", KeyEvent.VK_E);
			{				
				menu.add(createMenuItem("Jeweils letztes Element bei Bedarf strecken", XActionCommands.letztesElementStrecken, KeyEvent.VK_J, GlobalSettings.gibLetzteElementeStrecken()));
				menu.add(new JSeparator());

				JMenu menu2 = createMenu("Beschriftungsstil", KeyEvent.VK_B);
				{
					ButtonGroup group = new ButtonGroup();

					JRadioButtonMenuItem radioMenuitem = new JRadioButtonMenuItem("Java-ähnlich");
					radioMenuitem.addActionListener(controlling);
					radioMenuitem.setActionCommand(XActionCommands.beschriftungsStilJava.toString());
					radioMenuitem.setSelected(GlobalSettings.getBeschriftungsStilAktuell() == beschriftungsStilJava);
					group.add(radioMenuitem);
					menu2.add(radioMenuitem);

					radioMenuitem = new JRadioButtonMenuItem("Allgemein");
					radioMenuitem.addActionListener(controlling);
					radioMenuitem.setActionCommand(XActionCommands.beschriftungsStilFormal.toString());
					radioMenuitem.setSelected(GlobalSettings.getBeschriftungsStilAktuell() == beschriftungsStilFormal);
					group.add(radioMenuitem);
					menu2.add(radioMenuitem);

					radioMenuitem = new JRadioButtonMenuItem("Element Shortcuts");
					radioMenuitem.addActionListener(controlling);
					radioMenuitem.setActionCommand(XActionCommands.beschriftungsStilKeineBeschriftungen.toString());
					radioMenuitem.setSelected(GlobalSettings.getBeschriftungsStilAktuell() == beschriftungsStilKeineBeschriftungen);
					group.add(radioMenuitem);
					menu2.add(radioMenuitem);
				}
				menu.add(menu2);
				
				menu2 = createMenu("Look and Feel", KeyEvent.VK_L);
				{
					ButtonGroup group = new ButtonGroup();

					JRadioButtonMenuItem radioMenuitem = new JRadioButtonMenuItem("Betriebssystem Standard");
					radioMenuitem.addActionListener(controlling);
					radioMenuitem.setActionCommand(XActionCommands.lookAndFeelOSStandard.toString());
					radioMenuitem.setSelected(GlobalSettings.getLookAndFeelAktuell() == lookAndFeelOSStandard);
					group.add(radioMenuitem);
					menu2.add(radioMenuitem);

					radioMenuitem = new JRadioButtonMenuItem("Java Swing Standard");
					radioMenuitem.addActionListener(controlling);
					radioMenuitem.setActionCommand(XActionCommands.lookAndFeelSwingStandard.toString());
					radioMenuitem.setSelected(GlobalSettings.getLookAndFeelAktuell() == lookAndFeelSwingStandard);
					group.add(radioMenuitem);
					menu2.add(radioMenuitem);

					radioMenuitem = new JRadioButtonMenuItem("Nimbus");
					radioMenuitem.addActionListener(controlling);
					radioMenuitem.setActionCommand(XActionCommands.lookAndFeelNimbus.toString());
					radioMenuitem.setSelected(GlobalSettings.getLookAndFeelAktuell() == lookAndFeelNimbus);
					group.add(radioMenuitem);
					menu2.add(radioMenuitem);
					
					radioMenuitem = new JRadioButtonMenuItem("Motif");
					radioMenuitem.addActionListener(controlling);
					radioMenuitem.setActionCommand(XActionCommands.lookAndFeelMotif.toString());
					radioMenuitem.setSelected(GlobalSettings.getLookAndFeelAktuell() == lookAndFeelMotif);
					group.add(radioMenuitem);
					menu2.add(radioMenuitem);
				}
				menu.add(menu2);

				menu.add(createMenuItem("Startbeschriftungen ändern...", XActionCommands.startbeschriftungAendern, KeyEvent.VK_S));
				menu.add(createMenuItem("Schriftart ändern...", XActionCommands.schriftartAendern, KeyEvent.VK_F));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Größe von Elementen mit Mausrad ändern", XActionCommands.groesseAendernMitMausrad, KeyEvent.VK_G, GlobalSettings.isBeiMausradGroesseAendern()));
				menu.add(createMenuItem("Zoomeinstellungen...", XActionCommands.zoomeinstellungen, KeyEvent.VK_Z));
				menu.add(createMenuItem("Alle Vergrößerungen rückgängig machen", XActionCommands.vergroesserungenRuckgaengigMachen, KeyEvent.VK_A));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Shortcuts zum Elementeinfügen benutzen", XActionCommands.elementShortcutsVerwenden, KeyEvent.VK_C, GlobalSettings.isElementShortcutsVerwenden()));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Kantenglättung verwenden", XActionCommands.kantenglaettungVerwenden, KeyEvent.VK_K, GlobalSettings.isKantenglaettungVerwenden()));
			}
			menubar.add(menu);

			menu = createMenu("Hilfe", KeyEvent.VK_H);
			{
				menu.add(createMenuItem("Homepage", XActionCommands.homepage, KeyEvent.VK_M));
				menu.add(createMenuItem("Changelog", XActionCommands.changelog, KeyEvent.VK_C));
				menu.add(createMenuItem("Kontakt, Feedback, Verbesserungsvorschläge, Fehler melden", XActionCommands.kontaktformular, KeyEvent.VK_K));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Hilfedatei", XActionCommands.hilfe, KeyEvent.VK_L));
				menu.add(new JSeparator());
				menu.add(createMenuItem("Info", XActionCommands.info, KeyEvent.VK_I));
				menu.add(createMenuItem("Quellcode", XActionCommands.sourceCode, KeyEvent.VK_Q));
			}
			menubar.add(menu);
		}
		setJMenuBar(menubar);



		addWindowListener(controlling);

		setResizable(true);
		setVisible(true);
		//addComponentListener(this);




		//tabbedpane.changeListenerAktivieren(); //wenn der ChangeListener früher aktiviert wird, kommt es zu Problemen, da darin graphicsInitialisieren aufgerufen wird, was nicht funktioniert wenn die entsprechende Komponente noch nicht vollständig erzeugt ist


	}


	private JMenu createMenu(String name, int auswahlBuchstabe){
		JMenu neuMenu = new JMenu(name);
		neuMenu.setMnemonic(auswahlBuchstabe);
		return neuMenu;
	}

	private JMenuItem createMenuItem(String name, XActionCommands actionCommand, int auswahlBuchstabe){
		return createMenuItem(name, actionCommand, auswahlBuchstabe, -1, -1);
	}

	private JMenuItem createMenuItem(String name, XActionCommands actionCommand, int auswahlBuchstabe, int shortcutBuchstabe){
		return createMenuItem(name, actionCommand, auswahlBuchstabe, shortcutBuchstabe, GlobalSettings.strgOderApfelMask, false, false);
	}

	private JMenuItem createMenuItem(String name, XActionCommands actionCommand, int auswahlBuchstabe, int shortcutBuchstabe, int shortcutMask){
		return createMenuItem(name, actionCommand, auswahlBuchstabe, shortcutBuchstabe, shortcutMask, false, false);
	}

	private JMenuItem createMenuItem(String name, XActionCommands actionCommand, int auswahlBuchstabe, boolean isChecked){
		return createMenuItem(name, actionCommand, auswahlBuchstabe, -1, -1, true, isChecked);
	}

	private JMenuItem createMenuItem(String name, XActionCommands actionCommand, int auswahlBuchstabe, int shortcutBuchstabe, int shortcutMask, boolean isCheckBox, boolean isChecked){
		JMenuItem menuitem;

		if(isCheckBox){
			menuitem = new JCheckBoxMenuItem(name);
			((JCheckBoxMenuItem)menuitem).setSelected(isChecked);
		}else{
			menuitem = new JMenuItem(name);
		}

		menuitem.setActionCommand(actionCommand.toString());

		if(auswahlBuchstabe > -1){
			menuitem.setMnemonic(auswahlBuchstabe);
		}

		if(shortcutBuchstabe > -1){
			menuitem.setAccelerator(KeyStroke.getKeyStroke(shortcutBuchstabe, shortcutMask));
		}

		menuitem.addActionListener(controlling);

		return menuitem;
	}


	public StrTabbedPane gibTabbedpane(){
		return tabbedpane;
	}


	public AuswahlPanel gibAuswahlPanel(){
		return auswahlPanel;
	}


	public JMenuBar getMenubar() {
		return menubar;
	}

}
