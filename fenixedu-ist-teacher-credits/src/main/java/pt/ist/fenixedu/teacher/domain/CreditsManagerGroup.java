package pt.ist.fenixedu.teacher.domain;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("creditsManager")
public class CreditsManagerGroup extends GroupStrategy {

    @Override
    public String getPresentationName() {
        return "Credits Manager";
    }

    @Override
    public Set<User> getMembers() {
        Set<User> members = new HashSet<User>();
        for (Department department : Bennu.getInstance().getDepartmentsSet()) {
            for (Person person : department.getAssociatedPersonsSet()) {
                members.add(person.getUser());
            }
        }
        return members;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && !user.getPerson().getManageableDepartmentCreditsSet().isEmpty();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
