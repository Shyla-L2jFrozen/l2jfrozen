/*
 * Copyright (C) 2004-2016 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfrozen.gameserver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.common.crypt.NewCrypt;
import com.l2jfrozen.common.util.random.Rnd;
import com.l2jfrozen.gameserver.config.Config;
import com.l2jfrozen.gameserver.config.FService;
import com.l2jfrozen.gameserver.model.L2World;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.network.GameClientState;
import com.l2jfrozen.gameserver.network.L2GameClient;
import com.l2jfrozen.gameserver.network.gameserverpackets.AuthRequest;
import com.l2jfrozen.gameserver.network.gameserverpackets.BlowFishKey;
import com.l2jfrozen.gameserver.network.gameserverpackets.ChangeAccessLevel;
//import com.l2jfrozen.gameserver.network.gameserverpackets.ChangePassword;
import com.l2jfrozen.gameserver.network.gameserverpackets.PlayerAuthRequest;
import com.l2jfrozen.gameserver.network.gameserverpackets.PlayerInGame;
import com.l2jfrozen.gameserver.network.gameserverpackets.PlayerLogout;
import com.l2jfrozen.gameserver.network.gameserverpackets.PlayerTracert;
//import com.l2jfrozen.gameserver.network.gameserverpackets.ReplyCharacters;
import com.l2jfrozen.gameserver.network.gameserverpackets.ServerStatus;
import com.l2jfrozen.gameserver.network.loginserverpackets.AuthResponse;
//import com.l2jfrozen.gameserver.network.loginserverpackets.ChangePasswordResponse;
import com.l2jfrozen.gameserver.network.loginserverpackets.InitLS;
import com.l2jfrozen.gameserver.network.loginserverpackets.KickPlayer;
import com.l2jfrozen.gameserver.network.loginserverpackets.LoginServerFail;
import com.l2jfrozen.gameserver.network.loginserverpackets.PlayerAuthResponse;
import com.l2jfrozen.gameserver.network.serverpackets.AuthLoginFail;
import com.l2jfrozen.gameserver.network.serverpackets.CharSelectInfo;
import com.l2jfrozen.netcore.SessionKey;
import com.l2jfrozen.netcore.util.network.BaseSendablePacket;

public class LoginServerThread extends Thread
{
	protected static final Logger LOGGER = Logger.getLogger(LoginServerThread.class);
	protected static final Logger LOGGER_ACCOUNTING = Logger.getLogger("accounting");
	
	private static final int REVISION = 0x0102;
	private final String _hostname;
	private final int _port;
	private final int _gamePort;
	private Socket _loginSocket;
	private OutputStream _out;
	
	/**
	 * The BlowFish engine used to encrypt packets<br>
	 * It is first initialized with a unified key:<br>
	 * "_;v.]05-31!|+-%xT!^[$\00"<br>
	 * <br>
	 * and then after handshake, with a new key sent by<br>
	 * login server during the handshake. This new key is stored<br>
	 * in blowfishKey
	 */
	private NewCrypt _blowfish;
	private byte[] _hexID;
	private final boolean _acceptAlternate;
	private int _requestID;
	private final boolean _reserveHost;
	private int _maxPlayer;
	private final List<WaitingClient> _waitingClients;
	private final Map<String, L2GameClient> _accountsInGameServer = new ConcurrentHashMap<>();
	private int _status;
	private String _serverName;
	// private final List<String> _subnets;
	// private final List<String> _hosts;
	private final String _gameExternalHost;
	private final String _gameInternalHost;
	
	/**
	 * Instantiates a new login server thread.
	 */
	protected LoginServerThread()
	{
		super("LoginServerThread");
		_port = Config.GAME_SERVER_LOGIN_PORT;
		_gamePort = Config.PORT_GAME;
		_hostname = Config.GAME_SERVER_LOGIN_HOST;
		_hexID = Config.HEX_ID;
		if (_hexID == null)
		{
			_requestID = Config.REQUEST_ID;
			_hexID = generateHex(16);
		}
		else
		{
			_requestID = Config.SERVER_ID;
		}
		_acceptAlternate = Config.ACCEPT_ALTERNATE_ID;
		_reserveHost = Config.RESERVE_HOST_ON_LOGIN;
		// _subnets = Config.GAME_SERVER_SUBNETS;
		// _hosts = Config.GAME_SERVER_HOSTS;
		_waitingClients = new CopyOnWriteArrayList<>();
		_maxPlayer = Config.MAXIMUM_ONLINE_USERS;
		
		_gameExternalHost = Config.EXTERNAL_HOSTNAME;
		_gameInternalHost = Config.INTERNAL_HOSTNAME;
		
	}
	
	@Override
	public void run()
	{
		while (!isInterrupted())
		{
			int lengthHi = 0;
			int lengthLo = 0;
			int length = 0;
			boolean checksumOk = false;
			try
			{
				// Connection
				LOGGER.info("Connecting to login on {" + _hostname + "}:{" + _port + "}");
				_loginSocket = new Socket(_hostname, _port);
				final InputStream in = _loginSocket.getInputStream();
				_out = new BufferedOutputStream(_loginSocket.getOutputStream());
				
				// init Blowfish
				final byte[] blowfishKey = generateHex(40);
				// Protect the new blowfish key what cannot begin with zero
				if (blowfishKey[0] == 0)
				{
					blowfishKey[0] = (byte) Rnd.get(32, 64);
				}
				_blowfish = new NewCrypt("_;v.]05-31!|+-%xT!^[$\00");
				while (!isInterrupted())
				{
					lengthLo = in.read();
					lengthHi = in.read();
					length = (lengthHi * 256) + lengthLo;
					
					if (lengthHi < 0)
					{
						LOGGER.info("LoginServerThread: Login terminated the connection.");
						break;
					}
					
					final byte[] incoming = new byte[length - 2];
					
					int receivedBytes = 0;
					int newBytes = 0;
					int left = length - 2;
					while ((newBytes != -1) && (receivedBytes < (length - 2)))
					{
						newBytes = in.read(incoming, receivedBytes, left);
						receivedBytes = receivedBytes + newBytes;
						left -= newBytes;
					}
					
					if (receivedBytes != (length - 2))
					{
						LOGGER.warn("Incomplete Packet is sent to the server, closing connection.(LS)");
						break;
					}
					
					// decrypt if we have a key
					_blowfish.decrypt(incoming, 0, incoming.length);
					checksumOk = NewCrypt.verifyChecksum(incoming);
					
					if (!checksumOk)
					{
						LOGGER.warn("Incorrect packet checksum, ignoring packet (LS)");
						break;
					}
					
					final int packetType = incoming[0] & 0xff;
					switch (packetType)
					{
						case 0x00:
							final InitLS init = new InitLS(incoming);
							if (init.getRevision() != REVISION)
							{
								// TODO: revision mismatch
								LOGGER.warn("/!\\ Revision mismatch between LS and GS /!\\");
								break;
							}
							
							RSAPublicKey publicKey;
							
							try
							{
								final KeyFactory kfac = KeyFactory.getInstance("RSA");
								final BigInteger modulus = new BigInteger(init.getRSAKey());
								final RSAPublicKeySpec kspec1 = new RSAPublicKeySpec(modulus, RSAKeyGenParameterSpec.F4);
								publicKey = (RSAPublicKey) kfac.generatePublic(kspec1);
							}
							catch (final GeneralSecurityException e)
							{
								LOGGER.warn("Trouble while init the public key send by login");
								break;
							}
							// send the blowfish key through the rsa encryption
							sendPacket(new BlowFishKey(blowfishKey, publicKey));
							// now, only accept packet with the new encryption
							_blowfish = new NewCrypt(blowfishKey);
							
							final AuthRequest ar = new AuthRequest(_requestID, _acceptAlternate, _hexID, _gameExternalHost, _gameInternalHost, _gamePort, _reserveHost, _maxPlayer);
							sendPacket(ar);
							// sendPacket(new AuthRequest(_requestID, _acceptAlternate, _hexID, _gamePort, _reserveHost, _maxPlayer));
							break;
						case 0x01:
							final LoginServerFail lsf = new LoginServerFail(incoming);
							LOGGER.info("Damn! Registeration Failed: {" + lsf.getReasonString() + "}");
							// login will close the connection here
							break;
						case 0x02:
							final AuthResponse aresp = new AuthResponse(incoming);
							final int serverID = aresp.getServerId();
							_serverName = aresp.getServerName();
							saveHexid(serverID, hexToString(_hexID), FService.HEXID_FILE);
							LOGGER.info("Registered on login as Server {" + serverID + "}: {" + _serverName + "}");
							final ServerStatus st = new ServerStatus();
							if (Config.SERVER_LIST_BRACKET)
							{
								st.addAttribute(ServerStatus.SERVER_LIST_SQUARE_BRACKET, ServerStatus.ON);
							}
							else
							{
								st.addAttribute(ServerStatus.SERVER_LIST_SQUARE_BRACKET, ServerStatus.OFF);
							}
							st.addAttribute(ServerStatus.SERVER_TYPE, Config.SERVER_LIST_TYPE);
							if (Config.SERVER_GMONLY)
							{
								st.addAttribute(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_GM_ONLY);
							}
							else
							{
								st.addAttribute(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_AUTO);
							}
							if (Config.SERVER_LIST_AGE == 15)
							{
								st.addAttribute(ServerStatus.SERVER_AGE, ServerStatus.SERVER_AGE_15);
							}
							else if (Config.SERVER_LIST_AGE == 18)
							{
								st.addAttribute(ServerStatus.SERVER_AGE, ServerStatus.SERVER_AGE_18);
							}
							else
							{
								st.addAttribute(ServerStatus.SERVER_AGE, ServerStatus.SERVER_AGE_ALL);
							}
							sendPacket(st);
							if (L2World.getInstance().getAllPlayersCount() > 0)
							{
								final List<String> playerList = new ArrayList<>();
								for (final L2PcInstance player : L2World.getInstance().getAllPlayers())
								{
									playerList.add(player.getAccountName());
								}
								sendPacket(new PlayerInGame(playerList));
							}
							break;
						case 0x03:
							final PlayerAuthResponse par = new PlayerAuthResponse(incoming);
							final String account = par.getAccount();
							WaitingClient wcToRemove = null;
							synchronized (_waitingClients)
							{
								for (final WaitingClient wc : _waitingClients)
								{
									if (wc.account.equals(account))
									{
										wcToRemove = wc;
									}
								}
							}
							if (wcToRemove != null)
							{
								if (par.isAuthed())
								{
									final PlayerInGame pig = new PlayerInGame(par.getAccount());
									sendPacket(pig);
									wcToRemove.gameClient.setState(GameClientState.AUTHED);
									wcToRemove.gameClient.setSessionId(wcToRemove.session);
									final CharSelectInfo cl = new CharSelectInfo(wcToRemove.account, wcToRemove.gameClient.getSessionId().playOkID1);
									wcToRemove.gameClient.getConnection().sendPacket(cl);
									wcToRemove.gameClient.setCharSelection(cl.getCharInfo());
								}
								else
								{
									LOGGER.warn("Session key is not correct. Closing connection for account {" + wcToRemove.account + "}.");
									wcToRemove.gameClient.getConnection().sendPacket(new AuthLoginFail(1));
									wcToRemove.gameClient.closeNow();
									_accountsInGameServer.remove(wcToRemove.account);
								}
								_waitingClients.remove(wcToRemove);
							}
							break;
						case 0x04:
							final KickPlayer kp = new KickPlayer(incoming);
							doKickPlayer(kp.getAccount());
							break;
						// case 0x05:
						// RequestCharacters rc = new RequestCharacters(incoming);
						// getCharsOnServer(rc.getAccount());
						// break;
						// case 0x06:
						// new ChangePasswordResponse(incoming);
						// break;
					}
				}
			}
			catch (final UnknownHostException e)
			{
				LOGGER.warn("Unknown host!", e);
			}
			catch (final SocketException e)
			{
				LOGGER.warn("LoginServer not avaible, trying to reconnect...");
			}
			catch (final IOException e)
			{
				LOGGER.warn("Disconnected from Login, Trying to reconnect!", e);
			}
			finally
			{
				try
				{
					_loginSocket.close();
					if (isInterrupted())
					{
						return;
					}
				}
				catch (final Exception e)
				{
				}
			}
			
			try
			{
				Thread.sleep(5000); // 5 seconds tempo.
			}
			catch (final InterruptedException e)
			{
				return; // never swallow an interrupt!
			}
		}
	}
	
	public static byte[] generateHex(final int size)
	{
		final byte[] array = new byte[size];
		Rnd.nextBytes(array);
		if (CommonConfig.DEBUG)
		{
			LOGGER.debug("Generated random String:  \"" + array + "\"");
		}
		return array;
	}
	
	/**
	 * Adds the waiting client and send request.
	 * @param acc the account
	 * @param client the game client
	 * @param key the session key
	 */
	public void addWaitingClientAndSendRequest(final String acc, final L2GameClient client, final SessionKey key)
	{
		final WaitingClient wc = new WaitingClient(acc, client, key);
		synchronized (_waitingClients)
		{
			_waitingClients.add(wc);
		}
		final PlayerAuthRequest par = new PlayerAuthRequest(acc, key);
		try
		{
			sendPacket(par);
		}
		catch (final IOException e)
		{
			LOGGER.warn("Error while sending player auth request!");
		}
	}
	
	/**
	 * Removes the waiting client.
	 * @param client the client
	 */
	public void removeWaitingClient(final L2GameClient client)
	{
		WaitingClient toRemove = null;
		synchronized (_waitingClients)
		{
			for (final WaitingClient c : _waitingClients)
			{
				if (c.gameClient == client)
				{
					toRemove = c;
				}
			}
			if (toRemove != null)
			{
				_waitingClients.remove(toRemove);
			}
		}
	}
	
	/**
	 * Send logout for the given account.
	 * @param account the account
	 */
	public void sendLogout(final String account)
	{
		if (account == null)
		{
			return;
		}
		final PlayerLogout pl = new PlayerLogout(account);
		try
		{
			sendPacket(pl);
		}
		catch (final IOException e)
		{
			LOGGER.warn("Error while sending logout packet to login!");
		}
		finally
		{
			_accountsInGameServer.remove(account);
		}
	}
	
	/**
	 * Adds the game server login.
	 * @param account the account
	 * @param client the client
	 * @return {@code true} if account was not already logged in, {@code false} otherwise
	 */
	public boolean addGameServerLogin(final String account, final L2GameClient client)
	{
		return _accountsInGameServer.putIfAbsent(account, client) == null;
	}
	
	/**
	 * Send access level.
	 * @param account the account
	 * @param level the access level
	 */
	public void sendAccessLevel(final String account, final int level)
	{
		final ChangeAccessLevel cal = new ChangeAccessLevel(account, level);
		try
		{
			sendPacket(cal);
		}
		catch (final IOException e)
		{
		}
	}
	
	/**
	 * Send client tracert.
	 * @param account the account
	 * @param address the address
	 */
	public void sendClientTracert(final String account, final String[] address)
	{
		final PlayerTracert ptc = new PlayerTracert(account, address[0], address[1], address[2], address[3], address[4]);
		try
		{
			sendPacket(ptc);
		}
		catch (final IOException e)
		{
		}
	}
	
	/**
	 * Hex to string.
	 * @param hex the hex value
	 * @return the hex value as string
	 */
	private String hexToString(final byte[] hex)
	{
		return new BigInteger(hex).toString(16);
	}
	
	/**
	 * Kick player for the given account.
	 * @param account the account
	 */
	public void doKickPlayer(final String account)
	{
		final L2GameClient client = _accountsInGameServer.get(account);
		if (client != null)
		{
			if (Config.DEVELOPER)
				LOGGER_ACCOUNTING.info("Kicked by login: {" + client + "}");
			
			client.closeNow();
		}
	}
	
	/**
	 * Send packet.
	 * @param sl the sendable packet
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void sendPacket(final BaseSendablePacket sl) throws IOException
	{
		final byte[] data = sl.getContent();
		NewCrypt.appendChecksum(data);
		_blowfish.crypt(data, 0, data.length);
		
		final int len = data.length + 2;
		synchronized (_out) // avoids tow threads writing in the mean time
		{
			_out.write(len & 0xff);
			_out.write((len >> 8) & 0xff);
			_out.write(data);
			_out.flush();
		}
	}
	
	/**
	 * Sets the max player.
	 * @param maxPlayer The maxPlayer to set.
	 */
	public void setMaxPlayer(final int maxPlayer)
	{
		sendServerStatus(ServerStatus.MAX_PLAYERS, maxPlayer);
		_maxPlayer = maxPlayer;
	}
	
	/**
	 * Gets the max player.
	 * @return Returns the maxPlayer.
	 */
	public int getMaxPlayer()
	{
		return _maxPlayer;
	}
	
	/**
	 * Send server status.
	 * @param id the id
	 * @param value the value
	 */
	public void sendServerStatus(final int id, final int value)
	{
		final ServerStatus ss = new ServerStatus();
		ss.addAttribute(id, value);
		try
		{
			sendPacket(ss);
		}
		catch (final IOException e)
		{
		}
	}
	
	/**
	 * Send Server Type Config to LS.
	 */
	public void sendServerType()
	{
		final ServerStatus ss = new ServerStatus();
		ss.addAttribute(ServerStatus.SERVER_TYPE, Config.SERVER_LIST_TYPE);
		try
		{
			sendPacket(ss);
		}
		catch (final IOException e)
		{
		}
	}
	
	// /**
	// * Send change password.
	// * @param accountName the account name
	// * @param charName the char name
	// * @param oldpass the old pass
	// * @param newpass the new pass
	// */
	// public void sendChangePassword(String accountName, String charName, String oldpass, String newpass)
	// {
	// ChangePassword cp = new ChangePassword(accountName, charName, oldpass, newpass);
	// try
	// {
	// sendPacket(cp);
	// }
	// catch (IOException e)
	// {
	// }
	// }
	
	/**
	 * Gets the status string.
	 * @return the status string
	 */
	public String getStatusString()
	{
		return ServerStatus.STATUS_STRING[_status];
	}
	
	/**
	 * Gets the server name.
	 * @return the server name.
	 */
	public String getServerName()
	{
		return _serverName;
	}
	
	/**
	 * Sets the server status.
	 * @param status the new server status
	 */
	public void setServerStatus(final int status)
	{
		switch (status)
		{
			case ServerStatus.STATUS_AUTO:
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_AUTO);
				_status = status;
				break;
			case ServerStatus.STATUS_DOWN:
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_DOWN);
				_status = status;
				break;
			case ServerStatus.STATUS_FULL:
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_FULL);
				_status = status;
				break;
			case ServerStatus.STATUS_GM_ONLY:
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_GM_ONLY);
				_status = status;
				break;
			case ServerStatus.STATUS_GOOD:
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_GOOD);
				_status = status;
				break;
			case ServerStatus.STATUS_NORMAL:
				sendServerStatus(ServerStatus.SERVER_LIST_STATUS, ServerStatus.STATUS_NORMAL);
				_status = status;
				break;
			default:
				throw new IllegalArgumentException("Status does not exists:" + status);
		}
	}
	
	public L2GameClient getClient(final String name)
	{
		return name != null ? _accountsInGameServer.get(name) : null;
	}
	
	private static class WaitingClient
	{
		public String account;
		public L2GameClient gameClient;
		public SessionKey session;
		
		/**
		 * Instantiates a new waiting client.
		 * @param acc the acc
		 * @param client the client
		 * @param key the key
		 */
		public WaitingClient(final String acc, final L2GameClient client, final SessionKey key)
		{
			account = acc;
			gameClient = client;
			session = key;
		}
	}
	
	/**
	 * Gets the single instance of LoginServerThread.
	 * @return single instance of LoginServerThread
	 */
	public static LoginServerThread getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final LoginServerThread _instance = new LoginServerThread();
	}
	
	private static void saveHexid(final int serverId, final String hexId, final String fileName)
	{
		OutputStream out = null;
		try
		{
			final Properties hexSetting = new Properties();
			final File file = new File(fileName);
			if (file.createNewFile())
			{
				out = new FileOutputStream(file);
				hexSetting.setProperty("ServerID", String.valueOf(serverId));
				hexSetting.setProperty("HexID", hexId);
				hexSetting.store(out, "the hexID to auth into login");
			}
		}
		catch (final Exception e)
		{
			LOGGER.warn("Failed to save hex id to " + fileName + " File.");
			e.printStackTrace();
		}
		finally
		{
			
			if (out != null)
				try
				{
					out.close();
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			
		}
	}
}
