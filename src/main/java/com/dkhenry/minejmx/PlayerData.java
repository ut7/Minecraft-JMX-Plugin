package com.dkhenry.minejmx;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.SimpleType;

public class PlayerData implements DynamicMBean {
	// stuff we're exporting
	private long timeOnServer = 0; /**< Done */
	private int numberOfLogins = 0; /**< Done */
	private int blocksPlaced = 0; /**< Done */
	private int blocksDestroyed = 0; /**< Done */
	private int itemsCrafted = 0;
	private int playersKilled = 0; /**< Done */
	private Map<String,Integer> mobsKilled ; /**< Done */
	private int deaths = 0 ; /**< Done */
	private int active = 0 ; /**< Done */
	private double distanceMoved = 0.0; /**< Done */
	private int deathsByPlayer = 0; /**< Done */
	private int deathsByEnvironment = 0; /**< Done */
	private int deathsByNpe = 0; /**< Done */

	// internal use
	private long loggedInTimestamp = -1; // timestamp of when the player logged in; -1 if they're not logged in

	// need to access the plugin object from this one
	private MineJMX plugin;

	public PlayerData(MineJMX instance) {
		this.plugin = instance;
		this.mobsKilled = new HashMap<>();
		// real mobs
		this.mobsKilled.put("creeper", 0);
		this.mobsKilled.put("skeleton", 0);
		this.mobsKilled.put("spider", 0);
		this.mobsKilled.put("zombie", 0);
		this.mobsKilled.put("slime", 0);
		this.mobsKilled.put("enderman", 0);
		this.mobsKilled.put("blaze", 0);
		this.mobsKilled.put("pigman", 0);
		this.mobsKilled.put("ghasts", 0);
		this.mobsKilled.put("magma", 0);
		// animals
		this.mobsKilled.put("chicken", 0);
		this.mobsKilled.put("cow", 0);		
		this.mobsKilled.put("pig", 0);
		this.mobsKilled.put("dog", 0);
		this.mobsKilled.put("sheep", 0);
		this.mobsKilled.put("squid", 0);
		this.mobsKilled.put("wolf", 0);
		this.mobsKilled.put("ocolet", 0);
		this.mobsKilled.put("mooshurm", 0);
		// NPC
		this.mobsKilled.put("testificus", 0);
		this.mobsKilled.put("irongolom", 0);
		this.mobsKilled.put("snowgolom", 0);
	}

	// timeOnServer {{{
	public long getTimeOnServer() {
		return timeOnServer;
	}

	public void setTimeOnServer(long timeOnServer) {
		this.timeOnServer = timeOnServer;
	}

	public void incTimeOnServerBy(long ms) {
		this.timeOnServer += ms;
	}
	// }}}

	// {{{ numberOfLogins
	public int getNumberOfLogins() {
		return numberOfLogins;
	}

	public void setNumberOfLogins(int numberOfLogins) {
		this.numberOfLogins = numberOfLogins;
	}

	public void incNumberOfLogins() {
		this.numberOfLogins++ ;
	}
	// }}}

	// blocksPlaced {{{
	public int getBlocksPlaced() {
		return blocksPlaced;
	}

	public void setBlocksPlaced(int blocksPlaced) {
		this.blocksPlaced = blocksPlaced;
	}

	public void incBlocksPlaced() {
		this.blocksPlaced++ ;
	}
	// }}}

	// blocksDestroyed {{{
	public int getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public void setBlocksDestroyed(int blocksDestroyed) {
		this.blocksDestroyed = blocksDestroyed;
	}

	public void incBlocksDestroyed() {
		this.blocksDestroyed++ ;
	}
	// }}}

	// itemsCrafted {{{
	public int getItemsCrafted() {
		return itemsCrafted;
	}

	public void setItemsCrafted(int itemsCrafted) {
		this.itemsCrafted = itemsCrafted;
	}
	// }}}

	// playersKilled {{{
	public int getPlayersKilled() {
		return this.playersKilled;
	}

	public void setPlayersKilled(int playersKilled) {
		this.playersKilled = playersKilled;
	}

	public void incPlayersKilled() {
		this.playersKilled++;
	}
	// }}}

	// mobsKilled {{{
	public Map<String, Integer> getMobsKilled() {
		return this.mobsKilled;
	}

	public int getMobsKilled(String type) {
		return this.mobsKilled.get(type);
	}

	public void setMobsKilled(HashMap<String, Integer> mobsKilled) {
		this.mobsKilled = mobsKilled;
	}

	public void incMobsKilled(String type) {
		this.mobsKilled.put(type, this.mobsKilled.get(type)+1)  ;
	}
	// }}}

	// deaths {{{
	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void incDeaths() {
		this.deaths++ ;
	}
	// }}}

	// active {{{
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	// }}}

	// distancedMoved {{{
	public double getDistanceMoved() {
		return this.distanceMoved;
	}

	public void setDistanceMoved(double distanceMoved) {
		this.distanceMoved = distanceMoved;
	}

	public void incDistanceMovedBy(double distanceMoved) {
		this.distanceMoved += distanceMoved;
	}
	// }}}

	// deathsByPlayer {{{
	public int getDeathsByPlayer() {
		return this.deathsByPlayer;
	}

	public void setDeathsByPlayer(int deathsByPlayer) {
		this.deathsByPlayer = deathsByPlayer;
	}

	public void incDeathsByPlayer() {
		this.deathsByPlayer++;
	}
	// }}}

	// deathsByEnvironment {{{
	public int getDeathsByEnvironment() {
		return this.deathsByEnvironment;
	}

	public void setDeathsByEnvironment(int deathsByEnvironment) {
		this.deathsByEnvironment = deathsByEnvironment;
	}

	public void incDeathsByEnvironment() {
		this.deathsByEnvironment++;
	}
	// }}}

	// deathsByNpe {{{
	public int getDeathsByNpe() {
		return this.deathsByNpe;
	}

	public void setDeathsByNpe(int deathsByNpe) {
		this.deathsByNpe = deathsByNpe;
	}

	public void incDeathsByNpe() {
		this.deathsByNpe++;
	}
	// }}}

	public long timeSinceLogin() {
		if(this.loggedInTimestamp == -1) {
			return -1;
		}
		return System.currentTimeMillis() - this.loggedInTimestamp;
	}

	public long getFullTimeOnServer() {
		return (1 == this.active) ? (this.timeOnServer + this.timeSinceLogin()) : this.timeOnServer;
	}

	public void logIn() {
		this.incNumberOfLogins();
		this.setActive(1);
		this.loggedInTimestamp = System.currentTimeMillis();
	}

	public long logOut() {
		long playerLoggedInTime = this.timeSinceLogin();
		this.setActive(0);
		this.incTimeOnServerBy(playerLoggedInTime);
		this.loggedInTimestamp = -1;
		return playerLoggedInTime;
	}

	@Override
	public Object getAttribute(String arg0) throws AttributeNotFoundException,
			MBeanException, ReflectionException {

		switch (arg0) {
			case "timeOnServer":
				return this.getFullTimeOnServer();
			case "numberOfLogins":
				return getNumberOfLogins();
			case "blocksPlaced":
				return getBlocksPlaced();
			case "blocksDestroyed":
				return getBlocksDestroyed();
			case "itemsCrafted":
				return getItemsCrafted();
			case "playersKilled":
				return this.getPlayersKilled();
			case "mobsKilled":
				return
						this.mobsKilled.get("creeper") +
								this.mobsKilled.get("skeleton") +
								this.mobsKilled.get("spider") +
								this.mobsKilled.get("zombie") +
								this.mobsKilled.get("slime");
			case "creepersKilled":
				return this.mobsKilled.get("creeper");
			case "skeletonsKilled":
				return this.mobsKilled.get("skeleton");
			case "spidersKilled":
				return this.mobsKilled.get("spider");
			case "zombiesKilled":
				return this.mobsKilled.get("zombie");
			case "slimesKilled":
				return this.mobsKilled.get("slime");
			case "animalsKilled":
				return
						this.mobsKilled.get("chicken") +
								this.mobsKilled.get("cow") +
								this.mobsKilled.get("pig") +
								this.mobsKilled.get("sheep") +
								this.mobsKilled.get("squid") +
								this.mobsKilled.get("wolf");
			case "chickensKilled":
				return this.mobsKilled.get("chicken");
			case "cowsKilled":
				return this.mobsKilled.get("cow");
			case "pigsKilled":
				return this.mobsKilled.get("pig");
			case "sheepsKilled":
				return this.mobsKilled.get("sheep");
			case "squidsKilled":
				return this.mobsKilled.get("squid");
			case "wolfsKilled":
				return this.mobsKilled.get("wolf");
			case "deaths":
				return getDeaths();
			case "active":
				return getActive();
			case "distanceMoved":
				return this.getDistanceMoved();
			case "deathsByPlayer":
				return this.getDeathsByPlayer();
			case "deathsByEnvironment":
				return this.getDeathsByEnvironment();
			case "deathsByNpe":
				return this.getDeathsByNpe();
		}

		throw new AttributeNotFoundException("Cannot find " + arg0 + " attribute") ;
	}

	@Override
	public AttributeList getAttributes(String[] arg0) {
		AttributeList resultList = new AttributeList() ;
		if(arg0.length == 0 ) {
			return resultList ;
		}
		for (String anArg0 : arg0) {
			try {
				Object Value = getAttribute(anArg0);
				resultList.add(new Attribute(anArg0, Value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultList ;
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		OpenMBeanInfoSupport info;
		OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[25];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("timeOnServer", "Time spent on this server in milliseconds", SimpleType.LONG, true, false, false);
		attributes[1] = new OpenMBeanAttributeInfoSupport("numberOfLogins","Number of Logins to this Server",SimpleType.INTEGER, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("blocksPlaced","Number of Blocks Placed",SimpleType.INTEGER, true, false,false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("blocksDestroyed","Number of Blocks Destroyed",SimpleType.INTEGER, true, false,false);
		attributes[4] = new OpenMBeanAttributeInfoSupport("itemsCrafted","Number of items Crafted",SimpleType.INTEGER, true, false,false);
		attributes[5] = new OpenMBeanAttributeInfoSupport("playersKilled", "Number of players killed (PvP)", SimpleType.INTEGER, true, false, false);
		attributes[6] = new OpenMBeanAttributeInfoSupport("mobsKilled", "Number Of Mobs Killed", SimpleType.INTEGER, true, false, false);
		attributes[7] = new OpenMBeanAttributeInfoSupport("creepersKilled", "Number of Creepers Killed", SimpleType.INTEGER, true, false, false);
		attributes[8] = new OpenMBeanAttributeInfoSupport("skeletonsKilled", "Number of Skeletons Killed", SimpleType.INTEGER, true, false, false);
		attributes[9] = new OpenMBeanAttributeInfoSupport("spidersKilled", "Number of Spiders Killed", SimpleType.INTEGER, true, false, false);
		attributes[10] = new OpenMBeanAttributeInfoSupport("zombiesKilled", "Number of Zombies Killed", SimpleType.INTEGER, true, false, false);
		attributes[11] = new OpenMBeanAttributeInfoSupport("slimesKilled", "Number of Slimes killed", SimpleType.INTEGER, true, false, false);
		attributes[12] = new OpenMBeanAttributeInfoSupport("animalsKilled", "Number of animals killed", SimpleType.INTEGER, true, false, false);
		attributes[13] = new OpenMBeanAttributeInfoSupport("chickensKilled", "Number of Chickens killed", SimpleType.INTEGER, true, false, false);
		attributes[14] = new OpenMBeanAttributeInfoSupport("cowsKilled", "Number of Cows killed", SimpleType.INTEGER, true, false, false);
		attributes[15] = new OpenMBeanAttributeInfoSupport("pigsKilled", "Number of Pigs killed", SimpleType.INTEGER, true, false, false);
		attributes[16] = new OpenMBeanAttributeInfoSupport("sheepsKilled", "Number of Sheep killed", SimpleType.INTEGER, true, false, false);
		attributes[17] = new OpenMBeanAttributeInfoSupport("squidsKilled", "Number of Squids killed", SimpleType.INTEGER, true, false, false);
		attributes[18] = new OpenMBeanAttributeInfoSupport("wolfsKilled", "Number of Wolves killed", SimpleType.INTEGER, true, false, false);
		attributes[19] = new OpenMBeanAttributeInfoSupport("deaths", "Number of deaths on this server", SimpleType.INTEGER, true, false, false);
		attributes[20] = new OpenMBeanAttributeInfoSupport("active", "If this player is active", SimpleType.INTEGER, true, false, false);
		attributes[21] = new OpenMBeanAttributeInfoSupport("distanceMoved", "How far this player has moved", SimpleType.DOUBLE, true, false, false);
		attributes[22] = new OpenMBeanAttributeInfoSupport("deathsByPlayer", "How many times this player was killed by other players", SimpleType.INTEGER, true, false, false);
		attributes[23] = new OpenMBeanAttributeInfoSupport("deathsByEnvironment", "How many times this player was killed by environmental causes", SimpleType.INTEGER, true, false, false);
		attributes[24] = new OpenMBeanAttributeInfoSupport("deathsByNpe", "How many times this player was killed by non-Player Entities", SimpleType.INTEGER, true, false, false);

		//Build the info
		info = new OpenMBeanInfoSupport(this.getClass().getName(),
					"Quote - Open - MBean", attributes, null,
					null, null);
		return info;
	}

	@Override
	public Object invoke(String arg0, Object[] arg1, String[] arg2)
			throws MBeanException, ReflectionException {
		throw new ReflectionException(new NoSuchMethodException(arg0),"Cannot find the operation " + arg0) ;
	}

	@Override
	public void setAttribute(Attribute arg0) throws AttributeNotFoundException,
			InvalidAttributeValueException, MBeanException, ReflectionException {
		throw new AttributeNotFoundException("No attributes can be set on this MBean") ;

	}

	@Override
	public AttributeList setAttributes(AttributeList arg0) {
		return new AttributeList() ;
	}

	public String getMetricData() {
		String rvalue = "" ;
		for(Entry<String, Integer> entity : this.mobsKilled.entrySet()) {
			rvalue += ","+entity.getKey()+":"+entity.getValue() ;
		}
		return "timeOnServer:"+this.timeOnServer+
				",numberOfLogins:"+this.numberOfLogins+
				",blocksPlaced:"+this.blocksPlaced+
				",blocksDestroyed:"+this.blocksDestroyed+
				",itemsCrafted:"+this.itemsCrafted+
				",playersKilled:" + this.playersKilled +
				",deaths:"+this.deaths+
				",active:"+this.active +
				",distanceMoved:" + this.distanceMoved +
				",deathsByPlayer:" + this.deathsByPlayer +
				",deathsByEnvironment:" + this.deathsByEnvironment +
				",deathsByNpe:" + this.deathsByNpe +
				rvalue;
	}
}
