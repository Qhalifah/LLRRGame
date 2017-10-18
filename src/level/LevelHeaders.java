package level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import launcher.Launcher;
import launcher.ResourcesDownloader;

public class LevelHeaders {

	public static String addSpacesAtCapitals(String s) {
		String caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String r = "";
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (caps.contains(c + "")) r += " ";
			r += c;
		}
		return r.trim();
	}

	public static String load(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(ResourcesDownloader.resPath + "text/" + Launcher.lang + "/headers/" + fileName + ".header"))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			return sb.toString();
		} catch (IOException e) {
			System.out.println("No header found for level \"" + fileName + "\" in language " + Launcher.lang);
			return "";
		}
	}
}
