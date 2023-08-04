package sqlTEST;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import util.DBUtil;

public class sqlTest {
	
	@Test
	public void sqlTest() {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;


		try {
			conn = DBUtil.getConnection();
			String tableName = "place";
			
			String word = "'집'";
			
			String plusSQL = "UNION ALL SELECT food, count FROM "+tableName+" where word = "+word+"";
			
			String li = "SELECT food, SUM(count) AS result\r\n" + 
					"FROM (\r\n" + 
					"    SELECT food, count FROM taste where word = \"단\"\r\n" + 
					"    UNION ALL \r\n" + 
					"    SELECT food, count FROM PERSON where word = \"아기\"\r\n" + 
					plusSQL +
					") AS combined_table\r\n" + 
					"GROUP BY food order by result DESC LIMIT 1;";
			
			
			stmt = conn.prepareStatement(li);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String foodYo = rs.getString("food");
				int result = rs.getInt("result");
				System.out.println("음식 : " + foodYo);
				System.out.println("카운트 : " + result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(conn);
		}
	}
}
