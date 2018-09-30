package database;

public  class Person {
public String userName;
public String password;
public String progress;
public String score;
public String NIP;

Person(String userName,String password, String progress, String score) {
        this.userName = userName;
		  this.password = password;
        this.progress = progress;
        this.score =score;
   }

public String getUserName() {
        return userName;
    }

public String getProgress() {
        return progress;
    }
    
public String getScore() {
        return score;
    }
	  
public String getNIP() {
        return NIP;
    }
}