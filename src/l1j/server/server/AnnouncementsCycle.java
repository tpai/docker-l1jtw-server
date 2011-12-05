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
package l1j.server.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javolution.util.FastList;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class AnnouncementsCycle {
	private int round = 0;
	private String line = null;
	private boolean firstboot = true;;
	private StringBuffer sb = new StringBuffer();
	private static AnnouncementsCycle _instance;

	/** 緩衝讀取 */
	private static BufferedReader buf;

	/** announcementsCycle文件的位置 */
	private static File dir = new File("data/announceCycle.txt");

	/** 紀錄上一次修改時間 */
	private static long lastmodify = dir.lastModified();

	/** 在公告首顯示公告修改時間 */
	private boolean AnnounceTimeDisplay = Config.Announcements_Cycle_Modify_Time;

	/** 容器 */
	List<String> list = new FastList<String>();

	private AnnouncementsCycle() {
		cycle();
	}

	public static AnnouncementsCycle getInstance() {
		if (_instance == null) {
			_instance = new AnnouncementsCycle();
		}
		return _instance;
	}

	/**
	 * 從announcementsCycle.txt將字串讀入
	 */
	private void scanfile() {
		try {
			fileEnsure(); // 先確保檔案存在
			if (dir.lastModified() > lastmodify || firstboot) { // 如果有修改過
				list.clear(); // 清空容器
				buf = new BufferedReader(new InputStreamReader(new FileInputStream(dir)));
				while ((line = buf.readLine()) != null) {
					if (line.startsWith("#")||line.isEmpty()) // 略過註解
						continue;
					sb.delete(0, sb.length()); // 清空 buffer [未來擴充用]
					list.add(line);
				}
				lastmodify = dir.lastModified(); // 回存修改時間
			} else {
				// 檔案沒修改過，不做任何事。
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				buf.close();
				firstboot = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 確保announcementsCycle.txt存在
	 * 
	 * @throws IOException
	 *             產生檔案錯誤
	 */
	private void fileEnsure() throws IOException {
		if (!dir.exists())
			dir.createNewFile();
	}

	private void cycle() {
		AnnouncementsCycleTask task = new AnnouncementsCycleTask();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(task, 100000, 60000 * Config.Announcements_Cycle_Time); // 10分鐘公告一次
	}

	/**
	 * 處理廣播字串任務
	 */
	class AnnouncementsCycleTask implements Runnable {
		@Override
		public void run() {
			scanfile();
			// 啟用修改時間顯示 - 〈yyyy.MM.dd〉
			if (AnnounceTimeDisplay) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
				ShowAnnouncementsCycle("〈"+ formatter.format(new Date(lastmodify)) + "〉");
			}
			Iterator<String> iterator = list.listIterator();
			if (iterator.hasNext()) {
				round %= list.size();
				ShowAnnouncementsCycle(list.get(round));
				round++;
			}
		}
	}

	/**
	 * 把字串廣播到伺服器上
	 */
	private void ShowAnnouncementsCycle(String announcement) {
		Collection<L1PcInstance> AllPlayer = L1World.getInstance().getAllPlayers();
		for (L1PcInstance pc : AllPlayer)
			pc.sendPackets(new S_SystemMessage(announcement));
	}
}
