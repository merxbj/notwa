/*
 * SqlAnalyzer
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

package notwa.sql;

// TODO: <MERXBJ> Comment this precisely!
/**
 * This is interface implmented by the Visitor class (Visitor pattern).
 * @author Jaroslav Merxbauer
 * @authoer %I% %G%
 */
public interface SqlAnalyzer {

    /**
     * Analyzes the legacy <code>SimpleSqlFilter</code> data.
     * @param filter The actual filter to be analyzed.
     */
    public void analyzeSimpleSqlFilter(SimpleSqlFilter filter);

    /**
     * Analyzes the prefered <code>ComlexSqlFilter</code>.
     * @param filter The actual filter to be analyzed.
     */
    public void analyzeComplexSqlFiter(ComplexSqlFilter filter);
}
