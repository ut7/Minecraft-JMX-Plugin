package com.dkhenry.minejmx;

import java.io.File;
import java.io.IOException;
import java.lang.Class;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MineJMX extends JavaPlugin {
	Logger log = Logger.getLogger("Minecraft") ;

	/* The Listener Classes */
	private final MineJMXBlockListener blockListener = new MineJMXBlockListener(this) ;
	private final MineJMXPlayerListener playerListener = new MineJMXPlayerListener(this) ;
	//private final MineJMXServerListener serverListener = new MineJMXServerListener(this) ;
	private final MineJMXEntityListener entityListener = new MineJMXEntityListener(this) ;

	/* The JMx Specific Variables */
	private MBeanServer mbs ;

	/* The MBeans and their containers */
	public ServerData serverData ;
	public ServerPerformanceData serverPerformanceData ;
	public Map<String,PlayerData> playerData ;
	public Map<String,BlockData> blockData ;
	public Map<String,NpeData> npeData;

	private static String dir = "plugins";

	private ServerTickPoller tickPoller;

	/**
	 * @brief Since we don't want to make everyone modify their start script
	 * To Enable JMX we will do it programaticly
	 */
	private void enableJMX() {
		/* Enable the JMX Portion of the System */
		//acquiring platform MBeanServer
		mbs = ManagementFactory.getPlatformMBeanServer();
	}

	/**
	 * @brief This will add a new player and register him with the MBeanServer
	 * @param name The name of the player
	 * @param player The PlayerData Object or NULL to have one automatically created
	 */
	public void addPlayer(String name , PlayerData player) {
		if( player == null ) {
			player = new PlayerData(this) ;
		}
		// Register the MBean
		ObjectName oName ;
		try {
			oName = new ObjectName("org.dkhenry.minejmx:type=PlayerData,name="+name);
			if( mbs.isRegistered(oName) ) {
				mbs.unregisterMBean(oName) ;
			}
			mbs.registerMBean(player, oName) ;
		} catch (InstanceAlreadyExistsException | MBeanRegistrationException | MalformedObjectNameException | NotCompliantMBeanException | InstanceNotFoundException | NullPointerException e) {
			e.printStackTrace();
		}

		this.playerData.put(name, player) ;
	}

	public void addBlock(String mat, BlockData blockData) {
		if( blockData == null ) {
			blockData = new BlockData(this) ;
		}
		// Register the MBean
		ObjectName oName ;
		try {
			oName = new ObjectName("org.dkhenry.minejmx:type=BlockData,name="+ mat);
			if( mbs.isRegistered(oName) ) {
				mbs.unregisterMBean(oName) ;
			}
			mbs.registerMBean(blockData, oName) ;
		} catch (InstanceAlreadyExistsException | MBeanRegistrationException | MalformedObjectNameException | NotCompliantMBeanException | InstanceNotFoundException | NullPointerException e) {
			//e.printStackTrace();
		}

        this.blockData.put(mat, blockData) ;
	}

	public void addNpe(String className, NpeData data) {
		if(data == null) {
			data = new NpeData(this);
		}
		// Register the MBean
		ObjectName oName;
		try {
			oName = new ObjectName("org.dkhenry.minejmx:type=NpeData,name=" + className);
			if( mbs.isRegistered(oName) ) {
				mbs.unregisterMBean(oName) ;
			}
			mbs.registerMBean(data, oName) ;
		} catch (InstanceAlreadyExistsException | MBeanRegistrationException | MalformedObjectNameException | NotCompliantMBeanException | InstanceNotFoundException | NullPointerException e) {
			//e.printStackTrace();
		}

        this.npeData.put(className, data) ;
	}

	public PlayerData getPlayerData(String name, String logIfNotFound) {
		PlayerData playerData;
		if(this.playerData.containsKey(name)) {
			return this.playerData.get(name);
		}
		if(logIfNotFound.length() > 0) {
			this.log.info(logIfNotFound);
		}
		playerData = new PlayerData(this);
		this.addPlayer(name, playerData);
		return playerData;
	}

	public BlockData getBlockData(String mat, String logIfNotFound) {
		BlockData blockData;
		if(this.blockData.containsKey(mat)) {
			return this.blockData.get(mat);
		}
		if(logIfNotFound.length() > 0) {
			this.log.info(logIfNotFound);
		}
		blockData = new BlockData(this);
		this.addBlock(mat, blockData);
		return blockData;
	}

	public NpeData getNpeData(String type, String logIfNotFound) {
		NpeData npeData;
		if(this.npeData.containsKey(type)) {
			return this.npeData.get(type);
		}
		if(logIfNotFound.length() > 0) {
			this.log.info(logIfNotFound);
		}
		npeData = new NpeData(this);
		this.addNpe(type, npeData);
		return npeData;
	}

	public NpeData getNpeDataByClass(Class that) {
		String name = MineJMX.getSimpleClassName(that);
		if(name.startsWith("Craft")) {
			name = name.substring(5);
		}
		return this.getNpeData(name, "MineJMX is seeing non-player Entity type \"" + name + "\" for the first time.");
	}

	@Override
	public void onDisable() {
		log.info("The MineJMX Plugin has been disabled.") ;
	}

	@Override
	public void onEnable() {
		/* Do the Magic to Enable JMX  */
		enableJMX() ;

		this.serverData = new ServerData(this);
		this.serverPerformanceData = new ServerPerformanceData(this) ;
		this.playerData = new HashMap<>() ;
		this.blockData = new HashMap<>() ;
		this.npeData = new HashMap<>();

		ObjectName name;
		try {
			String serverName = Bukkit.getServer().getName();
			name = new ObjectName("org.dkhenry.minejmx:type=ServerData,name="+serverName);
			if (mbs.isRegistered(name) ) {
				mbs.unregisterMBean(name) ;
			}
			mbs.registerMBean(serverData, name) ;

			name = new ObjectName("org.dkhenry.minejmx:type=ServerPerformanceData,name="+serverName) ;
			if (mbs.isRegistered(name) ) {
				mbs.unregisterMBean(name) ;
			}
			mbs.registerMBean(serverPerformanceData, name) ;
		} catch (MalformedObjectNameException | NullPointerException | MBeanRegistrationException | InstanceAlreadyExistsException | InstanceNotFoundException | NotCompliantMBeanException e1) {
			//e1.printStackTrace();
		}

		/* Register the Listeners */
		PluginManager pm = this.getServer().getPluginManager() ;
		// The Block Events		
		pm.registerEvents(blockListener, this) ; 		

		// Player Events
		pm.registerEvents(playerListener, this) ; 				

		// Entity Events
		pm.registerEvents(entityListener, this) ;
		
		// Server Events
		this.tickPoller = new ServerTickPoller(this) ;
		this.tickPoller.setInterval(40) ;
		this.tickPoller.registerWithScheduler(getServer().getScheduler()) ;

		log.info("The MineJMX Plugin has been enabled.") ;
	}

	public static String getSimpleClassName(Class cls) {
		String name = cls.getName().replace('$', '.');
		if(name.lastIndexOf('.') > 0) {
			name = name.substring(name.lastIndexOf('.') + 1);
		}
		return name ;
	}
}

