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

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.MailTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Mail;
import l1j.server.server.utils.collections.Lists;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_Mail extends ServerBasePacket {

	private static final String S_MAIL = "[S] S_Mail";

	private byte[] _byte = null;

	/**
	 * 『來源:伺服器』<位址:186>{長度:216}(時間:1061159132)
     *  0000:  ba 00 08 00 63 78 00 00 01 d8 dc 25 52 01 ae a9    ....cx.....%R...
     *  0010:  ac f5 b9 d0 00 35 00 35 00 35 00 35 00 35 00 35    .....5.5.5.5.5.5
     *  0020:  00 00 00 65 78 00 00 01 c8 dd 25 52 01 ae a9 ac    ...ex.....%R....
     *  0030:  f5 b9 d0 00 32 00 32 00 32 00 32 00 32 00 32 00    ....2.2.2.2.2.2.
     *  0040:  32 00 32 00 32 00 00 00 eb 78 00 00 00 50 2f 3d    2.2.2....x...P/=
     *  0050:  52 00 ae a9 ac f5 b9 d0 00 33 00 33 00 33 00 00    R........3.3.3..
     *  0060:  00 ed 78 00 00 00 50 2f 3d 52 00 ae a9 ac f5 b9    ..x...P/=R......
     *  0070:  d0 00 33 00 33 00 00 00 ef 78 00 00 00 40 30 3d    ..3.3....x...@0=
     *  0080:  52 00 ae a9 ac f5 b9 d0 00 32 00 32 00 00 00 f1    R........2.2....
     *  0090:  78 00 00 00 40 30 3d 52 00 ae a9 ac f5 b9 d0 00    x...@0=R........
     *  00a0:  32 00 32 00 00 00 f3 78 00 00 00 d4 32 3d 52 00    2.2....x....2=R.
     *  00b0:  ae a9 ac f5 b9 d0 00 32 00 32 00 00 00 f5 78 00    .......2.2....x.
     *  00c0:  00 00 d0 36 3d 52 00 ae a9 ac f5 b9 d0 00 35 00    ...6=R........5.
     *  00d0:  35 00 00 00 45 1c c9 93                            5...E...
	 */
	// 打開收信夾 ?封信件顯示標題
	public S_Mail(L1PcInstance pc, int type) {
		List<L1Mail> mails = Lists.newList();
		MailTable.getInstance();
		for (L1Mail mail : MailTable.getAllMail()) {
			if(mail.getInBoxId() == pc.getId()){
				if (mail.getType() == type) {
					mails.add(mail);
				}
			}
		}

		writeC(Opcodes.S_OPCODE_MAIL);
		writeC(type);
		writeH(mails.size());
		
		if (mails.isEmpty()) {
			return;
		}
		
		for (int i = 0; i < mails.size(); i++) {
			L1Mail mail = mails.get(i);
			writeD(mail.getId());
			writeC(mail.getReadStatus());
			writeD((int) (mail.getDate().getTime() / 1000));
			writeC(mail.getSenderName().equalsIgnoreCase(pc.getName()) ? 1 : 0); // 寄件/備份
			writeS(mail.getSenderName().equalsIgnoreCase(pc.getName()) ? mail.getReceiverName() : mail.getSenderName());
			writeByte(mail.getSubject());
		}
	}
	
	/**
	 * <b>寄出信件</b>
	 * @param pc 寄出信件: 寄信人 , 寄件備份: 收信人<br>
	 * @param isDraft 是否是寄件備份 ? true:備份  , false:寄出
	 */
	public S_Mail(L1PcInstance pc, int mailId, boolean isDraft){
		MailTable.getInstance();
		L1Mail mail = MailTable.getMail(mailId);
		writeC(Opcodes.S_OPCODE_MAIL);
		writeC(0x50);
		writeD(mailId);
		writeC(isDraft ? 1 : 0);
		writeS(pc.getName());
		writeByte(mail.getSubject());
	}

	/**
	 * 寄信結果通知
	 * @param type 信件類別 
	 * @param isDelivered 寄出:1 ,失敗:0
	 */
	public S_Mail(int type, boolean isDelivered) {
		writeC(Opcodes.S_OPCODE_MAIL);
		writeC(type);
		writeC(isDelivered ? 1 : 0);
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
		// 0x30: 刪除一般 0x31:刪除血盟 0x32:一般存到保管箱 0x40:刪除保管箱
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
