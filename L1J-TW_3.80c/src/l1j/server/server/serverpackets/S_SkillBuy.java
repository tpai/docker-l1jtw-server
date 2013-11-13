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

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_SkillBuy extends ServerBasePacket {
	private static Logger _log = Logger.getLogger(S_SkillBuy.class.getName());
	private static final String _S_SKILL_BUY = "[S] S_SkillBuy";

	private byte[] _byte = null;

	public S_SkillBuy(int o, L1PcInstance pc) {
		int count = Scount(pc);
		int inCount = 0;
		for (int k = 0; k < count; k++) {
			if (!pc.isSkillMastery((k + 1))) {
				inCount++;
			}
		}

		try {
			writeC(Opcodes.S_OPCODE_SKILLBUY);
			writeD(100);
			writeH(inCount);
			for (int k = 0; k < count; k++) {
				if (!pc.isSkillMastery((k + 1))) {
					writeD(k);
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public int Scount(L1PcInstance pc) {
		int RC = 0;
		switch (pc.getType()) {
		case 0: // 君主
			if (pc.getLevel() > 20 || pc.isGm()) {
				RC = 16;
			} else if (pc.getLevel() > 10) {
				RC = 8;
			}
			break;

		case 1: // ナイト
			if (pc.getLevel() >= 50 || pc.isGm()) {
				RC = 8;
			}
			break;

		case 2: // エルフ
			if (pc.getLevel() >= 24 || pc.isGm()) {
				RC = 23;
			} else if (pc.getLevel() >= 16) {
				RC = 16;
			} else if (pc.getLevel() >= 8) {
				RC = 8;
			}
			break;

		case 3: // WIZ
			if (pc.getLevel() >= 12 || pc.isGm()) {
				RC = 23;
			} else if (pc.getLevel() >= 8) {
				RC = 16;
			} else if (pc.getLevel() >= 4) {
				RC = 8;
			}
			break;

		case 4: // DE
			if (pc.getLevel() >= 24 || pc.isGm()) {
				RC = 16;
			} else if (pc.getLevel() >= 12) {
				RC = 8;
			}
			break;

		default:
			break;
		}
		return RC;
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
		return _S_SKILL_BUY;
	}

}
