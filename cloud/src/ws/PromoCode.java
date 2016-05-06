package ws;

import java.util.Random;

/**
 * @param args
 */
public class PromoCode {

	static boolean charOk(char in) {
		String charOKstr = "0987654321";
		int coklen = charOKstr.length();	
		for (int i=0; i<coklen; i++) {
			if (in == charOKstr.charAt(i))
				return true;
		}
		return false;
	}

	// return 1~9
	static int randChar() {
		 Random rand = new Random(); 
		 return  1 + rand.nextInt(9); 
	}
	
	static boolean inputOk(String in) {
		int length = in.length();
		if (length != 8)
			return false;
		for (int i=0; i<length; i++) { 
			if (!charOk(in.charAt(i)))
					return false;
		}
		return true;
	}
	
	static String codeDirect(String in) {
		if (!inputOk(in))
			return "wrong input";
		String ret = "";
		int checksum = 0;
		int salt = randChar();
		checksum += salt;
		ret += salt;
		for (int i=0; i<8; i++) { // in.lenght = 8
			int charToAdd = (((int) in.charAt(i) - '0' + salt) % 10);
			ret += charToAdd;
			checksum += charToAdd;
		}
		checksum = checksum % 10;
		ret += checksum;
		// System.out.println("DEBUG:" + ret);
		return ret;
	}
	
	public static boolean codeOK(String in) {
		int length = in.length();
		if (length != 10) // 8 + 1 for satl + 1 for checksum
			return false;
		int checksum = 0;
		for (int i=0; i<9; i++) { // in.lenght = 8
			checksum += (int) in.charAt(i) - '0';
		}
		checksum = checksum % 10;
		return checksum == (int) in.charAt(9) - '0';
	}
	
	static String codeReverse(String in) {
		if (!codeOK(in))
			return "wrong input";
		int salt = (int) in.charAt(0) - '0';
		String ret = "";
		for (int i=1; i<9; i++) { // in.lenght = 8
			int charToAdd = ((int) in.charAt(i) - '0' - salt + 10) % 10; 
			ret += charToAdd;
		}
//		System.out.println("DEBUG:" + ret);
		return ret;
	}
}
