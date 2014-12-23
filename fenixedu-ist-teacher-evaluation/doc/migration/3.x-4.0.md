Please run the following SQL statement **after** the *fenixedu-academic* migration.

```sql
UPDATE FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = REPLACE(DOMAIN_CLASS_NAME,'org.fenixedu.academic.domain.teacher.evaluation', 'pt.ist.fenixedu.teacher.evaluation.domain') WHERE DOMAIN_CLASS_NAME like 'org.fenixedu.academic.domain.teacher.evaluation%';
```