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
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.L1GameTimeClock;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_OwnCharStatus extends ServerBasePacket {
	private static final String S_OWB_CHAR_STATUS = "[S] S_OwnCharStatus";

	private byte[] _byte = null;

	public S_OwnCharStatus(L1PcInstance pc) {
		int time = L1GameTimeClock.getInstance().currentTime().getSeconds();
		time = time - (time % 300);
		// _log.warning((new
		// StringBuilder()).append("送信時間:").append(i).toString());
		writeC(Opcodes.S_OPCODE_OWNCHARSTATUS);
		writeD(pc.getId());

		if (pc.getLevel() < 1) {
			writeC(1);
		}
		else if (pc.getLevel() > 127) {
			writeC(127);
		}
		else {
			writeC(pc.getLevel());
		}
		writeD(pc.getExp());

		writeC(pc.getStr());
		writeC(pc.getInt());
		writeC(pc.getWis());
		writeC(pc.getDex());
		writeC(pc.getCon());
		writeC(pc.getCha());
		writeH(pc.getCurrentHp());
		writeH(pc.getMaxHp());
		writeH(pc.getCurrentMp());
		writeH(pc.getMaxMp());
		writeC(pc.getAc());
		writeD(time);
		writeC(pc.get_food());
		writeC(pc.getInventory().getWeight240());
		writeH(pc.getLawful());
		writeC(pc.getFire());
		writeC(pc.getWater());
		writeC(pc.getWind());
		writeC(pc.getEarth());
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_OWB_CHAR_STATUS;
	}
}