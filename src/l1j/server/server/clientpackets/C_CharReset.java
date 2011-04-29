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
package l1j.server.server.clientpackets;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharReset;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.utils.CalcInitHpMp;
import l1j.server.server.utils.CalcStat;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到客戶端傳來角色升級/出生的封包
 */
public class C_CharReset extends ClientBasePacket {

	private static final String C_CHAR_RESET = "[C] C_CharReset";

	private static Logger _log = Logger.getLogger(C_CharReset.class.getName());

	/**
	 * //配置完初期點數 按確定 127.0.0.1 Request Work ID : 120 0000: 78 01 0d 0a 0b 0a 12
	 * 0d
	 * 
	 * //提升10及 127.0.0.1 Request Work ID : 120 0000: 78 02 07 00 //提升1及
	 * 127.0.0.1 Request Work ID : 120 0000: 78 02 00 04
	 * 
	 * //提升完等級 127.0.0.1 Request Work ID : 120 0000: 78 02 08 00 x...
	 * 
	 * //萬能藥 127.0.0.1 Request Work ID : 120 0000: 78 03 23 0a 0b 17 12 0d
	 */

	public C_CharReset(byte abyte0[], ClientThread clientthread) {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		int stage = readC();

		if (stage == 0x01) { // 0x01:キャラクター初期化
			int str = readC();
			int intel = readC();
			int wis = readC();
			int dex = readC();
			int con = readC();
			int cha = readC();
			int hp = CalcInitHpMp.calcInitHp(pc);
			int mp = CalcInitHpMp.calcInitMp(pc);
			pc.sendPackets(new S_CharReset(pc, 1, hp, mp, 10, str, intel, wis, dex, con, cha));
			initCharStatus(pc, hp, mp, str, intel, wis, dex, con, cha);
			CharacterTable.getInstance();
			CharacterTable.saveCharStatus(pc);
		}
		else if (stage == 0x02) { // 0x02:ステータス再分配
			int type2 = readC();
			if (type2 == 0x00) { // 0x00:Lv1UP
				setLevelUp(pc, 1);
			}
			else if (type2 == 0x07) { // 0x07:Lv10UP
				if (pc.getTempMaxLevel() - pc.getTempLevel() < 10) {
					return;
				}
				setLevelUp(pc, 10);
			}
			else if (type2 == 0x01) {
				pc.addBaseStr((byte) 1);
				setLevelUp(pc, 1);
			}
			else if (type2 == 0x02) {
				pc.addBaseInt((byte) 1);
				setLevelUp(pc, 1);
			}
			else if (type2 == 0x03) {
				pc.addBaseWis((byte) 1);
				setLevelUp(pc, 1);
			}
			else if (type2 == 0x04) {
				pc.addBaseDex((byte) 1);
				setLevelUp(pc, 1);
			}
			else if (type2 == 0x05) {
				pc.addBaseCon((byte) 1);
				setLevelUp(pc, 1);
			}
			else if (type2 == 0x06) {
				pc.addBaseCha((byte) 1);
				setLevelUp(pc, 1);
			}
			else if (type2 == 0x08) {
				switch (readC()) {
					case 1:
						pc.addBaseStr((byte) 1);
						break;
					case 2:
						pc.addBaseInt((byte) 1);
						break;
					case 3:
						pc.addBaseWis((byte) 1);
						break;
					case 4:
						pc.addBaseDex((byte) 1);
						break;
					case 5:
						pc.addBaseCon((byte) 1);
						break;
					case 6:
						pc.addBaseCha((byte) 1);
						break;
				}
				if (pc.getElixirStats() > 0) {
					pc.sendPackets(new S_CharReset(pc.getElixirStats()));
					return;
				}
				saveNewCharStatus(pc);
			}
		}
		else if (stage == 0x03) {
			pc.addBaseStr((byte) (readC() - pc.getBaseStr()));
			pc.addBaseInt((byte) (readC() - pc.getBaseInt()));
			pc.addBaseWis((byte) (readC() - pc.getBaseWis()));
			pc.addBaseDex((byte) (readC() - pc.getBaseDex()));
			pc.addBaseCon((byte) (readC() - pc.getBaseCon()));
			pc.addBaseCha((byte) (readC() - pc.getBaseCha()));
			saveNewCharStatus(pc);
		}
	}

	private void saveNewCharStatus(L1PcInstance pc) {
		pc.setInCharReset(false);
		if (pc.getOriginalAc() > 0) {
			pc.addAc(pc.getOriginalAc());
		}
		if (pc.getOriginalMr() > 0) {
			pc.addMr(0 - pc.getOriginalMr());
		}
		pc.refresh();
		pc.setCurrentHp(pc.getMaxHp());
		pc.setCurrentMp(pc.getMaxMp());
		if (pc.getTempMaxLevel() != pc.getLevel()) {
			pc.setLevel(pc.getTempMaxLevel());
			pc.setExp(ExpTable.getExpByLevel(pc.getTempMaxLevel()));
		}
		if (pc.getLevel() > 50) {
			pc.setBonusStats(pc.getLevel() - 50);
		}
		else {
			pc.setBonusStats(0);
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
		L1ItemInstance item = pc.getInventory().findItemId(49142); // 希望のロウソク
		if (item != null) {
			try {
				pc.getInventory().removeItem(item, 1);
				pc.save(); // 儲存玩家的資料到資料庫中
			}
			catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		L1Teleport.teleport(pc, 32628, 32772, (short) 4, 4, false);
	}

	private void initCharStatus(L1PcInstance pc, int hp, int mp, int str, int intel, int wis, int dex, int con, int cha) {
		pc.addBaseMaxHp((short) (hp - pc.getBaseMaxHp()));
		pc.addBaseMaxMp((short) (mp - pc.getBaseMaxMp()));
		pc.addBaseStr((byte) (str - pc.getBaseStr()));
		pc.addBaseInt((byte) (intel - pc.getBaseInt()));
		pc.addBaseWis((byte) (wis - pc.getBaseWis()));
		pc.addBaseDex((byte) (dex - pc.getBaseDex()));
		pc.addBaseCon((byte) (con - pc.getBaseCon()));
		pc.addBaseCha((byte) (cha - pc.getBaseCha()));
	}

	private void setLevelUp(L1PcInstance pc, int addLv) {
		pc.setTempLevel(pc.getTempLevel() + addLv);
		for (int i = 0; i < addLv; i++) {
			short randomHp = CalcStat.calcStatHp(pc.getType(), pc.getBaseMaxHp(), pc.getBaseCon(), pc.getOriginalHpup());
			short randomMp = CalcStat.calcStatMp(pc.getType(), pc.getBaseMaxMp(), pc.getBaseWis(), pc.getOriginalMpup());
			pc.addBaseMaxHp(randomHp);
			pc.addBaseMaxMp(randomMp);
		}
		int newAc = CalcStat.calcAc(pc.getTempLevel(), pc.getBaseDex());
		pc.sendPackets(new S_CharReset(pc, pc.getTempLevel(), pc.getBaseMaxHp(), pc.getBaseMaxMp(), newAc, pc.getBaseStr(), pc.getBaseInt(), pc
				.getBaseWis(), pc.getBaseDex(), pc.getBaseCon(), pc.getBaseCha()));
	}

	@Override
	public String getType() {
		return C_CHAR_RESET;
	}

}
