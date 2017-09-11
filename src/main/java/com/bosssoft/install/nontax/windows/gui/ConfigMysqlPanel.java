package com.bosssoft.install.nontax.windows.gui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;

import org.apache.log4j.Logger;

import com.bosssoft.platform.installer.core.MainFrameController;
import com.bosssoft.platform.installer.core.gui.AbstractControlPanel;
import com.bosssoft.platform.installer.core.gui.AbstractSetupPanel;
import com.bosssoft.platform.installer.core.util.I18nUtil;
import com.bosssoft.platform.installer.core.util.ReflectUtil;
import com.bosssoft.platform.installer.wizard.action.CheckDataSourceExistAction;
import com.bosssoft.platform.installer.wizard.cfg.ProductInstallConfigs;
import com.bosssoft.platform.installer.wizard.cfg.Server;
import com.bosssoft.platform.installer.wizard.gui.AbstractDBEditorPanel;
import com.bosssoft.platform.installer.wizard.gui.component.MultiLabel;
import com.bosssoft.platform.installer.wizard.gui.component.StepTitleLabel;
import com.bosssoft.platform.installer.wizard.gui.component.XFileChooser;
import com.bosssoft.platform.installer.wizard.util.DBConnectionUtil;

public class ConfigMysqlPanel extends AbstractSetupPanel implements ActionListener {
	Logger logger = Logger.getLogger(getClass());
	private StepTitleLabel line = new StepTitleLabel();

	private JLabel lblDB = new JLabel();
	private JComboBox cbxDb = new JComboBox();

	protected JTextField tfdUrl = new JTextField();
	protected JLabel lblIP = new JLabel();
	protected JLabel lblUrl = new JLabel();
	protected JLabel lblPort = new JLabel();
	protected JCheckBox chkInitDB = new JCheckBox();
	protected MultiLabel lblTabSpace = new MultiLabel(I18nUtil.getString("DBCONFIG.MSG.TABSPACE"), 4, 2, 3);
	protected JTextField tfdSID = new JTextField();
	protected JLabel lbPassword = new JLabel();
	protected JLabel lblUser = new JLabel();
	protected JLabel lblSID = new JLabel();

	protected JComboBox cbxDrivers = new JComboBox();

	protected JTextField tfdPort = new JTextField();

	protected JCheckBox chkUserDbDriver = new JCheckBox();
	protected JButton btnDBTest = new JButton();
	protected JLabel lblDbDriver = new JLabel();
	protected JTextField tfdIP = new JTextField();

	protected JTextField tfdInformixName = new JTextField();

	protected JPasswordField tfdPassword = new JPasswordField();
	protected JTextField tfdUser = new JTextField();
	protected XFileChooser fileChooser = new XFileChooser();
	protected XFileChooser SqlScriptChooser=new XFileChooser();
	protected TxtDocumentListener documentListener = new TxtDocumentListener();
	private   EditorActionListener actionListener = new EditorActionListener();

	public ConfigMysqlPanel() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	//加载支持的数据库列表，添加默认选中的数据库的编辑面板
	void jbInit() throws Exception {
		setOpaque(false);
		setLayout(null);
		this.line.setText(I18nUtil.getString("STEP.DBCONFIG"));
		this.line.setBounds(new Rectangle(26, 5, 581, 27));

		this.lblDB.setText(I18nUtil.getString("DBCONFIG.LABEL.DBTYPE"));
		this.lblDB.setBounds(new Rectangle(30, 38, 100, 16));

		this.cbxDb.setBounds(new Rectangle(162, 35, 240, 20));

		loadSupportedDBSvr();
		this.cbxDb.addActionListener(this);

		add(this.line, null);
		add(this.lblDB, null);
		add(this.cbxDb, null);
		
		this.lblDbDriver.setText(I18nUtil.getString("DBCONFIG.LABEL.DRIVER"));
		this.btnDBTest.setText(I18nUtil.getString("DBCONFIG.BUTTON.CONTEST"));
		this.btnDBTest.setMnemonic('T');
		this.btnDBTest.setMargin(new Insets(2, 2, 2, 2));
		this.btnDBTest.setOpaque(false);
		this.chkUserDbDriver.setText(I18nUtil.getString("DBCONFIG.LABEL.USERDRIVER"));
		this.chkUserDbDriver.setOpaque(false);
		this.chkUserDbDriver.setMargin(new Insets(0, 0, 0, 0));

		this.tfdUser.setBounds(new Rectangle(162, 148, 237, 21));
		this.tfdPassword.setBounds(new Rectangle(162, 178, 237, 21));
		this.tfdIP.setBounds(new Rectangle(162, 60, 237, 21));
		this.tfdIP.setText("127.0.0.1");
		this.lblDbDriver.setBounds(new Rectangle(48, 265, 87, 16));
		this.btnDBTest.setBounds(new Rectangle(290, 329, 120, 21));
		this.chkUserDbDriver.setBounds(new Rectangle(30, 233, 130, 25));
		this.tfdPort.setBounds(new Rectangle(133, 29, 54, 21));
		this.tfdPort.setBounds(new Rectangle(162, 89, 54, 21));
		this.cbxDrivers.setBounds(new Rectangle(162, 265, 237, 21));
		this.lblSID.setBounds(new Rectangle(30, 119, 94, 16));
		this.lblUser.setBounds(new Rectangle(30, 148, 96, 16));
		this.lbPassword.setBounds(new Rectangle(30, 180, 97, 16));
		this.tfdSID.setBounds(new Rectangle(162, 119, 237, 21));

		this.chkInitDB.setBounds(new Rectangle(30, 290, 200, 25));
		this.lblTabSpace.setBounds(new Rectangle(0, 251, 380, 80));
		this.lblTabSpace.setForeground(Color.red);
		this.lblTabSpace.setVerticalAlignment(1);
		this.lblTabSpace.setVisible(false);

		this.lblPort.setBounds(new Rectangle(30, 90, 70, 16));
		this.lblUrl.setBounds(new Rectangle(30, 208, 101, 16));
		this.lblIP.setBounds(new Rectangle(30, 61, 64, 16));
		this.tfdUrl.setBounds(new Rectangle(162, 207, 237, 21));

		this.lblSID.setText(I18nUtil.getString("DBCONFIG.LABEL.DBNAME"));
		this.lblUser.setText(I18nUtil.getString("DBCONFIG.LABEL.USER"));
		this.lbPassword.setText(I18nUtil.getString("DBCONFIG.LABEL.PASSWORD"));
		this.chkInitDB.setText(I18nUtil.getString("DBCONFIG.LABEL.INITDB"));
		this.chkInitDB.setOpaque(false);
		this.chkInitDB.setMargin(new Insets(0, 0, 0, 0));
		this.lblPort.setText(I18nUtil.getString("DBCONFIG.LABEL.PORT"));
		this.lblUrl.setText(I18nUtil.getString("DBCONFIG.LABEL.URL"));
		this.lblIP.setText(I18nUtil.getString("DBCONFIG.LABEL.IP"));
		this.cbxDrivers.setEnabled(false);

		this.fileChooser.setButtonText(I18nUtil.getString("BUTTON.BROWSE"));
		this.fileChooser.setButtonmnMnemonic('R');
		this.fileChooser.setEnabled(false);
		this.fileChooser.setBounds(new Rectangle(162, 236, 237, 21));
		
		this.SqlScriptChooser.setButtonText(I18nUtil.getString("BUTTON.BROWSE"));
		this.SqlScriptChooser.setButtonmnMnemonic('R');
		this.SqlScriptChooser.setEnabled(false);
		this.SqlScriptChooser.setBounds(new Rectangle(162, 295, 237, 21));
		add(this.lblPort, null);
		add(this.tfdSID, null);
		add(this.tfdPort, null);
		add(this.tfdUrl, null);
		add(this.chkUserDbDriver, null);
		add(this.lblDbDriver, null);
		add(this.chkInitDB, null);
		add(this.lblTabSpace, null);
		add(this.tfdPassword, null);
		add(this.btnDBTest, null);
		add(this.cbxDrivers, null);
		add(this.lbPassword, null);
		add(this.lblUser, null);
		add(this.lblUrl, null);
		add(this.tfdUser, null);
		add(this.fileChooser, null);
		add(this.lblSID, null);
		add(this.tfdIP, null);
		add(this.lblIP, null);
		add(this.SqlScriptChooser,null);

		this.fileChooser.setFileSelectionMode(0);
		this.fileChooser.setMultiSelectionEnabled(true);

		this.SqlScriptChooser.setFileSelectionMode(0);
		this.SqlScriptChooser.setMultiSelectionEnabled(true);
		
		this.tfdUser.getDocument().addDocumentListener(this.documentListener);
		this.tfdPassword.getDocument().addDocumentListener(this.documentListener);
		this.tfdIP.getDocument().addDocumentListener(this.documentListener);
		this.tfdPort.getDocument().addDocumentListener(this.documentListener);
		this.tfdSID.getDocument().addDocumentListener(this.documentListener);

		this.btnDBTest.addActionListener(this.actionListener);
		this.fileChooser.addActionListener(this.actionListener);

		this.chkUserDbDriver.addActionListener(this.actionListener);
		this.chkInitDB.addActionListener(this.actionListener);

		//refreshSubPanel();
	}

	public void afterShow() {
	}

	//将数据库配置信息保存到context中
	public void beforeNext() {
	
		/*if (this.dbEditorPanel != null) {
			Properties properties = this.dbEditorPanel.getProperties();
			getContext().putAll(properties);
			Server dbServer = (Server) this.cbxDb.getSelectedItem();

			getContext().setValue("DB_TYPE", dbServer.getName());
			getContext().setValue("DB_VERSION", dbServer.getVersion());

			getContext().setValue("DB_DS_JNDI_NAME", this.tfdDataSourceName.getText().trim());
		
			//记录日志
		   logger.info("config DB: "+properties);
		
		}*/
	}

	public void beforePrevious() {
	}

	public void beforeShow() {
		AbstractControlPanel controlPane = MainFrameController.getControlPanel();
		controlPane.setButtonVisible("finish", false);
		controlPane.setButtonVisible("help", false);
		/*if (this.dbEditorPanel != null)
			this.dbEditorPanel.resetTabSpaceText();*/
	}

	public boolean checkInput() {
		/*this.dbEditorPanel.setContext(getContext());
		getContext().putAll(this.dbEditorPanel.getProperties());
		if (this.dbEditorPanel == null) {
			return false;
		}

		if ("PE".equalsIgnoreCase(this.context.getStringValue("EDITION"))) {
			boolean exist = CheckDataSourceExistAction.checkDataSourceExist(this.context, this.tfdDataSourceName.getText().trim());
			if (exist) {
				String msg = I18nUtil.getString("CheckDataSourceExistAction.Exist.Error");
				showError(MessageFormat.format(msg, new Object[] { this.tfdDataSourceName.getText().trim() }));
				return false;
			}

		}

		String app_server_name = this.context.getStringValue("APP_SERVER_NAME");
		if (("WebLogic".indexOf(app_server_name) != -1) || ("WebSphere6.0".indexOf(app_server_name) != -1)) {
			String result = this.dbEditorPanel.testDBConnection();
			if (result != null) {
				showError(result);
				return false;
			}
		}
		return this.dbEditorPanel.checkInput();*/
		return true;
	}

	public void initialize(String[] parameters) {
	}

	private void loadSupportedDBSvr() {
		List list = ProductInstallConfigs.getSupportedDBSvrs();
		int i = 0;
		for (int j = list.size(); i < j; i++)
			this.cbxDb.addItem(list.get(i));
	}

	public void afterActions() {
	}

	public void actionPerformed(ActionEvent ae) {
		/*Object source = ae.getSource();
		if (source == this.cbxDb)
			refreshSubPanel();*/
	}

	/*private void refreshSubPanel() {
		Server dbServer = (Server) this.cbxDb.getSelectedItem();

		clearDBSvrPanel();
		String className = dbServer.getEditorPanel();
		if (className != null) {
			if (this.editorPanelMap.containsKey(dbServer.getName())) {
				this.dbEditorPanel = ((AbstractDBEditorPanel) this.editorPanelMap.get(dbServer.getName()));
			} else {
				this.dbEditorPanel = ((AbstractDBEditorPanel) ReflectUtil.newInstanceBy(className));
				this.editorPanelMap.put(dbServer.getName(), this.dbEditorPanel);
			}
			this.dbEditorPanel.setBounds(new Rectangle(30, 85, 390, 320));
			add(this.dbEditorPanel, null);
			this.dbEditorPanel.chkInitDB.setEnabled(true);
		}

		validate();
		repaint();
	}

	private void clearDBSvrPanel() {
		if (this.dbEditorPanel != null)
			remove(this.dbEditorPanel);
	}*/
	
	
	
	private void refreshDBUrl() {
		this.tfdUrl.setText(" " + getDBUrl());
	}
	
	private String getDBUrl() {
		String strURL = "jdbc:mysql://" + this.tfdIP.getText() + ":" + this.tfdPort.getText() + "/" + this.tfdSID.getText();
		return strURL;
	}

	protected class TxtDocumentListener implements DocumentListener {
		protected TxtDocumentListener() {
		}

		public void insertUpdate(DocumentEvent evt) {
			ConfigMysqlPanel.this.refreshDBUrl();
		}

		public void removeUpdate(DocumentEvent evt) {
			ConfigMysqlPanel.this.refreshDBUrl();
		}

		public void changedUpdate(DocumentEvent evt) {
			ConfigMysqlPanel.this.refreshDBUrl();
		}
	}
	
	class EditorActionListener implements ActionListener {
		EditorActionListener() {
		}

		public void actionPerformed(ActionEvent ae) {
			Object source = ae.getSource();
			if (fileChooser == source) {
				ConfigMysqlPanel.this.loadJDBCDriverFromFile();
			} else if (chkInitDB == source) {
				ConfigMysqlPanel.this.resetTabSpaceText();
				ConfigMysqlPanel.this.refreshLoadSqlScript();
			} else if (chkUserDbDriver == source) {
				ConfigMysqlPanel.this.refreshLoadUserDriver();
			} else if (btnDBTest == source) {
				/*String message = AbstractDBEditorPanel.this.testDBConnection();
				if (message != null)
					MainFrameController.showMessageDialog(message, I18nUtil.getString("DIALOG.TITLE.ERROR"), 0);
				else
					MainFrameController.showMessageDialog(I18nUtil.getString("DBCONFIG.MESSAGE.CONNECTIONOK"), I18nUtil.getString("DIALOG.TITLE.INFO"), 1);*/
			}
		}
	}

	public void loadJDBCDriverFromFile() {
		File[] files = this.fileChooser.getSelectedFiles();
		if ((files == null) || (files.length == 0))
			return;
		loadJDBCDriverFromFile(files);
		
	}

	public void refreshLoadUserDriver() {
		this.fileChooser.setEnabled(this.chkUserDbDriver.isSelected());
		this.cbxDrivers.setEnabled(this.chkUserDbDriver.isSelected());
	}

	public void refreshLoadSqlScript() {
		this.SqlScriptChooser.setEnabled(this.chkInitDB.isSelected());
	}

	public void resetTabSpaceText() {
		String db_type = getClass().getSimpleName().toLowerCase();

		this.lblTabSpace.setVisible(false);
		
	}

	private void loadJDBCDriverFromFile(File[] files) {
		this.cbxDrivers.removeAllItems();
		List clazzList = DBConnectionUtil.getAllDriverClassName(files);
		if (clazzList.size() == 0) {
			MainFrameController.showMessageDialog(I18nUtil.getString("DBINIT.MSG.NODBDRIVER"), I18nUtil.getString("DIALOG.TITLE.ERROR"), 0);
			return;
		}
		for (int i = 0; i < clazzList.size(); i++)
			this.cbxDrivers.addItem(clazzList.get(i));
		
	}
	
}
