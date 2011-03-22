/**
 * License THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED
 * BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS
 * AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO
 * BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE
 * CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */

package l1j.server.server.utils.collections;

import java.io.Serializable;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import javolution.util.FastMap;

public class Maps {
	public static <K, V> Map<K, V> newMap() {
		return new FastMap<K, V>();
	}

	public static <K, V> Map<K, V> newMap(Map<K, V> from) {
		return new FastMap<K, V>(from);
	}

	public static <K, V> Map<K, V> newWeakMap() {
		return new WeakHashMap<K, V>();
	}

	public static <K, V> Map<K, V> newWeakMap(Map<K, V> from) {
		return new WeakHashMap<K, V>(from);
	}

	public static <K, V> Map<K, V> newConcurrentMap() {
		return new ConcurrentHashMap<K, V>();
	}

	public static <K, V> Map<K, V> newConcurrentMap(Map<K, V> from) {
		return new ConcurrentHashMap<K, V>(from);
	}

	public static <K, V> Map<K, V> newSerializableMap() {
		return new SerializableHashMap<K, V>();
	}

	public static <K, V> Map<K, V> newSerializableMap(Map<K, V> from) {
		return new SerializableHashMap<K, V>(from);
	}

	public static class SerializableHashMap<K extends Object, V extends Object> extends FastMap<K, V> implements Serializable {
		private static final long serialVersionUID = 1L;

		public SerializableHashMap() {
			super();
		}

		public SerializableHashMap(Map<? extends K, ? extends V> m) {
			super(m);
		}
	}
}
