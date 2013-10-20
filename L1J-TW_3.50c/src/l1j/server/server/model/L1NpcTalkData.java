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
package l1j.server.server.model;

public class L1NpcTalkData {

	int ID;

	int NpcID;

	String normalAction;

	String caoticAction;

	String teleportURL;

	String teleportURLA;

	/**
	 * @return Returns the normalAction.
	 */
	public String getNormalAction() {
		return normalAction;
	}

	/**
	 * @param normalAction
	 *            The normalAction to set.
	 */
	public void setNormalAction(String normalAction) {
		this.normalAction = normalAction;
	}

	/**
	 * @return Returns the caoticAction.
	 */
	public String getCaoticAction() {
		return caoticAction;
	}

	/**
	 * @param caoticAction
	 *            The caoticAction to set.
	 */
	public void setCaoticAction(String caoticAction) {
		this.caoticAction = caoticAction;
	}

	/**
	 * @return Returns the teleportURL.
	 */
	public String getTeleportURL() {
		return teleportURL;
	}

	/**
	 * @param teleportURL
	 *            The teleportURL to set.
	 */
	public void setTeleportURL(String teleportURL) {
		this.teleportURL = teleportURL;
	}

	/**
	 * @return Returns the teleportURLA.
	 */
	public String getTeleportURLA() {
		return teleportURLA;
	}

	/**
	 * @param teleportURLA
	 *            The teleportURLA to set.
	 */
	public void setTeleportURLA(String teleportURLA) {
		this.teleportURLA = teleportURLA;
	}

	/**
	 * @return Returns the iD.
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param id
	 *            The iD to set.
	 */
	public void setID(int id) {
		ID = id;
	}

	/**
	 * @return Returns the npcID.
	 */
	public int getNpcID() {
		return NpcID;
	}

	/**
	 * @param npcID
	 *            The npcID to set.
	 */
	public void setNpcID(int npcID) {
		NpcID = npcID;
	}

}
