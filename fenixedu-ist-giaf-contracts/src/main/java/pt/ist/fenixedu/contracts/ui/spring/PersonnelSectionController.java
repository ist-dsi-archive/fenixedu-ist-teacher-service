package pt.ist.fenixedu.contracts.ui.spring;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringFunctionality(app = PersonnelSectionSpringApplication.class, title = "title.personnelSection.manageContracts",
        accessGroup = "#personnelSection")
@RequestMapping("/personnelSection")
public class PersonnelSectionController {

    @Autowired
    PersonnelSectionService service;

    @RequestMapping(method = GET)
    public String home(Model model, @ModelAttribute PersonnelSectionSearchBean search) {
        model.addAttribute("search", search);
        model.addAttribute("searchResult", search.search());
        return "/personnelSection/search";
    }

    @RequestMapping(method = GET, value = "createEmployee/{person}")
    public String createEmployee(Model model, @PathVariable Person person, @ModelAttribute PersonnelSectionSearchBean search) {
        try {
            service.createEmployee(person);
            search.setEmployeeNumber(person.getEmployee().getEmployeeNumber());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getLocalizedMessage());
        }

        model.addAttribute("search", search);
        model.addAttribute("searchResult", search.search());

        return "/personnelSection/search";
    }
}