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
package l1j.server.server.taskmanager;

import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.taskmanager.TaskManager.ExecutedTask;

/**
 * @author Layane
 * 
 */
public abstract class Task {
	private static Logger _log = Logger.getLogger(Task.class.getName());

	public void initializate() {
		if (Config.DEBUG) {
			_log.info("Task" + getName() + " inializate");
		}
	}

	public ScheduledFuture launchSpecial(ExecutedTask instance) {
		return null;
	}

	public abstract String getName();

	public abstract void onTimeElapsed(ExecutedTask task);

	public void onDestroy() {
	}
}
