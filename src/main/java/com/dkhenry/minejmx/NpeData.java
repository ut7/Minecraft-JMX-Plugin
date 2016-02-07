package com.dkhenry.minejmx;

import java.sql.ResultSet;
import java.sql.SQLException;

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

public class NpeData implements DynamicMBean {
	// stuff we're exporting
	private int totalDeaths = 0;/**< Done */
	private int deathsByPlayer = 0;/**< Done */
	private int deathsByEnvironment = 0;/**< Done */
	private int deathsByNpe = 0;/**< Done */
	private int playersKilled = 0;/**< Done */
	private int npesKilled = 0;/**< Done */

	// need to access the plugin object from this one
	private MineJMX plugin;

	public NpeData(MineJMX instance) {
		this.plugin = instance;
	}

	// totalDeaths {{{
	public int getTotalDeaths() {
		return totalDeaths;
	}

	public void setTotalDeaths(int totalDeaths) {
		this.totalDeaths = totalDeaths;
	}
	
	public void incTotalDeaths() { 
		incTotalDeaths(1) ; 
	}
	
	public void incTotalDeaths(int increment) { 
		this.totalDeaths+= increment ; 
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

	// npesKilled {{{
	public int getNpesKilled() {
		return this.npesKilled;
	}

	public void setNpesKilled(int npesKilled) {
		this.npesKilled = playersKilled;
	}

	public void incNpesKilled() {
		this.npesKilled++;
	}
	// }}}

	@Override public Object getAttribute(String arg0) throws AttributeNotFoundException, MBeanException, ReflectionException {
		switch (arg0) {
			case "totalDeaths":
				return this.getTotalDeaths();
			case "deathsByPlayer":
				return this.getDeathsByPlayer();
			case "deathsByEnvironment":
				return this.getDeathsByEnvironment();
			case "deathsByNpe":
				return this.getDeathsByNpe();
			case "playersKilled":
				return this.getPlayersKilled();
			case "npesKilled":
				return this.getNpesKilled();
		}
		throw new AttributeNotFoundException("Cannot find " + arg0 + " attribute");
	}

	@Override public AttributeList getAttributes(String[] arg0) {
		AttributeList resultList = new AttributeList();
		if(arg0.length == 0) {
			return resultList;
		}
		for (String anArg0 : arg0) {
			try {
				Object Value = getAttribute(anArg0);
				resultList.add(new Attribute(anArg0, Value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	@Override public MBeanInfo getMBeanInfo() {
		OpenMBeanInfoSupport info;
		OpenMBeanAttributeInfoSupport[] attributes = {
			new OpenMBeanAttributeInfoSupport("totalDeaths", "Total number of times killed", SimpleType.INTEGER, true, false, false),
			new OpenMBeanAttributeInfoSupport("deathsByPlayer", "Number of times killed by a player", SimpleType.INTEGER, true, false, false),
			new OpenMBeanAttributeInfoSupport("deathsByEnvironment", "Number of times killed by the environment", SimpleType.INTEGER, true, false, false),
			new OpenMBeanAttributeInfoSupport("deathsByNpe", "Number of times killed by other non-Player Entities", SimpleType.INTEGER, true, false, false),
			new OpenMBeanAttributeInfoSupport("playersKilled", "Number of players killed", SimpleType.INTEGER, true, false, false),
			new OpenMBeanAttributeInfoSupport("npesKilled", "Number of non-Player Entities", SimpleType.INTEGER, true, false, false)
		};

		//Build the info
		info = new OpenMBeanInfoSupport(this.getClass().getName(), "Quote - Open - MBean", attributes, null, null, null);
		return info;
	}

	@Override public Object invoke(String arg0, Object[] arg1, String[] arg2) throws MBeanException, ReflectionException {
		throw new ReflectionException(new NoSuchMethodException(arg0), "Cannot find the operation " + arg0);
	}

	@Override public void setAttribute(Attribute arg0) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
		throw new AttributeNotFoundException("No attributes can be set on this MBean");
	}

	@Override public AttributeList setAttributes(AttributeList arg0) {
		return new AttributeList();
	}

	// persistence {{{
	public String getMetricData() {
		return "deathsByPlayer:" + this.deathsByPlayer +
		       ",deathsByEnvironment:" + this.deathsByEnvironment +
			   ",deathsByNpe:" + this.deathsByNpe +
			   ",playersKilled:" + this.playersKilled;
	}

	public static NpeData instanceFromResultSet(ResultSet rs, MineJMX plugin) throws SQLException {
		NpeData entry = new NpeData(plugin);
		String data = rs.getString("data");
		if(data.length() <= 0) {
			return entry;
		}
		String[] datas = data.split(",");
		for(String s : datas) {
			String[] keyval = s.split(":");
			switch (keyval[0]) {
				case "deathsByPlayer":
					entry.setDeathsByPlayer(Integer.decode(keyval[1]));
					break;
				case "deathsByEnvironment":
					entry.setDeathsByEnvironment(Integer.decode(keyval[1]));
					break;
				case "deathsByNpe":
					entry.setDeathsByNpe(Integer.decode(keyval[1]));
					break;
				case "playersKilled":
					entry.setPlayersKilled(Integer.decode(keyval[1]));
					break;
			}
		}
		return entry;
	}
	// }}}
}
