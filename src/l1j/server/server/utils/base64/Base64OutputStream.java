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
package l1j.server.server.utils.base64;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Base64 OutputStream
 */
public class Base64OutputStream extends OutputStream {

	private OutputStream outputStream = null;

	private int buffer = 0;

	private int bytecounter = 0;

	private int linecounter = 0;

	private int linelength = 0;

	public Base64OutputStream(OutputStream outputStream) {
		this(outputStream, 76);// 每 76個Byte換行
	}

	public Base64OutputStream(OutputStream outputStream, int wrapAt) {
		this.outputStream = outputStream;
		this.linelength = wrapAt;
	}

	public void write(int b) throws IOException {
		int value = (b & 0xFF) << (16 - (bytecounter * 8));
		buffer = buffer | value;
		bytecounter++;
		if (bytecounter == 3) {
			commit();
		}
	}

	public void close() throws IOException {
		commit();
		outputStream.close();
	}

	protected void commit() throws IOException {
		if (bytecounter > 0) {
			if (linelength > 0 && linecounter == linelength) {
				outputStream.write("\r\n".getBytes());
				linecounter = 0;
			}
			char b1 = Base64.Shared.chars.charAt((buffer << 8) >>> 26);
			char b2 = Base64.Shared.chars.charAt((buffer << 14) >>> 26);
			char b3 = (bytecounter < 2) ? Base64.Shared.pad
					: Base64.Shared.chars.charAt((buffer << 20) >>> 26);
			char b4 = (bytecounter < 3) ? Base64.Shared.pad
					: Base64.Shared.chars.charAt((buffer << 26) >>> 26);
			outputStream.write(b1);
			outputStream.write(b2);
			outputStream.write(b3);
			outputStream.write(b4);
			linecounter += 4;
			bytecounter = 0;
			buffer = 0;
		}
	}

}