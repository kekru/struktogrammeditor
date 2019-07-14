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
import java.util.Date;
import java.util.Properties;

import de.kekru.struktogrammeditor.other.Helpers;
import de.kekru.struktogrammeditor.view.CodeErzeuger;
import de.kekru.struktogrammeditor.view.EinstellungsDialog;

public class GlobalSettings implements Konstanten{

	public static final int updateNummer = 8;
	public static String versionsString = "";
	public static String guiTitel = "";
	public static final String[] updateDaten = {"30.05.2011", "31.05.2011", "05.06.2011", "11.09.2011", "18.01.2012", "17.02.2012", "02.05.2012", "16.08.2012", "13.05.2014", "10.07.2014"};
	
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

	private static String zuletztGenutzterSpeicherpfad = "";
	private static String zuletztGenutzterPfadFuerBild = "";
	private static boolean letzteElementeStrecken = false;
	//private String[] elementBeschriftungen = EinstellungsDialog.standardWerte;//hier wird ja nur der Zeiger kopiert (wie bei Objekten üblich), nicht der Inhalt
	private static String[] elementBeschriftungenZumEinfuegenInDasStruktogramm = new String[EinstellungsDialog.anzahlStruktogrammElemente];
	public static final Font fontStandard = new Font("serif", Font.PLAIN, 15);
	private static final String einstellungsDateiPfad = "struktogrammeditor.properties";
	private static final String einstellungsDateiPfadBisVersion1Punkt4 = "StruktogrammeditorEinstellungen.txt";
	
	private static int codeErzeugerEinrueckungGesamt = 3;
	private static int codeErzeugerEinrueckungProStufe = 3;
	private static int codeErzeugerProgrammiersprache = CodeErzeuger.typJava;
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

	public static void init(){
		for(int i=0; i < EinstellungsDialog.anzahlStruktogrammElemente; i++){
			elementBeschriftungenZumEinfuegenInDasStruktogramm[i] = EinstellungsDialog.standardWerte[i];
		}
		
		loadSettings();
	}
	
	private static void readBuildInfoFile(){

		try {
	
			Properties pr = new Properties();

			InputStream in = null;
			try {
				in = new BufferedInputStream(GlobalSettings.class.getResourceAsStream(BUILDINFO_FILE));
				pr.load(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String s;
			
			s = pr.getProperty("version");
			if(s != null){
				versionsString = s;
				guiTitel = "Struktogrammeditor "+versionsString;
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
	
	
	public static String[] getCurrentElementBeschriftungsstil(){
		return elementAuswahlBeschriftungen[beschriftungsStilAktuell >= 0 && beschriftungsStilAktuell < elementAuswahlBeschriftungen.length ? beschriftungsStilAktuell : 0];
	}
	
	
	private static void loadSettings(){
		//Wenn eine alte Einstellungsdatei (bis einschließlich Version 1.4) existiert, diese laden...
		File f = new File(einstellungsDateiPfadBisVersion1Punkt4);
		if(f.exists()){
			String[] einstellungsdaten = Helpers.readTextFile(einstellungsDateiPfadBisVersion1Punkt4);

			if (einstellungsdaten != null){
				for(int i=0; i < einstellungsdaten.length; i++){
					elementBeschriftungenZumEinfuegenInDasStruktogramm[i] = einstellungsdaten[i];
				}
			}
			
			if(!f.delete()){//...und anschließend löschen
				f.deleteOnExit();
			}
			
		}
		
		
		
		//Neue Einstellungsdatei einlesen
		File propertiesFile = new File(einstellungsDateiPfad);


		if(propertiesFile.exists()){

			

			Properties pr = new Properties();

			try {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(propertiesFile));

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
				codeErzeugerProgrammiersprache = Integer.parseInt(s);
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
				zuletztGenutzterSpeicherpfad = s;
			}
			
			s = pr.getProperty("pathpictures");
			if(s != null){
				zuletztGenutzterPfadFuerBild = s;
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
					elementBeschriftungenZumEinfuegenInDasStruktogramm[i] = s;
				}				 	
			}		
		}		
	}
	
	
	public static void saveSettings(){
		
		Properties properties = new Properties();
		
		properties.setProperty("stretchlast", letzteElementeStrecken ? "1" : "0");
		
		properties.setProperty("cespaces", ""+codeErzeugerEinrueckungGesamt);
		properties.setProperty("cespacesperstep", ""+codeErzeugerEinrueckungProStufe);
		properties.setProperty("celanguage", ""+codeErzeugerProgrammiersprache);
		properties.setProperty("cecomments", codeErzeugerAlsKommentar ? "1" : "0");
		
		properties.setProperty("mousewheelresize", beiMausradGroesseAendern ? "1" : "0");
		properties.setProperty("useelementshortcuts", elementShortcutsVerwenden ? "1" : "0");
		
		properties.setProperty("pathfiles", zuletztGenutzterSpeicherpfad);
		properties.setProperty("pathpictures", zuletztGenutzterPfadFuerBild);
		
		properties.setProperty("zoomx", ""+xZoomProSchritt);
		properties.setProperty("zoomy", ""+yZoomProSchritt);
		
		properties.setProperty("captionstyle", ""+beschriftungsStilAktuell);
		properties.setProperty("lookandfeel", ""+lookAndFeelAktuell);
		
		properties.setProperty("useantialiasing", kantenglaettungVerwenden ? "1" : "0");
		
		for(int i=0; i < EinstellungsDialog.anzahlStruktogrammElemente; i++){
			
			properties.setProperty("caption"+i, elementBeschriftungenZumEinfuegenInDasStruktogramm[i]);
		}


		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(einstellungsDateiPfad)));

			properties.store(out, "Struktogrammeditor Properties");			
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void setzeSpeicherpfad(String pfad){
		if(!pfad.equals("")){
			zuletztGenutzterSpeicherpfad = pfad; //letzter richtiger Pfad wird gespeichert
		}
	}

	public static void setzeBildSpeicherpfad(String pfad){
		if(!pfad.equals("")){
			zuletztGenutzterPfadFuerBild = pfad;
		}
	}


	public static String getZuletztGenutzterSpeicherpfad() {
		return zuletztGenutzterSpeicherpfad;
	}


	public static String getZuletztGenutzterPfadFuerBild() {
		return zuletztGenutzterPfadFuerBild;
	}

	public static void setzeLetzteElementeStrecken(boolean strecken){
		letzteElementeStrecken = strecken;
	}

	public static boolean gibLetzteElementeStrecken(){
		return letzteElementeStrecken;
	}

	public static String gibElementBeschriftung(int typNummer){
		return elementBeschriftungenZumEinfuegenInDasStruktogramm[typNummer];
	}

	public static void setzeElementBeschriftungen(String[] neueBeschriftungen){
		elementBeschriftungenZumEinfuegenInDasStruktogramm = neueBeschriftungen; 
	}

	
	public static void setCodeErzeugerEinrueckungGesamt(
			int codeErzeugerEinrueckungGesamt) {
		GlobalSettings.codeErzeugerEinrueckungGesamt = codeErzeugerEinrueckungGesamt;
	}


	public static int getCodeErzeugerEinrueckungGesamt() {
		return codeErzeugerEinrueckungGesamt;
	}


	public static void setCodeErzeugerEinrueckungProStufe(
			int codeErzeugerEinrueckungProStufe) {
		GlobalSettings.codeErzeugerEinrueckungProStufe = codeErzeugerEinrueckungProStufe;
	}


	public static int getCodeErzeugerEinrueckungProStufe() {
		return codeErzeugerEinrueckungProStufe;
	}


	public static int getCodeErzeugerProgrammiersprache() {
		return codeErzeugerProgrammiersprache;
	}


	public static void setCodeErzeugerProgrammiersprache(
			int codeErzeugerProgrammiersprache) {
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
