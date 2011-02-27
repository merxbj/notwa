/*
 * NoteDal
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
package notwa.server.dal;

import notwa.wom.note.Note;
import notwa.wom.note.NoteCollection;
import notwa.wom.note.NotePrimaryKey;
import notwa.common.ParameterSet;
import notwa.common.Parameter;
import notwa.server.sql.Parameters;
import notwa.server.sql.Sql;
import notwa.exception.DalException;
import notwa.wom.Context;
import notwa.wom.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import notwa.server.common.DatabaseConnectionInfo;

/**
 * <code>NoteDal</code> is a <code>DataAccessLayer</code> concrete implementation
 * providing the actual data and methods how to maintain the note data persisted
 * in the database.
 * <p>The actuall workflow is maintained by the base class itself.</p>
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class NoteDal extends DataAccessLayer<Note, NoteCollection> {

    /**
     * The sole constructor delegating all the work to the base <code>class</code>.
     *
     * @param ci    The <code>ConnectionInfo</code> which refers the actual database
     *              where we want to collect data from.
     * @param context   The actual <code>Context</code> where we want to let the DAL
     *                  live its pittyful life of collectiong data.
     */
    public NoteDal(DatabaseConnectionInfo ci, Context context) {
        super(ci, context);
    }

    @Override
    protected String getSqlTemplate() {
        StringBuilder vanillaSql = new StringBuilder();

        vanillaSql.append("SELECT   note_id,\n");
        vanillaSql.append("         work_item_id,\n");
        vanillaSql.append("         author_user_id,\n");
        vanillaSql.append("         note\n");
        vanillaSql.append("FROM Work_Item_Note\n");
        vanillaSql.append("/** STATEMENT=WHERE;");
        vanillaSql.append("        {column=note_id;parameter=NoteId;}");
        vanillaSql.append("        {column=work_item_id;parameter=NoteWorkItemId;}");
        vanillaSql.append("        {column=author_user_id;parameter=NoteAuthorUserId;}");
        vanillaSql.append("**/");

        return vanillaSql.toString();
    }

    @Override
    protected Object getPrimaryKey(ResultSet rs) throws DalException {
        try {
            return new NotePrimaryKey(rs.getInt("note_id"), rs.getInt("work_item_id"));
        } catch (SQLException sex) {
            throw new DalException("Unable to read the note primary key from the database!", sex);
        }
    }

    @Override
    protected ParameterSet getPrimaryKeyParams(Object primaryKey) {
        NotePrimaryKey npk = (NotePrimaryKey) primaryKey;
        Parameter noteId = new Parameter(Parameters.Note.ID, npk.getNoteId(), Sql.Relation.EQUALTY);
        Parameter workItemId = new Parameter(Parameters.Note.WORK_ITEM_ID, npk.getWorkItemId(), Sql.Relation.EQUALTY);
        return new ParameterSet(new Parameter[] {noteId, workItemId});
    }

    @Override
    protected boolean isInCurrentContext(Object primaryKey) throws DalException {
        try {
            return currentContext.hasNote((NotePrimaryKey) primaryKey);
        } catch (Exception ex) {
            throw new DalException("Invalid primary key provided for context query!", ex);
        }
    }

    @Override
    protected Note getBusinessObject(Object primaryKey) throws DalException {
        try {
            return currentContext.getNote((NotePrimaryKey) primaryKey);
        } catch (Exception ex) {
            throw new DalException("Invalid primary key provided for context query!", ex);
        }
    }

    @Override
    protected Note getBusinessObject(Object primaryKey, ResultSet rs) throws DalException {
        try {
            NotePrimaryKey npk = (NotePrimaryKey) primaryKey;

            UserDal userDal = new UserDal(ci, currentContext);
            User author = userDal.get(rs.getInt("author_user_id"));

            Note n = new Note(npk);
            n.registerWithContext(currentContext);
            n.setAuthor(author);
            n.setNoteText(rs.getString("note"));

            return n;
        } catch (Exception ex) {
            throw new DalException("Error while parsing the Note from ResultSet!", ex);
        }
    }

    @Override
    protected void updateSingleRow(SmartResultSet srs, Note n) throws Exception {
        ResultSet rs = srs.getRs();
        rs.updateInt("note_id", n.getId().getNoteId());
        rs.updateInt("work_item_id", n.getId().getWorkItemId());
        rs.updateInt("author_user_id", n.getAuthor().getId());
        rs.updateString("note", n.getText());
    }

    @Override
    protected String getHighestUniqeIdentifierSql(Note n) {
        String sql = String.format("SELECT note_id FROM work_item_note WHERE work_item_id = %d ORDER BY note_id DESC", n.getId().getWorkItemId());
        return sql;
    }
}
