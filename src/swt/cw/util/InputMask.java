package swt.cw.util;

/**
 * Classe responsável por formatar textos usando máscara de entrada
 * @author Roger
 */
public class InputMask {

	public static final String CPF = "###.###.###-##";
	public static final String CNPJ = "##.###.###/####-##";
	public static final String FONE = "(##)####-####";
	public static final String CEP = "#####-###";
	public static final String PLACA = "UUU-####";
	public static final String DATA =  "dd/MM/yyyy";
	public static final String HORA = "HH:mm";
	public static final String DATA_HORA = "dd/MM/yyyy HH:mm";

	private static final char DIGIT = '#';
	private static final char LETTER = '?';
	private static final char LC = 'L';
	private static final char UC = 'U';
	private static final char ANY = 'A';

	/**
	 * Adiciona uma máscara de entrada em uma String
	 * @param string - Texto a ser modificado
	 * @param pattern - Mascara de entrada
	 * @return String com a mascara
	 */
	public static String maskString(String string, String pattern){
		pattern = pattern.replaceAll("[d,M,y,H,m,s]", "#");
		StringBuilder builder = new StringBuilder();
		char[] ta = string.toCharArray();
		char[] ma = pattern.toCharArray();
		int count = 0;
		for (int i = 0; i < ta.length; i++) {
			for (int j = count; j < ma.length; j++) {
				if (ma[j] == DIGIT){
					if (Character.isDigit(ta[i])){
						builder.append(ta[i]);
						count++;
						break;
					}else{
						break;
					}
				}else if (ma[j] == LETTER){
					if (Character.isLetter(ta[i])){
						builder.append(ta[i]);
						count++;
						break;
					}else{
						break;
					}
				}else if (ma[j] == LC){
					if (Character.isLetter(ta[i])){
						builder.append(Character.toLowerCase(ta[i]));
						count++;
						break;
					}else{
						break;
					}
				}else if (ma[j] == UC){
					if (Character.isLetter(ta[i])){
						builder.append(Character.toUpperCase(ta[i]));
						count++;
						break;
					}else{
						break;
					}
				}else if (ma[j] == ANY){
					if (Character.isLetterOrDigit(ta[i])){
						builder.append(ta[i]);
						count++;
						break;
					}else{
						break;
					}
				}else if (ma[j] == ' '){
					builder.append(ma[j]);
					count++;
				}else{
					builder.append(ma[j]);
					count++;
				}
			}
		}
		return builder.toString();
	}

	/**
	 * Retorna um string com caracteres válidos de acordo com a máscara de entrada
	 * @param string - Texto a ser verificado
	 * @param pattern - Padrão da máscara de entrada
	 * @return texto verificado(String)
	 */
	public static String getVerifyedString(String string, String pattern){
		pattern = pattern.replaceAll("[d,M,y,H,m,s]", "#");
		StringBuilder builder = new StringBuilder();
		char[] ta = string.toCharArray();
		char[] ma = pattern.toCharArray();
		int count = 0;
		for (int i = 0; i < ta.length; i++) {
			for (int j = count; j < ma.length; j++) {
				if (ma[j] == DIGIT){
					if (Character.isDigit(ta[i])){
						builder.append(ta[i]);
						count++;
						break;
					}else{
						break;
					}
				}else if (ma[j] == LETTER){
					if (Character.isLetter(ta[i])){
						builder.append(ta[i]);
						count++;
						break;
					}else{
						break;
					}
				}else if (ma[j] == LC){
					if (Character.isLetter(ta[i])){
						builder.append(Character.toLowerCase(ta[i]));
						count++;
						break;
					}else{
						break;
					}
				}else if (ma[j] == UC){
					if (Character.isLetter(ta[i])){
						builder.append(Character.toUpperCase(ta[i]));
						count++;
						break;
					}else{
						break;
					}
				}else if (ma[j] == ANY){
					if (Character.isLetterOrDigit(ta[i])){
						builder.append(ta[i]);
						count++;
						break;
					}else{
						break;
					}
				}
			}
		}
		return builder.toString();
	}

	/**
	 * Remove uma mascara de entrada de um texto
	 * @param string - Texto a ser limpo
	 * @param pattern - Padrão da máscara a ser retirada
	 * @return String limpa
	 */
	public static String unmaskString(String string, String pattern){
		pattern = pattern.replaceAll("[d,M,y,H,m,s]", "#");
		StringBuilder builder = new StringBuilder();
		char[] ta = string.toCharArray();
		char[] ma = pattern.toCharArray();
		for (int i = 0; i < ta.length; i++) {
			if (ma[i] == DIGIT || ma[i] == LETTER || ma[i] == UC || ma[i] == LC || ma[i] == ANY){
				builder.append(ta[i]);
			}
		}
		return builder.toString();
	}

	/**
	 * Remove a máscara de entrada de um texto(Método simples somente retorna as letras e números)
	 * @param string - texto com máscara
	 * @return texto sem máscara(String)
	 */
	public static String unmaskString(String string){
		StringBuilder builder = new StringBuilder();
		char[] ta = string.toCharArray();
		for (int i = 0; i < ta.length; i++) {
			if (Character.isLetterOrDigit(ta[i])){
				builder.append(ta[i]);
			}
		}
		return builder.toString();
	}

	/**
	 * Retorna o tamanho do texto sem carateres de máscara de entrada
	 * @param text - String com máscara de entrada
	 * @return tamanho do texto(int)
	 */
	public static int getTextInputSize(String text){
		int count = 0;
		char[] ta = text.toCharArray();
		for (int i = 0; i < ta.length; i++) {
			if (Character.isLetterOrDigit(ta[i])){
				count++;
			}
		}
		return count;
	}


	/**
	 * Retorna o tamanho do texto sem carateres de máscara de entrada
	 * @param text - String com máscara de entrada
	 * @param pattern - Padrão da máscara de entrada
	 * @return tamanho do texto(int)
	 */
	public static int getTextInputSize(String text, String pattern){
		pattern = pattern.replaceAll("[d,M,y,H,m,s]", "#");
		int count = 0;
		char[] ta = text.toCharArray();
		char[] ma = pattern.toCharArray();
		for (int i = 0; i < ta.length; i++) {
			if (ma[i] == DIGIT || ma[i] == LETTER || ma[i] == UC || ma[i] == LC || ma[i] == ANY){
				count++;
			}
		}
		return count;
	}

	/**
	 * Retorna o número de caracteres utilizáveis de uma máscara de entrada
	 * @param pattern - Padrão da máscara de entrada
	 * @return número de caracteres(int)
	 */
	public static int getMaskInputSize(String pattern){
		pattern = pattern.replaceAll("[d,M,y,H,m,s]", "#");
		int count = 0;
		char[] ta = pattern.toCharArray();
		for (int i = 0; i < ta.length; i++) {
			if (ta[i] == DIGIT || ta[i] == LETTER || ta[i] == UC || ta[i] == LC || ta[i] == ANY){
				count++;
			}
		}
		return count;
	}

}
