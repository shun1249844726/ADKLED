package com.lexinsmart.adkled.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DBCP {
	
	 //   private static ThreadLocal<Connection> tl=new ThreadLocal<Connection>();  

		protected static final Log log = LogFactory.getLog(DBCP.class.getName());
		private static DBCP _instance = null;
		private static DataSource dataSource;

		public static DBCP getInstance() {
			if (_instance == null) {
				_instance = new DBCP();
			}
			return _instance;
		}
	//
//		// 在静态代码块中进行配置文件与程序的关联
//		static {
//			Properties prop = new Properties();
//			String commConfigFilePath = "./src/dbcp.properties";
//			// String commConfigFilePath = DBCP.class.getResource("/").getFile()
//			// +"dbcp.properties";
//			InputStream in = null;
//			try {
//				in = new BufferedInputStream(new FileInputStream(commConfigFilePath));
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			try {
//				long time1 = System.currentTimeMillis();
//				prop.load(in);
//				ds = BasicDataSourceFactory.createDataSource(prop);
//				System.out.println("t1:  "+ (System.currentTimeMillis() -time1));
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

		public static DataSource getDataSource() {
			if (DBCP.dataSource == null) {
				try {
					PropertiesConfig config = PropertiesService.getApplicationConfig();
					BasicDataSource dbcpDataSource = new BasicDataSource();
					dbcpDataSource.setUrl(config.getProperty("url"));
					dbcpDataSource.setDriverClassName(config.getProperty("driver"));
					dbcpDataSource.setUsername(config.getProperty("username"));
					dbcpDataSource.setPassword(config.getProperty("password"));
					dbcpDataSource.setDefaultAutoCommit(Boolean.parseBoolean(config.getProperty("defaultAutoCommit")));
					dbcpDataSource.setMaxActive(Integer.parseInt(config.getProperty("maxActive")));
					dbcpDataSource.setMaxIdle(Integer.parseInt(config.getProperty("maxIdle")));
					dbcpDataSource.setMaxWait(Integer.parseInt(config.getProperty("maxActive")));
					DBCP.dataSource = (DataSource) dbcpDataSource;
//					System.out.println("dbcp数据源初始化成功!");

				} catch (Exception ex) {
//					System.out.println("dbcp数据源初始化失败:" + ex.getMessage());
				}
			}
			return DBCP.dataSource;
		}

		public static Connection getConnection() {
			try {
				DataSource dataSource = DBCP.getDataSource();
				return dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * get a connection from the pool
		 * 
		 * @return
		 */

//		public static Connection getConnection_2() {
//			Connection conn = null;
//			if (ds != null) {
//				try {
//					conn = ds.getConnection();
//				} catch (Exception e) {
//					log.info("getConnection fail:", e);
//					e.printStackTrace();
//				}
////				try {
////					conn.setAutoCommit(false);
////				} catch (SQLException e) {
////					e.printStackTrace();
////				}
//				return conn;
//			}
//			return conn;
//		}

		/**
		 * release the connection
		 * 
		 * @param con
		 */
		public static void releaseConnection(Connection con) {
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (SQLException ex) {
				log.info("releaseConnection fail:", ex);
				ex.printStackTrace();
			}finally {
			}
		}

		/**
		 * close statement and resultset
		 * 
		 * @param stm
		 * @param result
		 */
		public static void closeStatement(Statement stm, ResultSet result) {
			try {
				if (result != null) {
					result.close();
				}
			} catch (SQLException e) {
				log.error("closeStatement error:", e);
				e.printStackTrace();
			}

			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				log.error("closeStatement error:", e);
				e.printStackTrace();
			}
		}

		/**
		 * close statement
		 * 
		 * @param st
		 */
		public static void closeStatement(Statement st) {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					log.error("closeStatement error:", e);
					e.printStackTrace();
				}
			}
		}

		/**
		 * generic query method
		 * 
		 * @param sql
		 * @return
		 */
		public ResultSet executeQuery(String sql) {
			ResultSet rset = null;
			Statement _stmt = null;
			Connection con = getConnection();
			try {
				_stmt = con.createStatement();
				rset = _stmt.executeQuery(sql);
			} catch (Exception e) {
				log.error("executeQuery error:", e);
				e.printStackTrace();
			} finally {
				closeStatement(_stmt);
				releaseConnection(con);
			}
			return rset;
		}

		/**
		 * generic update method
		 * 
		 * @param sql
		 */
		public void updateDB(String sql) {
			Connection con = getConnection();
			Statement _stmt = null;
			try {
				_stmt = con.createStatement();
				_stmt.executeUpdate(sql);
			} catch (Exception e) {
				log.error("updateDB error:", e);
				e.printStackTrace();
			} finally {
				closeStatement(_stmt);
				releaseConnection(con);
			}
		}

//		public static void main(String[] args) {
//			for (int i = 0; i < 30; i++) {
//				long s = System.currentTimeMillis();
//				DBCP dbcp = DBCP.getInstance();
//				Connection con = getConnection();
//				System.out.println(con.toString());
//				dbcp.releaseConnection(con);
//				System.out.println(System.currentTimeMillis() - s);
//			}
//		}
	}