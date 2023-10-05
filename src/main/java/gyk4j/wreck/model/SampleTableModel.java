package gyk4j.wreck.model;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(SampleTableModel.class);
	
	private ArrayList<Field> columns = new ArrayList<Field>();
	private List<T> data = new ArrayList<>();
	
	private final Class<T> type;
	
	public SampleTableModel(Class<T> type) {
		super();
		this.type = type;
		
		Field[] fields = getType().getDeclaredFields();
		for(int i=0; i < fields.length; i++) {
			if (Modifier.isPrivate(fields[i].getModifiers()) && !Modifier.isStatic(fields[i].getModifiers())) {
				columns.add(fields[i]);
			}
		}
	}
	
	public Class<T> getType() {
        return this.type;
    }

	public String[] getHeader() {		
		return columns.stream().map(f -> f.getName().toUpperCase()).collect(Collectors.toList()).toArray(new String[0]);
	}

	public List<T> getData() {
		return data;
	}

	@Override
	public int getRowCount() {
		return getData().size();
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		T bean = getData().get(rowIndex);
		Field column = columns.get(columnIndex);
		Object value = null;
		try {
			column.setAccessible(true);
			value = column.get(bean);
		} catch (IllegalArgumentException e) {
			LOG.error(e.toString());
		} catch (IllegalAccessException e) {
			LOG.error(e.toString());
		} catch (SecurityException e) {
			LOG.error(e.toString());
		}
		return value;
	}

	@Override
	public String getColumnName(int column) {
		String n = columns.get(column).getName();
		return n.substring(0, 1).toUpperCase().concat(n.substring(1));
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?> clazz = null;
		Field field;
		try {
			field = columns.get(columnIndex);
			
			if((field != null))
				clazz = field.getType();
			
			if(clazz.isPrimitive()) {
				clazz =  MethodType.methodType(clazz).wrap().returnType();
			}
//			LOG.trace("Column class: {} -> {}", columnIndex, clazz.getName());
		} catch (SecurityException e) {
			LOG.error(e.toString());
		}
		
		return clazz;
	}
	
	public void addRow(T row) {
		data.add(row);
		this.fireTableRowsInserted(data.size()-1, data.size()-1);
	}
	
	public void removeRow(int row) {
		data.remove(row);
		this.fireTableRowsDeleted(row, row);
	}
	
	public void clear() {
		if(!data.isEmpty()) {
			data.clear();
			this.fireTableRowsDeleted(0, this.getRowCount()-1);
		}
	}
}
