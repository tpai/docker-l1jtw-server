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
package l1j.server.server.model.Instance;

import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.Random;

public class L1FishInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;
	private fishTimer _fishTimer;

	public L1FishInstance(L1Npc template) {
		super(template);
		_fishTimer = new fishTimer(this);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(_fishTimer, 1000, (Random.nextInt(30, 30) * 1000));
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCPack(this));
	}

	private class fishTimer extends TimerTask {

		private L1FishInstance _fish;

		public fishTimer(L1FishInstance fish) {
			_fish = fish;
		}

		@Override
		public void run() {
			if (_fish != null) {
				_fish.setHeading(Random.nextInt(8)); // 隨機面向
				_fish.broadcastPacket(new S_ChangeHeading(_fish)); // 更新面向
				_fish.broadcastPacket(new S_DoActionGFX(_fish.getId(), 0)); // 動作
			} else {
				cancel();
			}
		}
	}

}
