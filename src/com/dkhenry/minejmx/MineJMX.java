package com.dkhenry.minejmx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXPrincipal;
import javax.management.remote.JMXServiceURL;
import javax.security.auth.Subject;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
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
	private JMXConnectorServer cs ;
	private Registry reg ;

	/* The MBeans and their containers */
	public ServerData serverData ;
	public Map<String,PlayerData> playerData ;
	public Map<Material,BlockData> blockData ;

	/* The Configure variables */
	private String username = "admin" ;
	private String passwd = "passwd" ;
	private int port = 9999 ;
	private String ip = "*" ;
	private static String dir = "plugins" ;
	private static File Config = new File(dir + File.separator + "MineJMX.properties") ;
	private static Properties prop = new Properties() ;


	/* Class to enable Password Based JMx Authentication */
	private class JmxAuthenticatorImple implements JMXAuthenticator {

		@Override
		public Subject authenticate(Object credentials) {
			String[] info = (String[]) credentials ;
			if( info[0].equals(username) && info[1].equals(passwd)) {
				Subject s = new Subject() ;
				s.getPrincipals().add(new JMXPrincipal(info[0])) ;
				return s ;
			} else {
				throw new SecurityException() ;
			}
		}

	}

	private JmxAuthenticatorImple auth = new JmxAuthenticatorImple() ;

	/**
	 * @brief This Function handles Loading the Configuration
	 */
	private void loadConfig() {
		/* Read in the Properties File */
		if(! Config.exists()) {
			try {
				Config.createNewFile() ;
				FileOutputStream out = new FileOutputStream(Config) ;
				prop.put("username","admin") ;
				prop.put("password","passwd") ;
				prop.put("port","9999") ;
				prop.put("ip","") ;
				prop.store(out, "Autogenerated Config File") ;
				out.flush();
				out.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			FileInputStream in;
			try {
				in = new FileInputStream(Config);
				prop.load(in) ;
				this.username = prop.getProperty("username") ;
				this.passwd = prop.getProperty("password") ;
				this.port = Integer.parseInt(prop.getProperty("port")) ;
				this.ip = prop.getProperty("ip") ;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	/**
	 * @brief Since we don't want to make everyone modify their start script
	 * To Enable JMX we will do it programaticly
	 */
	private void enableJMX() {
		/* Enable the JMX Portion of the System */
		//acquiring platform MBeanServer
		mbs = ManagementFactory.getPlatformMBeanServer();
		if( null == mbs ) {
			log.info("Platform MBean Server isnull creating a new mbs") ;
			mbs = MBeanServerFactory.createMBeanServer();
		}

		//creating JMXConnectorServer instance
		JMXServiceURL url;

		try {
			String addr = "127.0.0.1" ;
			if ( this.ip.equals("")) {
				addr = Bukkit.getServer().getIp() ;
				log.info("MineJMX: Using Minecraft Server IP of: " + addr) ;
			} else if (this.ip.equals("*")) {
				addr = InetAddress.getLocalHost().getHostAddress() ;
				log.info("MineJMX: Using localhostname IP of: " + addr) ;
			} else {
				addr = this.ip ;
				log.info("MineJMX: Using Configured IP of: " + addr) ;
			}
			url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"
			      + addr + ":" + this.port + "/jmxrmi");
			Map<String, Object> env = new HashMap<String,Object>() ;
			env.put(JMXConnectorServer.AUTHENTICATOR, auth) ;
			log.info("Registering JMX Server On: " + url.toString()) ;
			cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env , mbs);
			reg = LocateRegistry.createRegistry(this.port);

			//starting JMXConnectorServer
			cs.start();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @brief This will add a new player and register him with the MBeanServer
	 * @param name The name of the player
	 * @param player The PlayerData Object or NULL to have one automatically created
	 */
	public void addPlayer(String name , PlayerData player) {
		if( player == null ) {
			player = new PlayerData() ;
		}
		// Register the MBean
		ObjectName oName ;
		try {
			oName = new ObjectName("org.dkhenry.minejmxjmx:type=PlayerData,name="+name);
			if( mbs.isRegistered(oName) ) {
				mbs.unregisterMBean(oName) ;
			}
			mbs.registerMBean(player, oName) ;
		} catch (InstanceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		}

		this.playerData.put(name, player) ;
	}

	public void addBlock(Material mat, BlockData blockData) {
		String name = mat.name() ;
		if( blockData == null ) {
			blockData = new BlockData() ;
		}
		// Register the MBean
		ObjectName oName ;
		try {
			oName = new ObjectName("org.dkhenry.minejmxjmx:type=BlockData,name="+name);
			if( mbs.isRegistered(oName) ) {
				mbs.unregisterMBean(oName) ;
			}
			mbs.registerMBean(blockData, oName) ;
		} catch (InstanceAlreadyExistsException e) {
			//e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			//e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			//e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			//e.printStackTrace();
		} catch (NullPointerException e) {
			//e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			//e.printStackTrace();
		}

		this.blockData.put(mat, blockData) ;
	}

	@Override
	public void onDisable() {
		//stopping JMXConnectorServer
		try {
			cs.stop();
			java.rmi.server.UnicastRemoteObject.unexportObject(reg,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("The MineJMX Plugin has been disabled.") ;
	}

	@Override
	public void onEnable() {
		/* Load our Configuration File */
		loadConfig() ;

		/* Do the Magic to Enable JMX  */
		enableJMX() ;


		playerData = new HashMap<String,PlayerData>() ;
		blockData = new HashMap<Material,BlockData>() ;
		serverData = new ServerData() ;

		ObjectName name;
		try {
			String serverName = Bukkit.getServer().getName();
			name = new ObjectName("org.dkhenry.minejmxjmx:type=ServerData,name="+serverName);
			if (mbs.isRegistered(name) ) {
				mbs.unregisterMBean(name) ;
			}
			mbs.registerMBean(serverData, name) ;
		} catch (MalformedObjectNameException e1) {
			//e1.printStackTrace();
		} catch (NullPointerException e1) {
			//e1.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			//e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			//e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			//e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Register the Listeners */
		PluginManager pm = this.getServer().getPluginManager() ;
		// The Block Events
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal ,this) ;
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal ,this) ;

		// Player Events
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal ,this) ;
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal ,this) ;

		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal ,this) ;
		// Server Events


		log.info("The MineJMX Plugin has been enabled.") ;
	}


}
