package de.kekru.struktogrammeditor.control;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import de.kekru.struktogrammeditor.other.Helpers;
import de.kekru.struktogrammeditor.other.SupportedLanguages;
import de.kekru.struktogrammeditor.struktogrammelemente.AnweisungsTyp;
import de.kekru.struktogrammeditor.view.EinstellungsDialog;

public class GlobalSettings implements Konstanten{

	public static final int updateNummer = 9;
	public static String versionsString = "";
	public static String guiTitel = "";
	public static final String[] updateDaten = {"30.05.2011", "31.05.2011", "05.06.2011", "11.09.2011", "18.01.2012", "17.02.2012", "02.05.2012", "16.08.2012", "13.05.2014", "10.07.2014", "03.06.2020"};
	
	public static final String logoName = "/icons/logostr.png";
	
	public static final String BUILDINFO_FILE = "/build.properties";
	public static String buildInfoGitHash = "";
	public static String buildInfoBuildTime = "";
	
	private static final String[][] elementAuswahlBeschriftungen = {
		{"Anweisung","if (Verzweigung)","switch (Auswahl)","For Schleife","While Schleife","Do-While Schleife","Endlosschleife","Aussprung","Aufruf","Leeres Element"},
		{"Anweisung","Verzweigung","Fallauswahl","Zählergesteuerte Schleife","Kopfgesteuerte Schleife","Fußgesteuerte Schleife","Endlosschleife","Aussprung","Aufruf","Leeres Element"},
		{"A","I","S","F","W","D","E","B","M","Leeres Element"}
	};
	private static int beschriftungsStilAktuell = 1;
	private static int lookAndFeelAktuell = 0;

	private static File zuletztGenutzterSpeicherpfad = new File("");
	private static File zuletztGenutzterPfadFuerBild = new File("");
	private static boolean letzteElementeStrecken = false;
	private static List<String> elementBeschriftungenZumEinfuegenInDasStruktogramm = new ArrayList<>();
	public static final Font fontStandard = new Font("serif", Font.PLAIN, 15);
	private static final File einstellungsDatei = new File("struktogrammeditor.properties");
	private static final File einstellungsDateiBisVersion1Punkt4 = new File("StruktogrammeditorEinstellungen.txt");
	
	private static int codeErzeugerEinrueckungGesamt = 3;
	private static int codeErzeugerEinrueckungProStufe = 3;
	private static SupportedLanguages codeErzeugerProgrammiersprache = SupportedLanguages.Java;
	private static boolean codeErzeugerAlsKommentar = true;
	
	private static boolean beiMausradGroesseAendern = false;
	private static boolean elementShortcutsVerwenden = true;
	
	private static int xZoomProSchritt = 10;
	private static int yZoomProSchritt = 10;
	
	private static boolean kantenglaettungVerwenden = false;
	
	public static final int strgOderApfelMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	static {
		readBuildInfoFile();
	}

	public static void init() {
		for (int i = 0; i < EinstellungsDialog.anzahlStruktogrammElemente; i++){
			elementBeschriftungenZumEinfuegenInDasStruktogramm.add(EinstellungsDialog.standardWerte[i]);
		}
		
		loadSettings();
	}
	
	private static void readBuildInfoFile () {
		try {
			Properties pr = new Properties();
			InputStream in = null;
			try {
				if (GlobalSettings.class.getResourceAsStream(BUILDINFO_FILE) == null) {
					System.err.println("Error no Buildinfo file could be found");
				} else {
					in = new BufferedInputStream(GlobalSettings.class.getResourceAsStream(BUILDINFO_FILE));
					pr.load(in);
					in.close();
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			String s;
			
			s = pr.getProperty("version");
			if(s != null){
				versionsString = s;
				guiTitel = "Struktogrammeditor " + versionsString;
			}

			s = pr.getProperty("revision");
			if(s != null){
				buildInfoGitHash = s;
			}
			
			s = pr.getProperty("timestamp");
			if(s != null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				buildInfoBuildTime = sdf.format(new Date(Long.parseLong(s)));
			}
		} catch (RuntimeException e){
			e.printStackTrace();
		}
	}
	
	
	public static String[] getCurrentElementBeschriftungsstil() {
		return elementAuswahlBeschriftungen[beschriftungsStilAktuell >= 0 && beschriftungsStilAktuell < elementAuswahlBeschriftungen.length ? beschriftungsStilAktuell : 0];
	}
	
	
	private static void loadSettings () {
		//Wenn eine alte Einstellungsdatei (bis einschließlich Version 1.4) existiert, diese laden...
		if(einstellungsDateiBisVersion1Punkt4.exists()){
			List<String> einstellungen = Helpers.readTextFile(einstellungsDateiBisVersion1Punkt4);

			if (einstellungen != null){
				for(String einstellung : einstellungen){
					elementBeschriftungenZumEinfuegenInDasStruktogramm.add(einstellung);
				}
			}
			
			if(!einstellungsDateiBisVersion1Punkt4.delete()){//...und anschließend löschen
				einstellungsDateiBisVersion1Punkt4.deleteOnExit();
			}
		}
		
		//Neue Einstellungsdatei einlesen
		if(einstellungsDatei.exists()){
			Properties pr = new Properties();
			try {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(einstellungsDatei));
				pr.load(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			String s;
			
			s = pr.getProperty("stretchlast");
			if(s != null){
				letzteElementeStrecken = s.equals("1");
			}
			
			s = pr.getProperty("cespaces");
			if(s != null){
				codeErzeugerEinrueckungGesamt = Integer.parseInt(s);
			}
			
			s = pr.getProperty("cespacesperstep");
			if(s != null){
				codeErzeugerEinrueckungProStufe = Integer.parseInt(s);
			}
			
			s = pr.getProperty("celanguage");
			if(s != null){
				codeErzeugerProgrammiersprache = SupportedLanguages.getByName(s);
			}
			
			s = pr.getProperty("cecomments");
			if(s != null){
				codeErzeugerAlsKommentar = s.equals("1");
			}
			
			s = pr.getProperty("mousewheelresize");
			if(s != null){
				beiMausradGroesseAendern = s.equals("1");
			}
			
			s = pr.getProperty("useelementshortcuts");
			if(s != null){
				elementShortcutsVerwenden = s.equals("1");
			}
			
			s = pr.getProperty("pathfiles");
			if(s != null){
				zuletztGenutzterSpeicherpfad = new File(s);
			}
			
			s = pr.getProperty("pathpictures");
			if(s != null){
				zuletztGenutzterPfadFuerBild = new File(s);
			}
			
			s = pr.getProperty("zoomx");
			if(s != null){
				xZoomProSchritt = Integer.parseInt(s);
			}
			
			s = pr.getProperty("zoomy");
			if(s != null){
				yZoomProSchritt = Integer.parseInt(s);
			}
			
			s = pr.getProperty("captionstyle");
			if(s != null){
				beschriftungsStilAktuell = Integer.parseInt(s);
			}
			
			s = pr.getProperty("lookandfeel");
			if(s != null){
				lookAndFeelAktuell = Integer.parseInt(s);
			}
			
			s = pr.getProperty("useantialiasing");
			if(s != null){
				kantenglaettungVerwenden = s.equals("1");
			}
			
			for(int i=0; i < EinstellungsDialog.anzahlStruktogrammElemente; i++){
				s = pr.getProperty("caption"+i);//Startbeschriftung der Struktogrammelemente
				if(s != null){
					elementBeschriftungenZumEinfuegenInDasStruktogramm.set(i, s);
				}				 	
			}		
		} else {
			try {
				einstellungsDatei.createNewFile();
			} catch (IOException e) {
				System.err.println("Could not create Settingsfile!");
				System.err.println(e.getMessage());
			}
		}
	}
	
	
	public static void saveSettings() {
		Properties properties = new Properties();
		
		properties.setProperty("stretchlast", letzteElementeStrecken ? "1" : "0");
		
		properties.setProperty("cespaces", "" + codeErzeugerEinrueckungGesamt);
		properties.setProperty("cespacesperstep", "" + codeErzeugerEinrueckungProStufe);
		properties.setProperty("celanguage", "" + codeErzeugerProgrammiersprache);
		properties.setProperty("cecomments", codeErzeugerAlsKommentar ? "1" : "0");
		
		properties.setProperty("mousewheelresize", beiMausradGroesseAendern ? "1" : "0");
		properties.setProperty("useelementshortcuts", elementShortcutsVerwenden ? "1" : "0");
		if (zuletztGenutzterSpeicherpfad != null && zuletztGenutzterSpeicherpfad.getPath() != null) {
			properties.setProperty("pathfiles", zuletztGenutzterSpeicherpfad.getPath());
		} else {
			properties.setProperty("pathfiles", "");
		}
		properties.setProperty("pathpictures", zuletztGenutzterPfadFuerBild.getPath());
		properties.setProperty("zoomx", "" + xZoomProSchritt);
		properties.setProperty("zoomy", "" + yZoomProSchritt);
		
		properties.setProperty("captionstyle", "" + beschriftungsStilAktuell);
		properties.setProperty("lookandfeel", "" + lookAndFeelAktuell);
		
		properties.setProperty("useantialiasing", kantenglaettungVerwenden ? "1" : "0");

		for(int i = 0; i < EinstellungsDialog.anzahlStruktogrammElemente; i++){
			properties.setProperty("caption" + i, elementBeschriftungenZumEinfuegenInDasStruktogramm.get(i));
		}


		try {
			//What if someone deleted it?
			if (!einstellungsDatei.exists()) {
				einstellungsDatei.createNewFile();
			}
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(einstellungsDatei));
			properties.store(out, "Struktogrammeditor Properties");
			System.out.println(einstellungsDatei.getAbsolutePath());
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void setzeSpeicherpfad(File pfad){
		zuletztGenutzterSpeicherpfad = pfad; //letzter richtiger Pfad wird gespeichert
	}

	public static void setzeBildSpeicherpfad(File pfad){
		zuletztGenutzterPfadFuerBild = pfad;
	}


	public static File getZuletztGenutzterSpeicherpfad() {
		return zuletztGenutzterSpeicherpfad;
	}


	public static File getZuletztGenutzterPfadFuerBild() {
		return zuletztGenutzterPfadFuerBild;
	}

	public static void setzeLetzteElementeStrecken(boolean strecken){
		letzteElementeStrecken = strecken;
	}

	public static boolean gibLetzteElementeStrecken(){
		return letzteElementeStrecken;
	}

	public static String gibElementBeschriftung(AnweisungsTyp anweisungsTyp){
		return elementBeschriftungenZumEinfuegenInDasStruktogramm.get(anweisungsTyp.getNumber());
	}

	public static void setzeElementBeschriftungen(List<String> neueBeschriftungen){
		elementBeschriftungenZumEinfuegenInDasStruktogramm = neueBeschriftungen; 
	}

	
	public static void setCodeErzeugerEinrueckungGesamt (int codeErzeugerEinrueckungGesamt) {
		GlobalSettings.codeErzeugerEinrueckungGesamt = codeErzeugerEinrueckungGesamt;
	}


	public static int getCodeErzeugerEinrueckungGesamt () {
		return codeErzeugerEinrueckungGesamt;
	}


	public static void setCodeErzeugerEinrueckungProStufe (int codeErzeugerEinrueckungProStufe) {
		GlobalSettings.codeErzeugerEinrueckungProStufe = codeErzeugerEinrueckungProStufe;
	}


	public static int getCodeErzeugerEinrueckungProStufe () {
		return codeErzeugerEinrueckungProStufe;
	}


	public static SupportedLanguages getCodeErzeugerProgrammiersprache() {
		return codeErzeugerProgrammiersprache;
	}


	public static void setCodeErzeugerProgrammiersprache(SupportedLanguages codeErzeugerProgrammiersprache) {
		GlobalSettings.codeErzeugerProgrammiersprache = codeErzeugerProgrammiersprache;
	}


	public static boolean isCodeErzeugerAlsKommentar() {
		return codeErzeugerAlsKommentar;
	}


	public static void setCodeErzeugerAlsKommentar(boolean codeErzeugerAlsKommentar) {
		GlobalSettings.codeErzeugerAlsKommentar = codeErzeugerAlsKommentar;
	}


	public static void setBeiMausradGroesseAendern(boolean beiMausradGroesseAendern) {
		GlobalSettings.beiMausradGroesseAendern = beiMausradGroesseAendern;
	}


	public static boolean isBeiMausradGroesseAendern() {
		return beiMausradGroesseAendern;
	}


	public static int getXZoomProSchritt() {
		return xZoomProSchritt;
	}


	public static void setXZoomProSchritt(int xZoomProSchritt) {
		GlobalSettings.xZoomProSchritt = xZoomProSchritt;
	}


	public static int getYZoomProSchritt() {
		return yZoomProSchritt;
	}


	public static void setYZoomProSchritt(int yZoomProSchritt) {
		GlobalSettings.yZoomProSchritt = yZoomProSchritt;
	}


	public static int getBeschriftungsStilAktuell() {
		return beschriftungsStilAktuell;
	}


	public static void setBeschriftungsStilAktuell(int beschriftungsStilAktuell) {
		GlobalSettings.beschriftungsStilAktuell = beschriftungsStilAktuell;
	}


	public static boolean isElementShortcutsVerwenden() {
		return elementShortcutsVerwenden;
	}


	public static void setElementShortcutsVerwenden(boolean elementShortcutsVerwenden) {
		GlobalSettings.elementShortcutsVerwenden = elementShortcutsVerwenden;
	}


	public static boolean isKantenglaettungVerwenden() {
		return kantenglaettungVerwenden;
	}


	public static void setKantenglaettungVerwenden(boolean kantenglaettungVerwenden) {
		GlobalSettings.kantenglaettungVerwenden = kantenglaettungVerwenden;
	}


	public static int getLookAndFeelAktuell() {
		return lookAndFeelAktuell;
	}


	public static void setLookAndFeelAktuell(int lookAndFeelAktuell) {
		GlobalSettings.lookAndFeelAktuell = lookAndFeelAktuell;
	}
}
