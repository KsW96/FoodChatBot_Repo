package imgFinder;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageReturner {
	public static void main(String[] args) {
		Gson gson = new Gson();
		OkHttpClient client = new OkHttpClient();
		String name = "설렁탕";
		Request request = new Request.Builder()
				.url("https://dapi.kakao.com/v2/search/image?query=" + name + "&page=1&size=1")
				.addHeader("Authorization", "KakaoAK 16a31b75ebda66fb5ccb58afcb8d9bc1").get().build();

		Call call = client.newCall(request);
		try (Response resp = call.execute()) {
			int code = resp.code();
			System.out.println(code);
			if (code == 200) {
				System.out.println("OK");

				String body = resp.body().string();
				System.out.println(body);

				try {
					JSONParser parser = new JSONParser();
					JSONArray values;
					values = (JSONArray) parser.parse(body);
					JSONObject value = (JSONObject) values.get(0);
					System.out.println((String) value.get("image_url"));
				} catch (ParseException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("aa");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}