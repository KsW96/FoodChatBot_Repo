package chatBot.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import chatBot.dao.ChatBotDAO;
import chatBot.model.RememberWordList;
import chatBot.model.jsonModel.Request;
import chatBot.service.InsertService;
import chatBot.service.RecommendService;
import chatBot.service.UnKnownService;
import nlp.NLP;
import util.DBUtil;
import util.ReturnTranslate;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
	UnKnownService us = new UnKnownService();
	RecommendService rs = new RecommendService();
	InsertService is = new InsertService();
	ObjectMapper mapper = new ObjectMapper();
	ChatBotDAO dao = new ChatBotDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(200);
		resp.setHeader("Content-Type", "application/json;charset=utf-8");
	}

	// 단어 모를때 정보 포장해서 요청하는 곳
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// request json 형태로 오는 정보임
		StringBuilder sb = new StringBuilder();
		BufferedReader br = req.getReader();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		String body = sb.toString();
		String[] jsonItems = body.split(",");

		System.out.println(body);
		JSONParser parser = new JSONParser();
		try {
			JSONArray jsonArr = (JSONArray) parser.parse(body);
			System.out.println(jsonArr.size());
			// word + category words에 넣어주기

			// category에
			// word + food

			// String word
			System.out.println(jsonArr.get(0));
			JSONObject word = (JSONObject) jsonArr.get(0);
			String strWord = word.get("request").toString();
			System.out.println("word: " + strWord);

			// String category
			System.out.println(jsonArr.get(1));
			JSONObject cate = (JSONObject) jsonArr.get(1);
			String category = cate.get("category").toString();
			System.out.println("category: " + category);

			// category가 값이 거절일 경우 = request 단어를 거절리스트에 저장해서 다시 그 음식을 추천 안하게끔 하는거
			// 분기점으로써 여기 분기가 실행된다면 아래 전체코드 실행되면 안됨
			
			if (category.equals("거절")) {
				RememberWordList.addRefusalList(strWord);
				// 감가상각하는거 만들기
			} else if (category.equals("수락")){
				
			} else if (category.contentEquals("음식")){
				
			} else {

				// 영어로 번역
				String categoryT = ReturnTranslate.Translate(category);
				System.out.println("번역된 카테고리" + categoryT);

				// 커넥션 생성
				Connection conn = DBUtil.getConnection();
				// words 에 넣어주기
				is.insert(conn, strWord, categoryT);

				// 해당 카테고리에 word, food 넣어주기

				// 새로운 단어 학습할때 해당카데고리에 모든 음식을 추가해주기

				// 메소드 food 테이블 접근해서 모든 food 가지는 list<String> 만들기

				// food 테이블 접근해서 word, 모든 food 넣어주고
				// 대화를 통해서 사용자가 선택한 food는 중복검사해서
				// 없으면 넣어주기 -> 완전 새로운 food일 경우니까 word접근해서 모든 테이블에 행으로 추가해주기

				// String refer foods
				for (int i = 2; i < jsonArr.size(); i++) {
					System.out.println(jsonArr.get(i));
					JSONObject food = (JSONObject) jsonArr.get(i);
					String foodTarget = food.get("food").toString();
					System.out.println("food: " + foodTarget);
					is.insertCategory(conn, categoryT, strWord, foodTarget);
				}
				List<String> list = RememberWordList.getKnownWordList();
				list.add(strWord);
				RememberWordList.addKnownWordList(strWord);
				String foodName = foodName(RememberWordList.getKnownWordList()); // !foodName 미완성임. 성우행님이 쿼리문 완성하면 변경됨
				System.out.println(RememberWordList.getKnownWordList());
				resp.setStatus(200);
				resp.setHeader("Content-Type", "application/json;charset=utf-8");
				String answer = "{\"answer\": \"" + foodName + "\"}";
				System.out.println("응답 answer : " + answer);

				resp.getWriter().write(answer);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 하나의 음식명을 반환하는 메소드
	}

	// 사용자 문자열 보내는 요청방식
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 시험(은) 가능.. (answer, request 값 넣어보세요)
		// resp.getWriter().write("{\"request\": \"" + "밥" + "\"}");
		// resp.getWriter().write("{\"request\": \"" + "밥" + "\"}");

		List<String> chat = splitString(req);
		for (String elem : chat) {
			System.out.println("단어들 : " + elem);
		}

		// 사용자 입력 문자열
		if (chat.size() != 0) { // 요청 body의 값이 chat 일때
			// chat을 자연어 처리해서 wordList로 넣는다
			String unknownWord = us.unknownWord(chat); // 단어 리스트를 넣어서 모르는 단어 하나를 받는다
			if (unknownWord != null) { // 모르는 단어가 있을 때 - 모르는 단어가 없으면 null을 반환해서 조건처리한다
				resp.setStatus(200);
				resp.setHeader("Content-Type", "application/json;charset=utf-8");
				String requestS = "{\"request\": \"" + unknownWord + "\"}";
				System.out.println("응답 request : " + requestS);
				resp.getWriter().write(requestS);
			} else { // 모르는 단어가 없을 때 - unknownWord 가 null 이면 모르는 단어가 없으므로 음식명을 반환한다.
				String foodName = foodName(RememberWordList.getKnownWordList());
				resp.setStatus(200);
				resp.setHeader("Content-Type", "application/json;charset=utf-8");
				String answer = "{\"answer\": \"" + foodName + "\"}";
				System.out.println("응답 answer : " + answer);
				resp.getWriter().write(answer);
			}
		} else {
			// 문제있는 상황에 여기로 옵니다.
			System.out.println("서블릿에서 문제가 있습니다.");
		}
	}

//	public void insert(String requestData) {
//		is.insert(requestData);
//	}

	public String foodName(List<String> knownList) {
		String foodName = rs.recommendFoodName(knownList);
		return foodName;
	}

	public List<String> splitString(HttpServletRequest req) throws IOException {
		List<String> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = req.getReader();
		NLP nlp = new NLP();

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		String body = sb.toString();
		System.out.println("사용자 요청 body 확인: " + body);

		Pattern p = Pattern.compile("\\{\"chat\":\"(.+?)\"\\}");
		Matcher m = p.matcher(body);
		m.find();
		String chat = m.group(1);

		list = nlp.doNLP(chat);
		list = rs.removeException(list);
		return list;
	}
}
