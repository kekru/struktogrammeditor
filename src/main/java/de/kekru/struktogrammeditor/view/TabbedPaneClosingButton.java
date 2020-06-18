package de.kekru.struktogrammeditor.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import de.kekru.struktogrammeditor.control.Struktogramm;

public class TabbedPaneClosingButton extends JButton {

	private static final long serialVersionUID = 1L;

	private Struktogramm struktogramm;
	private StrTabbedPane strtabbedpane;
	
	public TabbedPaneClosingButton(StrTabbedPane strtabbedpane) {
		super("Schließen");
		this.strtabbedpane = strtabbedpane;
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				strtabbedpane.aktuellesStruktogrammschliessen();
				if (strtabbedpane.getTabCount() == 0) {
					Object[] options = {"Ja", "Nein"};
					if (JOptionPane.showOptionDialog(strtabbedpane.gibGUI(), "Beenden?", "Beenden?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]) == 0){
						System.exit(0);
					}
				}
			}
		});
	}
	
}
