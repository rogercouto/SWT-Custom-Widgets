package swt.cw.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe respons\u00E1vel por formatar/converter tipos e dados
 * @author Roger
 *
 */
public class Format {

	public static final String INT = "0";
	public static final String DECIMAL = "0.00";
	public static final String DATE = "dd/MM/yyyy";
	public static final String TIME = "HH:mm";
	public static final String TIMESTAMP = "dd/MM/yyyy HH:mm:ss";
	
	/**
	 * Formata uma data/hora para texto
	 * @param date - Data a ser formatada
	 * @param pattern - padr\u00E3o da data (ex: dd/MM/yyyy)
	 * @return texto formatado(String)
	 */
	public static String toString(Date date, String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * Formata um n\u00FAmero para texto
	 * @param number - N\u00FAmero a ser formatado
	 * @param pattern - padr\u00E3o do n\u00FAmero (ex: #,##0.00)
	 * @return texto formatado(String)
	 */
	public static String toString(Number number, String pattern){
		return new DecimalFormat(pattern).format(number);
	}
	
	/**
	 * Converte um texto em data
	 * @param string - Texto a ser convertido
	 * @param pattern - Padr\u00E3o da data/hora (ex: dd/mm/yyyy)
	 * @return Data convertida (Date)
	 */
	public static Date toDate(String string, String pattern){
		try {
			String fullSDate = toStringDate(string, pattern);
			return new SimpleDateFormat(pattern).parse(fullSDate);
		} catch (ParseException e) {
		}
		return null;
	}
	
	/**
	 * Converte um texto em n\u00FAmero
	 * @param string - Texto a ser convertido
	 * @param pattern - padr\u00E3o do n\u00FAmero (ex: #,##0.00)
	 * @return Valor convertido (Number)
	 */
	public static Number toNumber(String string, String pattern){
		try {
			return new DecimalFormat(pattern).parse(string.replace('.', ','));
		} catch (ParseException e) {
		}
		return null;
	}
	
	/**
	 * Retorna um texto somente com carateres num\u00E9ricos v\u00E1lidos.
	 * @param text - String a ser formatada
	 * @return texto formatado(String)
	 */
	public static String toStringNumber(String text){
		char[] ca = text.toCharArray();
		StringBuilder builder = new StringBuilder();
		boolean haveComma = false;
		for (int i = 0; i < ca.length; i++) {
			if (i == 0 && ca[i] == '-')
				builder.append(ca[i]);
			if (i == 0 && ca[i] == ','){
				builder.append("0,");
				haveComma = true;
			}
			if (ca[i] == ','){
				if (!haveComma){
					builder.append(ca[i]);
					haveComma = true;
				}
			}
			if (Character.isDigit(ca[i]))
				builder.append(ca[i]);
		}
		return builder.toString();
	}
	
	/**
	 * Retorna um texto somente com carateres num\u00E9ricos v\u00E1lidos.
	 * @param text - String a ser formatada
	 * @param pattern - Padr\u00E3o do n\u00FAmero (Ex: 0.00)
	 * @return texto formatado(String)
	 */
	public static String toStringNumber(String text, String pattern){
		try {
			if (text.trim().length() == 0)
				return text;
			NumberFormat nf = new DecimalFormat(pattern);
			Number n = nf.parse(text);
			return nf.format(n);
		} catch (ParseException e) {
		}
		return "";
	}
	
	/**
	 * Retorna um texto com caracteres v\u00E1lidos para o tipo data/hora
	 * @param strDate - texto a ser formatado
	 * @param pattern - Padr\u00E3o da data/hora
	 * @return texto formatado(String)
	 */
	public static String toStringDate(String strDate, String pattern){
		String maskedDate = InputMask.maskString(strDate, pattern);
		if (maskedDate.length() < pattern.length()){
			char[] ca = maskedDate.toCharArray();
			char[] pa = pattern.toCharArray();
			StringBuilder builder = new StringBuilder();
			char[] yc = getCurrenYearAsArray();
			int yCount = 0;
			for (int i = 0; i < pa.length; i++) {
				if (i < ca.length){
					builder.append(ca[i]);
				}else{
					if (pa[i] == 'y'){
						builder.append(yc[yCount++]);
					}else if (pa[i] == 'H' || pa[i] == 'm' || pa[i] == 's'){
						builder.append('0');
					}else if (pa[i] == 'd' || pa[i] == 'M'){
						return "";
					}else{
						builder.append(pa[i]);
					}
				}
			}
			return builder.toString();
		}
		return maskedDate;
	}
	
	private static char[] getCurrenYearAsArray(){
		String year = new SimpleDateFormat("yyyy").format(new Date());
		return year.toCharArray();
	}
	
}
