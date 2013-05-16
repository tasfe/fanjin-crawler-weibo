package ort.firephone.SinaUtils.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.eclipse.swt.widgets.Display;

import ort.firephone.SinaUtils.Env;

public class TestFrame1 extends JFrame {
	public static void main(String argv[]) {
		Env.initial();
		TestFrame1 testFrame1 = new TestFrame1();
		testFrame1.setSize(600, 480);
		testFrame1.setVisible(true);
	}

	public TestFrame1() {
		try {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			jbInit();
			pack();
			// addWindowListener(new WinClose());
			ImageIcon img = new ImageIcon("images/rw.JPG");
			this.setIconImage(img.getImage());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	JPanel jPanel_north = new JPanel();
	JPanel jPanel_center = new JPanel();
	JPanel jPanel_south = new JPanel();

	public void jbInit() {
		BorderLayout mainBorderLayout = new BorderLayout();
		BorderLayout centerBorderLayout = new BorderLayout();
		JTabbedPane jTabbedPane1 = new JTabbedPane();
		this.getContentPane().setLayout(mainBorderLayout);
		this.getContentPane().add(jPanel_north, java.awt.BorderLayout.NORTH);
		this.getContentPane().add(jPanel_center, java.awt.BorderLayout.CENTER);
		this.getContentPane().add(jPanel_south, java.awt.BorderLayout.SOUTH);
		PollingPanel pollingPanel = new PollingPanel();
		SetMemberPanel setMemberPanel = new SetMemberPanel();
		FollowActionView followActionView = new FollowActionView();
		SearchPanel searchPanel = new SearchPanel();

		jTabbedPane1.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 12));
		jPanel_north.add(new JLabel("微博工具"));
		jPanel_center.setLayout(centerBorderLayout);
		jPanel_center.add(jTabbedPane1);
		jPanel_south.add(new JLabel(""));

		jTabbedPane1.add(pollingPanel, "微博取数据");
		jTabbedPane1.add(setMemberPanel, "新增空僵尸");
		jTabbedPane1.add(followActionView, "清");
		jTabbedPane1.add(searchPanel, "查询");
	}

}
