package chatBot.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import chatBot.dao.ChatBotDAO;
import chatBot.model.FoodCount;
import chatBot.model.WordCategory;
import chatBot.model.WordFoodCount;
import util.DBUtil;

public class recommendService {
	// 추가 작성해야 하는 것들
	// wfcList
	ChatBotDAO dao = new ChatBotDAO();
	checkedNewListService cnl = new checkedNewListService();
	List<String> foodList = null;
	List<String> wordList = null;
	List<String> exceptionList = null;

	public recommendService() {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			this.foodList = dao.getFood(conn);
			this.wordList = dao.getWords(conn);
			this.exceptionList = dao.getExceptions(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			DBUtil.close(conn);
		}
	}

	public List<String> getFoodList() {
		return foodList;
	}

	// 반복적으로 처리한 후에 제일 높은 음식명 반환
	public String recommendFood(List<String> wordList) {
		List<FoodCount> fcList = foodCounted(wordList);
		int baseCount = -10000;
		String firstFoodName = "";
		for (FoodCount fc : fcList) {
			if (baseCount <= fc.getCount()) {
				baseCount = fc.getCount();
				firstFoodName = fc.getFood();
			}
		}
		return firstFoodName;
	}

	// 단어 리스트를 받으면 반복적으로 체크하고 처리하는 메소드
	public List<FoodCount> foodCounted(List<String> wordList) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			cnl.checkFoodList(foodList, conn);
			cnl.checkWordList(this.wordList, conn);
			List<FoodCount> fcList = new ArrayList<FoodCount>();
			fillFcList(fcList);
			for (String word : wordList) {
				List<WordFoodCount> wfcList = getWfcList(word, conn);
				// null 아닐때 체크하기 만들기
				changeCount(fcList, wfcList);
			}
			return fcList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// fcList = 전체 음식과 점수, wfcList = 선택한 단어가 가지는 음식과 점수
	// 전체 음식과 점수를 감가하는 메소드
	public void changeCount(List<FoodCount> fcList, List<WordFoodCount> wfcList) {
		for (WordFoodCount wfc : wfcList) {
			String food = wfc.getFood();
			int index = fcList.indexOf(new FoodCount(food, 0));
			int count = wfc.getCount();
			FoodCount fc = fcList.get(index);
			int resultCount = fc.getCount() + count;
			fc.setCount(resultCount);
			fcList.set(index, fc);
		}
	}

	public void fillFcList(List<FoodCount> fcList) {
		for (String food : foodList) {
			fcList.add(new FoodCount(food, 0));
		}
	}

	// 단어 하나를 이용하여 해당하는 단어가 가지는 음식과 카운트를 반환하는 메소드
	public List<WordFoodCount> getWfcList(String word, Connection conn) throws SQLException {
		List<WordFoodCount> wfcList = null;
		WordCategory wc = dao.selectFromWords(word, conn);
		if (wc != null) {
			wfcList = dao.selectFromTable(wc, conn);
		} else {
			// !!!! 모르는 단어라서 단어를 추가하는 새로운 분기섬 생성해야함
		}
		return wfcList;
	}

	// excption테이블에 있는 필요없는 단어 제거해서 넘겨주기
	public List<String> removeException(List<String> list) {
		List<String> words = new ArrayList<>();
		List<String> exception = exceptionList;

		words.addAll(list);
		words.removeAll(exception);
		System.out.println(words);

		return words;
	}
}
