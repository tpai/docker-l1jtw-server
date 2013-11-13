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
package l1j.server.server.model.trap;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.storage.TrapStorage;

public abstract class L1Trap {
	protected final int _id;
	protected final int _gfxId;
	protected final boolean _isDetectionable;

	public L1Trap(TrapStorage storage) {
		_id = storage.getInt("id");
		_gfxId = storage.getInt("gfxId");
		_isDetectionable = storage.getBoolean("isDetectionable");
	}

	public L1Trap(int id, int gfxId, boolean detectionable) {
		_id = id;
		_gfxId = gfxId;
		_isDetectionable = detectionable;
	}

	public int getId() {
		return _id;
	}

	public int getGfxId() {
		return _gfxId;
	}

	protected void sendEffect(L1Object trapObj) {
		if (getGfxId() == 0) {
			return;
		}
		S_EffectLocation effect = new S_EffectLocation(trapObj.getLocation(),
				getGfxId());

		for (L1PcInstance pc : L1World.getInstance()
				.getRecognizePlayer(trapObj)) {
			pc.sendPackets(effect);
		}
	}

	public abstract void onTrod(L1PcInstance trodFrom, L1Object trapObj);

	public void onDetection(L1PcInstance caster, L1Object trapObj) {
		if (_isDetectionable) {
			sendEffect(trapObj);
		}
	}

	public static L1Trap newNull() {
		return new L1NullTrap();
	}
}

class L1NullTrap extends L1Trap {
	public L1NullTrap() {
		super(0, 0, false);
	}

	@Override
	public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
	}
}
