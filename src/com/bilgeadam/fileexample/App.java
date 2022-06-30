package com.bilgeadam.fileexample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
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

public class App {

	private final static String url = "jdbc:postgresql://localhost/people";
	private final static String user = "postgres";
	private final static String password = "root";
	
	public static void main(String[] args) throws ParseException {
		
		//createAStringPersonList();
		ArrayList<Person> personList = convertStringToPerson();
		
		insertPeople(personList);
		
		
	}
	
	public static ArrayList<String> createAStringPersonList() {
		
		ArrayList<String> personListString = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("Person.csv"))){
			
			String line;
			
			while((line = br.readLine()) != null) {
				personListString.add(line);
			}
			personListString.remove(0);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return personListString;
	}
	
	public static ArrayList<Person> convertStringToPerson() throws ParseException {
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		
		ArrayList<Person> personList = new ArrayList<>();
		
		ArrayList<String> tempList = createAStringPersonList();
		
		StringTokenizer st;
		
		for (String line : tempList) {
			
			Person person = new Person();
			
			st = new StringTokenizer(line,",");
			
			person.setId(Integer.parseInt(st.nextToken()));
			person.setFirstName(st.nextToken());
			person.setLastName(st.nextToken());
			person.setEmail(st.nextToken());
			person.setGender(st.nextToken());
			person.setBirthday(new Timestamp(df.parse(st.nextToken()).getTime()));
			
			personList.add(person);
			
		}
		return personList;
	}
	
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
	
	public static void insertPeople(List<Person> list) {
		
		String sql = "INSERT INTO persona(id,first_name,last_name,email,gender,birthday) VALUES (?,?,?,?,?,?)";
		
		try(Connection conn = connect();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			int count = 0;
			for (Person person : list) {
				stmt.setInt(1, person.getId());
				stmt.setString(2, person.getFirstName());
                stmt.setString(3, person.getLastName());
                stmt.setString(4, person.getEmail());
                stmt.setString(5, person.getGender());
                stmt.setTimestamp(6, person.getBirthday());
                
                stmt.addBatch();
                count++;
                if(count % 100 == 0 || count== list.size()) {
                	
                	stmt.executeBatch();
                }
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
