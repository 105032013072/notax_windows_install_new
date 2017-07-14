package com.bosssoft.install.nontax.windows;

import java.io.File;

import com.bosssoft.platform.installer.core.Constants;
import com.bosssoft.platform.installer.core.launch.LaunchException;
import com.bosssoft.platform.installer.core.launch.Launcher;
import com.bosssoft.platform.installer.core.util.InstallerFileManager;



public class DemoLauncher implements Constants {


	public static void main(String[] args) {
		try {	
			Launcher launcher = new Launcher();
			launcher.run(args);
		} catch (LaunchException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}

}