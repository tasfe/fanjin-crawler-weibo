package ort.firephone.SinaUtils.OneEvent;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.firephone.SinaUtil.cache.FcorpseCache;

import ort.firephone.SinaUtils.Env;
import ort.firephone.SinaUtils.db.Fcorpse;
import ort.firephone.SinaUtils.db.Fmember;
import ort.firephone.SinaUtils.db.Fuser;
import ort.firephone.SinaUtils.db.FuserOmit;
import weibo4j.Comment;
import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * 
 * 按郭美美的ID,1741865482， 按郭美美的某一个贴子上发评论的人列表，把所有微博ID找出来，按这个ID去其发的贴了，只要和郭美美相关的就评论支持
 */

// guomeimeifeng@sina.com fp1234 How2BreakY0u
// open 696533876 7983b439ac114ee5b33287d66501936d
/*
 * kangxuya91@sina.com----k77f9macrb luo81947@sina.com----u6jvhzb9cw
 * er19911983@sina.com----wp1lcn6iec chun91zhao@sina.com----eld75qmpvt
 * moxing2023@sina.com----eqimc5rkat gongyoute@sina.com----iipn42ifpy
 * shunkua920@sina.com----omyxi59vxp duanaxxvihcre@sina.com----nmhzdkmwmz
 * jiang9qdvzbrgb@sina.com----ejjnvnxqbk zhu80ha@sina.com----tk0jwqfpbj
 * yaman4709@sina.com----fe9y6tv8vy liangnbortttvu@sina.com----dkzmjjbwpx
 * yuwu6451@sina.com----gdtalefr4u
 */
public class guomeimei {
	// http://weibo.com/profile.php?uid=1741865482
	public static long GUOMEIMEI_ID = 1741865482;

	public String do_comment_id = "13040572854";

	public static void main(String argv[]) {
		String param1 = "2";
		if (argv.length >= 1) {
			param1 = argv[0];
		}
		Env.initial();
		guomeimei g = new guomeimei();
		g.postend();
		// g.getStatus(fguomeimei);
		if (param1.equals("1")) {
			Vector<Hashtable> vh = Fuser.getByFilter("%郭美%", "%红十字会%");
			g.getComments(g.do_comment_id, vh);
		} else {
			g.getUserFromComments(g.do_comment_id);
		}
	}

	public void postend() {
		Vector<Fcorpse> fcs = Fcorpse.getAllActive();
		for (int i = 0; i < fcs.size(); i++) {
			Fcorpse fc = fcs.get(i);
			try {
				//String msg = CommentCache2.getIns().getNext();
				//getWeibo(fc).updateComment(msg, do_comment_id, null);
				//System.out.println("--");
				//sleep(5000);
				getWeibo(fc).updateStatus("结束了，自杀了，要骂的请烧纸");
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getStatus(Fmember fguomeimei) {
		try {
			List<Status> allstatus = getWeibo(fguomeimei).getUserTimeline(
					"1741865482", new Paging(1, 200));
			for (int i = 0; i < allstatus.size(); i++) {
				System.out.println(allstatus.get(i).getId() + ","
						+ allstatus.get(i).getText());
			}

		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 从一个贴子中取出所有评论，取所有评论的作者详细信息。
	 * 
	 * @param s_id
	 */
	public void getUserFromComments(String s_id) {
		int page = 1;
		int since_num = 1;
		Fcorpse fc = FcorpseCache.getIns().getNextOne();
		while (true) {
			try {
				fc = FcorpseCache.getIns().getNextOne();
				Paging paging = new Paging(page, 200, since_num);
				page++;
				since_num += 200;
				List<Comment> comments = getWeibo(fc).getComments(s_id, paging);
				if (comments.size() == 0) {
					System.out.println("0000000");
				}
				for (int i = 0; i < comments.size(); i++) {
					Comment comment = comments.get(i);
					if (Fuser.isExist(comment.getUser().getId())) {
						continue;
					}
					fc = FcorpseCache.getIns().getNextOne();
					User user = getWeibo(fc).showUser(
							"" + comment.getUser().getId());
					sleep(100L);
					if (user != null) {
						Fuser.insertIntoDb(user);
						String ret = fc.getSina_user() + " -do- "
								+ comment.getUser().getId() + " ("
								+ user.getName() + ")  oked";
						System.out.println(ret);
					}
				}
				sleep(100L);

			} catch (Exception e) {
				e.printStackTrace();
				sleep(5 * 60 * 1000L);
			}
		}
	}

	public void SaveUserInfo(String id) {
		try {
			Fcorpse fc = FcorpseCache.getIns().getNextOne();
			User user = getWeibo(fc).showUser(id);
			sleep(100L);
			if (user != null) {
				Fuser.insertIntoDb(user);
				String ret = fc.getSina_user() + " do " + id + " ("
						+ user.getName() + ")  oked";
				System.out.println(ret);
			}
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sleep(4 * 60 * 1000L);
		}
	}

	/**
	 * 从一个贴子中取出所有评论，对所有评论进行回复。
	 * 
	 * @param s_id
	 */
	public void getComments(String s_id, Vector<Hashtable> vh) {
		Fcorpse fc = null;

		int page = 1;
		int since_num = 1;

		int vh_i = 1;
		int roll_num = 0;
		while (true) {
			try {
				if (vh_i < vh.size()) {
					Hashtable h = vh.get(vh_i++);
					Long user_id = Long.parseLong(h.get("id").toString());
					String status_id = h.get("status-id").toString();
					String screen_name = h.get("screen_name").toString();
					if (FuserOmit.isExist(user_id)) {
						continue;
					}
					FuserOmit one = new FuserOmit();
					one.setSina_id(user_id);
					one.setCode(1);
					one.insertIntoDb();
					fc = FcorpseCache.getIns().getNextOne();
					String add_str = screen_name;
					getWeibo(fc).updateComment(
							add_str + " " + CommentCache.getIns().getNext(),
							status_id, null);
					System.out.println("#1#  " + screen_name + " " + status_id
							+ "  " + fc.getSina_id() + fc.getOauth_name() + "");
					sleep(2000L);
				}
			} catch (Exception eee) {
				eee.printStackTrace();
				if (eee.getMessage().indexOf("由于用户设置，你无法进行评论") != -1) {

				} else {
					sleep(4 * 60 * 1000L);
				}
			}

			try {
				Paging paging = new Paging(page, 200, since_num);
				page++;
				since_num += 200;
				fc = FcorpseCache.getIns().getNextOne();
				List<Comment> comments = getWeibo(fc).getComments(s_id);
				for (int i = 0; i < comments.size(); i++) {
					Comment comment = comments.get(i);

					if (comment.getText().indexOf("郭小美美") != -1
							|| comment.getText().indexOf("支持小美") != -1) {
						continue;
					}

					if (FuserOmit.isExist(comment.getUser().getId())) {
						continue;
					}
					if (Fuser.isExist(comment.getUser().getId())) {
						SaveUserInfo("" + comment.getUser().getId());
					}
					FuserOmit one = new FuserOmit();
					one.setSina_id(comment.getUser().getId());
					one.setCode(1);
					one.insertIntoDb();
					String comment_username = comment.getUser().getName();
					repostComment(s_id, comment);
					sleep(2000L);
				}
			} catch (Exception ee) {
				ee.printStackTrace();
				if (ee.getMessage().indexOf("由于用户设置，你无法进行评论") != -1) {

				} else {
					sleep(4 * 60 * 1000L);
				}
			}

		}

	}

	public static void sleep(long usec) {
		try {
			Thread.sleep(usec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	long createfriendship_sec = 0;

	public void repostComment(String s_id, Comment comment) {
		String t_str = com.skyzoo.Jutil.Sys.getTimeString(null);
		String add_str = comment.getUser().getName();
		Fcorpse fc = FcorpseCache.getIns().getNextOne();
		String comment_str = CommentCache.getIns().getNext();
		try {
			getWeibo(fc).updateComment(add_str + " " + comment_str, s_id,
					"" + comment.getId());

			System.out.println("#2#  " + comment.getUser().getName() + "#"
					+ comment.getText() + "#" + comment_str + "#"
					+ fc.getSina_id() + "(" + fc.getOauth_name() + ")");

		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (e.getMessage().indexOf("由于用户设置，你无法进行评论") != -1) {

			} else {
				sleep(4 * 60 * 1000L);
			}
		}

		try {
			if (createfriendship_sec < System.currentTimeMillis()) {
				getWeibo(fc).createFriendship("" + comment.getUser().getId());
			}
		} catch (WeiboException ee) {
			createfriendship_sec = System.currentTimeMillis() + 3600 * 1000L;
			ee.printStackTrace();
		}
	}

	public void doCreateFriendship(Fmember fguomeimei, String id) {
		try {
			getWeibo(fguomeimei).createFriendship(id);
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Weibo getWeibo(Fcorpse fc) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fc.getApp_key(), fc.getApp_secret());
		weibo.setToken(fc.getOauth_token(), fc.getOauth_token_secret());
		return weibo;
	}

	private static Weibo getWeibo(Fmember fm) {
		Weibo weibo = new Weibo();
		weibo.setOAuthConsumer(fm.getApp_key(), fm.getApp_secret());
		weibo.setToken(fm.getOauth_token(), fm.getOauth_token_secret());
		return weibo;
	}

}

class CommentCache2 {
	Vector<String> vs = new Vector<String>();
	int c_n = 0;
	private static CommentCache2 ins = null;

	private CommentCache2() {
		vs.add("发现一直纠缠在郭美美这个问题上的人一定是闲的蛋疼的人。不然就是自己没什么钱的，看人家炫富眼红的。人家郭美美有钱碍着你们什么事了，她又没找你们要过一分钱。人家二奶怎么了，有那本事你们也去啊。总的来说骂郭美美的就是没本事的男人和丑不垃圾的女人");

		vs.add("为什么人们总是这么无聊呢。其实你也没什么错，开心一点。时间长了就好了。现在的人真八婆，就知道没事说别人");

		vs.add("仔细思考一下网络暴力其实很可怕、很恐怖！人肉公民、跟踪公民、暴露公民隐私是很过瘾、很给力，但也很恐怖！其威力和破坏力远远大于“文*革”时期的大字报。");
		vs.add("微博中人肉、跟踪和暴露隐私，实施网络暴力可以成为全国乃至全球的焦点事件，对于公民的负面影响和精神损害是不可估量的。");
		vs.add("人肉郭美美尽管事出有因，但是行使网络暴力是绝对不能提倡的。人肉和跟踪以及暴露网友个人隐私这样的行为实在是为人不齿也是违法的，建议立法部门明确提出制止网络暴力的条款，以保护网友的隐私权和法律赋予公民的个人权力。");

		vs.add("一个正常的法制国家是不能利用网络人肉和追踪公民暴露公民隐私来达到反腐败打击违法犯罪行为的");
		vs.add("我一直反感人肉和利用博客和微博侵犯它人隐私。那些媒体记者倒是挺敬业，只是我觉得没用对地方哈，有点像大陆人鄙视的海外及港台八卦记者很无聊。有种你直接采访某会去！");
		vs.add("围观@郭美美Baby 太离谱。她本人被追踪，很多隐私信息被网友公布在围脖上。这些做法不值得倡导，它侵害了公民的隐私权。");
		vs.add("对于这些个网络暴力不能容忍，受害者应该拿起法律武器维护自身的合法权益，只有人人敬畏法律、尊重法律、严格遵守法律，法律才有尊严建立法制社会的梦想才能变成现实。");

		vs.add("有些网友微博直播郭美美昨天在深圳机场登机，有些发出郭美美的身份证和登机牌和北京住址等信息，其行为确实有侵害公民隐私权的嫌疑。这种人肉和跟踪暴露它人隐私的行为是为正常的法制社会不齿也是不能被容忍的。");
		vs.add("【谁再炒作“郭美美”事件，小心追究法律责任】中国红十字会总会已向公安机关报案，并启动法律程序，维护红十字会的合法权益，维护中国红十字会的良好声誉。网上流传的......纯属不负责任的谣言。对今后继续恶意炒作此事件的单位和个人，红十字会将保留进一步追究其法律责任的权利");
		vs.add("也许我这样说很多人会不太高兴,但是我想说的是,我喜欢把自己当作一个OUTSIDER,冷静一点,你就会清醒很多.就现在而言,我的眼中,大家正在做的只是在用这样的方式将自己生活中那些委屈不平表达出来罢了.但是,何必呢?用这样的方式?这样去不放过一个小女生");

	}

	public String getNext() {
		if (c_n >= vs.size()) {
			c_n = 0;
		}
		return vs.get(c_n++);
	}

	public static CommentCache2 getIns() {
		if (ins == null) {
			ins = new CommentCache2();
		}
		return ins;
	}

}

class CommentCache {
	Vector<String> vs = new Vector<String>();
	int c_n = 0;
	private static CommentCache ins = null;

	private CommentCache() {

		vs.add("很多事不能由大家一起造谣郭美美就能决定的，不在网上乱发过激言论，后果要自付的");
		vs.add("跟大大家一起造谣郭美美，并不是说你不用负责任，你看着如何办你");
		vs.add("不要以为大家一起造谣郭美美时，你跟着造谣就没有事，网上还是小心一点的好");
		vs.add("造谣是要受报应的，你等着吧，马上给你报应，不要再造谣郭美美事件了");
		vs.add("有人在造谣郭美美事件，你不要跟着了，对你有什么好处呢！");
		vs.add("有点同情好吧，这样整人家，全是造谣，你跟着一起来造谣，等着被连累吧");

		vs.add("这件事实对郭美美不公平，大家不要这样对待一个无辜的人，要有点同情心嘛!");
		vs.add("这里的人全是暴民！，不能这样，不公平");
		vs.add("冒天下大不韪为郭美美呼吁，请看官注意，罪魁祸首不是她！");
		vs.add("她只是一个不知轻重从底层突然升入天堂的灰姑娘！2008年还在住廉租房,大家何必呢");
		vs.add("要追究的是她身后的腐败分子！他们拥有特权，攻城略地，而她，和你我一样，都是祭坛上的羔羊！！！");
		vs.add("放过她！揪出权贵才是正道！对郭美美不公平 ");
		vs.add("我们都愿意相信明天会更好，但我发现明天经常在欺骗今天,今天的确不是郭美美的错");
		vs.add("大家说的都没有错，但不要老是针对她嘛，她无错啊，");
		vs.add("大家不要这样对待一个无辜的人，这件事实对郭美美不公平，这里的人全是暴民！");
		vs.add("冒天下大不韪为郭美美呼吁，对郭美美不公平");
		vs.add("她只是一个不知轻重从底层突然升入天堂的灰姑娘！这件事实对郭美美不公平");
		vs.add("大家对红十字有意见，可以通过正常渠道去发泄，不要总是找个无辜的人！ 放过郭美美吧");
		vs.add("讨伐的是不合理的慈善制度！ 其实无关郭美美啊");
		vs.add("郭美美又不是红十字的官员，能让她如何解释什么嘛，大家放过她吧。");
		vs.add("这样对待一个弱女子，大家于心何忍。");
		vs.add("这就是网络暴力，对谁都不公平，不可以这样对付一个弱女子。");
		vs.add("国有国法，大家这样做是犯法的，不能这样网络暴力对待郭美美。");
		vs.add("中国是法制社会，有本事去法院告，在网络上搞暴力对郭美美不公。");
		vs.add("冲动是魔鬼，在网络上搞暴力对郭美美,不怕有后果吗");
		vs.add("在这个法制社会，大家说话最好还是小心一点，要负责任的，放过郭小美美");
		vs.add("并不是说在网络上就可以胡说八道，一样的付责任的，各位，郭小美美也付了代价，你们放过她吧");
		vs.add("如果不放过小郭，你们这样其实也是在犯法，你们就值吗?");
	}

	public String getNext() {
		if (c_n >= vs.size()) {
			c_n = 0;
		}
		return vs.get(c_n++);
	}

	public static CommentCache getIns() {
		if (ins == null) {
			ins = new CommentCache();
		}
		return ins;
	}

}
/*
 * 13040572854,数日来，本人已经受到家人和朋友的严厉教育和谴责！这几天我一直在反省这个给大家造成了伤害和烦恼的愚昧无知行为，希望各位网友不要再炒作，
 * 我决定用更多的时间来自我反省和自我教育！
 * 13040173058,本人杜撰这个身份是完全没有恶意的，请各位网友原谅！但本人万万没有想到这个愚昧行为会导致网友对中国红十字会产生误解
 * ，本人在此向中国红十字会鞠躬道歉，并向关心此事的网友深深致歉！
 * 13039755642,本人出于无知在新浪微博上自称为“中国红十字会商业总经理”，对此愚昧行为给中国红十字会造成的名誉损害和公众误解深表歉意
 * ！本人从未在中国红十字会工作，这个身份完全是本人杜撰出来的。
 * 12771051164,本人郑重声明：1.我和红十字协会没有任何关系；2.我不是红十字会会长或郭沫若先生的子孙
 * 。3.我当时新浪认证是演员。4.我所说的红十字商会并不是公司或机构名称
 * 。5.其他我不想解释太多，舆论和谣言太可怕，你们爱怎么说都可以但请不要攻击我的家人，我们也是纳税人挣自己该挣的花自己该花的碍着你们了吗
 * 12643207148,我始终相信，有些表情，只有面对某个人的时候才会出现。有些话，只有看到某个人才会说。有些事，只为那个人去做。这就是爱吧？[心]
 * 12586539642,【十二星座六月爱情旺、事业旺、财运旺三喜临门排行榜】
 * 冠军(天秤座)、亚军(双子座)、季军(射手座)、第4名(狮子座)、第5名(天蝎座
 * )、第6名(金牛座)、第7名(巨蟹座)、第8名(白羊座)、第9名(水瓶座)、
 * 第10名(处女座)、第11名(双鱼座)、第12名(魔羯座)。你会三喜临门不？[鼓掌]
 * 12205153576,好久没吃烧烤了，后现代的烧烤真是让人流口水[花心
 * ]对面的小胖纸要辣哭了[哈哈]吃吃吃，让我们变成两只可爱滴小胖胖吧[馋嘴]其实现在已经是小胖纸了。。[偷笑]天蝎跟双子怎么就这么和谐咧[挖鼻屎]
 * 12187283874,【分手后，会一直等待的星座】冠军:
 * 双子座；【吵架后,会先主动道歉的星座】冠军:双子座;【富有后,最懂感恩的星座】冠军:双子座;【结婚后
 * ,会主动做家务的星座】冠军:双子座;【最适合做另一半的星座】冠军:双子座; 是双子的麻烦高调转发[酷]崽崽你看到没[鼓掌][耶][害羞][害羞]
 * 12093470138,纠结了一晚上唱不唱歌最后结果－Go home[睡觉]今天终于女人一回了免得某人天天说我像小孩[不要]大伙儿觉得我今天女人不[委屈]
 * 12033355225
 * ,【12星座谁最痴心】冠军（双子：很花心却对某段感情很痴情）、亚军（双鱼：很随意但是爱上了就不离不弃）、季军（双鱼）第四（水瓶）、第五
 * （天平）、第六（魔羯）、第七（金牛）、第八（白羊）、第九（天蝎）、第十（狮子）、第十一（巨蟹）、第十二（射手）[生病]我中暑了头疼头晕噁心[晕]我要吃雪糕
 * 11955204868,有点受宠若惊[晕][鼓掌]一大早收到粉丝@骑驴找马yi
 * 的五大袋包裹，谢谢你的礼物，但。。似乎有点。。多吧？我妈让我问你家是不是开玩具厂的
 * [哈哈]我现在头疼这些娃娃该往哪摆，房间客厅都摆满了只能堆妈咪房里了[嘘]
 * 可怜我家崽崽大热天冒着大太阳跟保姆把娃娃拿回家，[亲亲]一个~崽崽有点吃醋喔[偷笑]
 * 11423038476,在家静养了两天，今儿天气晴朗崽崽带我去看功夫熊猫
 * 。这次车祸不止给我带来身体上的伤痛更多是心理，忽然觉得生命是那么脆弱，所有的美好或烦恼可能瞬间即逝
 * ，所以我们一定要好好珍惜身边爱你和你爱的人，不要在生命逝去的那一刻才后悔。做你认为对的开心的事，不管多艰难都要坚持[可爱][爱你]
 * 11332633874,
 * 好感动。。有这么多人关心。定的对方全责，我现在头疼也不知道是被气囊嘣的还是饿的，拖车的得半小时才到，我好饿[泪]一会还要先去医院全面检查
 * ，什么时候能吃上饭啊[晕][泪]
 * 11283762346,今天小白限行把小MINI开出遛遛～开着有点不习惯[挖鼻屎]一个人去三里屯修指甲好孤独呀，有木有人过来喝东西？
 * 11176574646,【十二星座之最】 最多女强人（双子）、
 * 最少女强人（巨蟹）、最会逗人开心（天秤）、最会投机取巧（双子）、最不会投机取巧（山羊）、最没有肚量
 * （山羊）、最有肚量（射手）、最不会讨人欢心（狮子）、最会讨人欢心（双鱼）、最有异性缘（金牛&天秤&天蝎&处女）Ps我目前状态女强人有点远，小女人还行
 * 11078227054,猜猜这是谁？[偷笑]跟妈妈在家看小时候的照片各种搞笑，原来我从小就是个臭美的小妮子～
 * 11028585896,加勒比呀加勒比海盗今天终于看上了，还是买的黄牛票[泪]感觉一般没以前搞笑剧情也一般，But，I like强尼戴普[爱你]
 * 10976016386,骑大马呀骑大马[爱你]骑了一小时下来腿走路都没力气了，以后每周都要跟崽崽来骑大马[可爱]开心～幸福ing[爱你]
 * 10967200260,第一次抱小狮子，还没断奶呢[可爱]肥嘟嘟的一顿扭一点都不配合，毛很舒服虽然很可爱。。但是很臭很臭很臭[晕]要臭晕了。
 * 10876607941
 * ,上来冒个泡，睡前小性感一下[可爱]我现在依然幸福，是的。。依然快乐[爱你]大家的留言都未能及时回复因为今天太累了，善良的人儿们都早点休息吧
 * ，安安啦崽崽～[爱你]
 */
