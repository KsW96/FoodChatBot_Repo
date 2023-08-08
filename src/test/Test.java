package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import kr.co.shineware.nlp.komoran.modeler.model.Observation;

public class Test {
	public static void main(String[] args) {
		String filePath = "src/dic.user"; // 상대 경로 설정
		File userDic = new File(filePath);
		System.out.println(userDic.getAbsolutePath());

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
