/*
 * NoteCollection
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
package notwa.dal;

import notwa.common.ConnectionInfo;
import notwa.exception.DalException;
import notwa.wom.user.UserCollection;
import notwa.wom.user.User;
import notwa.wom.Context;
import notwa.sql.Parameters;
import notwa.sql.SqlParameterSet;
import notwa.sql.Sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import notwa.sql.AnalyzableSql;
import notwa.sql.SqlFilter;
import notwa.sql.SqlParameter;
import notwa.sql.analyzers.ParameterValueAnalyzer;

/**
 * <code>ProjectToUserAssignmentDal</code> is a <code>DataAccessLayer</code>
 * concrete implementation providing the actual data and methods how to maintain
 * the user assignment data persisted in the database.
 * <p>The actuall workflow is maintained by the base class itself.</p>
 * <p>This DAL doesn't support the {@link #get(java.lang.Object)} method</p>
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class ProjectToUserAssignmentDal extends DataAccessLayer<User, UserCollection> {

    /**
     * The sole constructor delegating all the work to the base <code>class</code>.
     *
     * @param ci    The <code>ConnectionInfo</code> which refers the actual database
     *              where we want to collect data from.
     * @param context   The actual <code>Context</code> where we want to let the DAL
     *                  live its pittyful life of collectiong data.
     */
    public ProjectToUserAssignmentDal(ConnectionInfo ci, Context context) {
        super(ci, context);
    }

    @Override
    protected String getSqlTemplate() {
        StringBuilder vanillaSql = new StringBuilder();

        vanillaSql.append("SELECT   pua.user_id    AS user_id,\n");
        vanillaSql.append("         pua.project_id AS project_id\n");
        vanillaSql.append("FROM Project_User_Assignment pua\n");
        vanillaSql.append("JOIN Project p\n");
        vanillaSql.append("ON p.project_id = pua.project_id\n");
        vanillaSql.append("/** STATEMENT=WHERE;");
        vanillaSql.append("        {column=pua.project_id;parameter=ProjectId;}");
        vanillaSql.append("**/");

        return vanillaSql.toString();
    }

    @Override
    protected Object getPrimaryKey(ResultSet rs) throws DalException {
        try {
            return rs.getInt("user_id");
        } catch (SQLException sex) {
            throw new DalException("Unable to read the user id from the database!", sex);
        }
    }

    @Override
    protected SqlParameterSet getPrimaryKeyParams(Object primaryKey) {
        return new SqlParameterSet(new SqlParameter(Parameters.User.ID, primaryKey, Sql.Relation.EQUALTY));
    }

    @Override
    protected boolean isInCurrentContext(Object primaryKey) throws DalException {
        try {
            return currentContext.hasUser((Integer) primaryKey);
        } catch (Exception ex) {
            throw new DalException("Invalid primary key provided for context query!", ex);
        }
    }

    @Override
    protected User getBusinessObject(Object primaryKey) throws DalException {
        try {
            return currentContext.getUser((Integer) primaryKey);
        } catch (Exception ex) {
            throw new DalException("Invalid primary key provided for context query!", ex);
        }
    }

    @Override
    protected User getBusinessObject(Object primaryKey, ResultSet rs) throws DalException {
        UserDal userDal = new UserDal(ci, currentContext);
        return userDal.get(primaryKey);
    }

    @Override
    public User get(Object primaryKey) throws DalException {
        throw new DalException("This DataAccessLayer doesn't support operation get.");
    }

    @Override
    protected void updateSingleRow(SmartResultSet srs, User u) throws Exception {

        /**
         * Get the origin filter which is responsible for the result we are updating.
         * Make sure that this filter is analyzable, otherwise we are screwed!
         */
        SqlFilter filter = srs.getFilter();
        ParameterValueAnalyzer analyzer = new ParameterValueAnalyzer();
        if (!(srs instanceof AnalyzableSql)) {
            throw new DalException("Expected Analyzable SqlFilter in ProjectToUserAssignmentDal::updateSingleRow!");
        }

        /**
         * We have analyzable sql! Lets instantiate the analyzer and analyze it!
         */
        AnalyzableSql asql = (AnalyzableSql) filter;
        asql.provideAnalizableData(analyzer);

        /**
         * Ask the analyzer for the project_id parameter value which should indicate
         * whose project assignments are being updated!
         * This is particulary useful when we are assigning a new user. The 
         */
        Integer currentProjectId = analyzer.getParameterValue(Parameters.Project.ID);
        if (currentProjectId == 0) {
            throw new DalException("Unexpected project_id 0 when updating user assigned to it!");
        }

        ResultSet rs = srs.getRs();
        rs.updateInt("project_id", currentProjectId);
        rs.updateInt("user_id", u.getId());
    }

    @Override
    protected String getHighestUniqeIdentifierSql(User bo) {
        throw new UnsupportedOperationException("This table has not uniqe identifier!");
    }
}
