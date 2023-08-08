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
import chatBot.model.jsonModel.WoCate;
import chatBot.service.InsertService;
import chatBot.service.RecommendService;
import chatBot.service.UnKnownService;
import chatBot.service.UpdateService;
import imgFinder.ImageReturner;
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
	UpdateService updateS = new UpdateService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			JSONObject list = new JSONObject();
			list.put("list", is.searchAllFood(conn));
			System.out.println("응답 answer : " + list);
			resp.getWriter().write(String.valueOf(list));
			resp.setStatus(200);
			resp.setHeader("Content-Type", "application/json;charset=utf-8");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn);
		}
	}

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
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			// 커넥션 생성
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

			if (category.equals("거절")) {
				System.out.println("두풋 거절방식");
				updateS.updateByCount(conn, -1, strWord);
			} else if (category.equals("수락")) {
				System.out.println("두풋 수락방식");
				updateS.updateByCount(conn, 1, strWord);
			} else if (category.equals("음식")) {
				System.out.println("두풋 음식방식");
				// 모든 words의 word와 category 가져와서 해당카데고리에
				// words foodTarget 넣어주기
				is.insertFood(conn, strWord);
				List<WoCate> wordList = is.searchAllWord(conn);
				for (WoCate wc : wordList) {
					System.out.println("현재 words" + wc.getWord());
					String currentWord = wc.getWord();
					System.out.println("현재 words의 카테고리" + wc.getCategory());
					String currentCategory = wc.getCategory();
					is.insertCategory(conn, currentCategory, currentWord, strWord);
				}
			} else { // 거절, 수락, 음식 아니면 정보 저장후에 음식명 출력해야함
				System.out.println("두풋 저장방식");
				// words 에 넣어주기
				String categoryT = ReturnTranslate.Translate(category);
				System.out.println("번역된 카테고리" + categoryT);
				// 영어로 번역
				is.insertFood(conn, strWord);
				is.insert(conn, strWord, categoryT);

				// 해당 카테고리에 word, food 넣어주기

				// 새로운 단어 학습할때 해당카데고리에 모든 음식을 추가해주기

				// 메소드 food 테이블 접근해서 모든 food 가지는 list<String> 만들기

				// food 테이블 접근해서 word, 모든 food 넣어주고
				// 대화를 통해서 사용자가 선택한 food는 중복검사해서
				// 없으면 넣어주기 -> 완전 새로운 food일 경우니까 word접근해서 모든 테이블에 행으로 추가해주기

				// String refer foods
				// 먼저 food에 있는거 다때려 박고
				List<String> foodList = is.searchAllFood(conn);
				System.out.println("현재 모든 푸드리스트" + foodList.toString());
				for (String str : foodList) {
					is.insertCategory(conn, categoryT, strWord, str);
				}
				// 중복검사해서 있는거 카운트++, 없는거는 추가해주는데 새로운 food니까 food에 넣고, words에 접근해서 모든행들에 새로운
				// food넣어주기
				for (int i = 2; i < jsonArr.size(); i++) {

					System.out.println(jsonArr.get(i));
					JSONObject food = (JSONObject) jsonArr.get(i);
					String foodTarget = food.get("food").toString();

					if (!is.searchFood(conn, foodTarget)) {
						System.out.println("food: " + foodTarget);
						// food에 해당 새로운 음식인 foodTarget 넣어주고
						is.insertFood(conn, foodTarget);
						// 모든 words의 word와 category 가져와서 해당카데고리에
						// words foodTarget 넣어주기
						List<WoCate> wordList = is.searchAllWord(conn);
						for (WoCate wc : wordList) {
							System.out.println("현재 words" + wc.getWord());
							String currentWord = wc.getWord();
							System.out.println("현재 words의 카테고리" + wc.getCategory());
							String currentCategory = wc.getCategory();
							is.insertCategory(conn, currentCategory, currentWord, foodTarget);
						}
					}
				}
				// !태인이형이 주는 양식에 따라 db에 저장하는 형식의 코드를 작성한다.
				String requestData = null;
//				insert(requestData);
				// 하나의 음식명을 반환하는 메소드
				// 아는단어리스트에 새로 배운 단어 추가해야함
				String foodName = foodName(RememberWordList.getKnownWordList()); // !foodName 미완성임. 성우행님이 쿼리문 완성하면 변경됨
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
		} finally {
			DBUtil.close(conn);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("기억하는 단어들 : " + RememberWordList.getKnownWordList());

		JSONObject answer = new JSONObject();
		List<String> chat = splitString(req);
		if (chat == null) {
			answer.put("answer", "");
			resp.getWriter().write(String.valueOf(answer));
		}

		// 사용자 입력 문자열
		if (chat.size() != 0) { // 요청 body의 값이 chat 일때
			// chat을 자연어 처리해서 wordList로 넣는다
			String unknownWord = us.unknownWord(chat); // 단어 리스트를 넣어서 모르는 단어 하나를 받는다
			if (unknownWord != null) { // 모르는 단어가 있을 때 - 모르는 단어가 없으면 null을 반환해서 조건처리한다
				String requestS = "{\"request\": \"" + unknownWord + "\"}";
				System.out.println("응답 request : " + requestS);
				resp.getWriter().write(requestS);
			} else { // 모르는 단어가 없을 때 - unknownWord 가 null 이면 모르는 단어가 없으므로 음식명을 반환한다.
				String foodName = foodName(RememberWordList.getKnownWordList());
				answer.put("answer", foodName);
				answer.put("img", ImageReturner.imageReturn(foodName));
				System.out.println("응답 answer : " + answer);
				resp.getWriter().write(String.valueOf(answer));
			}
			resp.setStatus(200);
			resp.setHeader("Content-Type", "application/json;charset=utf-8");
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
