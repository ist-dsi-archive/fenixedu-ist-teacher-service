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
package pt.ist.fenixedu.contracts.tasks.giafsync;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import pt.ist.fenixedu.contracts.domain.personnelSection.contracts.ProfessionalRelation;
import pt.ist.fenixedu.contracts.persistenceTierOracle.Oracle.PersistentSuportGiaf;
import pt.ist.fenixedu.contracts.tasks.giafsync.GiafSync.MetadataProcessor;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

class ImportProfessionalRelationsFromGiaf implements MetadataProcessor {

    private static final String TRUE_STRING = "S";

    public ImportProfessionalRelationsFromGiaf() {

    }

    @Override
    public void processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
        int updatedRelations = 0;
        int newRelations = 0;

        PersistentSuportGiaf oracleConnection = PersistentSuportGiaf.getInstance();
        PreparedStatement preparedStatement = oracleConnection.prepareStatement(getQuery());
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            String giafId = result.getString("tab_cod");
            String nameString = result.getString("tab_cod_alg");
            if (StringUtils.isEmpty(nameString)) {
                nameString = result.getString("tab_cod_dsc");
            }
            Boolean fullTimeEquivalent = getBoolean(result.getString("eti"));

            ProfessionalRelation professionalRelation = metadata.relation(giafId);
            MultiLanguageString name = new MultiLanguageString(MultiLanguageString.pt, nameString);
            if (professionalRelation != null) {
                if (!professionalRelation.getName().equalInAnyLanguage(name)) {
                    professionalRelation.edit(name, fullTimeEquivalent);
                    updatedRelations++;
                }
            } else {
                metadata.registerRelation(giafId, fullTimeEquivalent, name);
                newRelations++;
            }
        }
        result.close();
        preparedStatement.close();
        oracleConnection.closeConnection();
        log.printf("Relations: %d updated, %d new\n", updatedRelations, newRelations);
    }

    private Boolean getBoolean(String booleanString) {
        return ((!StringUtils.isEmpty(booleanString)) && booleanString.equalsIgnoreCase(TRUE_STRING));
    }

    protected String getQuery() {
        return "SELECT tab_cod, tab_cod_dsc, tab_cod_alg, eti FROM SLTCODGER cod WHERE  ( cod.TAB_ID = 'FA' AND cod.TAB_NUM = 46.5 )";
    }
}
