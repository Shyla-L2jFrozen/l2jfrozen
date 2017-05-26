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
package com.l2jfrozen.gameserver.util.deamon.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Shyla
 */

@XmlRootElement
public class RuntimeStatus implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8586930329090845553L;
	
	// Env
	@XmlElement
	private String systemTime;
	@XmlElement
	private int avaibleCPUs;
	@XmlElement
	private String processorIdentifier;
	@XmlElement
	private String os;
	@XmlElement
	private String osBuild;
	@XmlElement
	private String osArch;
	
	// Java Platform Information;
	@XmlElement
	private String javaRuntimeName;
	@XmlElement
	private String javaVersion;
	@XmlElement
	private String javaClassVersion;
	
	// Runtime Information";
	@XmlElement
	private long currentFreeHeapSize;
	@XmlElement
	private long currentHeapSize;
	@XmlElement
	private long maximumHeapSize;
	
	// Virtual Machine Information (JVM)
	@XmlElement
	private String jvmName;
	@XmlElement
	private String jvmInstallationDirectory;
	@XmlElement
	private String jvmVersion;
	@XmlElement
	private String jvmVendor;
	@XmlElement
	private String jvmExtraInfo;
	
	/**
	 * 
	 * 
	 */
	public RuntimeStatus()
	{
		super();
		refreshStatus();
		
	}
	
	/**
	 * @return the systemTime
	 */
	public String getSystemTime()
	{
		return systemTime;
	}
	
	/**
	 * @return the avaibleCPUs
	 */
	public int getAvaibleCPUs()
	{
		return avaibleCPUs;
	}
	
	/**
	 * @return the processorIdentifier
	 */
	public String getProcessorIdentifier()
	{
		return processorIdentifier;
	}
	
	/**
	 * @return the os
	 */
	public String getOs()
	{
		return os;
	}
	
	/**
	 * @return the osArch
	 */
	public String getOsArch()
	{
		return osArch;
	}
	
	/**
	 * @return the javaRuntimeName
	 */
	public String getJavaRuntimeName()
	{
		return javaRuntimeName;
	}
	
	/**
	 * @return the javaVersion
	 */
	public String getJavaVersion()
	{
		return javaVersion;
	}
	
	/**
	 * @return the javaClassVersion
	 */
	public String getJavaClassVersion()
	{
		return javaClassVersion;
	}
	
	/**
	 * @return the currentFreeHeapSize
	 */
	public long getCurrentFreeHeapSize()
	{
		return currentFreeHeapSize;
	}
	
	/**
	 * @return the currentHeapSize
	 */
	public long getCurrentHeapSize()
	{
		return currentHeapSize;
	}
	
	/**
	 * @return the maximumHeapSize
	 */
	public long getMaximumHeapSize()
	{
		return maximumHeapSize;
	}
	
	/**
	 * @return the jvmName
	 */
	public String getJvmName()
	{
		return jvmName;
	}
	
	/**
	 * @return the jvmInstallationDirectory
	 */
	public String getJvmInstallationDirectory()
	{
		return jvmInstallationDirectory;
	}
	
	/**
	 * @return the jvmVersion
	 */
	public String getJvmVersion()
	{
		return jvmVersion;
	}
	
	/**
	 * @return the jvmVendor
	 */
	public String getJvmVendor()
	{
		return jvmVendor;
	}
	
	/**
	 * @return the jvmInfo
	 */
	public String getJvmExtraInfo()
	{
		return jvmExtraInfo;
	}
	
	/**
	 * @return the osBuild
	 */
	public String getOsBuild()
	{
		return osBuild;
	}

	public void refreshStatus()
	{
		avaibleCPUs = Runtime.getRuntime().availableProcessors();
		processorIdentifier = System.getenv("PROCESSOR_IDENTIFIER");
		
		os = System.getProperty("os.name");
		osBuild = System.getProperty("os.version");
		osArch = System.getProperty("os.arch");
		
		// Java Platform Information;
		javaRuntimeName = System.getProperty("java.runtime.name");
		javaVersion = System.getProperty("java.version");
		javaClassVersion = System.getProperty("java.class.version");
		
		// Runtime Information";
		currentFreeHeapSize = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		currentHeapSize = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		maximumHeapSize = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		
		// instanciates Date Objec
		final Date dateInfo = new Date();
		
		// generates a simple date format
		final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
		
		// generates String that will get the formater info with values
		systemTime = df.format(dateInfo);
		
		// Virtual Machine Information (JVM)
		jvmName = System.getProperty("java.vm.name");
		jvmInstallationDirectory = System.getProperty("java.home");
		jvmVersion = System.getProperty("java.vm.version");
		jvmVendor = System.getProperty("java.vm.vendor");
		jvmExtraInfo = System.getProperty("java.vm.info");
		
	}
}
