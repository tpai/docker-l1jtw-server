package l1j.server.server.utils;

import java.util.Timer;

import l1j.server.Config;
import l1j.server.server.storage.mysql.MysqlAutoBackup;

public class MysqlAutoBackupTimer {
	/**
	 * Mysql自動備份程序計時器
	 */
	public static synchronized void TimerStart() {
		int minutes = Config.MysqlAutoBackup;
		if (minutes == 0)
			return;
		Timer timer = new Timer();
		timer.schedule(new MysqlAutoBackup(), 60000, minutes * 60000);// 開機1分鐘後,每隔設定之時間備份一次
	}
}
