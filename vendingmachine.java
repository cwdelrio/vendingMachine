/*****************************************
*	This program emulates a		 *
*	vending Machine.		 *
*					 *
*	by Carlos Del Rio		 *
*****************************************/
import java.util.Scanner;	//import Scanner
import java.text.*;		//import Decimal format
public class vendingmachine //start main
{
	public static void main(String args[])throws NumberFormatException
	{	
		Boolean startover = true;	
		do
		{
			Item[][] items=
			{
			{new Item("Milky Way", 1.00, 566), new Item("Fruit Snack", 1.50, 592), new Item("Beef Jerky", 2.50, 544)},
			{new Item("Mars Bar", 1.00, 563), new Item("Milk Duds", 1.25, 521), new Item("Starburst", .75, 569)},
			{new Item("Bubblicious", .75, 222), new Item("Mike and Ike", .75, 299), new Item("Red Hots", .75, 233)},
			{new Item("Cup Cakes", 1.50, 513), new Item("Mini Oreos ", 2.25, 509), new Item("Gummy Worms", 1.75, 555)}
			};
			DecimalFormat money = new DecimalFormat("$"+"0.00"); //dollar format
			Money mymoney = new Money(); //create money object to track user money
			Boolean legal = true;
			Boolean legaly = true;
			Scanner scan = new Scanner(System.in);		
			int select; //selection int	       	 		      		
			displayMachine();
			if(startover)//boolean to change begining statement
				legaly = getAnswer("\n\nWOULD YOU LIKE TO MAKE A PURCHASE? ");//begins purchase if requested
			else
				legaly = false;
			if(!legaly) //begins purchase because user already said yes to startover
			{
				mymoney = Money.takeMoney(mymoney);   //let user choose how much money to use
				displayMachine();
				System.out.print("       YOU HAVE "+money.format(mymoney.total)+"\n"); //display money entered
				operateMachine(items, mymoney);
			}
				
		System.out.print("****************************************\n");
		System.out.print("*       Thank you for visiting         *\n");
		System.out.print("****************************************\n");
		startover = getAnswer("\nWOULD YOU LIKE TO MAKE ANOTHER PURCHASE? ");
		}
		while(!startover);
	}//end main
	public static void operateMachine(Item[][] items, Money mymoney)
	{
	int select;
	Boolean legal = true;
	Boolean ispurchase = true;
	select = chooseItem(items); //inputs and checks validity of item
	for(int i = 0; i < items.length; i++)   //processing money
	{
		for(int j = 0; j < items[0].length; j++)
		{
			if(select == items[i][j].number)
			{
				if(mymoney.total<items[i][j].price)//if money is not enough 
					do			   //allows user to enter more or quit
					{
					if(mymoney.total<items[i][j].price)
						legal = getAnswer("\nYOU DO NOT HAVE ENOUGH MONEY FOR THIS\nSELECTION WOULD YOU LIKE TO INSERT MORE? ");
					if(!legal)
						mymoney = Money.takeMoney(mymoney);
					else if(mymoney.total<items[i][j].price && legal)
					{
						legal = true;
						ispurchase = false;
					}
					if(mymoney.total>=items[i][j].price)
					{
						legal = true;
						ispurchase = true;
					}
					}	
					while(!legal);
					if(mymoney.total == items[i][j].price)//caculates money-price
					{
						mymoney.total = 0.00;
						ispurchase = true;
					}
					else if(mymoney.total>items[i][j].price)
					{
						double f = mymoney.total - items[i][j].price;//arbitrary variables (>'.'<) lazy 
						double g = roundDecimal(f);
						mymoney.total = g;
						ispurchase = true;
					}
					printRecipt(items[i][j], mymoney, ispurchase);//prints final reciept
				}
			}
		}
	}
	public static double roundDecimal(double number) //strange error when subtracting items.price from mymoney.total
	{						 //in some cases with multiple .## variables would return .000000001
        DecimalFormat rounded = new DecimalFormat("#.##");//remedied by rounding
	return Double.valueOf(rounded.format(number));
	}
	public static void printRecipt(Item myitem, Money mymoney, Boolean ispurchase)//prints a recipt of chosen item and change
	{
		String finalitem = "                ";				      //calls money.changeCalculator to do change
		String finalitem1;
		int itemindex;
		DecimalFormat money = new DecimalFormat("$"+"0.00"); //dollar format
		System.out.println("****************************************");
		System.out.println("*                                      *");
		System.out.println("*	    Thank You For Using        *");
		System.out.println("*   Del Rio's Delightfully Delicious   *");
		System.out.println("*   **(^.^) Depot of Deserts (^.^)**   *");
	        System.out.println("*     (   )                  (   )     *");
		System.out.println("*                                      *");
		System.out.println("*       Your Change is:                *");
		System.out.println("*  		"+money.format(mymoney.total)+"                  *");
		System.out.println("*  	                               *");
		mymoney = Money.changeCalculator(mymoney);
		System.out.println("*  	      You get:                 *");
		System.out.println("*  	    "+mymoney.quarter+" Quarters                **");
		System.out.println("*  	    "+mymoney.dime+" Dimes                    *");		
		System.out.println("*  	    "+mymoney.nickle+" Nickles                  *");
		System.out.println("*  	    "+mymoney.penny+" Pennies                  *");
		finalitem1 = finalitem.substring(myitem.name.length(), finalitem.length());
		String finalitem2 = myitem.name+finalitem1;
		if(ispurchase)
			{
			System.out.println("*  	Enjoy Your "+finalitem2+"    *");
			System.out.print("****************************************\n");
			}
		else
			System.out.println("*                                      *");
		
	}
	public static Boolean getAnswer(String quest) //asks a givin question and returns a boolean, false for yes, true for no
	{					      //for some parts not usable due to boolean use
		Scanner scan = new Scanner(System.in);
		Boolean legaly = true;
		Boolean legal = true;
		String ynchoice;
		
		do
		{
			if(!legaly)
				invalidType();
			else
				System.out.print(quest);
			ynchoice = scan.nextLine();
			ynchoice = ynchoice.toUpperCase();
			legaly = checkStringletter(ynchoice, "YES", "NO");
		}
		while(!legaly);
		legal = checkYesNo(ynchoice);
		return legal;
	}
	public static int chooseItem(Item[][] items)//prompts and checks if a selection as string, checks if in machine, and parses to int if so
	{
		Scanner scan = new Scanner(System.in);
		Boolean legal = true;
		String selection;
		int select = 0;
		do
		{
			if(!legal)
				invalidType();
			System.out.print("\nSelection #");
			selection = scan.nextLine(); 
			legal = checkSelection(selection);
			if(legal)
				{
				select = Integer.parseInt(selection);	
				legal = searchMachine(items, select);
				}
		}
		while(!legal);
		return select;
	}
	public static Boolean checkYesNo(String chkme) //checks given string for yes or no returns false for yes true for no
	{						
		char ch;
		String yes = "YES";
		String no = "NO";
		if(chkme.equalsIgnoreCase(yes))
			return false;
		else if(chkme.equalsIgnoreCase(no))
			return true;
		else if(chkme.charAt(0) == yes.charAt(0))
			return false;
		else if (chkme.charAt(0) == no.charAt(0))
			return true;
		else return false;
		
	}
	public static void invalidType()//prints invalid message
	{
	System.out.print("INVALID INPUT PLEASE TRY AGAIN\n");
	}
	public static Boolean checkStringletter(String chkme, String compme, String compme2) //checks to ensure a string is only letters
	{
		char ch;
		if(chkme.isEmpty())
			return false;
		else if(chkme.equalsIgnoreCase(compme) || chkme.equalsIgnoreCase(compme2))
			return true;
		else if(chkme.charAt(0)== compme.charAt(0)) 
		{
			for(int i = 0; i < chkme.length(); i++)
			{
				ch = chkme.charAt(i);
				if(!(Character.isLetter(ch)))
					return false;
			}
		}
		else if(chkme.charAt(0) == compme2.charAt(0))
		{
			for(int i = 0; i < chkme.length(); i++)
			{
				ch = chkme.charAt(i);
				if(!(Character.isLetter(ch)))
					return false;
			}
		}
		else
			return false;
		return true;
	}	
	public static Boolean checkStringNumber(String chkme, String compme, String compme2) //checks to ensure a string is only a number 
	{										     //or dollar formated value
		char ch;
		if(chkme.isEmpty())
			return false;
		else if(chkme.equalsIgnoreCase(compme) || chkme.equalsIgnoreCase(compme2))
			return true;
		else if((chkme.charAt(0)) == (compme.charAt(0)) ||(chkme.charAt(0)) == (compme2.charAt(0))) 
		{
			for(int i = 0; i < chkme.length(); i++)
			{
				ch = chkme.charAt(i);
				if(!(Character.isDigit(ch)))
					return false;
			}
		return true;
		}
		else if((chkme.charAt(0)) == '$'||(chkme.charAt(0)) == '.' || (chkme.charAt(0)) == '$'||(chkme.charAt(0)) == '.')
		{
			for(int i = 1; i < chkme.length(); i++)
			{
				ch = chkme.charAt(i);
				if(!(Character.isDigit(ch)))
					return false;
			}
			chkme = chkme.substring(1,chkme.length());
		}
		else
		return false;		
		return true;
	}
	public static Boolean checkSelection(String select)//checks selection to make sure it is same as machine//checks selection to see if number
	{
		if(select.isEmpty())										//created specificly for chooseItem
			return false;
		for(int i = 0; i < select.length(); i++)
		{
			char ch = select.charAt(i);
			if(!(Character.isDigit(ch)))
				return false;
		}
		return true;
	}	
	public static Boolean searchMachine(Item[][] items, int number1)// checks if choice exists in array of Item[][]
	{
		for( int k = 0;k < items.length; k++)
		{
			for(int l = 0; l < items[0].length; l++)
			{
				if(number1 == items[k][l].number)
					{
					return true;
					}
			}
		}
		return false;   //choices values are default(would use Boolean but need Item)
	}
	public static void displayMachine()  //Display Vending Machine
	{
	Item[][] displayitems=			//create separate array for displaying items in format 
	{
	{new Item("|    Milky Way", 1.00, 566), new Item("   Fruit Snack", 1.50, 592), new Item("   Beef Jerky\t|", 2.50, 544)},
	{new Item("|    Mars Bar", 1.00, 563), new Item("   Milk Duds", 1.25, 521), new Item("     Starburst\t|", .75, 569)},
	{new Item("|   Bubblicious", .75, 222), new Item("  Mike and Ike", .75, 299), new Item("    Red Hots\t|", .75, 233)},
	{new Item("|    Cup Cakes", 1.50, 513), new Item("    Mini Oreos ", 2.25, 509), new Item("  Gummy Worms\t|", 1.75, 555)}
	};
	System.out.print("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n");;
	System.out.print("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n");
	System.out.print("|    .------------------------------------.     ||                   |\n");
	System.out.print("|    | Del Rio's Delightfully Delicious   |     ||     __________    |\n");
	System.out.print("|    | **(^.^) Depot of Deserts (^.^)**   |     ||    /         /|   |\n");
	System.out.print("|    |   (   )                  (   )     |     ||   |----------||   |\n");
	System.out.print("|    *------------------------------------*     ||   |::::::::::||   |\n");
	System.out.print("|     ************************************      ||   |----------|/   |\n");
	System.out.print("|    **   Decide which desert you like   **     ||   Insert Bills    |\n"); 
	System.out.print("|   ***      Insert Your Money           ***    ||Only $1 or $5 Bills|\n");
	System.out.print("|    ** Then Enter Your Selection Below! **     ||     Accepted      |\n");
	System.out.print("|     ************************************      ||||||||||||||||||||||\n");
		int time = 1;
		for(int i = 0;i < displayitems.length; i++)
		{
			System.out.print("|");
			if(time == 1)
				{
				System.out.print("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
				time++;
				}
			System.out.print("\n|\t\t\t\t\t\t|\n");
			for(int j = 0;j < displayitems[0].length; j++)
			{
				displayitems[i][j].printName();
			}
			System.out.print("\n|");
			for(int j = 0;j < displayitems[0].length; j++)
			{
				
				displayitems[i][j].printPrice();
			}
			System.out.print("|\n|");
			for(int j = 0; j < displayitems[0].length; j++)
			{
				displayitems[i][j].printNumber();
			}
		}
		time = 1;
		System.out.print("|\n|\t\t\t\t\t\t|\n");
	System.out.print("|||||||||||||||||||||||||||||||||||||||||||||||||\n");
	System.out.print("|\t\t\t\t\t\t|\n");
	System.out.print("|    .-----------------------------------.      |\n");
	System.out.print("|    | |                                 |      |\n");
	System.out.print("|    | |                                 |      |\n");
	System.out.print("|    | |                                 |      |\n");
	System.out.print("|    | /---------------------------------|      |\n");
	System.out.print("|    |/----------------------------------|      |\n");
	System.out.print("|    *-----------------------------------*      |\n");
	System.out.print("|                                               |\n");
	System.out.print("|||||||||||||||||||||||||||||||||||||||||||||||||\n");
	System.out.print("|||||||\t\t\t\t\t  |||||||\n");
	System.out.print(" ||||| \t\t\t\t\t   ||||| \n");
	System.out.print(" _|||_ \t\t\t\t\t   _|||_ \n");
	System.out.print("|_____|\t\t\t\t\t  |_____|\n");
	}
}//end main		
class Item	//Item Object
{
	DecimalFormat money = new DecimalFormat("$"+"0.00"); //dollar format
	String name ;		//declaring object variables
	double price ;
	int number ;
	public Item(String x, double y, int z) //set constructors
	{
		name= x;
		price = y;
		number = z;
	}
	public void printName() //print perameters for name printing
	{
		System.out.print(name + "\t");
	}
	public void printPrice()//print perameters for price printing
	{
		System.out.print("      "+money.format(price) + "\t");
	}
	public void printNumber()//print perameters for number printing
	{
		System.out.print("      #"+number + "\t");
	}
}
class Money //creates money object for calculation and use in change calc
{
	public int dollar1 = 0;
	public int dollar5 = 0;
	public int quarter = 0;
	public int nickle = 0;
	public int dime = 0;
	public int penny = 0;
	public double total = 0.0;
	public static Money takeMoney(Money mymoney)// allowes user to insert money one piece at a time lik
	{                                           //a real vending machine.
		Scanner scan = new Scanner(System.in);
		Scanner scan1 = new Scanner(System.in);
		DecimalFormat money = new DecimalFormat("$"+"0.00"); //dollar format
		String type = " ";
		String choice = " ";
		String selection = " ";
		Money thismoney = mymoney;
		Boolean legal = true;
		Boolean legaly = true;
		Boolean onetime = true;
		char ch = ' ';
		char bill = 'B';
		char coin = 'C';
		do
		{
			if(!legal)
				vendingmachine.invalidType();
			System.out.print("\nWould you like to use Bills or Coins? ");
			selection = scan1.nextLine();
			selection = selection.toUpperCase();
			legal = vendingmachine.checkStringletter(selection, "BILLS", "COINS");
		}
		while(!legal);
		do   //bill checking and setting
		{
			if((selection.charAt(0)) == bill)
				{
					selection = "Bills";
					do
					{
						if(!legaly)
							vendingmachine.invalidType();
						System.out.print("\nWould you like to use a one($1) or a five($5): ");
						type = scan.nextLine();
						type = type.toUpperCase();
						legaly = vendingmachine.checkStringletter(type, "ONE", "FIVE");
						if(!legaly)
							legaly = vendingmachine.checkStringNumber(type, "1", "5");
					}
					while(!legaly);
					if(type.equalsIgnoreCase("one")||type.equalsIgnoreCase("1")||type.charAt(0) == 'O')
						{
						thismoney.total += 1;
						thismoney.dollar1++;
						}
					else if(type.equalsIgnoreCase("five")||type.equalsIgnoreCase("5")||type.charAt(0) == 'F')
						{
						thismoney.total += 5;
						thismoney.dollar5++;
						}
				}
			if((selection.charAt(0)) == coin)//coin checking and setting
				{
					selection = "Coins";
					do
					{
					if(!legaly)
						vendingmachine.invalidType();
					System.out.print("\nWould you like to use a\nQuarter(.25)\n"
							+"Dime(.10)\nNickle(.05)\nPenny(.01)? ");
					type = scan.nextLine();
					type = type.toUpperCase();
					legaly = vendingmachine.checkStringletter(type, "QUARTER", "DIME");
					if(!legaly)
						legaly = vendingmachine.checkStringletter(type, "NICKLE", "PENNY");
					if(!legaly)
						{
						legaly = vendingmachine.checkStringNumber(type, ".25", ".10");
						if(!legaly)
							legaly = vendingmachine.checkStringNumber(type, ".05", ".01");
						}
					}
					while(!legaly);
					if(type.equalsIgnoreCase("QUARTER")||type.equalsIgnoreCase(".25")||type.charAt(0) == 'Q')
						{
						thismoney.total += .25;
						thismoney.quarter++;
						}
					else if(type.equalsIgnoreCase("DIME")||type.equalsIgnoreCase(".10")||type.charAt(0) == 'D')
						{
						thismoney.total += .10;
						thismoney.dime++;
						}
					else if(type.equalsIgnoreCase("NICKLE")||type.equalsIgnoreCase(".05")||type.charAt(0) == 'N')
						{
						thismoney.total += .05;
						thismoney.nickle++;
						}
					else if(type.equalsIgnoreCase("PENNY")||type.equalsIgnoreCase(".01")||type.charAt(0) == 'P')
						{
						thismoney.total += .01;
						thismoney.penny++;
						}
				}
			do//choose to use same type again
	 		{
				if(!legaly)
					vendingmachine.invalidType();	
				System.out.print("\n         YOU CURRENTLY HAVE: "+money.format(thismoney.total)+"\n");
				System.out.print("\nWould you like to use any more "+selection.toLowerCase()+"? ");
				choice = scan.nextLine();
				choice = choice.toUpperCase();
				legaly = vendingmachine.checkStringletter(choice, "YES", "NO");
				
			}
			while(!legaly);
			legal = vendingmachine.checkYesNo(choice);
			if (onetime && legal)//switch types
				{
				if((selection.charAt(0)) == bill)
					{
					selection = "Coins";
					onetime = false;
					}
				else if((selection.charAt(0)) == coin)
					{
					selection = "Bills";
					onetime = false;
					}
				do
		 		{
					if(!legaly)//choose to use opposite type
						vendingmachine.invalidType();
					System.out.print("\nWould you like to use any " + selection.toLowerCase() +" ?" );
					choice = scan.nextLine();
					choice = choice.toUpperCase();
					legaly = vendingmachine.checkStringletter(choice, "YES", "NO");													
					legal = vendingmachine.checkYesNo(choice);
				}
				while(!legaly);	
				}
		}
		while(!legal);//reset variables for next use
		type = " ";
		choice = " ";
		selection = " ";
		legal = true;
		legaly = true;
		ch = ' ';
		bill = 'B';
		coin = 'C';
		return thismoney;
	}
	public static Money changeCalculator(Money mymoney)//calculate change in to highest
	{						   //most convienient denomination
		Money thismoney = new Money();
		thismoney.total = (mymoney.total*100);
		if(thismoney.total == 0)
			{
			return thismoney;
			}
		else
		{
		do
		{
			if(thismoney.total%25 == 0)
			{
				thismoney.quarter++;
				thismoney.total -= 25;
			}
			else if(thismoney.total%10 ==0)
			{
				thismoney.dime++;
				thismoney.total-=10;
			}
			else if(thismoney.total%5 == 0)
			{
				thismoney.nickle++;
				thismoney.total -=5;
			}
			else if(thismoney.total%1 == 0)
			{
				thismoney.penny++;
				thismoney.total -= 1;
			}
		}
		while(thismoney.total>0);
		return thismoney;
		}
	}
}		
