package ort.firephone.SinaUtils.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ort.firephone.SinaUtils.inf.RollFollowAction;
import ort.firephone.SinaUtils.view.PollingPanel.button_actionAdapter;

public class FollowActionView extends JPanel {
	JButton removeAllButton = new JButton("清除所有未成功");
	JTextArea textArea = new JTextArea();

	public FollowActionView() {

		BorderLayout mainBorderLayout = new BorderLayout();
		JPanel buttonPanel = new JPanel();
		JScrollPane jScrollPane = new JScrollPane();
		this.setLayout(mainBorderLayout);

		buttonPanel.add(removeAllButton);
		this.add(buttonPanel, BorderLayout.NORTH);
		textArea.setEnabled(true);
		jScrollPane.getViewport().add(textArea);
		this.add(jScrollPane, BorderLayout.CENTER);
		SetListener();
	}

	public void SetListener() {
		removeAllButton.addActionListener(new button_actionAdapter(this,
				removeAllButton));
	}

	public void removeAll() {
		RollFollowAction rollFollowAction = new RollFollowAction();
		rollFollowAction.DoFollowingAll();
	}

	class button_actionAdapter implements ActionListener {
		private FollowActionView adaptee;
		private JButton button = null;

		button_actionAdapter(FollowActionView adaptee, JButton button) {
			this.adaptee = adaptee;
			this.button = button;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				if (this.button == removeAllButton) {
					adaptee.removeAll();
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}
}
