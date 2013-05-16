package ort.firephone.SinaUtils.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.eclipse.swt.widgets.Display;

public class Test {
	public static void main(String argv[]) {
		x1();
	}

	public static void x1() {
		JDialog d = new JDialog();
		JButton b = new JButton();

		

		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				IEPanel ie = new IEPanel(Display.getDefault());
				ie.viewUrl("www.youlaopo.com");

			}

		});
		d.add(b);
		d.setVisible(true);
	}

}
