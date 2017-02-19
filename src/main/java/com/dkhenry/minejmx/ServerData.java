
package com.dkhenry.minejmx;

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

public class ServerData implements DynamicMBean {
	// stuff we're exporting to JMX
	private long blocksPlaced ; /**< Done */
	private long blocksDestroyed ; /**< Done */
	private long blocksSpread; /**< Done */
	private long blocksDecayed; /**< Done */
	private long itemsCrafted ;
	private long npesKilled ;/** In progress */
	private int playersKilled ; /** Done */
	private int numberOfPlayers ; /**< Done */
	private double playerDistanceMoved = 0.0; /**< In Progress */
    private long chunksLoaded;
    private long entityCount;
    private long entitiesPerChunkMax;

	// need to access the plugin object from this one
	private MineJMX plugin;

	public ServerData(MineJMX plugin) {
		this.plugin = plugin;
	}

	// blocksPlaced {{{
	public long getBlocksPlaced() {
		return blocksPlaced;
	}

	public void setBlocksPlaced(long blocksPlaced) {
		this.blocksPlaced = blocksPlaced;
	}

	public void incBlocksPlaced() {
		this.blocksPlaced++ ;
	}
	// }}}

	// itemsCrafted {{{
	public long getItemsCrafted() {
		return itemsCrafted;
	}

	public void setItemsCrafted(long itemsCrafted) {
		this.itemsCrafted = itemsCrafted;
	}

	public void incItemsCrafted() {
		this.itemsCrafted++ ;
	}
	// }}}

	// npesKilled {{{
	public long getNpesKilled() {
		return this.npesKilled;
	}

	public void setNpesKilled(long npesKilled) {
		this.npesKilled = npesKilled;
	}

	public void incNpesKilled() {
		this.npesKilled++;
	}
	// }}}

	// numberOfPlayers {{{
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public void incNumberOfPlayers() {
		this.numberOfPlayers++ ;
	}

	public void decNumberOfPlayers() {
		this.numberOfPlayers-- ;
	}
	// }}}

	// playersKilled {{{
	public int getPlayersKilled() {
		return playersKilled;
	}

	public void setPlayersKilled(int playersKilled) {
		this.playersKilled = playersKilled;
	}

	public void incPlayersKilled() {
		this.playersKilled++ ;
	}
	// }}}

	// blocksDestroyed {{{
	public long getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public void setBlocksDestroyed(long blocksDestroyed) {
		this.blocksDestroyed = blocksDestroyed;
	}

	public void incBlocksDestroyed() {
		this.blocksDestroyed++ ;
	}
	// }}}

	// blocksSpread {{{
	public long getBlocksSpread() {
		return this.blocksSpread;
	}

	public void setBlocksSpread(long blocksSpread) {
		this.blocksSpread = blocksSpread;
	}

	public void incBlocksSpread() {
		this.blocksSpread++;
	}
	// }}}

	// blocksDecayed {{{
	public long getBlocksDecayed() {
		return this.blocksDecayed;
	}

	public void setBlocksDecayed(long blocksDecayed) {
		this.blocksDecayed = blocksDecayed;
	}

	public void incBlocksDecayed() {
		this.blocksDecayed++;
	}
	// }}}

	// playerDistanceMoved {{{
	public double getPlayerDistanceMoved() {
		return this.playerDistanceMoved;
	}

	public void setPlayerDistanceMoved(double playerDistanceMoved) {
		this.playerDistanceMoved = playerDistanceMoved;
	}

	public void incPlayerDistanceMovedBy(double playerDistanceMoved) {
		this.playerDistanceMoved += playerDistanceMoved;
	}
	// }}}

    // chunksLoaded {{{
    public long getChunksLoaded() {
        return this.chunksLoaded;
    }

    public void setChunksLoaded(long chunksLoaded) {
        this.chunksLoaded = chunksLoaded;
    }
    // }}}

    // entityCount {{{
    public long getEntityCount() {
        return this.entityCount;
    }

    public void setEntityCount(long entityCount) {
        this.entityCount = entityCount;
    }
    // }}}

    // entitiesPerChunkMax {{{
    public long getEntitiesPerChunkMax() {
        return this.entitiesPerChunkMax;
    }

    public void setEntitiesPerChunkMax(long entitiesPerChunkMax) {
        this.entitiesPerChunkMax = entitiesPerChunkMax;
    }
    // }}}

	@Override
	public Object getAttribute(String arg0) throws AttributeNotFoundException,
			MBeanException, ReflectionException {

		switch (arg0) {
			case "blocksPlaced":
				return getBlocksPlaced();
			case "blocksDestroyed":
				return getBlocksDestroyed();
			case "blocksSpread":
				return this.getBlocksSpread();
			case "blocksDecayed":
				return this.getBlocksDecayed();
			case "itemsCrafted":
				return getItemsCrafted();
			case "playersKilled":
				return getPlayersKilled();
			case "npesKilled":
				return this.getNpesKilled();
			case "numberOfPlayers":
				return this.getNumberOfPlayers();
			case "playerDistanceMoved":
				return this.getPlayerDistanceMoved();
			case "chunksLoaded":
				return this.getChunksLoaded();
			case "entityCount":
				return this.getEntityCount();
			case "entitiesPerChunkMax":
				return this.getEntitiesPerChunkMax();
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
		OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[12];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("blocksPlaced","Number of Blocks Placed",SimpleType.LONG, true, false,false);
		attributes[1] = new OpenMBeanAttributeInfoSupport("blocksDestroyed","Number of Blocks Destroyed",SimpleType.LONG, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("blocksSpread", "Number of blocks naturally spread", SimpleType.LONG, true, false, false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("blocksDecayed", "Number of blocks naturally decayed", SimpleType.LONG, true, false, false);
		attributes[4] = new OpenMBeanAttributeInfoSupport("itemsCrafted","Number of items Crafted",SimpleType.LONG, true, false,false);
		attributes[5] = new OpenMBeanAttributeInfoSupport("playersKilled","Number Of Players Killed",SimpleType.INTEGER, true, false,false);
		attributes[6] = new OpenMBeanAttributeInfoSupport("npesKilled","Number of non-Player Entities killed",SimpleType.INTEGER, true, false, false);
		attributes[7] = new OpenMBeanAttributeInfoSupport("numberOfPlayers","Number of Players On Server",SimpleType.INTEGER, true, false,false);
		attributes[8] = new OpenMBeanAttributeInfoSupport("playerDistanceMoved", "Total player distance traveled", SimpleType.DOUBLE, true, false, false);
        attributes[9] = new OpenMBeanAttributeInfoSupport("chunksLoaded", "Total count of loaded chunks",
        SimpleType.LONG, true, false, false);
        attributes[10] = new OpenMBeanAttributeInfoSupport("entityCount", "Total count of active entities",
        SimpleType.LONG, true, false, false);
        attributes[11] = new OpenMBeanAttributeInfoSupport("entitiesPerChunkMax", "Max entity count among loaded chunks",
        SimpleType.LONG, true, false, false);

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
		return "blocksPlaced:"+this.blocksPlaced+
				",blocksDestroyed:"+this.blocksDestroyed+
				",blocksSpread:"+this.blocksSpread+
				",blocksDecayed:"+this.blocksDecayed+
				",itemsCrafted:"+this.itemsCrafted+
				",playersKilled:"+this.playersKilled +
				",npesKilled:" + this.npesKilled +
				",playerDistanceMoved:" + this.playerDistanceMoved +
                ",chunksLoaded:" + this.chunksLoaded +
                ",entityCount:" + this.entityCount +
                ",entitiesPerChunkMax:" + this.entitiesPerChunkMax;
	}
}

