package com.lexinsmart.adkled.db;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
* ��ȡ*.properties�����ļ�
*/
public class PropertiesConfig {

	// �����ļ���map key:propertiesName value:PropertiesUtil����
	private static HashMap<String, PropertiesConfig> propertiesMap = new HashMap<String, PropertiesConfig>();

	// �����ļ�
	private Properties properties;

	private PropertiesConfig() {

	}

	public synchronized static PropertiesConfig getInstance(String propertiesName) {

		PropertiesConfig configUtil = propertiesMap.get(propertiesName);

		if (configUtil == null) {
			configUtil = new PropertiesConfig();
			configUtil.analysisXml(propertiesName);
			propertiesMap.put(propertiesName, configUtil);
		}

		return configUtil;
	}

	private void analysisXml(String propertiesName) {
//		InputStream ins = null;
//		try {
//			ins = new BufferedInputStream(new FileInputStream(propertiesName));//����ʹ��"./conf/dbcp.properties"
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
		
		InputStream ins = getClass().getResourceAsStream(propertiesName);
		properties = new Properties();
		try {
			properties.load(ins);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

}
