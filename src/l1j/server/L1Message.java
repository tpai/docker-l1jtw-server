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
