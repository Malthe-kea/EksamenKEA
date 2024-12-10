//package org.example.eksamenkea.repository;
//
//import org.example.eksamenkea.model.Subproject;
//import org.example.eksamenkea.service.Errorhandling;
//import org.example.eksamenkea.util.ConnectionManager;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/TEST-DDL.sql") //Her loader vi altid vores DDL før der bliver kørt tests forfra.
//@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/TEST-DML.sql")//Her loader vi altid vores DML før der bliver kørt tests forfra
//
//class SubprojectRepositoryTest {
//    @Autowired
//    private SubprojectRepository subprojectRepository;
//
//    private int insertTestData(String sql) throws SQLException {
//        try (Connection connection = ConnectionManager.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//            preparedStatement.executeUpdate();
//
//            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    return generatedKeys.getInt(1);
//                } else {
//                    throw new SQLException("Failed to retrieve generated key.");
//                }
//            }
//        }
//
//    }
//
//    @Test
//    void getSubprojectBySubprojectId() throws SQLException, Errorhandling {
//        //Arrange
//        int newSubprojectId = insertTestData("INSERT INTO subproject (subproject_name, subproject_description, project_id) " +
//                "VALUES ('subTest', 'testofSub', 1)");
//
//        //act
//        Subproject subproject = subprojectRepository.getSubprojectBySubprojectId(newSubprojectId);
//
//        //assert
//        assertNotNull(subproject);//tjekker om subproject ik er null (eksistere
//        assertEquals(newSubprojectId, subproject.getSubprojectId());
//        assertEquals("subTest", subproject.getSubprojectName());
//
//    }
//
//    @Test
//    void updateSubproject() throws SQLException, Errorhandling {
//        //arrange
//        int subprojectId = insertTestData("INSERT INTO subproject(subproject_name, subproject_description, project_id) " +
//                "VALUES('updatedSubproject', 'updated description', 1 ) ");
//
//        //opret ny subprject med nye værdier
//        Subproject updatedSubproject = new Subproject(subprojectId, "updatedSubproject",
//                "updated description", 2);//ændret projekt_id
//
//        //act
//        subprojectRepository.updateSubproject(updatedSubproject);
//
//        //Assert
//        Subproject result = subprojectRepository.getSubprojectBySubprojectId(subprojectId);//henter den updaterede post fra database
//        assertNotNull(result);//tjek om den updaterede post eksisterer
//        assertEquals("updatedSubproject", result.getSubprojectName());
//        assertEquals("updated description", result.getSubprojectDescription());
//        assertEquals(2, result.getProjectId());//om den matcher den nye værdi
//    }
//}