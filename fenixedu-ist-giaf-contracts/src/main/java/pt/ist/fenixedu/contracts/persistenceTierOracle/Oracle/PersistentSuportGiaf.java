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
package pt.ist.fenixedu.contracts.persistenceTierOracle.Oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import pt.ist.fenixedu.contracts.persistenceTierOracle.FenixIstGiafContractsConfiguration;

public class PersistentSuportGiaf {
    private static PersistentSuportGiaf instance = null;

    private static String databaseUrl = null;

    private static Map<Thread, Connection> connectionsMap = new HashMap<>();

    public static synchronized PersistentSuportGiaf getInstance() {
        if (instance == null) {
            instance = new PersistentSuportGiaf();
        }
        return instance;
    }

    private Connection openConnection() throws SQLException {
        if (databaseUrl == null) {
            String DBUserName = FenixIstGiafContractsConfiguration.getConfiguration().dbGiafUser();
            String DBUserPass = FenixIstGiafContractsConfiguration.getConfiguration().dbGiafPass();
            String DBUrl = FenixIstGiafContractsConfiguration.getConfiguration().dbGiafAlias();
            if (DBUserName == null || DBUserPass == null || DBUrl == null) {
                throw new Error("Please configure GIAF database connection");
            }
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("jdbc:oracle:thin:");
            stringBuffer.append(DBUserName);
            stringBuffer.append("/");
            stringBuffer.append(DBUserPass);
            stringBuffer.append("@");
            stringBuffer.append(DBUrl);
            databaseUrl = stringBuffer.toString();
        }
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        Connection connection = DriverManager.getConnection(databaseUrl);
        connectionsMap.put(Thread.currentThread(), connection);
        return connection;
    }

    public void closeConnection() throws SQLException {
        Connection thisconnection = connectionsMap.get(Thread.currentThread());
        if (thisconnection != null) {
            thisconnection.close();
            connectionsMap.remove(Thread.currentThread());
        }
    }

    public synchronized void startTransaction() throws SQLException {
        Connection connection = openConnection();
        connection.setAutoCommit(false);
    }

    public synchronized void commitTransaction() throws SQLException {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        thisConnection.commit();
        closeConnection();
    }

    public synchronized void cancelTransaction() throws SQLException {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        thisConnection.rollback();
        closeConnection();
    }

    public synchronized PreparedStatement prepareStatement(String statement) throws SQLException {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        if (thisConnection == null) {
            thisConnection = openConnection();
        }
        return thisConnection.prepareStatement(statement);
    }

    public synchronized CallableStatement prepareCall(String statement) throws SQLException {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        if (thisConnection == null) {
            thisConnection = openConnection();
        }
        return thisConnection.prepareCall(statement);
    }

}
