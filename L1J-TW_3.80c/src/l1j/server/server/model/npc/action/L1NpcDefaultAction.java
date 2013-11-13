package l1j.server.server.model.npc.action;

import java.util.Map;
import java.util.logging.Logger;

import l1j.server.server.utils.collections.Maps;

public class L1NpcDefaultAction {
	static Logger _log = Logger.getLogger(L1NpcDefaultAction.class.getName());

	private static class Action {
		private int defaultAttack = 1; // 預設攻擊
		private int specialAttack = 0; // 特殊攻擊
		private int rangedAttack = 1; // 遠距攻擊
		private int status = 0; // 狀態
	}

	private static final Map<Integer, Action> _dataMap = Maps.newMap();

	private static final L1NpcDefaultAction _instance = new L1NpcDefaultAction();

	private L1NpcDefaultAction() {
		loadAction();
	}

	public static L1NpcDefaultAction getInstance() {
		return _instance;
	}

	public void loadAction() {
		int[] gfxid = null;
		Action action = null;

		/*******************************************************************************/

		gfxid = new int[] { 57, 3860, 3871 };

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 30; // 預設攻擊
			action.specialAttack = 0; // 特殊攻擊
			action.rangedAttack = 1; // 遠距攻擊
			action.status = 20; // 狀態
		}

		/*******************************************************************************/

		gfxid = new int[] { 816, 2284, 2323, 3105, 3126, 3137, 3140, 3142,
				3145, 3148, 3151, 3892, 3895, 3898, 3901, 4917,
				4918, 4919, 5879, 6087, 6140, 6145, 6150, 6155, 6160, 6269,
				6272, 6275, 6278, 6406
		};

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 1; // 預設攻擊
			action.specialAttack = 0; // 特殊攻擊
			action.rangedAttack = 1; // 遠距攻擊
			action.status = 20; // 狀態
		}

		/*******************************************************************************/

		gfxid = new int[] { 51, 110, 147, 2377, 4003 };

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 1; // 預設攻擊
			action.specialAttack = 30; // 特殊攻擊
			action.rangedAttack = 1; // 遠距攻擊
			action.status = 24; // 狀態
		}

		/*******************************************************************************/

		gfxid = new int[] { 3869, 3870 };

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 1; // 預設攻擊
			action.specialAttack = 0; // 特殊攻擊
			action.rangedAttack = 1; // 遠距攻擊
			action.status = 24; // 狀態
		}

		/*******************************************************************************/

		gfxid = new int[] { 1096, 1130, 1250, 1832, 2533, 2976, 3402,
				3412, 4423, 4429, 4463, 4521, 4531, 4550, 5062, 5317,
				5324, 5331, 5338, 6558, 7436
		};

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 30; // 預設攻擊
			action.specialAttack = 0; // 特殊攻擊
			action.rangedAttack = 1; // 遠距攻擊
			action.status = 0; // 狀態
		}

		/*******************************************************************************/

		gfxid = new int[] { 1603, 1828, 2716, 2747, 2757, 4017, 4025,
				4028, 4104, 4405, 4612, 4667, 4854, 4855, 5567, 5723,
				7880, 8331, 8679
		};

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 18; // 預設攻擊
			action.specialAttack = 0; // 特殊攻擊
			action.rangedAttack = 18; // 遠距攻擊
			action.status = 0; // 狀態
		}

		/*******************************************************************************/

		gfxid = new int[] { 2738, 4404, 7886 };

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 19; // 預設攻擊
			action.specialAttack = 0; // 特殊攻擊
			action.rangedAttack = 19; // 遠距攻擊
			action.status = 0; // 狀態
		}

		/*******************************************************************************/

		gfxid = new int[] { 2064, 3582, 4125, 5127, 5412 };

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 1; // 預設攻擊
			action.specialAttack = 0; // 特殊攻擊
			action.rangedAttack = 30; // 遠距攻擊
			action.status = 0; // 狀態
		}

		/*******************************************************************************/

		gfxid = new int[] { 32, 1780, 4121, 4796, 5463, 5657 };

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 30; // 預設攻擊
			action.specialAttack = 0; // 特殊攻擊
			action.rangedAttack = 30; // 遠距攻擊
			action.status = 0; // 狀態
		}

		/*******************************************************************************/

		gfxid = new int[] { 53, 148, 152, 173, 183, 185, 187, 240, 255,
				257, 864, 865, 894, 934, 936, 1011, 1020, 1022, 1047, 1098,
				1104, 1106, 1108, 1110, 1128, 1132, 1134, 1173, 1202, 1204,
				1206, 1222, 1318, 1321, 1477, 1517, 1525, 1542, 1555, 1569,
				1571, 1576, 1595, 1597, 1600, 1610, 1612, 1614, 1626, 1632,
				1642, 1649, 1659, 1666, 1762, 1770, 1839, 1841, 1844, 1997,
				2086, 2112, 2134, 2137, 2145, 2147, 2158, 2308, 2332, 2340,
				2350, 2351, 2353, 2356, 2357, 2358, 2361, 2364, 2365, 2366,
				2371, 2372, 2373, 2392, 2417, 2421, 2443, 2489, 2513, 2516,
				2524, 2538, 2755, 2771, 2788, 2843, 3044, 3049, 3066, 3092,
				3101, 3107, 3132, 3136, 3180, 3184, 3188, 3195, 3199, 3211,
				3357, 3366, 3404, 3409, 3547, 3596, 3644, 3652, 3801, 3864,
				3916, 3918, 3920, 3969, 3984, 3989, 4007, 4013, 4038, 4041, 
				4058, 4060, 4068, 4083, 4088, 4095, 4133, 4147, 4151, 4149, 
				4186, 4188, 4190, 4203, 4205, 4207, 4209, 4215, 4417,
				4420, 4426, 4457, 4460, 4466, 4472, 4475, 4493, 4502, 4519, 
				4542, 4559, 4561, 4563, 4569, 4574, 4582, 4587, 4607,
				4609, 4615, 4622, 4632, 4633, 4634, 4652, 4677, 4684, 4689,
				4698, 4738, 4740, 4744, 4746, 4753, 4771, 4786, 4787, 4788,
				4820, 4822, 4867, 4910, 4912, 4915, 4930, 4935, 4942, 5007,
				5008, 5014, 5033, 5065, 5089, 5107, 5110, 5112, 5114, 5259,
				5265, 5269, 5274, 5282, 5284, 5297, 5348, 5351, 5353, 5447,
				5471, 5507, 5511, 5547, 5554, 5596, 5608, 5683, 5687, 5688,
				5691, 5694, 5695, 5708, 5724, 5742, 5745, 5751, 5754, 5778,
				5781, 5818, 5938, 5969, 5972, 5977, 5979, 6322, 6560, 6562,
				6563, 6566, 6569, 6595, 6601, 6603, 6632, 6636, 6638, 6653,
				6654, 6699, 6704, 6776, 6778, 7111, 7113, 7167, 7170, 7173,
				7176, 7335, 7369, 7481, 7489, 7496, 7503, 7509, 7515, 7587,
				7590, 7591, 7593, 7596, 7745
		};

		for (int i = 0; i < gfxid.length; i++) {
			if (!_dataMap.containsKey(gfxid[i])) {
				action = new Action();
				_dataMap.put(gfxid[i], action);
			} else {
				System.out.println("重複編號：" + gfxid[i]);
				continue;
			}

			action.defaultAttack = 1; // 預設攻擊
			action.specialAttack = 30; // 特殊攻擊
			action.rangedAttack = 1; // 遠距攻擊
			action.status = 0; // 狀態
		}
	}

	public int getDefaultAttack(int gfxid) {
		if (_dataMap.containsKey(gfxid)) {
			return _dataMap.get(gfxid).defaultAttack;
		}
		return 1;
	}

	public int getSpecialAttack(int gfxid) {
		if (_dataMap.containsKey(gfxid)) {
			return _dataMap.get(gfxid).specialAttack;
		}
		return 0;
	}

	public int getRangedAttack(int gfxid) {
		if (_dataMap.containsKey(gfxid)) {
			return _dataMap.get(gfxid).rangedAttack;
		}
		return 1;
	}

	public int getStatus(int gfxid) {
		if (_dataMap.containsKey(gfxid)) {
			return _dataMap.get(gfxid).status;
		}
		return 0;
	}
}
