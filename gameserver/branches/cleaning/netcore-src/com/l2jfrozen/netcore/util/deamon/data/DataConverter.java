/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfrozen.netcore.util.deamon.data;

import java.io.Serializable;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * @author Shyla
 */
public class DataConverter
{
	private static DataConverter instance;
	
	private final Marshaller marshaller;
	
	private DataConverter() throws JAXBException
	{
		
		final JAXBContext context = JAXBContext.newInstance(RuntimeStatus.class, ServerConfigStatus.class, ServerStatus.class);
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
	}
	
	public static DataConverter getInstance() throws JAXBException
	{
		
		if (instance == null)
		{
			instance = new DataConverter();
		}
		return instance;
		
	}
	
	public String getXML(final Serializable data) throws JAXBException
	{
		
		final StringWriter sw = new StringWriter();
		marshaller.marshal(data, sw);
		return sw.toString();
		
	}
	
}
