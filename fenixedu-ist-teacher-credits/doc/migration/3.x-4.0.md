Please run the following SQL statements **after** the *fenixedu-academic* migration.

```sql
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.TeacherCreditsState' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.TeacherCreditsState';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.TeacherCredits' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.TeacherCredits';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.TeacherCreditsDocument' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.TeacherCreditsDocument';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.DepartmentCreditsPool' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.DepartmentCreditsPool';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.credits.AnnualTeachingCreditsDocument' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.credits.AnnualTeachingCreditsDocument';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.SupportLesson' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.SupportLesson';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.TeacherService' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.TeacherService';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.TeacherServiceLog' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.TeacherServiceLog';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.TeacherServiceItem' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.TeacherServiceItem';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.DegreeTeachingService' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.DegreeTeachingService';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.DegreeProjectTutorialService' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.DegreeProjectTutorialService';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.InstitutionWorkTime' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.InstitutionWorkTime';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.ReductionService' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.ReductionService';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.OtherService' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.OtherService';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.DegreeTeachingServiceCorrection' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.DegreeTeachingServiceCorrection';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.teacher.TeacherServiceComment' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.TeacherServiceComment';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.time.calendarStructure.TeacherCreditsFillingCE' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.time.calendarStructure.TeacherCreditsFillingCE';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.time.calendarStructure.TeacherCreditsFillingForDepartmentAdmOfficeCE' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.time.calendarStructure.TeacherCreditsFillingForDepartmentAdmOfficeCE';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.time.calendarStructure.TeacherCreditsFillingForTeacherCE' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.time.calendarStructure.TeacherCreditsFillingForTeacherCE';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.credits.AnnualTeachingCredits' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.credits.AnnualTeachingCredits';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.credits.AnnualCreditsState' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.credits.AnnualCreditsState';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.reports.EffectiveTeachingLoadReportFile' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.reports.EffectiveTeachingLoadReportFile';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.reports.TeachersByShiftReportFile' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.reports.TeachersByShiftReportFile';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.reports.TeachersListFromGiafReportFile' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.reports.TeachersListFromGiafReportFile';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.reports.TimetablesReportFile' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.reports.TimetablesReportFile';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.reports.TeacherCreditsReportFile' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.reports.TeacherCreditsReportFile';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME ='pt.ist.fenixedu.teacher.domain.credits.CreditsPersonFunctionsSharedQueueJob' where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.credits.CreditsPersonFunctionsSharedQueueJob';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = "org.fenixedu.academic.domain.teacher.Career" WHERE DOMAIN_CLASS_NAME in ("org.fenixedu.academic.domain.teacher.ProfessionalCareer", "org.fenixedu.academic.domain.teacher.TeachingCareer");

delete from QUEUE_JOB where ((OID >> 32) & 0xFFFF) = (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.TeacherCreditsQueueJob'); 
delete from TEACHER_SERVICE_ITEM where ((OID >> 32) & 0xFFFF) = (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.TeacherAdviseService');
delete from TEACHER_SERVICE_ITEM where ((OID >> 32) & 0xFFFF)  = (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.TeacherMasterDegreeService');
delete from TEACHER_SERVICE_ITEM where ((OID >> 32) & 0xFFFF) = (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.TeacherPastService');
delete from TEACHER_SERVICE_ITEM where ((OID >> 32) & 0xFFFF) = (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME ='org.fenixedu.academic.domain.teacher.TeacherServiceNotes');
```