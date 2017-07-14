package com.bosssoft.platform.nontax_windows_install;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Enumeration;

import com.bosssoft.platform.installer.core.IContext;
import com.bosssoft.platform.installer.core.initdb.DatabaseInitializerImpl;
import com.bosssoft.platform.installer.core.initdb.ScriptDefineLoader;
import com.bosssoft.platform.installer.wizard.action.InitDBSqlScript;
import com.bosssoft.platform.installer.wizard.util.DBConnectionUtil;

public class InitDBTest {

	public static void main(String[] args) {
         Connection con=getConnection();
         ScriptDefineLoader defineLoader=new ScriptDefineLoader();
         String[] componentNames=new String[1];
         componentNames[0]="cas";
         DatabaseInitializerImpl impl=new DatabaseInitializerImpl();
         impl.initialize(con, componentNames);
	   
	}

	
	public static Connection getConnection() {
		Connection conn = null;
		String user = "root";
		String password = "123";
		String url = "jdbc:mysql://127.0.0.1:3306/test";
		String driver = "com.mysql.jdbc.Driver";
		String userJdbcJars ="";
		try {
			conn = DBConnectionUtil.getConnection(userJdbcJars, driver, url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}
}
