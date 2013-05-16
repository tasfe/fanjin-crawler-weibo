package ort.firephone.SinaUtils.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.db.Fcorpse;

public class SetMemberPanel extends JPanel {

	JTextArea textArea = new JTextArea();
	JPanel buttonPanel = new JPanel();
	JLabel labelKEY = new JLabel("key:");
	JTextField textKEY = new JTextField();
	JLabel labelSECRET = new JLabel("secret:");
	JTextField textSECRET = new JTextField();
	JLabel labelDetail = new JLabel("下面填入 “用户名----密码” ，插入未授权账号");
	JButton startButton = new JButton("插入");

	public SetMemberPanel() {
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
		buttonPanel.setLayout(gbl);
		gbc.gridwidth = 1;
		textKEY.setColumns(10);
		textSECRET.setColumns(14);
		buttonPanel.add(labelKEY, gbc);
		buttonPanel.add(textKEY, gbc);
		buttonPanel.add(labelSECRET, gbc);
		buttonPanel.add(textSECRET, gbc);
		buttonPanel.add(startButton, gbc);
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		buttonPanel.add(labelDetail, gbc);

		this.add(buttonPanel, BorderLayout.NORTH);
		textArea.setEnabled(true);
		jScrollPane.getViewport().add(textArea);
		this.add(jScrollPane, BorderLayout.CENTER);

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				doStart();
			}
		});

		textKEY.setText(Env.CONSUMER_KEY);
		textSECRET.setText(Env.CONSUMER_SECRET);

	}

	public void doStart() {
		String key = textKEY.getText().trim();
		String secret = textSECRET.getText().trim();
		String all_text = textArea.getText().trim();
		String result_text = "";
		String lines[] = all_text.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			String item[] = line.split("----");
			if (item.length != 2) {
				continue;
			}
			Fcorpse one = new Fcorpse();
			one.setSina_id(0L);
			one.setApp_key(key);
			one.setApp_secret(secret);
			one.setSina_user(item[0]);
			one.setSina_pass(item[1]);
			one.setOauth_token("");
			one.setOauth_token_secret("");
			one.insertToDB();

		}
		textArea.setText(result_text);

	}
}
