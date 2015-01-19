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
package pt.ist.fenixedu.contracts.ui.struts.action.manager.personManagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.messaging.FindPersonAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixedu.contracts.domain.Employee;
import pt.ist.fenixedu.contracts.domain.organizationalStructure.Contract;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.GiafProfessionalData;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonAbsence;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonContractSituation;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonFunctionsAccumulation;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonGrantOwnerEquivalent;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonProfessionalCategory;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonProfessionalContract;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonProfessionalExemption;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonProfessionalRegime;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonProfessionalRelation;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonSabbatical;
import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.PersonServiceExemption;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/professionalInformation", module = "manager", functionality = FindPersonAction.class)
@Forwards({ @Forward(name = "showProfessionalInformation",
        path = "/manager/personManagement/contracts/showProfessionalInformation.jsp") })
public class ProfessionalInformationDA extends FenixDispatchAction {

    public ActionForward showProfessioanlData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        if (person.getPersonProfessionalData() != null) {
            request.setAttribute("professionalData", person.getPersonProfessionalData());
        }
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showSituations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonContractSituation> situations = new ArrayList<PersonContractSituation>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonContractSituation personContractSituation : giafProfessionalData.getPersonContractSituationsSet()) {
                    if (personContractSituation.getAnulationDate() == null) {
                        situations.add(personContractSituation);
                    }
                }
            }
        }
        request.setAttribute("situations", situations);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showCategories(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalCategory> categories = new ArrayList<PersonProfessionalCategory>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalCategory personProfessionalCategory : giafProfessionalData
                        .getPersonProfessionalCategoriesSet()) {
                    if (personProfessionalCategory.getAnulationDate() == null) {
                        categories.add(personProfessionalCategory);
                    }
                }
            }
        }
        request.setAttribute("categories", categories);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showRegimes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalRegime> regimes = new ArrayList<PersonProfessionalRegime>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalRegime personProfessionalRegime : giafProfessionalData.getPersonProfessionalRegimesSet()) {
                    if (personProfessionalRegime.getAnulationDate() == null) {
                        regimes.add(personProfessionalRegime);
                    }
                }
            }
        }
        request.setAttribute("regimes", regimes);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showRelations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalRelation> relations = new ArrayList<PersonProfessionalRelation>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalRelation personProfessionalRelation : giafProfessionalData
                        .getPersonProfessionalRelationsSet()) {
                    if (personProfessionalRelation.getAnulationDate() == null) {
                        relations.add(personProfessionalRelation);
                    }
                }
            }
        }
        request.setAttribute("relations", relations);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showContracts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalContract> contracts = new ArrayList<PersonProfessionalContract>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalContract personProfessionalContract : giafProfessionalData
                        .getPersonProfessionalContractsSet()) {
                    if (personProfessionalContract.getAnulationDate() == null) {
                        contracts.add(personProfessionalContract);
                    }
                }
            }
        }
        request.setAttribute("contracts", contracts);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showFunctionsAccumulations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonFunctionsAccumulation> functionsAccumulations = new ArrayList<PersonFunctionsAccumulation>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonFunctionsAccumulation employeeFunctionsAccumulation : giafProfessionalData
                        .getPersonFunctionsAccumulationsSet()) {
                    if (employeeFunctionsAccumulation.getAnulationDate() == null) {
                        functionsAccumulations.add(employeeFunctionsAccumulation);
                    }
                }
            }
        }
        request.setAttribute("functionsAccumulations", functionsAccumulations);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showSabbaticals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalExemption> sabbaticals = new ArrayList<PersonProfessionalExemption>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptionsSet()) {
                    if (personProfessionalExemption instanceof PersonSabbatical
                            && personProfessionalExemption.getAnulationDate() == null) {
                        sabbaticals.add(personProfessionalExemption);
                    }
                }
            }
        }
        request.setAttribute("sabbaticals", sabbaticals);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showServiceExemptions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalExemption> serviceExemptions = new ArrayList<PersonProfessionalExemption>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptionsSet()) {
                    if (personProfessionalExemption instanceof PersonServiceExemption
                            && personProfessionalExemption.getAnulationDate() == null) {
                        serviceExemptions.add(personProfessionalExemption);
                    }
                }
            }
        }
        request.setAttribute("serviceExemptions", serviceExemptions);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showGrantOwnerEquivalences(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalExemption> grantOwnerEquivalences = new ArrayList<PersonProfessionalExemption>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptionsSet()) {
                    if (personProfessionalExemption instanceof PersonGrantOwnerEquivalent
                            && personProfessionalExemption.getAnulationDate() == null) {
                        grantOwnerEquivalences.add(personProfessionalExemption);
                    }
                }
            }
        }
        request.setAttribute("grantOwnerEquivalences", grantOwnerEquivalences);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showAbsences(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalExemption> absences = new ArrayList<PersonProfessionalExemption>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptionsSet()) {
                    if (personProfessionalExemption instanceof PersonAbsence
                            && personProfessionalExemption.getAnulationDate() == null) {
                        absences.add(personProfessionalExemption);
                    }
                }
            }
        }
        request.setAttribute("absences", absences);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showEmployeeWorkingUnits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));
        List<Contract> workingUnits = new ArrayList<Contract>();
        Employee employee = person.getEmployee();
        if (employee != null) {
            workingUnits.addAll(employee.getWorkingContracts());
        }
        request.setAttribute("workingUnits", workingUnits);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

}