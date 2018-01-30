package com.lexinsmart.adkled.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.lexinsmart.adkled.db.DBCP;
import com.lexinsmart.adkled.model.ApcValues;


public class APCValuesDao {
	Connection connection = null;
	PreparedStatement ptmt = null;
	DBCP dbcp = DBCP.getInstance();
	ResultSet rest  = null;
	public APCValuesDao(Connection connection) {
		this.connection = connection;
	}
	public void release() {
		DBCP.releaseConnection(connection);
		DBCP.closeStatement(ptmt, rest);
		System.out.println("释放APCvaluesDao连接");
	}
	public void UpdateAPCValues(ArrayList<ApcValues> tankList) {
		try {
			int row = 0;
			String sql = "REPLACE INTO apcvalues(tagname,tagvalue,timestamp) VALUES(?,?,?) ";
			ptmt = connection.prepareStatement(sql);
			for (int i = 0; i < tankList.size(); i++) {
				ptmt.setString(1, tankList.get(i).getTagName());
				ptmt.setString(2, tankList.get(i).getTagValue());
				ptmt.setString(3, tankList.get(i).getTimestamp());
				ptmt.addBatch();
			}
			// 执行批处理操作并返回计数组成的数组  
            int[] rows = ptmt.executeBatch();  
            // 对行数赋值  
            row = rows.length;  
            System.out.println("addrow:   "+row);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
