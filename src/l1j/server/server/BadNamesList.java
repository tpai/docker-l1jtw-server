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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.utils.StreamUtil;

public class BadNamesList {
	private static Logger _log = Logger.getLogger(BadNamesList.class.getName());

	private static BadNamesList _instance;

	private ArrayList<String> _nameList = new ArrayList<String>();

	public static BadNamesList getInstance() {
		if (_instance == null) {
			_instance = new BadNamesList();
		}
		return _instance;
	}

	private BadNamesList() {
		LineNumberReader lnr = null;

		try {
			File mobDataFile = new File("data/badnames.txt");
			lnr = new LineNumberReader(new BufferedReader(new FileReader(
					mobDataFile)));

			String line = null;
			while ((line = lnr.readLine()) != null) {
				if (line.trim().length() == 0 || line.startsWith("#")) {
					continue;
				}
				StringTokenizer st = new StringTokenizer(line, ";");

				_nameList.add(st.nextToken());
			}

			_log.config("loaded " + _nameList.size() + " bad names");
		} catch (FileNotFoundException e) {
			_log.warning("badnames.txt is missing in data folder");
		} catch (Exception e) {
			_log.warning("error while loading bad names list : " + e);
		} finally {
			StreamUtil.close(lnr);
		}
	}

	public boolean isBadName(String name) {
		for (String badName : _nameList) {
			if (name.toLowerCase().contains(badName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public String[] getAllBadNames() {
		return _nameList.toArray(new String[_nameList.size()]);
	}
}
