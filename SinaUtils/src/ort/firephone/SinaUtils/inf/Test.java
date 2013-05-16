package ort.firephone.SinaUtils.inf;

import java.util.List;

import org.firephone.SinaUtil.cache.FcorpseCache;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.db.Fcorpse;
import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class Test {
	public static void main(String argv[]) {
		Env.initial();
		Weibo w = getWeibo();
		//w.setPhpProxyURL("http://nuoxxjun.sinaapp.com/s_inf/");
		// w.setPhpProxyURL("http://fpbook.yanjian.com/s_inf/");
		try {
			String user_id = "2110504975";
			
			List<Status> statuses = w.getUserTimeline(user_id, new Paging(1,
					200));
			w.setPhpProxyURL("http://yanjiangn2.208.idcice.net/s_inf/");
			List<Status> statuses_p = w.getUserTimeline(user_id, new Paging(1,
					200));
			for(int i=0;i<statuses.size();i++){
				Status a = statuses.get(i);
				Status b = statuses_p.get(i);
				if(!a.toString().equals(b.toString())){
					System.out.println(a);
					System.out.println(b);
					System.out.println("----------------");
				}else{
					System.out.println("----------------ok");
				}
			}
			
			if (statuses.size() == 200) {
				List<Status> statuses2 = w.getUserTimeline("" + user_id,
						new Paging(2, 200));
				statuses.addAll(statuses2);
			}
			System.out.println(statuses.size());
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
