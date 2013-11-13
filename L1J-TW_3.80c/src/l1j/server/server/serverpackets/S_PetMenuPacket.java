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
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

public class S_PetMenuPacket extends ServerBasePacket {

	private byte[] _byte = null;

	public S_PetMenuPacket(L1NpcInstance npc, int exppercet) {
		buildpacket(npc, exppercet);
	}

	private void buildpacket(L1NpcInstance npc, int exppercet) {
		writeC(Opcodes.S_OPCODE_SHOWHTML);

		if (npc instanceof L1PetInstance) { // ペット
			L1PetInstance pet = (L1PetInstance) npc;
			writeD(pet.getId());
			writeS("anicom");
			writeC(0x00);
			writeH(0x000b);
			switch (pet.getCurrentPetStatus()) {
			case 1:
				writeS("$469"); // 攻撃態勢
				break;
			case 2:
				writeS("$470"); // 防御態勢
				break;
			case 3:
				writeS("$471"); // 休憩
				break;
			case 5:
				writeS("$472"); // 警戒
				break;
			default:
				writeS("$471"); // 休憩
				break;
			}
			writeS(Integer.toString(pet.getCurrentHp())); // 現在のＨＰ
			writeS(Integer.toString(pet.getMaxHp())); // 最大ＨＰ
			writeS(Integer.toString(pet.getCurrentMp())); // 現在のＭＰ
			writeS(Integer.toString(pet.getMaxMp())); // 最大ＭＰ
			writeS(Integer.toString(pet.getLevel())); // レベル

			// 名前の文字数が8を超えると落ちる
			// なぜか"セント バーナード","ブレイブ ラビット"はOK
			// String pet_name = pet.get_name();
			// if (pet_name.equalsIgnoreCase("ハイ ドーベルマン")) {
			// pet_name = "ハイ ドーベルマ";
			// }
			// else if (pet_name.equalsIgnoreCase("ハイ セントバーナード")) {
			// pet_name = "ハイ セントバー";
			// }
			// writeS(pet_name);
			writeS(""); // ペットの名前を表示させると不安定になるので、非表示にする

			String s = "$610";
			if (pet.get_food() > 80) {
				s = "$612"; // 非常飽。
			} else if (pet.get_food() > 60) {
				s = "$611"; // 稍微飽。
			} else if (pet.get_food() > 30) {
				s = "$610"; // 普通。
			} else if (pet.get_food() > 10) {
				s = "$609"; // 稍微餓。
			} else if (pet.get_food() >= 0) {
				s = "$608"; // 非常餓。
			}
			writeS(s); // 飽食度
			writeS(Integer.toString(exppercet)); // 経験値
			writeS(Integer.toString(pet.getLawful())); // アライメント
		} else if (npc instanceof L1SummonInstance) { // サモンモンスター
			L1SummonInstance summon = (L1SummonInstance) npc;
			writeD(summon.getId());
			writeS("moncom");
			writeC(0x00);
			writeH(6); // 渡す引数文字の数の模様
			switch (summon.get_currentPetStatus()) {
			case 1:
				writeS("$469"); // 攻撃態勢
				break;
			case 2:
				writeS("$470"); // 防御態勢
				break;
			case 3:
				writeS("$471"); // 休憩
				break;
			case 5:
				writeS("$472"); // 警戒
				break;
			default:
				writeS("$471"); // 休憩
				break;
			}
			writeS(Integer.toString(summon.getCurrentHp())); // 現在のＨＰ
			writeS(Integer.toString(summon.getMaxHp())); // 最大ＨＰ
			writeS(Integer.toString(summon.getCurrentMp())); // 現在のＭＰ
			writeS(Integer.toString(summon.getMaxMp())); // 最大ＭＰ
			writeS(Integer.toString(summon.getLevel())); // レベル
			// writeS(summon.getNpcTemplate().get_nameid());
			// writeS(Integer.toString(0));
			// writeS(Integer.toString(790));
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}
}
