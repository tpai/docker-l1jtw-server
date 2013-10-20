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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.poison.L1SilencePoison;
import l1j.server.server.storage.TrapStorage;

public class L1PoisonTrap extends L1Trap {
	private final String _type;
	private final int _delay;
	private final int _time;
	private final int _damage;

	public L1PoisonTrap(TrapStorage storage) {
		super(storage);

		_type = storage.getString("poisonType");
		_delay = storage.getInt("poisonDelay");
		_time = storage.getInt("poisonTime");
		_damage = storage.getInt("poisonDamage");
	}

	@Override
	public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
		sendEffect(trapObj);

		if (_type.equals("d")) {
			L1DamagePoison.doInfection(trodFrom, trodFrom, _time, _damage);
		} else if (_type.equals("s")) {
			L1SilencePoison.doInfection(trodFrom);
		} else if (_type.equals("p")) {
			L1ParalysisPoison.doInfection(trodFrom, _delay, _time);
		}
	}
}
