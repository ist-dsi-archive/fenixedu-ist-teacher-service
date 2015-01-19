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
package pt.ist.fenixedu.contracts.ui.struts.action;

import java.util.Collection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.bennu.signals.Signal;

import pt.ist.fenixedu.contracts.domain.Employee;
import pt.ist.fenixedu.contracts.domain.organizationalStructure.ExternalContract;
import pt.ist.fenixedu.contracts.domain.organizationalStructure.PersonFunction;
import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduGiafContractsContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FenixFramework.getDomainModel().registerDeletionBlockerListener(
                Person.class,
                (person, blockers) -> {
                    if (person.getEmployee() != null) {
                        blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.person.cannot.be.deleted"));
                    }
                    if (((Collection<PersonFunction>) person.getParentAccountabilities(
                            AccountabilityTypeEnum.MANAGEMENT_FUNCTION, PersonFunction.class)).isEmpty()) {
                        blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.person.cannot.be.deleted"));
                    }
                });
        FenixFramework.getDomainModel().registerDeletionListener(Person.class, (person) -> {
            if (person.getResearcher() != null) {
                person.getResearcher().delete();
            }
        });
        FenixFramework.getDomainModel().registerDeletionBlockerListener(Unit.class, (unit, blockers) -> {
            if (unit.getFunctionsSet().isEmpty()) {
                blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.unit.cannot.be.deleted"));
            }
        });
        Signal.register("academic.thesis.participant.created",
                FenixEduGiafContractsContextListener::fillParticipantAffiliationAndCategory);
    }

    private static void fillParticipantAffiliationAndCategory(DomainObjectEvent<ThesisEvaluationParticipant> event) {
        ThesisEvaluationParticipant participation = event.getInstance();
        Person person = participation.getPerson();

        Teacher teacher = person.getTeacher();
        if (teacher != null && teacher.getDepartment() != null) {
            if (teacher.getLastCategory() == null) {
                participation.setCategory("-");
            } else {
                participation.setCategory(teacher.getLastCategory().getName().getContent());
            }
            participation.setAffiliation(teacher.getDepartment().getRealName());
        } else {
            Employee employee = person.getEmployee();
            if (employee != null) {
                Unit currentWorkingPlace = employee.getCurrentWorkingPlace();
                if (currentWorkingPlace != null) {
                    participation.setAffiliation(currentWorkingPlace.getNameWithAcronym());
                }
            } else {
                ExternalContract contract = ExternalContract.getExternalContract(person);
                if (contract != null) {
                    participation.setAffiliation(contract.getInstitutionUnit().getName());
                }
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
