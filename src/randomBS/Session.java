package randomBS;

import database.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Session {
	int ID;
	User user;
	
	public Session() {
		start();
	}
	
	public void start() {
		boolean validChoice;
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome To Avocado\n");
		int i;
		do {
			System.out.println("Would you like to:");
			System.out.println("     1. Login");
			System.out.println("     2. Sign up");
			i = sc.nextInt();
			if (i!= 1 && i != 2) {
				validChoice = false;
				System.out.println("Invalid choice made please try again.\n");
			}
			else {
				validChoice = true;
			}
		}
		while(!validChoice);
		
		if(i == 1) {
			login();
		}
		else {
			signup();
		}
		
		sc.close();
	}
	
	public void login() {
		Scanner sc = new Scanner(System.in);
		boolean validLogin;
		String username;
		
		Database database = new Database();
		
		do {
			System.out.print("Please Enter Your Username: ");
			
			username = sc.nextLine();
			
			System.out.print("Please Enter Your Password: ");
			
			String password = sc.nextLine();
			
			if(database.userExsits(username)) {
				if(database.validLogin(username, password)) {
					validLogin = true;
				}
				else {
					System.out.println("Incorrect Username or Password Please Try Again.\n");
					validLogin = false;
				}
			}
			else {
				System.out.println("Invalid Username or Password Provided Please Try Again.\n");
				validLogin = false;
			}
			
		}while(!validLogin);
		
		sc.close();
		this.user = database.loadUser(username);
	}
	
	//finish after constructor for user
	public void signup() {
		Scanner sc = new Scanner(System.in);
		Database database = new Database();
		boolean validUsernameChosen;
		
		//username
		String username;
		System.out.println("Welcome to the Avocado account setup wizzard");
		
		do {
			System.out.print("Choose a username: ");
			
			username = sc.nextLine();
			
			if(database.userExsits(username)) {
				System.out.println("Invalid username Chosen Please Try again.\n");
				validUsernameChosen = false;
			}
			else {
				validUsernameChosen = true;
			}
			
		}while(!validUsernameChosen);
		
		User user = new User();
		
		this.user = user;
		
		database.createUser(user);
		
		homepage();
	}
	
	public void homepage() {
		Scanner sc = new Scanner(System.in);
		boolean validChoice;
		int i;
		
		System.out.println("Welcome back " + this.user.getUsername() + ".\n");
		System.out.println("Your Score is: " + this.user.getScore());
		
		do {
			System.out.println("Would you like to:");
			System.out.println("     1. View Friends");
			System.out.println("     2. Update Information");
			
			if(this.user.optedIn())
				System.out.println("     3. View Leaderboard");
			
			i = sc.nextInt();
			
			if(i != 1 && i != 2 && (i != 3 && this.user.optedIn())) {
				System.out.println("Invalid Choice Made.");
				validChoice = false;
			}
			else {
				validChoice = true;
			}
		}while(!validChoice);
		
		sc.close();
		
		switch(i) {
			case 1:
				friends();
			case 2:
				update();
			case 3:
				leaderboard();
		}
	}
	
	public void friends() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Welcome to your friends page " + this.user.getUsername() + ".\n");
		System.out.println("Your friend ID is: " + this.user.getFriendCode() + "\n");
		
		ArrayList<NotCurrentUser> friends =  this.user.getFriendLeaderboard();
		ArrayList<String> suggestedFriends = this.user.getSuggested(5);
		
		System.out.println("Your Friend Leaderboard\n");
		
		for(int i=0; i<friends.size(); i++) {
			NotCurrentUser friend = friends.get(i);
			System.out.println((i+1) + ". " + friend.getUsername() + " " + friend.getScore());
		}
		
		System.out.println("\nSome People You May Know\n");
		
		for (String i : suggestedFriends) {
			System.out.println(i);
		}
		
		System.out.print("Press Enter to go back.");
		sc.nextLine();
		homepage();
	}
	
	public void leaderboard() {
		Scanner sc = new Scanner(System.in);
		Database database = new Database();
		int rank = this.user.getRank();
		
		System.out.println("Welcome to your leaderboard page " + this.user.getUsername() + ".\n");
		System.out.println("Your leaderboard rank is " + rank + ".\n");
		
		System.out.println("Leaderboard Top 5\n");
		
		for(int i=1; i<6; i++) {
			NotCurrentUser user = database.getLeaderboard(i);
			if (!user.getUsername().equals(""))
				System.out.print(user.getUsername() + " " + user.getScore());
		}
		
		System.out.println("\nLocal Leaderboard\n");
		
		for(int i = (rank-2); i < (rank+2); i++) {
			NotCurrentUser user = database.getLeaderboard(i);
			if (!user.getUsername().equals(""))
				System.out.print(user.getUsername() + " " + user.getScore());
		}
		
		System.out.print("Press Enter to go back.");
		sc.nextLine();
		homepage();
	}
	
	public void update() {
		Scanner sc = new Scanner(System.in);
		Database database = new Database();
		
		System.out.println("Welcome to your information update page " + this.user.getUsername() + ".\n");
		
		System.out.println("Your Recorded Current debt is: " + this.user.getCurrentDebt());
		
		if(this.user.optedIn())
			System.out.println("You are currently opted into the leaderboard.\n");
		else
			System.out.println("You are currently not opted into the leaderboard.\n");
		
		boolean valid;
		int choice;
		
		do {
			System.out.println("Would you like to:");
			System.out.println("     1. Go back to the homepage.");
			System.out.println("     2. Update your current debt.");
			System.out.println("     3. Change your leaderboard opt-in status.");
			choice = sc.nextInt();
			
			if(choice != 1 && choice != 2 && choice !=3) {
				System.out.println("Invalid selection please try again");
				valid = false;
			}
			else
				valid = true;
		}while(!valid);
		
		switch(choice) {
			case 1:
				homepage();
				break;
			case 2:
				debtupdate();
				database.updateDatabase(user);
				update();
				break;
			case 3:
				if(this.user.optedIn())
					this.user.optOut();
				else
					this.user.optIn();
				database.updateDatabase(this.user);
				update();
				break;
		}
			
		database.updateDatabase(this.user);
	}
	
	public void debtupdate() {
		boolean valid;
		double newDebt = 0.0;
		do {			
			try {
				Scanner sc = new Scanner(System.in);
				System.out.print("Please enter your new debt: ");
				newDebt = sc.nextDouble();
				valid = true;
			}
			catch (Exception e) {
				System.out.println("Invalid input try again.");
				valid = false;
			}
		}while(!valid);
		this.user.setCurrentDebt(newDebt);
	}
	
	public static void main(String[] args) {
		//Dakota test area
		
		Session test = new Session();
		
		//Andrew test area
		
		//Erfan test area
		
		//Daniel test area
	}
}
