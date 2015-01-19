/**
 * Copyright © 2011 Instituto Superior Técnico
 *
 * This file is part of FenixEdu GIAF Contracts.
 *
 * FenixEdu GIAF Contracts is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu GIAF Contracts is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu GIAF Contracts.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.contracts.domain.personnelSection.contracts;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonAbsence extends PersonAbsence_Base {

    public PersonAbsence(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate, final LocalDate endDate,
            final Absence absence, final String absenceGiafId, final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setAbsence(absence);
        setAbsenceGiafId(absenceGiafId);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    @Override
    public boolean isValid() {
        return getAbsence() != null && getBeginDate() != null && getEndDate() != null && !getBeginDate().isAfter(getEndDate());
    }

    @Override
    public boolean getIsSabaticalOrEquivalent() {
        return getAbsence().getIsSabaticalOrEquivalent();
    }

    @Override
    public boolean getHasMandatoryCredits() {
        return getAbsence().getHasMandatoryCredits();
    }

    @Override
    public boolean getGiveCredits() {
        return getAbsence().getGiveCredits();
    }

}
