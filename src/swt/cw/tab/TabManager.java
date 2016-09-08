package swt.cw.tab;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class TabManager extends Composite {

	private CTabFolder tabFolder;
	private List<Tab> aTabs = new ArrayList<>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TabManager(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	}
	
	public void add(Composite composite, String text){
		aTabs.add(new Tab(composite, text));
	}
	
	public void set(int index, Composite composite, String text){
		aTabs.set(index, new Tab(composite, text));
	}
	
	public void remove(int index){
		aTabs.remove(index);
	}
	
	public void open(int index){
		Tab tab = aTabs.get(index);
		if (tab.index < 0){
			CTabItem item = new CTabItem(tabFolder, SWT.CLOSE);
			item.setText(tab.text);
			tab.index = tabFolder.getItemCount()-1;
			item.setControl(tab.composite);
			item.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent arg0) {
					for (int i = index+1; i < aTabs.size(); i++) {
						if (aTabs.get(i).index > 0)
							aTabs.get(i).index--;
					}
					tab.index = -1;
				}
			});
			tabFolder.setSelection(item);
		}else{
			CTabItem item = tabFolder.getItem(tab.index);
			tabFolder.setSelection(item);
		}
		
	}
	
	public CTabFolder getCTabFolder(){
		return tabFolder;
	}
	
	class Tab{
		int index = -1;
		Composite composite;
		String text;
		public Tab(Composite composite, String text){
			super();
			this.composite = composite;
			this.text = text;
		}
	}
}
