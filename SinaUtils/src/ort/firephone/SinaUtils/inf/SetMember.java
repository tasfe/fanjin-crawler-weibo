package ort.firephone.SinaUtils.inf;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.db.Fcorpse;

import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;
import weibo4j.util.BareBonesBrowserLaunch;

public class SetMember {
	/**
	 * Usage: java -DWeibo4j.oauth.consumerKey=[consumer key]
	 * -DWeibo4j.oauth.consumerSecret=[consumer secret]
	 * Weibo4j.examples.OAuthUpdate [message]
	 * 
	 * @param args
	 *            message
	 */
	public static void main(String[] args) {
		try {
			Env.initial();
			Vector<Fcorpse> empty_members = Fcorpse.getEmpty();
			for (int i = 0; i < empty_members.size(); i++) {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Fcorpse one = empty_members.get(i);
				System.out.println(i + "/" + empty_members.size() + "  user="
						+ one.getSina_user() + "  password="
						+ one.getSina_pass());
				Weibo weibo = new Weibo();
				weibo.setOAuthConsumer(Env.CONSUMER_KEY, Env.CONSUMER_SECRET);
				// set callback url, desktop app please set to null
				// http://callback_url?oauth_token=xxx&oauth_verifier=xxx
				RequestToken requestToken = weibo.getOAuthRequestToken();

				System.out.println("Got request token.  ");
				System.out.println("Request token: " + requestToken.getToken());
				System.out.println("Request token secret: "
						+ requestToken.getTokenSecret());
				AccessToken accessToken = null;

				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				while (null == accessToken) {
					System.out
							.println("Open the following URL and grant access to your account:");
					System.out.println(requestToken.getAuthorizationURL());
					String reg_url = requestToken.getAuthorizationURL();
					reg_url += "&action=submit";
					reg_url += "&userId=" + one.getSina_user();
					reg_url += "&passwd=" + one.getSina_pass();
					String pin = getCode(reg_url);
					if (pin == null) {
						System.out.println("pin error");
						break;
					}

					/*
					 * BareBonesBrowserLaunch.openURL(requestToken
					 * .getAuthorizationURL());
					 * 
					 * System.out
					 * .print("Hit PIN, or  type 'skip' skip this user [Enter]:"
					 * );
					 * 
					 * String pin = br.readLine(); pin = pin.trim(); if
					 * (pin.equals("skip")) { break; }
					 */

					System.out.println("pin: " + br.toString());
					try {
						accessToken = requestToken.getAccessToken(pin);
					} catch (WeiboException te) {
						if (401 == te.getStatusCode()) {
							System.out
									.println("Unable to get the access token.");
						} else {
							te.printStackTrace();
						}
					}
					break;
				}
				if (accessToken == null) {
					System.out.println("\n-------------skip-----------\n");
					continue;
				}
				// System.out.println("Got access token.");
				System.out.println("Access token: " + accessToken.getToken());
				System.out.println("Access token secret: "
						+ accessToken.getTokenSecret());
				Fcorpse corpse = new Fcorpse();
				corpse.setApp_key(Env.CONSUMER_KEY);
				corpse.setApp_secret(Env.CONSUMER_SECRET);
				corpse.setSina_user(one.getSina_user());
				corpse.setSina_pass(one.getSina_pass());
				corpse.setOauth_token(accessToken.getToken());
				corpse.setOauth_token_secret(accessToken.getTokenSecret());
				corpse.setOauth_name(accessToken.getScreenName());
				corpse.setSina_id(accessToken.getUserId());
				corpse.setActive(1);
				corpse.insertOrUpdateToDB();
			}

			System.exit(0);
		} catch (WeiboException te) {
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		} catch (Exception ioe) {
			System.out.println("Failed to read the system input.");
			System.exit(-1);
		}
	}

	public static String getCode(String url_str) {
		String code = null;
		try {
			int b;
			String str = "";
			URL url = new URL(url_str);
			InputStream is = url.openStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			while ((b = is.read()) != -1) {
				str = str + (char) b;
			}
			is.close();
			bis.close();
			String lines[] = str.split("\n");
			for (int i = 0; i < lines.length; i++) {
				String line = lines[i];
				if (line.indexOf("getCodeWrap") != -1) {
					System.out.println(line);
					int pos_s = line.indexOf("<span class=\"fb\">");
					int pos_e = line.indexOf("</span>");
					code = line.substring(pos_s + 17, pos_e);
					System.out.println(code);
					break;
				}
			}

			// str = new String(str.getBytes("ISO-8859-1"), "GB2312");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;

	}

}
