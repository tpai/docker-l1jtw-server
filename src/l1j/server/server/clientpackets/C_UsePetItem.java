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

import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.PetItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1PetItem;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來使用寵物道具的封包
 */
public class C_UsePetItem extends ClientBasePacket {

	private static final String C_USE_PET_ITEM = "[C] C_UsePetItem";
	private static Logger _log = Logger.getLogger(C_UsePetItem.class.getName());

	public C_UsePetItem(byte abyte0[], ClientThread clientthread)
			throws Exception {
		super(abyte0);

		int data = readC();
		int petId = readD();
		int listNo = readC();

		L1PetInstance pet = (L1PetInstance) L1World.getInstance()
				.findObject(petId);
		L1PcInstance pc = clientthread.getActiveChar();

		if (pet == null && pc == null) {
			return;
		}
		L1ItemInstance item = pet.getInventory().getItems().get(listNo);
		if (item == null) {
			return;
		}

		if (item.getItem().getType2() == 0 // 種別：その他のアイテム
				&& item.getItem().getType() == 11) { // petitem
			int itemId = item.getItem().getItemId();
			if (itemId >= 40749 && itemId <= 40752
					|| itemId >= 40756 && itemId <= 40758) {
				usePetWeapon(pc, pet, item);
			} else if (itemId >= 40761 && itemId <= 40766) {
				usePetArmor(pc, pet, item);
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
			}
		} else {
			pc.sendPackets(new S_ServerMessage(79)); // \f1何も起きませんでした。
		}
	}

	private void usePetWeapon(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance weapon) {
		if (pet.getWeapon() == null) {
			setPetWeapon(pc, pet, weapon);
		} else { // 既に何かを装備している場合、前の装備をはずす
			if (pet.getWeapon().equals(weapon)) {
				removePetWeapon(pc, pet, pet.getWeapon());
			} else {
				removePetWeapon(pc, pet, pet.getWeapon());
				setPetWeapon(pc, pet, weapon);
			}
		}
	}

	private void usePetArmor(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance armor) {
		if (pet.getArmor() == null) {
			setPetArmor(pc, pet, armor);
		} else { // 既に何かを装備している場合、前の装備をはずす
			if (pet.getArmor().equals(armor)) {
				removePetArmor(pc, pet, pet.getArmor());
			} else {
				removePetArmor(pc, pet, pet.getArmor());
				setPetArmor(pc, pet, armor);
			}
		}
	}

	private void setPetWeapon(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.setHitByWeapon(petItem.getHitModifier());
		pet.setDamageByWeapon(petItem.getDamageModifier());
		pet.addStr(petItem.getAddStr());
		pet.addCon(petItem.getAddCon());
		pet.addDex(petItem.getAddDex());
		pet.addInt(petItem.getAddInt());
		pet.addWis(petItem.getAddWis());
		pet.addMaxHp(petItem.getAddHp());
		pet.addMaxMp(petItem.getAddMp());
		pet.addSp(petItem.getAddSp());
		pet.addMr(petItem.getAddMr());

		pet.setWeapon(weapon);
		weapon.setEquipped(true);
	}

	private void removePetWeapon(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.setHitByWeapon(0);
		pet.setDamageByWeapon(0);
		pet.addStr(-petItem.getAddStr());
		pet.addCon(-petItem.getAddCon());
		pet.addDex(-petItem.getAddDex());
		pet.addInt(-petItem.getAddInt());
		pet.addWis(-petItem.getAddWis());
		pet.addMaxHp(-petItem.getAddHp());
		pet.addMaxMp(-petItem.getAddMp());
		pet.addSp(-petItem.getAddSp());
		pet.addMr(-petItem.getAddMr());

		pet.setWeapon(null);
		weapon.setEquipped(false);
	}

	private void setPetArmor(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance armor) {
		int itemId = armor.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.addAc(petItem.getAddAc());
		pet.addStr(petItem.getAddStr());
		pet.addCon(petItem.getAddCon());
		pet.addDex(petItem.getAddDex());
		pet.addInt(petItem.getAddInt());
		pet.addWis(petItem.getAddWis());
		pet.addMaxHp(petItem.getAddHp());
		pet.addMaxMp(petItem.getAddMp());
		pet.addSp(petItem.getAddSp());
		pet.addMr(petItem.getAddMr());

		pet.setArmor(armor);
		armor.setEquipped(true);
	}

	private void removePetArmor(L1PcInstance pc, L1PetInstance pet,
			L1ItemInstance armor) {
		int itemId = armor.getItem().getItemId();
		L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
		if (petItem == null) {
			return;
		}

		pet.addAc(-petItem.getAddAc());
		pet.addStr(-petItem.getAddStr());
		pet.addCon(-petItem.getAddCon());
		pet.addDex(-petItem.getAddDex());
		pet.addInt(-petItem.getAddInt());
		pet.addWis(-petItem.getAddWis());
		pet.addMaxHp(-petItem.getAddHp());
		pet.addMaxMp(-petItem.getAddMp());
		pet.addSp(-petItem.getAddSp());
		pet.addMr(-petItem.getAddMr());

		pet.setArmor(null);
		armor.setEquipped(false);
	}

	@Override
	public String getType() {
		return C_USE_PET_ITEM;
	}
}
