package com.postgresqltutorial;

public class Actor {
	 private String firstName;
	    private String lastName;

	    public Actor(String firstName, String lastName) {
	        this.firstName = firstName;
	        this.lastName = lastName;

	    }

	    public Actor() {
	    }

	    /**
	     * @return the firstName
	     */
	    public String getFirstName() {
	        return firstName;
	    }

	    /**
	     * @param firstName the firstName to set
	     */
	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    /**
	     * @return the lastName
	     */
	    public String getLastName() {
	        return lastName;
	    }

	    /**
	     * @param lastName the lastName to set
	     */
	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }
}
