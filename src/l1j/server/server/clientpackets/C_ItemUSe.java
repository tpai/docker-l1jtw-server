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

import static l1j.server.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static l1j.server.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static l1j.server.server.model.skill.L1SkillId.AWAKEN_VALAKAS;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NOW;
import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static l1j.server.server.model.skill.L1SkillId.SOLID_CARRIAGE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FLOATING_EYE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_MITHRIL_POWDER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER_OF_EVA;

import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.ClientThread;
import l1j.server.server.FishingTimeController;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.LogEnchantTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1DragonSlayer;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1GuardianInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.identity.L1ItemId;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.action.Effect;
import l1j.server.server.model.item.action.Enchant;
import l1j.server.server.model.item.action.MagicDoll;
import l1j.server.server.model.item.action.Potion;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_AddSkill;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_DragonGate;
import l1j.server.server.serverpackets.S_Fishing;
import l1j.server.server.serverpackets.S_IdentifyDesc;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_ItemStatus;
import l1j.server.server.serverpackets.S_Letter;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShowPolyList;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.serverpackets.S_UseMap;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.types.Point;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.Random;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket
//

/**
 * TODO: 翻譯，太多了= = 處理收到由客戶端傳來使用道具的封包
 */
public class C_ItemUSe extends ClientBasePacket {

	private static final String C_ITEM_USE = "[C] C_ItemUSe";

	private static Logger _log = Logger.getLogger(C_ItemUSe.class.getName());

	public C_ItemUSe(byte abyte0[], ClientThread client) throws Exception {
		super(abyte0);
		int itemObjid = readD();

		L1PcInstance pc = client.getActiveChar();
		if (pc.isGhost()) {
			return;
		}
		L1ItemInstance l1iteminstance = pc.getInventory().getItem(itemObjid);

		if ((l1iteminstance == null) || (pc.isDead())) {
			return;
		}

		if (l1iteminstance.getItem().getUseType() == -1) { // none:不能使用的道具
			pc.sendPackets(new S_ServerMessage(74, l1iteminstance.getLogName())); // \f1%0は使用できません。
			return;
		}
		int pcObjid = pc.getId();
		if (pc.isTeleport()) { // 傳送中
			return;
		}
		if (!pc.getMap().isUsableItem()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1ここでは使えません。
			return;
		}
		int itemId;
		try {
			itemId = l1iteminstance.getItem().getItemId();
		}
		catch (Exception e) {
			return;
		}
		int l = 0;

		String s = "";
		@SuppressWarnings("unused")
		int bmapid = 0;
		int btele = 0;
		int blanksc_skillid = 0;
		int spellsc_objid = 0;
		int spellsc_x = 0;
		int spellsc_y = 0;
		int resid = 0;
		int letterCode = 0;
		String letterReceiver = "";
		byte[] letterText = null;
		int cookStatus = 0;
		int cookNo = 0;
		int fishX = 0;
		int fishY = 0;

		int use_type = l1iteminstance.getItem().getUseType();
		if ((itemId == 40088) || (itemId == 40096) || (itemId == 49308) || (itemId == 140088)) {
			s = readS();
		}
		else if ((itemId == L1ItemId.SCROLL_OF_ENCHANT_ARMOR) || (itemId == L1ItemId.SCROLL_OF_ENCHANT_WEAPON)
				|| (itemId == L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON) || (itemId == 40077) || (itemId == 40078) || (itemId == 40126)
				|| (itemId == 40098) || (itemId == 40129) || (itemId == 40130) || (itemId == 140129) || (itemId == 140130)
				|| (itemId == L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR) || (itemId == L1ItemId.B_SCROLL_OF_ENCHANT_WEAPON)
				|| (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR) || (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON)
				|| (itemId == 41029 // 召喚球の欠片
				) || (itemId == 40317)
				|| (itemId == 49188) // 索夏依卡靈魂之石
				|| (itemId == 41036) || (itemId == 41245) || (itemId == 40127) || (itemId == 40128) || (itemId == 41048) || (itemId == 41049)
				|| (itemId == 41050 // 糊付けされた航海日誌ページ
				) || (itemId == 41051) || (itemId == 41052) || (itemId == 41053 // 糊付けされた航海日誌ページ
				) || (itemId == 41054) || (itemId == 41055) || (itemId == 41056 // 糊付けされた航海日誌ページ
				) || (itemId == 41057 // 糊付けされた航海日誌ページ
				) || (itemId == 40925) || (itemId == 40926) || (itemId == 40927 // 浄化・ミステリアスポーション
				) || (itemId == 40928) || (itemId == 40929) || (itemId == 40931) || (itemId == 40932) || (itemId == 40933 // 加工されたサファイア
				) || (itemId == 40934) || (itemId == 40935) || (itemId == 40936) || (itemId == 40937 // 加工されたエメラルド
				) || (itemId == 40938) || (itemId == 40939) || (itemId == 40940) || (itemId == 40941 // 加工されたルビー
				) || (itemId == 40942) || (itemId == 40943) || (itemId == 40944) || (itemId == 40945 // 加工された地ダイア
				) || (itemId == 40946) || (itemId == 40947) || (itemId == 40948) || (itemId == 40949 // 加工された水ダイア
				) || (itemId == 40950) || (itemId == 40951) || (itemId == 40952) || (itemId == 40953 // 加工された風ダイア
				) || (itemId == 40954) || (itemId == 40955) || (itemId == 40956) || (itemId == 40957 // 加工された火ダイア
				) || (itemId == 40958) || (itemId == 40964 // ダークマジックパウダー
				) || (itemId == 49092 // 龜裂之核
				) || (itemId == 49094 // 歐西里斯初級寶箱碎片(下)
				) || (itemId == 49098 // 歐西里斯高級寶箱碎片(下)
				) || (itemId == 49317 // 庫庫爾坎初級寶箱碎片(下)
				) || (itemId == 49321 // 庫庫爾坎高級寶箱碎片(下)
				) || (itemId == 41426 // 封印卷軸
				) || (itemId == 41427 // 解除封印卷軸
				) || (itemId == 40075 // 毀滅盔甲的卷軸
				) || (itemId == 49311 // 象牙塔對盔甲施法的卷軸
				) || (itemId == 49312 // 象牙塔對武器施法的卷軸
				) || (itemId == 49148 // 飾品強化卷軸 Scroll of Enchant Accessory
				) || (itemId == 41429 // 風之武器強化卷軸
				) || (itemId == 41430 // 地之武器強化卷軸
				) || (itemId == 41431 // 水之武器強化卷軸
				) || (itemId == 41432 // 火之武器強化卷軸
				) || (itemId == 47041 // 地龍之精緻魔眼
				) || (itemId == 47042 // 水龍之精緻魔眼
				) || (itemId == 47043 // 風龍之精緻魔眼
				) || (itemId == 47044 // 火龍之精緻魔眼
				) || (itemId == 47045 // 誕生之精緻魔眼
				) || (itemId == 47046 // 形象之精緻魔眼
				) || (itemId == 47048 // 附魔強化卷軸
				) || (itemId == 47049 // 近戰附魔轉換卷軸
				) || (itemId == 47050 // 遠攻附魔轉換卷軸
				) || (itemId == 47051 // 恢復附魔轉換卷軸
				) || (itemId == 47052 // 防禦附魔轉換卷軸
				)) {
			l = readD();
		}
		else if ((itemId == 140100) || (itemId == 40100) || (itemId == 40099) || (itemId == 40086) || (itemId == 40863)) {
			bmapid = readH();
			btele = readD();
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
		}
		else if ((itemId == 40090) || (itemId == 40091) || (itemId == 40092) || (itemId == 40093) || (itemId == 40094)) { // ブランクスクロール(Lv1)～(Lv5)
			blanksc_skillid = readC();
		}
		else if ((use_type == 30) || (itemId == 40870) || (itemId == 40879)) { // spell_buff
			spellsc_objid = readD();
		}
		else if ((use_type == 5) || (use_type == 17)) { // spell_long、spell_short
			spellsc_objid = readD();
			spellsc_x = readH();
			spellsc_y = readH();
		}
		else if ((itemId == 40089) || (itemId == 140089)) { // 復活スクロール、祝福された復活スクロール
			resid = readD();
		}
		else if ((itemId == 40310) || (itemId == 40311) || (itemId == 40730) || (itemId == 40731) || (itemId == 40732)) { // 便箋
			letterCode = readH();
			letterReceiver = readS();
			letterText = readByte();
		}
		else if ((itemId >= 41255) && (itemId <= 41259)) { // 料理の本
			cookStatus = readC();
			cookNo = readC();
		}
		else if ((itemId == 41293) || (itemId == 41294)) { // 釣り竿
			fishX = readH();
			fishY = readH();
		}
		else {
			l = readC();
		}

		if (pc.getCurrentHp() > 0) {
			int delay_id = 0;
			if (l1iteminstance.getItem().getType2() == 0) { // 種別：その他のアイテム
				delay_id = ((L1EtcItem) l1iteminstance.getItem()).get_delayid();
			}
			if (delay_id != 0) { // ディレイ設定あり
				if (pc.hasItemDelay(delay_id) == true) {
					return;
				}
			}

			// 再使用チェック
			boolean isDelayEffect = false;
			if (l1iteminstance.getItem().getType2() == 0) { // etcitem
				int delayEffect = ((L1EtcItem) l1iteminstance.getItem()).get_delayEffect();
				if (delayEffect > 0) {
					isDelayEffect = true;
					Timestamp lastUsed = l1iteminstance.getLastUsed();
					if (lastUsed != null) {
						Calendar cal = Calendar.getInstance();
						if ((cal.getTimeInMillis() - lastUsed.getTime()) / 1000 <= delayEffect) {
							// \f1何も起きませんでした。
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
			}

			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(l);
			_log.finest("request item use (obj) = " + itemObjid + " action = " + l + " value = " + s);
			if ((itemId == 40077) || (itemId == L1ItemId.SCROLL_OF_ENCHANT_WEAPON) || (itemId == L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON)
					|| (itemId == 40130) || (itemId == 140130) || (itemId == L1ItemId.B_SCROLL_OF_ENCHANT_WEAPON)
					|| (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON) || (itemId == 40128)) { // 對武器施法的卷軸
				Enchant.scrollOfEnchantWeapon(pc, l1iteminstance, l1iteminstance1, client);
			}
			else if (itemId == 49312) { // 象牙塔對武器施法的卷軸
				Enchant.scrollOfEnchantWeaponIvoryTower(pc, l1iteminstance, l1iteminstance1, client);
			}
			else if ((itemId == 41429) || (itemId == 41430) || (itemId == 41431) || (itemId == 41432)) { // 武器屬性強化卷軸
				Enchant.scrollOfEnchantWeaponAttr(pc, l1iteminstance, l1iteminstance1, client);
			}
			else if ((itemId == 40078) || (itemId == L1ItemId.SCROLL_OF_ENCHANT_ARMOR) || (itemId == 40129) || (itemId == 140129)
					|| (itemId == L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR) || (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR) || (itemId == 40127)) { // 對盔甲施法的卷軸
				Enchant.scrollOfEnchantArmor(pc, l1iteminstance, l1iteminstance1, client);
			}
			else if (itemId == 49311) { // 象牙塔對盔甲施法的卷軸
				Enchant.scrollOfEnchantArmorIvoryTower(pc, l1iteminstance, l1iteminstance1, client);
			}
			else if (l1iteminstance.getItem().getType2() == 0) { // 道具類
				int item_minlvl = ((L1EtcItem) l1iteminstance.getItem()).getMinLevel();
				int item_maxlvl = ((L1EtcItem) l1iteminstance.getItem()).getMaxLevel();
				if ((item_minlvl != 0) && (item_minlvl > pc.getLevel()) && !pc.isGm()) {
					pc.sendPackets(new S_ServerMessage(318, String.valueOf(item_minlvl))); // 等級 %0以上才可使用此道具。
					return;
				}
				else if ((item_maxlvl != 0) && (item_maxlvl < pc.getLevel()) && !pc.isGm()) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, item_maxlvl)); // 等級%d以下才能使用此道具。
					return;
				}

				if (((itemId == 40576) && !pc.isElf()) // 魂の結晶の破片（白）
						|| ((itemId == 40577) && !pc.isWizard()) // 魂の結晶の破片（黒）
						|| ((itemId == 40578) && !pc.isKnight())) { // 魂の結晶の破片（赤）
					pc.sendPackets(new S_ServerMessage(264)); // \f1あなたのクラスではこのアイテムは使用できません。
					return;
				}

				if (l1iteminstance.getItem().getType() == 0) { // アロー
					pc.getInventory().setArrow(l1iteminstance.getItem().getItemId());
					pc.sendPackets(new S_ServerMessage(452, l1iteminstance.getLogName())); // %0が選択されました。
				}
				else if (l1iteminstance.getItem().getType() == 15) { // スティング
					pc.getInventory().setSting(l1iteminstance.getItem().getItemId());
					pc.sendPackets(new S_ServerMessage(452, // %0が選択されました。
							l1iteminstance.getLogName()));
				}
				else if (l1iteminstance.getItem().getType() == 16) { // treasure_box
					L1TreasureBox box = L1TreasureBox.get(itemId);

					if (box != null) {
						if (box.open(pc)) {
							L1EtcItem temp = (L1EtcItem) l1iteminstance.getItem();
							if (temp.get_delayEffect() > 0) {
								// 有限制再次使用的時間且可堆疊的道具
								if (l1iteminstance.isStackable()) {
									if (l1iteminstance.getCount() > 1) {
										isDelayEffect = true;
									}
									pc.getInventory().removeItem(l1iteminstance.getId(), 1);
								}
								else {
									isDelayEffect = true;
								}
							}
							else {
								pc.getInventory().removeItem(l1iteminstance.getId(), 1);
							}
						}
					}
				}
				else if (l1iteminstance.getItem().getType() == 2) { // light系アイテム
					if ((l1iteminstance.getRemainingTime() <= 0) && (itemId != 40004)) {
						return;
					}
					if (l1iteminstance.isNowLighting()) {
						l1iteminstance.setNowLighting(false);
						pc.turnOnOffLight();
					}
					else {
						l1iteminstance.setNowLighting(true);
						pc.turnOnOffLight();
					}
					pc.sendPackets(new S_ItemName(l1iteminstance));
				}
				else if (l1iteminstance.getItem().getType() == 17) { // 魔法娃娃類
					MagicDoll.useMagicDoll(pc, itemId, itemObjid);
				}
				else if (itemId == 47103) { // 新鮮的餌
					pc.sendPackets(new S_ServerMessage(452, l1iteminstance.getLogName()));
				}
				else if (itemId == 40003) { // ランタン オイル
					for (L1ItemInstance lightItem : pc.getInventory().getItems()) {
						if (lightItem.getItem().getItemId() == 40002) {
							lightItem.setRemainingTime(l1iteminstance.getItem().getLightFuel());
							pc.sendPackets(new S_ItemName(lightItem));
							pc.sendPackets(new S_ServerMessage(230)); // ランタンにオイルを注ぎました。
							break;
						}
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 43000) { // 復活のポーション（Lv99キャラのみが使用可能/Lv1に戻る効果）
					pc.setExp(1);
					pc.resetLevel();
					pc.setBonusStats(0);
					pc.sendPackets(new S_SkillSound(pcObjid, 191));
					pc.broadcastPacket(new S_SkillSound(pcObjid, 191));
					pc.sendPackets(new S_OwnCharStatus(pc));
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(new S_ServerMessage(822)); // 独自アイテムですので、メッセージは適当です。
					pc.save(); // DBにキャラクター情報を書き込む

					// 處理新手保護系統(遭遇的守護)狀態資料的變動
					pc.checkNoviceType();
				}
				else if (itemId == 40033) { // エリクサー:腕力
					if ((pc.getBaseStr() < 35) && (pc.getElixirStats() < 5)) {
						pc.addBaseStr((byte) 1); // 素のSTR値に+1
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save(); // DBにキャラクター情報を書き込む
					}
					else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1一つの能力値の最大値は25です。他の能力値を選択してください。
					}
				}
				else if (itemId == 40034) { // エリクサー:体力
					if ((pc.getBaseCon() < 35) && (pc.getElixirStats() < 5)) {
						pc.addBaseCon((byte) 1); // 素のCON値に+1
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save(); // DBにキャラクター情報を書き込む
					}
					else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1一つの能力値の最大値は25です。他の能力値を選択してください。
					}
				}
				else if (itemId == 40035) { // エリクサー:機敏
					if ((pc.getBaseDex() < 35) && (pc.getElixirStats() < 5)) {
						pc.addBaseDex((byte) 1); // 素のDEX値に+1
						pc.resetBaseAc();
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save(); // DBにキャラクター情報を書き込む
					}
					else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1一つの能力値の最大値は25です。他の能力値を選択してください。
					}
				}
				else if (itemId == 40036) { // エリクサー:知力
					if ((pc.getBaseInt() < 35) && (pc.getElixirStats() < 5)) {
						pc.addBaseInt((byte) 1); // 素のINT値に+1
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save(); // DBにキャラクター情報を書き込む
					}
					else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1一つの能力値の最大値は25です。他の能力値を選択してください。
					}
				}
				else if (itemId == 40037) { // エリクサー:精神
					if ((pc.getBaseWis() < 35) && (pc.getElixirStats() < 5)) {
						pc.addBaseWis((byte) 1); // 素のWIS値に+1
						pc.resetBaseMr();
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save(); // DBにキャラクター情報を書き込む
					}
					else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1一つの能力値の最大値は25です。他の能力値を選択してください。
					}
				}
				else if (itemId == 40038) { // エリクサー:魅力
					if ((pc.getBaseCha() < 35) && (pc.getElixirStats() < 5)) {
						pc.addBaseCha((byte) 1); // 素のCHA値に+1
						pc.setElixirStats(pc.getElixirStats() + 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.save(); // DBにキャラクター情報を書き込む
					}
					else {
						pc.sendPackets(new S_ServerMessage(481)); // \f1一つの能力値の最大値は25です。他の能力値を選択してください。
					}
				}
				// 治癒藥水、濃縮體力恢復劑、象牙塔治癒藥水
				else if ((itemId == L1ItemId.POTION_OF_HEALING) || (itemId == L1ItemId.CONDENSED_POTION_OF_HEALING) || (itemId == 40029)) {
					Potion.UseHeallingPotion(pc, l1iteminstance, 15, 189);
				}
				else if (itemId == 40022) { // 古代體力恢復劑
					Potion.UseHeallingPotion(pc, l1iteminstance, 20, 189);
				}
				else if ((itemId == L1ItemId.POTION_OF_EXTRA_HEALING) // 強力治癒藥水、濃縮強力體力恢復劑
						|| (itemId == L1ItemId.CONDENSED_POTION_OF_EXTRA_HEALING)) {
					Potion.UseHeallingPotion(pc, l1iteminstance, 45, 194);
				}
				else if (itemId == 40023) { // 古代強力體力恢復劑
					Potion.UseHeallingPotion(pc, l1iteminstance, 30, 194);
				}
				else if ((itemId == L1ItemId.POTION_OF_GREATER_HEALING) // 終極治癒藥水
						// 濃縮終極體力恢復劑、凝聚的化合物、鮮奶油蛋糕、神秘的體力藥水
						|| (itemId == L1ItemId.CONDENSED_POTION_OF_GREATER_HEALING)
						|| (itemId == 47114) || (itemId == 49137) || (itemId == 41141)) {
					Potion.UseHeallingPotion(pc, l1iteminstance, 75, 197);
				}
				else if (itemId == 40024) { // 古代終極體力恢復劑
					Potion.UseHeallingPotion(pc, l1iteminstance, 55, 197);
				}
				else if (itemId == 40506) { // 安特的水果
					Potion.UseHeallingPotion(pc, l1iteminstance, 70, 197);
				}
				else if ((itemId == 40026) || (itemId == 40027) || (itemId == 40028)) { // 香蕉汁、橘子汁、蘋果汁
					Potion.UseHeallingPotion(pc, l1iteminstance, 25, 189);
				}
				else if (itemId == 40058) { // 煙燻的麵包屑
					Potion.UseHeallingPotion(pc, l1iteminstance, 30, 189);
				}
				else if (itemId == 40071) { // 烤焦的麵包屑
					Potion.UseHeallingPotion(pc, l1iteminstance, 70, 197);
				}
				else if (itemId == 40734) { // 信賴貨幣
					Potion.UseHeallingPotion(pc, l1iteminstance, 50, 189);
				}
				else if (itemId == L1ItemId.B_POTION_OF_HEALING) { // 受祝福的 治癒藥水
					Potion.UseHeallingPotion(pc, l1iteminstance, 25, 189);
				}
				else if (itemId == L1ItemId.C_POTION_OF_HEALING) { // 受咀咒的 治癒藥水
					Potion.UseHeallingPotion(pc, l1iteminstance, 10, 189);
				}
				else if (itemId == L1ItemId.B_POTION_OF_EXTRA_HEALING) { // 受祝福的 強力治癒藥水
					Potion.UseHeallingPotion(pc, l1iteminstance, 55, 194);
				}
				else if (itemId == L1ItemId.B_POTION_OF_GREATER_HEALING) { // 受祝福的 終極治癒藥水
					Potion.UseHeallingPotion(pc, l1iteminstance, 85, 197);
				}
				else if (itemId == 140506) { // 受祝福的 安特的水果
					Potion.UseHeallingPotion(pc, l1iteminstance, 80, 197);
				}
				else if (itemId == 40043) { // 兔子的肝
					Potion.UseHeallingPotion(pc, l1iteminstance, 600, 189);
				}
				else if (itemId == 41403) { // 庫傑的糧食
					Potion.UseHeallingPotion(pc, l1iteminstance, 300, 189);
				}
				else if ((itemId >= 41417) && (itemId <= 41421)) { // 日本「亞丁的夏天」活動道具 - 刨冰
					Potion.UseHeallingPotion(pc, l1iteminstance, 90, 197);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 41337) { // 受祝福的五穀麵包
					Potion.UseHeallingPotion(pc, l1iteminstance, 85, 197);
				}
				else if (itemId == 40858) { // liquor（酒）
					pc.setDrink(true);
					pc.sendPackets(new S_Liquor(pc.getId(), 1));
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if ((itemId == L1ItemId.POTION_OF_CURE_POISON) || (itemId == 40507)) { // 翡翠藥水
					if (pc.hasSkillEffect(DECAY_POTION)) { // 藥水霜化術狀態
						pc.sendPackets(new S_ServerMessage(698)); // 喉嚨灼熱，無法喝東西。
						return;
					} else {
						pc.sendPackets(new S_SkillSound(pc.getId(), 192));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 192));
						if (itemId == L1ItemId.POTION_OF_CURE_POISON) {
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else if (itemId == 40507) {
							pc.getInventory().removeItem(l1iteminstance, 1);
						}

						pc.curePoison();
					}
				}
				else if ((itemId == L1ItemId.POTION_OF_HASTE_SELF) || (itemId == L1ItemId.B_POTION_OF_HASTE_SELF
						// 自我加速藥水、受祝福的 自我加速藥水
						) || (itemId == 40018 // 強力 自我加速藥水
						) || (itemId == 140018 // 受祝福的 強力 自我加速藥水
						) || (itemId == 40039 // 紅酒
						) || (itemId == 40040 // 威士忌
						) || (itemId == 40030 // 象牙塔加速藥水
						) || (itemId == 41338 // 受祝福的葡萄酒
						) || (itemId == 41261 // 飯團
						) || (itemId == 41262 // 雞肉串燒
						) || (itemId == 41268 // 小比薩
						) || (itemId == 41269 // 烤玉米
						) || (itemId == 41271 // 爆米花
						) || (itemId == 41272 // 甜不辣
						) || (itemId == 41273 // 鬆餅
						) || (itemId == 41342 // 梅杜莎之血
						) || (itemId == 49302 // 福利加速藥水
						) || (itemId == 49140 // 綠茶蛋糕卷
						)) {
					Potion.useGreenPotion(pc, l1iteminstance, itemId);
				}
				else if ((itemId == L1ItemId.POTION_OF_EMOTION_BRAVERY) // 勇敢藥水
						|| (itemId == L1ItemId.B_POTION_OF_EMOTION_BRAVERY) // 受祝福的 勇敢藥水
						|| (itemId == L1ItemId.POTION_OF_REINFORCED_CASE) // 強化勇氣的藥水
						|| (itemId == L1ItemId.W_POTION_OF_EMOTION_BRAVERY)) { // 福利勇敢藥水
					if (pc.isKnight()) { // 騎士
						Potion.Brave(pc, l1iteminstance, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
				else if (itemId == L1ItemId.FORBIDDEN_FRUIT) { // 生命之樹果實
					if (pc.isDragonKnight() || pc.isIllusionist()) { // 龍騎士、幻術師
						Potion.Brave(pc, l1iteminstance, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
				else if ((itemId == L1ItemId.ELVEN_WAFER) // 精靈餅乾
						|| (itemId == L1ItemId.B_ELVEN_WAFER) // 受祝福的 精靈餅乾
						|| (itemId == L1ItemId.W_POTION_OF_FOREST)) { // 福利森林藥水
					if (pc.isElf()) { // 妖精
						Potion.Brave(pc, l1iteminstance, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
				else if (itemId == L1ItemId.DEVILS_BLOOD) { // 惡魔之血
					if (pc.isCrown()) { // 王族
						Potion.Brave(pc, l1iteminstance, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
				else if (itemId == L1ItemId.COIN_OF_REPUTATION) { // 名譽貨幣
					if (!pc.isDragonKnight() && !pc.isIllusionist()) { // 龍騎士與幻術師無法使用
						Potion.Brave(pc, l1iteminstance, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
				else if (itemId == L1ItemId.CHOCOLATE_CAKE) { // 巧克力蛋糕
					Potion.ThirdSpeed(pc, l1iteminstance, 600);
				}
				else if ((itemId >= L1ItemId.POTION_OF_EXP_150)
						&& (itemId <= L1ItemId.SCROLL_FOR_ENCHANTING_BATTLE)) { // 150%神力藥水 ~ 強化戰鬥卷軸
					Effect.useEffectItem(pc, l1iteminstance);
				}
				else if ((itemId == 40066) || (itemId == 41413)) { // 年糕、月餅
					Potion.UseMpPotion(pc, l1iteminstance, 7, 6);
				}
				else if ((itemId == 40067) || (itemId == 41414)) { // 艾草年糕、福月餅
					Potion.UseMpPotion(pc, l1iteminstance, 15, 16);
				}
				else if (itemId == 40735) { // 勇氣貨幣
					Potion.UseMpPotion(pc, l1iteminstance, 60, 0);
				}
				else if ((itemId == 40042) || (itemId == 41142)) { // 精神藥水、神秘的魔力藥水
					Potion.UseMpPotion(pc, l1iteminstance, 50, 0);
				}
				else if (itemId == 41404) { // 庫傑的靈藥
					Potion.UseMpPotion(pc, l1iteminstance, 80, 21);
				}
				else if (itemId == 41412) { // 金粽子
					Potion.UseMpPotion(pc, l1iteminstance, 5, 16);
				}
				else if ((itemId == 40032) || (itemId == 40041) // 伊娃的祝福、人魚之鱗
						|| (itemId == 41344) || (itemId == 49303)) { // 水中的水、福利呼吸藥水
					Potion.useBlessOfEva(pc, l1iteminstance, itemId);
				}
				else if ((itemId == L1ItemId.POTION_OF_MANA) // 藍色藥水
						|| (itemId == L1ItemId.B_POTION_OF_MANA // 受祝福的 藍色藥水
						|| (itemId == 40736) || (itemId == 49306))) { // 智慧貨幣、福利藍色藥水
					Potion.useBluePotion(pc, l1iteminstance, itemId);
				}
				else if ((itemId == L1ItemId.POTION_OF_EMOTION_WISDOM) // 慎重藥水
						|| (itemId == L1ItemId.B_POTION_OF_EMOTION_WISDOM) // 受祝福的 慎重藥水
						|| (itemId == 49307)) { // 福利慎重藥水
					if (pc.isWizard()) { // 法師
						Potion.useWisdomPotion(pc, l1iteminstance, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
				else if (itemId == L1ItemId.POTION_OF_BLINDNESS) { // 黑色藥水
					Potion.useBlindPotion(pc, l1iteminstance);
				}
				else if ((itemId == 40088) || (itemId == 40096)
						|| (itemId == 49308) || (itemId == 140088)) { // 變形卷軸、福利變形藥水
					if (usePolyScroll(pc, itemId, s)) {
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(181)); // \f1無法變成你指定的怪物。
					}
				}
				else if ((itemId == 41154 // 暗之鱗
						) || (itemId == 41155 // 火之鱗
						) || (itemId == 41156 // 叛之鱗
						) || (itemId == 41157 // 恨之鱗
						) || (itemId == 49220)) { // 妖魔密使變形卷軸
					usePolyScale(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if ((itemId == 41143 // 海賊骷髏首領變身藥水
						) || (itemId == 41144 // 海賊骷髏士兵變身藥水
						) || (itemId == 41145 // 海賊骷髏刀手變身藥水
						) || (itemId == 49149 // 夏納的變身卷軸(等級30)
						) || (itemId == 49150 // 夏納的變身卷軸(等級40)
						) || (itemId == 49151 // 夏納的變身卷軸(等級52)
						) || (itemId == 49152 // 夏納的變身卷軸(等級55)
						) || (itemId == 49153 // 夏納的變身卷軸(等級60)
						) || (itemId == 49154 // 夏納的變身卷軸(等級65)
						) || (itemId == 49155 // 夏納的變身卷軸(等級70)
						) || (itemId == 49139 // 起司蛋糕
						)) {
					usePolyPotion(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 40317) { // 砥石
					// 武器か防具の場合のみ
					if ((l1iteminstance1.getItem().getType2() != 0) && (l1iteminstance1.get_durability() > 0)) {
						String msg0;
						pc.getInventory().recoveryDamage(l1iteminstance1);
						msg0 = l1iteminstance1.getLogName();
						if (l1iteminstance1.get_durability() == 0) {
							pc.sendPackets(new S_ServerMessage(464, msg0)); // %0%sは新品同様の状態になりました。
						}
						else {
							pc.sendPackets(new S_ServerMessage(463, msg0)); // %0の状態が良くなりました。
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId >= 47017 && itemId <= 47023) { // 龍之魔眼
					Effect.useEffectItem(pc, l1iteminstance);
				}
				else if (itemId == 47041) { // 地龍之精緻魔眼、水龍之精緻魔眼
					if (l1iteminstance1.getItem().getItemId() == 47042) { // 水龍之精緻魔眼
						pc.getInventory().consumeItem(47041, 1);
						pc.getInventory().consumeItem(47042, 1);
						createNewItem(pc, 47021, 1); // 誕生之魔眼
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 47042) { // 水龍之精緻魔眼
					if (l1iteminstance1.getItem().getItemId() == 47041) { // 地龍之精緻魔眼、水龍之精緻魔眼
						pc.getInventory().consumeItem(47041, 1);
						pc.getInventory().consumeItem(47042, 1);
						createNewItem(pc, 47021, 1); // 誕生之魔眼
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 47043) { // 風龍之精緻魔眼
					if (l1iteminstance1.getItem().getItemId() == 47045) { // 誕生之精緻魔眼
						pc.getInventory().consumeItem(47043, 1);
						pc.getInventory().consumeItem(47045, 1);
						createNewItem(pc, 47022, 1); // 形象之魔眼
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 47045) { // 誕生之精緻魔眼
					if (l1iteminstance1.getItem().getItemId() == 47043) { // 風龍之精緻魔眼
						pc.getInventory().consumeItem(47043, 1);
						pc.getInventory().consumeItem(47045, 1);
						createNewItem(pc, 47022, 1); // 形象之魔眼
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 47044) { // 火龍之精緻魔眼
					if (l1iteminstance1.getItem().getItemId() == 47046) { // 形象之精緻魔眼
						pc.getInventory().consumeItem(47044, 1);
						pc.getInventory().consumeItem(47046, 1);
						createNewItem(pc, 47023, 1); // 生命之魔眼
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 47046) { // 形象之精緻魔眼
					if (l1iteminstance1.getItem().getItemId() == 47044) { // 火龍之精緻魔眼
						pc.getInventory().consumeItem(47044, 1);
						pc.getInventory().consumeItem(47046, 1);
						createNewItem(pc, 47023, 1); // 生命之魔眼
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId >= 47049 && itemId <= 47052) { // XX附魔轉換卷軸
					if (l1iteminstance1.getItemId() >= 47053
							&& l1iteminstance1.getItemId() <= 47062) { // 附魔石 ~ 9階附魔石
						int type = (itemId - 47048) * 10; // type = 10,20,30,40
						int rnd = Random.nextInt(100) + 1;
						if (Config.MAGIC_STONE_TYPE < rnd) {
							int newItem = l1iteminstance1.getItemId() + type; // 附魔石(近戰) ~ 9階附魔石(近戰)
							L1Item template = ItemTable.getInstance().getTemplate(newItem);
							if (template == null) {
								pc.sendPackets(new S_ServerMessage(79));
							}
							createNewItem(pc, newItem, 1); // 獲得附魔石(XX)
						} else {
							pc.sendPackets(new S_ServerMessage(1411, l1iteminstance1.getName())); // 對\f1%0附加魔法失敗。
						}
						pc.getInventory().removeItem(l1iteminstance1, 1); // 刪除 - 附魔石
						pc.getInventory().removeItem(l1iteminstance, 1); // 刪除 - 附魔轉換卷軸
					} else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if (itemId == 47048) { // 附魔強化卷軸
					int item_id = l1iteminstance1.getItemId();
					if ((item_id < 47053) || (item_id > 47102)
							|| (item_id == 47062) || (item_id == 47072)
							|| (item_id == 47082) || (item_id == 47092)
							|| (item_id == 47102)) {
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}

					int rnd = Random.nextInt(100) + 1;
					if (Config.MAGIC_STONE_LEVEL < rnd
							|| (item_id >= 47053 && item_id <= 47056)
							|| (item_id >= 47063 && item_id <= 47066)
							|| (item_id >= 47073 && item_id <= 47076)
							|| (item_id >= 47083 && item_id <= 47086)
							|| (item_id >= 47093 && item_id <= 47096)) {
						int newItem = l1iteminstance1.getItemId() + 1; // X 階附魔石 -> X+1 階附魔石
						L1Item template = ItemTable.getInstance().getTemplate(newItem);
						if (template == null) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
						pc.sendPackets(new S_ServerMessage(1410, l1iteminstance1.getName())); // 對\f1%0附加強大的魔法力量成功。

						l1iteminstance1.setItem(template);
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_ITEMID);
						pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_ITEMID);
					} else {
						pc.sendPackets(new S_ServerMessage(1411, l1iteminstance1.getName())); // 對\f1%0附加魔法失敗。
						pc.getInventory().removeItem(l1iteminstance1, 1);
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if ((itemId >= 47064 && itemId <= 47067) || (itemId >= 47074 && itemId <= 47077)
						|| (itemId >= 47084 && itemId <= 47087) || (itemId >= 47094 && itemId <= 47097)) { // 1 ~ 4 階附魔石(近戰)(遠攻)(恢復)(防禦)
					if (pc.getInventory().consumeItem(41246, 30)) {
						Effect.useEffectItem(pc, l1iteminstance);
					} else {
						isDelayEffect = false;
						pc.sendPackets(new S_ServerMessage(337, "$5240"));
					}
				}
				else if ((itemId == 47068) || (itemId == 47069) || (itemId == 47078) || (itemId == 47079)
						 || (itemId == 47088) || (itemId == 47089) || (itemId == 47098) || (itemId == 47099)) { // 5 ~ 6階附魔石(近戰)(遠攻)(恢復)(防禦)
					if (pc.getInventory().consumeItem(41246, 60)) {
						Effect.useEffectItem(pc, l1iteminstance);
					} else {
						isDelayEffect = false;
						pc.sendPackets(new S_ServerMessage(337, "$5240"));
					}
				}
				else if ((itemId == 47070) || (itemId == 47080) || (itemId == 47090) || (itemId == 47100)) { // 7階附魔石(近戰)(遠攻)(恢復)(防禦)
					if (pc.getInventory().consumeItem(41246, 100)) {
						Effect.useEffectItem(pc, l1iteminstance);
					} else {
						isDelayEffect = false;
						pc.sendPackets(new S_ServerMessage(337, "$5240"));
					}
				}
				else if ((itemId == 47071) || (itemId == 47081) || (itemId == 47091) || (itemId == 47101)) { // 8階附魔石(近戰)(遠攻)(恢復)(防禦)
					if (pc.getInventory().consumeItem(41246, 200)) {
						Effect.useEffectItem(pc, l1iteminstance);
					} else {
						isDelayEffect = false;
						pc.sendPackets(new S_ServerMessage(337, "$5240"));
					}
				}
				else if ((itemId == 47072) || (itemId == 47082) || (itemId == 47092) || (itemId == 47102)) { // 9階附魔石(近戰)(遠攻)(恢復)(防禦)
					if (pc.getInventory().consumeItem(41246, 300)) {
						Effect.useEffectItem(pc, l1iteminstance);
					} else {
						isDelayEffect = false;
						pc.sendPackets(new S_ServerMessage(337, "$5240"));
					}
				}
				else if ((itemId == 40097) || (itemId == 40119)
						|| (itemId == 140119) || (itemId == 140329)) { // 解除咀咒的卷軸、原住民圖騰
					for (L1ItemInstance eachItem : pc.getInventory().getItems()) {
						if ((eachItem.getItem().getBless() != 2)
								&& (eachItem.getItem().getBless() != 130)) {
							continue;
						}
						if (!eachItem.isEquipped()
								&& ((itemId == 40119) || (itemId == 40097))) {
							// 裝備中才可解除咀咒
							continue;
						}
						int id_normal = eachItem.getItemId() - 200000;
						L1Item template = ItemTable.getInstance().getTemplate(id_normal);
						if (template == null) {
							continue;
						}
						if (eachItem.getBless() == 130) { // 封印中的咀咒裝備
							eachItem.setBless(129);
						} else { // 未封印的咀咒裝備
							eachItem.setBless(1);
						}
						if (pc.getInventory().checkItem(id_normal) && template.isStackable()) {
							pc.getInventory().storeItem(id_normal, eachItem.getCount());
							pc.getInventory().removeItem(eachItem, eachItem.getCount());
						}
						else {
							eachItem.setItem(template);
							pc.getInventory().updateItem(eachItem, L1PcInventory.COL_ITEMID);
							pc.getInventory().saveItem(eachItem, L1PcInventory.COL_ITEMID);
						}
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(new S_ServerMessage(155)); // \f1誰かが助けてくれたようです。
				}
				else if ((itemId == 40126) || (itemId == 40098)) { // 確認スクロール
					if (!l1iteminstance1.isIdentified()) {
						l1iteminstance1.setIdentified(true);
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
					}
					pc.sendPackets(new S_IdentifyDesc(l1iteminstance1));
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 41036) { // 糊
					int diaryId = l1iteminstance1.getItem().getItemId();
					if ((diaryId >= 41038) && (41047 >= diaryId)) {
						if ((Random.nextInt(99) + 1) <= Config.CREATE_CHANCE_DIARY) {
							createNewItem(pc, diaryId + 10, 1);
						}
						else {
							pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName())); // \f1%0が蒸発してなくなりました。
						}
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if ((itemId >= 41048) && (41055 >= itemId)) {
					// 糊付けされた航海日誌ページ：１～８ページ
					int logbookId = l1iteminstance1.getItem().getItemId();
					if (logbookId == (itemId + 8034)) {
						createNewItem(pc, logbookId + 2, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if ((itemId == 41056) || (itemId == 41057)) {
					// 糊付けされた航海日誌ページ：９，１０ページ
					int logbookId = l1iteminstance1.getItem().getItemId();
					if (logbookId == (itemId + 8034)) {
						createNewItem(pc, 41058, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40925) { // 浄化のポーション
					int earingId = l1iteminstance1.getItem().getItemId();
					if ((earingId >= 40987) && (40989 >= earingId)) { // 呪われたブラックイアリング
						if (Random.nextInt(100) < Config.CREATE_CHANCE_RECOLLECTION) {
							createNewItem(pc, earingId + 186, 1);
						}
						else {
							pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName())); // \f1%0が蒸発してなくなりました。
						}
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if ((itemId >= 40926) && (40929 >= itemId)) { // 一～四階神秘藥水
					int earing2Id = l1iteminstance1.getItem().getItemId();
					int potion1 = 0;
					int potion2 = 0;
					if ((earing2Id >= 41173) && (41184 >= earing2Id)) {
						if (itemId == 40926) {
							potion1 = 247;
							potion2 = 249;
						} else if (itemId == 40927) {
							potion1 = 249;
							potion2 = 251;
						} else if (itemId == 40928) {
							potion1 = 251;
							potion2 = 253;
						} else if (itemId == 40929) {
							potion1 = 253;
							potion2 = 255;
						}
						if ((earing2Id >= (itemId + potion1)) && ((itemId + potion2) >= earing2Id)) {
							if ((Random.nextInt(99) + 1) < Config.CREATE_CHANCE_MYSTERIOUS) {
								createNewItem(pc, (earing2Id - 12), 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(160, l1iteminstance1.getName()));
								// \f1%0%s %2 產生激烈的 %1 光芒，但是沒有任何事情發生。
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
						}
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if ((itemId >= 40931) && (40942 >= itemId)) { // 精工的藍、綠、紅寶石
					int earing3Id = l1iteminstance1.getItem().getItemId();
					if ((earing3Id >= 41161) && (41172 >= earing3Id)) {
						// ミステリアスイアリング類
						if (earing3Id == (itemId + 230)) {
							if ((Random.nextInt(99) + 1) < Config.CREATE_CHANCE_PROCESSING) {
								int[] earingId = { 41161, 41162, 41163, 41164, 41165, 41166, 41167, 41168, 41169, 41170, 41171, 41172 };
								int[] earinglevel = { 21014, 21006, 21007, 21015, 21009, 21008, 21016, 21012, 21010, 21017, 21013, 21011 };
								for (int i = 0; i < earingId.length; i++) {
									if (earing3Id == earingId[i]) {
										createNewItem(pc, earinglevel[i], 1);
										break;
									}
								}
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName())); // \f1%0%s 消失。
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
						}
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if ((itemId >= 40943) && (40958 >= itemId)) { // 精工的土、水、火、風之鑽
					int ringId = l1iteminstance1.getItem().getItemId();
					int ringlevel = 0;
					int gmas = 0;
					int gmam = 0;
					if ((ringId >= 41185) && (41200 >= ringId)) {
						// 細工されたリング類
						if ((itemId == 40943) || (itemId == 40947) || (itemId == 40951) || (itemId == 40955)) {
							gmas = 443;
							gmam = 447;
						} else if ((itemId == 40944) || (itemId == 40948) || (itemId == 40952) || (itemId == 40956)) {
							gmas = 442;
							gmam = 446;
						} else if ((itemId == 40945) || (itemId == 40949) || (itemId == 40953) || (itemId == 40957)) {
							gmas = 441;
							gmam = 445;
						} else if ((itemId == 40946) || (itemId == 40950) || (itemId == 40954) || (itemId == 40958)) {
							gmas = 444;
							gmam = 448;
						}
						if (ringId == (itemId + 242)) {
							if ((Random.nextInt(99) + 1) < Config.CREATE_CHANCE_PROCESSING_DIAMOND) {
								ringlevel = 20435 + (ringId - 41185);
								pc.sendPackets(new S_ServerMessage(gmas, l1iteminstance1.getName()));
								createNewItem(pc, ringlevel, 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(gmam, l1iteminstance.getName()));
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
						}
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 41029) { // 召喚球の欠片
					int dantesId = l1iteminstance1.getItem().getItemId();
					if ((dantesId >= 41030) && (41034 >= dantesId)) { // 召喚球のコア・各段階
						if ((Random.nextInt(99) + 1) < Config.CREATE_CHANCE_DANTES) {
							createNewItem(pc, dantesId + 1, 1);
						}
						else {
							pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName())); // \f1%0が蒸発してなくなりました。
						}
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40964) { // ダークマジックパウダー
					int historybookId = l1iteminstance1.getItem().getItemId();
					if ((historybookId >= 41011) && (41018 >= historybookId)) {
						if ((Random.nextInt(99) + 1) <= Config.CREATE_CHANCE_HISTORY_BOOK) {
							createNewItem(pc, historybookId + 8, 1);
						}
						else {
							pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName())); // \f1%0が蒸発してなくなりました。
						}
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if ((itemId == 40090) || (itemId == 40091) || (itemId == 40092) || (itemId == 40093) || (itemId == 40094)) { // ブランク
																																	// スクロール(Lv1)～ブランク
					// スクロール(Lv5)
					if (pc.isWizard()) { // ウィザード
						if (((itemId == 40090) && (blanksc_skillid <= 7)) || // ブランク
								(// スクロール(Lv1)でレベル1以下の魔法
								(itemId == 40091) && (blanksc_skillid <= 15)) || // ブランク
								(// スクロール(Lv2)でレベル2以下の魔法
								(itemId == 40092) && (blanksc_skillid <= 22)) || // ブランク
								(// スクロール(Lv3)でレベル3以下の魔法
								(itemId == 40093) && (blanksc_skillid <= 31)) || // ブランク
								(// スクロール(Lv4)でレベル4以下の魔法
								(itemId == 40094) && (blanksc_skillid <= 39))) { // ブランク
							// スクロール(Lv5)でレベル5以下の魔法
							L1ItemInstance spellsc = ItemTable.getInstance().createItem(40859 + blanksc_skillid);
							if (spellsc != null) {
								if (pc.getInventory().checkAddItem(spellsc, 1) == L1Inventory.OK) {
									L1Skills l1skills = SkillsTable.getInstance().getTemplate(blanksc_skillid + 1); // blanksc_skillidは0始まり
									if (pc.getCurrentHp() + 1 < l1skills.getHpConsume() + 1) {
										pc.sendPackets(new S_ServerMessage(279)); // \f1HPが不足していて魔法を使うことができません。
										return;
									}
									if (pc.getCurrentMp() < l1skills.getMpConsume()) {
										pc.sendPackets(new S_ServerMessage(278)); // \f1MPが不足していて魔法を使うことができません。
										return;
									}
									if (l1skills.getItemConsumeId() != 0) { // 材料が必要
										if (!pc.getInventory().checkItem(l1skills.getItemConsumeId(), l1skills.getItemConsumeCount())) { // 必要材料をチェック
											pc.sendPackets(new S_ServerMessage(299)); // \f1魔法を詠唱するための材料が足りません。
											return;
										}
									}
									pc.setCurrentHp(pc.getCurrentHp() - l1skills.getHpConsume());
									pc.setCurrentMp(pc.getCurrentMp() - l1skills.getMpConsume());
									int lawful = pc.getLawful() + l1skills.getLawful();
									if (lawful > 32767) {
										lawful = 32767;
									}
									if (lawful < -32767) {
										lawful = -32767;
									}
									pc.setLawful(lawful);
									if (l1skills.getItemConsumeId() != 0) { // 材料が必要
										pc.getInventory().consumeItem(l1skills.getItemConsumeId(), l1skills.getItemConsumeCount());
									}
									pc.getInventory().removeItem(l1iteminstance, 1);
									pc.getInventory().storeItem(spellsc);
								}
							}
						}
						else {
							pc.sendPackets(new S_ServerMessage(591)); // \f1スクロールがそんな強い魔法を記録するにはあまりに弱いです。
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(264)); // \f1あなたのクラスではこのアイテムは使用できません。
					}

					// スペルスクロール
				}
				else if ((((itemId >= 40859) && (itemId <= 40898)) && (itemId != 40863)) || ((itemId >= 49281) && (itemId <= 49286))) { // 40863はテレポートスクロールとして処理される
					if ((spellsc_objid == pc.getId()) && (l1iteminstance.getItem().getUseType() != 30)) { // spell_buff
						pc.sendPackets(new S_ServerMessage(281)); // \f1魔法が無効になりました。
						return;
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					if ((spellsc_objid == 0) && (l1iteminstance.getItem().getUseType() != 0) && (l1iteminstance.getItem().getUseType() != 26)
							&& (l1iteminstance.getItem().getUseType() != 27)) {
						return;
						// ターゲットがいない場合にhandleCommandsを送るとぬるぽになるためここでreturn
						// handleCommandsのほうで判断＆処理すべき部分かもしれない
					}
					int skillid = itemId - 40858;
					if (itemId == 49281) { // フィジカルエンチャント：STR
						skillid = 42;
					}
					else if (itemId == 49282) { // ブレスウェポン
						skillid = 48;
					}
					else if (itemId == 49283) { // ヒールオール
						skillid = 49;
					}
					else if (itemId == 49284) { // ホーリーウォーク(未実装)
						skillid = 52;
						return;
					}
					else if (itemId == 49285) { // グレーターヘイスト
						skillid = 54;
					}
					else if (itemId == 49286) { // フルヒール
						skillid = 57;
					}
					L1SkillUse l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(client.getActiveChar(), skillid, spellsc_objid, spellsc_x, spellsc_y, null, 0, L1SkillUse.TYPE_SPELLSC);

				}
				else if (((itemId >= 40373) && (itemId <= 40382 // 地図各種
						))
						|| ((itemId >= 40385) && (itemId <= 40390))) {
					pc.sendPackets(new S_UseMap(pc, l1iteminstance.getId(), l1iteminstance.getItem().getItemId()));
				}
				else if ((itemId == 40310) || (itemId == 40730) || (itemId == 40731) || (itemId == 40732)) { // 便箋(未使用)
					if (writeLetter(itemId, pc, letterCode, letterReceiver, letterText)) {
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
				else if (itemId == 40311) { // 血盟便箋(未使用)
					if (writeClanLetter(itemId, pc, letterCode, letterReceiver, letterText)) {
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
				else if ((itemId == 49016) || (itemId == 49018) || (itemId == 49020) || (itemId == 49022) || (itemId == 49024)) { // 便箋(未開封)
					pc.sendPackets(new S_Letter(l1iteminstance));
					l1iteminstance.setItemId(itemId + 1);
					pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_ITEMID);
					pc.getInventory().saveItem(l1iteminstance, L1PcInventory.COL_ITEMID);
				}
				else if ((itemId == 49017) || (itemId == 49019) || (itemId == 49021) || (itemId == 49023) || (itemId == 49025)) { // 便箋(開封済み)
					pc.sendPackets(new S_Letter(l1iteminstance));
				}
				else if ((itemId == 40314) || (itemId == 40316)) { // ペットのアミュレット
					if (pc.getInventory().checkItem(41160)) { // 召喚の笛
						if (withdrawPet(pc, itemObjid)) {
							pc.getInventory().consumeItem(41160, 1);
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40315) { // ペットの笛
					pc.sendPackets(new S_Sound(437));
					pc.broadcastPacket(new S_Sound(437));
					Object[] petList = pc.getPetList().values().toArray();
					for (Object petObject : petList) {
						if (petObject instanceof L1PetInstance) { // ペット
							L1PetInstance pet = (L1PetInstance) petObject;
							pet.call();
						}
					}
				}
				else if (itemId == 40493) { // マジックフルート
					pc.sendPackets(new S_Sound(165));
					pc.broadcastPacket(new S_Sound(165));
					for (L1Object visible : pc.getKnownObjects()) {
						if (visible instanceof L1GuardianInstance) {
							L1GuardianInstance guardian = (L1GuardianInstance) visible;
							if (guardian.getNpcTemplate().get_npcId() == 70850) { // パン
								if (createNewItem(pc, 88, 1)) {
									pc.getInventory().removeItem(l1iteminstance, 1);
								}
							}
						}
					}
				}
				else if (itemId == 40325) { // 2面コイン
					if (pc.getInventory().checkItem(40318, 1)) {
						int gfxid = 3237 + Random.nextInt(2);
						pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
						pc.getInventory().consumeItem(40318, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40326) { // 3方向ルーレット
					if (pc.getInventory().checkItem(40318, 1)) {
						int gfxid = 3229 + Random.nextInt(3);
						pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
						pc.getInventory().consumeItem(40318, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40327) { // 4方向ルーレット
					if (pc.getInventory().checkItem(40318, 1)) {
						int gfxid = 3241 + Random.nextInt(4);
						pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
						pc.getInventory().consumeItem(40318, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40328) { // 6面ダイス
					if (pc.getInventory().checkItem(40318, 1)) {
						int gfxid = 3204 + Random.nextInt(6);
						pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
						pc.getInventory().consumeItem(40318, 1);
					}
					else {
						// \f1何も起きませんでした。
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if ((itemId == 40089) || (itemId == 140089)) { // 復活スクロール、祝福された復活スクロール
					L1Character resobject = (L1Character) L1World.getInstance().findObject(resid);
					if (resobject != null) {
						if (resobject instanceof L1PcInstance) {
							L1PcInstance target = (L1PcInstance) resobject;
							if (pc.getId() == target.getId()) {
								return;
							}
							if (L1World.getInstance().getVisiblePlayer(target, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(target, 0)) {
									if (!visiblePc.isDead()) {
										// \f1その場所に他の人が立っているので復活させることができません。
										pc.sendPackets(new S_ServerMessage(592));
										return;
									}
								}
							}
							if ((target.getCurrentHp() == 0) && (target.isDead() == true)) {
								if (pc.getMap().isUseResurrection()) {
									target.setTempID(pc.getId());
									if (itemId == 40089) {
										// また復活したいですか？（Y/N）
										target.sendPackets(new S_Message_YN(321, ""));
									}
									else if (itemId == 140089) {
										// また復活したいですか？（Y/N）
										target.sendPackets(new S_Message_YN(322, ""));
									}
								}
								else {
									return;
								}
							}
						}
						else if (resobject instanceof L1NpcInstance) {
							if (!(resobject instanceof L1TowerInstance)) {
								L1NpcInstance npc = (L1NpcInstance) resobject;
								if (npc.getNpcTemplate().isCantResurrect() && !(npc instanceof L1PetInstance)) {
									pc.getInventory().removeItem(l1iteminstance, 1);
									return;
								}
								if ((npc instanceof L1PetInstance) && (L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0)) {
									for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
										if (!visiblePc.isDead()) {
											// \f1その場所に他の人が立っているので復活させることができません。
											pc.sendPackets(new S_ServerMessage(592));
											return;
										}
									}
								}
								if ((npc.getCurrentHp() == 0) && npc.isDead()) {
									npc.resurrect(npc.getMaxHp() / 4);
									npc.setResurrect(true);
									if ((npc instanceof L1PetInstance)) {
										L1PetInstance pet = (L1PetInstance) npc;
										// 開始飽食度計時
										pet.startFoodTimer(pet);
									}
								}
							}
						}
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (((itemId > 40169) && (itemId < 40226)) || ((itemId >= 45000) && (itemId <= 45022))) { // 魔法書
					useSpellBook(pc, l1iteminstance, itemId);
				}
				else if ((itemId > 40225) && (itemId < 40232)) {
					if (pc.isCrown() || pc.isGm()) {
						if ((itemId == 40226) && (pc.getLevel() >= 15)) {
							SpellBook4(pc, l1iteminstance, client);
						}
						else if ((itemId == 40228) && (pc.getLevel() >= 30)) {
							SpellBook4(pc, l1iteminstance, client);
						}
						else if ((itemId == 40227) && (pc.getLevel() >= 40)) {
							SpellBook4(pc, l1iteminstance, client);
						}
						else if (((itemId == 40231) || (itemId == 40232)) && (pc.getLevel() >= 45)) {
							SpellBook4(pc, l1iteminstance, client);
						}
						else if ((itemId == 40230) && (pc.getLevel() >= 50)) {
							SpellBook4(pc, l1iteminstance, client);
						}
						else if ((itemId == 40229) && (pc.getLevel() >= 55)) {
							SpellBook4(pc, l1iteminstance, client);
						}
						else {
							pc.sendPackets(new S_ServerMessage(312)); // LVが低くて
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if (((itemId >= 40232) && (itemId <= 40264 // 精霊の水晶
						))
						|| ((itemId >= 41149) && (itemId <= 41153))) {
					useElfSpellBook(pc, l1iteminstance, itemId);
				}
				else if ((itemId > 40264) && (itemId < 40280)) { // 闇精霊の水晶
					if (pc.isDarkelf() || pc.isGm()) {
						if ((itemId >= 40265) && (itemId <= 40269) && (pc.getLevel() >= 15)) {
							SpellBook1(pc, l1iteminstance, client);
						}
						else if ((itemId >= 40270) && (itemId <= 40274) && (pc.getLevel() >= 30)) {
							SpellBook1(pc, l1iteminstance, client);
						}
						else if ((itemId >= 40275) && (itemId <= 40279) && (pc.getLevel() >= 45)) {
							SpellBook1(pc, l1iteminstance, client);
						}
						else {
							pc.sendPackets(new S_ServerMessage(312));
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // (原文:闇精霊の水晶はダークエルフのみが習得できます。)
					}
				}
				else if (((itemId >= 40164) && (itemId <= 40166 // 技術書
						))
						|| ((itemId >= 41147) && (itemId <= 41148))) {
					if (pc.isKnight() || pc.isGm()) {
						if ((itemId >= 40164) && (itemId <= 40165 // スタン、リダクションアーマー
								) && (pc.getLevel() >= 50)) {
							SpellBook3(pc, l1iteminstance, client);
						}
						else if ((itemId >= 41147) && (itemId <= 41148 // ソリッドキャリッジ、カウンターバリア
								) && (pc.getLevel() >= 50)) {
							SpellBook3(pc, l1iteminstance, client);
						}
						else if ((itemId == 40166) && (pc.getLevel() >= 60)) { // バウンスアタック
							SpellBook3(pc, l1iteminstance, client);
						}
						else {
							pc.sendPackets(new S_ServerMessage(312));
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if ((itemId >= 49102) && (itemId <= 49116)) { // ドラゴンナイトの書板
					if (pc.isDragonKnight() || pc.isGm()) {
						if ((itemId >= 49102) && (itemId <= 49106 // ドラゴンナイト秘技LV1
								) && (pc.getLevel() >= 15)) {
							SpellBook5(pc, l1iteminstance, client);
						}
						else if ((itemId >= 49107) && (itemId <= 49111 // ドラゴンナイト秘技LV2
								) && (pc.getLevel() >= 30)) {
							SpellBook5(pc, l1iteminstance, client);
						}
						else if ((itemId >= 49112) && (itemId <= 49116 // ドラゴンナイト秘技LV3
								) && (pc.getLevel() >= 45)) {
							SpellBook5(pc, l1iteminstance, client);
						}
						else {
							pc.sendPackets(new S_ServerMessage(312));
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if ((itemId >= 49117) && (itemId <= 49136)) { // 記憶の水晶
					if (pc.isIllusionist() || pc.isGm()) {
						if ((itemId >= 49117) && (itemId <= 49121 // イリュージョニスト魔法LV1
								) && (pc.getLevel() >= 10)) {
							SpellBook6(pc, l1iteminstance, client);
						}
						else if ((itemId >= 49122) && (itemId <= 49126 // イリュージョニスト魔法LV2
								) && (pc.getLevel() >= 20)) {
							SpellBook6(pc, l1iteminstance, client);
						}
						else if ((itemId >= 49127) && (itemId <= 49131 // イリュージョニスト魔法LV3
								) && (pc.getLevel() >= 30)) {
							SpellBook6(pc, l1iteminstance, client);
						}
						else if ((itemId >= 49132) && (itemId <= 49136 // イリュージョニスト魔法LV4
								) && (pc.getLevel() >= 40)) {
							SpellBook6(pc, l1iteminstance, client);
						}
						else {
							pc.sendPackets(new S_ServerMessage(312));
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if ((itemId == 40079) || (itemId == 40095) || (itemId == 40521)) { // 傳送回家的卷軸、象牙塔傳送回家的卷軸、精靈羽翼
					if (pc.getMap().isEscapable() || pc.isGm()) {
						int[] loc = Getback.GetBack_Location(pc, true);
						L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(276)); // \f1在此無法使用傳送。
					}
				}
				else if (itemId == 40124) { // 血盟帰還スクロール
					if (pc.getMap().isEscapable() || pc.isGm()) {
						int castle_id = 0;
						int house_id = 0;
						if (pc.getClanid() != 0) { // クラン所属
							L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
							if (clan != null) {
								castle_id = clan.getCastleId();
								house_id = clan.getHouseId();
							}
						}
						if (castle_id != 0) { // 城主クラン員
							if (pc.getMap().isEscapable() || pc.isGm()) {
								int[] loc = new int[3];
								loc = L1CastleLocation.getCastleLoc(castle_id);
								int locx = loc[0];
								int locy = loc[1];
								short mapid = (short) (loc[2]);
								L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
							else {
								pc.sendPackets(new S_ServerMessage(647));
							}
						}
						else if (house_id != 0) { // アジト所有クラン員
							if (pc.getMap().isEscapable() || pc.isGm()) {
								int[] loc = new int[3];
								loc = L1HouseLocation.getHouseLoc(house_id);
								int locx = loc[0];
								int locy = loc[1];
								short mapid = (short) (loc[2]);
								L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
							else {
								pc.sendPackets(new S_ServerMessage(647));
							}
						}
						else {
							if (pc.getHomeTownId() > 0) {
								int[] loc = L1TownLocation.getGetBackLoc(pc.getHomeTownId());
								int locx = loc[0];
								int locy = loc[1];
								short mapid = (short) (loc[2]);
								L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
							else {
								int[] loc = Getback.GetBack_Location(pc, true);
								L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(647));
					}
				}
				else if ((itemId == 140100) || (itemId == 40100) || (itemId == 40099 // 祝福されたテレポートスクロール、テレポートスクロール
						) || (itemId == 40086) || (itemId == 40863)) { // スペルスクロール(テレポート)
					L1BookMark bookm = pc.getBookMark(btele);
					if (bookm != null) { // ブックマークを取得出来たらテレポート
						if (pc.getMap().isEscapable() || pc.isGm()) {
							int newX = bookm.getLocX();
							int newY = bookm.getLocY();
							short mapId = bookm.getMapId();

							if (itemId == 40086) { // マステレポートスクロール
								for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc)) {
									if ((pc.getLocation().getTileLineDistance(member.getLocation()) <= 3) && (member.getClanid() == pc.getClanid())
											&& (pc.getClanid() != 0) && (member.getId() != pc.getId())) {
										L1Teleport.teleport(member, newX, newY, mapId, 5, true);
									}
								}
							}
							L1Teleport.teleport(pc, newX, newY, mapId, 5, true);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
						else {
							L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), false);
							pc.sendPackets(new S_ServerMessage(79));
						}
					}
					else {
						if (pc.getMap().isTeleportable() || pc.isGm()) {
							L1Location newLocation = pc.getLocation().randomLocation(200, true);
							int newX = newLocation.getX();
							int newY = newLocation.getY();
							short mapId = (short) newLocation.getMapId();

							if (itemId == 40086) { // マステレポートスクロール
								for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc)) {
									if ((pc.getLocation().getTileLineDistance(member.getLocation()) <= 3) && (member.getClanid() == pc.getClanid())
											&& (pc.getClanid() != 0) && (member.getId() != pc.getId())) {
										L1Teleport.teleport(member, newX, newY, mapId, 5, true);
									}
								}
							}
							L1Teleport.teleport(pc, newX, newY, mapId, 5, true);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
						else {
							L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), false);
							pc.sendPackets(new S_ServerMessage(276));
						}
					}
				}
				else if (itemId == 240100) { // 呪われたテレポートスクロール(オリジナルアイテム)
					L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), true);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if ((itemId >= 40901) && (itemId <= 40908)) { // 各種エンゲージリング
					L1PcInstance partner = null;
					boolean partner_stat = false;
					if (pc.getPartnerId() != 0) { // 結婚中
						partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
						if ((partner != null) && (partner.getPartnerId() != 0) && (pc.getPartnerId() == partner.getId())
								&& (partner.getPartnerId() == pc.getId())) {
							partner_stat = true;
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(662)); // \f1あなたは結婚していません。
						return;
					}

					if (partner_stat) {
						boolean castle_area = L1CastleLocation.checkInAllWarArea(
						// いずれかの城エリア
								partner.getX(), partner.getY(), partner.getMapId());
						if (((partner.getMapId() == 0) || (partner.getMapId() == 4) || (partner.getMapId() == 304)) && (castle_area == false)) {
							L1Teleport.teleport(pc, partner.getX(), partner.getY(), partner.getMapId(), 5, true);
						}
						else {
							pc.sendPackets(new S_ServerMessage(547)); // \f1あなたのパートナーは今あなたが行けない所でプレイ中です。
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(546)); // \f1あなたのパートナーは今プレイをしていません。
					}
				}
				else if (itemId == 40555) { // 秘密の部屋のキー
					if (pc.isKnight() && ((pc.getX() >= 32806) && // オリム部屋
							(pc.getX() <= 32814)) && ((pc.getY() >= 32798) && (pc.getY() <= 32807)) && (pc.getMapId() == 13)) {
						short mapid = 13;
						L1Teleport.teleport(pc, 32815, 32810, mapid, 5, false);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40417) { // ソウルクリスタル
					if (((pc.getX() >= 32665) && // 海賊島
							(pc.getX() <= 32674))
							&& ((pc.getY() >= 32976) && (pc.getY() <= 32985)) && (pc.getMapId() == 440)) {
						short mapid = 430;
						L1Teleport.teleport(pc, 32922, 32812, mapid, 5, true);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40566) { // ミステリアス シェル
					if (pc.isElf()
							&& ((pc.getX() >= 33971) && // 象牙の塔の村の南にある魔方陣の座標
							(pc.getX() <= 33975)) && ((pc.getY() >= 32324) && (pc.getY() <= 32328)) && (pc.getMapId() == 4)
							&& !pc.getInventory().checkItem(40548)) { // 亡霊の袋
						boolean found = false;
						for (L1Object obj : L1World.getInstance().getObject()) {
							if (obj instanceof L1MonsterInstance) {
								L1MonsterInstance mob = (L1MonsterInstance) obj;
								if (mob != null) {
									if (mob.getNpcTemplate().get_npcId() == 45300) {
										found = true;
										break;
									}
								}
							}
						}
						if (found) {
							pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
						}
						else {
							L1SpawnUtil.spawn(pc, 45300, 0, 0); // 古代人の亡霊
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40557) { // 暗殺リスト(グルーディン)
					if ((pc.getX() == 32620) && (pc.getY() == 32641) && (pc.getMapId() == 4)) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45883) {
									pc.sendPackets(new S_ServerMessage(79));
									return;
								}
							}
						}
						L1SpawnUtil.spawn(pc, 45883, 0, 300000);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40563) { // 暗殺リスト(火田村)
					if ((pc.getX() == 32730) && (pc.getY() == 32426) && (pc.getMapId() == 4)) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45884) {
									pc.sendPackets(new S_ServerMessage(79));
									return;
								}
							}
						}
						L1SpawnUtil.spawn(pc, 45884, 0, 300000);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40561) { // 暗殺リスト(ケント)
					if ((pc.getX() == 33046) && (pc.getY() == 32806) && (pc.getMapId() == 4)) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45885) {
									pc.sendPackets(new S_ServerMessage(79));
									return;
								}
							}
						}
						L1SpawnUtil.spawn(pc, 45885, 0, 300000);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40560) { // 暗殺リスト(ウッドベック)
					if ((pc.getX() == 32580) && (pc.getY() == 33260) && (pc.getMapId() == 4)) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45886) {
									pc.sendPackets(new S_ServerMessage(79));
									return;
								}
							}
						}
						L1SpawnUtil.spawn(pc, 45886, 0, 300000);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40562) { // 暗殺リスト(ハイネ)
					if ((pc.getX() == 33447) && (pc.getY() == 33476) && (pc.getMapId() == 4)) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45887) {
									pc.sendPackets(new S_ServerMessage(79));
									return;
								}
							}
						}
						L1SpawnUtil.spawn(pc, 45887, 0, 300000);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40559) { // 暗殺リスト(アデン)
					if ((pc.getX() == 34215) && (pc.getY() == 33195) && (pc.getMapId() == 4)) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45888) {
									pc.sendPackets(new S_ServerMessage(79));
									return;
								}
							}
						}
						L1SpawnUtil.spawn(pc, 45888, 0, 300000);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40558) { // 暗殺リスト(ギラン)
					if ((pc.getX() == 33513) && (pc.getY() == 32890) && (pc.getMapId() == 4)) {
						for (L1Object object : L1World.getInstance().getObject()) {
							if (object instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (npc.getNpcTemplate().get_npcId() == 45889) {
									pc.sendPackets(new S_ServerMessage(79));
									return;
								}
							}
						}
						L1SpawnUtil.spawn(pc, 45889, 0, 300000);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 40572) { // アサシンの印
					if ((pc.getX() == 32778) && (pc.getY() == 32738) && (pc.getMapId() == 21)) {
						L1Teleport.teleport(pc, 32781, 32728, (short) 21, 5, true);
					}
					else if ((pc.getX() == 32781) && (pc.getY() == 32728) && (pc.getMapId() == 21)) {
						L1Teleport.teleport(pc, 32778, 32738, (short) 21, 5, true);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if ((itemId == 40006) || (itemId == 40412) || (itemId == 140006)) { // 松木魔杖、黑暗安特的樹枝
					if (pc.getMap().isUsePainwand()) {
						S_AttackPacket s_attackPacket = new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand);
						pc.sendPackets(s_attackPacket);
						pc.broadcastPacket(s_attackPacket);
						int[] mobArray =
						{ 45008, 45140, 45016, 45021, 45025, 45033, 45099, 45147, 45123, 45130, 45046, 45092, 45138, 45098, 45127, 45143, 45149,
								45171, 45040, 45155, 45192, 45173, 45213, 45079, 45144 };
						// ゴブリン・ホブコブリン・コボルト・鹿・グレムリン
						// インプ・インプエルダー・オウルベア・スケルトンアーチャー・スケルトンアックス
						// ビーグル・ドワーフウォーリアー・オークスカウト・ガンジオーク・ロバオーク
						// ドゥダーマラオーク・アトゥバオーク・ネルガオーク・ベアー・トロッグ
						// ラットマン・ライカンスロープ・ガースト・ノール・リザードマン
						/*
						 * 45005, 45008, 45009, 45016, 45019, 45043, 45060,
						 * 45066, 45068, 45082, 45093, 45101, 45107, 45126,
						 * 45129, 45136, 45144, 45157, 45161, 45173, 45184,
						 * 45223 }; // カエル、ゴブリン、オーク、コボルド、 // オーク
						 * アーチャー、ウルフ、スライム、ゾンビ、 // フローティングアイ、オーク ファイター、 // ウェア
						 * ウルフ、アリゲーター、スケルトン、 // ストーン ゴーレム、スケルトン アーチャー、 // ジャイアント
						 * スパイダー、リザードマン、グール、 // スパルトイ、ライカンスロープ、ドレッド スパイダー、 //
						 * バグベアー
						 */
						int rnd = Random.nextInt(mobArray.length);
						L1SpawnUtil.spawn(pc, mobArray[rnd], 0, 300000);
						if ((itemId == 40006) || (itemId == 140006)) {
							l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
							pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
							if (l1iteminstance.getChargeCount() <= 0) { // 次數為 0時刪除
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						}
						else {
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
					else {
						// \f1何も起きませんでした。
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if (itemId == 40007) { // 閃電魔杖
					int dmg = 0;
					int[] data = null;
					L1Object target = L1World.getInstance().findObject(spellsc_objid);
					if (target != null) {
						dmg = doWandAction(pc, target);
					}
					data = new int[] {ActionCodes.ACTION_Wand, dmg, 10, 6}; // data = {actid, dmg, spellgfx, use_type}
					pc.sendPackets(new S_UseAttackSkill(pc, spellsc_objid, spellsc_x, spellsc_y, data));
					pc.broadcastPacket(new S_UseAttackSkill(pc, spellsc_objid, spellsc_x, spellsc_y, data));
					l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
					pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
					if (l1iteminstance.getChargeCount() <= 0) { // 次數為 0時刪除
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
				else if ((itemId == 40008) || (itemId == 40410) || (itemId == 140008)) { // 楓木魔杖、黑暗安特的樹皮
					if ((pc.getMapId() == 63) || (pc.getMapId() == 552) || (pc.getMapId() == 555) || (pc.getMapId() == 557) || (pc.getMapId() == 558)
							|| (pc.getMapId() == 779)) { // 水中では使用不可
						pc.sendPackets(new S_ServerMessage(563)); // \f1ここでは使えません。
					}
					else {
						S_AttackPacket s_attackPacket = new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand);
						pc.sendPackets(s_attackPacket);
						pc.broadcastPacket(s_attackPacket);
						L1Object target = L1World.getInstance().findObject(spellsc_objid);
						if (target != null) {
							L1Character cha = (L1Character) target;
							polyAction(pc, cha);
							if ((itemId == 40008) || (itemId == 140008)) {
								l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
								pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
								if (l1iteminstance.getChargeCount() <= 0) { // 次數為 0時刪除
									pc.getInventory().removeItem(l1iteminstance, 1);
								}
							}
							else {
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						}
						else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
						}
					}
				}
				else if ((itemId >= 40289) && (itemId <= 40297)) { // 傲慢の塔11~91階テレポートアミュレット
					useToiTeleportAmulet(pc, itemId, l1iteminstance);
				}
				else if ((itemId >= 40280) && (itemId <= 40288)) {
					// 封印された傲慢の塔11～91階テレポートアミュレット
					pc.getInventory().removeItem(l1iteminstance, 1);
					L1ItemInstance item = pc.getInventory().storeItem(itemId + 9, 1);
					if (item != null) {
						pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
					}
					// 肉類
				}
				else if ((itemId == 40056) || (itemId == 40057) || (itemId == 40059) || (itemId == 40060) || (itemId == 40061) || (itemId == 40062)
						|| (itemId == 40063) || (itemId == 40064) || (itemId == 40065) || (itemId == 40069) || (itemId == 40072) || (itemId == 40073)
						|| (itemId == 140061) || (itemId == 140062) || (itemId == 140065) || (itemId == 140069) || (itemId == 140072)
						|| (itemId == 41296) || (itemId == 41297) || (itemId == 41266) || (itemId == 41267) || (itemId == 41274) || (itemId == 41275)
						|| (itemId == 41276) || (itemId == 41252) || (itemId == 49040) || (itemId == 49041) || (itemId == 49042) || (itemId == 49043)
						|| (itemId == 49044) || (itemId == 49045) || (itemId == 49046) || (itemId == 49047)) {
					pc.getInventory().removeItem(l1iteminstance, 1);
					// XXX 食べ物毎の満腹度(100単位で変動)
					short foodvolume1 = (short) (l1iteminstance.getItem().getFoodVolume() / 10);
					short foodvolume2 = 0;
					if (foodvolume1 <= 0) {
						foodvolume1 = 5;
					}
					if (pc.get_food() >= 225) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, (short) pc.get_food()));
					}
					else {
						foodvolume2 = (short) (pc.get_food() + foodvolume1);
						if (foodvolume2 <= 225) {
							pc.set_food(foodvolume2);
							pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, (short) pc.get_food()));
						}
						else {
							pc.set_food((short) 225);
							pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, (short) pc.get_food()));
						}
					}
					if (itemId == 40057) { // フローティングアイ肉
						pc.setSkillEffect(STATUS_FLOATING_EYE, 0);
					}
					pc.sendPackets(new S_ServerMessage(76, l1iteminstance.getItem().getIdentifiedNameId()));
				}
				else if (itemId == 40070) { // 進化果實
					pc.sendPackets(new S_ServerMessage(76, l1iteminstance.getLogName()));
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 41298) { // 鱈魚
					Potion.UseHeallingPotion(pc, l1iteminstance, 4, 189);
				}
				else if (itemId == 41299) { // 虎斑帶魚
					Potion.UseHeallingPotion(pc, l1iteminstance, 15, 194);
				}
				else if (itemId == 41300) { // 鮪魚
					Potion.UseHeallingPotion(pc, l1iteminstance, 35, 197);
				}
				else if ((itemId >= 40136) && (itemId <= 40161)) { // 煙火
					int soundid = 3198;
					if (itemId == 40154) {
						soundid = 3198;
					}
					else if (itemId == 40152) {
						soundid = 2031;
					}
					else if (itemId == 40141) {
						soundid = 2028;
					}
					else if (itemId == 40160) {
						soundid = 2030;
					}
					else if (itemId == 40145) {
						soundid = 2029;
					}
					else if (itemId == 40159) {
						soundid = 2033;
					}
					else if (itemId == 40151) {
						soundid = 2032;
					}
					else if (itemId == 40161) {
						soundid = 2037;
					}
					else if (itemId == 40142) {
						soundid = 2036;
					}
					else if (itemId == 40146) {
						soundid = 2039;
					}
					else if (itemId == 40148) {
						soundid = 2043;
					}
					else if (itemId == 40143) {
						soundid = 2041;
					}
					else if (itemId == 40156) {
						soundid = 2042;
					}
					else if (itemId == 40139) {
						soundid = 2040;
					}
					else if (itemId == 40137) {
						soundid = 2047;
					}
					else if (itemId == 40136) {
						soundid = 2046;
					}
					else if (itemId == 40138) {
						soundid = 2048;
					}
					else if (itemId == 40140) {
						soundid = 2051;
					}
					else if (itemId == 40144) {
						soundid = 2053;
					}
					else if (itemId == 40147) {
						soundid = 2045;
					}
					else if (itemId == 40149) {
						soundid = 2034;
					}
					else if (itemId == 40150) {
						soundid = 2055;
					}
					else if (itemId == 40153) {
						soundid = 2038;
					}
					else if (itemId == 40155) {
						soundid = 2044;
					}
					else if (itemId == 40157) {
						soundid = 2035;
					}
					else if (itemId == 40158) {
						soundid = 2049;
					}
					else {
						soundid = 3198;
					}

					S_SkillSound s_skillsound = new S_SkillSound(pc.getId(), soundid);
					pc.sendPackets(s_skillsound);
					pc.broadcastPacket(s_skillsound);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if ((itemId >= 41357) && (itemId <= 41382)) { // アルファベット花火
					int soundid = itemId - 34946;
					S_SkillSound s_skillsound = new S_SkillSound(pc.getId(), soundid);
					pc.sendPackets(s_skillsound);
					pc.broadcastPacket(s_skillsound);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 40615) { // 影の神殿2階の鍵
					if (((pc.getX() >= 32701) && (pc.getX() <= 32705)) && ((pc.getY() >= 32894) && (pc.getY() <= 32898)) && (pc.getMapId() == 522)) { // 影の神殿1F
						L1Teleport.teleport(pc, ((L1EtcItem) l1iteminstance.getItem()).get_locx(), ((L1EtcItem) l1iteminstance.getItem()).get_locy(),
								((L1EtcItem) l1iteminstance.getItem()).get_mapid(), 5, true);
					}
					else {
						// \f1何も起きませんでした。
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if ((itemId == 40616) || (itemId == 40782) || (itemId == 40783)) { // 影の神殿3階の鍵
					if (((pc.getX() >= 32698) && (pc.getX() <= 32702)) && ((pc.getY() >= 32894) && (pc.getY() <= 32898)) && (pc.getMapId() == 523)) { // 影の神殿2階
						L1Teleport.teleport(pc, ((L1EtcItem) l1iteminstance.getItem()).get_locx(), ((L1EtcItem) l1iteminstance.getItem()).get_locy(),
								((L1EtcItem) l1iteminstance.getItem()).get_mapid(), 5, true);
					}
					else {
						// \f1何も起きませんでした。
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if (itemId == 40692) { // 完成された宝の地図
					if (pc.getInventory().checkItem(40621)) {
						// \f1何も起きませんでした。
						pc.sendPackets(new S_ServerMessage(79));
					}
					else if (((pc.getX() >= 32856) && (pc.getX() <= 32858)) && ((pc.getY() >= 32857) && (pc.getY() <= 32858))
							&& (pc.getMapId() == 443)) { // 海賊島のダンジョン３階
						L1Teleport.teleport(pc, ((L1EtcItem) l1iteminstance.getItem()).get_locx(), ((L1EtcItem) l1iteminstance.getItem()).get_locy(),
								((L1EtcItem) l1iteminstance.getItem()).get_mapid(), 5, true);
					}
					else {
						// \f1何も起きませんでした。
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if (itemId == 41146) { // ドロモンドの招待状
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei001"));
				}
				else if (itemId == 40641) { // トーキングスクロール
					if (Config.ALT_TALKINGSCROLLQUEST == true) {
						if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 0) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolla"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 1) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollb"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 2) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollc"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 3) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolld"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 4) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolle"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 5) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollf"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 6) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollg"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 7) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollh"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 8) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolli"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 9) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollj"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 10) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollk"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 11) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolll"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 12) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollm"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 13) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolln"));
						}
						else if (pc.getQuest().get_step(L1Quest.QUEST_TOSCROLL) == 255) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollo"));
						}
					}
					else {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollp"));
					}
				}
				else if (itemId == 40383) { // 地図：歌う島
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei035"));
				}
				else if (itemId == 40384) { // 地図：隠された渓谷
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei036"));
				}
				else if (itemId == 40101) { // 隠された渓谷帰還スクロール
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei037"));
				}
				else if (itemId == 41209) { // ポピレアの依頼書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei002"));
				}
				else if (itemId == 41210) { // 研磨材
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei003"));
				}
				else if (itemId == 41211) { // ハーブ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei004"));
				}
				else if (itemId == 41212) { // 特製キャンディー
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei005"));
				}
				else if (itemId == 41213) { // ティミーのバスケット
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei006"));
				}
				else if (itemId == 41214) { // 運の証
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei012"));
				}
				else if (itemId == 41215) { // 知の証
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei010"));
				}
				else if (itemId == 41216) { // 力の証
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei011"));
				}
				else if (itemId == 41222) { // マシュル
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei008"));
				}
				else if (itemId == 41223) { // 武具の破片
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei007"));
				}
				else if (itemId == 41224) { // バッジ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei009"));
				}
				else if (itemId == 41225) { // ケスキンの発注書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei013"));
				}
				else if (itemId == 41226) { // パゴの薬
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei014"));
				}
				else if (itemId == 41227) { // アレックスの紹介状
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei033"));
				}
				else if (itemId == 41228) { // ラビのお守り
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei034"));
				}
				else if (itemId == 41229) { // スケルトンの頭
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei025"));
				}
				else if (itemId == 41230) { // ジーナンへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei020"));
				}
				else if (itemId == 41231) { // マッティへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei021"));
				}
				else if (itemId == 41233) { // ケーイへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei019"));
				}
				else if (itemId == 41234) { // 骨の入った袋
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei023"));
				}
				else if (itemId == 41235) { // 材料表
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei024"));
				}
				else if (itemId == 41236) { // ボーンアーチャーの骨
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei026"));
				}
				else if (itemId == 41237) { // スケルトンスパイクの骨
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei027"));
				}
				else if (itemId == 41239) { // ヴートへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei018"));
				}
				else if (itemId == 41240) { // フェーダへの手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei022"));
				}
				else if (itemId == 41060) { // ノナメの推薦書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "nonames"));
				}
				else if (itemId == 41061) { // 調査団の証書：エルフ地域ドゥダ-マラカメ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kames"));
				}
				else if (itemId == 41062) { // 調査団の証書：人間地域ネルガバクモ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bakumos"));
				}
				else if (itemId == 41063) { // 調査団の証書：精霊地域ドゥダ-マラブカ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bukas"));
				}
				else if (itemId == 41064) { // 調査団の証書：オーク地域ネルガフウモ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "huwoomos"));
				}
				else if (itemId == 41065) { // 調査団の証書：調査団長アトゥバノア
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "noas"));
				}
				else if (itemId == 41356) { // パルームの資源リスト
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rparum3"));
				}
				else if (itemId == 40701) { // 小さな宝の地図
					if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 1) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "firsttmap"));
					}
					else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 2) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapa"));
					}
					else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 3) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapb"));
					}
					else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 4) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapc"));
					}
					else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 5) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapd"));
					}
					else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 6) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmape"));
					}
					else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 7) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapf"));
					}
					else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 8) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapg"));
					}
					else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 9) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmaph"));
					}
					else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 10) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapi"));
					}
				}
				else if (itemId == 40663) { // 息子の手紙
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "sonsletter"));
				}
				else if (itemId == 40630) { // ディエゴの古い日記
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "diegodiary"));
				}
				else if (itemId == 41340) { // 傭兵団長 ティオンの紹介状
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tion"));
				}
				else if (itemId == 41317) { // ラルソンの推薦状
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rarson"));
				}
				else if (itemId == 41318) { // クエンのメモ
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kuen"));
				}
				else if (itemId == 41329) { // 剥製の製作依頼書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "anirequest"));
				}
				else if (itemId == 41346) { // ロビンフッドのメモ1
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinscroll"));
				}
				else if (itemId == 41347) { // ロビンフッドのメモ2
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinscroll2"));
				}
				else if (itemId == 41348) { // ロビンフッドの紹介状
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinhood"));
				}
				else if (itemId == 41007) { // イリスの命令書：霊魂の安息
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll"));
				}
				else if (itemId == 41009) { // イリスの命令書：同盟の意志
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll2"));
				}
				else if (itemId == 41019) { // ラスタバド歴史書１章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory1"));
				}
				else if (itemId == 41020) { // ラスタバド歴史書２章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory2"));
				}
				else if (itemId == 41021) { // ラスタバド歴史書３章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory3"));
				}
				else if (itemId == 41022) { // ラスタバド歴史書４章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory4"));
				}
				else if (itemId == 41023) { // ラスタバド歴史書５章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory5"));
				}
				else if (itemId == 41024) { // ラスタバド歴史書６章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory6"));
				}
				else if (itemId == 41025) { // ラスタバド歴史書７章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory7"));
				}
				else if (itemId == 41026) { // ラスタバド歴史書８章
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory8"));
				}
				else if (itemId == 41208) { // 散りゆく魂
					if (((pc.getX() >= 32844) && (pc.getX() <= 32845)) && ((pc.getY() >= 32693) && (pc.getY() <= 32694)) && (pc.getMapId() == 550)) { // 船の墓場:地上層
						L1Teleport.teleport(pc, ((L1EtcItem) l1iteminstance.getItem()).get_locx(), ((L1EtcItem) l1iteminstance.getItem()).get_locy(),
								((L1EtcItem) l1iteminstance.getItem()).get_mapid(), 5, true);
					}
					else {
						// \f1何も起きませんでした。
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if (itemId == 40700) { // シルバーフルート
					pc.sendPackets(new S_Sound(10));
					pc.broadcastPacket(new S_Sound(10));
					if (((pc.getX() >= 32619) && (pc.getX() <= 32623)) && ((pc.getY() >= 33120) && (pc.getY() <= 33124)) && (pc.getMapId() == 440)) { // 海賊島前半魔方陣座標
						boolean found = false;
						for (L1Object obj : L1World.getInstance().getObject()) {
							if (obj instanceof L1MonsterInstance) {
								L1MonsterInstance mob = (L1MonsterInstance) obj;
								if (mob != null) {
									if (mob.getNpcTemplate().get_npcId() == 45875) {
										found = true;
										break;
									}
								}
							}
						}
						if (found) {}
						else {
							L1SpawnUtil.spawn(pc, 45875, 0, 0); // ラバーボーンヘッド
						}
					}
				}
				else if (itemId == 41121) { // カヘルの契約書
					if ((pc.getQuest().get_step(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END) || pc.getInventory().checkItem(41122, 1)) {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
					else {
						createNewItem(pc, 41122, 1);
					}
				}
				else if (itemId == 41130) { // 血痕の契約書
					if ((pc.getQuest().get_step(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END) || pc.getInventory().checkItem(41131, 1)) {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
					else {
						createNewItem(pc, 41131, 1);
					}
				}
				else if (itemId == 42501) { // ストームウォーク
					if (pc.getCurrentMp() < 10) {
						pc.sendPackets(new S_ServerMessage(278)); // \f1MPが不足していて魔法を使うことができません。
						return;
					}
					pc.setCurrentMp(pc.getCurrentMp() - 10);
					// pc.sendPackets(new S_CantMove()); // テレポート後に移動不可能になる場合がある
					L1Teleport.teleport(pc, spellsc_x, spellsc_y, pc.getMapId(), pc.getHeading(), true, L1Teleport.CHANGE_POSITION);
				}
				else if ((itemId == 41293)) { // 魔法釣竿
					startFishing(pc, itemId, fishX, fishY);
				}
				else if (itemId == 41245) { // 溶解剤
					useResolvent(pc, l1iteminstance1, l1iteminstance);
				}
				else if ((itemId >= 41255) && (itemId <= 41259)) { // 料理の本
					if (cookStatus == 0) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.COOK_WINDOW, (itemId - 41255)));
					}
					else {
						makeCooking(pc, cookNo);
					}
				}
				else if (itemId == 41260) { // 薪
					for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 3)) {
						if (object instanceof L1EffectInstance) {
							if (((L1NpcInstance) object).getNpcTemplate().get_npcId() == 81170) {
								// すでに周囲に焚き火があります。
								pc.sendPackets(new S_ServerMessage(1162));
								return;
							}
						}
					}
					int[] loc = new int[2];
					loc = pc.getFrontLoc();
					L1EffectSpawn.getInstance().spawnEffect(81170, 600000, loc[0], loc[1], pc.getMapId());
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (((itemId >= 41277) && (itemId <= 41292)) || ((itemId >= 49049) && (itemId <= 49064))
						|| ((itemId >= 49244) && (itemId <= 49259))
						|| itemId == L1ItemId.POTION_OF_WONDER_DRUG) { // 魔法料理、象牙塔妙藥
					L1Cooking.useCookingItem(pc, l1iteminstance);
				}
				else if ((itemId >= 41383) && (itemId <= 41400)) { // 家具
					useFurnitureItem(pc, itemId, itemObjid);
				}
				else if (itemId == 41401) { // 家具除去ワンド
					useFurnitureRemovalWand(pc, spellsc_objid, l1iteminstance);
				}
				else if (itemId == 41411) { // 銀のチョンズ
					Potion.UseHeallingPotion(pc, l1iteminstance, 10, 189);
				}
				else if (itemId == 41345) { // 酸性の乳液
					L1DamagePoison.doInfection(pc, pc, 3000, 5);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 41315) { // 聖水
					if (pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
						return;
					}
					if (pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
						pc.removeSkillEffect(STATUS_HOLY_MITHRIL_POWDER);
					}
					pc.setSkillEffect(STATUS_HOLY_WATER, 900 * 1000);
					pc.sendPackets(new S_SkillSound(pc.getId(), 190));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
					pc.sendPackets(new S_ServerMessage(1141));
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 41316) { // 神聖なミスリル パウダー
					if (pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
						return;
					}
					if (pc.hasSkillEffect(STATUS_HOLY_WATER)) {
						pc.removeSkillEffect(STATUS_HOLY_WATER);
					}
					pc.setSkillEffect(STATUS_HOLY_MITHRIL_POWDER, 900 * 1000);
					pc.sendPackets(new S_SkillSound(pc.getId(), 190));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
					pc.sendPackets(new S_ServerMessage(1142));
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 41354) { // 神聖なエヴァの水
					if (pc.hasSkillEffect(STATUS_HOLY_WATER) || pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
						return;
					}
					pc.setSkillEffect(STATUS_HOLY_WATER_OF_EVA, 900 * 1000);
					pc.sendPackets(new S_SkillSound(pc.getId(), 190));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
					pc.sendPackets(new S_ServerMessage(1140));
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 49092) { // 龜裂之核
					int targetItemId = l1iteminstance1.getItem().getItemId();
					// 上鎖的歐西里斯初級寶箱、上鎖的歐西里斯高級寶箱
					if ((targetItemId == 49095) || (targetItemId == 49099)
							// 上鎖的庫庫爾坎初級寶箱、上鎖的庫庫爾坎高級寶箱
							|| (targetItemId == 49318) || (targetItemId == 49322)) {
						createNewItem(pc, targetItemId + 1, 1);
						pc.getInventory().consumeItem(targetItemId, 1);
						pc.getInventory().consumeItem(49092, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 49094) { // 歐西里斯初級寶箱碎片(下)
					if (l1iteminstance1.getItem().getItemId() == 49093) {
						pc.getInventory().consumeItem(49093, 1);
						pc.getInventory().consumeItem(49094, 1);
						createNewItem(pc, 49095, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 49098) { // 歐西里斯高級寶箱碎片(下)
					if (l1iteminstance1.getItem().getItemId() == 49097) {
						pc.getInventory().consumeItem(49097, 1);
						pc.getInventory().consumeItem(49098, 1);
						createNewItem(pc, 49099, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 49317) { // 庫庫爾坎初級寶箱碎片：下
					if (l1iteminstance1.getItem().getItemId() == 49316) {
						pc.getInventory().consumeItem(49316, 1);
						pc.getInventory().consumeItem(49317, 1);
						createNewItem(pc, 49318, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				else if (itemId == 49321) { // 庫庫爾坎高級寶箱碎片(下)
					if (l1iteminstance1.getItem().getItemId() == 49320) {
						pc.getInventory().consumeItem(49320, 1);
						pc.getInventory().consumeItem(49321, 1);
						createNewItem(pc, 49322, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1沒有任何事情發生。
					}
				}
				/** 飾品強化卷軸 Scroll of Enchant Accessory */
				else if (itemId == 49148) {
					if (!pc.getInventory().checkItem(itemId, 1)) { // 身上沒有飾品強化卷軸
						return;
					}
					if (l1iteminstance1 == null) { // 目標飾品為空值
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}
					if (l1iteminstance1.getBless() >= 128) { // 封印中
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}
					if (l1iteminstance1.getItem().getType2() != 2
							|| l1iteminstance1.getItem().getType() < 8
							|| l1iteminstance1.getItem().getType() > 12
							|| l1iteminstance1.getItem().getGrade() == -1) { // 非防具、非飾品類、無飾品等級
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}
					int enchant_level = l1iteminstance1.getEnchantLevel();

					if (enchant_level < 0 || enchant_level >= 10) { // 強化上限 + 10
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}

					int rnd = Random.nextInt(100) + 1;
					int enchant_chance_accessory;
					int enchant_level_tmp = enchant_level;
					// +6 時獎勵效果判斷
					boolean award = false;
					// 成功率： +0-70% ~ +9-25%
					enchant_chance_accessory = (50 + enchant_level_tmp) / (enchant_level_tmp + 1) + 20;

					if (rnd < enchant_chance_accessory) { // 成功
						if (l1iteminstance1.getEnchantLevel() == 5) {
							award = true;
						}
						switch (l1iteminstance1.getItem().getGrade()) {
							case 0: // 上等
								// 四屬性 +1
								l1iteminstance1.setFireMr(l1iteminstance1.getFireMr() + 1);
								l1iteminstance1.setWaterMr(l1iteminstance1.getWaterMr() + 1);
								l1iteminstance1.setEarthMr(l1iteminstance1.getEarthMr() + 1);
								l1iteminstance1.setWindMr(l1iteminstance1.getWindMr() + 1);
								// LV6 額外獎勵：體力與魔力回復量 +1
								if (award) {
									l1iteminstance1.setHpr(l1iteminstance1.getHpr() + 1);
									l1iteminstance1.setMpr(l1iteminstance1.getMpr() + 1);
								}
								// 裝備中
								if (l1iteminstance1.isEquipped()) {
									pc.addFire(1);
									pc.addWater(1);
									pc.addEarth(1);
									pc.addWind(1);
								}
								break;
							case 1: // 中等
								// HP +2
								l1iteminstance1.setaddHp(l1iteminstance1.getaddHp() + 2);
								// LV6 額外獎勵：魔防 +1
								if (award) {
									l1iteminstance1.setM_Def(l1iteminstance1.getM_Def() + 1);
								}
								// 裝備中
								if (l1iteminstance1.isEquipped()) {
									pc.addMaxHp(2);
									if (award) {
										pc.addMr(1);
										pc.sendPackets(new S_SPMR(pc));
									}
								}
								break;
							case 2: // 初等
								// MP +1
								l1iteminstance1.setaddMp(l1iteminstance1.getaddMp() + 1);
								// LV6 額外獎勵：魔攻 +1
								if (award) {
									l1iteminstance1.setaddSp(l1iteminstance1.getaddSp() + 1);
								}
								// 裝備中
								if (l1iteminstance1.isEquipped()) {
									pc.addMaxMp(1);
									if (award) {
										pc.addSp(1);
										pc.sendPackets(new S_SPMR(pc));
									}
								}
								break;
							case 3: // 特等
								// 功能台版未實裝。
								break;
							default:
								pc.sendPackets(new S_ServerMessage(79));
								return;
						}
					} else { // 飾品強化失敗
						FailureEnchant(pc, l1iteminstance1, client);
						pc.getInventory().removeItem(l1iteminstance, 1);
						return;
					}
					SuccessEnchant(pc, l1iteminstance1, client, 1);
					pc.sendPackets(new S_ItemStatus(l1iteminstance1));
					// 儲存道具
					CharactersItemStorage storage = CharactersItemStorage.create();
					// 更新 character_items
					storage.updateFireMr(l1iteminstance1);
					storage.updateWaterMr(l1iteminstance1);
					storage.updateEarthMr(l1iteminstance1);
					storage.updateWindMr(l1iteminstance1);
					storage.updateaddSp(l1iteminstance1);
					storage.updateaddHp(l1iteminstance1);
					storage.updateaddMp(l1iteminstance1);
					storage.updateHpr(l1iteminstance1);
					storage.updateMpr(l1iteminstance1);
					storage.updateM_Def(l1iteminstance1);
					// 刪除
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 41426) { // 封印スクロール
					L1ItemInstance lockItem = pc.getInventory().getItem(l);
					if (((lockItem != null) && (lockItem.getItem().getType2() == 1)) || (lockItem.getItem().getType2() == 2)
							|| ((lockItem.getItem().getType2() == 0) && lockItem.getItem().isCanSeal())) {
						if ((lockItem.getBless() == 0) || (lockItem.getBless() == 1) || (lockItem.getBless() == 2) || (lockItem.getBless() == 3)) {
							int bless = 1;
							switch (lockItem.getBless()) {
								case 0:
									bless = 128;
									break;
								case 1:
									bless = 129;
									break;
								case 2:
									bless = 130;
									break;
								case 3:
									bless = 131;
									break;
							}
							lockItem.setBless(bless);
							pc.getInventory().updateItem(lockItem, L1PcInventory.COL_BLESS);
							pc.getInventory().saveItem(lockItem, L1PcInventory.COL_BLESS);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
						else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 41427) { // 封印解除スクロール
					L1ItemInstance lockItem = pc.getInventory().getItem(l);
					if (((lockItem != null) && (lockItem.getItem().getType2() == 1)) || (lockItem.getItem().getType2() == 2)
							|| ((lockItem.getItem().getType2() == 0) && lockItem.getItem().isCanSeal())) {
						if ((lockItem.getBless() == 128) || (lockItem.getBless() == 129) || (lockItem.getBless() == 130)
								|| (lockItem.getBless() == 131)) {
							int bless = 1;
							switch (lockItem.getBless()) {
								case 128:
									bless = 0;
									break;
								case 129:
									bless = 1;
									break;
								case 130:
									bless = 2;
									break;
								case 131:
									bless = 3;
									break;
							}
							lockItem.setBless(bless);
							pc.getInventory().updateItem(lockItem, L1PcInventory.COL_BLESS);
							pc.getInventory().saveItem(lockItem, L1PcInventory.COL_BLESS);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
						else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 41428) { // 太古の玉爾
					if ((pc != null) && (l1iteminstance != null)) {
						Account account = Account.load(pc.getAccountName());
						if (account == null) {
							pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
							return;
						}
						int characterSlot = account.getCharacterSlot();
						int maxAmount = Config.DEFAULT_CHARACTER_SLOT + characterSlot;
						if (maxAmount >= 8) {
							pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
							return;
						}
						if (characterSlot < 0) {
							characterSlot = 0;
						}
						else {
							characterSlot += 1;
						}
						account.setCharacterSlot(characterSlot);
						Account.updateCharacterSlot(account);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
				else if (itemId == 40075) { // 防具破壊スクロール
					if (l1iteminstance1.getItem().getType2() == 2) {
						int msg = 0;
						switch (l1iteminstance1.getItem().getType()) {
							case 1: // helm
								msg = 171; // \f1ヘルムが塵になり、風に飛んでいきます。
								break;
							case 2: // armor
								msg = 169; // \f1アーマーが壊れ、下に落ちました。
								break;
							case 3: // T
								msg = 170; // \f1シャツが細かい糸になり、破けて落ちました。
								break;
							case 4: // cloak
								msg = 168; // \f1マントが破れ、塵になりました。
								break;
							case 5: // glove
								msg = 172; // \f1グローブが消えました。
								break;
							case 6: // boots
								msg = 173; // \f1靴がバラバラになりました。
								break;
							case 7: // shield
								msg = 174; // \f1シールドが壊れました。
								break;
							default:
								msg = 167; // \f1肌がムズムズします。
								break;
						}
						pc.sendPackets(new S_ServerMessage(msg));
						pc.getInventory().removeItem(l1iteminstance1, 1);
					}
					else {
						pc.sendPackets(new S_ServerMessage(154)); // \f1スクロールが散らばります。
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
				else if (itemId == 49210) { // プロケルの1番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "first_p"));
				}
				else if (itemId == 49211) { // プロケルの2番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "second_p"));
				}
				else if (itemId == 49212) { // プロケルの3番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "third_p"));
				}
				else if (itemId == 49287) { // プロケルの4番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "fourth_p"));
				}
				else if (itemId == 49288) { // プロケルの5番目の指令書
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "fifth_p"));
				}
				else if (itemId == 49222) { // オーク密使の笛
					if (pc.isDragonKnight() && (pc.getMapId() == 61)) { // HC3F
						boolean found = false;
						for (L1Object obj : L1World.getInstance().getObject()) {
							if (obj instanceof L1MonsterInstance) {
								L1MonsterInstance mob = (L1MonsterInstance) obj;
								if (mob != null) {
									if (mob.getNpcTemplate().get_npcId() == 46161) {
										found = true;
										break;
									}
								}
							}
						}
						if (found) {
							pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
						}
						else {
							L1SpawnUtil.spawn(pc, 46161, 0, 0); // オーク
																// 密使リーダー
						}
						pc.getInventory().consumeItem(49222, 1);
					}
				}
				// 幻術士試練 增加 start
				else if (itemId == 49188) { // 索夏依卡靈魂之石
					if (l1iteminstance1.getItem().getItemId() == 49186) {
						L1ItemInstance item1 = ItemTable.getInstance().createItem(49189);
						item1.setCount(1);
						if (pc.getInventory().checkAddItem(item1, 1) == L1Inventory.OK) {
							pc.getInventory().storeItem(item1);
							pc.sendPackets(new S_ServerMessage(403, item1.getLogName()));
							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.getInventory().removeItem(l1iteminstance1, 1);
						}
					}
					else {
						pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
					}
				}
				else if (itemId == 49189) { // 索夏依卡靈魂之笛
					if (pc.isIllusionist() && (pc.getMapId() == 4)) { // 古魯丁祭壇
						boolean found = false;
						for (L1Object obj : L1World.getInstance().getObject()) {
							if (obj instanceof L1MonsterInstance) {
								L1MonsterInstance mob = (L1MonsterInstance) obj;
								if (mob != null) {
									if (mob.getNpcTemplate().get_npcId() == 46163) {// 艾爾摩索夏依卡將軍的冤魂
										found = true;
										break;
									}
								}
							}
						}
						if (found) {
							pc.sendPackets(new S_ServerMessage(79));
						}
						else {
							L1SpawnUtil.spawn(pc, 46163, 0, 0);
						}
						pc.getInventory().consumeItem(49189, 1);
					}
				}
				else if (itemId == 49201) { // 完成的時間水晶球
					if (pc.isIllusionist() && (pc.getMapId() == 4)) { // 火龍窟
						boolean found = false;
						for (L1Object obj : L1World.getInstance().getObject()) {
							if (obj instanceof L1MonsterInstance) {
								L1MonsterInstance mob = (L1MonsterInstance) obj;
								if (mob != null) {
									if (mob.getNpcTemplate().get_npcId() == 81254) {// 時空裂痕
										found = true;
										break;
									}
								}
							}
						}
						if (found) {
							pc.sendPackets(new S_ServerMessage(79));
						}
						else {
							L1SpawnUtil.spawn(pc, 81254, 0, 0);
						}
						pc.getInventory().consumeItem(49201, 1);
					}
				// 幻術士試練 增加 end

				} else if (itemId == 47010) { // 龍之鑰匙
					if (!L1CastleLocation.checkInAllWarArea(pc.getLocation())) {// 檢查是否在城堡區域內
						pc.sendPackets(new S_DragonGate(pc ,L1DragonSlayer.getInstance().checkDragonPortal()));
					} else {
						pc.sendPackets(new S_ServerMessage(79)); // 沒有任何事情發生
					}
				}
				else {
					int locX = ((L1EtcItem) l1iteminstance.getItem()).get_locx();
					int locY = ((L1EtcItem) l1iteminstance.getItem()).get_locy();
					short mapId = ((L1EtcItem) l1iteminstance.getItem()).get_mapid();
					if ((locX != 0) && (locY != 0)) { // 各種テレポートスクロール
						if (pc.getMap().isEscapable() || pc.isGm()) {
							L1Teleport.teleport(pc, locX, locY, mapId, pc.getHeading(), true);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
						else {
							pc.sendPackets(new S_ServerMessage(647));
						}
					}
					else {
						if (l1iteminstance.getCount() < 1) { // あり得ない？
							pc.sendPackets(new S_ServerMessage(329, l1iteminstance.getLogName())); // \f1%0を持っていません。
						}
						else {
							pc.sendPackets(new S_ServerMessage(74, l1iteminstance.getLogName())); // \f1%0は使用できません。
						}
					}
				}
			}
			else if (l1iteminstance.getItem().getType2() == 1) {
				// 種別：武器
				int min = l1iteminstance.getItem().getMinLevel();
				int max = l1iteminstance.getItem().getMaxLevel();
				if ((min != 0) && (min > pc.getLevel())) {
					// 等級 %0以上才可使用此道具。
					pc.sendPackets(new S_ServerMessage(318, String.valueOf(min)));
				}
				else if ((max != 0) && (max < pc.getLevel())) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max)); // 等級%d以下才能使用此道具。
				}
				else {
					if ((pc.isCrown() && l1iteminstance.getItem().isUseRoyal()) || (pc.isKnight() && l1iteminstance.getItem().isUseKnight())
							|| (pc.isElf() && l1iteminstance.getItem().isUseElf()) || (pc.isWizard() && l1iteminstance.getItem().isUseMage())
							|| (pc.isDarkelf() && l1iteminstance.getItem().isUseDarkelf())
							|| (pc.isDragonKnight() && l1iteminstance.getItem().isUseDragonknight())
							|| (pc.isIllusionist() && l1iteminstance.getItem().isUseIllusionist())) {
						UseWeapon(pc, l1iteminstance);
					}
					else {
						// \f1あなたのクラスではこのアイテムは使用できません。
						pc.sendPackets(new S_ServerMessage(264));
					}
				}
			}
			else if (l1iteminstance.getItem().getType2() == 2) { // 種別：防具
				if ((pc.isCrown() && l1iteminstance.getItem().isUseRoyal()) || (pc.isKnight() && l1iteminstance.getItem().isUseKnight())
						|| (pc.isElf() && l1iteminstance.getItem().isUseElf()) || (pc.isWizard() && l1iteminstance.getItem().isUseMage())
						|| (pc.isDarkelf() && l1iteminstance.getItem().isUseDarkelf())
						|| (pc.isDragonKnight() && l1iteminstance.getItem().isUseDragonknight())
						|| (pc.isIllusionist() && l1iteminstance.getItem().isUseIllusionist())) {

					int min = ((L1Armor) l1iteminstance.getItem()).getMinLevel();
					int max = ((L1Armor) l1iteminstance.getItem()).getMaxLevel();
					if ((min != 0) && (min > pc.getLevel())) {
						// 等級 %0以上才可使用此道具。
						pc.sendPackets(new S_ServerMessage(318, String.valueOf(min)));
					}
					else if ((max != 0) && (max < pc.getLevel())) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max)); // 等級%d以下才能使用此道具。
					}
					else {
						UseArmor(pc, l1iteminstance);
					}
				}
				else {
					// \f1あなたのクラスではこのアイテムは使用できません。
					pc.sendPackets(new S_ServerMessage(264));
				}
			}

			// 効果ディレイがある場合は現在時間をセット
			if (isDelayEffect) {
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				l1iteminstance.setLastUsed(ts);
				pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_DELAY_EFFECT);
				pc.getInventory().saveItem(l1iteminstance, L1PcInventory.COL_DELAY_EFFECT);
			}

			L1ItemDelay.onItemUse(client, l1iteminstance); // アイテムディレイ開始
		}
	}

	private void SuccessEnchant(L1PcInstance pc, L1ItemInstance item, ClientThread client, int i) {
		String s = "";
		String sa = "";
		String sb = "";
		String s1 = item.getName();
		String pm = "";
		if (item.getEnchantLevel() > 0) {
			pm = "+";
		}
		if (item.getItem().getType2() == 1) {
			if (!item.isIdentified() || (item.getEnchantLevel() == 0)) {
				switch (i) {
					case -1:
						s = s1;
						sa = "$246";
						sb = "$247";
						break;

					case 1: // '\001'
						s = s1;
						sa = "$245";
						sb = "$247";
						break;

					case 2: // '\002'
						s = s1;
						sa = "$245";
						sb = "$248";
						break;

					case 3: // '\003'
						s = s1;
						sa = "$245";
						sb = "$248";
						break;
				}
			}
			else {
				switch (i) {
					case -1:
						s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$246";
						sb = "$247";
						break;

					case 1: // '\001'
						s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$245";
						sb = "$247";
						break;

					case 2: // '\002'
						s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$245";
						sb = "$248";
						break;

					case 3: // '\003'
						s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$245";
						sb = "$248";
						break;
				}
			}
		}
		else if (item.getItem().getType2() == 2) {
			if (!item.isIdentified() || (item.getEnchantLevel() == 0)) {
				switch (i) {
					case -1:
						s = s1;
						sa = "$246";
						sb = "$247";
						break;

					case 1: // '\001'
						s = s1;
						sa = "$252";
						sb = "$247 ";
						break;

					case 2: // '\002'
						s = s1;
						sa = "$252";
						sb = "$248 ";
						break;

					case 3: // '\003'
						s = s1;
						sa = "$252";
						sb = "$248 ";
						break;
				}
			}
			else {
				switch (i) {
					case -1:
						s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$246";
						sb = "$247";
						break;

					case 1: // '\001'
						s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$252";
						sb = "$247 ";
						break;

					case 2: // '\002'
						s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$252";
						sb = "$248 ";
						break;

					case 3: // '\003'
						s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(s1).toString(); // \f1%0が%2%1光ります。
						sa = "$252";
						sb = "$248 ";
						break;
				}
			}
		}
		pc.sendPackets(new S_ServerMessage(161, s, sa, sb));
		int oldEnchantLvl = item.getEnchantLevel();
		int newEnchantLvl = item.getEnchantLevel() + i;
		int safe_enchant = item.getItem().get_safeenchant();
		item.setEnchantLevel(newEnchantLvl);
		client.getActiveChar().getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
		if (newEnchantLvl > safe_enchant) {
			client.getActiveChar().getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
		}
		if ((item.getItem().getType2() == 1) && (Config.LOGGING_WEAPON_ENCHANT != 0)) {
			if ((safe_enchant == 0) || (newEnchantLvl >= Config.LOGGING_WEAPON_ENCHANT)) {
				LogEnchantTable logenchant = new LogEnchantTable();
				logenchant.storeLogEnchant(pc.getId(), item.getId(), oldEnchantLvl, newEnchantLvl);
			}
		}
		if ((item.getItem().getType2() == 2) && (Config.LOGGING_ARMOR_ENCHANT != 0)) {
			if ((safe_enchant == 0) || (newEnchantLvl >= Config.LOGGING_ARMOR_ENCHANT)) {
				LogEnchantTable logenchant = new LogEnchantTable();
				logenchant.storeLogEnchant(pc.getId(), item.getId(), oldEnchantLvl, newEnchantLvl);
			}
		}

		if (item.getItem().getType2() == 2) {
			if (item.isEquipped()) {
				if ((item.getItem().getType() < 8
						|| item.getItem().getType() > 12)) {
					pc.addAc(-i);
				}
				int i2 = item.getItem().getItemId();
				if ((i2 == 20011) || (i2 == 20110) || (i2 == 21108) || (i2 == 120011)) { // マジックヘルム、マジックチェーンメイル、キャラクター名の魔法抵抗のＴシャツ
					pc.addMr(i);
					pc.sendPackets(new S_SPMR(pc));
				}
				if ((i2 == 20056) || (i2 == 120056) || (i2 == 220056)) { // マジック
																			// クローク
					pc.addMr(i * 2);
					pc.sendPackets(new S_SPMR(pc));
				}
			}
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
	}

	private void FailureEnchant(L1PcInstance pc, L1ItemInstance item, ClientThread client) {
		String s = "";
		String sa = "";
		int itemType = item.getItem().getType2();
		String nameId = item.getName();
		String pm = "";
		if (itemType == 1) { // 武器
			if (!item.isIdentified() || (item.getEnchantLevel() == 0)) {
				s = nameId; // \f1%0が強烈に%1光ったあと、蒸発してなくなります。
				sa = "$245";
			}
			else {
				if (item.getEnchantLevel() > 0) {
					pm = "+";
				}
				s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(nameId).toString(); // \f1%0が強烈に%1光ったあと、蒸発してなくなります。
				sa = "$245";
			}
		}
		else if (itemType == 2) { // 防具
			if (!item.isIdentified() || (item.getEnchantLevel() == 0)) {
				s = nameId; // \f1%0が強烈に%1光ったあと、蒸発してなくなります。
				sa = " $252";
			}
			else {
				if (item.getEnchantLevel() > 0) {
					pm = "+";
				}
				s = (new StringBuilder()).append(pm).append(item.getEnchantLevel()).append(" ").append(nameId).toString(); // \f1%0が強烈に%1光ったあと、蒸発してなくなります。
				sa = " $252";
			}
		}
		pc.sendPackets(new S_ServerMessage(164, s, sa));
		pc.getInventory().removeItem(item, item.getCount());
	}

	private boolean usePolyScroll(L1PcInstance pc, int item_id, String s) {
		int awakeSkillId = pc.getAwakeSkillId();
		if ((awakeSkillId == AWAKEN_ANTHARAS) || (awakeSkillId == AWAKEN_FAFURION) || (awakeSkillId == AWAKEN_VALAKAS)) {
			pc.sendPackets(new S_ServerMessage(1384)); // 目前狀態中無法變身。
			return false;
		}

		int time = 0;
		if ((item_id == 40088) || (item_id == 40096)) { // 變形卷軸、象牙塔變形卷軸
			time = 1800;
		}
		else if (item_id == 49308) { // 福利變形藥水
			time = Random.nextInt(2401, 4800);
		}
		else if (item_id == 140088) { // 受祝福的 變形卷軸
			time = 2100;
		}

		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		if ((poly != null) || s.equals("")) {
			if (s.equals("")) {
				if ((pc.getTempCharGfx() == 6034) || (pc.getTempCharGfx() == 6035)) {
					return true;
				} else {
					pc.removeSkillEffect(SHAPE_CHANGE);
					return true;
				}
			} else if ((poly.getMinLevel() <= pc.getLevel()) || pc.isGm()) {
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
				return true;
			}
		}
		return false;
	}

	private void usePolyScale(L1PcInstance pc, int itemId) {
		int time = 900;
		int awakeSkillId = pc.getAwakeSkillId();
		if ((awakeSkillId == AWAKEN_ANTHARAS) || (awakeSkillId == AWAKEN_FAFURION) || (awakeSkillId == AWAKEN_VALAKAS)) {
			pc.sendPackets(new S_ServerMessage(1384)); // 目前狀態中無法變身。
			return;
		}

		int polyId = 0;
		if (itemId == 41154) { // 暗之鱗
			polyId = 3101;
		} else if (itemId == 41155) { // 火之鱗
			polyId = 3126;
		} else if (itemId == 41156) { // 叛之鱗
			polyId = 3888;
		} else if (itemId == 41157) { // 恨之鱗
			polyId = 3784;
		} else if (itemId == 49220) { // 妖魔密使變形卷軸
			polyId = 6984;
			time = 1200;
		}
		L1PolyMorph.doPoly(pc, polyId, time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
	}

	private void usePolyPotion(L1PcInstance pc, int itemId) {
		int time = 1800;
		int awakeSkillId = pc.getAwakeSkillId();
		if ((awakeSkillId == AWAKEN_ANTHARAS) || (awakeSkillId == AWAKEN_FAFURION) || (awakeSkillId == AWAKEN_VALAKAS)) {
			pc.sendPackets(new S_ServerMessage(1384)); // 目前狀態中無法變身。
			return;
		}

		int polyId = 0;
		if (itemId == 41143) { // 海賊骷髏首領變身藥水
			polyId = 6086;
			time = 900;
		}
		else if (itemId == 41144) { // 海賊骷髏士兵變身藥水
			polyId = 6087;
			time = 900;
		}
		else if (itemId == 41145) { // 海賊骷髏刀手變身藥水
			polyId = 6088;
			time = 900;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 0) && pc.isCrown()) { // 夏納的變身卷軸(等級30)
			polyId = 6822;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 1) && pc.isCrown()) {
			polyId = 6823;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 0) && pc.isKnight()) {
			polyId = 6824;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 1) && pc.isKnight()) {
			polyId = 6825;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 0) && pc.isElf()) {
			polyId = 6826;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 1) && pc.isElf()) {
			polyId = 6827;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 0) && pc.isWizard()) {
			polyId = 6828;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 1) && pc.isWizard()) {
			polyId = 6829;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 0) && pc.isDarkelf()) {
			polyId = 6830;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 1) && pc.isDarkelf()) {
			polyId = 6831;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 0) && pc.isDragonKnight()) {
			polyId = 7139;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 1) && pc.isDragonKnight()) {
			polyId = 7140;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 0) && pc.isIllusionist()) {
			polyId = 7141;
		}
		else if ((itemId == 49149) && (pc.get_sex() == 1) && pc.isIllusionist()) {
			polyId = 7142;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 0) && pc.isCrown()) { // 夏納的變身卷軸(等級40)
			polyId = 6832;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 1) && pc.isCrown()) {
			polyId = 6833;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 0) && pc.isKnight()) {
			polyId = 6834;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 1) && pc.isKnight()) {
			polyId = 6835;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 0) && pc.isElf()) {
			polyId = 6836;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 1) && pc.isElf()) {
			polyId = 6837;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 0) && pc.isWizard()) {
			polyId = 6838;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 1) && pc.isWizard()) {
			polyId = 6839;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 0) && pc.isDarkelf()) {
			polyId = 6840;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 1) && pc.isDarkelf()) {
			polyId = 6841;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 0) && pc.isDragonKnight()) {
			polyId = 7143;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 1) && pc.isDragonKnight()) {
			polyId = 7144;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 0) && pc.isIllusionist()) {
			polyId = 7145;
		}
		else if ((itemId == 49150) && (pc.get_sex() == 1) && pc.isIllusionist()) {
			polyId = 7146;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 0) && pc.isCrown()) { // 夏納的變身卷軸(等級52)
			polyId = 6842;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 1) && pc.isCrown()) {
			polyId = 6843;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 0) && pc.isKnight()) {
			polyId = 6844;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 1) && pc.isKnight()) {
			polyId = 6845;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 0) && pc.isElf()) {
			polyId = 6846;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 1) && pc.isElf()) {
			polyId = 6847;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 0) && pc.isWizard()) {
			polyId = 6848;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 1) && pc.isWizard()) {
			polyId = 6849;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 0) && pc.isDarkelf()) {
			polyId = 6850;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 1) && pc.isDarkelf()) {
			polyId = 6851;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 0) && pc.isDragonKnight()) {
			polyId = 7147;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 1) && pc.isDragonKnight()) {
			polyId = 7148;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 0) && pc.isIllusionist()) {
			polyId = 7149;
		}
		else if ((itemId == 49151) && (pc.get_sex() == 1) && pc.isIllusionist()) {
			polyId = 7150;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 0) && pc.isCrown()) { // 夏納的變身卷軸(等級55)
			polyId = 6852;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 1) && pc.isCrown()) {
			polyId = 6853;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 0) && pc.isKnight()) {
			polyId = 6854;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 1) && pc.isKnight()) {
			polyId = 6855;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 0) && pc.isElf()) {
			polyId = 6856;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 1) && pc.isElf()) {
			polyId = 6857;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 0) && pc.isWizard()) {
			polyId = 6858;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 1) && pc.isWizard()) {
			polyId = 6859;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 0) && pc.isDarkelf()) {
			polyId = 6860;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 1) && pc.isDarkelf()) {
			polyId = 6861;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 0) && pc.isDragonKnight()) {
			polyId = 7151;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 1) && pc.isDragonKnight()) {
			polyId = 7152;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 0) && pc.isIllusionist()) {
			polyId = 7153;
		}
		else if ((itemId == 49152) && (pc.get_sex() == 1) && pc.isIllusionist()) {
			polyId = 7154;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 0) && pc.isCrown()) { // 夏納的變身卷軸(等級60)
			polyId = 6862;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 1) && pc.isCrown()) {
			polyId = 6863;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 0) && pc.isKnight()) {
			polyId = 6864;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 1) && pc.isKnight()) {
			polyId = 6865;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 0) && pc.isElf()) {
			polyId = 6866;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 1) && pc.isElf()) {
			polyId = 6867;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 0) && pc.isWizard()) {
			polyId = 6868;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 1) && pc.isWizard()) {
			polyId = 6869;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 0) && pc.isDarkelf()) {
			polyId = 6870;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 1) && pc.isDarkelf()) {
			polyId = 6871;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 0) && pc.isDragonKnight()) {
			polyId = 7155;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 1) && pc.isDragonKnight()) {
			polyId = 7156;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 0) && pc.isIllusionist()) {
			polyId = 7157;
		}
		else if ((itemId == 49153) && (pc.get_sex() == 1) && pc.isIllusionist()) {
			polyId = 7158;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 0) && pc.isCrown()) { // 夏納的變身卷軸(等級65)
			polyId = 6872;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 1) && pc.isCrown()) {
			polyId = 6873;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 0) && pc.isKnight()) {
			polyId = 6874;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 1) && pc.isKnight()) {
			polyId = 6875;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 0) && pc.isElf()) {
			polyId = 6876;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 1) && pc.isElf()) {
			polyId = 6877;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 0) && pc.isWizard()) {
			polyId = 6878;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 1) && pc.isWizard()) {
			polyId = 6879;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 0) && pc.isDarkelf()) {
			polyId = 6880;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 1) && pc.isDarkelf()) {
			polyId = 6881;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 0) && pc.isDragonKnight()) {
			polyId = 7159;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 1) && pc.isDragonKnight()) {
			polyId = 7160;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 0) && pc.isIllusionist()) {
			polyId = 7161;
		}
		else if ((itemId == 49154) && (pc.get_sex() == 1) && pc.isIllusionist()) {
			polyId = 7162;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 0) && pc.isCrown()) { // 夏納的變身卷軸(等級70)
			polyId = 6882;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 1) && pc.isCrown()) {
			polyId = 6883;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 0) && pc.isKnight()) {
			polyId = 6884;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 1) && pc.isKnight()) {
			polyId = 6885;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 0) && pc.isElf()) {
			polyId = 6886;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 1) && pc.isElf()) {
			polyId = 6887;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 0) && pc.isWizard()) {
			polyId = 6888;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 1) && pc.isWizard()) {
			polyId = 6889;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 0) && pc.isDarkelf()) {
			polyId = 6890;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 1) && pc.isDarkelf()) {
			polyId = 6891;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 0) && pc.isDragonKnight()) {
			polyId = 7163;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 1) && pc.isDragonKnight()) {
			polyId = 7164;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 0) && pc.isIllusionist()) {
			polyId = 7165;
		}
		else if ((itemId == 49155) && (pc.get_sex() == 1) && pc.isIllusionist()) {
			polyId = 7166;
		}
		else if ((itemId == 49139)) { // 起司蛋糕
			polyId = 6137; // 52級死亡騎士
			time = 900;
		}
		L1PolyMorph.doPoly(pc, polyId, time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
	}

	private void UseArmor(L1PcInstance activeChar, L1ItemInstance armor) {
		int type = armor.getItem().getType();
		L1PcInventory pcInventory = activeChar.getInventory();
		boolean equipeSpace; // 装備する箇所が空いているか
		if (type == 9) { // リングの場合
			equipeSpace = pcInventory.getTypeEquipped(2, 9) <= 1;
		}
		else {
			equipeSpace = pcInventory.getTypeEquipped(2, type) <= 0;
		}

		if (equipeSpace && !armor.isEquipped()) { // 使用した防具を装備していなくて、その装備箇所が空いている場合（装着を試みる）
			int polyid = activeChar.getTempCharGfx();

			if (!L1PolyMorph.isEquipableArmor(polyid, type)) { // その変身では装備不可
				return;
			}

			if (((type == 13) && (pcInventory.getTypeEquipped(2, 7) >= 1)) || ((type == 7) && (pcInventory.getTypeEquipped(2, 13) >= 1))) { // シールド、ガーダー同時裝備不可
				activeChar.sendPackets(new S_ServerMessage(124)); // \f1すでに何かを装備しています。
				return;
			}
			if ((type == 7) && (activeChar.getWeapon() != null)) { // シールドの場合、武器を装備していたら両手武器チェック
				if (activeChar.getWeapon().getItem().isTwohandedWeapon()) { // 両手武器
					activeChar.sendPackets(new S_ServerMessage(129)); // \f1両手の武器を武装したままシールドを着用することはできません。
					return;
				}
			}

			if ((type == 3) && (pcInventory.getTypeEquipped(2, 4) >= 1)) { // シャツの場合、マントを着てないか確認
				activeChar.sendPackets(new S_ServerMessage(126, "$224", "$225")); // \f1%1上に%0を着ることはできません。
				return;
			}
			else if ((type == 3) && (pcInventory.getTypeEquipped(2, 2) >= 1)) { // シャツの場合、メイルを着てないか確認
				activeChar.sendPackets(new S_ServerMessage(126, "$224", "$226")); // \f1%1上に%0を着ることはできません。
				return;
			}
			else if ((type == 2) && (pcInventory.getTypeEquipped(2, 4) >= 1)) { // メイルの場合、マントを着てないか確認
				activeChar.sendPackets(new S_ServerMessage(126, "$226", "$225")); // \f1%1上に%0を着ることはできません。
				return;
			}

			pcInventory.setEquipped(armor, true);
		}
		else if (armor.isEquipped()) { // 使用した防具を装備していた場合（脱着を試みる）
			if (armor.getItem().getBless() == 2) { // 呪われていた場合
				activeChar.sendPackets(new S_ServerMessage(150)); // \f1はずすことができません。呪いをかけられているようです。
				return;
			}
			if ((type == 3) && (pcInventory.getTypeEquipped(2, 2) >= 1)) { // シャツの場合、メイルを着てないか確認
				activeChar.sendPackets(new S_ServerMessage(127)); // \f1それは脱ぐことができません。
				return;
			}
			else if (((type == 2) || (type == 3)) && (pcInventory.getTypeEquipped(2, 4) >= 1)) { // シャツとメイルの場合、マントを着てないか確認
				activeChar.sendPackets(new S_ServerMessage(127)); // \f1それは脱ぐことができません。
				return;
			}
			if (type == 7) { // シールドの場合、ソリッドキャリッジの効果消失
				if (activeChar.hasSkillEffect(SOLID_CARRIAGE)) {
					activeChar.removeSkillEffect(SOLID_CARRIAGE);
				}
			}
			pcInventory.setEquipped(armor, false);
		}
		else {
			activeChar.sendPackets(new S_ServerMessage(124)); // \f1すでに何かを装備しています。
		}
		// セット装備用HP、MP、MR更新
		activeChar.setCurrentHp(activeChar.getCurrentHp());
		activeChar.setCurrentMp(activeChar.getCurrentMp());
		activeChar.sendPackets(new S_OwnCharAttrDef(activeChar));
		activeChar.sendPackets(new S_OwnCharStatus(activeChar));
		activeChar.sendPackets(new S_SPMR(activeChar));
	}

	private void UseWeapon(L1PcInstance activeChar, L1ItemInstance weapon) {
		L1PcInventory pcInventory = activeChar.getInventory();
		if ((activeChar.getWeapon() == null) || !activeChar.getWeapon().equals(weapon)) { // 指定された武器が装備している武器と違う場合、装備できるか確認
			int weapon_type = weapon.getItem().getType();
			int polyid = activeChar.getTempCharGfx();

			if (!L1PolyMorph.isEquipableWeapon(polyid, weapon_type)) { // その変身では装備不可
				return;
			}
			if (weapon.getItem().isTwohandedWeapon() && (pcInventory.getTypeEquipped(2, 7) >= 1)) { // 両手武器の場合、シールド装備の確認
				activeChar.sendPackets(new S_ServerMessage(128)); // \f1シールドを装備している時は両手で持つ武器を使うことはできません。
				return;
			}
		}

		if (activeChar.getWeapon() != null) { // 既に何かを装備している場合、前の装備をはずす
			if (activeChar.getWeapon().getItem().getBless() == 2) { // 呪われていた場合
				activeChar.sendPackets(new S_ServerMessage(150)); // \f1はずすことができません。呪いをかけられているようです。
				return;
			}
			if (activeChar.getWeapon().equals(weapon)) {
				// 装備交換ではなく外すだけ
				pcInventory.setEquipped(activeChar.getWeapon(), false, false, false);
				return;
			}
			else {
				pcInventory.setEquipped(activeChar.getWeapon(), false, false, true);
			}
		}

		if (weapon.getItemId() == 200002) { // 呪われたダイスダガー
			activeChar.sendPackets(new S_ServerMessage(149, weapon.getLogName())); // \f1%0が手にくっつきました。
		}
		pcInventory.setEquipped(weapon, true, false, false);
	}

	private void useSpellBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int itemAttr = 0;
		int locAttr = 0; // 0:other 1:law 2:chaos
		boolean isLawful = true;
		int pcX = pc.getX();
		int pcY = pc.getY();
		int mapId = pc.getMapId();
		int level = pc.getLevel();
		if ((itemId == 45000) || (itemId == 45008) || (itemId == 45018) || (itemId == 45021) || (itemId == 40171) || (itemId == 40179)
				|| (itemId == 40180) || (itemId == 40182) || (itemId == 40194) || (itemId == 40197) || (itemId == 40202) || (itemId == 40206)
				|| (itemId == 40213) || (itemId == 40220) || (itemId == 40222)) {
			itemAttr = 1;
		}
		if ((itemId == 45009) || (itemId == 45010) || (itemId == 45019) || (itemId == 40172) || (itemId == 40173) || (itemId == 40178)
				|| (itemId == 40185) || (itemId == 40186) || (itemId == 40192) || (itemId == 40196) || (itemId == 40201) || (itemId == 40204)
				|| (itemId == 40211) || (itemId == 40221) || (itemId == 40225)) {
			itemAttr = 2;
		}
		// ロウフルテンプル
		if (((pcX > 33116) && (pcX < 33128) && (pcY > 32930) && (pcY < 32942) && (mapId == 4))
				|| ((pcX > 33135) && (pcX < 33147) && (pcY > 32235) && (pcY < 32247) && (mapId == 4))
				|| ((pcX >= 32783) && (pcX <= 32803) && (pcY >= 32831) && (pcY <= 32851) && (mapId == 77))) {
			locAttr = 1;
			isLawful = true;
		}
		// カオティックテンプル
		if (((pcX > 32880) && (pcX < 32892) && (pcY > 32646) && (pcY < 32658) && (mapId == 4))
				|| ((pcX > 32662) && (pcX < 32674) && (pcY > 32297) && (pcY < 32309) && (mapId == 4))) {
			locAttr = 2;
			isLawful = false;
		}
		if (pc.isGm()) {
			SpellBook(pc, item, isLawful);
		}
		else if (((itemAttr == locAttr) || (itemAttr == 0)) && (locAttr != 0)) {
			if (pc.isKnight()) {
				if ((itemId >= 45000) && (itemId <= 45007) && (level >= 50)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 45000) && (itemId <= 45007)) {
					pc.sendPackets(new S_ServerMessage(312));
				}
				else {
					pc.sendPackets(new S_ServerMessage(79));
				}
			}
			else if (pc.isCrown() || pc.isDarkelf()) {
				if ((itemId >= 45000) && (itemId <= 45007) && (level >= 10)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 45008) && (itemId <= 45015) && (level >= 20)) {
					SpellBook(pc, item, isLawful);
				}
				else if (((itemId >= 45008) && (itemId <= 45015)) || ((itemId >= 45000) && (itemId <= 45007))) {
					pc.sendPackets(new S_ServerMessage(312)); // レベルが低くてその魔法を覚えることができません。
				}
				else {
					pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
				}
			}
			else if (pc.isElf()) {
				if ((itemId >= 45000) && (itemId <= 45007) && (level >= 8)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 45008) && (itemId <= 45015) && (level >= 16)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 45016) && (itemId <= 45022) && (level >= 24)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40170) && (itemId <= 40177) && (level >= 32)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40178) && (itemId <= 40185) && (level >= 40)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40186) && (itemId <= 40193) && (level >= 48)) {
					SpellBook(pc, item, isLawful);
				}
				else if (((itemId >= 45000) && (itemId <= 45022)) || ((itemId >= 40170) && (itemId <= 40193))) {
					pc.sendPackets(new S_ServerMessage(312)); // レベルが低くてその魔法を覚えることができません。
				}
				else {
					pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
				}
			}
			else if (pc.isWizard()) {
				if ((itemId >= 45000) && (itemId <= 45007) && (level >= 4)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 45008) && (itemId <= 45015) && (level >= 8)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 45016) && (itemId <= 45022) && (level >= 12)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40170) && (itemId <= 40177) && (level >= 16)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40178) && (itemId <= 40185) && (level >= 20)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40186) && (itemId <= 40193) && (level >= 24)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40194) && (itemId <= 40201) && (level >= 28)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40202) && (itemId <= 40209) && (level >= 32)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40210) && (itemId <= 40217) && (level >= 36)) {
					SpellBook(pc, item, isLawful);
				}
				else if ((itemId >= 40218) && (itemId <= 40225) && (level >= 40)) {
					SpellBook(pc, item, isLawful);
				}
				else {
					pc.sendPackets(new S_ServerMessage(312)); // レベルが低くてその魔法を覚えることができません。
				}
			}
		}
		else if ((itemAttr != locAttr) && (itemAttr != 0) && (locAttr != 0)) {
			// 間違ったテンプルで読んだ場合雷が落ちる
			pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
			S_SkillSound effect = new S_SkillSound(pc.getId(), 10);
			pc.sendPackets(effect);
			pc.broadcastPacket(effect);
			// ダメージは適当
			pc.setCurrentHp(Math.max(pc.getCurrentHp() - 45, 0));
			if (pc.getCurrentHp() <= 0) {
				pc.death(null);
			}
			pc.getInventory().removeItem(item, 1);
		}
		else {
			pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
		}
	}

	private void useElfSpellBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int level = pc.getLevel();
		if ((pc.isElf() || pc.isGm()) && isLearnElfMagic(pc)) {
			if ((itemId >= 40232) && (itemId <= 40234) && (level >= 10)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 40235) && (itemId <= 40236) && (level >= 20)) {
				SpellBook2(pc, item);
			}
			if ((itemId >= 40237) && (itemId <= 40240) && (level >= 30)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 40241) && (itemId <= 40243) && (level >= 40)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 40244) && (itemId <= 40246) && (level >= 50)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 40247) && (itemId <= 40248) && (level >= 30)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 40249) && (itemId <= 40250) && (level >= 40)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 40251) && (itemId <= 40252) && (level >= 50)) {
				SpellBook2(pc, item);
			}
			else if ((itemId == 40253) && (level >= 30)) {
				SpellBook2(pc, item);
			}
			else if ((itemId == 40254) && (level >= 40)) {
				SpellBook2(pc, item);
			}
			else if ((itemId == 40255) && (level >= 50)) {
				SpellBook2(pc, item);
			}
			else if ((itemId == 40256) && (level >= 30)) {
				SpellBook2(pc, item);
			}
			else if ((itemId == 40257) && (level >= 40)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 40258) && (itemId <= 40259) && (level >= 50)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 40260) && (itemId <= 40261) && (level >= 30)) {
				SpellBook2(pc, item);
			}
			else if ((itemId == 40262) && (level >= 40)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 40263) && (itemId <= 40264) && (level >= 50)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 41149) && (itemId <= 41150) && (level >= 50)) {
				SpellBook2(pc, item);
			}
			else if ((itemId == 41151) && (level >= 40)) {
				SpellBook2(pc, item);
			}
			else if ((itemId >= 41152) && (itemId <= 41153) && (level >= 50)) {
				SpellBook2(pc, item);
			}
		}
		else {
			pc.sendPackets(new S_ServerMessage(79)); // (原文:精霊の水晶はエルフのみが習得できます。)
		}
	}

	private boolean isLearnElfMagic(L1PcInstance pc) {
		int pcX = pc.getX();
		int pcY = pc.getY();
		int pcMapId = pc.getMapId();
		if (((pcX >= 32786) && (pcX <= 32797) && (pcY >= 32842) && (pcY <= 32859) && (pcMapId == 75 // 象牙の塔
				))
				|| (pc.getLocation().isInScreen(new Point(33055, 32336)) && (pcMapId == 4))) { // マザーツリー
			return true;
		}
		return false;
	}

	private void SpellBook(L1PcInstance pc, L1ItemInstance item, boolean isLawful) {
		String s = "";
		int i = 0;
		int level1 = 0;
		int level2 = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		for (int skillId = 1; skillId < 81; skillId++) {
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
			String s1 = null;
			if (Config.CLIENT_LANGUAGE == 3) {
				s1 = (new StringBuilder()).append("魔法書(").append(l1skills.getName()).append(")").toString();
			}
			else if (Config.CLIENT_LANGUAGE == 5) {
				s1 = (new StringBuilder()).append("魔法书(").append(l1skills.getName()).append(")").toString();
			}
			else {
				s1 = (new StringBuilder()).append("魔法書(").append(l1skills.getName()).append(")").toString();
			}
			if (item.getItem().getName().equalsIgnoreCase(s1)) {
				int skillLevel = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (skillLevel) {
					case 1: // '\001'
						level1 = i7;
						break;

					case 2: // '\002'
						level2 = i7;
						break;

					case 3: // '\003'
						l = i7;
						break;

					case 4: // '\004'
						i1 = i7;
						break;

					case 5: // '\005'
						j1 = i7;
						break;

					case 6: // '\006'
						k1 = i7;
						break;

					case 7: // '\007'
						l1 = i7;
						break;

					case 8: // '\b'
						i2 = i7;
						break;

					case 9: // '\t'
						j2 = i7;
						break;

					case 10: // '\n'
						k2 = i7;
						break;

					case 11: // '\013'
						l2 = i7;
						break;

					case 12: // '\f'
						i3 = i7;
						break;

					case 13: // '\r'
						j3 = i7;
						break;

					case 14: // '\016'
						k3 = i7;
						break;

					case 15: // '\017'
						l3 = i7;
						break;

					case 16: // '\020'
						i4 = i7;
						break;

					case 17: // '\021'
						j4 = i7;
						break;

					case 18: // '\022'
						k4 = i7;
						break;

					case 19: // '\023'
						l4 = i7;
						break;

					case 20: // '\024'
						i5 = i7;
						break;

					case 21: // '\025'
						j5 = i7;
						break;

					case 22: // '\026'
						k5 = i7;
						break;

					case 23: // '\027'
						l5 = i7;
						break;

					case 24: // '\030'
						i6 = i7;
						break;
				}
			}
		}

		int objid = pc.getId();
		pc.sendPackets(new S_AddSkill(level1, level2, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5, k5, l5, i6, 0, 0, 0,
				0));
		S_SkillSound s_skillSound = new S_SkillSound(objid, isLawful ? 224 : 231);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(objid, i, s, 0, 0);
		pc.getInventory().removeItem(item, 1);
	}

	private void SpellBook1(L1PcInstance pc, L1ItemInstance l1iteminstance, ClientThread clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		for (int j6 = 97; j6 < 112; j6++) {
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = null;
			if (Config.CLIENT_LANGUAGE == 3) {
				s1 = (new StringBuilder()).append("黑暗精靈水晶(").append(l1skills.getName()).append(")").toString();
			}
			else if (Config.CLIENT_LANGUAGE == 5) {
				s1 = (new StringBuilder()).append("黑暗精灵水晶(").append(l1skills.getName()).append(")").toString();
			}
			else {
				s1 = (new StringBuilder()).append("闇精霊の水晶(").append(l1skills.getName()).append(")").toString();
			}
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
					case 1: // '\001'
						j = i7;
						break;

					case 2: // '\002'
						k = i7;
						break;

					case 3: // '\003'
						l = i7;
						break;

					case 4: // '\004'
						i1 = i7;
						break;

					case 5: // '\005'
						j1 = i7;
						break;

					case 6: // '\006'
						k1 = i7;
						break;

					case 7: // '\007'
						l1 = i7;
						break;

					case 8: // '\b'
						i2 = i7;
						break;

					case 9: // '\t'
						j2 = i7;
						break;

					case 10: // '\n'
						k2 = i7;
						break;

					case 11: // '\013'
						l2 = i7;
						break;

					case 12: // '\f'
						i3 = i7;
						break;

					case 13: // '\r'
						j3 = i7;
						break;

					case 14: // '\016'
						k3 = i7;
						break;

					case 15: // '\017'
						l3 = i7;
						break;

					case 16: // '\020'
						i4 = i7;
						break;

					case 17: // '\021'
						j4 = i7;
						break;

					case 18: // '\022'
						k4 = i7;
						break;

					case 19: // '\023'
						l4 = i7;
						break;

					case 20: // '\024'
						i5 = i7;
						break;

					case 21: // '\025'
						j5 = i7;
						break;

					case 22: // '\026'
						k5 = i7;
						break;

					case 23: // '\027'
						l5 = i7;
						break;

					case 24: // '\030'
						i6 = i7;
						break;
				}
			}
		}

		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5, k5, l5, i6, 0, 0, 0, 0));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 231);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook2(L1PcInstance pc, L1ItemInstance l1iteminstance) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		for (int j6 = 129; j6 <= 176; j6++) {
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = null;
			if (Config.CLIENT_LANGUAGE == 3) {
				s1 = (new StringBuilder()).append("精靈水晶(").append(l1skills.getName()).append(")").toString();
			}
			else if (Config.CLIENT_LANGUAGE == 5) {
				s1 = (new StringBuilder()).append("精灵水晶(").append(l1skills.getName()).append(")").toString();
			}
			else {
				s1 = (new StringBuilder()).append("精霊の水晶(").append(l1skills.getName()).append(")").toString();
			}
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				if (!pc.isGm() && (l1skills.getAttr() != 0) && (pc.getElfAttr() != l1skills.getAttr())) {
					if ((pc.getElfAttr() == 0) || (pc.getElfAttr() == 1) || (pc.getElfAttr() == 2) || (pc.getElfAttr() == 4)
							|| (pc.getElfAttr() == 8)) { // 属性値が異常な場合は全属性を覚えられるようにしておく
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}
				}
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
					case 1: // '\001'
						j = i7;
						break;

					case 2: // '\002'
						k = i7;
						break;

					case 3: // '\003'
						l = i7;
						break;

					case 4: // '\004'
						i1 = i7;
						break;

					case 5: // '\005'
						j1 = i7;
						break;

					case 6: // '\006'
						k1 = i7;
						break;

					case 7: // '\007'
						l1 = i7;
						break;

					case 8: // '\b'
						i2 = i7;
						break;

					case 9: // '\t'
						j2 = i7;
						break;

					case 10: // '\n'
						k2 = i7;
						break;

					case 11: // '\013'
						l2 = i7;
						break;

					case 12: // '\f'
						i3 = i7;
						break;

					case 13: // '\r'
						j3 = i7;
						break;

					case 14: // '\016'
						k3 = i7;
						break;

					case 15: // '\017'
						l3 = i7;
						break;

					case 16: // '\020'
						i4 = i7;
						break;

					case 17: // '\021'
						j4 = i7;
						break;

					case 18: // '\022'
						k4 = i7;
						break;

					case 19: // '\023'
						l4 = i7;
						break;

					case 20: // '\024'
						i5 = i7;
						break;

					case 21: // '\025'
						j5 = i7;
						break;

					case 22: // '\026'
						k5 = i7;
						break;

					case 23: // '\027'
						l5 = i7;
						break;

					case 24: // '\030'
						i6 = i7;
						break;
				}
			}
		}

		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5, k5, l5, i6, 0, 0, 0, 0));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook3(L1PcInstance pc, L1ItemInstance l1iteminstance, ClientThread clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		for (int j6 = 87; j6 <= 91; j6++) {
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(j6);

			String s1 = null;
			if (Config.CLIENT_LANGUAGE == 3) {
				s1 = (new StringBuilder()).append("技術書(").append(l1skills.getName()).append(")").toString();
			}
			else if (Config.CLIENT_LANGUAGE == 5) {
				s1 = (new StringBuilder()).append("技术书(").append(l1skills.getName()).append(")").toString();
			}
			else {
				s1 = (new StringBuilder()).append("技術書(").append(l1skills.getName()).append(")").toString();
			}
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
					case 1: // '\001'
						j = i7;
						break;

					case 2: // '\002'
						k = i7;
						break;

					case 3: // '\003'
						l = i7;
						break;

					case 4: // '\004'
						i1 = i7;
						break;

					case 5: // '\005'
						j1 = i7;
						break;

					case 6: // '\006'
						k1 = i7;
						break;

					case 7: // '\007'
						l1 = i7;
						break;

					case 8: // '\b'
						i2 = i7;
						break;

					case 9: // '\t'
						j2 = i7;
						break;

					case 10: // '\n'
						k2 = i7;
						break;

					case 11: // '\013'
						l2 = i7;
						break;

					case 12: // '\f'
						i3 = i7;
						break;

					case 13: // '\r'
						j3 = i7;
						break;

					case 14: // '\016'
						k3 = i7;
						break;

					case 15: // '\017'
						l3 = i7;
						break;

					case 16: // '\020'
						i4 = i7;
						break;

					case 17: // '\021'
						j4 = i7;
						break;

					case 18: // '\022'
						k4 = i7;
						break;

					case 19: // '\023'
						l4 = i7;
						break;

					case 20: // '\024'
						i5 = i7;
						break;

					case 21: // '\025'
						j5 = i7;
						break;

					case 22: // '\026'
						k5 = i7;
						break;

					case 23: // '\027'
						l5 = i7;
						break;

					case 24: // '\030'
						i6 = i7;
						break;
				}
			}
		}

		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5, k5, l5, i6, 0, 0, 0, 0));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook4(L1PcInstance pc, L1ItemInstance l1iteminstance, ClientThread clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		for (int j6 = 113; j6 < 121; j6++) {
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = null;
			if (Config.CLIENT_LANGUAGE == 3) {
				s1 = (new StringBuilder()).append("魔法書(").append(l1skills.getName()).append(")").toString();
			}
			else if (Config.CLIENT_LANGUAGE == 5) {
				s1 = (new StringBuilder()).append("魔法书(").append(l1skills.getName()).append(")").toString();
			}
			else {
				s1 = (new StringBuilder()).append("魔法書(").append(l1skills.getName()).append(")").toString();
			}
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
					case 1: // '\001'
						j = i7;
						break;

					case 2: // '\002'
						k = i7;
						break;

					case 3: // '\003'
						l = i7;
						break;

					case 4: // '\004'
						i1 = i7;
						break;

					case 5: // '\005'
						j1 = i7;
						break;

					case 6: // '\006'
						k1 = i7;
						break;

					case 7: // '\007'
						l1 = i7;
						break;

					case 8: // '\b'
						i2 = i7;
						break;

					case 9: // '\t'
						j2 = i7;
						break;

					case 10: // '\n'
						k2 = i7;
						break;

					case 11: // '\013'
						l2 = i7;
						break;

					case 12: // '\f'
						i3 = i7;
						break;

					case 13: // '\r'
						j3 = i7;
						break;

					case 14: // '\016'
						k3 = i7;
						break;

					case 15: // '\017'
						l3 = i7;
						break;

					case 16: // '\020'
						i4 = i7;
						break;

					case 17: // '\021'
						j4 = i7;
						break;

					case 18: // '\022'
						k4 = i7;
						break;

					case 19: // '\023'
						l4 = i7;
						break;

					case 20: // '\024'
						i5 = i7;
						break;

					case 21: // '\025'
						j5 = i7;
						break;

					case 22: // '\026'
						k5 = i7;
						break;

					case 23: // '\027'
						l5 = i7;
						break;

					case 24: // '\030'
						i6 = i7;
						break;
				}
			}
		}

		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5, k5, l5, i6, 0, 0, 0, 0));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook5(L1PcInstance pc, L1ItemInstance l1iteminstance, ClientThread clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		int i8 = 0;
		int j8 = 0;
		int k8 = 0;
		int l8 = 0;
		for (int j6 = 181; j6 <= 195; j6++) {
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = null;
			if (Config.CLIENT_LANGUAGE == 3) {
				s1 = (new StringBuilder()).append("龍騎士書板(").append(l1skills.getName()).append(")").toString();
			}
			else if (Config.CLIENT_LANGUAGE == 5) {
				s1 = (new StringBuilder()).append("龙骑士书板(").append(l1skills.getName()).append(")").toString();
			}
			else {
				s1 = (new StringBuilder()).append("ドラゴンナイトの書板（").append(l1skills.getName()).append("）").toString();
			}
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
					case 1: // '\001'
						j = i7;
						break;

					case 2: // '\002'
						k = i7;
						break;

					case 3: // '\003'
						l = i7;
						break;

					case 4: // '\004'
						i1 = i7;
						break;

					case 5: // '\005'
						j1 = i7;
						break;

					case 6: // '\006'
						k1 = i7;
						break;

					case 7: // '\007'
						l1 = i7;
						break;

					case 8: // '\b'
						i2 = i7;
						break;

					case 9: // '\t'
						j2 = i7;
						break;

					case 10: // '\n'
						k2 = i7;
						break;

					case 11: // '\013'
						l2 = i7;
						break;

					case 12: // '\f'
						i3 = i7;
						break;

					case 13: // '\r'
						j3 = i7;
						break;

					case 14: // '\016'
						k3 = i7;
						break;

					case 15: // '\017'
						l3 = i7;
						break;

					case 16: // '\020'
						i4 = i7;
						break;

					case 17: // '\021'
						j4 = i7;
						break;

					case 18: // '\022'
						k4 = i7;
						break;

					case 19: // '\023'
						l4 = i7;
						break;

					case 20: // '\024'
						i5 = i7;
						break;

					case 21: // '\025'
						j5 = i7;
						break;

					case 22: // '\026'
						k5 = i7;
						break;

					case 23: // '\027'
						l5 = i7;
						break;

					case 24: // '\030'
						i6 = i7;
						break;

					case 25: // '\031'
						j8 = i7;
						break;

					case 26: // '\032'
						k8 = i7;
						break;

					case 27: // '\033'
						l8 = i7;
						break;
					case 28: // '\034'
						i8 = i7;
						break;
				}
			}
		}

		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5, k5, l5, i6, j8, k8, l8, i8));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook6(L1PcInstance pc, L1ItemInstance l1iteminstance, ClientThread clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		int i8 = 0;
		int j8 = 0;
		int k8 = 0;
		int l8 = 0;
		for (int j6 = 201; j6 <= 220; j6++) {
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = null;
			if (Config.CLIENT_LANGUAGE == 3) {
				s1 = (new StringBuilder()).append("記憶水晶(").append(l1skills.getName()).append(")").toString();
			}
			else if (Config.CLIENT_LANGUAGE == 5) {
				s1 = (new StringBuilder()).append("记忆水晶(").append(l1skills.getName()).append(")").toString();
			}
			else {
				s1 = (new StringBuilder()).append("記憶の水晶(").append(l1skills.getName()).append("）").toString();
			}
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
					case 1: // '\001'
						j = i7;
						break;

					case 2: // '\002'
						k = i7;
						break;

					case 3: // '\003'
						l = i7;
						break;

					case 4: // '\004'
						i1 = i7;
						break;

					case 5: // '\005'
						j1 = i7;
						break;

					case 6: // '\006'
						k1 = i7;
						break;

					case 7: // '\007'
						l1 = i7;
						break;

					case 8: // '\b'
						i2 = i7;
						break;

					case 9: // '\t'
						j2 = i7;
						break;

					case 10: // '\n'
						k2 = i7;
						break;

					case 11: // '\013'
						l2 = i7;
						break;

					case 12: // '\f'
						i3 = i7;
						break;

					case 13: // '\r'
						j3 = i7;
						break;

					case 14: // '\016'
						k3 = i7;
						break;

					case 15: // '\017'
						l3 = i7;
						break;

					case 16: // '\020'
						i4 = i7;
						break;

					case 17: // '\021'
						j4 = i7;
						break;

					case 18: // '\022'
						k4 = i7;
						break;

					case 19: // '\023'
						l4 = i7;
						break;

					case 20: // '\024'
						i5 = i7;
						break;

					case 21: // '\025'
						j5 = i7;
						break;

					case 22: // '\026'
						k5 = i7;
						break;

					case 23: // '\027'
						l5 = i7;
						break;

					case 24: // '\030'
						i6 = i7;
						break;

					case 25: // '\031'
						j8 = i7;
						break;

					case 26: // '\032'
						k8 = i7;
						break;

					case 27: // '\033'
						l8 = i7;
						break;
					case 28: // '\034'
						i8 = i7;
						break;
				}
			}
		}

		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5, k5, l5, i6, j8, k8, l8, i8));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private int doWandAction(L1PcInstance user, L1Object target) {
		if (user.getId() == target.getId()) {
			return 0; // 目標為自身
		}
		if (user.glanceCheck(target.getX(), target.getY()) == false) {
			return 0; // 有障礙物
		}

		// XXX 適当なダメージ計算、要修正
		int dmg = (Random.nextInt(11) - 5) + user.getStr();
		dmg = Math.max(1, dmg);

		if (target instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) target;
			if (pc.getMap().isSafetyZone(pc.getLocation()) || user.checkNonPvP(user, pc)) {
				return 0;
			}
			if ((pc.hasSkillEffect(50) == true) || (pc.hasSkillEffect(78) == true) || (pc.hasSkillEffect(157) == true)) {
				return 0;
			}

			int newHp = pc.getCurrentHp() - dmg;
			if (newHp > 0) {
				pc.setCurrentHp(newHp);
			}
			else if ((newHp <= 0) && pc.isGm()) {
				pc.setCurrentHp(pc.getMaxHp());
			}
			else if ((newHp <= 0) && !pc.isGm()) {
				pc.death(user);
			}
			return dmg;
		}
		else if (target instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) target;
			mob.receiveDamage(user, dmg);
			return dmg;
		}
		return 0;
	}

	private void polyAction(L1PcInstance attacker, L1Character cha) {
		boolean isSameClan = false;
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if ((pc.getClanid() != 0) && (attacker.getClanid() == pc.getClanid())) { // 目標為盟友
				isSameClan = true;
			}
		}
		if ((attacker.getId() != cha.getId()) && !isSameClan) { // 非自身及盟友
			int probability = 3 * (attacker.getLevel() - cha.getLevel()) + 100 - cha.getMr();
			int rnd = Random.nextInt(100) + 1;
			if (rnd > probability) {
				attacker.sendPackets(new S_ServerMessage(79));
				return;
			}
		}

		int[] polyArray = {
				29, 945, 947, 979, 1037, 1039, 3860, 3861, 3862, 3863, 3864,
				3865, 3904, 3906, 95, 146, 2374, 2376, 2377, 2378, 3866, 3867,
				3868, 3869, 3870, 3871, 3872, 3873, 3874, 3875, 3876
		};

		int pid = Random.nextInt(polyArray.length);
		int polyId = polyArray[pid];

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int awakeSkillId = pc.getAwakeSkillId();
			if ((awakeSkillId == AWAKEN_ANTHARAS) || (awakeSkillId == AWAKEN_FAFURION) || (awakeSkillId == AWAKEN_VALAKAS)) {
				if (attacker.getId() == pc.getId()) {
					attacker.sendPackets(new S_ServerMessage(1384)); // 目前狀態中無法變身。
				} else {
					attacker.sendPackets(new S_ServerMessage(79));
				}
				return;
			}

			if (pc.getInventory().checkEquipped(20281)) { // 裝備變形控制戒指
				pc.sendPackets(new S_ShowPolyList(pc.getId()));
				if (!pc.isShapeChange()) {
					pc.setShapeChange(true);
				}
			}
			else {
				if (attacker.getId() != pc.getId()) {
					pc.sendPackets(new S_ServerMessage(241, attacker.getName())); // %0%s 把你變身。
				}
				L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
				L1PolyMorph.doPoly(pc, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
			}
		}
		else if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) cha;
			if (mob.getLevel() < 50) {
				int npcId = mob.getNpcTemplate().get_npcId();
				if ((npcId != 45338) && (npcId != 45370) && (npcId != 45456 // クロコダイル、バンディットボス、ネクロマンサー
						) && (npcId != 45464) && (npcId != 45473) && (npcId != 45488 // セマ、バルタザール、カスパー
						) && (npcId != 45497) && (npcId != 45516) && (npcId != 45529 // メルキオール、イフリート、ドレイク(DV)
						) && (npcId != 45458)) { // ドレイク(船長)
					L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
					L1PolyMorph.doPoly(mob, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC);
				}
			}
		}
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			}
			else { // 持てない場合は地面に落とす 処理のキャンセルはしない（不正防止）
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0を手に入れました。
			return true;
		}
		else {
			return false;
		}
	}

	private void useToiTeleportAmulet(L1PcInstance pc, int itemId, L1ItemInstance item) {
		boolean isTeleport = false;
		if ((itemId == 40289) || (itemId == 40293)) { // 11,51Famulet
			if ((pc.getX() >= 32816) && (pc.getX() <= 32821) && (pc.getY() >= 32778) && (pc.getY() <= 32783) && (pc.getMapId() == 101)) {
				isTeleport = true;
			}
		}
		else if ((itemId == 40290) || (itemId == 40294)) { // 21,61Famulet
			if ((pc.getX() >= 32815) && (pc.getX() <= 32820) && (pc.getY() >= 32815) && (pc.getY() <= 32820) && (pc.getMapId() == 101)) {
				isTeleport = true;
			}
		}
		else if ((itemId == 40291) || (itemId == 40295)) { // 31,71Famulet
			if ((pc.getX() >= 32779) && (pc.getX() <= 32784) && (pc.getY() >= 32778) && (pc.getY() <= 32783) && (pc.getMapId() == 101)) {
				isTeleport = true;
			}
		}
		else if ((itemId == 40292) || (itemId == 40296)) { // 41,81Famulet
			if ((pc.getX() >= 32779) && (pc.getX() <= 32784) && (pc.getY() >= 32815) && (pc.getY() <= 32820) && (pc.getMapId() == 101)) {
				isTeleport = true;
			}
		}
		else if (itemId == 40297) { // 91Famulet
			if ((pc.getX() >= 32706) && (pc.getX() <= 32710) && (pc.getY() >= 32909) && (pc.getY() <= 32913) && (pc.getMapId() == 190)) {
				isTeleport = true;
			}
		}

		if (isTeleport) {
			L1Teleport.teleport(pc, item.getItem().get_locx(), item.getItem().get_locy(), item.getItem().get_mapid(), 5, true);
		}
		else {
			pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
		}
	}

	private boolean writeLetter(int itemId, L1PcInstance pc, int letterCode, String letterReceiver, byte[] letterText) {

		int newItemId = 0;
		if (itemId == 40310) {
			newItemId = 49016;
		}
		else if (itemId == 40730) {
			newItemId = 49020;
		}
		else if (itemId == 40731) {
			newItemId = 49022;
		}
		else if (itemId == 40732) {
			newItemId = 49024;
		}
		L1ItemInstance item = ItemTable.getInstance().createItem(newItemId);
		if (item == null) {
			return false;
		}
		item.setCount(1);

		if (sendLetter(pc, letterReceiver, item, true)) {
			saveLetter(item.getId(), letterCode, pc.getName(), letterReceiver, letterText);
		}
		else {
			return false;
		}
		return true;
	}

	private boolean writeClanLetter(int itemId, L1PcInstance pc, int letterCode, String letterReceiver, byte[] letterText) {
		L1Clan targetClan = null;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getClanName().toLowerCase().equals(letterReceiver.toLowerCase())) {
				targetClan = clan;
				break;
			}
		}
		if (targetClan == null) {
			pc.sendPackets(new S_ServerMessage(434)); // 受信者がいません。
			return false;
		}

		String memberName[] = targetClan.getAllMembers();
		for (String element : memberName) {
			L1ItemInstance item = ItemTable.getInstance().createItem(49016);
			if (item == null) {
				return false;
			}
			item.setCount(1);
			if (sendLetter(pc, element, item, false)) {
				saveLetter(item.getId(), letterCode, pc.getName(), element, letterText);
			}
		}
		return true;
	}

	private boolean sendLetter(L1PcInstance pc, String name, L1ItemInstance item, boolean isFailureMessage) {
		L1PcInstance target = L1World.getInstance().getPlayer(name);
		if (target != null) {
			if (target.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
				target.getInventory().storeItem(item);
				target.sendPackets(new S_SkillSound(target.getId(), 1091));
				target.sendPackets(new S_ServerMessage(428)); // 手紙が届きました。
			}
			else {
				if (isFailureMessage) {
					// 相手のアイテムが重すぎるため、これ以上あげられません。
					pc.sendPackets(new S_ServerMessage(942));
				}
				return false;
			}
		}
		else {
			if (CharacterTable.doesCharNameExist(name)) {
				try {
					int targetId = CharacterTable.getInstance().restoreCharacter(name).getId();
					CharactersItemStorage storage = CharactersItemStorage.create();
					if (storage.getItemCount(targetId) < 180) {
						storage.storeItem(targetId, item);
					}
					else {
						if (isFailureMessage) {
							// 相手のアイテムが重すぎるため、これ以上あげられません。
							pc.sendPackets(new S_ServerMessage(942));
						}
						return false;
					}
				}
				catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
			else {
				if (isFailureMessage) {
					pc.sendPackets(new S_ServerMessage(109, name)); // %0という名前の人はいません。
				}
				return false;
			}
		}
		return true;
	}

	private void saveLetter(int itemObjectId, int code, String sender, String receiver, byte[] text) {
		// 日付を取得する
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
		TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		String date = sdf.format(Calendar.getInstance(tz).getTime());

		// subjectとcontentの区切り(0x00 0x00)位置を見つける
		int spacePosition1 = 0;
		int spacePosition2 = 0;
		for (int i = 0; i < text.length; i += 2) {
			if ((text[i] == 0) && (text[i + 1] == 0)) {
				if (spacePosition1 == 0) {
					spacePosition1 = i;
				}
				else if ((spacePosition1 != 0) && (spacePosition2 == 0)) {
					spacePosition2 = i;
					break;
				}
			}
		}

		// letterテーブルに書き込む
		int subjectLength = spacePosition1 + 2;
		int contentLength = spacePosition2 - spacePosition1;
		if (contentLength <= 0) {
			contentLength = 1;
		}
		byte[] subject = new byte[subjectLength];
		byte[] content = new byte[contentLength];
		System.arraycopy(text, 0, subject, 0, subjectLength);
		System.arraycopy(text, subjectLength, content, 0, contentLength);
		LetterTable.getInstance().writeLetter(itemObjectId, code, sender, receiver, date, 0, subject, content);
	}

	private boolean withdrawPet(L1PcInstance pc, int itemObjectId) {
		if (!pc.getMap().isTakePets()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1ここでは使えません。
			return false;
		}

		int petCost = 0;
		Object[] petList = pc.getPetList().values().toArray();
		for (Object pet : petList) {
			if (pet instanceof L1PetInstance) {
				if (((L1PetInstance) pet).getItemObjId() == itemObjectId) { // 既に引き出しているペット
					return false;
				}
			}
			petCost += ((L1NpcInstance) pet).getPetcost();
		}
		int charisma = pc.getCha();
		if (pc.isCrown()) { // 君主
			charisma += 6;
		}
		else if (pc.isElf()) { // エルフ
			charisma += 12;
		}
		else if (pc.isWizard()) { // WIZ
			charisma += 6;
		}
		else if (pc.isDarkelf()) { // DE
			charisma += 6;
		}
		else if (pc.isDragonKnight()) { // ドラゴンナイト
			charisma += 6;
		}
		else if (pc.isIllusionist()) { // イリュージョニスト
			charisma += 6;
		}
		charisma -= petCost;
		int petCount = charisma / 6;
		if (petCount <= 0) {
			pc.sendPackets(new S_ServerMessage(489)); // 引き取ろうとするペットが多すぎます。
			return false;
		}

		L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
		if (l1pet != null) {
			L1Npc npcTemp = NpcTable.getInstance().getTemplate(l1pet.get_npcid());
			L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
			pet.setPetcost(6);
		}
		return true;
	}

	private void startFishing(L1PcInstance pc, int itemId, int fishX, int fishY) {
		if ((pc.getMapId() != 5300) && (pc.getMapId() != 5301)) {
			// 無法在這個地區使用釣竿。
			pc.sendPackets(new S_ServerMessage(1138));
			return;
		}
		if (pc.getTempCharGfx() != pc.getClassId()) {
			// 這裡不可以變身。
			pc.sendPackets(new S_ServerMessage(1170));
			return;
		}

		int rodLength = 6;

		if (pc.getMap().isFishingZone(fishX, fishY)) {
			if (pc.getMap().isFishingZone(fishX + 1, fishY) && pc.getMap().isFishingZone(fishX - 1, fishY)
					&& pc.getMap().isFishingZone(fishX, fishY + 1) && pc.getMap().isFishingZone(fishX, fishY - 1)) {
				if ((fishX > pc.getX() + rodLength) || (fishX < pc.getX() - rodLength)) {
					pc.sendPackets(new S_ServerMessage(1138));
				}
				else if ((fishY > pc.getY() + rodLength) || (fishY < pc.getY() - rodLength)) {
					pc.sendPackets(new S_ServerMessage(1138));
				}
				else if (pc.getInventory().consumeItem(47103, 1)) { // 新鮮的餌
					pc.setFishX(fishX);
					pc.setFishY(fishY);
					pc.sendPackets(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX, fishY));
					pc.broadcastPacket(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX, fishY));
					pc.setFishing(true);
					long time = System.currentTimeMillis() + 10000 + Random.nextInt(5) * 1000;
					pc.setFishingTime(time);
					FishingTimeController.getInstance().addMember(pc);
				}
				else {
					// 釣魚需要有餌。
					pc.sendPackets(new S_ServerMessage(1137));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1138));
			}
		}
		else {
			pc.sendPackets(new S_ServerMessage(1138));
		}
	}

	private void useResolvent(L1PcInstance pc, L1ItemInstance item, L1ItemInstance resolvent) {
		if ((item == null) || (resolvent == null)) {
			pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
			return;
		}
		if ((item.getItem().getType2() == 1) || (item.getItem().getType2() == 2)) { // 武器・防具
			if (item.getEnchantLevel() != 0) { // 強化済み
				pc.sendPackets(new S_ServerMessage(1161)); // 溶解できません。
				return;
			}
			if (item.isEquipped()) { // 装備中
				pc.sendPackets(new S_ServerMessage(1161)); // 溶解できません。
				return;
			}
		}
		int crystalCount = ResolventTable.getInstance().getCrystalCount(item.getItem().getItemId());
		if (crystalCount == 0) {
			pc.sendPackets(new S_ServerMessage(1161)); // 溶解できません。
			return;
		}

		int rnd = Random.nextInt(100) + 1;
		if ((rnd >= 1) && (rnd <= 50)) {
			crystalCount = 0;
			pc.sendPackets(new S_ServerMessage(158, item.getName())); // \f1%0が蒸発してなくなりました。
		}
		else if ((rnd >= 51) && (rnd <= 90)) {
			crystalCount *= 1;
		}
		else if ((rnd >= 91) && (rnd <= 100)) {
			crystalCount *= 1.5;
			pc.getInventory().storeItem(41246, (int) (crystalCount * 1.5));
		}
		if (crystalCount != 0) {
			L1ItemInstance crystal = ItemTable.getInstance().createItem(41246);
			crystal.setCount(crystalCount);
			if (pc.getInventory().checkAddItem(crystal, 1) == L1Inventory.OK) {
				pc.getInventory().storeItem(crystal);
				pc.sendPackets(new S_ServerMessage(403, crystal.getLogName())); // %0を手に入れました。
			}
			else { // 持てない場合は地面に落とす 處理のキャンセルはしない（不正防止）
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(crystal);
			}
		}
		pc.getInventory().removeItem(item, 1);
		pc.getInventory().removeItem(resolvent, 1);
	}

	private void makeCooking(L1PcInstance pc, int cookNo) {
		boolean isNearFire = false;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 3)) {
			if (obj instanceof L1EffectInstance) {
				L1EffectInstance effect = (L1EffectInstance) obj;
				if (effect.getGfxId() == 5943) {
					isNearFire = true;
					break;
				}
			}
		}
		if (!isNearFire) {
			pc.sendPackets(new S_ServerMessage(1160)); // 料理には焚き火が必要です。
			return;
		}
		if (pc.getMaxWeight() <= pc.getInventory().getWeight()) {
			pc.sendPackets(new S_ServerMessage(1103)); // アイテムが重すぎて、料理できません。
			return;
		}
		if (pc.hasSkillEffect(COOKING_NOW)) {
			return;
		}
		pc.setSkillEffect(COOKING_NOW, 3 * 1000);

		int chance = Random.nextInt(100) + 1;
		if (cookNo == 0) { // フローティングアイステーキ
			if (pc.getInventory().checkItem(40057, 1)) {
				pc.getInventory().consumeItem(40057, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 41277, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 41285, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 1) { // ベアーステーキ
			if (pc.getInventory().checkItem(41275, 1)) {
				pc.getInventory().consumeItem(41275, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 41278, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 41286, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 2) { // ナッツ餅
			if (pc.getInventory().checkItem(41263, 1) && pc.getInventory().checkItem(41265, 1)) {
				pc.getInventory().consumeItem(41263, 1);
				pc.getInventory().consumeItem(41265, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 41279, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 41287, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 3) { // 蟻脚のチーズ焼き
			if (pc.getInventory().checkItem(41274, 1) && pc.getInventory().checkItem(41267, 1)) {
				pc.getInventory().consumeItem(41274, 1);
				pc.getInventory().consumeItem(41267, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 41280, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 41288, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 4) { // フルーツサラダ
			if (pc.getInventory().checkItem(40062, 1) && pc.getInventory().checkItem(40069, 1) && pc.getInventory().checkItem(40064, 1)) {
				pc.getInventory().consumeItem(40062, 1);
				pc.getInventory().consumeItem(40069, 1);
				pc.getInventory().consumeItem(40064, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 41281, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 41289, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 5) { // フルーツ甘酢あんかけ
			if (pc.getInventory().checkItem(40056, 1) && pc.getInventory().checkItem(40060, 1) && pc.getInventory().checkItem(40061, 1)) {
				pc.getInventory().consumeItem(40056, 1);
				pc.getInventory().consumeItem(40060, 1);
				pc.getInventory().consumeItem(40061, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 41282, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 41290, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 6) { // 猪肉の串焼き
			if (pc.getInventory().checkItem(41276, 1)) {
				pc.getInventory().consumeItem(41276, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 41283, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 41291, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 7) { // キノコスープ
			if (pc.getInventory().checkItem(40499, 1) && pc.getInventory().checkItem(40060, 1)) {
				pc.getInventory().consumeItem(40499, 1);
				pc.getInventory().consumeItem(40060, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 41284, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 41292, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 8) { // キャビアカナッペ
			if (pc.getInventory().checkItem(49040, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49040, 1);
				pc.getInventory().consumeItem(49048, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49049, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49057, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 9) { // アリゲーターステーキ
			if (pc.getInventory().checkItem(49041, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49041, 1);
				pc.getInventory().consumeItem(49048, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49050, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49058, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 10) { // タートルドラゴンの菓子
			if (pc.getInventory().checkItem(49042, 1) && pc.getInventory().checkItem(41265, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49042, 1);
				pc.getInventory().consumeItem(41265, 1);
				pc.getInventory().consumeItem(49048, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49051, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49059, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 11) { // キウィパロット焼き
			if (pc.getInventory().checkItem(49043, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49043, 1);
				pc.getInventory().consumeItem(49048, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49052, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49060, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 12) { // スコーピオン焼き
			if (pc.getInventory().checkItem(49044, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49044, 1);
				pc.getInventory().consumeItem(49048, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49053, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49061, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 13) { // イレッカドムシチュー
			if (pc.getInventory().checkItem(49045, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49045, 1);
				pc.getInventory().consumeItem(49048, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49054, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49062, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 14) { // クモ脚の串焼き
			if (pc.getInventory().checkItem(49046, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49046, 1);
				pc.getInventory().consumeItem(49048, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49055, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49063, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 15) { // クラブスープ
			if (pc.getInventory().checkItem(49047, 1) && pc.getInventory().checkItem(40499, 1) && pc.getInventory().checkItem(49048, 1)) {
				pc.getInventory().consumeItem(49047, 1);
				pc.getInventory().consumeItem(40499, 1);
				pc.getInventory().consumeItem(49048, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49056, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49064, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 16) { // クラスタシアンのハサミ焼き
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49260, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49260, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49244, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49252, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 17) { // グリフォン焼き
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49261, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49261, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49245, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49253, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 18) { // コカトリスステーキ
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49262, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49262, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49246, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49254, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 19) { // タートルドラゴン焼き
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49263, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49263, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49247, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49255, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 20) { // レッサードラゴンの手羽先
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49264, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49264, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49248, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49256, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 21) { // ドレイク焼き
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49265, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49265, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49249, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49257, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 22) { // 深海魚のシチュー
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49266, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49266, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49250, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49258, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
		else if (cookNo == 23) { // バシリスクの卵スープ
			if (pc.getInventory().checkItem(49048, 1) && pc.getInventory().checkItem(49243, 1) && pc.getInventory().checkItem(49267, 1)) {
				pc.getInventory().consumeItem(49048, 1);
				pc.getInventory().consumeItem(49243, 1);
				pc.getInventory().consumeItem(49267, 1);
				if ((chance >= 1) && (chance <= 90)) {
					createNewItem(pc, 49251, 1);
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6392));
				}
				else if ((chance >= 91) && (chance <= 95)) {
					createNewItem(pc, 49259, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6390));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6390));
				}
				else if ((chance >= 96) && (chance <= 100)) {
					pc.sendPackets(new S_ServerMessage(1101)); // 料理が失敗しました。
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 6394));
				}
			}
			else {
				pc.sendPackets(new S_ServerMessage(1102)); // 料理の材料が足りません。
			}
		}
	}

	private void useFurnitureItem(L1PcInstance pc, int itemId, int itemObjectId) {
		if (!L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId())) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1ここでは使えません。
			return;
		}

		boolean isAppear = true;
		L1FurnitureInstance furniture = null;
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1FurnitureInstance) {
				furniture = (L1FurnitureInstance) l1object;
				if (furniture.getItemObjId() == itemObjectId) { // 既に引き出している家具
					isAppear = false;
					break;
				}
			}
		}

		if (isAppear) {
			if ((pc.getHeading() != 0) && (pc.getHeading() != 2)) {
				return;
			}
			int npcId = 0;
			if (itemId == 41383) { // ジャイアントアントソルジャーの剥製
				npcId = 80109;
			}
			else if (itemId == 41384) { // ベアーの剥製
				npcId = 80110;
			}
			else if (itemId == 41385) { // ラミアの剥製
				npcId = 80113;
			}
			else if (itemId == 41386) { // ブラックタイガーの剥製
				npcId = 80114;
			}
			else if (itemId == 41387) { // 鹿の剥製
				npcId = 80115;
			}
			else if (itemId == 41388) { // ハーピーの剥製
				npcId = 80124;
			}
			else if (itemId == 41389) { // ブロンズナイト
				npcId = 80118;
			}
			else if (itemId == 41390) { // ブロンズホース
				npcId = 80119;
			}
			else if (itemId == 41391) { // 燭台
				npcId = 80120;
			}
			else if (itemId == 41392) { // ティーテーブル
				npcId = 80121;
			}
			else if (itemId == 41393) { // 火鉢
				npcId = 80126;
			}
			else if (itemId == 41394) { // たいまつ
				npcId = 80125;
			}
			else if (itemId == 41395) { // 君主用のお立ち台
				npcId = 80111;
			}
			else if (itemId == 41396) { // 旗
				npcId = 80112;
			}
			else if (itemId == 41397) { // ティーテーブル用の椅子(右)
				npcId = 80116;
			}
			else if (itemId == 41398) { // ティーテーブル用の椅子(左)
				npcId = 80117;
			}
			else if (itemId == 41399) { // パーティション(右)
				npcId = 80122;
			}
			else if (itemId == 41400) { // パーティション(左)
				npcId = 80123;
			}

			try {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
				if (l1npc != null) {
					try {
						String s = l1npc.getImpl();
						Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
						Object aobj[] =
						{ l1npc };
						furniture = (L1FurnitureInstance) constructor.newInstance(aobj);
						furniture.setId(IdFactory.getInstance().nextId());
						furniture.setMap(pc.getMapId());
						if (pc.getHeading() == 0) {
							furniture.setX(pc.getX());
							furniture.setY(pc.getY() - 1);
						}
						else if (pc.getHeading() == 2) {
							furniture.setX(pc.getX() + 1);
							furniture.setY(pc.getY());
						}
						furniture.setHomeX(furniture.getX());
						furniture.setHomeY(furniture.getY());
						furniture.setHeading(0);
						furniture.setItemObjId(itemObjectId);

						L1World.getInstance().storeObject(furniture);
						L1World.getInstance().addVisibleObject(furniture);
						FurnitureSpawnTable.getInstance().insertFurniture(furniture);
					}
					catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
			catch (Exception exception) {}
		}
		else {
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
		}
	}

	// 傢俱移除魔杖
	private void useFurnitureRemovalWand(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackPacket s_attackPacket = new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackPacket);
		pc.broadcastPacket(s_attackPacket);
		int chargeCount = item.getChargeCount();
		if (chargeCount <= 0) {
			return;
		}

		L1Object target = L1World.getInstance().findObject(targetId);
		if ((target != null) && (target instanceof L1FurnitureInstance)) {
			L1FurnitureInstance furniture = (L1FurnitureInstance) target;
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
			item.setChargeCount(item.getChargeCount() - 1);
			pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}

	@Override
	public String getType() {
		return C_ITEM_USE;
	}
}
