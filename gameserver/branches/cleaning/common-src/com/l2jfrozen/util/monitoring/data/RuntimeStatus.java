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
package com.l2jfrozen.util.monitoring.data;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Shyla
 */
public class RuntimeStatus extends MonitoredStatus
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8586930329090845553L;
	
	// Env
	private String systemTime;
	private int avaibleCPUs;
	private String processorIdentifier;
	private String os;
	private String osBuild;
	private String osArch;
	
	// Java Platform Information;
	private String javaRuntimeName;
	private String javaVersion;
	private String javaClassVersion;
	
	// Runtime Information";
	private long currentFreeHeapSize;
	private long currentHeapSize;
	private long maximumHeapSize;
	
	// Virtual Machine Information (JVM)
	private String jvmName;
	private String jvmInstallationDirectory;
	private String jvmVersion;
	private String jvmVendor;
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
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "RuntimeStatus [systemTime=" + systemTime + ", avaibleCPUs=" + avaibleCPUs + ", processorIdentifier=" + processorIdentifier + ", os=" + os + ", osBuild=" + osBuild + ", osArch=" + osArch + ", javaRuntimeName=" + javaRuntimeName + ", javaVersion=" + javaVersion + ", javaClassVersion=" + javaClassVersion + ", currentFreeHeapSize=" + currentFreeHeapSize + ", currentHeapSize=" + currentHeapSize + ", maximumHeapSize=" + maximumHeapSize + ", jvmName=" + jvmName + ", jvmInstallationDirectory=" + jvmInstallationDirectory + ", jvmVersion=" + jvmVersion + ", jvmVendor=" + jvmVendor + ", jvmExtraInfo=" + jvmExtraInfo + "]";
	}
	
	/**
	 * returns how many processors are installed on this system.
	 * @return
	 */
	public String getCpuInfo()
	{
		final StringWriter sw = new StringWriter();
		sw.append("Avaible CPU(s): " + avaibleCPUs + " \n");
		sw.append("Processor(s) Identifier: " + processorIdentifier + " \n");
		
		return sw.toString();
	}
	
	/**
	 * returns the operational system server is running on it.
	 * @return
	 */
	public String getOSInfo()
	{
		final StringWriter sw = new StringWriter();
		sw.append("OS: " + os + " \n");
		sw.append("OS Build: " + osBuild + " \n");
		sw.append("OS Arch: " + osArch + " \n");
		
		return sw.toString();
	}
	
	/**
	 * returns JAVA Runtime Enviroment properties
	 * @return
	 */
	public String getJreInfo()
	{
		final StringWriter sw = new StringWriter();
		sw.append("Java Platform Information" + " \n");
		sw.append("Java Runtime  Name: " + javaRuntimeName + " \n");
		sw.append("Java Version: " + javaVersion + " \n");
		sw.append("Java Class Version: " + javaClassVersion + " \n");
		
		return sw.toString();
	}
	
	/**
	 * returns general infos related to machine
	 * @return
	 */
	public String getRuntimeInfo()
	{
		final StringWriter sw = new StringWriter();
		sw.append("Runtime Information" + " \n");
		sw.append("Current Free Heap Size: " + currentFreeHeapSize + " mb" + " \n");
		sw.append("Current Heap Size: " + currentHeapSize + " mb" + " \n");
		sw.append("Maximum Heap Size: " + maximumHeapSize + " mb" + " \n");
		
		return sw.toString();
	}
	
	/**
	 * calls time service to get system time.
	 * @return
	 */
	public String getSystemTimeInfo()
	{
		final StringWriter sw = new StringWriter();
		sw.append("INFO  System Time: " + systemTime);
		return sw.toString();
	}
	
	/**
	 * gets system JVM properties.
	 * @return
	 */
	public String getJvmInfo()
	{
		final StringWriter sw = new StringWriter();
		sw.append("Virtual Machine Information (JVM)" + " \n");
		sw.append("JVM Name: " + jvmName + " \n");
		sw.append("JVM installation directory: " + jvmInstallationDirectory + " \n");
		sw.append("JVM version: " + jvmVersion + " \n");
		sw.append("JVM Vendor: " + jvmVendor + " \n");
		sw.append("JVM Extra Info: " + jvmExtraInfo + " \n");
		
		return sw.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.util.monitoring.data.MonitoredStatus#getStatus()
	 */
	@Override
	public String getDynamicStatus()
	{
		final StringWriter sw = new StringWriter();
		sw.append("=============================================================-[ Server status ] \n");
		sw.append(getSystemTimeInfo());
		// sw.append(getOSInfo());
		// sw.append(getCpuInfo());
		// sw.append(getRuntimeInfo());
		// sw.append(getJreInfo());
		// sw.append(getJvmInfo());
		
		return sw.toString();
	}
	
	@Override
	public String getStaticStatus()
	{
		final StringWriter sw = new StringWriter();
		
		sw.append(getOSInfo());
		sw.append(getCpuInfo());
		sw.append(getJreInfo());
		sw.append(getJvmInfo());
		
		return sw.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.util.monitoring.data.MonitoredStatus#refreshStatus()
	 */
	@Override
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
