/**
 * Copyright © 2011 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Teacher Credits.
 *
 * FenixEdu Teacher Credits is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Teacher Credits is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Teacher Credits.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.teacher.ui.renderers.providers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.service.services.commons.AbstractSearchObjects;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class SearchInternalUnits extends AbstractSearchObjects<Unit> implements AutoCompleteProvider<Unit> {

    @Override
    public Collection<Unit> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<Unit> units = UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT);
        units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEGREE_UNIT));
        units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));
        for (Iterator<Unit> iterator = units.iterator(); iterator.hasNext();) {
            Unit unit = iterator.next();
            if (unit.getUnitName().getIsExternalUnit()) {
                iterator.remove();
            }
        }

        return super.process(units, value, maxCount, argsMap);
    }

}
