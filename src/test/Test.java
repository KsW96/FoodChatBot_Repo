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
		ObjectMapper mapper = new ObjectMapper();
		String json = "[{\"request\":\"요청\"},{\"food\":\"짜장\"}]";
		try {
			Request request = mapper.readValue(json, Request.class);
			Food food = mapper.readValue(json, Food.class);
			System.out.println(food.getFood());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


