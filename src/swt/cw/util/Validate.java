package swt.cw.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Validate {
	
	private static int[] getIndices(char c, String string){
		List<Integer> list = new ArrayList<>();
		char[] ca = string.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			if (ca[i]== c)
				list.add(i);
		}
		int[] res = new int[list.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = list.get(i);
		}
		return res;
	}
	
	public static boolean isBissexto(int ano){
		if (ano % 400 == 0)
			return true;
		else if (ano % 4 == 0 && ano % 100 != 0)
			return true;
		return false;
	}
	
	public static int getMaxDias(int mes, int ano){
		if (mes == 4 || mes == 6 || mes == 9 || mes == 11)
			return 30;
		else if (mes == 2){
			if (isBissexto(ano))
				return 29;
			else
				return 28;
		}
		return 31;
	}
	
	private static int getAnoAtual(){
		Calendar cal = Calendar.getInstance();  
        return cal.get(Calendar.YEAR); 
	}
	
	private static int getMesAtual(){
		Calendar cal = Calendar.getInstance();  
        return cal.get(Calendar.MONTH) + 1;  
	}
	
	public static boolean strDateTime(String string, String pattern){
		if (string.length() > pattern.length())
			return false;
		if (pattern.length() > string.length())
			pattern = pattern.substring(0, string.length()-1);
		char[] pChars = {'d','M','y','H','m','s'};
		char[] cString = string.toCharArray();
		int[] splitDate = new int[6];
		for (int i = 0; i < pChars.length; i++) {
			StringBuilder builder = new StringBuilder();
			int[] pIndices = getIndices(pChars[i], pattern);
			for (int j = 0; j < pIndices.length; j++) {
				builder.append(cString[pIndices[j]]);
			}
			if (builder.length() > 0)
				splitDate[i] = Integer.parseInt(builder.toString());
			else
				splitDate[i] = -1;
		}
		if (splitDate[5] != -1 && splitDate[5] > 60)
			return false;
		if (splitDate[4] != -1 && splitDate[4] > 60)
			return false;
		if (splitDate[3] != -1 && splitDate[3] > 23)
			return false;
		if (splitDate[2] != -1 && (splitDate[2] > 3000 || splitDate[2] < 1000))
			return false;
		if (splitDate[1] != -1 && splitDate[1] > 12)
			return false;
		if (splitDate[0] != -1){
			int max = getMaxDias(
					splitDate[1]!= -1? splitDate[1]:getMesAtual() ,
					splitDate[2] != -1 ? splitDate[2]: getAnoAtual());
			if (splitDate[0] > max)
				return false;
		}
		return true;
	}
}
