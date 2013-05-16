package ort.firephone.SinaUtils.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ort.firephone.SinaUtils.db.FSstatus;

public class SearchPanel extends JPanel {
	JTextArea textArea = new JTextArea();
	JButton confirmButton = new JButton("确定");
	JLabel labelKEY = new JLabel("key:");
	JTextField textKEY = new JTextField();
	JPanel topPanel = new JPanel();

	public SearchPanel() {
		BorderLayout mainBorderLayout = new BorderLayout();
		JScrollPane jScrollPane = new JScrollPane();
		this.setLayout(mainBorderLayout);
		GridBagLayout gbl;
		GridBagConstraints gbc;
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(2, 3, 2, 3);
		topPanel.setLayout(gbl);
		gbc.gridwidth = 1;
		textKEY.setColumns(10);
		topPanel.add(labelKEY, gbc);
		topPanel.add(textKEY, gbc);
		topPanel.add(confirmButton, gbc);
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		this.add(topPanel, BorderLayout.NORTH);
		textArea.setEnabled(true);
		jScrollPane.getViewport().add(textArea);
		this.add(jScrollPane, BorderLayout.CENTER);

		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startSearch(textKEY.getText().trim());
			}
		});
	}

	public void startSearch(String key) {
		Vector<String> vs = FSstatus.searchText(key, 2000, 0);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < vs.size(); i++) {
			sb.append(vs.get(i)+"\r\n");
		}
		textArea.setText(sb.toString());
	}
}
