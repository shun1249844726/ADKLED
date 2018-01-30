package com.lexinsmart.adkled.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.lexinsmart.adkled.action.LEDAction;
import com.lexinsmart.adkled.dao.APCValuesDao;
import com.lexinsmart.adkled.db.DBCP;
import com.lexinsmart.adkled.model.ApcValues;


public class APCValuesService {

	Logger logger = Logger.getLogger(APCValuesService.class);
	Connection connection = null;// 数据库的连接
	APCValuesDao apcValuesDao = null;

	public APCValuesService() {
		try {
			connection = DBCP.getConnection();// 事物管理，最后commit；
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	public void setAPCValues(ArrayList<ApcValues> tankList) {
		apcValuesDao = new APCValuesDao(connection);
		apcValuesDao.UpdateAPCValues(tankList);
		
		try {
			connection.commit();
			logger.info("commit success!");
		} catch (SQLException e) {
			logger.error(e);
			try {
				logger.error("rollback");
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			apcValuesDao.release();
			DBCP.releaseConnection(connection);
			System.out.println("释放连接");
		}
	}
}
