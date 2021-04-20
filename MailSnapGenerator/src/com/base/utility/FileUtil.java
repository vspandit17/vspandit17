package com.base.utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public abstract class FileUtil {
	static FileOutputStream fout = null;

	private FileUtil() {
	}

	public static String[] csvToArray(String fileName) throws IOException {
		String string = readFile(fileName);
		return string.split("\n");
	}

	public static String readFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists())
			throw new FileNotFoundException(fileName + " not found.");
		FileInputStream fis = new FileInputStream(file);
		byte[] array = new byte[fis.available()];
		fis.read(array);
		fis.close();
		return new String(array);
	}

	/**
	 * This method loads a file using scanner and file input stream and then
	 * reads its contents into a string object
	 * 
	 * @param fileName
	 * @return
	 */
	public static String loadFile(String fileName, Scanner sc) {
		String line = "";
		try {
			sc = new Scanner(new FileInputStream(fileName));
		} catch (FileNotFoundException fe) {
			System.out.println("Alert!File not found.");
		}
		while (sc.hasNextLine()) {
			line += sc.next() + ",";
		}
		return line;
	}

	public static String loadFile(BufferedInputStream bufferedInputStream, String fileName) {
		String line = "";
		try {
			if (bufferedInputStream == null) {
				bufferedInputStream = new BufferedInputStream(new FileInputStream(fileName));
			}
			byte[] b = new byte[bufferedInputStream.available()];
			bufferedInputStream.read(b);
			String str = new String(b);
			Scanner sc = new Scanner(str);
			while (sc.hasNextLine())
				line += sc.next() + ",";

		} catch (Exception e) {
		}

		return line;
	}

	/**
	 * returns array generated by splitting the string elements separated by ','
	 * 
	 * @param line
	 * @return
	 */
	public String[] getDataInArray(String line) {
		return line.split(",");
	}

	/**
	 * reads the entire file into a byte array. *
	 * 
	 * @param fileName
	 * @return
	 */
	public static byte[] readFile2Bytes(String fileName) {
		byte[] b = null;
		try {
			File in = new File(fileName);
			FileInputStream fin = new FileInputStream(in);
			b = new byte[fin.available()];
			fin.read(b);
			in = null;
			fin.close();
		} catch (Exception e) {
		}
		return b;
	}

	/**
	 * writes the entire text to a file without append.
	 * 
	 * @param text
	 * @param outputFile
	 */
	public static void writeToFile(String text, String outputFile) {
		if (text != null) {
			try {
				fout = new FileOutputStream(new File(outputFile));
				fout.write(text.getBytes());
				fout.flush();
			} catch (FileNotFoundException fe) {
				System.err.println("File not found.");
			} catch (IOException ioe) {
			} finally {
				fout = null;
			}
		}

	}

}