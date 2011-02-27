/*
 * SimpleSqlFilter
 *
 * Copyright (C) 2010  Jaroslav Merxbauer
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package notwa.server.sql;

import notwa.common.Parameter;
import notwa.common.ParameterSet;
import java.util.Iterator;
import notwa.exception.IllegalSqlOperationException;

/**
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class SimpleSqlFilter implements SqlFilter, SqlFilterBuilder, AnalyzableSql {

    private ParameterSet parameters;
    private String relation;

    @Override
    public String formatForSql() {

        /**
         * Don't bother with the sql building if there are no parameters to resolve
         */
        if (parameters.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder("(");
        for (Iterator<Parameter> it = parameters.iterator(); it.hasNext();) {
            Parameter param = it.next();
            builder.append(param.formatForSql());
            if (it.hasNext()) {
                builder.append(" ").append(relation).append(" ");
            }
        }

        return builder.append(")").toString();
    }

    @Override
    public void where(SqlFilter filter) {
        if (this.parameters.size() > 0) {
            throw new IllegalSqlOperationException("Adding SqlFilter using where() twice per single statement!");
        }

        if (!(filter instanceof Parameter)) {
            throw new IllegalSqlOperationException("Unsupported SqlFilter for SimpleSqlFilter!");
        }

        this.parameters.add((Parameter) filter);
    }

    @Override
    public void and(SqlFilter filter) {
        if (this.relation.equals("")) {
            this.relation = Sql.Logical.CONJUNCTION;
        }

        if (this.relation.equals(Sql.Logical.DISJUNCTION)) {
            throw new IllegalSqlOperationException("Adding SqlFilter using and() while or() has been already used in SimpleSqlFilter!");
        }

        if (!(filter instanceof Parameter)) {
            throw new IllegalSqlOperationException("Unsupported SqlFilter for SimpleSqlFilter!");
        }

        this.parameters.add((Parameter) filter);
    }

    @Override
    public void or(SqlFilter filter) {
        if (this.relation.equals("")) {
            this.relation = Sql.Logical.DISJUNCTION;
        }

        if (this.relation.equals(Sql.Logical.CONJUNCTION)) {
            throw new IllegalSqlOperationException("Adding SqlFilter using or() while and() has been already used in SimpleSqlFilter!");
        }

        if (!(filter instanceof Parameter)) {
            throw new IllegalSqlOperationException("Unsupported SqlFilter for SimpleSqlFilter!");
        }

        this.parameters.add((Parameter) filter);
    }

    public SimpleSqlFilter() {
        this.relation = "";
        this.parameters = new ParameterSet();
    }

    public SimpleSqlFilter(ParameterSet parameters, String relation) {
        this.parameters = parameters;
        this.relation = relation;
    }

    @Override
    public void provideAnalizableData(SqlAnalyzer analyzer) {
        analyzer.analyzeSimpleSqlFilter(this);
    }

    public ParameterSet getParameters() {
        return parameters;
    }

}
