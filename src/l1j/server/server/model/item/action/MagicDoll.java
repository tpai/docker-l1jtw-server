package l1j.server.server.model.item.action;

import l1j.server.Config;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Npc;

public class MagicDoll {

	public static void useMagicDoll(L1PcInstance pc, int itemId, int itemObjectId) {
		boolean isAppear = true;
		L1DollInstance doll = null;
		Object[] dollList = pc.getDollList().values().toArray();
		for (Object dollObject : dollList) {
			doll = (L1DollInstance) dollObject;
			if (doll.getItemObjId() == itemObjectId) {
				isAppear = false;
				break;
			}
		}

		if (isAppear) {
			if (!pc.getInventory().checkItem(41246, 50)) {
				pc.sendPackets(new S_ServerMessage(337, "$5240")); // 魔法結晶體不足
				return;
			}
			if (dollList.length >= Config.MAX_DOLL_COUNT) {
				pc.sendPackets(new S_ServerMessage(79)); // 沒有任何事情發生
				return;
			}
			int npcId = 0;
			int dollType = 0;
			switch (itemId) {
				case 41248: // 魔法娃娃：肥肥
					npcId = 80106;
					dollType = L1DollInstance.DOLLTYPE_BUGBEAR;
					break; 
				case 41249: // 魔法娃娃：小思克巴
					npcId = 80107;
					dollType = L1DollInstance.DOLLTYPE_SUCCUBUS;
					break; 
				case 41250: // 魔法娃娃：野狼寶寶
					npcId = 80108;
					dollType = L1DollInstance.DOLLTYPE_WAREWOLF;
					break; 
				case 49037: // 魔法娃娃：長老
					npcId = 80129;
					dollType = L1DollInstance.DOLLTYPE_ELDER;
					break; 
				case 49038: // 魔法娃娃：奎斯坦修
					npcId = 80130;
					dollType = L1DollInstance.DOLLTYPE_CRUSTANCEAN;
					break; 
				case 49039: // 魔法娃娃：石頭高崙
					npcId = 80131;
					dollType = L1DollInstance.DOLLTYPE_GOLEM;
				break;
				case 47105: // 魔法娃娃：希爾黛斯
					npcId = 92109;
					dollType = L1DollInstance.DOLLTYPE_SEADANCER;
					break;
				case 47106: // 魔法娃娃：蛇女
					npcId = 92103;
					dollType = L1DollInstance.DOLLTYPE_SERPENTWOMAN;
					break;
				case 47107: // 魔法娃娃：雪怪
					npcId = 92102;
					dollType = L1DollInstance.DOLLTYPE_SNOWMAN;
					break;
				case 47108: // 魔法娃娃：亞力安
					npcId = 92104;
					dollType = L1DollInstance.DOLLTYPE_COCKATRICE;
					break;
				case 47109: // 魔法娃娃：史巴托
					npcId = 92106;
					dollType = L1DollInstance.DOLLTYPE_SPARTOI;
					break;
				case 47110: // 神秘稜鏡：淘氣幼龍
					npcId = 92110;
					dollType = L1DollInstance.DOLLTYPE_AZURU_HACHIRIN;
					break;
				case 47111: // 神秘稜鏡：頑皮幼龍
					npcId = 92111;
					dollType = L1DollInstance.DOLLTYPE_CRIMSON_HACHIRIN;
					break;
				case 47112: // 神秘稜鏡：高等淘氣幼龍
					npcId = 92112;
					dollType = L1DollInstance.DOLLTYPE_MALE_HI_HACHIRIN ;
					break;
				case 47113: // 神秘稜鏡：高等頑皮幼龍
					npcId = 92113;
					dollType = L1DollInstance.DOLLTYPE_FEMALE_HI_HACHIRIN;
					break;
			}

			L1Npc template = NpcTable.getInstance().getTemplate(npcId);
			doll = new L1DollInstance(template, pc, dollType, itemObjectId);
			pc.sendPackets(new S_SkillSound(doll.getId(), 5935));
			pc.broadcastPacket(new S_SkillSound(doll.getId(), 5935));
			pc.sendPackets(new S_SkillIconGFX(56, 1800));
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.getInventory().consumeItem(41246, 50);
		}
		else {
			pc.sendPackets(new S_SkillSound(doll.getId(), 5936));
			pc.broadcastPacket(new S_SkillSound(doll.getId(), 5936));
			doll.deleteDoll();
			pc.sendPackets(new S_SkillIconGFX(56, 0));
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
	}

}
