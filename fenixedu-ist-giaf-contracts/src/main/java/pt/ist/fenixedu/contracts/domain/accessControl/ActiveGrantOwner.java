package pt.ist.fenixedu.contracts.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

import pt.ist.fenixedu.contracts.domain.Employee;
import pt.ist.fenixedu.contracts.domain.util.CategoryType;

@GroupOperator("activeGrantOwner")
public class ActiveGrantOwner extends GroupStrategy {
    private static final long serialVersionUID = 3734411152566615242L;

    @Override
    public String getPresentationName() {
        return "Active Grant Owner";
    }

    @Override
    public Set<User> getMembers() {
        return Bennu.getInstance().getEmployeesSet().stream().filter(ActiveGrantOwner::isGrantOwner)
                .map(employee -> employee.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && user.getPerson().getEmployee() != null
                && isGrantOwner(user.getPerson().getEmployee());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    protected static boolean isGrantOwner(Employee employee) {
        return (employee.getPerson().getPersonProfessionalData() != null ? employee.getPerson().getPersonProfessionalData()
                .getCurrentPersonContractSituationByCategoryType(CategoryType.GRANT_OWNER) : null) != null;
    }

}
