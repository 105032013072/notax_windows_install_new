package com.bosssoft.install.nontax.windows.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.bosssoft.platform.installer.core.gui.AbstractSetupPanel;
import com.bosssoft.platform.installer.core.util.I18nUtil;
import com.bosssoft.platform.installer.wizard.gui.component.StepTitleLabel;
import com.bosssoft.platform.installer.wizard.gui.component.XFileChooser;
import com.bosssoft.platform.installer.wizard.gui.validate.ValidatorHelper;

public class ConfigProduct extends AbstractSetupPanel implements ActionListener{
	Logger logger = Logger.getLogger(getClass());
	private StepTitleLabel line = new StepTitleLabel();
	private JPanel setupPane = new JPanel();
	
	private BorderLayout borderLayout1 = new BorderLayout();
	
	private JTextArea introduction = new JTextArea();

	private XFileChooser xcdataStorage = new XFileChooser();

	private JLabel lbdataStorage=new JLabel();
	
	public ConfigProduct () {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void jbInit() {
		
		setLayout(this.borderLayout1);
		setOpaque(false);

		this.setupPane.setLayout(null);
		
		setLayout(this.borderLayout1);
		setOpaque(false);

		this.setupPane.setLayout(null);
		this.line.setText(I18nUtil.getString("STEP.PRODUCTCONFIG"));
		this.line.setBounds(new Rectangle(26, 5, 581, 27));
		
		this.introduction.setOpaque(false);
		this.introduction.setEditable(false);
		this.introduction.setLineWrap(true);
		this.introduction.setWrapStyleWord(true);
		this.introduction.setBounds(new Rectangle(37, 43, 375, 97));
		this.introduction.setText(I18nUtil.getString("CONFIG.PRODUCT.INTRODUCTION"));
		 
		lbdataStorage.setText(I18nUtil.getString("CONFIG.PRODUCT.CONFIG1"));
		lbdataStorage.setBounds(new Rectangle(30, 95, 170, 20));
		
		this.xcdataStorage.setButtonText(I18nUtil.getString("BUTTON.BROWSE"));
		this.xcdataStorage.setButtonmnMnemonic('R');
		this.xcdataStorage.setBounds(new Rectangle(162, 95, 237, 21));
		
		add(this.setupPane, "Center");
		this.setupPane.add(line,null);
		this.setupPane.setOpaque(false);
		this.setupPane.add(this.line, null);
		this.setupPane.add(lbdataStorage, null);
		this.setupPane.add(xcdataStorage, null);
		this.setupPane.add(introduction,null);
		
		
	}

	public void actionPerformed(ActionEvent arg0) {
		
		
	}

	@Override
	public void initialize(String[] paramArrayOfString) {
		
		
	}

	@Override
	public void beforeShow() {
		
		
	}

	@Override
	public boolean checkInput() {
		
		String dir = this.xcdataStorage.getFilePath();
		if (ValidatorHelper.isBlankOrNull(dir)) {
			showError(I18nUtil.getString("CONFIG.PRODUCT.CONFIG1.ERROR.EMPTY"));
			return false;
		}

		String pattern = null;
		if (System.getProperty("os.name").toLowerCase().indexOf("window") != -1)
			pattern = "[a-zA-Z]:[/\\\\][\\.\\w\\-_/\\\\]+";
		else {
			pattern = "[\\.\\w\\-_/\\\\]+";
		}

		if (!ValidatorHelper.isPatternValid(dir, pattern)) {
			showError(I18nUtil.getString("CONFIG.PRODUCT.CONFIG1.ERROR.ILLEGAL"));
			return false;
		}
		return true;
	}

	@Override
	public void beforePrevious() {
		
		
	}

	@Override
	public void beforeNext() {
		String storageDir= xcdataStorage.getFilePath();
		Properties p=new Properties();
		p.setProperty("nontax.data.root.dir", storageDir);
		getContext().setValue("PRODUCT_PROPERTY", p);
		
	    logger.info("set PRODUCT_PROPERTY: "+p);
		
	}

	@Override
	public void afterActions() {
		
		
	}

}
