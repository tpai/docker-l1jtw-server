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

import java.util.List;
import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.MailTable;
import l1j.server.server.templates.L1Mail;
import l1j.server.server.utils.collections.Lists;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_Mail extends ServerBasePacket {

	private static final String S_MAIL = "[S] S_Mail";

	private byte[] _byte = null;

	/**
	 * 
	 * //一般信件的標題 [Server] opcode = 48 3封 0000: [30][00 03][00][27 00 00
	 * 00][00][09][01][12][32 32 33 33 0...'.......2233 0010: 32 31 00] [31
	 * 00]00 [00] [28 00 00 00] [01] 09 01 12 32 21.1...(.......2 0020: 32 33 33
	 * 32 31 00 31 00 00 00 2a 00 00 00 00 09 23321.1...*..... 0030: 01 13 32 32
	 * 33 33 32 31 00 31 00 00 00 93 0a 00 ..223321.1......
	 * 
	 * [Server] opcode = 48 2封 0000: 30 /00 02/ 00/ 27 00 00 00/ 00/ 09 01 12 32
	 * 32 33 33 0...'.......2233 0010: 32 31 00 31 00 00 00 28 00 00 00 00 09 01
	 * 12 32 21.1...(.......2 0020: 32 33 33 32 31 00 31 00 00 00 96 3d c4 79 1a
	 * 4d 23321.1....=.y.M
	 */
	// 打開收信夾 ?封信件顯示標題
	public S_Mail(String receiverName, int type) {
		List<L1Mail> mails = Lists.newList();
		MailTable.getInstance();
		for (L1Mail mail : MailTable.getAllMail()) {
			if (mail.getReceiverName().equalsIgnoreCase(receiverName)) {
				if (mail.getType() == type) {
					mails.add(mail);
				}
			}
		}
		if (mails.isEmpty()) {
			return;
		}

		writeC(Opcodes.S_OPCODE_MAIL);
		writeC(type);
		writeH(mails.size());
		for (int i = 0; i < mails.size(); i++) {
			L1Mail mail = mails.get(i);
			writeD(mail.getId());
			writeC(mail.getReadStatus());

			StringTokenizer st = new StringTokenizer(mail.getDate(), "/"); // yy/mm/dd
			int size = st.countTokens();
			for (int j = 0; j < size; j++) {
				// XXX writeC(Year) writeC(Month) writeC(Day)
				writeC(Integer.parseInt(st.nextToken()));
			}
			writeS(mail.getSenderName());
			writeByte(mail.getSubject());
		}
	}

	/**
	 * //無法傳送信件 [Server] opcode = 48 0000: 30 20 00 45 54 fa 00 b5
	 */
	public S_Mail(int type) { // 受信者にメール通知
		writeC(Opcodes.S_OPCODE_MAIL);
		writeC(type);
	}

	/**
	 * //讀取一般信件 [Server] opcode = 48 0000: [30] [10] [29 00 00 00] [32 00] 00 00
	 * a4 cb 00 03 08 00 0.)...2.........
	 * 
	 * //信件存到保管箱 [Server] opcode = 48 0000: [30] [40] [2b 00 00 00] [01] 95
	 * 
	 */
	public S_Mail(int mailId, int type) {
		// 刪除信件
		// 0x30: 刪除一般 0x31:刪除血盟 0x32:?般存到保管箱 0x40:刪除保管箱
		if ((type == 0x30) || (type == 0x31) || (type == 0x32) || (type == 0x40)) {
			writeC(Opcodes.S_OPCODE_MAIL);
			writeC(type);
			writeD(mailId);
			writeC(1);
			return;
		}
		MailTable.getInstance();
		L1Mail mail = MailTable.getMail(mailId);
		if (mail != null) {
			writeC(Opcodes.S_OPCODE_MAIL);
			writeC(type);
			writeD(mail.getId());
			writeByte(mail.getContent());
		}
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
		return S_MAIL;
	}
}
