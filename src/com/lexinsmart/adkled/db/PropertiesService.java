package com.lexinsmart.adkled.db;

import java.io.IOException;

/**
* properties������
*/
public class PropertiesService {

	/**
	 * application�����ļ�
	 * 
	 * @return
	 * @throws IOException 
	 */
	public static PropertiesConfig getApplicationConfig() throws IOException {
//		String commConfigFilePath = PropertiesService.class.getResource("/").getFile();
//		System.out.println(commConfigFilePath);

		String path = "/dbcp.properties";
		return PropertiesConfig.getInstance(path);
	}
}
