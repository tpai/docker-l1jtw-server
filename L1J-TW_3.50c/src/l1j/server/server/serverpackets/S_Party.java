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

public class S_Party extends ServerBasePacket {

	private static final String _S_Party = "[S] S_Party";
	private byte[] _byte = null;

	public S_Party(String htmlid, int objid) {
		buildPacket(htmlid, objid, "", "", 0);
	}

	public S_Party(String htmlid, int objid, String partyname,
			String partymembers) {

		buildPacket(htmlid, objid, partyname, partymembers, 1);
	}

	private void buildPacket(String htmlid, int objid, String partyname,
			String partymembers, int type) {
		writeC(Opcodes.S_OPCODE_SHOWHTML);
		writeD(objid);
		writeS(htmlid);
		writeH(type);
		writeH(0x02);
		writeS(partyname);
		writeS(partymembers);
	}

	public S_Party(int type, L1PcInstance pc) {// 3.3C 組隊系統
		switch (type) {
		case 104:
			newMember(pc);
			break;
		case 105:
			oldMember(pc);
			break;
		case 106:
			changeLeader(pc);
		case 110:
			refreshParty(pc);
			break;
		default:
			break;
		}
	}

	/**
	 * 新加入隊伍的玩家
	 * 
	 * @param pc
	 */
	public void newMember(L1PcInstance pc) {
		L1PcInstance leader = pc.getParty().getLeader();
		L1PcInstance member[] = pc.getParty().getMembers();
		double nowhp = 0.0d;
		double maxhp = 0.0d;
		if (pc.getParty() == null) {
			return;
		} else {
			writeC(Opcodes.S_OPCODE_PACKETBOX);
			writeC(S_PacketBox.UPDATE_OLD_PART_MEMBER);
			nowhp = leader.getCurrentHp();
			maxhp = leader.getMaxHp();
			writeC(member.length - 1);
			writeD(leader.getId());
			writeS(leader.getName());
			writeC((int) (nowhp / maxhp) * 100);
			writeD(leader.getMapId());
			writeH(leader.getX());
			writeH(leader.getY());
			for (int i = 0, a = member.length; i < a; i++) {
				if (member[i].getId() == leader.getId() || member[i] == null)
					continue;
				nowhp = member[i].getCurrentHp();
				maxhp = member[i].getMaxHp();
				writeD(member[i].getId());
				writeS(member[i].getName());
				writeC((int) (nowhp / maxhp) * 100);
				writeD(member[i].getMapId());
				writeH(member[i].getX());
				writeH(member[i].getY());
			}
			writeC(0x00);
		}
	}

	/**
	 * 舊的隊員
	 * 
	 * @param pc
	 */
	public void oldMember(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(S_PacketBox.PATRY_UPDATE_MEMBER);
		writeD(pc.getId());
		writeS(pc.getName());
		writeD(pc.getMapId());
		writeH(pc.getX());
		writeH(pc.getY());
	}

	/**
	 * 更換隊長
	 * 
	 * @param pc
	 */
	public void changeLeader(L1PcInstance pc) {
		writeC(Opcodes.S_OPCODE_PACKETBOX);
		writeC(S_PacketBox.PATRY_SET_MASTER);
		writeD(pc.getId());
		writeH(0x0000);
	}

	/**
	 * 更新隊伍
	 * 
	 * @param pc
	 */
	public void refreshParty(L1PcInstance pc) {
		L1PcInstance member[] = pc.getParty().getMembers();
		if (pc.getParty() == null) {
			return;
		} else {
			writeC(Opcodes.S_OPCODE_PACKETBOX);
			writeC(S_PacketBox.PATRY_MEMBERS);
			writeC(member.length);
			for (int i = 0, a = member.length; i < a; i++) {
				writeD(member[i].getId());
				writeD(member[i].getMapId());
				writeH(member[i].getX());
				writeH(member[i].getY());
			}
			writeC(0x00);
		}
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
		return _S_Party;
	}

}
