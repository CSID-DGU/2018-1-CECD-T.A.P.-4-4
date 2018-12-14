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
				if (newFile.createNewFile()) { // ������ �����Ǵ� ����
					System.out.println("������ �����Ǿ����ϴ�.");
				} else {
					System.out.println("������ �������� ���߽��ϴ�.");
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("���� ó��");
				System.out.println("������ �����ϴ� �������� ������ �߻��߽��ϴ�.");
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
