package swt.cw.dialog;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.model.FindSource;
import swt.cw.model.SaveListener;
import swt.cw.table.DataViwer;
import swt.cw.util.Screen;

public class FindDialog extends Dialog {

	private Object result;
	private Shell shell;
	private Text text;
	private ToolItem btnFind;
	private DataViwer table;
	private Label lblErro;
	private Button btnIns;
	private Button btnSel;
	private Button btnCancel;
	private ToolBar toolBar;
	private Menu popUp = null;
	
	private FindSource source;
	private SaveListener saveListener;
	private int index;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public FindDialog(Shell parent) {
		super(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		createContents();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		initialize();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(518, 317);
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.horizontalSpacing = 0;
		shell.setLayout(gl_shell);
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		Label lblBusca = new Label(composite, SWT.NONE);
		lblBusca.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBusca.setText("Busca:");
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		btnFind = new ToolItem(toolBar, SWT.DROP_DOWN);
		btnFind.setImage(SWTResourceManager.getImage(FindDialog.class, "/icon/find.png"));
		table = new DataViwer(shell, SWT.BORDER);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Composite composite_1 = new Composite(shell, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(4, false);
		gl_composite_1.horizontalSpacing = 1;
		composite_1.setLayout(gl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblErro = new Label(composite_1, SWT.NONE);
		lblErro.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblErro.setText("Nenhum registro encontrado!");
		btnIns = new Button(composite_1, SWT.NONE);
		btnIns.setImage(SWTResourceManager.getImage(FindDialog.class, "/icon/insert.png"));
		btnIns.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnIns.setToolTipText("Inserir");
		btnSel = new Button(composite_1, SWT.NONE);
		btnSel.setImage(SWTResourceManager.getImage(FindDialog.class, "/icon/accept.png"));
		btnSel.setToolTipText("Selecionar");
		btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.setImage(SWTResourceManager.getImage(FindDialog.class, "/icon/cancel.png"));
		btnCancel.setToolTipText("Cancelar");
		lblErro.setVisible(false);
		btnIns.setVisible(false);
		popUp = new Menu(shell,SWT.POP_UP);
		Screen.centralize(shell, getParent());
	}
	
	private void initialize(){
		btnFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (arg0.detail != SWT.ARROW){
					find();
				}else{
					Rectangle rect = btnFind.getBounds ();
					Point pt = new Point (rect.x, rect.y + rect.height);
					pt = toolBar.toDisplay (pt);
					popUp.setLocation (pt.x, pt.y);
					popUp.setVisible (true);
				}
			}
		});
		if (popUp.getItemCount() > 0)
			popUp.getItem(0).setSelection(true);
		for (MenuItem item : popUp.getItems()) {
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					int newIndex = getIndex();
					if (newIndex != index){
						index = newIndex;
						find();
					}
				}
			});
		}
		btnSel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				select();
			}
		});
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				select();
			}
		});
		table.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				btnSel.setEnabled(table.checkSelection(new Point(arg0.x, arg0.y)));
			}
		});
		table.addListener(SWT.Selection,  new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				btnSel.setEnabled(table.getSelectionIndex() >= 0);
			}
		});
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR)
					find();
				else if (arg0.keyCode == SWT.ARROW_DOWN && !table.isEmpty())
					table.setFocus();
			}
		});
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (text.getText().isEmpty())
					find();
			}
		});
		btnIns.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (saveListener != null){
					Object object = saveListener.handleEvent(null);
					if (object != null){
						result = object;
						shell.close();
					}
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR)
					select();
			}
		});
		btnSel.setEnabled(false);
		if (saveListener != null)
			btnIns.setEnabled(true);
	}
	
	@Override
	public void setText(String text){
		shell.setText(text);
	}
	
	public void addColumn(String fieldName, String displayName, boolean findable){
		table.addColumn(fieldName, displayName);
		if (findable){
			MenuItem item = new MenuItem(popUp,SWT.RADIO);
			item.setText(displayName);
		}
	}
	
	public void addColumn(String fieldName, String displayName, String pattern, boolean findable){
		table.addColumn(fieldName, displayName, pattern);
		if (findable){
			MenuItem item = new MenuItem(popUp,SWT.RADIO);
			item.setText(displayName);
		}
	}
	
	public void setFindSource(FindSource source){
		this.source = source;
	}
	
	public void setInsertListener(SaveListener listener){
		this.saveListener = listener;
		if (listener != null)
			btnIns.setVisible(true);
	}
	
	private int getIndex(){
		MenuItem[] items = popUp.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getSelection())
				return i;
		}
		return -1;
	}
	
	public void find(){
		List<?> data = source.getList(index, text.getText()); 
		table.setData(data);
		lblErro.setVisible(data.size() == 0);
		
	}
	
	public void select(){
		Object object = table.getSelection();
		if (object == null)
			return;
		result = object;
		shell.close();
	}
	
	public void refresh(){
		table.refresh();
	}
	
	public void setErrorMessage(String message){
		lblErro.setText(message);
	}
	public void setFindIcon(Image image){
		btnFind.setImage(image);
	}
	public void setInsertIcon(Image image){
		btnIns.setImage(image);
	}
	public void setSelectIcon(Image image){
		btnSel.setImage(image);
	}
	public void setCancelIcon(Image image){
		btnCancel.setImage(image);
	}
	public void setIcons(Image[] icons){
		if (icons.length != 4)
			throw new RuntimeException("Numero de ícones != 4");
		btnFind.setImage(icons[0]);
		btnIns.setImage(icons[1]);
		btnSel.setImage(icons[2]);
		btnCancel.setImage(icons[3]);
	}
	public void setImage(Image image){
		shell.setImage(image);
	}
	
	public void setFindText(String text){
		this.text.setText(text);
	}
	
}
