package com.estafet.bankx.camel.experimental;

import com.estafet.bankx.camel.integration.ServicesTest;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.InputStream;

/**
 * DBUnit external sources of information:<br/>
 * <ul>
 *     <li>Database Tests With DbUnit (Part 1) - http://www.marcphilipp.de/blog/2012/03/13/database-tests-with-dbunit-part-1/</li>
 * </ul>
 *
 * Created by Yordan Nalbantov.
 */
public class Tests {

    private static final String DATABASE_DRIVER_CLASS = "org.postgresql.Driver";
    private static final String DATABASE_CONNECTION_URL = "jdbc:postgresql://localhost:5432/ibanbd";
    private static final String DATABASE_USERNAME = "postgres";
    private static final String DATABASE_PASSWORD = "welcome1";

    private static IDataSet dataSetAccountInitial = null;
    private static IDataSet dataSetAccountExpected = null;

    private static IDatabaseTester databaseTester = null;


    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        InputStream accountInitialInputStream = ServicesTest.class.getResourceAsStream("/integration/dataset/db_initial.xml");
        dataSetAccountInitial = new XmlDataSet(accountInitialInputStream);

        InputStream accountExpectedInputStream = ServicesTest.class.getResourceAsStream("/integration/dataset/db_expected.xml");
        dataSetAccountExpected = new XmlDataSet(accountExpectedInputStream);

        databaseTester = new JdbcDatabaseTester(DATABASE_DRIVER_CLASS, DATABASE_CONNECTION_URL, DATABASE_USERNAME,
                DATABASE_PASSWORD);
        // Perform a DELETE_ALL operation followed by an INSERT operation.
        // Here is a documentation on different DatabaseOperation options.
        // http://dbunit.sourceforge.net/components.html#databaseoperation
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSetAccountInitial);
        databaseTester.onSetup();
    }

    @AfterClass
    public static void oneTimeTearDown() {

    }
}
