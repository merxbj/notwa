/*
 * SqlStatement
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

import notwa.logger.LoggingFacade;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * This <code>class</code> represents a single parameter placeholders statement
 * present in the SQL template.
 * This <code>SqlStatement</code> should be than provided with the parameter collection
 * and it will produce the built SQL to be a part of the related SQL template.
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class SqlStatement {
    private String connectWith;
    private HashMap<String,String> mappings;
    private StringBuilder statement;

    /**
     * The sole, parameter-less, constructor, initializing the class members.
     */
    public SqlStatement() {
        statement = new StringBuilder();
        mappings = new HashMap<String, String>();
    }

    /**
     * Accepts the single raw statement factored out from the sql template and
     * brings him into the object look.
     * <p>It, at first, recognizes, how the given statement should be related with
     * its surroundings (It is the opening WHERE? Or continues it as AND? Or or?)
     * Than it creates parameter name to column name mappings, to be able to compile
     * the final result which should replace the parameter name with the actual
     * value found within the <code>SqlParameter</code>.</p>
     *
     * @param rawStatement The raw statement.
     * @return  <code>true</code> if the parsing process completes successfully,
     *          <code>false</code> otherwise.
     */
    public boolean parse(String rawStatement) {
        connectWith = rawStatement.substring(rawStatement.indexOf("=") + 1, rawStatement.indexOf(";")).trim();

        try {
            StringTokenizer relations = new StringTokenizer(rawStatement, "{}");
            relations.nextToken(); // skip the first token which is obviously the statement header parsed before
            while (relations.hasMoreTokens()) {
                String column = null;
                String parameter = null;
                StringTokenizer relation = new StringTokenizer(relations.nextToken(), ";=");
                while (relation.hasMoreTokens()) {
                    String token = relation.nextToken();
                    if (token.equals("column")) {
                        column = relation.nextToken().trim();
                    } else if (token.equals("parameter")) {
                        parameter = relation.nextToken().trim();
                    }
                    if (column != null && parameter != null) {
                        mappings.put(parameter, column);
                        column = null;
                        parameter = null;
                    }
                }
            }
        } catch (Exception ex) {
            LoggingFacade.handleException(ex);
            return false;
        }
        return true;
    }

    /**
     * Returns the final SQL Query result built from the statement given at the
     * begining by mixing it with the actual <code>SqlParameter</code>s.
     *
     * @return The SQL Query part of the parent sql template.
     */
    public String compileStatement() {
        return statement.toString();
    }

    /**
     * Appends the given <code>SqlParameter</code> to this <code>SqlStatement</code>
     * itteratively building the resulting SQL part.
     *
     * @param parameter The parameter where the value and the actual relation will
     *                  be found.
     */
    public void applyFilter(SqlFilter filter) {
        TemplateResolver resolver = new TemplateResolver(this.mappings);
        statement.append(connectWith);
        statement.append(" ");
        statement.append(resolver.resolve(filter.formatForSql()));
    }
}
