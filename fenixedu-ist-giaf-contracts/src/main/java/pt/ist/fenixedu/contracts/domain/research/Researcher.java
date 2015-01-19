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
package pt.ist.fenixedu.contracts.domain.research;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonContractSituation;
import pt.ist.fenixedu.contracts.domain.util.CategoryType;

public class Researcher extends Researcher_Base {
    public Researcher(Person person) {
        super();
        setPerson(person);
        setAllowsToBeSearched(Boolean.FALSE);
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setPerson(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private String normalizeKeywords(String keywordList) {
        String[] keys = keywordList.split(",");

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String[] dtd = key.split(" ");

            for (String eee : dtd) {
                if (eee.trim().length() > 0) {
                    sb.append(eee.trim()).append(" ");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(",");
        }

        return sb.substring(0, sb.length() - 1);
    }

    public boolean isActiveContractedResearcher() {
        PersonContractSituation currentResearcherContractSituation = getCurrentContractedResearcherContractSituation();
        return currentResearcherContractSituation != null;
    }

    public PersonContractSituation getCurrentContractedResearcherContractSituation() {
        return getPerson().getPersonProfessionalData() != null ? getPerson().getPersonProfessionalData()
                .getCurrentPersonContractSituationByCategoryType(CategoryType.RESEARCHER) : null;
    }

}
