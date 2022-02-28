import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Bomb extends JButton
{
	private boolean isClicked;
	private boolean isFlagable;
	private Game game;
	
	public Bomb(Game currentGame)
	{		
		game = currentGame;
		
		isClicked = false;
		isFlagable = false;
		
		addActionListener(new BombListener());
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
	
	public void showBomb()
	{
		setText("BOMB");
		
		setForeground(Color.RED);
	}
	
	private class BombListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(isFlagable && !isClicked)
			{
				//increment amount of flags
				
				setText("FLAG");
				setForeground(Color.ORANGE);
				
				game.makeAllUnflagable();
			}	
			else if(!isClicked)
			{
				//if text is "FLAG" --> decrease amount of flags
				
				//says it is now clicked
				click();
				
				showBomb();
				
				game.endGame();
			}
		}
	}
}