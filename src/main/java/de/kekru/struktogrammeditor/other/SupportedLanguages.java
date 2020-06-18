package de.kekru.struktogrammeditor.other;

import java.util.stream.Stream;

/**
*
* Struktogrammeditor
*
* @version Unbekannt
* @author Kevin Krummenauer
* Initialer Code
* 
* @version 1.7.4
* @author Rafael Sundorf
* Umstellung auf Enum und anderen Anpassung
*/
public enum SupportedLanguages {
	
	Java ("Java", 0),
	Delphi ("Delphi", 1),
	PHP ("PHP", 2);
	
	
	private String name;
	private int id;
	private SupportedLanguages (String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName () {
		return this.name;
	}
	
	
	public int getId () {
		return this.id;
	}
	
	public static SupportedLanguages getById (int id) {
	    return Stream.of(values())
	            .filter(entry -> entry.getId() == id)
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("No SupportedLanguages found with id " + id));
	}
	
	public static SupportedLanguages getByName (String name) {
		for (SupportedLanguages l : values()) {
			if (l.name.equals(name)) {
				return l;
			}
		}
		return null;
	}
	
	/**
	 * Checks if the Language is supported
	 * @param name
	 * @return
	 */
	public static boolean isSupported (String name) {
		for (SupportedLanguages l : values()) {
			if (l.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
