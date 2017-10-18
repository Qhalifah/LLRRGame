package level.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import launcher.Launcher;

public class TextEditor extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textField;

	/**
	 * Create the frame.
	 */
	public TextEditor(Component putBeside, String[][] notifiers, int x, int y) {
		setTitle("Text Editor");
		setBounds(putBeside.getX() + putBeside.getWidth(), putBeside.getY(), 400, 200);
		setPreferredSize(new Dimension(400, 200));
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		pack();
		getContentPane().setLayout(null);

		JLabel lblLanguage = new JLabel("Language: " + Launcher.lang);
		lblLanguage.setBounds(12, 13, 370, 16);
		getContentPane().add(lblLanguage);

		JSeparator separator = new JSeparator();
		separator.setBounds(22, 42, 360, 2);
		getContentPane().add(separator);

		textField = new JTextArea();
		textField.setWrapStyleWord(true);
		textField.setLineWrap(true);
		textField.setBounds(54, 57, 328, 57);
		textField.setText(notifiers[x][y]);
		getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblText = new JLabel("Text:");
		lblText.setBounds(12, 60, 30, 16);
		getContentPane().add(lblText);

		JButton btnSet = new JButton("Set");
		btnSet.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				notifiers[x][y] = textField.getText();
				dispose();
			}
		});
		btnSet.setBounds(12, 127, 173, 25);
		getContentPane().add(btnSet);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setBounds(209, 127, 173, 25);
		getContentPane().add(btnClose);
	}
}
