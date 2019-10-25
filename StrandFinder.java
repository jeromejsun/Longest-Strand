import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;

public class StrandFinder {
	
	class ByteContent {
		byte contents[];
		String filename;
		
		ByteContent(String fn) {
			filename = fn;
			File file = new File(fn);
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(file);
				contents = new byte[(int)file.length()];
				fin.read(contents);
				System.out.println(">> " + filename + " read " + (int)file.length() + " bytes");
			}
			catch (Exception ex) {
				System.out.println(">> ex: " + ex.toString());
			}
			finally {
				try {
					fin.close();
				}
				catch (Exception ex) {
				}
			}
		}
	}
	
	public StrandFinder(List<String> files) {
		int checkLen = 2;
		int offset1 = 0;
		int offset2 = 0;
		ByteContent content1 = null;
		ByteContent content2 = null;
		
		List<ByteContent> data = new ArrayList<>();
		for(String f: files) {
			ByteContent bc = new ByteContent(f);
			data.add(bc);
		}

		int len = data.size();
		for(int i = 0; i < (len - 1); i++) {
			byte[] contents1 = data.get(i).contents;
			int len1 = contents1.length;
			for(int j = (i + 1); j < len; j++) {
				byte[] contents2 = data.get(j).contents;
				int len2 = contents2.length;
				int cursor1 = 0;
				int cursor2 = 0;
				while(cursor1 + checkLen <= len1) {
					while(cursor2 + checkLen <= len2) {
						while(matched(contents1, contents2, cursor1, cursor2, checkLen)) {
							// System.out.println("match found for len = " + checkLen);
							content1 = data.get(i);
							content2 = data.get(j);
							offset1 = cursor1;
							offset2 = cursor2;
							checkLen += 1;
						}
						cursor2++;
					}
					cursor1++;
					cursor2 = 0;
				}
			}
		}
		
		System.out.println(">> Found maxlen matched strand = " + (checkLen - 1));
		System.out.println(">> Found in file1 = " + content1.filename + ", strand starts with offset = " + offset1);
		System.out.println(">> Found in file2 = " + content2.filename + ", strand starts with offset = " + offset2);
	}
	
	private boolean matched(byte[] contents1, byte[] contents2, int cursor1, int cursor2, int checkLen) {
		int len1 = contents1.length;
		if(cursor1 + checkLen > len1)
			return false;
		
		int len2 = contents2.length;
		if(cursor2 + checkLen > len2)
			return false;
		
		for(int i = 0; i < checkLen; i++) {
			if(contents1[cursor1 + i] != contents2[cursor2 + i])
				return false;
		}
		// System.out.println("return true: " + checkLen);
		return true;
	}
	
	public static void main(String[] args) {
		List<String> files = new ArrayList<>();
		files.add("sample.1");
		files.add("sample.2");
		files.add("sample.3");
		files.add("sample.4");
		files.add("sample.5");
		files.add("sample.6");
		files.add("sample.7");
		files.add("sample.8");
		files.add("sample.9");
		files.add("sample.10");
		
		StrandFinder finder = new StrandFinder(files);
	}
}