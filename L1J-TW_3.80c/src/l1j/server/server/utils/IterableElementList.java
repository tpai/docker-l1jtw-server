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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IterableElementList implements Iterable<Element> {
	IterableNodeList _list;

	private class MyIterator implements Iterator<Element> {
		private Iterator<Node> _itr;
		private Element _next = null;

		public MyIterator(Iterator<Node> itr) {
			_itr = itr;
			updateNextElement();
		}

		private void updateNextElement() {
			while (_itr.hasNext()) {
				Node node = _itr.next();
				if (node instanceof Element) {
					_next = (Element) node;
					return;
				}
			}
			_next = null;
		}

		@Override
		public boolean hasNext() {
			return _next != null;
		}

		@Override
		public Element next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Element result = _next;
			updateNextElement();
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public IterableElementList(NodeList list) {
		_list = new IterableNodeList(list);
	}

	@Override
	public Iterator<Element> iterator() {
		return new MyIterator(_list.iterator());
	}

}
