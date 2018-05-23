import java.util.Scanner;
import java.util.Random;

public class Handler {
	
	static void print(char c)
	{
		System.out.println(c);
		
	}
	
	static void print(String s)
	{
		System.out.println(s);
		
	}
	
	static void print(int i)
	{
		System.out.println(i);
		
	}
	
	static void print(Boolean i)
	{
		System.out.println(i);
		
	}

	public static void main(String[] args) {
		// initializes game
		Scanner in = new Scanner(System.in);
		ConnectFour game = new ConnectFour();
		boolean notOver = true;
		char yes = '*';
		while (yes != 'X' && yes != 'O') 
		{
			System.out.println("Please enter who will go first: X for human, O for computer");
			yes =in.next().charAt(0);
		}
		game.set_first(yes);
		game.print_board();
		//runs game until gameover condition is met
		while (notOver)
		{
			char state = game.game_over();
			if (game.game_over() == 'G')
			{
				if (game.getTurn() == 'X')
				{
					int col = -1;
					do 
					{	
						System.out.println("Please enter a column from 0-6");
						if (in.hasNextInt())
							col = in.nextInt();
					} while (!game.valid_move(col) );	
					game.sift_down(col, 'X');
				}
				else
				{
					int move = game.choose_move();
					game.sift_down(move, 'O');
					print("AI Turn");
					print("AI move: " + move );
				}
				game.print_board();
			}
			else
			{
				notOver = false;
				if (state == 'D')
					System.out.println("It's a draw!");
				else if (state == 'X')
					System.out.println("Human wins!");
				else
					System.out.println("AI wins!");
				game.print_score();
			}
			
		}

		

	}

}
