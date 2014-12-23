package pt.ist.fenixedu.contracts.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

import pt.ist.fenixedu.contracts.domain.research.Researcher;

@GroupOperator("activeResearchers")
public class ActiveResearchers extends GroupStrategy {
    private static final long serialVersionUID = -6648971466827719165L;

    @Override
    public String getPresentationName() {
        return "Active Researchers";
    }

    @Override
    public Set<User> getMembers() {
        return Bennu.getInstance().getResearchersSet().stream().filter(Researcher::isActiveContractedResearcher)
                .map(researcher -> researcher.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && user.getPerson().getResearcher() != null
                && user.getPerson().getResearcher().isActiveContractedResearcher();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
