package chatBot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;

public class ChatBotDAO {
	// 키워드를 받으면 between 테이블을 조회해서 해당하는 숫자값 출력. 따뜻한 등으로 없는 키워드이면 -1 반환
	public int selectWhereBetween(String keyword) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM betweens WHERE text = ?;");
			stmt.setString(1, keyword);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id_use = rs.getInt("id_use");
				return id_use;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(conn);
		}
		return 0;
	}

	// 키워드를 이용하여 매칭되는 음식명을 반환하는 메소드
	public List<String> selectFormKeyword(String keyword) {
		List<String> list = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM food_keyword WHERE keyword = ?;");
			stmt.setString(1, keyword);
			rs = stmt.executeQuery();
			list = new ArrayList<>();
			while (rs.next()) {
				String food = rs.getString("food");
				list.add(food);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(conn);
		}
		return list;
	}

	// 키워드를 이용하여 매칭되는 음식명을 반환하는 메소드
	public List<String> selectFormMatching(String keyword) {
		List<String> list = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM food_matching WHERE keyword = ?;");
			stmt.setString(1, keyword);
			rs = stmt.executeQuery();
			list = new ArrayList<>();
			while (rs.next()) {
				String food = rs.getString("food");
				list.add(food);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(conn);
		}
		return list;
	}

	// 키워드를 이용하여 매칭되는 음식명을 반환하는 메소드
	public List<String> selectFormIngredient(String keyword) {
		List<String> list = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM food_ingredient WHERE ingredient = ?;");
			stmt.setString(1, keyword);
			rs = stmt.executeQuery();
			list = new ArrayList<>();
			while (rs.next()) {
				String food = rs.getString("food");
				list.add(food);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(conn);
		}
		return list;
	}

	public int plusResolve() {
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("UPDATE resolved SET count = count + 1 WHERE current_date();")) {
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public String getResolve() {
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM resolved WHERE date = current_date()")) {
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				return rs.getString("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public String getById(int i) {
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM food WHERE id = ?")) {
			stmt.setInt(1, i);
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				return rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
