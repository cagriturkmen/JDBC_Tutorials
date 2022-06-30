package com.postgresqltutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class App {
	/*
	The address of the PostgreSQL database server e.g., localhost
	The database name e.g., dvdrental
	The username and password of the account that you will use to connect to the database.
	 * */
	
	private final String url = "jdbc:postgresql://localhost/dvdrental";
	private final String user = "postgres";
	private final String password = "root";
	
	/*
 	To establish a connection to the PostgreSQL database server, you call the getConnection method of the DriverManager class. 
 	This method returns a Connection object.
	The following connect() method connects to the PostgreSQL database server and returns a Connection object.
*/
	public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
	
	public int getActorCount() {
        String SQL = "SELECT count(*) FROM actor";
        int count = 0;

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return count;
    }
	
	 /**
     * Get all rows in the actor table
     */
    public void getActors() {

        String SQL = "SELECT actor_id,first_name, last_name FROM actor";

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            // display actor information
            displayActor(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Display actor
     *
     * @param rs
     * @throws SQLException
     */
    private void displayActor(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(rs.getString("actor_id") + "\t"
                    + rs.getString("first_name") + "\t"
                    + rs.getString("last_name"));

        }
    }
    
    /**
     * Find actor by his/her ID
     *
     * @param actorID
     */
    
    /*
      	To query the database with parameters, you use the PreparedStatement object.

		First, you use the question mark (?) as the placeholder in the SQL statement. Then, you use methods of 
		the PreparedStatement object such as setInt, setString,… to pass the value to the placeholders.

		The following method allows you to find an actor by his/her id.
*/
    public void findActorByID(int actorID) {
        String SQL = "SELECT actor_id,first_name,last_name "
                + "FROM actor "
                + "WHERE actor_id = ?";

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, actorID);
            ResultSet rs = pstmt.executeQuery();
            displayActor(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public long insertActor(Actor actor) {
        String SQL = "INSERT INTO actor(first_name,last_name) "
                + "VALUES(?,?)";

        long id = 0;

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, actor.getFirstName());
            pstmt.setString(2, actor.getLastName());

            int affectedRows = pstmt.executeUpdate();
            // check the affected rows 
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }
    /**
     * insert multiple actors
     */
    public void insertActors(List<Actor> list) {
        String SQL = "INSERT INTO actor(first_name,last_name) "
                + "VALUES(?,?)";
        try (
                Connection conn = connect();
                PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;

            for (Actor actor : list) {
                statement.setString(1, actor.getFirstName());
                statement.setString(2, actor.getLastName());

                statement.addBatch();
                count++;
                // execute every 100 rows or less
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    /**
     * Update actor's last name based on actor's id
     *
     * @param id
     * @param lastName
     * @return the number of affected rows
     */
    public int updateLastName(int id, String lastName) {
        String SQL = "UPDATE actor "
                + "SET last_name = ? "
                + "WHERE actor_id = ?";

        int affectedrows = 0;

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, lastName);
            pstmt.setInt(2, id);

            affectedrows = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }
    public int deleteActor(int id) {
        String SQL = "DELETE FROM actor WHERE actor_id = ?";

        int affectedrows = 0;

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, id);

            affectedrows = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }


	public static void main(String[] args) {
		
		 App app = new App();
	      //  app.connect();
		
		 //int actorCount = app.getActorCount();
		//System.out.println(actorCount);
		 
		// app.getActors();
		 
		// app.findActorByID(200);
		
		 /*
		 Actor actor = new Actor("Tarık","Akan");
		 
		 long id = app.insertActor(actor);
		 */
		 /*
		  ArrayList<Actor> actorList = new ArrayList();
		  actorList.add(new Actor("",""));
	  	  actorList.add(new Actor("",""));
	  	  app.insertAcyors(actorList);
		  
		  * */
		 
	    //    app.updateLastName(200, "Climo");
		 
	        app.deleteActor(214);


		 
	}

}
