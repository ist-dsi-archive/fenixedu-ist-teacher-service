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
package pt.ist.fenixedu.teacher.ui.struts.action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixedu.teacher.dto.DepartmentCreditsAuthorizationBean;
import pt.ist.fenixedu.teacher.ui.struts.action.CreditsManagerApp;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = CreditsManagerApp.class, path = "department-credits-authorizations", titleKey = "label.permissions",
        bundle = "TeacherCreditsSheetResources")
@Mapping(path = "/departmentCreditsAuthorizations", module = "scientificCouncil")
@Forwards(@Forward(name = "departmentCreditsAuthorizations",
        path = "/scientificCouncil/credits/departmentCredits/departmentCreditsAuthorizations.jsp"))
public class DepartmentCreditsAuthorizationsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareDepartmentCredits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DepartmentCreditsAuthorizationBean departmentCreditsAuthorizationBean = getRenderedObject();
        if (departmentCreditsAuthorizationBean == null) {
            departmentCreditsAuthorizationBean = new DepartmentCreditsAuthorizationBean();
        }
        request.setAttribute("departmentCreditsAuthorizationBean", departmentCreditsAuthorizationBean);
        return mapping.findForward("departmentCreditsAuthorizations");
    }

    public ActionForward addRoleDepartmentCredits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DepartmentCreditsAuthorizationBean departmentCreditsAuthorizationBean = getRenderedObject();
        request.setAttribute("success", departmentCreditsAuthorizationBean.assignPermission());
        request.setAttribute("departmentCreditsAuthorizationBean", departmentCreditsAuthorizationBean);
        return mapping.findForward("departmentCreditsAuthorizations");
    }

    public ActionForward removeRoleDepartmentCredits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject(request.getParameter("personId"));
        Department department = FenixFramework.getDomainObject(request.getParameter("departmentId"));
        DepartmentCreditsAuthorizationBean departmentCreditsAuthorizationBean =
                new DepartmentCreditsAuthorizationBean(department, person);
        departmentCreditsAuthorizationBean.removePermission();
        request.setAttribute("departmentCreditsAuthorizationBean", departmentCreditsAuthorizationBean);
        return mapping.findForward("departmentCreditsAuthorizations");
    }

}
