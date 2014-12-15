package pt.ist.fenixedu.contracts.service;

import java.util.Collection;
import java.util.Set;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.collect.Sets;

public class UpdateDegreeCurricularPlanMembersGroup {

    @Atomic
    public static void run(DegreeCurricularPlan degreeCurricularPlan, String[] add, String[] remove) {
        Group original = degreeCurricularPlan.getCurricularPlanMembersGroup();

        Group changed = original;
        if (add != null) {
            for (String personID : add) {
                Person person = FenixFramework.getDomainObject(personID);
                changed = changed.grant(person.getUser());
            }
        }
        if (remove != null) {
            for (String personID : remove) {
                Person person = FenixFramework.getDomainObject(personID);
                changed = changed.revoke(person.getUser());
            }
        }

        updateBolonhaManagerRoleToGroupDelta(degreeCurricularPlan, original, changed);
        degreeCurricularPlan.setCurricularPlanMembersGroup(changed);
    }

    private static void updateBolonhaManagerRoleToGroupDelta(DegreeCurricularPlan degreeCurricularPlan, Group original,
            Group changed) {
        Set<User> originalMembers = original.getMembers();
        Set<User> newMembers = changed.getMembers();
        for (User user : Sets.difference(originalMembers, newMembers)) {
            Person person = user.getPerson();
            if (RoleType.BOLONHA_MANAGER.isMember(user) && !belongsToOtherGroupsWithSameRole(degreeCurricularPlan, person)) {
                RoleType.revoke(RoleType.BOLONHA_MANAGER, user);
            }
        }
        for (User user : Sets.difference(newMembers, originalMembers)) {
            RoleType.grant(RoleType.BOLONHA_MANAGER, user);
        }
    }

    private static boolean belongsToOtherGroupsWithSameRole(DegreeCurricularPlan dcpWhoAsks, Person person) {
        for (Degree bolonhaDegree : Degree.readBolonhaDegrees()) {
            for (DegreeCurricularPlan dcp : bolonhaDegree.getDegreeCurricularPlansSet()) {
                if (dcp != dcpWhoAsks) {
                    if (dcp.getCurricularPlanMembersGroup().isMember(person.getUser())) {
                        return true;
                    }
                }
            }
        }

        Collection<Department> departments = Bennu.getInstance().getDepartmentsSet();
        for (Department department : departments) {
            Group group = department.getCompetenceCourseMembersGroup();
            if (group != null && group.isMember(person.getUser())) {
                return true;
            }
        }

        return false;
    }

}
