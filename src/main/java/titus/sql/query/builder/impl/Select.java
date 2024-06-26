package titus.sql.query.builder.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import titus.sql.query.builder.ISelect;

/**
 * The Class SelectStatement.
 */
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Select implements ISelect {

	/** The count query. */
	private String countQuery;

	/** The data query. */
	private String dataQuery;

}
