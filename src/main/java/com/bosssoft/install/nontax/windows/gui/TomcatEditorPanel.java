package com.bosssoft.install.nontax.windows.gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bosssoft.platform.installer.core.IContext;
import com.bosssoft.platform.installer.core.MainFrameController;
import com.bosssoft.platform.installer.core.util.I18nUtil;
import com.bosssoft.platform.installer.wizard.gui.AbstractASEditorPanel;
import com.bosssoft.platform.installer.wizard.gui.component.XFileChooser;
import com.bosssoft.platform.installer.wizard.gui.validate.ValidatorHelper;
import com.bosssoft.platform.installer.wizard.util.XmlUtil;

public class TomcatEditorPanel extends AbstractASEditorPanel implements ActionListener,FocusListener{
   
	Logger logger = Logger.getLogger(getClass());

	private JLabel lblTCHome = new JLabel();
	
	private JLabel lblTCPort=new JLabel();
	
	private JTextField txPort = new JTextField();

	protected XFileChooser tcHomeChooser = new XFileChooser();
	
	private JCheckBox chkInstall = new JCheckBox();

	public TomcatEditorPanel() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		setLayout(null);
		setOpaque(false);
		this.lblTCHome.setText("Tomcat Home:");
		//this.lblTCHome.setBounds(new Rectangle(0, 5, 90, 16));
		this.lblTCHome.setBounds(new Rectangle(0, 32, 90, 16));
		
		this.tcHomeChooser.setBounds(new Rectangle(117, 30, 253, 21));
		this.tcHomeChooser.setButtonText(I18nUtil.getString("BUTTON.BROWSE2"));
		
		
		this.lblTCPort.setText("Tomcat Port:");
		this.lblTCPort.setBounds(new Rectangle(0, 59, 90, 16));
		this.txPort.setBounds(new Rectangle(117, 56, 215, 22));
		
		this.chkInstall.setText("安装应用服务器");
		this.chkInstall.setBounds(new Rectangle(0, 5, 200, 16));
		this.chkInstall.setOpaque(false);
		this.chkInstall.setSelected(false);
		
		
		tcHomeChooser.addActionListener(this);
		tcHomeChooser.getTextField().addFocusListener(this);
		chkInstall.addActionListener(this);
		
		add(this.lblTCHome, null);
		add(this.tcHomeChooser, null);
		add(this.txPort,null);
		add(this.lblTCPort,null);
		add(this.chkInstall,null);
	}

	public Properties getProperties() {
		Properties p = new Properties();
		p.put("AS_TOMCAT_HOME", this.tcHomeChooser.getText());
		p.put("AS_TOMCAT_VERSION", getParameter());
		p.put("APP_SERVER_PORT", this.txPort.getText());
		p.put("APP_SERVER_DEPLOY_DIR", this.tcHomeChooser.getText()+File.separator+"webapps");
		p.put("IS_APP_SERVER_INSTALL", this.chkInstall.isSelected());
		return p;
	}

	public boolean checkInput() {
		IContext context = getContext();
		if(this.chkInstall.isSelected()) return true;

		if (this.tcHomeChooser.getText().trim().length() == 0) {
			MainFrameController.showMessageDialog(I18nUtil.getString("APPSVR.TC.MSG.TCHOMENULL"), I18nUtil.getString("DIALOG.TITLE.ERROR"), 0);
			return false;
		}
		if (!isTomcatHome(this.tcHomeChooser.getText().trim())) {
			MainFrameController.showMessageDialog(I18nUtil.getString("APPSVR.TC.MSG.TCHOMEERROR"), I18nUtil.getString("DIALOG.TITLE.ERROR"), 0);
			return false;
		}

		context.setValue("AS_TOMCAT_HOME", this.tcHomeChooser.getText().trim());

		return true;
	}

	public static boolean isTomcatHome(String home) {
		return ValidatorHelper.isContainsFileOrDir(home, new String[] { "webapps", "bin", "conf", "work" });
	}

	public String getChooserAppSerHome() {
		return this.tcHomeChooser.getText().trim();
	}

	public void actionPerformed(ActionEvent ac) {
		   if(ac.getSource()==this.tcHomeChooser){
			   this.txPort.setText("");
				
				XFileChooser xc=(XFileChooser) ac.getSource();
				String TomcatPath=xc.getText();
				String port=getTomcatPort(TomcatPath);
				this.txPort.setText(port);
				this.txPort.setEditable(false);
		   }else if(ac.getSource()==this.chkInstall){
			   if(chkInstall.isSelected()){
				   this.txPort.setText("8080");
				   this.txPort.setEnabled(false);
				   this.tcHomeChooser.setText("");
				   this.tcHomeChooser.setEnabled(false);
			   } else{
				   this.txPort.setText("");
				   this.txPort.setEnabled(true);
				   this.tcHomeChooser.setEnabled(true);
			   }
		   }
		
		    
	}

	public void focusLost(FocusEvent arg0) {
		this.txPort.setText("");
			JTextField jf=(JTextField) arg0.getSource();
			String TomcatPath=jf.getText();
			String port=getTomcatPort(TomcatPath);
			this.txPort.setText(port);
			this.txPort.setEditable(false);
		
	}
	public void focusGained(FocusEvent arg0) {
		
		
	}

	
    
	
	private String getTomcatPort(String tomcatPath) {
		String port=null;
		String serverConf=tomcatPath + File.separator + "conf" + File.separator + "server.xml";
		if(!new File(serverConf).exists()) MainFrameController.showMessageDialog("tomcat "+I18nUtil.getString("APPSVR.CONF.ERROR"), I18nUtil.getString("DIALOG.TITLE.ERROR"), 0);
		else{
			try{
				  Document doc = XmlUtil.getDocument(new File(serverConf));
					Elements elements = XmlUtil.findElements(doc, "Server").select("Service").select("Connector");
					Iterator<Element> it=elements.iterator();
					while(it.hasNext()){
						Element element=it.next();
						String protocol=element.attr("protocol");
						if ((protocol == null) || (protocol.indexOf("AJP") == -1)) {
							port=element.attr("port");
						}
					}

			  }catch(Exception e){
				  e.printStackTrace();
			  }
		}
		return port;
	}

}
