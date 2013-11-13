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
package l1j.server.server.utils;

// 全部staticにしてもいいかもしれない
public class ByteArrayUtil {
	private final byte[] _byteArray;

	public ByteArrayUtil(byte[] byteArray) {
		_byteArray = byteArray;
	}

	public String getTerminatedString(int i) {
		StringBuffer stringbuffer = new StringBuffer();
		for (int j = i; j < _byteArray.length && _byteArray[j] != 0; j++) {
			stringbuffer.append((char) _byteArray[j]);
		}

		return stringbuffer.toString();
	}

	public String dumpToString() {
		StringBuffer stringbuffer = new StringBuffer();
		int j = 0;
		for (int k = 0; k < _byteArray.length; k++) {
			if (j % 16 == 0) {
				stringbuffer.append((new StringBuilder()).append(fillHex(k, 4))
						.append(": ").toString());
			}
			stringbuffer.append((new StringBuilder()).append(
					fillHex(_byteArray[k] & 0xff, 2)).append(" ").toString());
			if (++j != 16) {
				continue;
			}
			stringbuffer.append("   ");
			int i1 = k - 15;
			for (int l1 = 0; l1 < 16; l1++) {
				byte byte0 = _byteArray[i1++];
				if (byte0 > 31 && byte0 < 128) {
					stringbuffer.append((char) byte0);
				} else {
					stringbuffer.append('.');
				}
			}

			stringbuffer.append("\n");
			j = 0;
		}

		int l = _byteArray.length % 16;
		if (l > 0) {
			for (int j1 = 0; j1 < 17 - l; j1++) {
				stringbuffer.append("   ");
			}

			int k1 = _byteArray.length - l;
			for (int i2 = 0; i2 < l; i2++) {
				byte byte1 = _byteArray[k1++];
				if (byte1 > 31 && byte1 < 128) {
					stringbuffer.append((char) byte1);
				} else {
					stringbuffer.append('.');
				}
			}

			stringbuffer.append("\n");
		}
		return stringbuffer.toString();
	}

	private String fillHex(int i, int j) {
		String s = Integer.toHexString(i);
		for (int k = s.length(); k < j; k++) {
			s = (new StringBuilder()).append("0").append(s).toString();
		}

		return s;
	}
}
