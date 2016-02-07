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

public class BlockData implements DynamicMBean {
	private long blocksPlaced =0 ; /**< Done */
	private long blocksDestroyed = 0 ; /**< Done */
	private long blocksSpread = 0; /**< Done */
	private long blocksDecayed = 0; /**< Done */

	private MineJMX plugin;

	public BlockData(MineJMX instance) {
		plugin = instance ;
	}
	// blocksPlaced {{{
	public void setBlocksPlaced(long blocksPlaced) {
		this.blocksPlaced = blocksPlaced;
	}

	public long getBlocksPlaced() {
		return blocksPlaced;
	}

	public void incBlocksPlaced() {
		this.blocksPlaced++ ;
	}
	// }}}

	// blocksDestroyed {{{
	public void setBlocksDestroyed(long blocksDestroyed) {
		this.blocksDestroyed = blocksDestroyed;
	}

	public long getBlocksDestroyed() {
		return blocksDestroyed;
	}

	public void incBlocksDestroyed() {
		this.blocksDestroyed++ ;
	}
	// }}}

	// blocksSpread {{{
	public void setBlocksSpread(long blocksSpread) {
		this.blocksSpread = blocksSpread;
	}

	public long getBlocksSpread() {
		return this.blocksSpread;
	}

	public void incBlocksSpread() {
		this.blocksSpread++;
	}
	// }}}

	// blocksDecayed {{{
	public void setBlocksDecayed(long blocksDecayed) {
		this.blocksDecayed = blocksDecayed;
	}

	public long getBlocksDecayed() {
		return this.blocksDecayed;
	}

	public void incBlocksDecayed() {
		this.blocksDecayed++;
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
	    OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[4];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("blocksPlaced","Number of Total Blocks Placed",SimpleType.LONG, true, false,false);
		attributes[1] = new OpenMBeanAttributeInfoSupport("blocksDestroyed","Number of Total Blocks Destroyed",SimpleType.LONG, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("blocksSpread", "Number of Total Blocks Spread", SimpleType.LONG, true, false, false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("blocksDecayed", "Number of total leaves decayed", SimpleType.LONG, true, false, false);

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
		throw new AttributeNotFoundException("No attributes can be set on this MBean");
	}

	@Override
	public AttributeList setAttributes(AttributeList arg0) {
		return new AttributeList() ;
	}

	public String getMetricData() {
		return "blocksPlaced:"+this.blocksPlaced+
			   ",blocksDestroyed:"+this.blocksDestroyed+
			   ",blocksSpread:"+this.blocksSpread+
			   ",blocksDecayed:"+this.blocksDecayed  ;
	}

	public static BlockData instanceFromResultSet(ResultSet rs, MineJMX plugin) throws SQLException {
		BlockData bd = new BlockData(plugin) ;
		String data = rs.getString("data") ;
		if(data.length() <=0 ) {
			return bd ;
		}
		String[] datas = data.split(",") ;
		for(String s : datas) {
			String[] keyval = s.split(":") ;
			switch (keyval[0]) {
				case "blocksPlaced":
					bd.setBlocksPlaced(Integer.decode(keyval[1]));
					break;
				case "blocksDestroyed":
					bd.setBlocksDestroyed(Integer.decode(keyval[1]));
					break;
				case "blocksSpread":
					bd.setBlocksSpread(Integer.decode(keyval[1]));
					break;
				case "blocksDecayed":
					bd.setBlocksDecayed(Integer.decode(keyval[1]));
					break;
			}
		}
		return bd ;
	}
}

