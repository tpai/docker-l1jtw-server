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

package l1j.server.server;

public class ActionCodes {

	public ActionCodes() {
	}

	public static final int ACTION_Appear = 4;

	public static final int ACTION_Hide = 11;

	public static final int ACTION_AntharasHide = 20;

	public static final int ACTION_Walk = 0;

	public static final int ACTION_Attack = 1;

	public static final int ACTION_Damage = 2;

	public static final int ACTION_Idle = 3;

	public static final int ACTION_SwordWalk = 4;

	public static final int ACTION_SwordAttack = 5;

	public static final int ACTION_SwordDamage = 6;

	public static final int ACTION_SwordIdle = 7;

	public static final int ACTION_Die = 8;

	public static final int ACTION_AxeWalk = 11;

	public static final int ACTION_AxeAttack = 12;

	public static final int ACTION_AxeDamage = 13;

	public static final int ACTION_AxeIdle = 14;

	public static final int ACTION_HideDamage = 13;

	public static final int ACTION_HideIdle = 14;

	public static final int ACTION_Pickup = 15;

	public static final int ACTION_Throw = 16;

	public static final int ACTION_Wand = 17;

	public static final int ACTION_SkillAttack = 18;

	public static final int ACTION_SkillBuff = 19;

	public static final int ACTION_BowWalk = 20;

	public static final int ACTION_BowAttack = 21;

	public static final int ACTION_BowDamage = 22;

	public static final int ACTION_BowIdle = 23;

	public static final int ACTION_SpearWalk = 24;

	public static final int ACTION_SpearAttack = 25;

	public static final int ACTION_SpearDamage = 26;

	public static final int ACTION_SpearIdle = 27;

	public static final int ACTION_On = 28;

	public static final int ACTION_Off = 29;

	public static final int ACTION_Open = 28;

	public static final int ACTION_Close = 29;

	public static final int ACTION_South = 28;

	public static final int ACTION_West = 29;

	public static final int ACTION_AltAttack = 30;

	public static final int ACTION_SpellDirectionExtra = 31;

	public static final int ACTION_TowerCrack1 = 32;

	public static final int ACTION_TowerCrack2 = 33;

	public static final int ACTION_TowerCrack3 = 34;

	public static final int ACTION_TowerDie = 35;

	public static final int ACTION_DoorAction1 = 32;

	public static final int ACTION_DoorAction2 = 33;

	public static final int ACTION_DoorAction3 = 34;

	public static final int ACTION_DoorAction4 = 35;

	public static final int ACTION_DoorAction5 = 36;

	public static final int ACTION_DoorDie = 37;

	public static final int ACTION_StaffWalk = 40;

	public static final int ACTION_StaffAttack = 41;

	public static final int ACTION_StaffDamage = 42;

	public static final int ACTION_StaffIdle = 43;

	public static final int ACTION_Moveup = 44;

	public static final int ACTION_Movedown = 45;

	public static final int ACTION_DaggerWalk = 46;

	public static final int ACTION_DaggerAttack = 47;

	public static final int ACTION_DaggerDamage = 48;

	public static final int ACTION_DaggerIdle = 49;

	public static final int ACTION_TwoHandSwordWalk = 50;

	public static final int ACTION_TwoHandSwordAttack = 51;

	public static final int ACTION_TwoHandSwordDamage = 52;

	public static final int ACTION_TwoHandSwordIdle = 53;

	public static final int ACTION_EdoryuWalk = 54;

	public static final int ACTION_EdoryuAttack = 55;

	public static final int ACTION_EdoryuDamage = 56;

	public static final int ACTION_EdoryuIdle = 57;

	public static final int ACTION_ClawWalk = 58;

	public static final int ACTION_ClawAttack = 59;

	public static final int ACTION_ClawIdle = 61;

	public static final int ACTION_ClawDamage = 60;

	public static final int ACTION_ThrowingKnifeWalk = 62;

	public static final int ACTION_ThrowingKnifeAttack = 63;

	public static final int ACTION_ThrowingKnifeDamage = 64;

	public static final int ACTION_ThrowingKnifeIdle = 65;

	public static final int ACTION_Think = 66; // Alt+4

	public static final int ACTION_Aggress = 67; // Alt+3

	public static final int ACTION_Salute = 68; // Alt+1

	public static final int ACTION_Cheer = 69; // Alt+2

	public static final int ACTION_Shop = 70;

	public static final int ACTION_Fishing = 71;

	public static int getDefaultActionId(int gfxid) {
		switch (gfxid) {
			case 1603: // 希爾黛斯
			case 1828: // 爆彈花
			case 2716: // 古代亡靈
			case 2747: // 火精靈王
			case 2757: // 吸血鬼
			case 4017: // 水靈之主
			case 4025: // 深淵火靈
			case 4028: // 深淵水靈
			case 4104: // 吸血鬼
			case 4405: // 污濁的火精靈王
			case 4667: // 神官長．邦妮
			case 4854: // 魔獸師長．辛克萊
			case 4855: // 魔法團長．卡勒米爾
			case 5567: // 長老隨從
			case 5723: // 長老隨從
			case 7880: // 火精靈王
			case 8331: // 火精靈王
			case 8679: // 吸血鬼
				return ACTION_SkillAttack;
			case 2738: // 水精靈王
			case 4404: // 污濁的水精靈王
			case 7886: // 水精靈王
				return ACTION_SkillBuff;
			case 32: // 長者
			case 1780: // 烈炎獸
			case 2064: // 雪人
			case 3582: // 鼴鼠
			case 4121: // 烈炎獸
			case 4125: // 深淵弓箭手
			case 4612: // 火焰之影(變身後)
			case 4796: // 海賊骷髏刀手
			case 5127: // 腐爛的 骷髏弓箭手
			case 5412: // 船舶之墓殭屍
			case 5463: // 巴薩斯
			case 5657: // 小巴薩斯
				return ACTION_AltAttack;
		}
		return ACTION_Attack;
	}
	
	public static int getSpecialActionId(int gfxid) {
		switch (gfxid) {
			case 53: // 巴風特
			case 96: // 狼
			case 152: // 食人妖精
			case 173: // 卡士伯
			case 183: // 西瑪
			case 185: // 巴土瑟
			case 187: // 馬庫爾
			case 240: // 死亡騎士
			case 255: // 卡司特
			case 257: // 卡司特王
			case 865: // 污濁 芮克妮
			case 894: // 食人妖精王
			case 934: // 柯利
			case 936: // 牧羊犬
			case 1011: // 巴列斯
			case 1020: // 歐吉
			case 1022: // 哥布林
			case 1047: // 毒蠍
			case 1096: // 骷髏弓箭手
			case 1098: // 多羅
			case 1104: // 骷髏斧手
			case 1106: // 骷髏槍兵
			case 1108: // 萊肯
			case 1110: // 狼人
			case 1128: // 阿魯巴
			case 1130: // 骷髏神射手
			case 1132: // 骷髏鬥士
			case 1134: // 骷髏警衛
			case 1173: // 蜥蜴人
			case 1202: // 獨眼巨人
			case 1204: // 格利芬
			case 1206: // 哈柏哥布林
			case 1318: // 鬼魂(綠)
			case 1321: // 鬼魂(紅)
			case 1477: // 冰原狼人
			case 1525: // 冰石高崙
			case 1542: // 巨人(茶)
			case 1555: // 巨人(黑)
			case 1569: // 巨人(白)
			case 1571: // 影魔
			case 1576: // 死神
			case 1595: // 蟑螂人
			case 1597: // 蛇女
			case 1600: // 蛇女
			case 1610: // 蟹人
			case 1612: // 鼠人
			case 1614: // 穴居人
			case 1626: // 人魚
			case 1632: // 奎斯坦修
			case 1642: // 熊
			case 1649: // 龍龜
			case 1666: // 火蜥蜴
			case 1762: // 熔岩高崙
			case 1770: // 火焰戰士
			case 1841: // 阿西塔基奧
			case 1844: // 龍蠅
			case 2086: // 冰原老虎
			case 2112: // 艾爾摩法師
			case 2134: // 雪怪
			case 2137: // 艾爾摩將軍
			case 2145: // 哈士奇
			case 2147: // 鋼鐵高崙
			case 2158: // 冰人
			case 2308: // 翼魔
			case 2332: // 夢魘
			case 2340: // 變形怪
			case 2350: // 蜥蜴人
			case 2351: // 蛇女
			case 2353: // 格利芬
			case 2356: // 阿魯巴
			case 2357: // 食人妖精
			case 2358: // 食人妖精王
			case 2361: // 獨眼巨人
			case 2364: // 巨斧牛人
			case 2365: // 鏈鎚牛人
			case 2366: // 萊肯
			case 2371: // 多羅
			case 2372: // 卡司特
			case 2373: // 卡司特王
			case 2392: // 魔狼
			case 2407: // 奇美拉
			case 2417: // 強盜頭目
			case 2421: // 古代巨人
			case 2443: // 強盜(鈍器)
			case 2489: // 強盜(劍)
			case 2513: // 死亡之劍
			case 2524: // 小惡魔
			case 2538: // 炎魔
			case 2755: // 獨角獸
			case 2771: // 鎧甲守護神
			case 2788: // 殺人蜂
			case 2843: // 刺客首領
			case 3044: // 黑暗妖精士兵
			case 3049: // 黑豹
			case 3066: // 騎士范德
			case 3092: // 鐮刀死神的使者
			case 3101: // 克特
			case 3107: // 高等哈士奇
			case 3132: // 高等杜賓狗
			case 3136: // 警衛
			case 3180: // 艾莉絲
			case 3184: // 高等牧羊犬
			case 3188: // 高等熊
			case 3195: // 墮落的司祭
			case 3199: // 高等狼
			case 3211: // 高等柯利
			case 3357: // 魔熊
			case 3366: // 黑暗妖精將軍
			case 3404: // 黑暗妖精警衛(槍)
			case 3409: // 馴獸師
			case 3547: // 黑虎
			case 3596: // 歐姆裝甲兵
			case 3644: // 歐姆民兵
			case 3652: // 犰狳
			case 3864: // 妖魔鬥士
			case 3916: // 奇岩 魔狼
			case 3918: // 奇岩 冰原老虎
			case 3920: // 奇岩 蟑螂人
			case 3969: // 墮落的司祭(三頭魔)
			case 3984: // 墮落的司祭(噴毒獸)
			case 3989: // 墮落的司祭(鐮刀手)
			case 4003: // 黑騎士
			case 4007: // 西斯
			case 4013: // 火靈之主
			case 4038: // 浣熊
			case 4041: // 地靈之主
			case 4058: // 地底魔蠍
			case 4060: // 地底蛇蠍
			case 4068: // 墮落
			case 4083: // 火蜥蜴
			case 4088: // 夢魘
			case 4095: // 死神
			case 4133: // 高等浣熊
			case 4147: // 奇異鸚鵡
			case 4151: // 重裝蜥蜴人
			case 4149: // 狂暴蜥蜴人
			case 4186: // 海賊骷髏
			case 4188: // 海賊骷髏首領
			case 4190: // 海賊骷髏士兵
			case 4203: // 狂野之魔
			case 4205: // 狂野之毒
			case 4207: // 狂野毒牙
			case 4209: // 墳墓守護者
			case 4215: // 巨大墳墓守護者
			case 4417: // 污濁 人類(木棒)
			case 4420: // 污濁 人類(斧)
			case 4426: // 污濁 人類(劍)
			case 4457: // 污濁妖魔
			case 4460: // 污濁 妖魔斧兵
			case 4463: // 污濁妖魔弓箭手
			case 4466: // 阿利歐克
			case 4472: // 污濁妖魔鬥士
			case 4475: // 污濁 妖魔槍兵
			case 4493: // 樹精
			case 4502: // 樹精分身
			case 4519: // 貪慾的士兵
			case 4542: // 老虎
			case 4559: // 貪慾的戰士
			case 4561: // 伯爵的親衛隊
			case 4563: // 死亡的司祭(巴風特)
			case 4569: // 火焰之影(變身前)
			case 4574: // 死亡的司祭(小惡魔)
			case 4582: // 高麗犬
			case 4587: // 死亡
			case 4607: // 血刺客
			case 4609: // 拉斯塔巴德親衛隊
			case 4615: // 血騎士
			case 4622: // 地獄奴隸
			case 4632: // 水元素守護者
			case 4633: // 火元素守護者
			case 4634: // 地元素守護者
			case 4652: // 死亡的司祭(巴列斯)
			case 4677: // 迪哥
			case 4684: // 巨鼠
			case 4689: // 歐姆戰士
			case 4698: // 豪勢
			case 4738: // 異變 蟹人
			case 4740: // 貪慾的杜賓狗
			case 4744: // 重裝歐姆戰士
			case 4746: // 庫曼
			case 4753: // 變形怪
			case 4771: // 異變 蜥蜴人
			case 4786: // 風元素守護者
			case 4787: // 闇元素守護者
			case 4788: // 異變 人魚
			case 4820: // 異變萊肯
			case 4822: // 異變 狼人
			case 4867: // 瑪依奴夏門的鋼鐵高崙
			case 4910: // 瑪依奴夏門的石頭高崙
			case 4912: // 瑪依奴夏門的鑽石高崙
			case 4915: // 冰人
			case 4930: // 腐爛的 殭屍槍兵
			case 4935: // 腐爛的 殭屍將軍
			case 4942: // 瑪依奴
			case 5008: // 巴貝多
			case 5014: // 腐爛的 骷髏斧手
			case 5033: // 腐爛的 骷髏槍兵
			case 5065: // 高麗幼犬
			case 5089: // 真．虎男
			case 5107: // 瑪依奴夏門的熔岩高崙
			case 5110: // 腐爛的 人形殭屍
			case 5112: // 腐爛的 人形殭屍
			case 5114: // 腐爛的 人形殭屍
			case 5259: // 特提斯
			case 5265: // 安加斯
			case 5269: // 小安加斯
			case 5274: // 長老．泰瑪斯
			case 5282: // 翼龍
			case 5284: // 長老．拉曼斯
			case 5297: // 長老．艾迪爾
			case 5348: // 瑪依奴夏門的活鎧甲
			case 5351: // 狄高
			case 5353: // 小狄高
			case 5447: // 受詛咒的巫女莎爾
			case 5471: // 長老．巴陸德
			case 5507: // 炎魔
			case 5511: // 炎魔
			case 5547: // 大王烏賊
			case 5554: // 半魚人
			case 5596: // 血刺客
			case 5608: // 受詛咒的黑暗妖精騎士
			case 5683: // 污濁獨眼巨人
			case 5687: // 污濁歐吉
			case 5688: // 污濁多羅
			case 5691: // 污濁妖魔鬥士
			case 5694: // 受詛咒的黑暗妖精鬥士
			case 5695: // 受詛咒的黑暗妖精騎士
			case 5708: // 半魚人首領
			case 5724: // 受詛咒的黑暗妖精騎士
			case 5742: // 受詛咒的黑暗妖精騎士
			case 5745: // 受詛咒的黑暗妖精騎士
			case 5751: // 受詛咒的黑暗妖精鬥士
			case 5754: // 受詛咒的黑暗妖精鬥士
			case 5778: // 受詛咒的黑暗妖精鬥士
			case 5781: // 受詛咒的黑暗妖精鬥士
			case 5818: // 卡普
			case 5938: // 污濁 妖魔戰士
			case 5969: // 怨靈(男)
			case 5972: // 士兵的怨靈
			case 5977: // 怨靈(女)
			case 5979: // 哈蒙將軍的怨靈
			case 6322: // 高等袋鼠
			case 6560: // 冰之女王禁衛兵
			case 6562: // 底比斯巴斯(黃)
			case 6563: // 底比斯巴斯(紫)
			case 6566: // 冰魔
			case 6569: // 底比斯 賀洛斯
			case 6595: // 底比斯 阿努比斯
			case 6601: // 底比斯 凱比斯(黑)
			case 6603: // 底比斯 凱比斯(紅)
			case 6632: // 冰原老虎
			case 6636: // 萊肯
			case 6638: // 鋼鐵高崙
			case 6653: // 底比斯 阿努斯(黃)
			case 6654: // 底比斯 阿努斯(紅)
			case 6699: // 底比斯 斯芬克斯(白)
			case 6704: // 底比斯 斯芬克斯(黑)
				return ACTION_AltAttack;
			case 2402: // 巨大牛人
				return ACTION_SkillAttack;
		}
		return 0;
	}
}