package chatBot.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chatBot.dao.ChatBotDAO;
import imgFinder.ImageReturner;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String resolve = "0개의 해결된 사건";
		String food = "밥 먹자";
		req.setAttribute("resolve", resolve);

		resp.setStatus(200);
		resp.setHeader("Content-Type", "application/json;charset=utf-8");

		resp.getWriter().write("{\"food\": \"" + food + "\",");
		resp.getWriter().write("\"resolve\": \"" + resolve + "\"}");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = req.getReader();
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

		System.out.println(chat);
	}


}
