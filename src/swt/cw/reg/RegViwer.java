package swt.cw.reg;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import swt.cw.model.RegSource;
import swt.cw.model.SaveListener;
import swt.cw.table.DataViwer;

public class RegViwer extends Composite {

	private Text text;
	private DateTime dtIni;
	private DateTime dtFim;
	private ToolItem btnFind;
	private Label lblError;
	private ToolBar toolBar;
	private Menu popUp = null;
	private Label lblPerodo;
	private Label lblAt;
	private Label lblBusca;

	private RegSource source;
	private SaveListener insertListener;
	private SaveListener updateListener;
	private SaveListener deleteListener;
	private int index = 0;
	private DataViwer table;
	private boolean filter = false;
	private Composite composite_1;
	private Button btnIns;
	private Button btnEdt;
	private Button btnDel;
	private int eCount = 0;

	private Rectangle[] btnBounds = {
			new Rectangle(0, 0, 40, 32),
			new Rectangle(40, 0, 40, 32),
			new Rectangle(80, 0, 40, 32)
	};
	private ToolBar toolBarClose;
	private ToolItem tbtClose;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RegViwer(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(10, false);
		gridLayout.horizontalSpacing = 1;
		setLayout(gridLayout);
		composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		btnIns = new Button(composite_1, SWT.NONE);
		btnIns.setVisible(true);
		btnIns.setToolTipText("Inserir");
		btnIns.setImage(SWTResourceManager.getImage(RegViwer.class, "/icon/insert.png"));
		btnIns.setBounds(btnBounds[0]);
		btnEdt = new Button(composite_1, SWT.NONE);
		btnEdt.setVisible(true);
		btnEdt.setToolTipText("Editar");
		btnEdt.setImage(SWTResourceManager.getImage(RegViwer.class, "/icon/edit.png"));
		btnEdt.setBounds(btnBounds[1]);
		btnDel = new Button(composite_1, SWT.NONE);
		btnDel.setVisible(true);
		btnDel.setToolTipText("Excluir");
		btnDel.setImage(SWTResourceManager.getImage(RegViwer.class, "/icon/delete.png"));
		btnDel.setBounds(btnBounds[2]);
		lblPerodo = new Label(this, SWT.NONE);
		lblPerodo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPerodo.setText(" Período: ");
		dtIni = new DateTime(this, SWT.BORDER | SWT.SHORT);
		dtIni.setDay(1);
		GridData gd_dtIni = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_dtIni.widthHint = 100;
		dtIni.setLayoutData(gd_dtIni);
		lblAt = new Label(this, SWT.NONE);
		lblAt.setText(" até ");
		dtFim = new DateTime(this, SWT.BORDER | SWT.SHORT);
		GridData gd_dtFim = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dtFim.widthHint = 130;
		dtFim.setLayoutData(gd_dtFim);
		lblBusca = new Label(this, SWT.NONE);
		lblBusca.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBusca.setText(" Busca:");
		text = new Text(this, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 150;
		text.setLayoutData(gd_text);
		toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		btnFind = new ToolItem(toolBar, SWT.DROP_DOWN);
		btnFind.setImage(SWTResourceManager.getImage(RegViwer.class, "/icon/find.png"));
		table = new DataViwer(this, SWT.BORDER);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 10, 1));
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 10, 1));
		lblError = new Label(composite, SWT.NONE);
		lblError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblError.setVisible(false);
		lblError.setText("Nenhum registro encontrado!");
		toolBarClose = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBarClose.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		tbtClose = new ToolItem(toolBarClose, SWT.NONE);
		tbtClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Composite parent = getParent();
				dispose();
				parent.dispose();
				
			}
		});
		tbtClose.setText("x");
		popUp = new Menu(getShell(), SWT.POP_UP);
	}
	
	public void setCloseVisible(boolean visible){
		toolBarClose.setVisible(visible);
	}

	public void open(){
		lblPerodo.setVisible(filter);
		lblAt.setVisible(filter);
		dtIni.setVisible(filter);
		dtFim.setVisible(filter);
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
			popUp.getItem(index).setSelection(true);
		if (source == null || popUp.getItemCount() == 0){
			lblBusca.setVisible(false);
			text.setVisible(false);
			toolBar.setVisible(false);
		}
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
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				update();
			}
		});
		table.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				btnEdt.setEnabled(table.checkSelection(new Point(arg0.x, arg0.y)));
				btnDel.setEnabled(table.checkSelection(new Point(arg0.x, arg0.y)));
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR)
					find();
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
				actInsert();
			}
		});
		btnEdt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				actUpdate();
			}
		});
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				if (table.getSelectionIndex() >= 0)
					actUpdate();
			}
		});
		btnDel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				actDelete();
			}
		});
		if (insertListener != null){
			btnIns.setBounds(btnBounds[eCount++]);
			btnIns.setVisible(true);
		}else{
			btnIns.setVisible(false);
		}
		if (updateListener != null){
			btnEdt.setBounds(btnBounds[eCount++]);
			btnEdt.setVisible(true);
		}else{
			btnEdt.setVisible(false);
		}
		if (deleteListener != null){
			btnDel.setBounds(btnBounds[eCount++]);
			btnDel.setVisible(true);
		}else{
			btnDel.setVisible(false);
		}
		btnEdt.setEnabled(false);
		btnDel.setEnabled(false);
		table.refresh();
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

	public void setRegSource(RegSource source){
		this.source = source;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public void setData(List<?> data){
		table.setData(data);
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
		if (source == null)
			return;
		List<?> data = new ArrayList<>();
		if (filter){
			LocalDate di = LocalDate.of(dtIni.getYear(), dtIni.getMonth()+1, dtIni.getDay());
			LocalDate df = LocalDate.of(dtFim.getYear(), dtFim.getMonth()+1, dtFim.getDay());
			data = source.getList(index, di, df, text.getText());
		}else{
			data = source.getList(index, null, null,text.getText());
		}
		table.setData(data);
		lblError.setVisible(data.size() == 0);
	}

	private void actInsert(){
		if (insertListener == null)
			return;
		Object object = insertListener.handleEvent(null);
		if (object != null)
			table.add(object);
	}

	private void actUpdate(){
		if (updateListener == null)
			return;
		int index = table.getSelectionIndex();
		if (index < 0)
			return;
		Object object = table.getSelection();
		object = updateListener.handleEvent(object);
		if (object != null)
			table.set(index, object);
	}
	
	public Object getSelection(){
		return table.getSelection();
	}

	private void actDelete(){
		if (deleteListener == null)
			return;
		int index = table.getSelectionIndex();
		if (index < 0)
			return;
		Object object = table.getSelection();
		boolean delete = (boolean)deleteListener.handleEvent(object);
		if (delete)
			table.remove(index);
	}

	public void setAlignment(int columnIndex, int aligment){
		table.setAlignment(columnIndex, aligment);
	}

	public void setWidth(int columnIndex, int width){
		table.setWidth(columnIndex, width);
	}

	public void setPattern(int columnIndex, String pattern){
		table.setPattern(columnIndex, pattern);
	}

	public void setInsertListener(SaveListener listener){
		insertListener = listener;
	}

	public void setUpdateListener(SaveListener listener){
		updateListener = listener;
	}

	public void setDeleteListener(SaveListener listener){
		deleteListener = listener;
	}

	public void setFindIndex(int index){
		this.index = index;
	}

	public void setIcons(Image[] icons){
		if (icons.length != 4)
			throw new RuntimeException();
		btnIns.setImage(icons[0]);
		btnEdt.setImage(icons[1]);
		btnDel.setImage(icons[2]);
		btnFind.setImage(icons[3]);
	}
	
}
