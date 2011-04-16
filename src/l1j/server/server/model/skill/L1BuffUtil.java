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
package l1j.server.server.model.skill;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Awake;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1CurseParalysis;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_ChangeName;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShowPolyList;
import l1j.server.server.serverpackets.S_ShowSummonList;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillIconShield;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.Random;

import static l1j.server.server.model.skill.L1SkillId.*;

public class L1BuffUtil {
	public static void haste(L1PcInstance pc, int timeMillis) {

		int objId = pc.getId();

		/* 已存在加速狀態消除 */
		if (pc.hasSkillEffect(HASTE)
				|| pc.hasSkillEffect(GREATER_HASTE)
				|| pc.hasSkillEffect(STATUS_HASTE)) {
			if (pc.hasSkillEffect(HASTE)) {					// 加速術
				pc.killSkillEffectTimer(HASTE);
			} else if (pc.hasSkillEffect(GREATER_HASTE)) {	// 強力加速術
				pc.killSkillEffectTimer(GREATER_HASTE);
			} else if (pc.hasSkillEffect(STATUS_HASTE)) {	// 自我加速藥水
				pc.killSkillEffectTimer(STATUS_HASTE);
			}
		}
		/* 抵消緩速魔法效果 緩速術 集體緩速術 地面障礙 */
		if (pc.hasSkillEffect(SLOW) || pc.hasSkillEffect(MASS_SLOW) || pc.hasSkillEffect(ENTANGLE)) {
			if (pc.hasSkillEffect(SLOW)) {				// 緩速術
				pc.killSkillEffectTimer(SLOW);
			} else if (pc.hasSkillEffect(MASS_SLOW)) {	// 集體緩速術
				pc.killSkillEffectTimer(MASS_SLOW);
			} else if (pc.hasSkillEffect(ENTANGLE)) {	// 地面障礙
				pc.killSkillEffectTimer(ENTANGLE);
			}
			pc.sendPackets(new S_SkillHaste(objId, 0, 0));
			pc.broadcastPacket(new S_SkillHaste(objId, 0, 0));
		}

		pc.setSkillEffect(STATUS_HASTE, timeMillis);

		pc.sendPackets(new S_SkillSound(objId, 191));
		pc.broadcastPacket(new S_SkillSound(objId, 191));
		pc.sendPackets(new S_SkillHaste(objId, 1, timeMillis / 1000));
		pc.broadcastPacket(new S_SkillHaste(objId, 1, 0));
		pc.sendPackets(new S_ServerMessage(184)); // \f1你的動作突然變快。 */
		pc.setMoveSpeed(1);
	}

	public static void brave(L1PcInstance pc, int timeMillis) {
		// 消除重複狀態
		if (pc.hasSkillEffect(STATUS_BRAVE)) {			// 勇敢藥水	1.33倍
			pc.killSkillEffectTimer(STATUS_BRAVE);
		}
		if (pc.hasSkillEffect(STATUS_ELFBRAVE)) {		// 精靈餅乾	1.15倍
			pc.killSkillEffectTimer(STATUS_ELFBRAVE);
		}
		if (pc.hasSkillEffect(HOLY_WALK)) {				// 神聖疾走	移速1.33倍
			pc.killSkillEffectTimer(HOLY_WALK);
		}
		if (pc.hasSkillEffect(MOVING_ACCELERATION)) {	// 行走加速	移速1.33倍
			pc.killSkillEffectTimer(MOVING_ACCELERATION);
		}
		if (pc.hasSkillEffect(WIND_WALK)) {				// 風之疾走	移速1.33倍
			pc.killSkillEffectTimer(WIND_WALK);
		}
		if (pc.hasSkillEffect(BLOODLUST)) {				// 血之渴望	攻速1.33倍
			pc.killSkillEffectTimer(BLOODLUST);
		}
		if (pc.hasSkillEffect(STATUS_BRAVE2)) {			// 超級加速	2.66倍
			pc.killSkillEffectTimer(STATUS_BRAVE2);
		}

		pc.setSkillEffect(STATUS_BRAVE, timeMillis);

		int objId = pc.getId();
		pc.sendPackets(new S_SkillSound(objId, 751));
		pc.broadcastPacket(new S_SkillSound(objId, 751));
		pc.sendPackets(new S_SkillBrave(objId, 1, timeMillis / 1000));
		pc.broadcastPacket(new S_SkillBrave(objId, 1, 0));
		pc.setBraveSpeed(1);
	}

	public static void thirdSpeed(L1PcInstance pc) {
		if (pc.hasSkillEffect(EFFECT_THIRD_SPEED)) {
			pc.killSkillEffectTimer(EFFECT_THIRD_SPEED);
		}

		pc.setSkillEffect(EFFECT_THIRD_SPEED, 600 * 1000);

		pc.sendPackets(new S_SkillSound(pc.getId(), 8031));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 8031));
		pc.sendPackets(new S_Liquor(pc.getId(), 8)); // 人物 * 1.15
		pc.broadcastPacket(new S_Liquor(pc.getId(), 8)); // 人物 * 1.15
		pc.sendPackets(new S_ServerMessage(1065)); // 將發生神秘的奇蹟力量。
	}

	public static int skillEffect(L1Character _user, L1Character cha, L1Character _target, int skillId, int _getBuffIconDuration, int dmg) {
		L1PcInstance _player = null;
		if (_user instanceof L1PcInstance) {
			L1PcInstance _pc = (L1PcInstance) _user;
			_player = _pc;
		}
		
		switch(skillId) {
		// PC、NPC 2方皆有效果
			// 解毒術
			case CURE_POISON:
				cha.curePoison();
				break;
			// 聖潔之光
			case REMOVE_CURSE:
				cha.curePoison();
				if (cha.hasSkillEffect(STATUS_CURSE_PARALYZING) || cha.hasSkillEffect(STATUS_CURSE_PARALYZED)) {
					cha.cureParalaysis();
				}
				break;
			// 返生術、終極返生術
			case RESURRECTION:
			case GREATER_RESURRECTION:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (_player.getId() != pc.getId()) {
						if (L1World.getInstance().getVisiblePlayer(pc, 0).size() > 0) {
							for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(pc, 0)) {
								if (!visiblePc.isDead()) {
									// 復活失敗，因為這個位置已被佔據。
									_player.sendPackets(new S_ServerMessage(592));
									return 0;
								}
							}
						}
						if ((pc.getCurrentHp() == 0) && pc.isDead()) {
							if (pc.getMap().isUseResurrection()) {
								if (skillId == RESURRECTION) {
									pc.setGres(false);
								}
								else if (skillId == GREATER_RESURRECTION) {
									pc.setGres(true);
								}
								pc.setTempID(_player.getId());
								pc.sendPackets(new S_Message_YN(322, "")); // 是否要復活？ (Y/N)
							}
						}
					}
				} else if (cha instanceof L1NpcInstance) {
					if (!(cha instanceof L1TowerInstance)) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						if (npc.getNpcTemplate().isCantResurrect() && !(npc instanceof L1PetInstance)) {
							return 0;
						}
						if ((npc instanceof L1PetInstance) && (L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0)) {
							for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
								if (!visiblePc.isDead()) {
									// 復活失敗，因為這個位置已被佔據。
									_player.sendPackets(new S_ServerMessage(592));
									return 0;
								}
							}
						}
						if ((npc.getCurrentHp() == 0) && npc.isDead()) {
							npc.resurrect(npc.getMaxHp() / 4);
							npc.setResurrect(true);
						}
					}
				}
				break;
			// 生命呼喚
			case CALL_OF_NATURE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (_player.getId() != pc.getId()) {
						if (L1World.getInstance().getVisiblePlayer(pc, 0).size() > 0) {
							for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(pc, 0)) {
								if (!visiblePc.isDead()) {
									// 復活失敗，因為這個位置已被佔據。
									_player.sendPackets(new S_ServerMessage(592));
									return 0;
								}
							}
						}
						if ((pc.getCurrentHp() == 0) && pc.isDead()) {
							pc.setTempID(_player.getId());
							pc.sendPackets(new S_Message_YN(322, "")); // 是否要復活？ (Y/N)
						}
					}
				} else if (cha instanceof L1NpcInstance) {
					if (!(cha instanceof L1TowerInstance)) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						if (npc.getNpcTemplate().isCantResurrect() && !(npc instanceof L1PetInstance)) {
							return 0;
						}
						if ((npc instanceof L1PetInstance) && (L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0)) {
							for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
								if (!visiblePc.isDead()) {
									// 復活失敗，因為這個位置已被佔據。
									_player.sendPackets(new S_ServerMessage(592));
									return 0;
								}
							}
						}
						if ((npc.getCurrentHp() == 0) && npc.isDead()) {
							npc.resurrect(cha.getMaxHp());
							npc.resurrect(cha.getMaxMp() / 100);
							npc.setResurrect(true);
						}
					}
				}
				break;
			// 無所遁形
			case DETECTION:
				if (cha instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					int hiddenStatus = npc.getHiddenStatus();
					if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
						npc.appearOnGround(_player);
					}
				}
				break;
			// 弱化屬性
			case ELEMENTAL_FALL_DOWN:
				if (_user instanceof L1PcInstance) {
					int playerAttr = _player.getElfAttr();
					int i = -50;
					if (playerAttr != 0) {
						_player.sendPackets(new S_SkillSound(cha.getId(), 4396));
						_player.broadcastPacket(new S_SkillSound(cha.getId(), 4396));
					}
					switch (playerAttr) {
						case 0:
							_player.sendPackets(new S_ServerMessage(79));
							break;
						case 1:
							cha.addEarth(i);
							cha.setAddAttrKind(1);
							break;
						case 2:
							cha.addFire(i);
							cha.setAddAttrKind(2);
							break;
						case 4:
							cha.addWater(i);
							cha.setAddAttrKind(4);
							break;
						case 8:
							cha.addWind(i);
							cha.setAddAttrKind(8);
							break;
						default:
							break;
					}
				}
				break;

		// 物理性技能效果
			// 三重矢
			case TRIPLE_ARROW:
				boolean gfxcheck = false;
				int[] BowGFX =
				{ 138, 37, 3860, 3126, 3420, 2284, 3105, 3145, 3148, 3151, 3871, 4125, 2323, 3892, 3895, 3898, 3901, 4917, 4918, 4919, 4950,
						6087, 6140, 6145, 6150, 6155, 6160, 6269, 6272, 6275, 6278, 6826, 6827, 6836, 6837, 6846, 6847, 6856, 6857, 6866, 6867,
						6876, 6877, 6886, 6887 };
				int playerGFX = _player.getTempCharGfx();
				for (int gfx : BowGFX) {
					if (playerGFX == gfx) {
						gfxcheck = true;
						break;
					}
				}
				if (!gfxcheck) {
					return 0;
				}

				L1ItemInstance bow = _player.getWeapon();
				L1ItemInstance arrow = null;
				arrow = _player.getInventory().getArrow();

				for (int i = 3; i > 0; i--) {
					if (arrow == null && bow.getItem().getItemId() != 190) {
						_player.sendPackets(new S_AttackPacket(_player, _target.getId(), 1, 0));
						_player.broadcastPacket(new S_AttackPacket(_player, _target.getId(), 1, 0));
					} else {
						_target.onAction(_player);
					}
				}
				_player.sendPackets(new S_SkillSound(_player.getId(), 4394));
				_player.broadcastPacket(new S_SkillSound(_player.getId(), 4394));
				break;
			// 屠宰者
			case FOE_SLAYER:
				_player.setFoeSlayer(true);
				for (int i = 3; i > 0; i--) {
					_target.onAction(_player);
				}
				_player.setFoeSlayer(false);

				_player.sendPackets(new S_EffectLocation(_target.getX(), _target.getY(), 6509));
				_player.broadcastPacket(new S_EffectLocation(_target.getX(), _target.getY(), 6509));
				_player.sendPackets(new S_SkillSound(_player.getId(), 7020));
				_player.broadcastPacket(new S_SkillSound(_player.getId(), 7020));

				if (_player.hasSkillEffect(SPECIAL_EFFECT_WEAKNESS_LV1)) {
					_player.killSkillEffectTimer(SPECIAL_EFFECT_WEAKNESS_LV1);
					_player.sendPackets(new S_SkillIconGFX(75, 0));
				} else if (_player.hasSkillEffect(SPECIAL_EFFECT_WEAKNESS_LV2)) {
					_player.killSkillEffectTimer(SPECIAL_EFFECT_WEAKNESS_LV2);
					_player.sendPackets(new S_SkillIconGFX(75, 0));
				} else if (_player.hasSkillEffect(SPECIAL_EFFECT_WEAKNESS_LV3)) {
					_player.killSkillEffectTimer(SPECIAL_EFFECT_WEAKNESS_LV3);
					_player.sendPackets(new S_SkillIconGFX(75, 0));
				}
			break;
			// 暴擊
			case SMASH:
				_target.onAction(_player, SMASH);
				break;
			// 骷髏毀壞
			case BONE_BREAK:
				_target.onAction(_player, BONE_BREAK);
				break;

		// 機率性魔法
			// 混亂
			case CONFUSION:
				// 發動判斷
				if (_user instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _user;
					if (!cha.hasSkillEffect(CONFUSION)) {
						int change = Random.nextInt(100) + 1;
						if (change < (30 + Random.nextInt(11))) { // 30 ~ 40%
							pc.sendPackets(new S_SkillSound(cha.getId(), 6525));
							pc.broadcastPacket(new S_SkillSound(cha.getId(), 6525));
							cha.setSkillEffect(CONFUSION, 2 * 1000); // 發動後再次發動間隔 2秒
							cha.setSkillEffect(CONFUSION_ING, 8 * 1000);
							if (cha instanceof L1PcInstance) {
								L1PcInstance targetPc = (L1PcInstance) cha;
								targetPc.sendPackets(new S_ServerMessage(1339)); // 突然感覺到混亂。
							}
						}
					}
				}
				break;
			// 闇盲咒術
			// 黑闇之影
			case CURSE_BLIND:
			case DARKNESS:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) { // 漂浮之眼肉效果
						pc.sendPackets(new S_CurseBlind(2));
					} else {
						pc.sendPackets(new S_CurseBlind(1));
					}
				}
				break;
			// 毒咒
			case CURSE_POISON:
				L1DamagePoison.doInfection(_user, cha, 3000, 5);
				break;
			// 木乃伊的咀咒
			case CURSE_PARALYZE:
			case CURSE_PARALYZE2:
				if (!cha.hasSkillEffect(EARTH_BIND) && !cha.hasSkillEffect(ICE_LANCE) && !cha.hasSkillEffect(FREEZING_BLIZZARD)
						&& !cha.hasSkillEffect(FREEZING_BREATH)) {
					if (cha instanceof L1PcInstance) {
						L1CurseParalysis.curse(cha, 8000, 16000);
					} else if (cha instanceof L1MonsterInstance) {
						L1CurseParalysis.curse(cha, 8000, 16000);
					}
				}
				break;
			// 弱化術
			case WEAKNESS:
				cha.addDmgup(-5);
				cha.addHitup(-1);
				break;
			// 疾病術
			case DISEASE:
				cha.addDmgup(-6);
				cha.addAc(12);
				break;
			// 風之枷鎖
			case WIND_SHACKLE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration));
					pc.broadcastPacket(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration));
				}
				break;
			// 魔法相消術
			case CANCELLATION:
				if (cha instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					int npcId = npc.getNpcTemplate().get_npcId();
					if (npcId == 71092) { // 調査員
						if (npc.getGfxId() == npc.getTempCharGfx()) {
							npc.setTempCharGfx(1314);
							npc.broadcastPacket(new S_ChangeShape(npc.getId(), 1314));
							return 0;
						}
						else {
							return 0;
						}
					}
					if (npcId == 45640) { // ユニコーン
						if (npc.getGfxId() == npc.getTempCharGfx()) {
							npc.setCurrentHp(npc.getMaxHp());
							npc.setTempCharGfx(2332);
							npc.broadcastPacket(new S_ChangeShape(npc.getId(), 2332));
							npc.setName("$2103");
							npc.setNameId("$2103");
							npc.broadcastPacket(new S_ChangeName(npc.getId(), "$2103"));
						}
						else if (npc.getTempCharGfx() == 2332) {
							npc.setCurrentHp(npc.getMaxHp());
							npc.setTempCharGfx(2755);
							npc.broadcastPacket(new S_ChangeShape(npc.getId(), 2755));
							npc.setName("$2488");
							npc.setNameId("$2488");
							npc.broadcastPacket(new S_ChangeName(npc.getId(), "$2488"));
						}
					}
					if (npcId == 81209) { // ロイ
						if (npc.getGfxId() == npc.getTempCharGfx()) {
							npc.setTempCharGfx(4310);
							npc.broadcastPacket(new S_ChangeShape(npc.getId(), 4310));
							return 0;
						}
						else {
							return 0;
						}
					}
				}
				if ((_player != null) && _player.isInvisble()) {
					_player.delInvis();
				}
				if (!(cha instanceof L1PcInstance)) {
					L1NpcInstance npc = (L1NpcInstance) cha;
					npc.setMoveSpeed(0);
					npc.setBraveSpeed(0);
					npc.broadcastPacket(new S_SkillHaste(cha.getId(), 0, 0));
					npc.broadcastPacket(new S_SkillBrave(cha.getId(), 0, 0));
					npc.setWeaponBreaked(false);
					npc.setParalyzed(false);
					npc.setParalysisTime(0);
				}

				// スキルの解除
				for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
					if (isNotCancelable(skillNum) && !cha.isDead()) {
						continue;
					}
					cha.removeSkillEffect(skillNum);
				}

				// ステータス強化、異常の解除
				cha.curePoison();
				cha.cureParalaysis();
				for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_END; skillNum++) {
					if ((skillNum == STATUS_CHAT_PROHIBITED // チャット禁止は解除しない
							)
							|| (skillNum == STATUS_CURSE_BARLOG // バルログの呪いは解除しない
							) || (skillNum == STATUS_CURSE_YAHEE)) { // ヤヒの呪いは解除しない
						continue;
					}
					cha.removeSkillEffect(skillNum);
				}

				if (cha instanceof L1PcInstance) {}

				// 料理の解除
				for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
					if (isNotCancelable(skillNum)) {
						continue;
					}
					cha.removeSkillEffect(skillNum);
				}

				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;

					// アイテム装備による変身の解除
					L1PolyMorph.undoPoly(pc);
					pc.sendPackets(new S_CharVisualUpdate(pc));
					pc.broadcastPacket(new S_CharVisualUpdate(pc));

					// ヘイストアイテム装備時はヘイスト関連のスキルが何も掛かっていないはずなのでここで解除
					if (pc.getHasteItemEquipped() > 0) {
						pc.setMoveSpeed(0);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
					}
				}
				cha.removeSkillEffect(STATUS_FREEZE); // Freeze解除
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_CharVisualUpdate(pc));
					pc.broadcastPacket(new S_CharVisualUpdate(pc));
					if (pc.isPrivateShop()) {
						pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, pc.getShopChat()));
						pc.broadcastPacket(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, pc.getShopChat()));
					}
					if (_user instanceof L1PcInstance) {
						L1PinkName.onAction(pc, _user);
					}
				}
				break;
			// 沉睡之霧
			case FOG_OF_SLEEPING:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true));
				}
				cha.setSleeped(true);
				break;
			// 護衛毀滅
			case GUARD_BRAKE:
				cha.addAc(15);
				break;
			// 驚悚死神
			case HORROR_OF_DEATH:
				cha.addStr(-5);
				cha.addInt(-5);
				break;
			// 釋放元素
			case RETURN_TO_NATURE:
				if (Config.RETURN_TO_NATURE && (cha instanceof L1SummonInstance)) {
					L1SummonInstance summon = (L1SummonInstance) cha;
					summon.broadcastPacket(new S_SkillSound(summon.getId(), 2245));
					summon.returnToNature();
				} else {
					if (_user instanceof L1PcInstance) {
						_player.sendPackets(new S_ServerMessage(79));
					}
				}
				break;
			// 壞物術
			case WEAPON_BREAK:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					L1ItemInstance weapon = pc.getWeapon();
					if (weapon != null) {
						int weaponDamage = Random.nextInt(_user.getInt() / 3) + 1;
						// \f1你的%0%s壞了。
						pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
						pc.getInventory().receiveDamage(weapon, weaponDamage);
					}
				} else {
					((L1NpcInstance) cha).setWeaponBreaked(true);
				}
				break;

		// 輔助性魔法
			// 鏡像
			case MIRROR_IMAGE:
				if (_user instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _user;
					byte dodge = pc.getDodge(); // 取得角色目前閃避率
					dodge = (byte) (dodge + 5); // 鏡像閃避率增加50
					int[] type = {dodge};
					pc.setDodge(dodge);
					pc.sendPackets(new S_PacketBox(88, type));
				}
				break;
			// 激勵士氣
			case GLOWING_AURA:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(5);
					pc.addBowHitup(5);
					pc.addMr(20);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(new S_SkillIconAura(113, _getBuffIconDuration));
				}
				break;
			// 鋼鐵士氣
			case SHINING_AURA:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addAc(-8);
					pc.sendPackets(new S_SkillIconAura(114, _getBuffIconDuration));
				}
				break;
			// 衝擊士氣
			case BRAVE_AURA:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(5);
					pc.sendPackets(new S_SkillIconAura(116, _getBuffIconDuration));
				}
				break;
			// 防護罩
			case SHIELD:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addAc(-2);
					pc.sendPackets(new S_SkillIconShield(5, _getBuffIconDuration));
				}
				break;
			// 影之防護
			case SHADOW_ARMOR:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addAc(-3);
					pc.sendPackets(new S_SkillIconShield(3, _getBuffIconDuration));
				}
				break;
			// 大地防護
			case EARTH_SKIN:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addAc(-6);
					pc.sendPackets(new S_SkillIconShield(6, _getBuffIconDuration));
				}
				break;
			// 大地的祝福
			case EARTH_BLESS:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addAc(-7);
					pc.sendPackets(new S_SkillIconShield(7, _getBuffIconDuration));
				}
				break;
			// 鋼鐵防護
			case IRON_SKIN:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addAc(-10);
					pc.sendPackets(new S_SkillIconShield(10, _getBuffIconDuration));
				}
				break;
			// 體魄強健術
			case PHYSICAL_ENCHANT_STR:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addStr((byte) 5);
					pc.sendPackets(new S_Strup(pc, 5, _getBuffIconDuration));
				}
				break;
			// 通暢氣脈術
			case PHYSICAL_ENCHANT_DEX:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDex((byte) 5);
					pc.sendPackets(new S_Dexup(pc, 5, _getBuffIconDuration));
				}
				break;
			// 力量提升
			case DRESS_MIGHTY:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addStr((byte) 2);
					pc.sendPackets(new S_Strup(pc, 2, _getBuffIconDuration));
				}
				break;
			// 敏捷提升
			case DRESS_DEXTERITY:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDex((byte) 2);
					pc.sendPackets(new S_Dexup(pc, 2, _getBuffIconDuration));
				}
				break;
			// 魔法防禦
			case RESIST_MAGIC:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addMr(10);
					pc.sendPackets(new S_SPMR(pc));
				}
				break;
			// 淨化精神
			case CLEAR_MIND:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addWis((byte) 3);
					pc.resetBaseMr();
				}
				break;
			// 屬性防禦
			case RESIST_ELEMENTAL:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addWind(10);
					pc.addWater(10);
					pc.addFire(10);
					pc.addEarth(10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;
			// 單屬性防禦
			case ELEMENTAL_PROTECTION:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					int attr = pc.getElfAttr();
					if (attr == 1) {
						pc.addEarth(50);
					}
					else if (attr == 2) {
						pc.addFire(50);
					}
					else if (attr == 4) {
						pc.addWater(50);
					}
					else if (attr == 8) {
						pc.addWind(50);
					}
				}
				break;
			// 心靈轉換
			case BODY_TO_MIND:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.setCurrentMp(pc.getCurrentMp() + 2);
				}
				break;
			// 魂體轉換
			case BLOODY_SOUL:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.setCurrentMp(pc.getCurrentMp() + 12);
				}
				break;
			// 隱身術、暗隱術
			case INVISIBILITY:
			case BLIND_HIDING:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Invis(pc.getId(), 1));
					pc.broadcastPacketForFindInvis(new S_RemoveObject(pc), false);
				}
				break;
			// 火焰武器
			case FIRE_WEAPON:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(4);
					pc.sendPackets(new S_SkillIconAura(147, _getBuffIconDuration));
				}
				break;
			// 烈炎氣息
			case FIRE_BLESS:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(4);
					pc.sendPackets(new S_SkillIconAura(154, _getBuffIconDuration));
				}
				break;
			// 烈炎武器
			case BURNING_WEAPON:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgup(6);
					pc.addHitup(3);
					pc.sendPackets(new S_SkillIconAura(162, _getBuffIconDuration));
				}
				break;
			// 風之神射
			case WIND_SHOT:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addBowHitup(6);
					pc.sendPackets(new S_SkillIconAura(148, _getBuffIconDuration));
				}
				break;
			// 暴風之眼
			case STORM_EYE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addBowHitup(2);
					pc.addBowDmgup(3);
					pc.sendPackets(new S_SkillIconAura(155, _getBuffIconDuration));
				}
				break;
			// 暴風神射
			case STORM_SHOT:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addBowDmgup(5);
					pc.addBowHitup(-1);
					pc.sendPackets(new S_SkillIconAura(165, _getBuffIconDuration));
				}
				break;
			// 狂暴術
			case BERSERKERS:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addAc(10);
					pc.addDmgup(5);
					pc.addHitup(2);
				}
				break;
			// 變形術
			case SHAPE_CHANGE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_ShowPolyList(pc.getId()));
					if (!pc.isShapeChange()) {
						pc.setShapeChange(true);
					}
				}
				break;
			// 靈魂昇華
			case ADVANCE_SPIRIT:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.setAdvenHp(pc.getBaseMaxHp() / 5);
					pc.setAdvenMp(pc.getBaseMaxMp() / 5);
					pc.addMaxHp(pc.getAdvenHp());
					pc.addMaxMp(pc.getAdvenMp());
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					if (pc.isInParty()) { // パーティー中
						pc.getParty().updateMiniHP(pc);
					}
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				}
				break;
			// 神聖疾走、行走加速、風之疾走
			case HOLY_WALK:
			case MOVING_ACCELERATION:
			case WIND_WALK:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.setBraveSpeed(4);
					pc.sendPackets(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 4, 0));
				}
				break;
			// 血之渴望
			case BLOODLUST:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.setBraveSpeed(6);
					pc.sendPackets(new S_SkillBrave(pc.getId(), 6, _getBuffIconDuration));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 6, 0));
				}
				break;
			// 覺醒技能
			case AWAKEN_ANTHARAS:
			case AWAKEN_FAFURION:
			case AWAKEN_VALAKAS:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					L1Awake.start(pc, skillId);
				}
				break;
			// 幻覺：歐吉
			case ILLUSION_OGRE:
				cha.addDmgup(4);
				cha.addHitup(4);
				cha.addBowDmgup(4);
				cha.addBowHitup(4);
				break;
			// 幻覺：巫妖
			case ILLUSION_LICH:
				cha.addSp(2);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SPMR(pc));
				}
				break;
			// 幻覺：鑽石高侖
			case ILLUSION_DIA_GOLEM:
				cha.addAc(-20);
				break;
			// 幻覺：化身
			case ILLUSION_AVATAR:
				cha.addDmgup(10);
				cha.addBowDmgup(10);
				break;
			// 洞察
			case INSIGHT:
				cha.addStr((byte) 1); 
				cha.addCon((byte) 1);
				cha.addDex((byte) 1);
				cha.addWis((byte) 1);
				cha.addInt((byte) 1);
				break;
			// 恐慌
			case PANIC:
				cha.addStr((byte) -1);
				cha.addCon((byte) -1);
				cha.addDex((byte) -1);
				cha.addWis((byte) -1);
				cha.addInt((byte) -1);
				break;
			// 絕對屏障
			case ABSOLUTE_BARRIER:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.stopHpRegeneration();
					pc.stopMpRegeneration();
					pc.stopMpRegenerationByDoll();
				}
				break;

		// 目標 NPC
			// 能量感測
			case WEAK_ELEMENTAL:
				if (cha instanceof L1MonsterInstance) {
					L1Npc npcTemp = ((L1MonsterInstance) cha).getNpcTemplate();
					int weakAttr = npcTemp.get_weakAttr();
					if ((weakAttr & 1) == 1) { // 地
						cha.broadcastPacket(new S_SkillSound(cha.getId(), 2169));
					} else if ((weakAttr & 2) == 2) { // 火
						cha.broadcastPacket(new S_SkillSound(cha.getId(), 2167));
					} else if ((weakAttr & 4) == 4) { // 水
						cha.broadcastPacket(new S_SkillSound(cha.getId(), 2166));
					} else if ((weakAttr & 8) == 8) { // 風
						cha.broadcastPacket(new S_SkillSound(cha.getId(), 2168));
					} else {
						if (_user instanceof L1PcInstance) {
							_player.sendPackets(new S_ServerMessage(79));
						}
					}
				} else {
					if (_user instanceof L1PcInstance) {
						_player.sendPackets(new S_ServerMessage(79));
					}
				}
				break;

		// 傳送性魔法
			// 世界樹的呼喚
			case TELEPORT_TO_MATHER:
				if (_user instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.getMap().isEscapable() || pc.isGm()) {
						L1Teleport.teleport(pc, 33051, 32337, (short) 4, 5, true);
					} else {
						pc.sendPackets(new S_ServerMessage(647));
						L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), false);
					}
				}
				break;

		// 召喚、迷魅、造屍
			// 召喚術
			case SUMMON_MONSTER:
				if (_user instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					int level = pc.getLevel();
					int[] summons;
					if (pc.getMap().isRecallPets()) {
						if (pc.getInventory().checkEquipped(20284)) {
							pc.sendPackets(new S_ShowSummonList(pc.getId()));
							if (!pc.isSummonMonster()) {
								pc.setSummonMonster(true);
							}
						}
						else {
							/*
							 * summons = new int[] { 81083, 81084, 81085,
							 * 81086, 81087, 81088, 81089 };
							 */
							summons = new int[]
							{ 81210, 81213, 81216, 81219, 81222, 81225, 81228 };
							int summonid = 0;
							// int summoncost = 6;
							int summoncost = 8;
							int levelRange = 32;
							for (int i = 0; i < summons.length; i++) { // 該当ＬＶ範囲検索
								if ((level < levelRange) || (i == summons.length - 1)) {
									summonid = summons[i];
									break;
								}
								levelRange += 4;
							}

							int petcost = 0;
							Object[] petlist = pc.getPetList().values().toArray();
							for (Object pet : petlist) {
								// 現在のペットコスト
								petcost += ((L1NpcInstance) pet).getPetcost();
							}
							int pcCha = pc.getCha();
							if (pcCha > 34) { // max count = 5
								pcCha = 34;
							}
							int charisma = pcCha + 6 - petcost;
							// int charisma = pc.getCha() + 6 - petcost;
							int summoncount = charisma / summoncost;
							L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
							for (int i = 0; i < summoncount; i++) {
								L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
								summon.setPetcost(summoncost);
							}
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				break;
			// 召喚屬性精靈、召喚強力屬性精靈
			case LESSER_ELEMENTAL:
			case GREATER_ELEMENTAL:
				if (_user instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					int attr = pc.getElfAttr();
					if (attr != 0) { // 無属性でなければ実行
						if (pc.getMap().isRecallPets()) {
							int petcost = 0;
							Object[] petlist = pc.getPetList().values().toArray();
							for (Object pet : petlist) {
								// 現在のペットコスト
								petcost += ((L1NpcInstance) pet).getPetcost();
							}

							if (petcost == 0) { // 1匹も所属NPCがいなければ実行
								int summonid = 0;
								int summons[];
								if (skillId == LESSER_ELEMENTAL) { // レッサーエレメンタル[地,火,水,風]
									summons = new int[]
									{ 45306, 45303, 45304, 45305 };
								}
								else {
									// グレーターエレメンタル[地,火,水,風]
									summons = new int[]
									{ 81053, 81050, 81051, 81052 };
								}
								int npcattr = 1;
								for (int i = 0; i < summons.length; i++) {
									if (npcattr == attr) {
										summonid = summons[i];
										i = summons.length;
									}
									npcattr *= 2;
								}
								// 特殊設定の場合ランダムで出現
								if (summonid == 0) {

									int k3 = Random.nextInt(4);
									summonid = summons[k3];
								}

								L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
								L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
								summon.setPetcost(pc.getCha() + 7); // 精霊の他にはNPCを所属させられない
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
					} else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				break;
			// 迷魅術
			case TAMING_MONSTER:
				if (cha instanceof L1MonsterInstance) {
					L1MonsterInstance npc = (L1MonsterInstance) cha;
					// 可迷魅的怪物
					if (npc.getNpcTemplate().isTamable()) {
						int petcost = 0;
						Object[] petlist = _user.getPetList().values().toArray();
						for (Object pet : petlist) {
							// 現在のペットコスト
							petcost += ((L1NpcInstance) pet).getPetcost();
						}
						int charisma = _user.getCha();
						if (_player.isElf()) { // エルフ
							if (charisma > 30) { // max count = 7
								charisma = 30;
							}
							charisma += 12;
						}
						else if (_player.isWizard()) { // ウィザード
							if (charisma > 36) { // max count = 7
								charisma = 36;
							}
							charisma += 6;
						}
						charisma -= petcost;
						if (charisma >= 6) { // ペットコストの確認
							L1SummonInstance summon = new L1SummonInstance(npc, _user, false);
							_target = summon; // ターゲット入替え
						}
						else {
							_player.sendPackets(new S_ServerMessage(319)); // \f1これ以上のモンスターを操ることはできません。
						}
					}
				}
				break;
			// 造屍術
			case CREATE_ZOMBIE:
				if (cha instanceof L1MonsterInstance) {
					L1MonsterInstance npc = (L1MonsterInstance) cha;
					int petcost = 0;
					Object[] petlist = _user.getPetList().values().toArray();
					for (Object pet : petlist) {
						// 現在のペットコスト
						petcost += ((L1NpcInstance) pet).getPetcost();
					}
					int charisma = _user.getCha();
					if (_player.isElf()) { // エルフ
						if (charisma > 30) { // max count = 7
							charisma = 30;
						}
						charisma += 12;
					}
					else if (_player.isWizard()) { // ウィザード
						if (charisma > 36) { // max count = 7
							charisma = 36;
						}
						charisma += 6;
					}
					charisma -= petcost;
					if (charisma >= 6) { // ペットコストの確認
						L1SummonInstance summon = new L1SummonInstance(npc, _user, true);
						_target = summon; // ターゲット入替え
					} else {
						_player.sendPackets(new S_ServerMessage(319)); // \f1これ以上のモンスターを操ることはできません。
					}
				}
				break;

		// 怪物專屬魔法
			case 10026:
			case 10027:
			case 10028:
			case 10029:
				if (_user instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) _user;
					_user.broadcastPacket(new S_NpcChatPacket(npc, "$3717", 0)); // さあ、おまえに安息を与えよう。
				} else {
					_player.broadcastPacket(new S_ChatPacket(_player, "$3717", 0, 0)); // さあ、おまえに安息を与えよう。
				}
				break;
			case 10057:
				L1Teleport.teleportToTargetFront(cha, _user, 1);
				break;
			case STATUS_FREEZE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
				}
				break;
			default:
				break;
		}
		return dmg;
	}

	private static boolean isNotCancelable(int skillNum) {
		return (skillNum == ENCHANT_WEAPON) || (skillNum == BLESSED_ARMOR)
				|| (skillNum == ABSOLUTE_BARRIER) || (skillNum == ADVANCE_SPIRIT)
				|| (skillNum == SHOCK_STUN) || (skillNum == SHADOW_FANG)
				|| (skillNum == REDUCTION_ARMOR) || (skillNum == SOLID_CARRIAGE)
				|| (skillNum == COUNTER_BARRIER) || (skillNum == AWAKEN_ANTHARAS)
				|| (skillNum == AWAKEN_FAFURION) || (skillNum == AWAKEN_VALAKAS);
	}
}
