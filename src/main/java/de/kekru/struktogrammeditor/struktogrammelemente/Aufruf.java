package struktogrammelemente;

import java.awt.Graphics2D;

import control.GlobalSettings;
import control.Struktogramm;

public class Aufruf extends Anweisung {

   public Aufruf(Graphics2D g){
      super(g);

      setzeText(GlobalSettings.gibElementBeschriftung(Struktogramm.typAufruf));
   }


   @Override
   public void zeichne(){
       super.zeichne();

       g.drawLine(gibX() +10,gibY(),gibX() +10,gibY() +gibHoehe());
       g.drawLine(gibX() +gibBreite() -10,gibY(),gibX() +gibBreite() -10,gibY() +gibHoehe());
   }
}