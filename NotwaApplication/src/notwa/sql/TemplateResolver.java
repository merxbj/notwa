/*
 * TemplateResolver
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

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import notwa.exception.IllegalSqlOperationException;

/**
 *
 * @author Jaroslav Merxbauer
 * @version %I% %G%
 */
public class TemplateResolver {
    private Map<String, String> mappings;

    public TemplateResolver(Map<String, String> mappings) {
        this.mappings = mappings;
    }

    public String resolve(String template) {
        String toResolve = template;
        for (Entry<String, String> mapping : mappings.entrySet()) {
            String target = mapping.getKey();
            String ammunition = mapping.getValue();

            Pattern pattern = Pattern.compile(String.format("\\$\\{%s\\}", target));
            Matcher matcher = pattern.matcher(toResolve);

            toResolve = matcher.replaceAll(ammunition);
        }

        Pattern pattern = Pattern.compile("\\$\\{.*\\}");
        Matcher matcher = pattern.matcher(toResolve);

        if (matcher.find()) {
            String message = String.format("Unresolved SQL Parameter %s! Forget to specify mapping?", matcher.group());
            throw new IllegalSqlOperationException(message);
        }

        return toResolve;
    }
}
