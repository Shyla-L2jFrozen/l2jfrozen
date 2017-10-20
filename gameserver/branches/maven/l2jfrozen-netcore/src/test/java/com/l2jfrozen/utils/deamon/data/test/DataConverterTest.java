package com.l2jfrozen.utils.deamon.data.test;

import javax.xml.bind.JAXBException;

import com.l2jfrozen.netcore.util.deamon.data.DataConverter;
import com.l2jfrozen.netcore.util.deamon.data.ServerConfigStatus;

public class DataConverterTest {

	public static void main(String[] args) throws JAXBException {
		
		ServerConfigStatus scs = new ServerConfigStatus();
		String xml = DataConverter.getInstance().getXML(scs);
		System.out.println(xml);
		ServerConfigStatus output = (ServerConfigStatus) DataConverter.getInstance().getObject(xml);
		
		System.out.println(output.getGameServerIp());
	}

}
