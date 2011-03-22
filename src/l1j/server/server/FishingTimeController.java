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

package l1j.server.server;

import java.util.List;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.collections.Lists;

public class FishingTimeController implements Runnable {
	private static Logger _log = Logger.getLogger(FishingTimeController.class.getName());

	private static FishingTimeController _instance;

	private final List<L1PcInstance> _fishingList = Lists.newList();

	public static FishingTimeController getInstance() {
		if (_instance == null) {
			_instance = new FishingTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(300);
				fishing();
			}
		}
		catch (Exception e1) {}
	}

	public void addMember(L1PcInstance pc) {
		if ((pc == null) || _fishingList.contains(pc)) {
			return;
		}
		_fishingList.add(pc);
	}

	public void removeMember(L1PcInstance pc) {
		if ((pc == null) || !_fishingList.contains(pc)) {
			return;
		}
		_fishingList.remove(pc);
	}

	private void fishing() {
		if (_fishingList.size() > 0) {
			long currentTime = System.currentTimeMillis();
			for (int i = 0; i < _fishingList.size(); i++) {
				L1PcInstance pc = _fishingList.get(i);
				if (pc.isFishing()) {
					long time = pc.getFishingTime();
					if ((currentTime <= (time + 1000)) && (currentTime >= (time - 1000)) && !pc.isFishingReady()) {
						pc.setFishingReady(true);
						// pc.sendPackets(new S_Fishing());
						pc.sendPackets(new S_PacketBox(S_PacketBox.FISHING));
					}
					else if (currentTime > (time + 1000)) {
						pc.setFishingTime(0);
						pc.setFishingReady(false);
						pc.setFishing(false);
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.broadcastPacket(new S_CharVisualUpdate(pc));
						pc.sendPackets(new S_ServerMessage(1163, "")); // 釣りが終了しました。
						removeMember(pc);
					}
				}
			}
		}
	}

}
