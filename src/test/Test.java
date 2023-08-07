package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chatBot.dao.ChatBotDAO;
import chatBot.model.jsonModel.Food;
import chatBot.model.jsonModel.Request;
import chatBot.service.RecommendService;
import util.DBUtil;

public class Test {
	public static void main(String[] args) {
		System.out.println("시작");
		ChatBotDAO dao = new ChatBotDAO();
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			dao.updateByCount(conn, 1, "ingredient", "말", "죽");
			System.out.println("정상");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("실패");
		} finally {
			DBUtil.close(conn);
		}
	}
}


