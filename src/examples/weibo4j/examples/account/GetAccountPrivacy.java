package weibo4j.examples.account;

import weibo4j.Account;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class GetAccountPrivacy {

	public static void main(String[] args) {
		String access_token ="2.00v5kaoCepUOIBe138b73c36VA1I_E";
		Account am = new Account();
		am.client.setToken(access_token);
		try {
            JSONObject json = am.getAccountPrivacy();
			Log.logInfo(json.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
