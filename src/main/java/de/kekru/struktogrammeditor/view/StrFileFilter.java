package de.kekru.struktogrammeditor.view;
import java.io.File;

import javax.swing.filechooser.FileFilter;

//FileFilter für Dateitypen zum Abspeichern des Struktogramms und Dateitypen zum Abspeichern als Bilddatei
//http://www.java2s.com/Code/JavaAPI/javax.swing/JFileChoosersetFileFilterFileFilterfilter.htm
public class StrFileFilter extends FileFilter {
	private int filtertyp;
	public static final int filterAlleSpeicherdateien = 0;
	private static final int filterAlleBilddateien = 3;

	public StrFileFilter(int filtertyp){
		this.filtertyp = filtertyp;
	}

	//accept(...)-Methode überschreiben, damit der JFileChooser weis, ob er eine bestimmte Datei anzeigen soll
	public boolean accept(File f){
		return f.isDirectory() || dateiAkzeptiert(f.getAbsolutePath());//Ordner anzeigen und Dateien anzeigen die durch dateiAkzeptiert(...) akzeptiert werden
	}


	//Beschreibungen für die einzelnen Filtertypen
	public String getDescription(){
		switch(filtertyp){
		case filterAlleSpeicherdateien: return "XML Dateien und strk Dateien";
		case 1: return "strk Dateien";
		case 2: return "XML Dateien";
		case filterAlleBilddateien: return "Bilddateien";
		case 4: return "BMP Dateien";
		case 5: return "GIF Dateien";
		case 6: return "JPG Dateien";
		case 7: return "PNG Dateien";
		default: return "";
		}
	}


	private String gibAktuelleErweiterung(){//Dateierweiterung bei diesem Filter
		switch(filtertyp){
		case filterAlleSpeicherdateien: return ".strk";
		case 1: return ".strk";
		case 2: return ".xml";
		case filterAlleBilddateien: return ".png";
		case 4: return ".bmp";
		case 5: return ".gif";
		case 6: return ".jpg";
		case 7: return ".png";
		default: return "";
		}
	}


	public String erweiterungBeiBedarfAnhaengen(String pfad){
		/*if (!pfad.endsWith(gibAktuelleErweiterung())){//wenn der Pfad nicht mit der richtigen Dateierweiterung endet...
         return pfad + gibAktuelleErweiterung();//...wird diese angehangen
      }else{
         return pfad;
      }*/

		if(dateiAkzeptiert(pfad)){
			return pfad;
		}else{
			return pfad + gibAktuelleErweiterung();
		}
	}


	private boolean dateiAkzeptiert(String pfad){
		pfad = pfad.toLowerCase();
		switch(filtertyp){
		case filterAlleSpeicherdateien: return pfad.endsWith(".xml") || pfad.endsWith(".strk"); //wenn der Filter "XML Dateien und strk Dateien" ist, werden .xml und .strk Dateien akzeptiert
		case filterAlleBilddateien: return pfad.endsWith(".bmp") || pfad.endsWith(".gif") || pfad.endsWith(".jpg") || pfad.endsWith(".jpeg") || pfad.endsWith(".png"); //wenn der Filter "Bilddateien" ist, werden alle Bilddateien akzeptiert
		default: return pfad.endsWith(gibAktuelleErweiterung()); //es werden nur die Dateien mit genau der ausgesuchten Endung akzeptiert
		}
	}

}