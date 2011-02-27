/*
 * FilterRule
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

package notwa.wom.filter;

import notwa.wom.BusinessObject;
import notwa.wom.Context;

/**
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class FilterRule extends BusinessObject implements Cloneable, Comparable<FilterRule> {

    private FilterRulePrimaryKey id;
    private String description;

    public FilterRule(FilterRulePrimaryKey id) {
        this.id = id;
    }

    public FilterRule(int ruleId) {
        super();
        this.id = new FilterRulePrimaryKey(ruleId);
    }

    public FilterRule(int ruleId, int ownerId) {
        super();
        this.id = new FilterRulePrimaryKey(ruleId, ownerId);
    }

    @Override
    public int getUniqeIdentifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasUniqeIdentifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerWithContext(Context currentContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setUniqeIdentifier(int value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(FilterRule other) {
        return this.id.compareTo(other.id);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        FilterRule clone = (FilterRule) super.clone();

        clone.id = (FilterRulePrimaryKey) this.id.clone(); // deep copy of this
        clone.description = this.description;
        return clone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (isAttached()) {
            attachedBOC.setUpdated(this);
        }
    }

}
