package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;

public class FileController {

	public void saveFile(String path, JSONObject jsonObject) {

		try {

			System.out.println(path);

			BufferedWriter jsonBW = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("/"+path + "tmp.json"), "UTF-8"));

			FileWriter file = new FileWriter("/"+path + "tmp.json");
			file.write(jsonObject.toJSONString());
			file.flush();
			file.close();

			jsonBW.write(jsonObject.toJSONString());
			jsonBW.flush();
			jsonBW.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveFileS(String path, JSONObject jsonObject, String keyword) {

		try {
			System.out.println("/"+path+ keyword);

		/*	File newFile = new File("/"+path+ keyword);
			try {
				if (newFile.createNewFile()) { // 파일이 생성되는 시점
					System.out.println("파일이 생성되었습니다.");
				} else {
					System.out.println("파일을 생성하지 못했습니다.");
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("예외 처리");
				System.out.println("파일을 생성하는 과정에서 오류가 발생했습니다.");
			}*/

			BufferedWriter jsonBW = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("/"+path+ keyword), "UTF-8"));

			FileWriter file = new FileWriter("/"+path + keyword);
			file.write(jsonObject.toJSONString());
			file.flush();
			file.close();

			jsonBW.write(jsonObject.toJSONString());
			jsonBW.flush();
			jsonBW.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
