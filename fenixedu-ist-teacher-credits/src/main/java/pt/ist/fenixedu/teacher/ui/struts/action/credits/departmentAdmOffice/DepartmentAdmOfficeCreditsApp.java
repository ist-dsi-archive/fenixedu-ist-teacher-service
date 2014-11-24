package pt.ist.fenixedu.teacher.ui.struts.action.credits.departmentAdmOffice;

import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "TeacherCreditsSheetResources", path = "credits", titleKey = "label.credits",
        hint = "Department Admin Office",
        accessGroup = "role(DEPARTMENT_CREDITS_MANAGER) & role(DEPARTMENT_ADMINISTRATIVE_OFFICE)")
public class DepartmentAdmOfficeCreditsApp {
}