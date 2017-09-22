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
package com.l2jfrozen.loginserver.network;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.common.util.random.Rnd;
import com.l2jfrozen.loginserver.LoginController;
import com.l2jfrozen.loginserver.network.serverpackets.L2LoginServerPacket;
import com.l2jfrozen.loginserver.network.serverpackets.LoginFail;
import com.l2jfrozen.loginserver.network.serverpackets.LoginFailReason;
import com.l2jfrozen.loginserver.network.serverpackets.PlayFail;
import com.l2jfrozen.loginserver.network.serverpackets.PlayFailReason;
import com.l2jfrozen.netcore.MMOClient;
import com.l2jfrozen.netcore.MMOConnection;
import com.l2jfrozen.netcore.SessionKey;
import a.a.t;
import a.a.v;
import a.a.y;
/**
 * Represents a client connected into the LoginServer
 * @author KenM
 */
public final class L2LoginClient extends MMOClient<MMOConnection<L2LoginClient>>
{
	private static final Logger _log = Logger.getLogger(L2LoginClient.class.getName());
	
	private LoginClientState _state;
	
	// Crypt
	private final t _t;
	private final v _scrambledPair;
	private final byte[] _blowfishKey;
	
	private String _account;
	private int _accessLevel;
	private int _lastServer;
	private SessionKey _sessionKey;
	private final int _sessionId;
	private boolean _joinedGS;
	private Map<Integer, Integer> _charsOnServers;
	private Map<Integer, long[]> _charsToDelete;
	
	private final long _connectionStartTime;
	
	/**
	 * @param con
	 */
	public L2LoginClient(final MMOConnection<L2LoginClient> con)
	{
		super(con);
		_state = LoginClientState.CONNECTED;
		_scrambledPair = LoginController.getInstance().getScrambledRSAKeyPair();
		_blowfishKey = LoginController.getInstance().getBlowfishKey();
		_sessionId = Rnd.nextInt();
		_connectionStartTime = System.currentTimeMillis();
		_t = new t();
		_t.setKey(_blowfishKey);
	}
	
	@Override
	public boolean decrypt(final ByteBuffer buf, final int size)
	{
		boolean isChecksumValid = false;
		try
		{
			isChecksumValid = _t.a(buf.array(), buf.position(), size);
			if (!isChecksumValid)
			{
				_log.warning("Wrong checksum from client: " + toString());
				super.getConnection().close((y<L2LoginClient>) null);
				return false;
			}
			return true;
		}
		catch (final Exception e)
		{
			_log.warning(getClass().getSimpleName() + ": " + e.getMessage());
			super.getConnection().close((y<L2LoginClient>) null);
			return false;
		}
	}
	
	@Override
	public boolean encrypt(final ByteBuffer buf, int size)
	{
		final int offset = buf.position();
		try
		{
			size = _t.b(buf.array(), offset, size);
		}
		catch (final Exception e)
		{
			_log.warning(getClass().getSimpleName() + ": " + e.getMessage());
			return false;
		}
		buf.position(offset + size);
		return true;
	}
	
	public LoginClientState getState()
	{
		return _state;
	}
	
	public void setState(final LoginClientState state)
	{
		_state = state;
	}
	
	public byte[] getBlowfishKey()
	{
		return _blowfishKey;
	}
	
	public byte[] getScrambledModulus()
	{
		return _scrambledPair.ai;
	}
	
	public RSAPrivateKey getRSAPrivateKey()
	{
		return (RSAPrivateKey) _scrambledPair.ah.getPrivate();
	}
	
	public String getAccount()
	{
		return _account;
	}
	
	public void setAccount(final String account)
	{
		_account = account;
	}
	
	public void setAccessLevel(final int accessLevel)
	{
		_accessLevel = accessLevel;
	}
	
	public int getAccessLevel()
	{
		return _accessLevel;
	}
	
	public void setLastServer(final int lastServer)
	{
		_lastServer = lastServer;
	}
	
	public int getLastServer()
	{
		return _lastServer;
	}
	
	public int getSessionId()
	{
		return _sessionId;
	}
	
	public boolean hasJoinedGS()
	{
		return _joinedGS;
	}
	
	public void setJoinedGS(final boolean val)
	{
		_joinedGS = val;
	}
	
	public void setSessionKey(final SessionKey sessionKey)
	{
		_sessionKey = sessionKey;
	}
	
	public SessionKey getSessionKey()
	{
		return _sessionKey;
	}
	
	public long getConnectionStartTime()
	{
		return _connectionStartTime;
	}
	
	public void sendPacket(final L2LoginServerPacket lsp)
	{
		getConnection().sendPacket(lsp);
	}
	
	public void close(final LoginFailReason reason)
	{
		getConnection().close(new LoginFail(reason));
	}
	
	public void close(final PlayFailReason reason)
	{
		getConnection().close(new PlayFail(reason));
	}
	
	public void close(final L2LoginServerPacket lsp)
	{
		getConnection().close(lsp);
	}
	
	public void setCharsOnServ(final int servId, final int chars)
	{
		if (_charsOnServers == null)
		{
			_charsOnServers = new HashMap<>();
		}
		_charsOnServers.put(servId, chars);
	}
	
	public Map<Integer, Integer> getCharsOnServ()
	{
		return _charsOnServers;
	}
	
	public void serCharsWaitingDelOnServ(final int servId, final long[] charsToDel)
	{
		if (_charsToDelete == null)
		{
			_charsToDelete = new HashMap<>();
		}
		_charsToDelete.put(servId, charsToDel);
	}
	
	public Map<Integer, long[]> getCharsWaitingDelOnServ()
	{
		return _charsToDelete;
	}
	
	@Override
	public void onDisconnection()
	{
		if (CommonConfig.DEBUG)
		{
			_log.info("DISCONNECTED: " + toString());
		}
		
		if (!hasJoinedGS() || ((getConnectionStartTime() + LoginController.LOGIN_TIMEOUT) < System.currentTimeMillis()))
		{
			LoginController.getInstance().removeAuthedLoginClient(getAccount());
		}
	}
	
	@Override
	public String toString()
	{
		final InetAddress address = getConnection().getInetAddress();
		if (getState() == LoginClientState.AUTHED_LOGIN)
		{
			return "[" + getAccount() + " (" + (address == null ? "disconnected" : address.getHostAddress()) + ")]";
		}
		return "[" + (address == null ? "disconnected" : address.getHostAddress()) + "]";
	}
	
	@Override
	protected void onForcedDisconnection(final boolean critical)
	{
		// empty
	}
	
}
