/*
 * FilterRulePrimaryKey
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

/**
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class FilterRulePrimaryKey implements Comparable<FilterRulePrimaryKey>, Cloneable {

    private int ruleId;
    private int ownerId;

    public FilterRulePrimaryKey(int owner_id) {
        this.ownerId = owner_id;
        this.ruleId = 0;
    }

    public FilterRulePrimaryKey(int ruleId, int ownerId) {
        this.ruleId = ruleId;
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    @Override
    public int compareTo(FilterRulePrimaryKey other) {
        Integer r1 = this.ruleId;
        Integer r2 = other.ruleId;
        Integer o1 = this.ownerId;
        Integer o2 = other.ownerId;

        int compare = o1.compareTo(o2);
        if (compare == 0) {
            compare = r2.compareTo(r1);
        }

        return compare;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FilterRulePrimaryKey other = (FilterRulePrimaryKey) obj;
        if (this.ruleId != other.ruleId) {
            return false;
        }
        if (this.ownerId != other.ownerId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.ruleId;
        hash = 47 * hash + this.ownerId;
        return hash;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        FilterRulePrimaryKey clone = (FilterRulePrimaryKey) super.clone();
        clone.ruleId = this.ruleId;
        clone.ownerId = this.ownerId;
        return clone;
    }
    
}
