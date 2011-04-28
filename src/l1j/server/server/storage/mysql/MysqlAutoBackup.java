/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.server.server.storage.mysql;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimerTask;

import l1j.server.Config;
import l1j.server.L1Message;

/**
 * MySQL dump 備份程序
 * 
 * @author L1J-TW-99nets
 */
public class MysqlAutoBackup extends TimerTask {
	private static MysqlAutoBackup _instance;
	final String Username = Config.DB_LOGIN;
	final String Passwords = Config.DB_PASSWORD;
	public static String Database = null;
	static File dir = new File(".\\DbBackup\\");

	public static MysqlAutoBackup getInstance() {
		if (_instance == null) {
			_instance = new MysqlAutoBackup();
		}
		return _instance;
	}

	public MysqlAutoBackup() {
		L1Message.getInstance();
		/** 資料庫名 */
		StringTokenizer sk = new StringTokenizer(Config.DB_URL, "/");
		sk.nextToken();
		sk.nextToken();
		sk = new StringTokenizer(sk.nextToken(), "?");
		Database = sk.nextToken();
		/** 資料夾相關 */
		if (!dir.isDirectory()) {
			dir.mkdir();
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("(MYSQL is backing now...)");
			/**
			 * mysqldump --user=[Username] --password=[password] [databasename]
			 * > [backupfile.sql]
			 */
			StringBuilder exeText = new StringBuilder("mysqldump --user=");
			exeText.append(Username + " --password=");
			exeText.append(Passwords + " ");
			exeText.append(Database + " > ");
			exeText.append(dir.getAbsolutePath()
					+ new SimpleDateFormat("\\yyyy-MM-dd-hhmm")
							.format(new Date()) + ".sql");
			try {
				Runtime rt = Runtime.getRuntime();
				rt.exec("cmd /c " + exeText.toString());
			} finally {
				System.out.println("(MYSQL is backing over.)" + "\n"
						+ L1Message.waitingforuser);// 等待玩家連線
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}