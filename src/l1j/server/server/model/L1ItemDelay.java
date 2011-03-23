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

package l1j.server.server.model;

import l1j.server.server.ClientThread;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1EtcItem;

// Referenced classes of package l1j.server.server.model:
// L1ItemDelay

public class L1ItemDelay {

	private L1ItemDelay() {
	}

	static class ItemDelayTimer implements Runnable {
		private int _delayId;

		private L1Character _cha;

		public ItemDelayTimer(L1Character cha, int id, int time) {
			_cha = cha;
			_delayId = id;
		}

		@Override
		public void run() {
			stopDelayTimer(_delayId);
		}

		public void stopDelayTimer(int delayId) {
			_cha.removeItemDelay(delayId);
		}
	}

	public static void onItemUse(ClientThread client, L1ItemInstance item) {
		int delayId = 0;
		int delayTime = 0;

		L1PcInstance pc = client.getActiveChar();

		if (item.getItem().getType2() == 0) {
			// 種別：その他のアイテム
			delayId = ((L1EtcItem) item.getItem()).get_delayid();
			delayTime = ((L1EtcItem) item.getItem()).get_delaytime();
		}
		else if (item.getItem().getType2() == 1) {
			// 種別：武器
			return;
		}
		else if (item.getItem().getType2() == 2) {
			// 種別：防具

			if ((item.getItem().getItemId() == 20077) || (item.getItem().getItemId() == 20062) || (item.getItem().getItemId() == 120077)) {
				// インビジビリティクローク、バルログブラッディクローク
				if (item.isEquipped() && !pc.isInvisble()) {
					pc.beginInvisTimer();
				}
			}
			else {
				return;
			}
		}

		ItemDelayTimer timer = new ItemDelayTimer(pc, delayId, delayTime);

		pc.addItemDelay(delayId, timer);
		GeneralThreadPool.getInstance().schedule(timer, delayTime);
	}

}
