package swt.cw;



import java.security.acl.Group;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

import swt.cw.util.Format;
import swt.cw.util.InputMask;
import swt.cw.util.Validate;

public class Customizer {


	private static boolean format = true;

	/**
	 * Modifica um TextField para aceitar somente números
	 * @param textField - Campo de texto a ser customizado
	 * @param pattern - Padrão do campo de texto ("0,00") Obs: Não aceita separador decimal nem caracteres monetários
	 * @param min - valor mínimo aceito pelo campo
	 * @param max - valor máximo aceito pelo campo
	 */
	public static void setNumeric(Text textField, String pattern, Number min, Number max){
		String oldText = textField.getText();
		textField.setText("");
		format = true;
		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				if (format){
					format = false;
					textField.setText(Format.toStringNumber(textField.getText()));
					textField.setSelection(textField.getText().length());
					format = true;
				}
			}
		};
		FocusListener focusListener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				Number n = Format.toNumber(textField.getText(), pattern);
                if (n == null)
                    return;
                if (n.doubleValue() < min.doubleValue())
                    n = min.doubleValue();
                else if (n.doubleValue() > max.doubleValue())
                    n = max.doubleValue();
                textField.setText(Format.toString(n, pattern));
			}
		};
		addListeners(textField, modifyListener, focusListener, pattern);
		textField.setText(oldText);
	}

	/**
	 * Modifica um TextField para aceitar somente números
	 * @param textField - Campo de texto a ser customizado
	 * @param digits - número de digitos após a vírgula
	 */
	public static void setNumeric(Text textField, int digits){
		setNumeric(textField, getPattern(digits), Double.MIN_VALUE, Double.MAX_VALUE);
	}

	/**
	 * Modifica um TextField para aceitar somente números
	 * @param textField - Campo de texto a ser customizado
	 * @param digits - número de digitos após a vírgula
	 * @param min - valor mínimo aceito pelo campo
	 * @param max - valor máximo aceito pelo campo
	 */
	public static void setNumeric(Text textField, int digits, Number min, Number max){
		setNumeric(textField, getPattern(digits), min, max);
	}

	/**
	 * Adiciona uma mascara de entrada em um campo de texto
	 * @param textField - campo de texto a ser customizado
	 * @param pattern - padrão da máscara de entrada (Obs: # - Número, ? - Letra, L - Letra minúscula, U - Letra maiúsucla, A - Letra ou número)
	 */
	public static void setInputMask(Text textField, String pattern){
		String oldText = textField.getText();
		textField.setText("");
		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				if (format){
					format = false;
					textField.setText(InputMask.maskString(textField.getText(),pattern));
					textField.setSelection(textField.getText().length());
					format = true;
				}

			}
		};
		addListeners(textField, modifyListener, null, pattern);
		textField.setText(oldText);
	}
	
	public static void setTemporal(Text textField, String pattern){
		String oldText = textField.getText();
		textField.setText("");
		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				if (format){
					format = false;
					textField.setText(InputMask.maskString(textField.getText(),pattern));
					textField.setSelection(textField.getText().length());
					format = true;
				}

			}
		};
		FocusListener focusListener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (!textField.getText().isEmpty()){
					if (Validate.strDateTime(textField.getText(), pattern))
						textField.setText(Format.toStringDate(textField.getText(), pattern));
					else
						textField.setText("");
				}
			}
		};
		addListeners(textField, modifyListener, focusListener, pattern);
		textField.setText(oldText);
	}

	private static void addListeners(Text textField, ModifyListener modifyListener, FocusListener focusListener, String pattern){
		if (textField.getData() != null)
			removeAll(textField);
		if (modifyListener != null)
			textField.addModifyListener(modifyListener);
		if (focusListener != null)
			textField.addFocusListener(focusListener);
		CListener cListener = new CListener();
		cListener.modifyListener = modifyListener;
		cListener.focusListener = focusListener;
		cListener.pattern = pattern;
		textField.setData(cListener);
	}

	/**
	 * Remove a customização do campo de texto
	 * @param textField - campo de texto a ser revertido
	 */
	public static void removeAll(Text textField){
		String oldText = textField.getText();
		textField.setText("");
		Object object = textField.getData();
		if (object instanceof CListener) {
			CListener cListener = (CListener) object;
			if (cListener.modifyListener != null)
				textField.removeModifyListener(cListener.modifyListener);
			if (cListener.focusListener != null)
				textField.removeFocusListener(cListener.focusListener);
		}
		textField.setData(null);
		textField.setText(oldText);
	}

	private static String getPattern(int digits){
		StringBuilder builder = new StringBuilder();
		builder.append('0');
		for (int i = 0; i < digits; i++) {
			if (i == 0)
				builder.append('.');
			builder.append('0');
		}
		return builder.toString();
	}
	
	private static boolean isComposite(Control control){
		Class<?>[] composites = {Shell.class, Composite.class, Group.class, SashForm.class,
				ScrolledComposite.class, TabFolder.class, CTabFolder.class, ViewForm.class, CBanner.class};
		for (Class<?> c : composites) {
			if (control.getClass().equals(c))
				return true;
		}
		return false;
	}
	/**
	 * Adiciona um listener quando qualquer campo for alterado em um formulário
	 * @param listener
	 */
	public static void addModifyListener(Composite composite, Listener listener){
		Control[] controls = composite.getChildren();
		for (Control control : controls) {
			if (isComposite(control)){
				Composite composite2 = (Composite)control;
				addModifyListener(composite2, listener);
			}else{
				control.addListener(SWT.Selection, listener);
				control.addListener(SWT.Modify, listener);
			}
		}
	}
	
}
