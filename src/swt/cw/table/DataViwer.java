package swt.cw.table;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class DataViwer extends Composite {

	public static final int DEF_WIDTH = 100;
	public static final int DEF_ALIGN = SWT.LEFT;

	private Table table;
	private List<Column> columns = new ArrayList<>();
	private List<Object> data = new ArrayList<>();

	private String emptyValue = "-";
	private boolean sortEnabled = true;
	private boolean deselectColumns = true;
	private Listener sortListener;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DataViwer(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		table = new Table(this, style | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createListeners();
	}

	private void createListeners(){
		sortListener = new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				if (!sortEnabled)
					return;
				TableColumn sortColumn = table.getSortColumn();
		        TableColumn currentColumn = (TableColumn) arg0.widget;
		        int dir = table.getSortDirection();
		        if (sortColumn == currentColumn) {
		        	dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
		        }else{
		        	table.setSortColumn(currentColumn);
		        	dir = SWT.DOWN;
		        }
		        final int direction = dir;
		        int index = -1;
		        for (int i = 0; i < table.getColumns().length; i++) {
					if (currentColumn.equals(table.getColumn(i))){
						index = i;
						break;
					}
				}
		        final Column sc = columns.get(index);
		        int isel = table.getSelectionIndex();
		        Object osel = null;
		        if (isel >= 0)
		        	osel = data.get(isel);
		        Comparator<Object> comparator = new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						Object fo1 = getData(o1, sc.fieldName);
						Object fo2 = getData(o2, sc.fieldName);
						if (fo1 == null && fo2 == null)
							return 0;
						else if (fo1 == null && fo2 != null)
							return direction == SWT.DOWN ? -1 : 1;
						else if (fo1 != null && fo2 == null)
							return direction == SWT.DOWN ? 1 : -1;
						if (fo1.getClass() == Integer.class){
							Integer v1 = (Integer)fo1;
							Integer v2 = (Integer)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == Long.class){
							Long v1 = (Long)fo1;
							Long v2 = (Long)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == Float.class){
							Float v1 = (Float)fo1;
							Float v2 = (Float)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == Double.class){
							Double v1 = (Double)fo1;
							Double v2 = (Double)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == Character.class){
							Character v1 = (Character)fo1;
							Character v2 = (Character)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == Boolean.class){
							Boolean v1 = (Boolean)fo1;
							Boolean v2 = (Boolean)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == Date.class){
							Date v1 = (Date)fo1;
							Date v2 = (Date)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == LocalDate.class){
							LocalDate v1 = (LocalDate)fo1;
							LocalDate v2 = (LocalDate)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == LocalTime.class){
							LocalTime v1 = (LocalTime)fo1;
							LocalTime v2 = (LocalTime)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == LocalDateTime.class){
							LocalDateTime v1 = (LocalDateTime)fo1;
							LocalDateTime v2 = (LocalDateTime)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else if (fo1.getClass() == String.class){
							String v1 = (String)fo1;
							String v2 = (String)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}else{
							String v1 = (String)fo1;
							String v2 = (String)fo2;
							return direction == SWT.DOWN ? v1.compareTo(v2) : v2.compareTo(v1);
						}
					}
		        };
		        Collections.sort(data, comparator);
		        refresh();
		        table.setSortDirection(dir);
		        if (osel != null){
		        	for (int i = 0; i < data.size(); i++) {
		        		if (osel.equals(data.get(i))){
		        			table.setSelection(i);
		        			break;
		        		}
		        	}
		        }
			}
		};
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (!deselectColumns)
					return;
				TableItem item = table.getItem(new Point(arg0.x, arg0.y));
				if (item == null)
					table.deselectAll();
			}
		});
	}

	@Override
	protected void checkSubclass() {}

	public void setData(List<?> data){
		this.data = new ArrayList<>();
		for (Object object : data) {
			this.data.add(object);
		}
		refresh();
	}

	public void addColumn(String fieldName, String displayName){
		addColumn(fieldName, displayName, null);
	}

	public void add(Object object){
		data.add(object);
		refresh();
	}

	public void set(int index, Object object){
		data.set(index, object);
		refresh();
	}

	public void remove(int index){
		data.remove(index);
		refresh();
	}

	public Object get(int index){
		return data.get(index);
	}

	public Object getSelection(){
		int index = table.getSelectionIndex();
		if (index < 0)
			return null;
		return data.get(index);
	}

	public int getSelectionIndex(){
		return table.getSelectionIndex();
	}

	public boolean checkSelection(Point p){
		return table.getItem(p) != null;
	}

	public void addColumn(String fieldName, String displayName, String pattern){
		if (fieldName == null || fieldName.isEmpty())
			throw new RuntimeException("Invalid field name!");
		if (displayName == null || displayName.isEmpty())
			throw new RuntimeException("Invalid display name!");
		if (fieldName.contains(".")){
			String[] fns = fieldName.split("[.]");
			if (fns.length < 2)
				throw new RuntimeException("Invalid field name!");
		}
		if (pattern != null)
			columns.add(new Column(fieldName, displayName, pattern));
		else
			columns.add(new Column(fieldName, displayName));
		TableColumn tColumn = new TableColumn(table, SWT.NONE);
		tColumn.setText(displayName);
		tColumn.setAlignment(DEF_ALIGN);
		tColumn.setWidth(DEF_WIDTH);
		tColumn.addListener(SWT.Selection, sortListener);
		refresh();
	}

	public void setAlignment(int columnIndex, int aligment){
		table.getColumn(columnIndex).setAlignment(aligment);
	}

	public void setWidth(int columnIndex, int width){
		table.getColumn(columnIndex).setWidth(width);
	}

	public void setPattern(int columnIndex, String pattern){
		columns.get(columnIndex).pattern = pattern;
	}

	public void refresh(){
		if (data == null)
			return;
		if (columns.size() == 0)
			return;
		table.removeAll();
		for (int i = 0; i < data.size(); i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			for(int j = 0; j < columns.size(); j++){
				item.setText(j, format(getData(data.get(i), columns.get(j).fieldName),columns.get(j).pattern));
			}
		}
	}

	private Object getData(Object object, String fieldName){
		if (object == null)
			return null;
		if (!fieldName.contains(".")){
			Field field = getField(fieldName, object.getClass());
			if (field == null)
				throw new RuntimeException("Invalid field name("+fieldName+")!");
			Object data = get(field, object);
			return data;
		}else{
			String[] fnp = fieldName.split("[.]");
			if (fnp.length < 2)
				throw new RuntimeException("Invalid field name!");
			Field fk = getField(fnp[0], object.getClass());
			if (fk == null)
				throw new RuntimeException("Invalid field name!");
			Object fkObject = get(fk, object);
			StringBuilder builder = new StringBuilder();
			for (int i = 1; i < fnp.length; i++){
				if (i > 1)
					builder.append('.');
				builder.append(fnp[i]);
			}
			return getData(fkObject, fieldName.substring(fnp[0].length()+1, fieldName.length()));
		}
	}

	public static Field getField(String fieldName, Class<?> c){
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields)
			if (field.getName().compareTo(fieldName) == 0)
				return field;
		if (c.equals(Object.class))
			return null;
		return getField(fieldName, c.getSuperclass());
	}

	public static Object get(Field field, Object object){
		field.setAccessible(true);
		try {
			return field.get(object);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String format(Object value, String pattern){
		if (value == null)
			return emptyValue;
		if (value.getClass().equals(Double.class)||value.getClass().equals(Double.TYPE)||
				value.getClass().equals(Float.class)||value.getClass().equals(Float.TYPE)){
			if (pattern == null)
				return new DecimalFormat("0.00").format(value);
			else
				return new DecimalFormat(pattern).format(value);
		}else if (value.getClass() == Date.class){
			Date ld = (Date)value;
			if (pattern == null)
				return new SimpleDateFormat("dd/MM/yyyy").format(ld);
			else
				return new SimpleDateFormat(pattern).format(ld);
		}else if (value.getClass() == LocalDate.class){
			LocalDate ld = (LocalDate)value;
			if (pattern == null)
				return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(ld);
			else
				return DateTimeFormatter.ofPattern(pattern).format(ld);
		}else if (value.getClass() == LocalTime.class){
			LocalTime lt = (LocalTime)value;
			if (pattern == null)
				return DateTimeFormatter.ofPattern("HH:mm").format(lt);
			else
				return DateTimeFormatter.ofPattern(pattern).format(lt);
		}else if (value.getClass() == LocalDateTime.class){
			LocalDateTime ldt = (LocalDateTime)value;
			if (pattern == null)
				return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(ldt);
			else
				return DateTimeFormatter.ofPattern(pattern).format(ldt);
		}else if (value.getClass() == Boolean.class || value.getClass() == Boolean.TYPE){
			Boolean b = (Boolean) value;
			if (pattern == null)
				return b ? "Sim" : "N\u00e3o";
			else{
				String[] sp = pattern.split(":");
				if (sp.length != 2)
					throw new RuntimeException("Invalid pattern!");
				return b ? sp[0] : sp[1];
			}
		}
		return value.toString();
	}

	public void addListener(int eventType, Listener listener){
		table.addListener(eventType, listener);
	}

	public void removeListener(int eventType, Listener listener){
		table.removeListener(eventType, listener);
	}

	public int getCheckedCount(){
		int count = 0;
		TableItem[] items = table.getItems();
		for (TableItem item : items) {
			if (item.getChecked())
				count++;
		}
		return count;
	}
	
	public List<?> getCheckedItems(){
		TableItem[] items = table.getItems();
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getChecked())
				list.add(data.get(i));
		}
		return list;
	}
	
	public boolean isEmpty(){
		return data == null || data.size() == 0;
	}
	
	public int getItemCount(){
		if (data != null)
			return data.size();
		return 0;
	}
	
	class Column{
		String fieldName;
		String displayName;
		String pattern = null;
		public Column(String fieldName, String displayName){
			super();
			this.fieldName = fieldName;
			this.displayName = displayName;
		}
		public Column(String fieldName, String displayName, String pattern){
			super();
			this.fieldName = fieldName;
			this.displayName = displayName;
			this.pattern = pattern;
		}
	}
}