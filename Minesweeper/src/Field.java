import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class Field extends JButton
{
	private boolean isClicked;
	private boolean isFlagable;
	private boolean isFlagged;
	private Game game;
	private int index;
	
	public Field(Game currentGame, int givenIndex)
	{
		index = givenIndex;
		
		game = currentGame;
		
		isClicked = false;	
		isFlagable = false;
		isFlagged = false;
		
		addActionListener(new FieldListener());
	}
	
	public void makeFlagable()
	{
		isFlagable = true;
	}
	
	public void makeUnflagable()
	{
		isFlagable = false;
	}
	
	//leads to lack of possible action upon clicking
 	public void click()
	{
		isClicked = true;
	}

	public int findBombs()
	{
		return game.countBombs(index);
	}
	
	private class FieldListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(isFlagable && !isClicked)
			{
				game.moreFlags();
				
				setText("FLAG");
				setForeground(Color.ORANGE);
				
				game.makeAllUnflagable();
				
				isFlagged = true;
			}
			else if(!isClicked)
			{
				if (isFlagged)
					game.lessFlags();
				
				//makes button not clickable again
				click();
				
				setText(String.valueOf(findBombs()));
				
				setForeground(Color.BLACK);
								
				game.fieldCounter();
			}
		}
	}
}
