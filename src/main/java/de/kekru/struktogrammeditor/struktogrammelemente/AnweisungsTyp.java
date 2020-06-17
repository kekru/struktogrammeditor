package de.kekru.struktogrammeditor.struktogrammelemente;

public enum AnweisungsTyp {
	Anweisung(0),
	Verzweigung(1),
	Fallauswahl(2),
	ForSchleife(3),
	WhileSchleife(4),
	DoUntilSchleife(5),
	Endlosschleife(6),
	Aussprung(7),
	Aufruf(8),
	LeerElement(9),
	ERROR (-1);
	
	private int number;
	private AnweisungsTyp (int number) {
		this.number = number;
	}
	
	public int getNumber () {
		return this.number;
	}
	
	public static AnweisungsTyp getByNumber (int number) {
		for (AnweisungsTyp a : values()) {
			if (a.number == number) {
				return a;
			}
		}
		return null;
	}
}
