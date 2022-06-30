package com.postgresqltutorial.readfilewritetodb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Test {
	

	private final static String url = "jdbc:postgresql://localhost/people";
	private final static String user = "postgres";
	private final static String password = "root";

	public static void main(String[] args) throws ParseException {
				
		ArrayList<Person> personList =convertStringToPerson();

		System.out.println(personList.get(10));
		insertActors(personList);
		
		//someStreamPractices(personList);
		
		
	}
		public static ArrayList<String> createAStringPersonList() {
	        ArrayList<String> personListString = new ArrayList<>();

			try {
				BufferedReader br = new BufferedReader(new FileReader("Person.csv"));
			    // read until end of file
			    String line;

			    while ((line = br.readLine()) != null) {
			        personListString.add(line);
			    } 
			    personListString.remove(0);
			    //System.out.println(personListString.get(5));;
			    
			    // close the reader
			    br.close();
			    return personListString;

		     }

		     catch(Exception e) {
		        e.getStackTrace();
		     }
		    return personListString;

		}
		
		public static ArrayList<Person> convertStringToPerson() throws ParseException {
			
			String pattern = "MM/dd/yyyy";
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			
			ArrayList<Person> personList = new ArrayList<>();
			
			ArrayList<String> tempList =createAStringPersonList();
			StringTokenizer st;
			
			for (String line : tempList) {
				
				Person person = new Person();
				//Date birthday;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

				 st = new StringTokenizer(line,",");
				
					person.setId(Integer.parseInt(st.nextToken()));
					person.setFirstName(st.nextToken());
					person.setLastName(st.nextToken());
					person.setEmail(st.nextToken());
					person.setGender(st.nextToken());
					person.setBirthday(new Timestamp(df.parse(st.nextToken()).getTime()) );
					
					personList.add(person);
			}
			return personList;
			
		}
		
		
		/*
	 	To establish a connection to the PostgreSQL database server, you call the getConnection method of the DriverManager class. 
	 	This method returns a Connection object.
		The following connect() method connects to the PostgreSQL database server and returns a Connection object.
	*/
		public static Connection connect() {
	        Connection conn = null;
	        try {
	            conn = DriverManager.getConnection(url, user, password);
	            System.out.println("Connected to the PostgreSQL server successfully.");
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }

	        return conn;
	    }
		
		 public static void insertActors(List<Person> list) {
		        String SQL = "INSERT INTO persona(id,first_name,last_name,email,gender,birthday) VALUES (?,?,?,?,?,?)";
		        try (
		                Connection conn = connect();
		                PreparedStatement statement = conn.prepareStatement(SQL);) {
		            int count = 0;

		            for (Person person : list) {
		                statement.setInt(1, person.getId());
		                statement.setString(2, person.getFirstName());
		                statement.setString(3, person.getLastName());
		                statement.setString(4, person.getEmail());
		                statement.setString(5, person.getGender());
		                statement.setTimestamp(6, person.getBirthday());

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
		
		 public static void someStreamPractices(List<Person> people) {
		        Timestamp ts = Timestamp.valueOf("2004-06-30 00:00:00");  

			 List<Person> adultList =people.stream()
			 .filter((person) -> person.getBirthday().before(ts))
			 .collect(Collectors.toList())
			 ;
			 
			 System.out.println(adultList);
		 }
	
}
