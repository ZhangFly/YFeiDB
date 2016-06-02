package zfly;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.sun.istack.internal.NotNull;

class ReflectTableImpl1 extends ReflectTableStrategy {

	@Override
	public void excute(@NotNull Class<?> clazz) {

		reflectTableName(clazz);

		for (Field f : clazz.getDeclaredFields()) {
			YFeiColumn additionInfo = null;
			if (f.isAnnotationPresent(YFeiColumn.class)) {
				additionInfo = f.getAnnotation(YFeiColumn.class);
				if (additionInfo.ignore()) {
					continue;
				}
			}
			reflectPrimaryKey(additionInfo, f, clazz);
			reflectColumnName(additionInfo, f, clazz);
		}
	}

	private void reflectTableName(@NotNull final Class<?> clazz) {
		String tableName = StringUtils.EMPTY;
		if (clazz.isAnnotationPresent(YFeiTable.class)) {
			tableName = clazz.getAnnotation(YFeiTable.class).value();
		} else {
			tableName = clazz.getSimpleName();
		}
		notifyGotTableName(tableName, clazz);
	}

	private void reflectColumnName(final YFeiColumn additionInfo, final Field f, final Class<?> clazz) {
		String columnName = StringUtils.EMPTY;
		if (additionInfo == null) {
			columnName = f.getName();
			notifyGotColumnName(columnName, f, clazz);
			return;
		}
		if (!StringUtils.equals(additionInfo.alias(), "")) {
			columnName = additionInfo.alias();
		} else {
			columnName = f.getName();
		}
		notifyGotColumnName(columnName, f, clazz);
	}

	private void reflectPrimaryKey(final YFeiColumn additionInfo, final Field f, final Class<?> clazz) {
		if (additionInfo == null) {
			return;
		}
		if (!additionInfo.primaryKey()) {
			return;
		}
		String primaryKey = StringUtils.EMPTY;
		if (!StringUtils.equals(additionInfo.alias(), "")) {
			primaryKey = additionInfo.alias();
		}
		primaryKey = f.getName();
		notifyGotPrimaryKey(primaryKey, f, clazz);
	}
}
