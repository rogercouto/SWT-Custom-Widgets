package swt.cw.util;

import java.lang.reflect.Field;

public class DevUtil {

	public static void printFields(Object object){
		if (object == null){
			System.out.println(object);
			return;
		}
		Field[] fields = object.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				if (i > 0)
					System.out.print(" | ");
				fields[i].setAccessible(true);
				System.out.print(fields[i].getName()+" = "+fields[i].get(object));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}
	
}
