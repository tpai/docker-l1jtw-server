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
package l1j.server;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileLogFormatter extends Formatter {
	private static final String CRLF = "\r\n";

	private static final String $ = "\t";

	@Override
	public String format(LogRecord record) {
		StringBuffer output = new StringBuffer();
		output.append(record.getMillis());
		output.append($);
		output.append(record.getLevel().getName());
		output.append($);
		output.append(record.getThreadID());
		output.append($);
		output.append(record.getLoggerName());
		output.append($);
		output.append(record.getMessage());
		output.append(CRLF);
		return output.toString();
	}
}
