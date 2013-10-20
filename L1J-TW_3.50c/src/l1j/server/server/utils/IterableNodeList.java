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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * org.w3c.dom.NodeListにIterableを付加するためのアダプタ。
 */
// 標準ライブラリに同じものが用意されているようなら置換してください。
public class IterableNodeList implements Iterable<Node> {
	private final NodeList _list;

	private class MyIterator implements Iterator<Node> {
		private int _idx = 0;

		@Override
		public boolean hasNext() {
			return _idx < _list.getLength();
		}

		@Override
		public Node next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return _list.item(_idx++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public IterableNodeList(NodeList list) {
		_list = list;
	}

	@Override
	public Iterator<Node> iterator() {
		return new MyIterator();
	}

}
