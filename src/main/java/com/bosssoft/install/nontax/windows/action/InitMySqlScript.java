package com.bosssoft.install.nontax.windows.action;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.texen.util.FileUtil;

import com.bosssoft.platform.installer.core.IContext;
import com.bosssoft.platform.installer.core.InstallException;
import com.bosssoft.platform.installer.core.action.IAction;
import com.bosssoft.platform.installer.core.initdb.SqlScriptRunner;
import com.bosssoft.platform.installer.core.util.InstallerFileManager;
import com.bosssoft.platform.installer.wizard.action.InitDBSqlScript;
import com.bosssoft.platform.installer.wizard.util.DBConnectionUtil;

public class InitMySqlScript implements IAction{
	transient Logger logger = Logger.getLogger(getClass());
	
	public void execute(IContext context, Map params) throws InstallException {
		File parentDir=new File(InstallerFileManager.getInstallerHome()+File.separator+"db"+File.separator+"script");
		String[] files=parentDir.list();
		StringBuffer strbuffer=new StringBuffer("");
		for (String f : files) {
			strbuffer.append(f).append(",");
		}
		context.setValue("DB_INIT_SQLSCRIPT", strbuffer.toString());
		InitDBSqlScript initDBSqlScript=new InitDBSqlScript();
		initDBSqlScript.execute(context, params);
	}

	public void rollback(IContext context, Map params) throws InstallException {
		
		
	}



}
