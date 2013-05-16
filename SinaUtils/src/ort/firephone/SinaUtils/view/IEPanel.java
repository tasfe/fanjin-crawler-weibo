package ort.firephone.SinaUtils.view;

import javax.swing.JFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

public class IEPanel extends Shell {
	Display display;

	public static void main(String args[]) {
		Display display = Display.getDefault();
		IEPanel shell = new IEPanel(display);
		shell.viewUrl("www.baidu.com");
		JFrame f = new JFrame();
	}

	public void viewUrl(String url) {
		this.setLayout(new FillLayout());
		Menu bar = new Menu(this, SWT.BAR);
		this.setMenuBar(bar);
		OleFrame frame = new OleFrame(this, SWT.NONE);
		OleControlSite clientsite = null;
		OleAutomation browser = null;
		try {
			clientsite = new OleControlSite(frame, SWT.NONE, "Shell.Explorer");
			browser = new OleAutomation(clientsite);
			clientsite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
			this.open();
			int[] browserIDs = browser.getIDsOfNames(new String[] { "Navigate",
					"URL" });
			Variant[] address = new Variant[] { new Variant(url) };
			browser.invoke(browserIDs[0], address, new int[] { browserIDs[1] });
		} catch (Exception ex) {
			System.out.println("Failed to create IE! " + ex.getMessage());
			return;
		}

		while (this != null && !this.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		browser.dispose();
		display.dispose();
	}

	public IEPanel(Display display) {
		super(display);
		this.display = display;
		createContents();

	}

	protected void createContents() {
		setText("如何在Java中嵌入IE？");
		this.setSize(500, 400);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
