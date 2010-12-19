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
package l1j.server.server.command.executor;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Lawful;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Status implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Status.class.getName());

	private L1Status() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Status();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String char_name = st.nextToken();
			String param = st.nextToken();
			int value = Integer.parseInt(st.nextToken());

			L1PcInstance target = null;
			if (char_name.equalsIgnoreCase("me")) {
				target = pc;
			} else {
				target = L1World.getInstance().getPlayer(char_name);
			}

			if (target == null) {
				pc.sendPackets(new S_ServerMessage(73, char_name)); // \f1%0はゲームをしていません。
				return;
			}

			// -- not use DB --
			if (param.equalsIgnoreCase("AC")) {
				target.addAc((byte) (value - target.getAc()));
			} else if (param.equalsIgnoreCase("MR")) {
				target.addMr((short) (value - target.getMr()));
			} else if (param.equalsIgnoreCase("HIT")) {
				target.addHitup((short) (value - target.getHitup()));
			} else if (param.equalsIgnoreCase("DMG")) {
				target.addDmgup((short) (value - target.getDmgup()));
				// -- use DB --
			} else {
				if (param.equalsIgnoreCase("HP")) {
					target
							.addBaseMaxHp((short) (value - target
									.getBaseMaxHp()));
					target.setCurrentHpDirect(target.getMaxHp());
				} else if (param.equalsIgnoreCase("MP")) {
					target
							.addBaseMaxMp((short) (value - target
									.getBaseMaxMp()));
					target.setCurrentMpDirect(target.getMaxMp());
				} else if (param.equalsIgnoreCase("LAWFUL")) {
					target.setLawful(value);
					S_Lawful s_lawful = new S_Lawful(target.getId(), target
							.getLawful());
					target.sendPackets(s_lawful);
					target.broadcastPacket(s_lawful);
				} else if (param.equalsIgnoreCase("KARMA")) {
					target.setKarma(value);
				} else if (param.equalsIgnoreCase("GM")) {
					if (value > 200) {
						value = 200;
					}
					target.setAccessLevel((short) value);
					target.sendPackets(new S_SystemMessage(
							"リスタートすれば、GMに昇格されています。"));
				} else if (param.equalsIgnoreCase("STR")) {
					target.addBaseStr((byte) (value - target.getBaseStr()));
				} else if (param.equalsIgnoreCase("CON")) {
					target.addBaseCon((byte) (value - target.getBaseCon()));
				} else if (param.equalsIgnoreCase("DEX")) {
					target.addBaseDex((byte) (value - target.getBaseDex()));
				} else if (param.equalsIgnoreCase("INT")) {
					target.addBaseInt((byte) (value - target.getBaseInt()));
				} else if (param.equalsIgnoreCase("WIS")) {
					target.addBaseWis((byte) (value - target.getBaseWis()));
				} else if (param.equalsIgnoreCase("CHA")) {
					target.addBaseCha((byte) (value - target.getBaseCha()));
				} else {
					pc.sendPackets(new S_SystemMessage("ステータス " + param
							+ " は不明です。"));
					return;
				}
				target.save(); // DBにキャラクター情報を書き込む
			}
			target.sendPackets(new S_OwnCharStatus(target));
			pc.sendPackets(new S_SystemMessage(target.getName() + " の" + param
					+ "を" + value + "に変更しました。"));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName
					+ " キャラクター名|me ステータス 変更値 と入力して下さい。"));
		}
	}
}
