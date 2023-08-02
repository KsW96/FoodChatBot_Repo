package test;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import chatBot.dao.ChatBotDAO;
import util.DBUtil;

public class Test {
	public static void main(String[] args) {
		Properties prop = new Properties();
		ClassLoader classLoader = DBUtil.class.getClassLoader();
		try {
			prop.load(classLoader.getResourceAsStream("ttt.properties"));
			String first = prop.getProperty("botText1_1");
			System.out.println(first);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
