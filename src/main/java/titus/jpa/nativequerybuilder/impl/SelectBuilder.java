package titus.jpa.nativequerybuilder.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import titus.jpa.nativequerybuilder.IBuilder;
import titus.jpa.nativequerybuilder.IColumn;
import titus.jpa.nativequerybuilder.ICondition;
import titus.jpa.nativequerybuilder.IPage;
import titus.jpa.nativequerybuilder.ISelect;
import titus.jpa.nativequerybuilder.ISelectBuilder;
import titus.jpa.nativequerybuilder.ISelectFrom;
import titus.jpa.nativequerybuilder.ISelectable;
import titus.jpa.nativequerybuilder.ISubSelect;
import titus.jpa.nativequerybuilder.ITable;
import titus.jpa.nativequerybuilder.INativeSql.INativeSqlBuildContext;
import titus.jpa.nativequerybuilder.ISelectBuilder.ISelectFromBuilder;
import titus.jpa.nativequerybuilder.ISelectBuilder.ISelectGroupByBuilder;
import titus.jpa.nativequerybuilder.ISelectBuilder.ISelectOrderByBuilder;
import titus.jpa.nativequerybuilder.ISelectBuilder.ISelectPageBuilder;
import titus.jpa.nativequerybuilder.ISelectBuilder.ISelectWhereBuilder;

/**
 * The Class SelectBuilder.
 */
public class SelectBuilder implements ISelectBuilder, ISelectFromBuilder, ISelectWhereBuilder, ISelectGroupByBuilder, ISelectOrderByBuilder, ISelectPageBuilder {

	/**
	 * Builder.
	 *
	 * @return the i select builder
	 */
	public static ISelectBuilder builder() {
		return new SelectBuilder();
	}

	/** The select all columns. */
	private boolean selectAllColumns;

	/** The select all columns from table. */
	private ITable selectAllColumnsFromTable;

	/** The columns. */
	private List<ISelectable> columns;

	/** The table. */
	private ISelectFrom table;

	/** The distinct. */
	private boolean distinct;

	/** The where. */
	private ICondition where;

	/** The page. */
	private IPage page;

	/** The closed. */
	private boolean closed = false;

	/**
	 * Instantiates a new select builder.
	 */
	private SelectBuilder() {
		this.reset();
	}

	/**
	 * Check.
	 */
	private void check() {
		if (this.closed)
			throw new RuntimeException("Query is builded. User \"reset\" function or create new ISelectBuilder instance!");
	}

	/**
	 * Reset.
	 */
	public void reset() {
		this.closed = false;
		this.selectAllColumns = false;
		this.selectAllColumnsFromTable = null;
		this.columns = new ArrayList<>();
		this.table = null;
		this.page = null;
		this.where = null;
	}

	/**
	 * Distinct.
	 *
	 * @return the i select builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder#distinct()
	 */
	@Override
	public ISelectBuilder distinct() {
		this.distinct = true;
		return this;
	}

	/**
	 * Columns.
	 *
	 * @param aColumns
	 *            the a columns
	 * @return the i select from builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder#columns(intranet.quarkus.utils.jpa.nativequerybuilder.ISelectColumn[])
	 */
	@Override
	public ISelectFromBuilder columns(final ISelectable... aColumns) {
		this.check();
		if (!this.selectAllColumns && aColumns != null)
			this.columns.addAll(Arrays.asList(aColumns));
		return this;
	}

	/**
	 * Columns.
	 *
	 * @param aColumns
	 *            the a columns
	 * @return the i select from builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder#columns(java.lang.String[])
	 */
	@Override
	public ISelectFromBuilder columns(final String... aColumns) {
		this.check();
		if (!this.selectAllColumns && aColumns != null)
			this.columns.addAll(Arrays
					.stream(aColumns)
					.map(column -> IColumn
							.builder()
							.name(column)
							.build()
							.toSelectable()
							.build())
					.toList());
		return this;
	}

	/**
	 * All columns.
	 *
	 * @return the i select from builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder#allColumns()
	 */
	@Override
	public ISelectFromBuilder allColumns() {
		this.check();
		this.selectAllColumns = true;
		this.columns.clear();
		return this;
	}

	/**
	 * All columns from table.
	 *
	 * @param aTable
	 *            the a table
	 * @return the i select from builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder#allColumnsFromTable(titus.jpa.nativequerybuilder.ITable)
	 */
	@Override
	public ISelectFromBuilder allColumnsFromTable(final ITable aTable) {
		this.check();
		this.selectAllColumnsFromTable = aTable;
		this.selectAllColumns = true;
		this.columns.clear();
		return this;
	}

	/**
	 * From.
	 *
	 * @param aTable
	 *            the a table
	 * @return the i select where builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder.ISelectFromBuilder#from(intranet.quarkus.utils.jpa.nativequerybuilder.impl.Table)
	 */
	@Override
	public ISelectWhereBuilder from(final ISelectFrom aTable) {
		this.check();
		this.table = aTable;
		return this;
	}

	/**
	 * From.
	 *
	 * @param aTable
	 *            the a table
	 * @return the i select where builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder.ISelectFromBuilder#from(java.lang.String)
	 */
	@Override
	public ISelectWhereBuilder from(final String aTable) {
		this.check();
		this.table = ITable.builder().name(aTable).build();
		return this;
	}

	/**
	 * Where.
	 *
	 * @param anCondition
	 *            the an condition
	 * @return the i select where builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder.ISelectWhereBuilder#where(titus.jpa.nativequerybuilder.ICondition)
	 */
	@Override
	public ISelectWhereBuilder where(final ICondition anCondition) {
		this.check();
		this.where = anCondition;
		return this;
	}

	/**
	 * Group by.
	 *
	 * @return the i select group by builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder.ISelectWhereBuilder#groupBy()
	 */
	@Override
	public ISelectGroupByBuilder groupBy() {
		this.check();
		return this;
	}

	/**
	 * Order by.
	 *
	 * @return the i select order by builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder.ISelectWhereBuilder#orderBy()
	 */
	@Override
	public ISelectOrderByBuilder orderBy() {
		this.check();
		return this;
	}

	/**
	 * Page.
	 *
	 * @param aPage
	 *            the a page
	 * @param aSize
	 *            the a size
	 * @return the i builder
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder.ISelectPageBuilder#page(int,
	 *      int)
	 */
	@Override
	public IBuilder<ISelect> page(final int aPage, final int aSize) {
		this.page = Page
				.builder()
				.page(aPage)
				.size(aSize)
				.build();
		return this;
	}

	/**
	 * Builds the base query.
	 *
	 * @param anContext
	 *            the an context
	 * @return the string
	 */
	private String buildBaseQuery(final INativeSqlBuildContext anContext) {
		StringBuilder builder = new StringBuilder();

		builder
				.append(SqlContants.KEYWORD__FROM)
				.append(Constants.ONE_BLANK)
				.append(this.table.buildNativeSql(anContext));

		if (this.where != null)
			builder.append(Constants.ONE_BLANK)
					.append(SqlContants.KEYWORD__WHERE)
					.append(Constants.ONE_BLANK)
					.append(this.where.buildNativeSql(anContext));

		return builder.toString();
	}

	/**
	 * Builds the data query.
	 *
	 * @param aBaseQuery
	 *            the a base query
	 * @param anContext
	 *            the an context
	 * @return the string
	 */
	private String buildDataQuery(final String aBaseQuery, final INativeSqlBuildContext anContext) {
		StringBuilder builder = new StringBuilder();

		builder.append(SqlContants.KEYWORD__SELECT)
				.append(Constants.ONE_BLANK);

		if (this.distinct)
			builder.append(SqlContants.KEYWORD__DISTINCT)
					.append(Constants.ONE_BLANK);

		if (this.selectAllColumns) {
			if (this.selectAllColumnsFromTable != null)
				builder.append(this.selectAllColumnsFromTable.getAlias())
						.append(SqlContants.NAMESPACE_DELIMITER);
			builder.append(SqlContants.SELECT_ALL_COLUMNS);
		} else
			builder.append(
					this.columns
							.stream()
							.map(column -> column.buildNativeSql(anContext))
							.collect(Collectors.joining(SqlContants.VALUE_DELIMITER)));

		builder.append(Constants.ONE_BLANK)
				.append(aBaseQuery);

		// TODO Group By
		// TODO Order By
		if (this.page != null)
			builder.append(Constants.ONE_BLANK)
					.append(this.page.buildNativeSql(anContext));

		return builder.toString().toUpperCase();
	}

	/**
	 * Builds the count query.
	 *
	 * @param aBaseQuery
	 *            the a base query
	 * @param anContext
	 *            the an context
	 * @return the string
	 */
	private String buildCountQuery(final String aBaseQuery, final INativeSqlBuildContext anContext) {
		StringBuilder builder = new StringBuilder();

		builder.append(SqlContants.KEYWORD__SELECT)
				.append(Constants.ONE_BLANK);
		if (this.distinct)
			builder.append(SqlContants.COUNT_ROWS_DISTINCT);
		else
			builder.append(SqlContants.COUNT_ROWS);
		builder.append(Constants.ONE_BLANK)
				.append(aBaseQuery);

		return builder.toString().toUpperCase();
	}

	/**
	 * Valid.
	 */
	private void valid() {
		if (!this.selectAllColumns && this.columns.isEmpty())
			throw new RuntimeException("Select all columns or define the columns to be select!");

		if (this.table == null)
			throw new RuntimeException("A table is required!");

	}

	/**
	 * Builds the select.
	 *
	 * @param anContext
	 *            the an context
	 * @return the i select
	 */
	private ISelect buildSelect(final INativeSqlBuildContext anContext) {
		final String querybase = this.buildBaseQuery(anContext);

		return Select
				.builder()
				.countQuery(this.buildCountQuery(querybase, anContext))
				.dataQuery(this.buildDataQuery(querybase, anContext))
				.build();
	}

	/**
	 * Builds the.
	 *
	 * @return the select statement
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder.ISelectBuildBuilder#build()
	 */
	@Override
	public ISelect build() {
		this.valid();
		this.closed = true;

		return this.buildSelect(new NativeSqlBuildContext());
	}

	/**
	 * Builds the as sub select.
	 *
	 * @return the i sub select
	 * @see titus.jpa.nativequerybuilder.ISelectBuilder.ISelectBuilderFinalStage#buildAsSubSelect()
	 */
	@Override
	public ISubSelect buildAsSubSelect() {
		this.valid();
		this.closed = true;
		return new SubSelect(this::buildSelect);
	}

	/**
	 * The Class SubSelect.
	 */
	private static class SubSelect implements ISubSelect {

		/** The builder. */
		private final Function<INativeSqlBuildContext, ISelect> builder;

		/** The select. */
		private ISelect select;

		/**
		 * Instantiates a new sub select.
		 *
		 * @param aBuilder
		 *            the a builder
		 */
		public SubSelect(final Function<INativeSqlBuildContext, ISelect> aBuilder) {
			this.builder = aBuilder;
		}

		/**
		 * Builds the native sql.
		 *
		 * @param anContext
		 *            the an context
		 * @return the string
		 * @see titus.jpa.nativequerybuilder.INativeSql#buildNativeSql(titus.jpa.nativequerybuilder.INativeSql.INativeSqlBuildContext)
		 */
		@Override
		public String buildNativeSql(final INativeSqlBuildContext anContext) {
			if (this.select == null)
				this.select = this.builder.apply(anContext);

			return this.select.getDataQuery();
		}
	}
}