/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class NameIdMaker {
	private static Object[] _desc;
	
	public static void main(String args[]) {
		try {
			if (args.length < 1) {
				System.out.println("引数が足りません。");
				System.out.println("USAGE: inputfile [language]");
				return;
			}
			if (args.length >= 2) {
				loadTable(args[1]);
			} else {
				loadTable("j");
			}
			convert(args[0]);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	private static class Desc {
		int _id;
		String _text;

		Desc(int id, String text) {
			_id = id;
			_text = text;
		}

		private int getId() {
			return _id;
		}

		private String getText() {
			return _text;
		}
	}

	private static class DescComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			return ((Desc) o2).getText().length()
					- ((Desc) o1).getText().length();
		}
	}

	public static boolean containsKey(String key) {
		for (Object o : _desc) {
			Desc d = (Desc) o;
			if (d.getText().equals(key)) {
				return true;
			}
		}
		return false;
	}

	public static int getId(String key) {
		for (Object o : _desc) {
			Desc d = (Desc) o;
			if (d.getText().equals(key)) {
				return d.getId();
			}
		}
		return 0;
	}

	/**
	 * desc-j.tblを読み込み配列に格納する
	 */
	public static void loadTable(String lang) throws IOException {
		String path = "Desc-" + lang + ".tbl";
		LineNumberReader inr = null;
		try {
			File file = new File(path);
			inr = new LineNumberReader(new FileReader(file));
			inr.readLine(); // 先頭行は不要
			String line;
			List<Desc> desc = new ArrayList<Desc>();
			while ((line = inr.readLine()) != null) {
				// _desc.put(line, inr.getLineNumber() - 2);
				if (!line.isEmpty()
//						&& line.getBytes().length > 4 // 短い文字列("肉"など)は置換するとかえって長くなることが
//						&& line.length() < 24 // アイテムやNPCの名前とは思えない
						&& !line.matches("[0-9]")) {
					desc.add(new Desc(inr.getLineNumber() - 2, line));
				}
			}
			_desc = desc.toArray();
			Arrays.sort(_desc, new DescComparator());
		} catch (FileNotFoundException e) {
			throw new Error("Failed to Load " + path + " File.");
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			inr.close();
		}
	}
	
	/**
	 * 入力ファイルを読み込み、置換し、出力する
	 */
	public static void convert(String inputpath) throws IOException {
		String outputpath = inputpath.replace(".txt", "_id.txt");
		LineNumberReader inr = null;
		FileOutputStream fos = new FileOutputStream(outputpath);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "Shift-JIS");
		PrintWriter pw = new PrintWriter(osw);
		try {
			File file = new File(inputpath);
			inr = new LineNumberReader(new FileReader(file));
			String line;
			System.out.println(inputpath + " -> " + outputpath + " ... ");
			while ((line = inr.readLine()) != null) {
				pw.println(getReplacedString(line, false));
			}
			System.out.println(inr.getLineNumber() + "件を出力しました。");
		} catch (FileNotFoundException e) {
			throw new Error("Failed to Load " + inputpath + " File.");
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			inr.close();
			pw.close();
			osw.close();
			fos.close();
		}
	}

	/**
	 * 置換された文字列を返す
	 */
	public static String getReplacedString(String str, boolean takeShorter) {
		if (str.isEmpty()) {
			return "";
		}
		if (containsKey(str)) {
			return "$" + String.valueOf(getId(str));
		}
		String replacement = str;
		for (Object o : _desc) {
			Desc d = (Desc) o;
			if (replacement.indexOf(d.getText()) > -1) {
				String ptn1 = ".*" + d.getText() + "\\B.*";
				String ptn2 = ".*\\B" + d.getText() + ".*";
				if (replacement.matches(ptn1) || replacement.matches(ptn2)) {
					// 単語単位でのみ置換する(前後に空白がないような場合は置換しない)
					continue;
				}
				replacement = replacement.replace(d.getText(), "$" + d.getId());
				if (replacement.matches("^[\\d\\$ ]+$")) {
					// 置換すべき部分が残っていなければ終了
					break;
				}
			}
		}
		return replacement;
	}
}


// 置換方法を変えたので不要に
/**
 * 引数の文字列のうちdesc-j.tblに存在する部分を$xxxxの形に置換した文字列を返す。<br>
 * 例: "強化 グリーン ポーション" -> "$1652 $234"<br>
 * 例: "オアシス テレポート スクロール" -> "オアシス $230"<br>
 * 
 * @param str
 *            文字列
 * @param takeShorter
 *            true: 置換前の方がバイト数が少なくなる場合置換しない
 * @return 置換された文字列
 */
//public static String getReplacedString(String str, boolean takeShorter) {
//	if (str.isEmpty()) {
//		return "";
//	}
//	if (containsKey(str)) {
//		return "$" + String.valueOf(getId(str));
//	}
//	StringBuilder nameid = new StringBuilder();
//	StringBuilder temp = new StringBuilder();
//	String[] parts = str.split(" ");
//	boolean found;
//	int i;
//	int j;
//	for (i = 0; i < parts.length;) {
//		found = false;
//		for (j = parts.length - 1; j >= i; j--) {
//			if (i == 0 && j == parts.length - 1) {
//				// 文字列全体と同じなのでスキップ
//				continue;
//			}
//			for (int x = i; x <= j; x++) {
//				temp.append(parts[x]);
//				if (x + 1 <= j) {
//					temp.append(" ");
//				}
//			}
//			if (containsKey(temp.toString())) {
//				found = true;
//				break;
//			} else {
//				temp.delete(0, temp.length());
//			}
//		}
//		if (found) {
//			int id = getId(temp.toString());
//			if (takeShorter
//					&& ((temp.length() <= 2 && id >= 1000) || (temp
//							.length() <= 1 && id >= 10))) {
//				// 元のほうが短くなる場合は置換せずそのまま
//				nameid.append(temp.toString() + " ");
//			} else {
//				nameid.append("$" + String.valueOf(getId(temp.toString()))
//						+ " ");
//			}
//			temp.delete(0, temp.length());
//			i = j + 1;
//		} else {
//			nameid.append(parts[i] + " ");
//			i++;
//		}
//	}
//	return nameid.toString().trim();
//}
