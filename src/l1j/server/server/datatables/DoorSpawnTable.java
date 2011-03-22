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

package l1j.server.server.datatables;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.collections.Lists;

public class DoorSpawnTable {
	private static Logger _log = Logger.getLogger(DoorSpawnTable.class.getName());

	private static DoorSpawnTable _instance;

	private final List<L1DoorInstance> _doorList = Lists.newList();

	public static DoorSpawnTable getInstance() {
		if (_instance == null) {
			_instance = new DoorSpawnTable();
		}
		return _instance;
	}

	private DoorSpawnTable() {
		FillDoorSpawnTable();
	}

	private void FillDoorSpawnTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_door");
			rs = pstm.executeQuery();
			do {
				if (!rs.next()) {
					break;
				}

				L1Npc l1npc = NpcTable.getInstance().getTemplate(81158);
				if (l1npc != null) {
					String s = l1npc.getImpl();
					Constructor constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
					Object parameters[] =
					{ l1npc };
					L1DoorInstance door = (L1DoorInstance) constructor.newInstance(parameters);
					door = (L1DoorInstance) constructor.newInstance(parameters);
					door.setId(IdFactory.getInstance().nextId());

					door.setDoorId(rs.getInt(1));
					door.setGfxId(rs.getInt(3));
					door.setX(rs.getInt(4));
					door.setY(rs.getInt(5));
					door.setMap((short) rs.getInt(6));
					door.setHomeX(rs.getInt(4));
					door.setHomeY(rs.getInt(5));
					door.setDirection(rs.getInt(7));
					door.setLeftEdgeLocation(rs.getInt(8));
					door.setRightEdgeLocation(rs.getInt(9));
					door.setMaxHp(rs.getInt(10));
					door.setCurrentHp(rs.getInt(10));
					door.setKeeperId(rs.getInt(11));

					L1World.getInstance().storeObject(door);
					L1World.getInstance().addVisibleObject(door);

					_doorList.add(door);
				}
			}
			while (true);
		}
		catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		catch (SecurityException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		catch (ClassNotFoundException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		catch (IllegalArgumentException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		catch (InstantiationException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		catch (IllegalAccessException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		catch (InvocationTargetException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public L1DoorInstance[] getDoorList() {
		return _doorList.toArray(new L1DoorInstance[_doorList.size()]);
	}
}
