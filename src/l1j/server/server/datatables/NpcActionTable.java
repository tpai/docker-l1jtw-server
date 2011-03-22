/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.server.datatables;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.npc.action.L1NpcXmlParser;
import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.PerformanceTimer;
import l1j.server.server.utils.collections.Lists;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class NpcActionTable {
	private static Logger _log = Logger.getLogger(NpcActionTable.class.getName());

	private static NpcActionTable _instance;

	private final List<L1NpcAction> _actions = Lists.newList();

	private final List<L1NpcAction> _talkActions = Lists.newList();

	private List<L1NpcAction> loadAction(File file, String nodeName)

	throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);

		if (!doc.getDocumentElement().getNodeName().equalsIgnoreCase(nodeName)) {
			return Lists.newList();
		}
		return L1NpcXmlParser.listActions(doc.getDocumentElement());
	}

	private void loadAction(File file) throws Exception {
		_actions.addAll(loadAction(file, "NpcActionList"));
	}

	private void loadTalkAction(File file) throws Exception {
		_talkActions.addAll(loadAction(file, "NpcTalkActionList"));
	}

	private void loadDirectoryActions(File dir) throws Exception {
		for (String file : dir.list()) {
			File f = new File(dir, file);
			if (FileUtil.getExtension(f).equalsIgnoreCase("xml")) {
				loadAction(f);
				loadTalkAction(f);
			}
		}
	}

	private NpcActionTable() throws Exception {
		File usersDir = new File("./data/xml/NpcActions/users/");
		if (usersDir.exists()) {
			loadDirectoryActions(usersDir);
		}
		loadDirectoryActions(new File("./data/xml/NpcActions/"));
	}

	public static void load() {
		try {
			PerformanceTimer timer = new PerformanceTimer();
			System.out.print("loading npcaction...");
			_instance = new NpcActionTable();
			System.out.println("OK! " + timer.get() + "ms");
		}
		catch (Exception e) {
			_log.log(Level.SEVERE, "NpcActionを読み込めませんでした", e);
			System.exit(0);
		}
	}

	public static NpcActionTable getInstance() {
		return _instance;
	}

	public L1NpcAction get(String actionName, L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _actions) {
			if (action.acceptsRequest(actionName, pc, obj)) {
				return action;
			}
		}
		return null;
	}

	public L1NpcAction get(L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _talkActions) {
			if (action.acceptsRequest("", pc, obj)) {
				return action;
			}
		}
		return null;
	}
}
