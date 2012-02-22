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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ClientThread;
import l1j.server.server.WarTimeController;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.identity.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.S_ChangeName;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Resurrection;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Trade;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Attr extends ClientBasePacket {

	private static final Logger _log = Logger.getLogger(C_Attr.class.getName());

	private static final String C_ATTR = "[C] C_Attr";

	private static final int HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

	private static final int HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	@SuppressWarnings("static-access")
	public C_Attr(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);
		int i = readH(); // 3.51C未知的功能
		int attrcode;

		if (i == 479) {
		   attrcode = i;
		} else {
		   @SuppressWarnings("unused")
		   int count = readD(); // 紀錄世界中發送YesNo的次數
		   attrcode = readH();
		}

		String name ;
		int c;
		
		L1PcInstance pc = clientthread.getActiveChar();

		switch (attrcode) {
		case 97: // \f3%0%s 想加入你的血盟。你接受嗎。(Y/N)
			c = readH();
			L1PcInstance joinPc = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			if (joinPc != null) {
				if (c == 0) { // No
					joinPc.sendPackets(new S_ServerMessage(96, pc.getName())); // \f1%0%s
																				// 拒絕你的請求。
				} else if (c == 1) { // Yes
					int clan_id = pc.getClanid();
					String clanName = pc.getClanname();
					L1Clan clan = L1World.getInstance().getClan(clanName);
					if (clan != null) {
						int maxMember = 0;
						int charisma = pc.getCha();
						// 公式
						maxMember = charisma * 3*( 2+ pc.getLevel() / 50 );
						// 未過45 人數/3
						if (!pc.getQuest().isEnd(L1Quest.QUEST_LEVEL45)) 
							maxMember /= 3;						
						
						if (Config.MAX_CLAN_MEMBER > 0) { // 設定檔中如果有設定血盟的人數上限
							maxMember = Config.MAX_CLAN_MEMBER;
						}

						if (joinPc.getClanid() == 0) { // 加入玩家未加入血盟
							String clanMembersName[] = clan.getAllMembers();
							if (maxMember <= clanMembersName.length) { // 血盟還有空間可以讓玩家加入
								joinPc.sendPackets( // %0%s 無法接受你成為該血盟成員。
								new S_ServerMessage(188, pc.getName()));
								return;
							}
							for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
								clanMembers.sendPackets(new S_ServerMessage(94,
										joinPc.getName())); // \f1你接受%0當你的血盟成員。
							}
							joinPc.setClanid(clan_id);
							joinPc.setClanname(clanName);
							joinPc.setClanRank(L1Clan.CLAN_RANK_PUBLIC);
							joinPc.setTitle("");
							joinPc.sendPackets(new S_CharTitle(joinPc.getId(),
									""));
							joinPc.broadcastPacket(new S_CharTitle(joinPc
									.getId(), ""));
							joinPc.save(); // 儲存加入的玩家資料
							clan.addMemberName(joinPc.getName());
							joinPc.sendPackets(new S_ServerMessage(95, clanName)); // \f1加入%0血盟。
						} else { // 如果是王族加入（聯合血盟）
							if (Config.CLAN_ALLIANCE) {
								changeClan(clientthread, pc, joinPc, maxMember);
							} else {
								joinPc.sendPackets(new S_ServerMessage(89)); // \f1你已經有血盟了。
							}
						}
					}
				}
			}
			break;

		case 217: // %0 血盟向你的血盟宣戰。是否接受？(Y/N)
		case 221: // %0 血盟要向你投降。是否接受？(Y/N)
		case 222: // %0 血盟要結束戰爭。是否接受？(Y/N)
			c = readH();
			L1PcInstance enemyLeader = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			if (enemyLeader == null) {
				return;
			}
			pc.setTempID(0);
			String clanName = pc.getClanname();
			String enemyClanName = enemyLeader.getClanname();
			if (c == 0) { // No
				if (i == 217) {
					enemyLeader.sendPackets(new S_ServerMessage(236, clanName)); // %0
																					// 血盟拒絕你的宣戰。
				} else if ((i == 221) || (i == 222)) {
					enemyLeader.sendPackets(new S_ServerMessage(237, clanName)); // %0
																					// 血盟拒絕你的提案。
				}
			} else if (c == 1) { // Yes
				if (i == 217) {
					L1War war = new L1War();
					war.handleCommands(2, enemyClanName, clanName); // 盟戰開始
				} else if ((i == 221) || (i == 222)) {
					// 取得線上所有的盟戰
					for (L1War war : L1World.getInstance().getWarList()) {
						if (war.CheckClanInWar(clanName)) { // 如果有現在的血盟
							if (i == 221) {
								war.SurrenderWar(enemyClanName, clanName); // 投降
							} else if (i == 222) {
								war.CeaseWar(enemyClanName, clanName); // 結束
							}
							break;
						}
					}
				}
			}
			break;

		case 252: // \f2%0%s 要與你交易。願不願交易？ (Y/N)
			c = readH();
			L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(pc.getTradeID());
			if (trading_partner != null) {
				if (c == 0) // No
				{
					trading_partner.sendPackets(new S_ServerMessage(253, pc
							.getName())); // %0%d
											// 拒絕與你交易。
					pc.setTradeID(0);
					trading_partner.setTradeID(0);
				} else if (c == 1) // Yes
				{
					pc.sendPackets(new S_Trade(trading_partner.getName()));
					trading_partner.sendPackets(new S_Trade(pc.getName()));
				}
			}
			break;

		case 321: // 是否要復活？ (Y/N)
			c = readH();
			L1PcInstance resusepc1 = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			if (resusepc1 != null) { // 如果有這個人
				if (c == 0) { // No

				} else if (c == 1) { // Yes
					resurrection( pc, resusepc1, (short) (pc.getMaxHp() / 2));
				}
			}
			break;

		case 322: // 是否要復活？ (Y/N)
			c = readH();
			L1PcInstance resusepc2 = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			if (resusepc2 != null) { // 祝福された 復活スクロール、リザレクション、グレーター リザレクション
				if (c == 0) { // No

				} else if (c == 1) { // Yes
					resurrection( pc, resusepc2, pc.getMaxHp());
					// EXPロストしている、G-RESを掛けられた、EXPロストした死亡
					// 全てを満たす場合のみEXP復旧
					if ((pc.getExpRes() == 1) && pc.isGres()
							&& pc.isGresValid()) {
						pc.resExp();
						pc.setExpRes(0);
						pc.setGres(false);
					}
				}
			}
			break;

		case 325: // 你想叫牠什麼名字？
			c = readH(); // ?
			name = readS();
			L1PetInstance pet = (L1PetInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			renamePet(pet, name);
			break;

		case 512: // 請輸入血盟小屋名稱?
			c = readH(); // ?
			name = readS();
			int houseId = pc.getTempID();
			pc.setTempID(0);
			if (name.length() <= 16) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				house.setHouseName(name);
				HouseTable.getInstance().updateHouse(house); // 更新到資料庫中
			} else {
				pc.sendPackets(new S_ServerMessage(513)); // 血盟小屋名稱太長。
			}
			break;

		case 630: // %0%s 要與你決鬥。你是否同意？(Y/N)
			c = readH();
			L1PcInstance fightPc = (L1PcInstance) L1World.getInstance().findObject(pc.getFightId());
			if (c == 0) {
				pc.setFightId(0);
				fightPc.setFightId(0);
				fightPc.sendPackets(new S_ServerMessage(631, pc.getName())); // %0%dがあなたとの決闘を断りました。
			} else if (c == 1) {
				fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL,
						fightPc.getFightId(), fightPc.getId()));
				pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, pc
						.getFightId(), pc.getId()));
			}
			break;

		case 653: // 若你離婚，你的結婚戒指將會消失。你決定要離婚嗎？(Y/N)
			c = readH();
			L1PcInstance target653 = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
			if (c == 0) { // No
				return;
			} else if (c == 1) { // Yes
				if (target653 != null) {
					target653.setPartnerId(0);
					target653.save();
					target653.sendPackets(new S_ServerMessage(662)); // \f1你(妳)目前未婚。
				} else {
					CharacterTable.getInstance().updatePartnerId(
							pc.getPartnerId());
				}
			}
			pc.setPartnerId(0);
			pc.save(); // 將玩家資料儲存到資料庫中
			pc.sendPackets(new S_ServerMessage(662)); // \f1你(妳)目前未婚。
			break;

		case 654: // %0 向你(妳)求婚，你(妳)答應嗎?
			c = readH();
			L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			if (partner != null) {
				if (c == 0) { // No
					partner.sendPackets(new S_ServerMessage( // %0
																// 拒絕你(妳)的求婚。
							656, pc.getName()));
				} else if (c == 1) { // Yes
					pc.setPartnerId(partner.getId());
					pc.save();
					pc.sendPackets(new S_ServerMessage( // 倆人的結婚在所有人的祝福下完成
							790));
					pc.sendPackets(new S_ServerMessage( // 恭喜!! %0
														// 已接受你(妳)的求婚。
							655, partner.getName()));

					partner.setPartnerId(pc.getId());
					partner.save();
					partner.sendPackets(new S_ServerMessage( // 恭喜!! %0
																// 已接受你(妳)的求婚。
							790));
					partner.sendPackets(new S_ServerMessage( // 恭喜!! %0
																// 已接受你(妳)的求婚。
							655, pc.getName()));
				}
			}
			break;

		// コールクラン
		case 729: // 盟主正在呼喚你，你要接受他的呼喚嗎？(Y/N)
			c = readH();
			if (c == 0) { // No

			} else if (c == 1) { // Yes
				callClan(pc);
			}
			break;

		case 738: // 恢復經驗值需消耗%0金幣。想要恢復經驗值嗎?
			c = readH();
			if ((c == 1) && (pc.getExpRes() == 1)) { // Yes
				int cost = 0;
				int level = pc.getLevel();
				int lawful = pc.getLawful();
				if (level < 45) {
					cost = level * level * 100;
				} else {
					cost = level * level * 200;
				}
				if (lawful >= 0) {
					cost = (cost / 2);
				}
				if (pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
					pc.resExp();
					pc.setExpRes(0);
				} else {
					pc.sendPackets(new S_ServerMessage(189)); // \f1金幣不足。
				}
			}
			break;

		case 951: // 您要接受玩家 %0%s 提出的隊伍對話邀請嗎？(Y/N)
			c = readH();
			L1PcInstance chatPc = (L1PcInstance) L1World.getInstance().findObject(pc.getPartyID());
			if (chatPc != null) {
				if (c == 0) { // No
					chatPc.sendPackets(new S_ServerMessage(423, pc.getName())); // %0%s
																				// 拒絕了您的邀請。
					pc.setPartyID(0);
				} else if (c == 1) { // Yes
					if (chatPc.isInChatParty()) {
						if (chatPc.getChatParty().isVacancy() || chatPc.isGm()) {
							chatPc.getChatParty().addMember(pc);
						} else {
							chatPc.sendPackets(new S_ServerMessage(417)); // 你的隊伍已經滿了，無法再接受隊員。
						}
					} else {
						L1ChatParty chatParty = new L1ChatParty();
						chatParty.addMember(chatPc);
						chatParty.addMember(pc);
						chatPc.sendPackets(new S_ServerMessage(424, pc
								.getName())); // %0%s
												// 加入了您的隊伍。
					}
				}
			}
			break;

		case 953: // 玩家 %0%s 邀請您加入隊伍？(Y/N)
			c = readH();
			L1PcInstance target = (L1PcInstance) L1World.getInstance().findObject(pc.getPartyID());
			if (target != null) {
				if (c == 0) // No
				{
					target.sendPackets(new S_ServerMessage(423, pc.getName())); // %0%s
																				// 拒絕了您的邀請。
					pc.setPartyID(0);
				} else if (c == 1) // Yes
				{
					if (target.isInParty()) {
						// 隊長組隊中
						if (target.getParty().isVacancy() || target.isGm()) {
							// 組隊是空的
							target.getParty().addMember(pc);
						} else {
							// 組隊滿了
							target.sendPackets(new S_ServerMessage(417)); // 你的隊伍已經滿了，無法再接受隊員。
						}
					} else {
						// 還沒有組隊，建立一個新組隊
						L1Party party = new L1Party();
						party.addMember(target);
						party.addMember(pc);
						target.sendPackets(new S_ServerMessage(424, pc
								.getName())); // %0%s
												// 加入了您的隊伍。
					}
				}
			}
			break;

			case 954: // 玩家 %0%s 邀請您加入自動分配隊伍？(Y/N)
				c = readH();
				L1PcInstance target2 = (L1PcInstance) L1World.getInstance().findObject(pc.getPartyID());
				if (target2 != null) {
					if (c == 0) { // No
						target2.sendPackets(new S_ServerMessage(423, pc.getName())); // %0%s
																				// 拒絕了您的邀請。
						pc.setPartyID(0);
					}
					else if (c == 1) { // Yes
						if (target2.isInParty()) {
							// 隊長組隊中
							if (target2.getParty().isVacancy() || target2.isGm()) {
								// 組隊是空的
								target2.getParty().addMember(pc);
							}
							else {
								// 組隊滿了
								target2.sendPackets(new S_ServerMessage(417)); // 你的隊伍已經滿了，無法再接受隊員。
							}
						}
						else {
							// 還沒有組隊，建立一個新組隊
							L1Party party = new L1Party();
							party.addMember(target2);
							party.addMember(pc);
							target2.sendPackets(new S_ServerMessage(424, pc.getName())); // %0%s
																						// 加入了您的隊伍。
						}
					}
				}
				break;

		case 479: // 提昇能力值？（str、dex、int、con、wis、cha）
			if (readC() == 1) {
				String s = readS();
				if (!(pc.getLevel() - 50 > pc.getBonusStats())) {
					return;
				}
				if (s.toLowerCase().equals("str".toLowerCase())) {
					// if(l1pcinstance.get_str() < 255)
					if (pc.getBaseStr() < 35) {
						pc.addBaseStr((byte) 1); // 素のSTR値に+1
						pc.setBonusStats(pc.getBonusStats() + 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.save(); // 將玩家資料儲存到資料庫中
					} else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1屬性最大值只能到35。
																	// 請重試一次。
					}
				} else if (s.toLowerCase().equals("dex".toLowerCase())) {
					// if(l1pcinstance.get_dex() < 255)
					if (pc.getBaseDex() < 35) {
						pc.addBaseDex((byte) 1); // 素のDEX値に+1
						pc.resetBaseAc();
						pc.setBonusStats(pc.getBonusStats() + 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.save(); // 將玩家資料儲存到資料庫中
					} else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1屬性最大值只能到35。
																	// 請重試一次。
					}
				} else if (s.toLowerCase().equals("con".toLowerCase())) {
					// if(l1pcinstance.get_con() < 255)
					if (pc.getBaseCon() < 35) {
						pc.addBaseCon((byte) 1); // 素のCON値に+1
						pc.setBonusStats(pc.getBonusStats() + 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.save(); // 將玩家資料儲存到資料庫中
					} else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1屬性最大值只能到35。
																	// 請重試一次。
					}
				} else if (s.toLowerCase().equals("int".toLowerCase())) {
					// if(l1pcinstance.get_int() < 255)
					if (pc.getBaseInt() < 35) {
						pc.addBaseInt((byte) 1); // 素のINT値に+1
						pc.setBonusStats(pc.getBonusStats() + 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.save(); // 將玩家資料儲存到資料庫中
					} else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1屬性最大值只能到35。
																	// 請重試一次。
					}
				} else if (s.toLowerCase().equals("wis".toLowerCase())) {
					// if(l1pcinstance.get_wis() < 255)
					if (pc.getBaseWis() < 35) {
						pc.addBaseWis((byte) 1); // 素のWIS値に+1
						pc.resetBaseMr();
						pc.setBonusStats(pc.getBonusStats() + 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.save(); // 將玩家資料儲存到資料庫中
					} else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1屬性最大值只能到35。
																	// 請重試一次。
					}
				} else if (s.toLowerCase().equals("cha".toLowerCase())) {
					// if(l1pcinstance.get_cha() < 255)
					if (pc.getBaseCha() < 35) {
						pc.addBaseCha((byte) 1); // 素のCHA値に+1
						pc.setBonusStats(pc.getBonusStats() + 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.save(); // 將玩家資料儲存到資料庫中
					} else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1屬性最大值只能到35。
																	// 請重試一次。
					}
				}
			}
			break;
		case 1256:// 寵物競速 預約名單回應
			l1j.server.server.model.game.L1PolyRace.getInstance().requsetAttr(pc, readC());
			break;
		default:
			break;
		}
	}

	private void resurrection(L1PcInstance pc, L1PcInstance resusepc,
			short resHp) {
		// 由其他角色復活
		pc.sendPackets(new S_SkillSound(pc.getId(), '\346'));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), '\346'));
		pc.resurrect(resHp);
		pc.setCurrentHp(resHp);
		pc.startHpRegeneration();
		pc.startMpRegeneration();
		pc.startHpRegenerationByDoll();
		pc.startMpRegenerationByDoll();
		pc.stopPcDeleteTimer();
		pc.sendPackets(new S_Resurrection(pc, resusepc, 0));
		pc.broadcastPacket(new S_Resurrection(pc, resusepc, 0));
		pc.sendPackets(new S_CharVisualUpdate(pc));
		pc.broadcastPacket(new S_CharVisualUpdate(pc));
	}

	private void changeClan(ClientThread clientthread, L1PcInstance pc,
			L1PcInstance joinPc, int maxMember) {
		int clanId = pc.getClanid();
		String clanName = pc.getClanname();
		L1Clan clan = L1World.getInstance().getClan(clanName);

		int oldClanId = joinPc.getClanid();
		String oldClanName = joinPc.getClanname();
		L1Clan oldClan = L1World.getInstance().getClan(oldClanName);
		
		if ((clan != null) && (oldClan != null) && joinPc.isCrown() && // 自己的王族
				(joinPc.getId() == oldClan.getLeaderId())) {
			if (maxMember < clan.getAllMembers().length + oldClan.getAllMembers().length) { // 沒有空缺
				joinPc.sendPackets( // %0%s 無法接受你成為該血盟成員。
				new S_ServerMessage(188, pc.getName()));
				return;
			}
			for (L1PcInstance element : clan.getOnlineClanMember()) {
				element.sendPackets(new S_ServerMessage(94, joinPc.getName())); // \f1你接受%0當你的血盟成員。
			}

			for (String element : oldClan.getAllMembers()) {
				L1PcInstance oldClanMember = L1World.getInstance().getPlayer(element);
				if (oldClanMember != null) { // 舊血盟成員在線上
					oldClanMember.setClanid(clanId);
					oldClanMember.setClanname(clanName);
					// TODO: 翻譯
					// 血盟連合に加入した君主はガーディアン
					// 君主が連れてきた血盟員は見習い
					if (oldClanMember.getId() == joinPc.getId()) {
						oldClanMember.setClanRank(L1Clan.CLAN_RANK_GUARDIAN);
					} else {
						oldClanMember.setClanRank(L1Clan.CLAN_RANK_PROBATION);
					}
					try {
						// 儲存玩家資料到資料庫中
						oldClanMember.save();
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
					clan.addMemberName(oldClanMember.getName());
					oldClanMember
							.sendPackets(new S_ServerMessage(95, clanName)); // \f1加入%0血盟。
				} else { // 舊血盟成員不在線上
					try {
						L1PcInstance offClanMember = CharacterTable
								.getInstance().restoreCharacter(element);
						offClanMember.setClanid(clanId);
						offClanMember.setClanname(clanName);
						offClanMember.setClanRank(L1Clan.CLAN_RANK_PROBATION);
						offClanMember.save(); // 儲存玩家資料到資料庫中
						clan.addMemberName(offClanMember.getName());
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
			// 刪除舊盟徽
			String emblem_file = String.valueOf(oldClanId);
			File file = new File("emblem/" + emblem_file);
			file.delete();
			ClanTable.getInstance().deleteClan(oldClanName);
		}
	}

	private static void renamePet(L1PetInstance pet, String name) {
		if ((pet == null) || (name == null)) {
			throw new NullPointerException();
		}

		int petItemObjId = pet.getItemObjId();
		L1Pet petTemplate = PetTable.getInstance().getTemplate(petItemObjId);
		if (petTemplate == null) {
			throw new NullPointerException();
		}

		L1PcInstance pc = (L1PcInstance) pet.getMaster();
		if (PetTable.isNameExists(name)) {
			pc.sendPackets(new S_ServerMessage(327)); // 同樣的名稱已經存在。
			return;
		}
		L1Npc l1npc = NpcTable.getInstance().getTemplate(pet.getNpcId());
		if (!(pet.getName().equalsIgnoreCase(l1npc.get_name()))) {
			pc.sendPackets(new S_ServerMessage(326)); // 一旦你已決定就不能再變更。
			return;
		}
		pet.setName(name);
		petTemplate.set_name(name);
		PetTable.getInstance().storePet(petTemplate); // 儲存寵物資料到資料庫中
		L1ItemInstance item = pc.getInventory().getItem(pet.getItemObjId());
		pc.getInventory().updateItem(item);
		pc.sendPackets(new S_ChangeName(pet.getId(), name));
		pc.broadcastPacket(new S_ChangeName(pet.getId(), name));
	}

	private void callClan(L1PcInstance pc) {
		L1PcInstance callClanPc = (L1PcInstance) L1World.getInstance()
				.findObject(pc.getTempID());
		pc.setTempID(0);
		if (callClanPc == null) {
			return;
		}
		if (!pc.getMap().isEscapable() && !pc.isGm()) {
			// 這附近的能量影響到瞬間移動。在此地無法使用瞬間移動。
			pc.sendPackets(new S_ServerMessage(647));
			L1Teleport.teleport(pc, pc.getLocation(), pc.getHeading(), false);
			return;
		}
		if (pc.getId() != callClanPc.getCallClanId()) {
			return;
		}

		boolean isInWarArea = false;
		int castleId = L1CastleLocation.getCastleIdByArea(callClanPc);
		if (castleId != 0) {
			isInWarArea = true;
			if (WarTimeController.getInstance().isNowWar(castleId)) {
				isInWarArea = false; // 戰爭也可以在時間的旗
			}
		}
		short mapId = callClanPc.getMapId();
		if (((mapId != 0) && (mapId != 4) && (mapId != 304)) || isInWarArea) {
			// 沒有任何事情發生。
			pc.sendPackets(new S_ServerMessage(79));
			return;
		}

		L1Map map = callClanPc.getMap();
		int locX = callClanPc.getX();
		int locY = callClanPc.getY();
		int heading = callClanPc.getCallClanHeading();
		locX += HEADING_TABLE_X[heading];
		locY += HEADING_TABLE_Y[heading];
		heading = (heading + 4) % 4;

		boolean isExsistCharacter = false;
		for (L1Object object : L1World.getInstance().getVisibleObjects(
				callClanPc, 1)) {
			if (object instanceof L1Character) {
				L1Character cha = (L1Character) object;
				if ((cha.getX() == locX) && (cha.getY() == locY)
						&& (cha.getMapId() == mapId)) {
					isExsistCharacter = true;
					break;
				}
			}
		}

		if (((locX == 0) && (locY == 0)) || !map.isPassable(locX, locY)
				|| isExsistCharacter) {
			// 因你要去的地方有障礙物以致於無法直接傳送到該處。
			pc.sendPackets(new S_ServerMessage(627));
			return;
		}
		L1Teleport.teleport(pc, locX, locY, mapId, heading, true,
				L1Teleport.CALL_CLAN);
	}

	@Override
	public String getType() {
		return C_ATTR;
	}
}