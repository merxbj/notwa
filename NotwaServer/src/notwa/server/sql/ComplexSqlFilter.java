/*
 * ComplexSqlFilter
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

import java.util.ArrayList;
import java.util.Iterator;
import notwa.exception.IllegalSqlOperationException;

/**
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class ComplexSqlFilter implements SqlFilter, SqlFilterBuilder, AnalyzableSql {

    private ArrayList<SqlFilter> filters;

    @Override
    public String formatForSql() {

        /**
         * Don't bother with the sql building if there are no parameters to resolve!
         */
        if (filters.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Iterator<SqlFilter> it = filters.iterator(); it.hasNext();) {
            SqlFilter filter = it.next();
            builder.append(filter.formatForSql());
            if (it.hasNext()) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public ComplexSqlFilter() {
        filters = new ArrayList<SqlFilter>();
    }

    @Override
    public void where(SqlFilter filter) {
        if (this.filters.size() > 0) {
            throw new IllegalSqlOperationException("Adding SqlFilter using where() twice per single statement!");
        }
        this.filters.add(filter);
    }

    @Override
    public void and(SqlFilter filter) {
        this.filters.add(new RelatedSqlFilter(Sql.Logical.CONJUNCTION, filter));
    }

    @Override
    public void or(SqlFilter filter) {
        this.filters.add(new RelatedSqlFilter(Sql.Logical.DISJUNCTION, filter));
    }

    class RelatedSqlFilter implements SqlFilter {
        private String connectingRelation;
        private SqlFilter filter;

        @Override
        public String formatForSql() {
            StringBuilder builder = new StringBuilder();
            builder.append(connectingRelation).append(" ");
            builder.append(filter.formatForSql());
            return builder.toString();
        }

        public RelatedSqlFilter(String relation, SqlFilter filter) {
            this.connectingRelation = relation;
            this.filter = filter;
        }
    }

    @Override
    public void provideAnalizableData(SqlAnalyzer analyzer) {
        analyzer.analyzeComplexSqlFiter(this);
    }

}
