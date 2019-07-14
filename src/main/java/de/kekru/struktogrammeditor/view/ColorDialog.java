package de.kekru.struktogrammeditor.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ColorDialog extends JDialog {

	private static final long serialVersionUID = 5666531548056491207L;
	private static final JColorChooser colorChooser = new JColorChooser(); //static und final -> nur einmal erzeugen, damit die Farben im Chooser bleiben
	private boolean okGeklickt = false;
	
	private ColorDialog(Dialog owner){

		super(owner, "Farbauswahl", true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());
		add(new JScrollPane(colorChooser), BorderLayout.CENTER);

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		{
			JButton button = new JButton("OK");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					okButtonGeklickt();
				}
			});
			panel.add(button);
			
			button = new JButton("Abbrechen");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					abbrechenButtonGeklickt();
				}
			});
			panel.add(button);
			
			button = new JButton("Schwarz");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					colorChooser.setColor(Color.black);
				}
			});
			panel.add(button);
			
			button = new JButton("Weiß");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					colorChooser.setColor(Color.white);
				}
			});
			panel.add(button);
		}
		add(panel, BorderLayout.SOUTH);



		setSize(600,470);
		setLocationRelativeTo(owner);
		setVisible(true);


	}
	
	
	private void okButtonGeklickt(){
		okGeklickt = true;
		dispose();
	}
	
	
	private void abbrechenButtonGeklickt(){
		dispose();
	}
	
	public boolean isOkGeklickt() {
		return okGeklickt;
	}


	public static Color showColorChooser(Dialog owner, Color startFarbe){
		if(startFarbe != null){
			colorChooser.setColor(startFarbe);
		}
		
		ColorDialog colorDialog = new ColorDialog(owner);
		
		return colorDialog.isOkGeklickt() ? colorChooser.getColor() : startFarbe;		
	}	

}
