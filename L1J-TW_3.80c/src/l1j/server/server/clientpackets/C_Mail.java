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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ClientThread;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.MailTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.identity.L1SystemMessageId;
import l1j.server.server.serverpackets.S_Mail;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Mail;
import l1j.server.server.utils.collections.Lists;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

/**
 * 處理收到由客戶端傳來信件的封包
 */
public class C_Mail extends ClientBasePacket {

	private static final String C_MAIL = "[C] C_Mail";

	private static Logger _log = Logger.getLogger(C_Mail.class.getName());

	private static int TYPE_NORMAL_MAIL = 0; // 一般

	private static int TYPE_CLAN_MAIL = 1; // 血盟

	private static int TYPE_MAIL_BOX = 2; // 保管箱

	public C_Mail(byte abyte0[], ClientThread client) {
		super(abyte0);
		
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		int type = readC();
		
		if ((type == 0x00) || (type == 0x01) || (type == 0x02)) { // 開啟
			pc.sendPackets(new S_Mail(pc , type));
		}
		else if ((type == 0x10) || (type == 0x11) || (type == 0x12)) { // 讀取
			int mailId = readD();
			MailTable.getInstance();
			L1Mail mail = MailTable.getMail(mailId);
			if (mail.getReadStatus() == 0) {
				MailTable.getInstance().setReadStatus(mailId);
			}
			pc.sendPackets(new S_Mail(mailId, type));
		}
		else if (type == 0x20) { // 一般信紙
			if (pc.getInventory().checkItem(40308, 50)) {
				pc.getInventory().consumeItem(40308, 50);
			} else {
				pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$189));
				return;
			}
			readH(); // 世界寄信次數紀錄
			String receiverName = readS();
			byte[] text = readByte();
			L1PcInstance receiver = L1World.getInstance().getPlayer(receiverName);
			
			if (receiver != null) { // 對方在線上
				if (getMailSizeByPc(receiver, TYPE_NORMAL_MAIL) >= 40) { 
					pc.sendPackets(new S_Mail(type, false));
					return;
				}
				
				/* 寄件備份*/
				int mailId = MailTable.getInstance().writeMail(TYPE_NORMAL_MAIL, receiverName, pc, text, pc.getId());
				pc.sendPackets(new S_Mail(receiver, mailId, true));
				
				int mailId2 = MailTable.getInstance().writeMail(TYPE_NORMAL_MAIL, receiverName, pc, text, receiver.getId());
				
				if (receiver.getOnlineStatus() == 1) {
					receiver.sendPackets(new S_Mail(pc, mailId2, false));
					receiver.sendPackets(new S_SkillSound(receiver.getId(), 1091));
				}
			} else { // 對方離線中
				try {
					L1PcInstance restorePc = CharacterTable.getInstance().restoreCharacter(receiverName);
					if (restorePc != null) {
						if (getMailSizeByPc(restorePc, TYPE_NORMAL_MAIL) >= 40) {
							pc.sendPackets(new S_Mail(type, false));
							return;
						}
						/* 寄件備份*/
						int mailId = MailTable.getInstance().writeMail(TYPE_NORMAL_MAIL, receiverName, pc, text, pc.getId());
						pc.sendPackets(new S_Mail(restorePc, mailId, true));
						
						MailTable.getInstance().writeMail(TYPE_NORMAL_MAIL, receiverName, pc, text, restorePc.getId());
					} else {
						pc.sendPackets(new S_ServerMessage(109, receiverName)); // %0という名前の人はいません。
					}
				}
				catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
			pc.sendPackets(new S_Mail(type, true));
		}
		else if (type == 0x21) { // 血盟信紙
			if (pc.getClanid() > 0) {
				pc.getInventory().consumeItem(40308, 50);
			} else {
				pc.sendPackets(new S_ServerMessage(L1SystemMessageId.$1262));
				return;
			}
			readH();
			String clanName = readS();
			byte[] text = readByte();
			L1Clan clan = L1World.getInstance().getClan(clanName);
			if (clan != null) {
				for (String name : clan.getAllMembers()) {
					L1PcInstance clanPc = L1World.getInstance().getPlayer(name);
					int size = getMailSizeByPc(clanPc, TYPE_CLAN_MAIL);
					if (size >= 50) {
						continue;
					}
					MailTable.getInstance().writeMail(TYPE_CLAN_MAIL, name, pc, text, clanPc.getId());
					if (clanPc != null) { // 在線上
						clanPc.sendPackets(new S_Mail(clanPc, TYPE_CLAN_MAIL));
						clanPc.sendPackets(new S_SkillSound(clanPc.getId(), 1091));
					}
				}
			}
		} else if ((type == 0x30) || (type == 0x31) || (type == 0x32)) { // 刪除
			int mailId = readD();
			MailTable.getInstance().deleteMail(mailId);
			pc.sendPackets(new S_Mail(mailId, type));
		} else if (type == 0x60) { // 多選刪除
			int count = readD();
			for (int i = 0; i < count; i++) {
				int mailId = readD();
				pc.sendPackets(new S_Mail(mailId, (MailTable.getMail(mailId).getType() + 0x30)));
				MailTable.getInstance().deleteMail(mailId);
			}
		} else if (type == 0x40) { // 保管箱儲存
			int mailId = readD();
			MailTable.getInstance().setMailType(mailId, TYPE_MAIL_BOX);
			pc.sendPackets(new S_Mail(mailId, type));
		}
	}

	private int getMailSizeByPc(L1PcInstance pc, int type) {
		List<L1Mail> mails = Lists.newList();
		MailTable.getInstance();
		for (L1Mail mail : MailTable.getAllMail()) {
			if (mail.getInBoxId() == pc.getId()) {
				if (mail.getType() == type) {
					mails.add(mail);
				}
			}
		}
		return mails.size();
	}

	@Override
	public String getType() {
		return C_MAIL;
	}
}
