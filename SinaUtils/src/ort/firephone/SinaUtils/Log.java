package ort.firephone.SinaUtils;

import javax.swing.JTextArea;

public class Log {
	final static public int Severity_Emergency = 0;
	final static public int Severity_Alert = 1;
	final static public int Severity_Critical = 2;
	final static public int Severity_Error = 3;
	final static public int Severity_Warning = 4;
	final static public int Severity_Notice = 5;
	final static public int Severity_Informational = 6;
	final static public int Severity_Debug = 6;

	public static JTextArea textArea = null;
	public static int textArea_len = 0;

	static public String getEmsg(Exception e) {
		try {
			String aboutStr = e.toString() + "\n";
			StackTraceElement x[] = e.getStackTrace();
			for (int i = 0; i < x.length; i++) {
				aboutStr += x[i].toString() + "\n";
			}
			return aboutStr;
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return "getEmsg error";
	}

	static public void log(int severity, Exception e) {
		log(getEmsg(e));
	}

	static public void log(int severity, String log) {
		log(log);
	}

	static public void info(String log) {
		log(Log.Severity_Informational, log);
	}

	static synchronized public void log(String str) {
		if (textArea != null) {
			textArea.insert(str + "\r\n", 0);
			textArea_len += str.length();
			if (textArea_len >= 256 * 1024) {
				String msg = "";
				try {
					msg = textArea.getText(0, 64 * 1024);
				} catch (Exception e) {
					e.printStackTrace();
				}
				textArea.setText(msg);
				textArea_len = msg.length();
			}
		} else {
			System.out.println(str);
		}
	}
}
