package org.healthystyle.event.repository.dialect;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

public class PostgreSQLFunctionContributor implements FunctionContributor {

	@Override
	public void contributeFunctions(FunctionContributions functionContributions) {
		SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();
		functionRegistry.register("haversine", new StandardSQLFunction("haversine"));
	}

}
