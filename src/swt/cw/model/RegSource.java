package swt.cw.model;

import java.time.LocalDate;
import java.util.List;

public interface RegSource {

	public List<?> getList(int index, LocalDate di, LocalDate df, String text);
	
}
