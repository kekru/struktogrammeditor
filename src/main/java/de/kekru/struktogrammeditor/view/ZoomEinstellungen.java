package de.kekru.struktogrammeditor.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import de.kekru.struktogrammeditor.control.GlobalSettings;
import de.kekru.struktogrammeditor.other.JNumberField;

public class ZoomEinstellungen extends JDialog {
	private static final long serialVersionUID = -4780523744293396039L;
	private JNumberField numberfieldX, numberfieldY;

	public ZoomEinstellungen(GUI gui){
		super(gui, "Zoom Einstellungen", true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);   
		setSize(380, 220);	    
		setLocationRelativeTo(gui);

		setLayout(new BorderLayout(10,10));

		add(new JLabel("<html><b>Wie viele Pixel soll das zu vergrößernde oder das zu verkleinernde Element pro Schritt in die jeweilige Richtung vergößert oder verkleinert werden?<b></html>;"), BorderLayout.NORTH);

		JPanel panel;
		JButton button;

		panel = new JPanel();
		{
			panel.setLayout(new GridLayout(2,2));
			panel.add(new JLabel("Waagerechte Größenänderung:"));
			panel.add(numberfieldX = new JNumberField());
			numberfieldX.setInt(GlobalSettings.getXZoomProSchritt());
			panel.add(new JLabel("Senkrechte Größenänderung:"));
			panel.add(numberfieldY = new JNumberField());
			numberfieldY.setInt(GlobalSettings.getYZoomProSchritt());
		}

		add(panel, BorderLayout.CENTER);


		panel = new JPanel();
		{
			panel.setLayout(new FlowLayout());

			panel.add(button = new JButton("OK"));
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					okGeklickt();
				}
			});


			panel.add(button = new JButton("Abbrechen"));
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					abbrechenGeklickt();
				}
			});
		}

		add(panel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void okGeklickt(){
		if(numberfieldX.isNumeric() && numberfieldY.isNumeric()){
			GlobalSettings.setXZoomProSchritt(numberfieldX.getInt());
			GlobalSettings.setYZoomProSchritt(numberfieldY.getInt());
			GlobalSettings.saveSettings();
			dispose();
		}
	}

	private void abbrechenGeklickt(){
		dispose();
	}
}
