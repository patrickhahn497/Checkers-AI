import java.util.ArrayList;
import java.util.Random;

public class ConnectFour {
	
	char[][] board;
	int[] full;
	int[] last_move;
	char turn;
	char AIcol;
	int max_col;
	int total = 0;
	int[][] directions = {{1,0}, {0, 1}, {1, 1}, {1, -1}};
	/**
	 * initializes ConnectFour and fills board with *, which indicates empty
	 */
	ConnectFour()
	{
		//7 columns and 6 rows. 
		//0 indicates the bottom most row and the left most column
		board = new char[7][6];
		full = new int[7];
		last_move = new int[2];
		for (int i = 0; i < 7; i++)
		{
			full[i] = 0;
			for (int j = 0; j < 6; j++)
			{
				board[i][j] = '*';
			}
		}
		
	}
	
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
	
	/**
	 * prints position i and j
	 * @param i
	 * @param j
	 */
	void printPos(int i, int j)
	{
		System.out.println("col: " + i +  " y: " + j);
		
	}

	
	/**
	 * @return turn
	 */
	char getTurn()
	{
		return turn;
	}
	
	/**
	 * 
	 * @param turn
	 * @return other player's turn
	 */
	char otherTurn(char turn)
	{
		if (turn == 'X')
			return 'O';
		else
			return 'X';
	}
	
	/**
	 * prints current state of board
	 */
	void print_board()
	{
		for (int j = 5; j > -1; j--)
		{
			for (int i = 0; i < 7; i++)
			{
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
	}
	

	
	/**
	 * sets the first turn as the input player
	 * @param c
	 */
	void set_first(char c)
	{
		if (c == 'X')
			turn = 'X';
		else
			turn = 'O';
	}
	
	
	/**
	 * @param i
	 * @param j
	 * @return whether piece is out of bounds or not
	 */
	boolean outOfBounds(int i, int j)
	{
		if (i > 6 || i <0 ||j > 5 || j <0)
			return true;
		
		return false;
	}
	
	/**
	 * @param i
	 * @return whether piece is out of bounds or not
	 */
	boolean outOfBounds(int i)
	{
		if (i <= 6 && i >=0 )
		{
			if (full[i] < 6)
				return false;
		}
		return true;
	}
	
	/**
	 * checks if the move is valid
	 * @param col
	 * @return whether move is valid
	 */
	boolean valid_move(int col)
	{
		if (!outOfBounds(col))
			return true;
		else
			return false;
		
	}
	
	/**
	 * inserts piece into appropriate place on board and changes turn
	 * @param col
	 * @param piece
	 */
	void sift_down(int col, char piece)
	{
		board[col][full[col]] = piece;
		last_move[0] = col;
		last_move[1] = full[col];
		full[col]++;
		change_turn();
		total++;
	}
	
	/**
	 * removes a piece from a column
	 * @param col
	 */
	void reverse(int col)
	{
		full[col]--;
		board[col][full[col]] = '*';
		change_turn();
		total--;

	}
	
	/**
	 * switches the current turn
	 */
	void change_turn()
	{
		if (turn == 'X')
			turn = 'Y';
		else
			turn = 'X';
	}
	
	
	/**
	 * 
	 * @return status of game G: still going, X: human win, O: AI win, D: draw
	 */
	char game_over()
	{
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				for (int x = 0; x < 4; x++)
				{
					char p = board[i][j];
					
					if(p != '*')
					{
						if (checkSides(directions[x][0], directions[x][1], i, j, p) >= 4)
						{
							return p;
						}
					}
				}	
			}
		}
		if (total >= 42)
			return 'D';
		return 'G';
		
	}
	

	
	/**
	 * checks how many of each piece are in one direction. 0 1 Horizantal, 1 0 vertical, 1 1  right up diag, 1 -1 3 left up diag
	 * @return
	 */
	int checkSides(int x_factor, int y_factor, int i, int j, char turn)
	{
		int most = 1;
		int factor = 1;
		
		//this one checks the normal way
		while ( !outOfBounds(i+factor*x_factor, j+factor*y_factor))
		{
			if (board[i+ factor*x_factor][j+ factor*y_factor] == turn)
			{
				most++;
				factor++;
			}
			else
				break;
		}
		//puts everything in the opposite direction
		factor = 1;
		while (  !outOfBounds(i-factor*x_factor, j-factor*y_factor)) //theres an out of bounds error here
		{
			if (board[i- factor*x_factor][j- factor*y_factor] == turn)
			{
				most++;
				factor++;
			}
			else
				break;
		}

		return most; 
	}
	

	/**
	 * @return -1 if no valid moves, ideal column number otherwise
	 */
	int choose_move()
	{
		int col = -1;
		int max = -99999;
		for (int i = 0; i < 7; i++)
		{
			if (valid_move(i))
			{
				sift_down(i, 'O');
				int val = search(3, 'X');
				reverse(i);
				if (val > max)
				{
					max = val;
					col = i;
				}
				
			}
		}
		return col;
	}
	
	/**
	 * recursively searches for best/worst move for the ai
	 * @param depth
	 * @param turn
	 * @return if turn is opponent, choose the worst move, if the turn is ai, then choose the best move
	 */
	int search(int depth, char turn)
	{
		if (depth == 0 || game_over() != 'G')
		{
			return depth*9000 +eval('O');
		}
		else
		{
			if (turn == 'O')
			{
				int max = -99999;
				for (int i = 0; i < 7; i++)
				{
					
					if (valid_move(i))
					{
						sift_down(i, turn);
						int val = search(depth-1,otherTurn( turn));
						reverse(i);
						if (val > max)
						{
							max = val;
						}	
					}
				}
				return max;
			}
			else
			{
				int min = 99999;
				for (int i = 0; i < 7; i++)
				{
					
					if (valid_move(i))
					{
						sift_down(i, turn);
						int val = search(depth-1, otherTurn(turn));
						reverse(i);
						if (val < min)
						{
							min = val;
						}	
					}
					
				}
				return min;	
			}
			
		}
	}

	/**
	 * @param turn
	 * @return score of the player of the input turn
	 */
	int player_score(char turn)
	{
		int score = 0;
		if (board[3][0] == turn)
			score += 2;
		else if (board[3][0] == otherTurn(turn))
			score -=2;
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				if (board[i][j]== turn)
				{
					for (int x = 0; x < 4; x++)
					{
						int inorder = checkSides(directions[x][0], directions[x][1], i, j, turn);
						score += inorder;
						if (inorder >= 4)
						{
							score += 9999;
						}
							
							
					}	
				}
			}
		}
		return score;
		
	}

	/**
	 * prints the score of both sides
	 */
	void print_score()
	{
		int X = 0;
		int O = 0;
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				if (board[i][j] == 'X')
				{
					X++;
				}
				else if (board[i][j]=='O')
				{
					O++;
				}
				
			}
		}
		print("X: " + X);
		print("O: " + O);
	}


	/**
	 * evaluates state of the board according to player
	 * @param turn
	 * @return current player score - other player score
	 */
	int eval(char turn)
	{
		if (turn == 'X')
			return player_score('X') - player_score('O');
		else
			return player_score('O') - player_score('X');	

	}

	
	

}
