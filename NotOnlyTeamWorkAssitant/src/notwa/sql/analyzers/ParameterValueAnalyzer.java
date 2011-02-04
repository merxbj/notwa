/*
 * ParameterValueAnalyzer
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

package notwa.sql.analyzers;

import notwa.sql.ComplexSqlFilter;
import notwa.sql.SimpleSqlFilter;
import notwa.sql.SqlAnalyzer;
import notwa.sql.SqlParameter;
import notwa.sql.SqlParameterSet;
import notwa.sql.Sql;

//TODO: <MERXBJ> Better comment!
//TODO: <MERXBJ> Make more generic!!!
/**
 * Very simple for now!
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class ParameterValueAnalyzer implements SqlAnalyzer {

    SqlParameterSet parameters;

    @Override
    public void analyzeComplexSqlFiter(ComplexSqlFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void analyzeSimpleSqlFilter(SimpleSqlFilter filter) {
        parameters = filter.getParameters();
    }

    /**
     * This is a very hack of mine! Fortunatelly I was able to extract him out
     * to standalone class through Visitor pattern.
     * If you are still reading this, please, don't dare to use it until you
     * fully understand its purpose!
     * @param <TOutput>
     * @param name
     * @return
     */
    public <TOutput> TOutput getParameterValue(String name) {
        for (SqlParameter p : parameters) {
            if (p.getName().equals(name) && p.getRelation().equals(Sql.Relation.EQUALTY)) {
                return (TOutput) p.getValue();
            }
        }
        return null;
    }

}
