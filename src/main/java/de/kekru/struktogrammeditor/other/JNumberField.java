package de.kekru.struktogrammeditor.other;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.JTextField;

/**
Swing-component for input and output of numeric values.
*/

public class JNumberField extends JTextField {
  
  private static final long serialVersionUID = -3137694650317084473L;

/** constructor for a JNumberField */
  public JNumberField() {
    enableEvents(AWTEvent.KEY_EVENT_MASK);
  }

  /** Gets a double-value from the JNumberField. */
  public double getDouble() {
    Double d = new Double(getText());
    return d.doubleValue();
  }

  /** Gets a float-value from the JNumberField. */
  public float getFloat() {
    Double d = new Double(getText());
    return d.floatValue();
  }

  /** Gets an int-value from the JNumberField. */
  public int getInt() {
    Double d = new Double(getText());
    return d.intValue();
  }

  /** Gets a long-value from the JNumberField. */
  public long getLong() {
    Double d = new Double(getText());
    return d.longValue();
  }

  /** Checks wether the JNumberField contains a valid numeric value. */
  public boolean isNumeric() {
    final String Digits     = "(\\p{Digit}+)";
    final String HexDigits  = "(\\p{XDigit}+)";
    // an exponent is 'e' or 'E' followed by an optionally
    // signed decimal integer.
    final String Exp        = "[eE][+-]?"+Digits;
    final String fpRegex    =
      ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
       "[+-]?(" + // Optional sign character
       "NaN|" +           // "NaN" string
       "Infinity|" +      // "Infinity" string

       // A decimal floating-point string representing a finite positive
       // number without a leading sign has at most five basic pieces:
       // Digits . Digits ExponentPart FloatTypeSuffix
       //
       // Since this method allows integer-only strings as input
       // in addition to strings of floating-point literals, the
       // two sub-patterns below are simplifications of the grammar
       // productions from the Java Language Specification, 2nd
       // edition, section 3.10.2.

       // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
       "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

       // . Digits ExponentPart_opt FloatTypeSuffix_opt
       "(\\.("+Digits+")("+Exp+")?)|"+

       // Hexadecimal strings
       "((" +
        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
        "(0[xX]" + HexDigits + "(\\.)?)|" +

        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

        ")[pP][+-]?" + Digits + "))" +
             "[fFdD]?))" +
             "[\\x00-\\x20]*");// Optional trailing "whitespace"

    return java.util.regex.Pattern.matches(fpRegex, getText());
  }

  /** Sets a double-value into the JNumberField. */
  public void setDouble(double d) {
    setText(String.valueOf(d));
  }

  /** Sets a double-value with N digits into the JNumberField. */
  public void setDouble(double d, int N) {
    setText(String.format(Locale.ENGLISH, "%." + N + "f", d));
  }

  /** Sets a float-value into the JNumberField. */
  public void setFloat(float f) {
    setText(String.valueOf(f));
  }

  /** Sets a float-value with N digits into the JNumberField. */
  public void setFloat(float f, int N) {
    setText(String.format(Locale.ENGLISH, "%." + N + "f", f));
  }

  /** Sets an int-value into the JNumberField. */
  public void setInt(int i) {
    setText(String.valueOf(i));
  }

  /** Sets a long-value into the JNumberField. */
  public void setLong(long l) {
    setText(String.valueOf(l));
  }
  
  /** Clears the JNumberField */
  public void clear() {
    setText("");
  }

  protected void processKeyEvent(KeyEvent e) {
    super.processKeyEvent(e);
    if (isNumeric() || getText().equals("-") ||
        getText().equals("") || getText().equals("."))
      setBackground(Color.white);
    else
      setBackground(Color.red);
  }
  
}