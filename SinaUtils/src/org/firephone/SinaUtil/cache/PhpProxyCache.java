package org.firephone.SinaUtil.cache;

import java.util.Vector;

import ort.firephone.SinaUtils.Log;
import ort.firephone.SinaUtils.db.RunConfig;

import weibo4j.User;
import weibo4j.Weibo;

public class PhpProxyCache {
	private Vector<String> all_phpproxy = new Vector<String>();
	private static PhpProxyCache ins;
	int current_pos = 0;
	long last_update_time = 0L;
	boolean is_refresh_runing = false;

	public static synchronized PhpProxyCache getIns() {
		if (ins == null) {
			ins = new PhpProxyCache();
		}
		return ins;
	}

	public Vector<String> refresh_new() {
		Vector<String> new_phpproxy = new Vector<String>();
		try {
			String msg = "**************refresh " + RunConfig.NAME_PHPPROXY
					+ " **************\r\n";
			Vector<RunConfig> phpRroxyConfig = RunConfig
					.getByName(RunConfig.NAME_PHPPROXY);
			for (int i = 0; i < phpRroxyConfig.size(); i++) {
				String one = phpRroxyConfig.get(i).getValue();
				if (checkPhpProxy(one)) {
					new_phpproxy.add(one);
					msg += phpRroxyConfig.get(i).getName() + "."
							+ phpRroxyConfig.get(i).getSub_name() + " -- "
							+ phpRroxyConfig.get(i).getValue() + "\r\n";
				} else {
					msg += phpRroxyConfig.get(i).getName() + "."
							+ phpRroxyConfig.get(i).getSub_name() + " -- "
							+ phpRroxyConfig.get(i).getValue()
							+ "  --fail  omit\r\n";
				}
			}
			Log.log(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new_phpproxy;
	}

	public boolean checkPhpProxy(String proxy) {
		try {
			String user_id = "2110504975";
			Weibo w = getWeibo();
			w.setPhpProxyURL(proxy);
			User user = w.showUser("" + user_id);
			if (user == null) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void try_refresh_thread() {
		if (is_refresh_runing) {
			return;
		}
		is_refresh_runing = true;
		try {
			new Thread() {
				public void run() {
					try {
						Vector<String> new_phpproxy = refresh_new();
						setPhpProxy(new_phpproxy);
					} catch (Exception e) {
						e.printStackTrace();
					}
					is_refresh_runing = false;
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
			is_refresh_runing = false;
		}
	}

	private synchronized void setPhpProxy(Vector<String> new_phpproxy) {
		is_refresh_runing = true;
		this.all_phpproxy = new_phpproxy;
		last_update_time = System.currentTimeMillis();
		is_refresh_runing = false;
	}

	private PhpProxyCache() {
		Vector<String> new_phpproxy = refresh_new();
		setPhpProxy(new_phpproxy);
		/*
		 * all_phpproxy.add("http://yanjiangn2.wz13.linkjy.com/s_inf/"); //
		 * all_phpproxy.add("http://yanjian.w14.host400.cn/s_inf/");
		 * all_phpproxy.add("http://yanjian1978.w15.iiskj.net/s_inf/"); //
		 * all_phpproxy.add("http://nuoxxjun.sinaapp.com/s_inf/");
		 * all_phpproxy.add("http://tnotes.sinaapp.com/s_inf/");
		 * all_phpproxy.add("http://yanjiangn.8434.idcice.com/s_inf/");
		 * all_phpproxy.add("http://192.168.77.71/s_inf/"); //
		 * all_phpproxy.add("http://yanjian.usaxx.usazj.com/s_inf/"); //
		 * all_phpproxy.add("");
		 */
	}

	public synchronized String getNext() {
		if ((System.currentTimeMillis() - last_update_time) >= 600 * 1000L) {
			try_refresh_thread();
		}
		if (all_phpproxy.size() == 0) {
			return null;
		}
		if (current_pos < all_phpproxy.size()) {
			return all_phpproxy.get(current_pos++);
		} else {
			current_pos = 0;
			return all_phpproxy.get(current_pos++);
		}
	}

	public synchronized String getNextByFilter(String filter) {
		if ((System.currentTimeMillis() - last_update_time) >= 600 * 1000L) {
			try_refresh_thread();
		}
		if (all_phpproxy.size() == 0) {
			return null;
		}
		for (int i = 0; i < all_phpproxy.size(); i++) {
			if (current_pos < all_phpproxy.size()) {
				String phpproxy = all_phpproxy.get(current_pos++);
				if (phpproxy.indexOf(filter) != -1) {
					return phpproxy;
				}
			} else {
				current_pos = 0;
				String phpproxy = all_phpproxy.get(current_pos++);
				if (phpproxy.indexOf(filter) != -1) {
					return phpproxy;
				}
			}
		}
		return null;

	}

	private static Weibo getWeibo() {
		Weibo weibo = new Weibo();
		weibo
				.setOAuthConsumer("2247832591",
						"766ce77ea1493dfcf2be91d2bf8d8bd4");
		weibo.setToken("27141f99544da62384dfc3ad1de40dd7",
				"6e31ffc889a34c41de09205827a0ef63");
		return weibo;
	}
}
