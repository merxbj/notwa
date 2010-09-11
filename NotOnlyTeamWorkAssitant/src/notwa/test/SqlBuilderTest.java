/*
 * SqlBuilderTest
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

package notwa.test;

import notwa.sql.*;
import java.sql.Timestamp;

/**
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class SqlBuilderTest {

    public SqlBuilderTest() {
        /**
         * Prepare all valid and invalid parameters for the following sql
         */
        SqlParameter p1 = new SqlParameter(Parameters.WorkItem.ASSIGNED_USER, 10000001, Sql.Relation.EQUALTY);
        SqlParameter p2 = new SqlParameter(Parameters.WorkItem.STATUS, 10000002, Sql.Relation.GREATER_OR_EQUALS);
        SqlParameter p3 = new SqlParameter(Parameters.WorkItem.DEADLINE, Timestamp.valueOf("2010-08-01 08:12:55"), Sql.Relation.EQUALTY);
        SqlParameter p4 = new SqlParameter(Parameters.User.ID, 123456, Sql.Relation.EQUALTY);
        
        /**
         * Build the first simple sql filter
         */
        SqlFilterBuilder ssf = new SimpleSqlFilter();
        ssf.where(p1);
        ssf.and(p2);

        /**
         * Make it advanced
         */
        SqlFilterBuilder cssf = new ComplexSqlFilter();
        cssf.where((SqlFilter) ssf);
        cssf.or(p3);
        //cssf.and(p4);

        /**
         * Switch from Builder to Filter iface
         */
        SqlFilter sf1 = (SqlFilter) ssf;
        SqlFilter sf2 = (SqlFilter) cssf;

        StringBuilder vanillaSql = new StringBuilder();
        vanillaSql.append("SELECT   work_item_id,\n");
        vanillaSql.append("         assigned_user_id,\n");
        vanillaSql.append("         status_id,\n");
        vanillaSql.append("         project_id,\n");
        vanillaSql.append("         parent_work_item_id,\n");
        vanillaSql.append("         subject,\n");
        vanillaSql.append("         working_priority,\n");
        vanillaSql.append("         description,\n");
        vanillaSql.append("         expected_timestamp,\n");
        vanillaSql.append("         last_modified_timestamp\n");
        vanillaSql.append("FROM Work_Item\n");
        vanillaSql.append("/** STATEMENT=WHERE;");
        vanillaSql.append("        {column=work_item_id;parameter=WorkItemId;}");
        vanillaSql.append("        {column=status_id;parameter=WorkItemStatusId;}");
        vanillaSql.append("        {column=working_priority;parameter=WorkItemPriorityId;}");
        vanillaSql.append("        {column=assigned_user_id;parameter=WorkItemAssignedUserId;}");
        vanillaSql.append("        {column=expected_timestamp;parameter=WorkItemDeadline;}");
        vanillaSql.append("**/");

        /**
         * Build and print the resulting sql
         */
        SqlBuilder builder = new SqlBuilder(vanillaSql.toString(), sf2);
        System.out.println(builder.compileSql());
    }
}
