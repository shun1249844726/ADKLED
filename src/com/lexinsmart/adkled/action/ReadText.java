package com.lexinsmart.adkled.action;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.lexinsmart.adkled.model.ApcValues;
import com.lexinsmart.adkled.utils.ReadProsUtil;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbSession;

public class ReadText {
	public static Logger logger = Logger.getLogger(ReadText.class);
	private static final String DOMAIN_IP = ReadProsUtil.pros.getProperty("domainIP");
	private static final String DOMAIN_NAME = ReadProsUtil.pros.getProperty("domainName");
	private static final String USERNAME = ReadProsUtil.pros.getProperty("username");
	private static final String PASSWORD = ReadProsUtil.pros.getProperty("password");
	private static final String FILE_URL = ReadProsUtil.pros.getProperty("fileURL");
	private static final String MATERIAL = ReadProsUtil.pros.getProperty("material");
	private static final String REGEX = "TAGNAME=(Root.+?)ITEM=VALUE,VALUE=(.+?),TIMESTAMP=(.+?),QUA";
	
	//获取罐对象数据集合
	public static ArrayList<ApcValues> getTankList(String filestr){
		ArrayList<ApcValues> list = new ArrayList<ApcValues>();
		Matcher matcher = regexPattern().matcher(filestr);
		while(matcher.find()){
			ApcValues tank = new ApcValues();
			tank.setTagName(matcher.group(1));
			tank.setTagValue(matcher.group(2));
			tank.setTimestamp(matcher.group(3));
			list.add(tank);
		}
		return list;
	}
	//解析正则，返回模板
	public static Pattern regexPattern(){
		Pattern pattern = Pattern.compile(REGEX);
		return pattern;
	}
	//指定文件路径的文本转成字符串，路径可在db.properties配置文件中指定
	public static String fileToString() {
		SmbFile file = null;
		try {
			System.setProperty("jcifs.smb.client.dfs.disabled", "true");
			UniAddress dc = UniAddress.getByName(DOMAIN_IP);
			NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(DOMAIN_NAME, USERNAME, PASSWORD);
			SmbSession.logon(dc, authentication);
			file = new SmbFile(FILE_URL,authentication);
			logger.info("authentication success!");
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error(e1);
			e1.printStackTrace();
		}
		StringBuilder str = new StringBuilder();
		SmbFileInputStream fr = null;
		BufferedReader bfr = null;
		try {
			if(file==null||!file.exists())return str.toString();
			fr = new SmbFileInputStream(file);
			bfr = new BufferedReader(new InputStreamReader(fr,"UTF-8"));
			String temp = null;
			while((temp=bfr.readLine())!=null){
				str.append(temp);
			}
			logger.info("get file content success!");

		} catch (Exception e) {
			logger.error(e);
			System.out.println("file2String异常时间----"+new Date().toString());
			e.printStackTrace();
		} finally {
			if(bfr!=null){
				try {
					bfr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return str.toString();
	}

	//读取储罐物料信息
	public static Map<String,String> getMaterials(String filename){
		BufferedReader bfr = null;
		Map<String,String> map = new HashMap<String, String>();
		try {
			bfr = new BufferedReader(new FileReader(MATERIAL));
			String temp;
			while((temp=bfr.readLine())!=null){
				String[] strarr = temp.trim().split("[,，+]");
				map.put(strarr[0],strarr[1]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(bfr!=null){
				try {
					bfr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	} 
}
