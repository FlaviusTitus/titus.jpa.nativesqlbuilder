package titus.sql.query.builder.impl.functions;

import titus.sql.query.builder.IColumn;
import titus.sql.query.builder.ISqlFunction;

/**
 * The Class BasicSqlFunction.
 */
public class BasicSqlFunction implements ISqlFunction {

	/** The column. */
	private final IColumn column;

	/** The function name. */
	private final String functionName;

	/**
	 * Instantiates a new count.
	 *
	 * @param aFunctionName the a function name
	 * @param anColumn      the an column
	 */
	public BasicSqlFunction(final String aFunctionName, final IColumn anColumn) {
		this.functionName = aFunctionName;
		this.column = anColumn;
	}

	/**
	 * Builds the native sql.
	 *
	 * @param anContext the an context
	 * @return the string
	 */
	@Override
	public String buildNativeSql(final INativeSqlBuildContext anContext) {
		return String.format("%s( %s )", this.functionName, this.column.buildNativeSql(anContext));
	}
}
