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
package l1j.server.server.model.skill;

public class L1SkillId {
	/** 魔法開頭 */
	public static final int SKILLS_BEGIN = 1;

	/*
	 * Regular Magic Lv1-10
	 */
	/** 法師魔法 (初級治癒術) */
	public static final int HEAL = 1; // E: LESSER_HEAL

	/** 法師魔法 (日光術) */
	public static final int LIGHT = 2;

	/** 法師魔法 (防護罩) */
	public static final int SHIELD = 3;

	/** 法師魔法 (光箭) */
	public static final int ENERGY_BOLT = 4;

	/** 法師魔法 (指定傳送) */
	public static final int TELEPORT = 5;

	/** 法師魔法 (冰箭) */
	public static final int ICE_DAGGER = 6;

	/** 法師魔法 (風刃) */
	public static final int WIND_CUTTER = 7; // E: WIND_SHURIKEN

	/** 法師魔法 (神聖武器) */
	public static final int HOLY_WEAPON = 8;

	/** 法師魔法 (解毒術) */
	public static final int CURE_POISON = 9;

	/** 法師魔法 (寒冷戰慄) */
	public static final int CHILL_TOUCH = 10;

	/** 法師魔法 (毒咒) */
	public static final int CURSE_POISON = 11;

	/** 法師魔法 (擬似魔法武器) */
	public static final int ENCHANT_WEAPON = 12;

	/** 法師魔法 (無所遁形術) */
	public static final int DETECTION = 13;

	/** 法師魔法 (負重強化) */
	public static final int DECREASE_WEIGHT = 14;

	/** 法師魔法 (地獄之牙) */
	public static final int FIRE_ARROW = 15;

	/** 法師魔法 (火箭) */
	public static final int STALAC = 16;

	/** 法師魔法 (極光雷電) */
	public static final int LIGHTNING = 17;

	/** 法師魔法 (起死回生術) */
	public static final int TURN_UNDEAD = 18;

	/** 法師魔法 (中級治癒術) */
	public static final int EXTRA_HEAL = 19; // E: HEAL

	/** 法師魔法 (闇盲咒術) */
	public static final int CURSE_BLIND = 20;

	/** 法師魔法 (鎧甲護持) */
	public static final int BLESSED_ARMOR = 21;

	/** 法師魔法 (寒冰氣息) */
	public static final int FROZEN_CLOUD = 22;

	/** 法師魔法 (能量感測) */
	public static final int WEAK_ELEMENTAL = 23; // E: REVEAL_WEAKNESS

	// none = 24
	/** 法師魔法 (燃燒的火球) */
	public static final int FIREBALL = 25;

	/** 法師魔法 (通暢氣脈術) */
	public static final int PHYSICAL_ENCHANT_DEX = 26; // E: ENCHANT_DEXTERITY

	/** 法師魔法 (壞物術) */
	public static final int WEAPON_BREAK = 27;

	/** 法師魔法 (吸血鬼之吻) */
	public static final int VAMPIRIC_TOUCH = 28;

	/** 法師魔法 (緩速術) */
	public static final int SLOW = 29;

	/** 法師魔法 (岩牢) */
	public static final int EARTH_JAIL = 30;

	/** 法師魔法 (魔法屏障) */
	public static final int COUNTER_MAGIC = 31;

	/** 法師魔法 (冥想術) */
	public static final int MEDITATION = 32;

	/** 法師魔法 (木乃伊的詛咒) */
	public static final int CURSE_PARALYZE = 33;

	/** 法師魔法 (極道落雷) */
	public static final int CALL_LIGHTNING = 34;

	/** 法師魔法 (高級治癒術) */
	public static final int GREATER_HEAL = 35;

	/** 法師魔法 (迷魅術) */
	public static final int TAMING_MONSTER = 36; // E: TAME_MONSTER

	/** 法師魔法 (聖潔之光) */
	public static final int REMOVE_CURSE = 37;

	/** 法師魔法 (冰錐) */
	public static final int CONE_OF_COLD = 38;

	/** 法師魔法 (魔力奪取) */
	public static final int MANA_DRAIN = 39;

	/** 法師魔法 (黑闇之影) */
	public static final int DARKNESS = 40;

	/** 法師魔法 (造屍術) */
	public static final int CREATE_ZOMBIE = 41;

	/** 法師魔法 (體魄強健術) */
	public static final int PHYSICAL_ENCHANT_STR = 42; // E: ENCHANT_MIGHTY

	/** 法師魔法 (加速術) */
	public static final int HASTE = 43;

	/** 法師魔法 (魔法相消術) */
	public static final int CANCELLATION = 44; // E: CANCEL MAGIC

	/** 法師魔法 (地裂術) */
	public static final int ERUPTION = 45;

	/** 法師魔法 (烈炎術) */
	public static final int SUNBURST = 46;

	/** 法師魔法 (弱化術) */
	public static final int WEAKNESS = 47;

	/** 法師魔法 (祝福魔法武器) */
	public static final int BLESS_WEAPON = 48;

	/** 法師魔法 (體力回復術) */
	public static final int HEAL_ALL = 49; // E: HEAL_PLEDGE

	/** 法師魔法 (冰矛圍籬) */
	public static final int ICE_LANCE = 50;

	/** 法師魔法 (召喚術) */
	public static final int SUMMON_MONSTER = 51;

	/** 法師魔法 (神聖疾走) */
	public static final int HOLY_WALK = 52;

	/** 法師魔法 (龍捲風) */
	public static final int TORNADO = 53;

	/** 法師魔法 (強力加速術) */
	public static final int GREATER_HASTE = 54;

	/** 法師魔法 (狂暴術) */
	public static final int BERSERKERS = 55;

	/** 法師魔法 (疾病術) */
	public static final int DISEASE = 56;

	/** 法師魔法 (全部治癒術) */
	public static final int FULL_HEAL = 57;

	/** 法師魔法 (火牢) */
	public static final int FIRE_WALL = 58;

	/** 法師魔法 (冰雪暴) */
	public static final int BLIZZARD = 59;

	/** 法師魔法 (隱身術) */
	public static final int INVISIBILITY = 60;

	/** 法師魔法 (返生術) */
	public static final int RESURRECTION = 61;

	/** 法師魔法 (震裂術) */
	public static final int EARTHQUAKE = 62;

	/** 法師魔法 (治癒能量風暴) */
	public static final int LIFE_STREAM = 63;

	/** 法師魔法 (魔法封印) */
	public static final int SILENCE = 64;

	/** 法師魔法 (雷霆風暴) */
	public static final int LIGHTNING_STORM = 65;

	/** 法師魔法 (沉睡之霧) */
	public static final int FOG_OF_SLEEPING = 66;

	/** 法師魔法 (變形術) */
	public static final int SHAPE_CHANGE = 67; // E: POLYMORPH

	/** 法師魔法 (聖結界) */
	public static final int IMMUNE_TO_HARM = 68;

	/** 法師魔法 (集體傳送術) */
	public static final int MASS_TELEPORT = 69;

	/** 法師魔法 (火風暴) */
	public static final int FIRE_STORM = 70;

	/** 法師魔法 (藥水霜化術) */
	public static final int DECAY_POTION = 71;

	/** 法師魔法 (強力無所遁形術) */
	public static final int COUNTER_DETECTION = 72;

	/** 法師魔法 (創造魔法武器) */
	public static final int CREATE_MAGICAL_WEAPON = 73;

	/** 法師魔法 (流星雨) */
	public static final int METEOR_STRIKE = 74;

	/** 法師魔法 (終極返生術) */
	public static final int GREATER_RESURRECTION = 75;

	/** 法師魔法 (集體緩速術) */
	public static final int MASS_SLOW = 76;

	/** 法師魔法 (究極光裂術) */
	public static final int DISINTEGRATE = 77; // E: DESTROY

	/** 法師魔法 (絕對屏障) */
	public static final int ABSOLUTE_BARRIER = 78;

	/** 法師魔法 (靈魂昇華) */
	public static final int ADVANCE_SPIRIT = 79;

	/** 法師魔法 (冰雪颶風) */
	public static final int FREEZING_BLIZZARD = 80;

	// none = 81 - 86
	/*
	 * Knight skills
	 */
	/** 騎士魔法 (衝擊之暈) */
	public static final int SHOCK_STUN = 87; // E: STUN_SHOCK

	/** 騎士魔法 (增幅防禦) */
	public static final int REDUCTION_ARMOR = 88;

	/** 騎士魔法 (尖刺盔甲) */
	public static final int BOUNCE_ATTACK = 89;

	/** 騎士魔法 (堅固防護) */
	public static final int SOLID_CARRIAGE = 90;

	/** 騎士魔法 (反擊屏障) */
	public static final int COUNTER_BARRIER = 91;

	// none = 92-96
	/*
	 * Dark Spirit Magic
	 */
	public static final int BLIND_HIDING = 97;

	public static final int ENCHANT_VENOM = 98;

	public static final int SHADOW_ARMOR = 99;

	public static final int BRING_STONE = 100;

	public static final int MOVING_ACCELERATION = 101; // E: PURIFY_STONE

	public static final int BURNING_SPIRIT = 102;

	public static final int DARK_BLIND = 103;

	public static final int VENOM_RESIST = 104;

	public static final int DOUBLE_BRAKE = 105;

	public static final int UNCANNY_DODGE = 106;

	public static final int SHADOW_FANG = 107;

	public static final int FINAL_BURN = 108;

	public static final int DRESS_MIGHTY = 109;

	public static final int DRESS_DEXTERITY = 110;

	public static final int DRESS_EVASION = 111;

	// none = 112
	/*
	 * Royal Magic
	 */
	/** 精準目標 */
	public static final int TRUE_TARGET = 113;

	/** 激勵士氣 */
	public static final int GLOWING_AURA = 114;

	/** 鋼鐵士氣 */
	public static final int SHINING_AURA = 115;

	public static final int CALL_CLAN = 116; // E: CALL_PLEDGE_MEMBER

	/** 衝擊士氣 */
	public static final int BRAVE_AURA = 117;

	public static final int RUN_CLAN = 118;

	// unknown = 119 - 120
	// none = 121 - 128
	/*
	 * Spirit Magic
	 */
	public static final int RESIST_MAGIC = 129;

	public static final int BODY_TO_MIND = 130;

	/** 妖精魔法 (世界樹的呼喚) */
	public static final int TELEPORT_TO_MATHER = 131;

	public static final int TRIPLE_ARROW = 132;

	/** 妖精魔法 (弱化屬性) */
	public static final int ELEMENTAL_FALL_DOWN = 133;

	/** 妖精魔法 (鏡反射) */
	public static final int COUNTER_MIRROR = 134;

	// none = 135 - 136
	public static final int CLEAR_MIND = 137;

	public static final int RESIST_ELEMENTAL = 138;

	// none = 139 - 144
	public static final int RETURN_TO_NATURE = 145;

	public static final int BLOODY_SOUL = 146; // E: BLOOD_TO_SOUL

	public static final int ELEMENTAL_PROTECTION = 147; // E:PROTECTION_FROM_ELEMENTAL

	public static final int FIRE_WEAPON = 148;

	public static final int WIND_SHOT = 149;

	public static final int WIND_WALK = 150;

	public static final int EARTH_SKIN = 151;

	public static final int ENTANGLE = 152;

	public static final int ERASE_MAGIC = 153;

	public static final int LESSER_ELEMENTAL = 154; // E:SUMMON_LESSER_ELEMENTAL

	/** 烈炎氣息 */
	public static final int FIRE_BLESS = 155; // E: BLESS_OF_FIRE

	/** 暴風之眼 */
	public static final int STORM_EYE = 156; // E: EYE_OF_STORM

	public static final int EARTH_BIND = 157;

	public static final int NATURES_TOUCH = 158;

	/** 大地的祝福 */
	public static final int EARTH_BLESS = 159; // E: BLESS_OF_EARTH

	public static final int AQUA_PROTECTER = 160;

	/** 精靈魔法 (封印禁地)*/
	public static final int AREA_OF_SILENCE = 161;

	public static final int GREATER_ELEMENTAL = 162; // E:SUMMON_GREATER_ELEMENTAL

	public static final int BURNING_WEAPON = 163;

	public static final int NATURES_BLESSING = 164;

	public static final int CALL_OF_NATURE = 165; // E: NATURES_MIRACLE

	public static final int STORM_SHOT = 166;

	/** 妖精魔法 (風之枷鎖) */
	public static final int WIND_SHACKLE = 167;

	public static final int IRON_SKIN = 168;

	public static final int EXOTIC_VITALIZE = 169;

	/** 水之元氣 */
	public static final int WATER_LIFE = 170;

	public static final int ELEMENTAL_FIRE = 171;

	public static final int STORM_WALK = 172;

	public static final int POLLUTE_WATER = 173;

	public static final int STRIKER_GALE = 174;

	public static final int SOUL_OF_FLAME = 175;

	public static final int ADDITIONAL_FIRE = 176;

	// none = 177-180
	/*
	 * Dragon Knight skills
	 */
	public static final int DRAGON_SKIN = 181;

	public static final int BURNING_SLASH = 182;

	public static final int GUARD_BRAKE = 183;

	public static final int MAGMA_BREATH = 184;

	public static final int AWAKEN_ANTHARAS = 185;

	public static final int BLOODLUST = 186;

	/** 龍騎士魔法 (屠宰者) */
	public static final int FOE_SLAYER = 187;

	public static final int RESIST_FEAR = 188;

	public static final int SHOCK_SKIN = 189;

	public static final int AWAKEN_FAFURION = 190;

	public static final int MORTAL_BODY = 191;

	public static final int THUNDER_GRAB = 192;

	/** 龍騎士魔法 (驚悚死神) */
	public static final int HORROR_OF_DEATH = 193;

	public static final int FREEZING_BREATH = 194;

	public static final int AWAKEN_VALAKAS = 195;

	// none = 196-200
	/*
	 * Illusionist Magic
	 */
	public static final int MIRROR_IMAGE = 201;

	/** 幻術士魔法 (混亂)*/
	public static final int CONFUSION = 202;

	/** 幻術士魔法 (暴擊)*/
	public static final int SMASH = 203;

	/** 幻術士魔法 (幻覺：歐吉)*/
	public static final int ILLUSION_OGRE = 204;

	public static final int CUBE_IGNITION = 205;

	/** 幻術士魔法 (專注) */
	public static final int CONCENTRATION = 206;

	public static final int MIND_BREAK = 207;

	public static final int BONE_BREAK = 208;

	public static final int ILLUSION_LICH = 209;

	public static final int CUBE_QUAKE = 210;

	/** 幻術士魔法 (耐力) */
	public static final int PATIENCE = 211;

	public static final int PHANTASM = 212;

	public static final int ARM_BREAKER = 213;

	public static final int ILLUSION_DIA_GOLEM = 214;

	public static final int CUBE_SHOCK = 215;

	/** 幻術士魔法 (洞察)*/
	public static final int INSIGHT = 216;

	/** 幻術士魔法 (恐慌)*/
	public static final int PANIC = 217;

	/** 幻術士魔法 (疼痛的歡愉) */
	public static final int JOY_OF_PAIN = 218;

	/** 幻術士魔法 (幻覺：化身) */
	public static final int ILLUSION_AVATAR = 219;

	public static final int CUBE_BALANCE = 220;

	public static final int SKILLS_END = 220;

	/*
	 * Status
	 */
	public static final int STATUS_BEGIN = 1000;

	public static final int STATUS_BRAVE = 1000;

	public static final int STATUS_HASTE = 1001;

	public static final int STATUS_BLUE_POTION = 1002;

	public static final int STATUS_UNDERWATER_BREATH = 1003;

	public static final int STATUS_WISDOM_POTION = 1004;

	public static final int STATUS_CHAT_PROHIBITED = 1005;

	public static final int STATUS_POISON = 1006;

	public static final int STATUS_POISON_SILENCE = 1007;

	public static final int STATUS_POISON_PARALYZING = 1008;

	public static final int STATUS_POISON_PARALYZED = 1009;

	public static final int STATUS_CURSE_PARALYZING = 1010;

	public static final int STATUS_CURSE_PARALYZED = 1011;

	public static final int STATUS_FLOATING_EYE = 1012;

	public static final int STATUS_HOLY_WATER = 1013;

	public static final int STATUS_HOLY_MITHRIL_POWDER = 1014;

	public static final int STATUS_HOLY_WATER_OF_EVA = 1015;

	public static final int STATUS_ELFBRAVE = 1016;

	public static final int STATUS_RIBRAVE = 1017;

	public static final int STATUS_CUBE_IGNITION_TO_ALLY = 1018;

	public static final int STATUS_CUBE_IGNITION_TO_ENEMY = 1019;

	public static final int STATUS_CUBE_QUAKE_TO_ALLY = 1020;

	public static final int STATUS_CUBE_QUAKE_TO_ENEMY = 1021;

	public static final int STATUS_CUBE_SHOCK_TO_ALLY = 1022;

	public static final int STATUS_CUBE_SHOCK_TO_ENEMY = 1023;

	public static final int STATUS_MR_REDUCTION_BY_CUBE_SHOCK = 1024;

	public static final int STATUS_CUBE_BALANCE = 1025;

	public static final int STATUS_BRAVE2 = 1026;

	public static final int STATUS_END = 1026;

	public static final int GMSTATUS_BEGIN = 2000;

	public static final int GMSTATUS_INVISIBLE = 2000;

	public static final int GMSTATUS_HPBAR = 2001;

	public static final int GMSTATUS_SHOWTRAPS = 2002;

	public static final int GMSTATUS_FINDINVIS = 2003;

	public static final int GMSTATUS_END = 2003;

	public static final int COOKING_NOW = 2999;

	public static final int COOKING_BEGIN = 3000;

	public static final int COOKING_1_0_N = 3000;

	public static final int COOKING_1_1_N = 3001;

	public static final int COOKING_1_2_N = 3002;

	public static final int COOKING_1_3_N = 3003;

	public static final int COOKING_1_4_N = 3004;

	public static final int COOKING_1_5_N = 3005;

	public static final int COOKING_1_6_N = 3006;

	public static final int COOKING_1_7_N = 3007;

	public static final int COOKING_1_0_S = 3008;

	public static final int COOKING_1_1_S = 3009;

	public static final int COOKING_1_2_S = 3010;

	public static final int COOKING_1_3_S = 3011;

	public static final int COOKING_1_4_S = 3012;

	public static final int COOKING_1_5_S = 3013;

	public static final int COOKING_1_6_S = 3014;

	public static final int COOKING_1_7_S = 3015;

	public static final int COOKING_2_0_N = 3016;

	public static final int COOKING_2_1_N = 3017;

	public static final int COOKING_2_2_N = 3018;

	public static final int COOKING_2_3_N = 3019;

	public static final int COOKING_2_4_N = 3020;

	public static final int COOKING_2_5_N = 3021;

	public static final int COOKING_2_6_N = 3022;

	public static final int COOKING_2_7_N = 3023;

	public static final int COOKING_2_0_S = 3024;

	public static final int COOKING_2_1_S = 3025;

	public static final int COOKING_2_2_S = 3026;

	public static final int COOKING_2_3_S = 3027;

	public static final int COOKING_2_4_S = 3028;

	public static final int COOKING_2_5_S = 3029;

	public static final int COOKING_2_6_S = 3030;

	public static final int COOKING_2_7_S = 3031;

	public static final int COOKING_3_0_N = 3032;

	public static final int COOKING_3_1_N = 3033;

	public static final int COOKING_3_2_N = 3034;

	public static final int COOKING_3_3_N = 3035;

	public static final int COOKING_3_4_N = 3036;

	public static final int COOKING_3_5_N = 3037;

	public static final int COOKING_3_6_N = 3038;

	public static final int COOKING_3_7_N = 3039;

	public static final int COOKING_3_0_S = 3040;

	public static final int COOKING_3_1_S = 3041;

	public static final int COOKING_3_2_S = 3042;

	public static final int COOKING_3_3_S = 3043;

	public static final int COOKING_3_4_S = 3044;

	public static final int COOKING_3_5_S = 3045;

	public static final int COOKING_3_6_S = 3046;

	public static final int COOKING_3_7_S = 3047;

	public static final int COOKING_END = 3047;

	public static final int STATUS_FREEZE = 10071;

	public static final int CURSE_PARALYZE2 = 10101;

	// 編號待修正 (可攻擊炎魔、火焰之影狀態)
	public static final int STATUS_CURSE_BARLOG = 1015;

	public static final int STATUS_CURSE_YAHEE = 1014;

	// 相消無法消除的狀態
	/** 三段加速 */
	public static final int EFFECT_THIRD_SPEED = 4000;

	public static final int EFFECT_BEGIN = 4001;

	/** 神力藥水150% */
	public static final int EFFECT_POTION_OF_EXP_150 = 4001;

	/** 神力藥水175% */
	public static final int EFFECT_POTION_OF_EXP_175 = 4002;

	/** 神力藥水200% */
	public static final int EFFECT_POTION_OF_EXP_200 = 4003;

	/** 神力藥水225% */
	public static final int EFFECT_POTION_OF_EXP_225 = 4004;

	/** 神力藥水250% */
	public static final int EFFECT_POTION_OF_EXP_250 = 4005;

	/** 媽祖的祝福 */
	public static final int EFFECT_BLESS_OF_MAZU = 4006;

	/** 戰鬥藥水 */
	public static final int EFFECT_POTION_OF_BATTLE = 4007;

	/** 體力增強卷軸 */
	public static final int EFFECT_STRENGTHENING_HP = 4008;

	/** 魔力增強卷軸 */
	public static final int EFFECT_STRENGTHENING_MP = 4009;

	/** 強化戰鬥卷軸 */
	public static final int EFFECT_ENCHANTING_BATTLE = 4010;

	public static final int EFFECT_END = 4010;

	// 特殊狀態
	public static final int SPECIAL_EFFECT_BEGIN = 5001;

	/** 鎖鏈劍 (弱點曝光 LV1) **/
	public static final int SPECIAL_EFFECT_WEAKNESS_LV1 = 5001;

	/** 鎖鏈劍 (弱點曝光 LV2) **/
	public static final int SPECIAL_EFFECT_WEAKNESS_LV2 = 5002;

	/** 鎖鏈劍 (弱點曝光 LV3) **/
	public static final int SPECIAL_EFFECT_WEAKNESS_LV3 = 5003;

	public static final int SPECIAL_EFFECT_END = 5003;

	// 戰鬥特化狀態
	/** 新手保護(遭遇的守護) **/
	public static final int STATUS_NOVICE = 8000;
}
