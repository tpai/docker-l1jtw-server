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

import javolution.util.FastTable;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 屬於PacketBox的封包 只是抓出來另外寫
 * GameStart	進入賽跑的畫面
 * GameEnd		離開賽跑的畫面
 */
public class S_Race extends ServerBasePacket {
	private static final String S_RACE = "[S] S_Race";

	private byte[] _byte = null;

	public static final int GameStart = 0x40;
	public static final int CountDown = 0x41;
	public static final int PlayerInfo = 0x42;
	public static final int Lap = 0x43;
	public static final int Winner = 0x44;
	public static final int GameOver = 0x45;
	public static final int GameEnd = 0x46;

	//GameStart// CountDown// GameOver// GameEnd
	public S_Race(int type) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(type);
		if (type == GameStart) {
			writeC(0x05); //倒數5秒
		}
	}

	public S_Race(FastTable<L1PcInstance> playerList, L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(PlayerInfo);
		writeH(playerList.size()); //參賽者人數
		writeH(playerList.indexOf(pc)); //名次
		for (L1PcInstance player : playerList) {
			if (player == null) {
				continue;
			}
			writeS(player.getName());
		}
	}

	public S_Race(int maxLap, int lap) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(Lap);
		writeH(maxLap); //最大圈數
		writeH(lap); //目前圈數
	}

	public S_Race(String winnerName, int time) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(Winner);
		writeS(winnerName);
		writeD(time * 1000);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_RACE;
	}
}
