
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

public class ServerPerformanceData implements DynamicMBean {

	private MineJMX plugin ; 
	
	private long serverTicks ; 	
	private long runningTasks ; 
	private long pendingTasks ; 
	private int ticksPerSecondAverage ; 
	private int ticksPerSecondInstantious ; 
	private int serverLag ; 
		
	public ServerPerformanceData(MineJMX instance) { 
		plugin = instance ; 
	}

	@Override
	public Object getAttribute(String arg0) throws AttributeNotFoundException,
			MBeanException, ReflectionException {
		switch (arg0) {
			case "serverTicks":
				return getServerTicks();
			case "runningTasks":
				return getRunningTasks();
			case "pendingTasks":
				return getPendingTasks();
			case "ticksPerSecondAverage":
				return getTicksPerSecondAverage();
			case "ticksPerSecondInstantious":
				return getTicksPerSecondInstantious();
			case "serverLag":
				return getServerLag();
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
		OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[6];

		//Build the Attributes
		attributes[0] = new OpenMBeanAttributeInfoSupport("serverTicks","Number or Total Server Ticks",SimpleType.LONG, true, false,false);		
		attributes[1] = new OpenMBeanAttributeInfoSupport("runningTasks","Number or Running Tasks on this Server",SimpleType.LONG, true, false,false);
		attributes[2] = new OpenMBeanAttributeInfoSupport("pendingTasks","Number or Total Server Ticks",SimpleType.LONG, true, false,false);
		attributes[3] = new OpenMBeanAttributeInfoSupport("ticksPerSecondAverage","Average Number of Ticks Per Second ",SimpleType.INTEGER, true, false,false);
		attributes[4] = new OpenMBeanAttributeInfoSupport("ticksPerSecondInstantious","Number of Ticks Per Second ",SimpleType.INTEGER, true, false,false);
		attributes[5] = new OpenMBeanAttributeInfoSupport("serverLag","Generic Server Lag Indicator ( In Ticks per Second )",SimpleType.INTEGER, true, false,false);
		
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

	public long getServerTicks() {
		return serverTicks;
	}

	public void setServerTicks(long serverTicks) {
		this.serverTicks = serverTicks;
	}
	
	public void addTicks(long ServerTicks) { 
		this.serverTicks+=ServerTicks ; 
	}

	public long getRunningTasks() {
		return plugin.getServer().getScheduler().getActiveWorkers().size() ; 
	}

	public void setRunningTasks(long runningTasks) {		
	}

	public long getPendingTasks() {
		return plugin.getServer().getScheduler().getPendingTasks().size() ; 
	}

	public void setPendingTasks(long pendingTasks) {		
	}

	public int getTicksPerSecondAverage() {
		return ticksPerSecondAverage;
	}

	public void setTicksPerSecondAverage(int ticksPerSecondAverage) {
		this.ticksPerSecondAverage = ticksPerSecondAverage;
	}

	public int getTicksPerSecondInstantious() {
		return ticksPerSecondInstantious;
	}

	public void setTicksPerSecondInstantious(int ticksPerSecondInstantious) {
		this.ticksPerSecondInstantious = ticksPerSecondInstantious;
	}
	
	public void setTickRate(int ticksPerSecond) { 
		this.ticksPerSecondInstantious = ticksPerSecond ; 
		this.ticksPerSecondAverage = (this.ticksPerSecondAverage*19 + ticksPerSecond) / 20 ;  
	}

	public int getServerLag() {		
		return this.ticksPerSecondAverage - this.ticksPerSecondInstantious  ;  		
	}

	public void setServerLag(int serverLag) {
		
	}
	
	public String getMetricData() {		 
		String rvalue = "" ;
		return "serverTicks:"+this.serverTicks+
				",runningTasks:"+this.runningTasks+
				",pendingTasks:"+this.pendingTasks+
				",ticksPerSecondAverage:"+this.ticksPerSecondAverage+
				",ticksPerSecondInstantious:"+this.ticksPerSecondInstantious+
				",serverLag:"+this.serverLag ; 				
	}
}
