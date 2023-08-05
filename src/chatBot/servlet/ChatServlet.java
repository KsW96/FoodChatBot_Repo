package chatBot.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import chatBot.model.KnownWordList;
import chatBot.model.jsonModel.Request;
import chatBot.service.InsertService;
import chatBot.service.RecommendService;
import chatBot.service.UnKnownService;
import nlp.NLP;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
	UnKnownService us = new UnKnownService();
	RecommendService rs = new RecommendService();
	InsertService is = new InsertService();
	KnownWordList knownWordList = new KnownWordList();
	ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(200);
		resp.setHeader("Content-Type", "application/json;charset=utf-8");
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

		Request request = mapper.readValue(jsonItems[0], Request.class);
			
			
		if (request != null) { // 요청 body의 값이 request 일때
			// !태인이형이 주는 양식에 따라 db에 저장하는 형식의 코드를 작성한다.
			String requestData = null;
			insert(requestData);
			// 하나의 음식명을 반환하는 메소드
			// 아는단어리스트에 새로 배운 단어 추가해야함
			String foodName = foodName(knownWordList.getKnownWordList()); // !foodName 미완성임. 성우행님이 쿼리문 완성하면 변경됨
			resp.setStatus(200);
			resp.setHeader("Content-Type", "application/json;charset=utf-8");
			resp.getWriter().write("\"answer\": \"" + foodName + "\"}");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 시험(은) 가능.. (answer, request 값 넣어보세요)
		 resp.getWriter().write("{\"request\": \"" + "밥" + "\"}");

		List<String> chat = splitString(req);
		for (String elem : chat) {
			System.out.println(elem);
		}

		// 사용자 입력 문자열
		if (chat.size() != 0) { // 요청 body의 값이 chat 일때
			// chat을 자연어 처리해서 wordList로 넣는다
			List<String> wordList = null;
			String unknownWord = us.unknownWord(wordList); // 단어 리스트를 넣어서 모르는 단어 하나를 받는다
			if (unknownWord != null) { // 모르는 단어가 있을 때 - 모르는 단어가 없으면 null을 반환해서 조건처리한다
				resp.setStatus(200);
				resp.setHeader("Content-Type", "application/json;charset=utf-8");
				resp.getWriter().write("\"request\": \"" + unknownWord + "\"}");
			} else { // 모르는 단어가 없을 때 - unknownWord 가 null 이면 모르는 단어가 없으므로 음식명을 반환한다.
				String foodName = foodName(knownWordList.getKnownWordList());
				resp.setStatus(200);
				resp.setHeader("Content-Type", "application/json;charset=utf-8");
				resp.getWriter().write("\"answer\": \"" + foodName + "\"}");
			}
		} else {
			// 문제있는 상황에 여기로 옵니다.
			System.out.println("서블릿에서 문제가 있습니다.");
		}
	}

	public void insert(String requestData) {
		is.insert(requestData);
	}

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
