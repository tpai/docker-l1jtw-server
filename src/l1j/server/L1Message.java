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

import java.util.ResourceBundle;

public class L1Message {

	private static L1Message _instance;
	ResourceBundle resource;
	
	private L1Message() {
		resource = ResourceBundle.getBundle("messages");
		initLocaleMessage();
	}

	public static L1Message getInstance() {
		if (_instance == null) {
			_instance = new L1Message();
		}
		return _instance;
	}
	
	public void initLocaleMessage() {
		_memoryUse = resource.getString("l1j.server.memoryUse");
	}
	
	
	public static String _memoryUse;
	
}
