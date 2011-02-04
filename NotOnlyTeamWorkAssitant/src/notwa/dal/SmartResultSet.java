/*
 * SmartResultSet
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

import java.sql.ResultSet;
import notwa.sql.SqlFilter;

/**
 * Smart <code>ResultSet</code> is aware of the SqlFilter that was responsible
 * of its creation!
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class SmartResultSet {
    private ResultSet rs;
    private SqlFilter filter;

    public SmartResultSet(ResultSet rs, SqlFilter filter) {
        this.rs = rs;
        this.filter = filter;
    }

    public SqlFilter getFilter() {
        return filter;
    }

    public ResultSet getRs() {
        return rs;
    }
}
