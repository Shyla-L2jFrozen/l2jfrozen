/* L2jFrozen Project - www.l2jfrozen.com 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jfrozen.netcore;

/**
 * @author KenM
 * @param <T>
 */
public abstract class SendablePacket<T extends MMOClient<?>> extends AbstractPacket<T>
{
	public final void putInt(final int value)
	{
		_buf.putInt(value);
	}
	
	public final void putDouble(final double value)
	{
		_buf.putDouble(value);
	}
	
	public final void putFloat(final float value)
	{
		_buf.putFloat(value);
	}
	
	/**
	 * Write <B>byte</B> to the buffer. <BR>
	 * 8bit integer (00)
	 * @param data
	 */
	public final void writeC(final int data)
	{
		_buf.put((byte) data);
	}
	
	/**
	 * Write <B>double</B> to the buffer. <BR>
	 * 64bit double precision float (00 00 00 00 00 00 00 00)
	 * @param value
	 */
	public final void writeF(final double value)
	{
		_buf.putDouble(value);
	}
	
	/**
	 * Write <B>short</B> to the buffer. <BR>
	 * 16bit integer (00 00)
	 * @param value
	 */
	public final void writeH(final int value)
	{
		_buf.putShort((short) value);
	}
	
	/**
	 * Write <B>int</B> to the buffer. <BR>
	 * 32bit integer (00 00 00 00)
	 * @param value
	 */
	public final void writeD(final int value)
	{
		_buf.putInt(value);
	}
	
	/**
	 * Write <B>long</B> to the buffer. <BR>
	 * 64bit integer (00 00 00 00 00 00 00 00)
	 * @param value
	 */
	public final void writeQ(final long value)
	{
		_buf.putLong(value);
	}
	
	/**
	 * Write <B>byte[]</B> to the buffer. <BR>
	 * 8bit integer array (00 ...)
	 * @param data
	 */
	public final void writeB(final byte[] data)
	{
		_buf.put(data);
	}
	
	/**
	 * Write <B>String</B> to the buffer.
	 * @param text
	 */
	public final void writeS(final String text)
	{
		if (text != null)
		{
			final int len = text.length();
			for (int i = 0; i < len; i++)
			{
				_buf.putChar(text.charAt(i));
			}
		}
		
		_buf.putChar('\000');
	}
	
	public abstract void write();
}