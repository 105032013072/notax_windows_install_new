package com.bosssoft.install.nontax.windows.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.texen.util.FileUtil;

import com.bosssoft.platform.installer.core.IContext;
import com.bosssoft.platform.installer.core.InstallException;
import com.bosssoft.platform.installer.core.action.IAction;
import com.bosssoft.platform.installer.core.util.InstallerFileManager;
import com.bosssoft.platform.installer.wizard.util.DBConnectionUtil;

public class InstallMysql implements IAction{
	transient Logger logger = Logger.getLogger(getClass());
	
	public void execute(IContext context, Map params) throws InstallException {
		String mysqlHome=(String) context.getValue("MYSQL_HOME");
		String dir=mysqlHome.substring(0, mysqlHome.indexOf(":"));
		String dbname=(String) context.getValue("DB_NAME");
		String batFile=InstallerFileManager.getInstallerHome()+File.separator+"mysql_install.bat";
		try {
			runbat(batFile,dir,mysqlHome,dbname);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InstallException(e);
		}
		
	}

	

	private void runbat(String batFile,String... argStrings) throws Exception {

		String cmd = "cmd /c start " + batFile + " ";
	    if (argStrings != null && argStrings.length > 0) {
	        for (String string : argStrings) {
	            cmd += string + " ";
	        }
	    }
		System.out.println(cmd);
	    Process ps = Runtime.getRuntime().exec(cmd);
	     //取得命令结果的输出流
	    InputStream fis=ps.getInputStream();
	    //用一个读输出流类去读
	    InputStreamReader isr=new InputStreamReader(fis);
	    //用缓冲器读行
	    BufferedReader br=new BufferedReader(isr);
	    String line=null;
	    //直到读完为止
	    while((line=br.readLine())!=null) {
	        System.out.println(line);
	    }
	    
	    int i = ps.exitValue();  //接收执行完毕的返回值
	    if (i == 0) {
	        System.out.println("执行完成.");
	    } else {
	        System.out.println("执行失败.");
	    }
	    ps.destroy();

		
	}

	public void rollback(IContext context, Map params) throws InstallException {
		
		
	}

}
