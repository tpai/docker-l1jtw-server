package l1j.server.server.utils;

public class Random {
	private static final int _max = Short.MAX_VALUE;

	private static int _idx = 0;

	private static double[] _value = new double[_max + 1];

	static {
		for (_idx = 0; _idx < _max + 1; _idx++) {
			_value[_idx] = (Math.random() + Math.random() + Math.random() + Math.random() + Math.random()) % 1.0;
		}
	}
	
	// 取得整數
	public static int nextInt(int n) {
		_idx = _idx & _max;
		return (int) (_value[_idx++] * n);
	}
	
	// 取得整數+偏移
	public static int nextInt(int n, int offset) {
		_idx = _idx & _max;
		return offset + (int) (_value[_idx++] * n);
	}
	
	// 隨機布林值
	public static boolean nextBoolean() {
		return (nextInt(2) == 1);
	}
	
	// 隨機位元
	public static byte nextByte() {
		return (byte)nextInt(256);
	}
	
	// 隨機長整數
	public static long nextLong() {
		long l = nextInt(Integer.MAX_VALUE) << 32 + nextInt(Integer.MAX_VALUE);
		return l;
	}
}