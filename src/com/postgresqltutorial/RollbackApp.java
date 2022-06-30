/**
 * need to fix the db ALTER TABLE film_actor ALTER COLUMN actor_id TYPE INT;
 * ALTER TABLE film_actor ALTER COLUMN film_id TYPE INT;
 */
package com.postgresqltutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author postgresqltutorial.com
 */
public class RollbackApp {

    private final String url = "jdbc:postgresql://localhost/dvdrental";
    private final String user = "postgres";
    private final String password = "postgres";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     * @throws java.sql.SQLException
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Close a AutoCloseable object
     *
     * @param closable
     */
    private RollbackApp close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    /**
     * insert an actor and assign him to a specific film
     *
     * @param actor
     * @param filmId
     */
    public void addActorAndAssignFilm(Actor actor, int filmId) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;

        // insert an actor into the actor table
        String SQLInsertActor = "INSERT INTO actor(first_name,last_name) "
                + "VALUES(?,?)";

        // assign actor to a film
        String SQLAssignActor = "INSERT INTO film_actor(actor_id,film_id) "
                + "VALUES(?,?)";

        int actorId = 0;
        try {
            // connect to the database
            conn = connect();
            conn.setAutoCommit(false);

            // add actor
            pstmt = conn.prepareStatement(SQLInsertActor,
                    Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, actor.getFirstName());
            pstmt.setString(2, actor.getLastName());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // get actor id
                rs = pstmt.getGeneratedKeys();

                if (rs.next()) {
                    actorId = rs.getInt(1);
                    if (actorId > 0) {
                        pstmt2 = conn.prepareStatement(SQLAssignActor);
                        pstmt2.setInt(1, actorId);
                        pstmt2.setInt(2, filmId);
                        pstmt2.executeUpdate();
                    }
                }
            } else {
                // rollback the transaction if the insert failed
                conn.rollback();
            }

            // commit the transaction if everything is fine
            conn.commit();

            System.out.println(
                    String.format("The actor was inserted with id %d and "
                            + "assigned to the film %d", actorId, filmId));

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            // roll back the transaction
            System.out.println("Rolling back the transaction...");
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } finally {
            this.close(rs)
                    .close(pstmt)
                    .close(pstmt2)
                    .close(conn);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RollbackApp app = new RollbackApp();
        // OK transaction
         app.addActorAndAssignFilm(new Actor("Bruce", "Lee"), 1);
        
        // Failed transaction
        // app.addActorAndAssignFilm(new Actor("Lily", "Lee"), 9999);
    }
}