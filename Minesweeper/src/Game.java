//import stuff for GUIs
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
//import Random
import java.util.Random;

//creates a board
public class Game extends JFrame
{
	public static void main(String[] args)
	{
		new Game();
	}
	
	//fields
	private final int BOMBS_AMOUNT = 7;
	private final int FIELD_AMOUNT = 45;
	private int flagsAmount = 0;
	private int fieldsToClick;
	private Bomb[] bombs;
	private Field[] fields;
	private JPanel flagPanel;

	
	//an array that holds fields and bombs
	private JComponent[] cells;
	
	//stores indices of bombs
	private int[] bombIndices;
	
	public Game()
	{
		flagPanel = new JPanel();
		
		flagPanel.setBackground(Color.LIGHT_GRAY);

		fieldsToClick = FIELD_AMOUNT;

		setLayout(new BorderLayout());
		
		buildBoard();
		
		buildFlag();
		
		showStatus();
		
		pack();
		
		//make window visible and close-able
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void showStatus()
	{
		JPanel statusPanel = new JPanel();
		
		statusPanel.setLayout(new GridLayout(2, 1));
		
		JLabel bombAmountText = new JLabel("Total amount of bombs: " + BOMBS_AMOUNT);
				
		statusPanel.add(bombAmountText);
		
		add(statusPanel, BorderLayout.NORTH);
	}
	
	public void moreFlags()
	{
		flagsAmount++;
		showStatus();
		
	}
	
	public void lessFlags()
	{
		flagsAmount--;
		showStatus();
	}
	
	public void makeAllFlagable()
	{
		for(int bombCounter = 0; bombCounter < bombs.length; bombCounter++)
		{
			bombs[bombCounter].makeFlagable();
		}
		for(int fieldCounter = 0; fieldCounter < fields.length; fieldCounter++)
		{
			fields[fieldCounter].makeFlagable();
		}
		
		flagPanel.setBackground(Color.ORANGE);
	}
	
	public void makeAllUnflagable()
	{
		for(int bombCounter = 0; bombCounter < bombs.length; bombCounter++)
		{
			bombs[bombCounter].makeUnflagable();
		}
		for(int fieldCounter = 0; fieldCounter < fields.length; fieldCounter++)
		{
			fields[fieldCounter].makeUnflagable();
		}
		
		flagPanel.setBackground(Color.LIGHT_GRAY);
	}
	
	public void buildFlag()
	{		
		JButton flagButton = new JButton();
		
		flagButton.setText("FLAG");
		
		flagButton.setFont(new Font("", Font.BOLD, 15));
		
		flagButton.setForeground(Color.ORANGE);
		
		//anonymous inner class
		flagButton.addActionListener(new ActionListener() 
		{	
			public void actionPerformed(ActionEvent e)
			{	
				makeAllFlagable();	
			}	
		});
		
		flagPanel.add(flagButton);
		
		add(flagPanel, BorderLayout.SOUTH);
	}
	
	public void buildBoard()
	{
		//variables used for randomly placing spaces, fields, and bombs
		Random randomNumber = new Random();
		int fieldCounter = FIELD_AMOUNT;
		int bombCounter = BOMBS_AMOUNT;
		
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(8, 8));
		
		//stores bombs and fields; used in endGame
		bombs = new Bomb[BOMBS_AMOUNT];
		fields = new Field[FIELD_AMOUNT];
		
		//stores indices of bombs; used to find bombs
		bombIndices = new int[BOMBS_AMOUNT];
		
		//decides order of the board
		cells = new JComponent[8*8];
		for(int counter = 0; counter < 8*8; counter++)
		{
			int location;
			
			//decides location of placing
			do 
			{
				location = randomNumber.nextInt(8*8);
			}
			while(cells[location] != null);
						
			if(bombCounter > 0)
			{
				--bombCounter;
				bombs[bombCounter] = new Bomb(this);
				cells[location] = bombs[bombCounter];
				
				//stores index of bomb
				bombIndices[bombCounter] = location;
			}
			else if(fieldCounter > 0)
			{
				--fieldCounter;
				fields[fieldCounter] = new Field(this, location);
				cells[location] = fields[fieldCounter];
			}
			else
			{
				cells[location] = new Space();
			}
		}
		
		//write a for loop to add each component to the boardPanel
		for(int counter = 0; counter < 8*8; counter++)
		{
			boardPanel.add(cells[counter]);
		}
		
		add(boardPanel, BorderLayout.CENTER);
	}
	
	public int countBombs(int index)
	{
		boolean hasNorth = true, hasSouth = true, hasEast = true, hasWest = true;
		
		int amountOfBombs = 0;
		
		int[] potentialIndices = new int[8];
		
		//initializes every potential index to -1 in case the field isn't available
		for(int counter = 0; counter < potentialIndices.length; counter++)
		{
			potentialIndices[counter] = -1;
		}
		
		//checks borders of the field
		if(index % 8 == 7)
			hasEast = false;
		if(index % 8 == 0)
			hasWest = false;
		if(index - 8 < 0)
			hasNorth = false;
		if(index + 8 > (8*8 - 1))
			hasSouth = false;
		
		//northern potential indices
		if(hasNorth)
		{
			if(hasWest)
				potentialIndices[0] = index - 9;
			
			potentialIndices[1] = index - 8;
			
			if(hasEast)
				potentialIndices[2] = index - 7;
		}
		
		//eastern and western potential indices
		if(hasWest)
			potentialIndices[3] = index - 1;
		if(hasEast)
			potentialIndices[4] = index + 1;
		
		//southern potential indices
		if(hasSouth)
		{
			if(hasWest)
				potentialIndices[5] = index + 7;
			
			potentialIndices[6] = index + 8;
			
			if(hasEast)
				potentialIndices[7] = index + 9;
		}
		
		for(int potentialIndex : potentialIndices)
		{
			if((potentialIndex >= 0) && (potentialIndex <= 8*8 -1))
			{
				for(int bombIndex : bombIndices)
				{
					if(potentialIndex == bombIndex)
					{
						amountOfBombs++;
					}
				}
			}
		}

		return amountOfBombs;
	}

	public void fieldCounter()
	{
		--fieldsToClick;
		
		if(fieldsToClick == 0)
		{
			endGame();
		}
	}

	public void endGame()
	{
		
		if(fieldsToClick == 0)
		{
			setBackground(Color.GREEN);

			JOptionPane.showMessageDialog(null, "Congradulations! You won");
		}
		else
		{
			setBackground(Color.RED);

			JOptionPane.showMessageDialog(null, "You lost...");
		}
		
		//makes everything not possible to click
		for(int bombCounter = 0; bombCounter < bombs.length; bombCounter++)
		{
			bombs[bombCounter].click();
			bombs[bombCounter].showBomb();
		}
		for(int fieldCounter = 0; fieldCounter < fields.length; fieldCounter++)
		{
			fields[fieldCounter].click();
		}
	}
}