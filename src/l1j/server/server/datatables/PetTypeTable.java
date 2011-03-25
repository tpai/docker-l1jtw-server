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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1PetType;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Maps;

public class PetTypeTable {
	private static PetTypeTable _instance;

	private static Logger _log = Logger.getLogger(PetTypeTable.class.getName());

	private Map<Integer, L1PetType> _types = Maps.newMap();

	private Set<String> _defaultNames = new HashSet<String>();

	public static void load() {
		_instance = new PetTypeTable();
	}

	public static PetTypeTable getInstance() {
		return _instance;
	}

	private PetTypeTable() {
		loadTypes();
	}

	private void loadTypes() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM pettypes");

			rs = pstm.executeQuery();

			while (rs.next()) {
				int baseNpcId = rs.getInt("BaseNpcId");
				String name = rs.getString("Name");
				int itemIdForTaming = rs.getInt("ItemIdForTaming");
				int hpUpMin = rs.getInt("HpUpMin");
				int hpUpMax = rs.getInt("HpUpMax");
				int mpUpMin = rs.getInt("MpUpMin");
				int mpUpMax = rs.getInt("MpUpMax");
				int npcIdForEvolving = rs.getInt("NpcIdForEvolving");
				int msgIds[] = new int[5];
				for (int i = 0; i < 5; i++) {
					msgIds[i] = rs.getInt("MessageId" + (i + 1));
				}
				int defyMsgId = rs.getInt("DefyMessageId");
				IntRange hpUpRange = new IntRange(hpUpMin, hpUpMax);
				IntRange mpUpRange = new IntRange(mpUpMin, mpUpMax);
				_types.put(baseNpcId, new L1PetType(baseNpcId, name, itemIdForTaming, hpUpRange, mpUpRange, npcIdForEvolving, msgIds, defyMsgId));
				_defaultNames.add(name.toLowerCase());
			}
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public L1PetType get(int baseNpcId) {
		return _types.get(baseNpcId);
	}

	public boolean isNameDefault(String name) {
		return _defaultNames.contains(name.toLowerCase());
	}
}
