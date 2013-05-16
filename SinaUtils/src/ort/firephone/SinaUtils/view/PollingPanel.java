package ort.firephone.SinaUtils.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.inf.GetFollowerIds;
import ort.firephone.SinaUtils.inf.GetTimeLine;
import ort.firephone.SinaUtils.inf.GetUserInfo;

import weibo4j.Weibo;

public class PollingPanel extends JPanel {
	JButton followersButton = new JButton("取粉丝ID");
	JButton usersButton = new JButton("取用户信息");
	JButton timelineButton = new JButton("取用户微博");
	JButton allButton = new JButton("取全部");
	JTextArea textArea = new JTextArea();

	GetFollowerIds getFollowerIds;
	GetUserInfo getUserInfo;
	GetTimeLine getTimeLine;

	public PollingPanel() {

		BorderLayout mainBorderLayout = new BorderLayout();
		JPanel buttonPanel = new JPanel();
		JScrollPane jScrollPane = new JScrollPane();
		this.setLayout(mainBorderLayout);

		buttonPanel.add(followersButton);
		buttonPanel.add(usersButton);
		buttonPanel.add(timelineButton);
		buttonPanel.add(allButton);
		this.add(buttonPanel, BorderLayout.NORTH);
		textArea.setEnabled(true);
		jScrollPane.getViewport().add(textArea);
		this.add(jScrollPane, BorderLayout.CENTER);
		SetListener();
	}

	public void SetListener() {
		followersButton.addActionListener(new button_actionAdapter(this,
				followersButton));
		usersButton.addActionListener(new button_actionAdapter(this,
				usersButton));
		timelineButton.addActionListener(new button_actionAdapter(this,
				timelineButton));
		allButton.addActionListener(new button_actionAdapter(this, allButton));
	}

	public void doGetFollowersId(ActionEvent e) {
		Log.textArea = textArea;
		if (getFollowerIds != null) {
			if (getFollowerIds.isAlive()) {
				Log.log(Log.Severity_Informational, "getFollowerIds 已经在运行");
				return;
			}
		}
		getFollowerIds = new GetFollowerIds();
		getFollowerIds.start();
	}

	public void doGetUserInfo(ActionEvent e) {
		Log.textArea = textArea;
		getUserInfo = new GetUserInfo();
		getUserInfo.start();
	}

	public void doGetTimeLine(ActionEvent e) {
		Log.textArea = textArea;
		getTimeLine = new GetTimeLine();
		getTimeLine.start();
	}

	public void doAll(ActionEvent e) {
		Log.textArea = textArea;

	}

	class button_actionAdapter implements ActionListener {
		private PollingPanel adaptee;
		private JButton button = null;

		button_actionAdapter(PollingPanel adaptee, JButton button) {
			this.adaptee = adaptee;
			this.button = button;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				if (this.button == followersButton) {
					adaptee.doGetFollowersId(e);
				} else if (this.button == usersButton) {
					adaptee.doGetUserInfo(e);
				} else if (this.button == timelineButton) {
					adaptee.doGetTimeLine(e);
				} else if (this.button == allButton) {
					adaptee.doAll(e);
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}
}
