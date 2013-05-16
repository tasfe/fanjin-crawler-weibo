package ort.firephone.SinaUtils.inf;

import ort.firephone.SinaUtils.Env;

public class RollGetFavFollowers {
	static int getUserInfo_step = 0;
	static int getTimeLine_step = 0;
	static int getFollowerIds_step = 0;

	public static void main(String argv[]) {
		try {
			Env.initial();
			while (true) {
				try {

					if (getFollowerIds_step == 0) {
						new Thread() {
							public void run() {
								(new GetFollowerIds()).run();
								Sleep(4 * 60 * 60 * 1000L);
								getFollowerIds_step = 1;
							}
						}.start();
					}

					if (getUserInfo_step == 0) {
						getUserInfo_step = 1;
						new Thread() {
							public void run() {
								GetUserInfo getUserInfo = new GetUserInfo();
								getUserInfo.setEatGap(200);
								getUserInfo.setThreadNum(8);
								getUserInfo.start();
								getUserInfo.processAllApple();
								getUserInfo_step = 0;
							}
						}.start();
					}

					if (getTimeLine_step == 0) {
						getTimeLine_step = 1;
						new Thread() {
							public void run() {
								GetTimeLine getTimeLine = new GetTimeLine();
								getTimeLine.setEatGap(800);
								getTimeLine.setThreadNum(8);
								getTimeLine.start();
								getTimeLine.processAllApple();
								getTimeLine_step = 0;
							}
						}.start();
					}
					Sleep(360 * 1000L);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Thread.sleep(Env.roll_gap_sec * 1000L);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doOnce() {
		(new GetFollowerIds()).run();

		GetUserInfo getUserInfo = new GetUserInfo();
		getUserInfo.start();
		getUserInfo.processAllApple();

		GetTimeLine getTimeLine = new GetTimeLine();
		getTimeLine.start();
		getTimeLine.processAllApple();
	}

	public static void Sleep(long usec) {
		try {
			Thread.sleep(usec);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
