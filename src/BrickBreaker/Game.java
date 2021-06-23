package BrickBreaker;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Toolkit;

public class Game extends JPanel implements KeyListener, ActionListener{
	private boolean play=false;
	private int score=0;
	private int lives=3;
	private Timer time;
	private int numberBricks=28;
	private int delay=5;
	private int slider=310;
	private int col=7;
	private int row=4;
	private int lengthSlider=100;
	//bricks
	private int bricks[][];
	private int brickWidth=90;
	private int brickHeight=60;
	//position of ball
	private int pos_ballx=120;
	private int poss_bally=350;
	private Boolean lost=false;
	private Boolean won=false;
	private double ball_x_dir=-2;
	private double ball_y_dir=-3;
	private static Color SEA= new Color(53,203,215);
	private static Color SEAWEED= new Color(25,133,27);
	private static Color ABYSS= new Color(17,74,105);

	public Game() {
		bricks=new int [row][col];
		Arrays.fill(bricks[0], 2);//More resistant blocks, meant to be hit twice
		for (int i=1;i<row;i++) {
			Arrays.fill(bricks[i],1);
		}
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(true);
		time = new Timer(delay,this);
		time.start();
	}
	

	public void paint(Graphics g) {
		g.setColor(ABYSS);
		g.fillRect(0,0,697,697);
		drawbricks((Graphics2D) g);
		//slider
		g.setColor(SEAWEED);
		g.fillRect(slider, 600, lengthSlider, 8);
		//ball
		g.setColor(Color.white);
		g.fillOval(pos_ballx, poss_bally, 20, 20);
		g.setColor(Color.black);
		//Lives
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Lives: "+lives,10,650);
		//Score
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Score: "+score,500,650);
		
		//After losing a life code to display the ball again
		lifeLost(g);
		if(!play && lost==false) {
			displayMessage(g);
		}
		
		if(lost && !play) {
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.drawString("You lost!", 260,350);
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Press enter to restart", 260,400);
		}
		
		if(numberBricks==0) {
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.drawString("You won!", 260,350);
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Press enter to restart", 260,400);
	
		}
		g.dispose();
		
	}

	public void lifeLost(Graphics g) {
		if(poss_bally>670) {
			g.setColor(Color.white);
			g.fillOval(pos_ballx, poss_bally, 20, 20);
			g.setColor(Color.black);
			
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		time.start();
		if(play) {
		
			collision();
			repaint();
		}
		
	}
	
	public void displayMessage(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		g.drawString("Press enter to play", 100,350);
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			play=true;
		}
		
		if((e.getKeyCode()==KeyEvent.VK_ENTER && (lost||won)) || (e.getKeyCode()==KeyEvent.VK_RIGHT &&(lost || won)) ||(e.getKeyCode()==KeyEvent.VK_LEFT&&(lost||won))) {
			bricks=new int [row][col];
			Arrays.fill(bricks[0], 2);//More resistant blocks, meant to be hit twice
			for (int i=1;i<row;i++) {
				Arrays.fill(bricks[i],1);
			}
			ball_y_dir=-3;
			ball_x_dir=-2;
			numberBricks=28;
			delay=5;
			slider=310;
			score=0;
			lives=3;
			lost=false;
			play=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			if(slider>=580) {
				slider=580;
			}else {
				play=true;
				if(numberBricks>25) {
					slider+=20;
				}
				else if(numberBricks>10) {
					slider+=40;
				}
				else {
					slider+=60;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			if(slider<=10) {
				slider=10;
			}else {
				play=true;
				if(numberBricks>25) {
					slider-=20;
				}
				else if(numberBricks>10) {
					slider-=40;
				}
				else {
					slider-=60;
				}
			}
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	


	
	public void collision() {
		
		if(new Rectangle(pos_ballx,poss_bally,20,20).intersects(new Rectangle(slider,600,lengthSlider,8))) {
			ball_y_dir=-(ball_y_dir);
			int randomNum = ThreadLocalRandom.current().nextInt(-3, 0); //To make direction of ball vary randomly(bc i'm too lazy to do physics)
			if(ball_x_dir<0) {
				ball_x_dir=randomNum;
			}
			else {
				ball_x_dir=-randomNum;
			}
		}
			
		//Control ball bounce
		
		pos_ballx+=ball_x_dir;
		poss_bally+=ball_y_dir;
		
		if(pos_ballx<0) {//bounce back on left wall
			ball_x_dir=-(ball_x_dir);
		}
	
		if(poss_bally<0) { //bounce back on top wall
			ball_y_dir=-(ball_y_dir);
		}
		if(pos_ballx>670) { //bounce back on right wall
			ball_x_dir=-(ball_x_dir);;
		}
		
		if(poss_bally>670 && !lost) {
			lives--;
			play=false;
			ball_x_dir=-2;
			ball_y_dir=-ball_y_dir;
			pos_ballx=120;
			poss_bally=350;
			}
		if(lives==0) {
			play=false;
			lost=true;
		}
		
		if(numberBricks==0) {
			won=true;
			play=false;
		}
		Boolean velocityUpdate=false; //Variable to check if collision with ball and two bricks happen
		for (int i = 0; i<row;i++) {
			for(int j= 0; j<col; j++) {
				if(bricks[i][j]>0) {
					int x=30 + j*brickWidth;
					int y=50+ i*brickHeight;
					
				
					
					Rectangle ballb=new Rectangle(pos_ballx,poss_bally,20,20);
					Rectangle brickb=new Rectangle(x,y,brickWidth,brickHeight);
					if(ballb.intersects(brickb)) {
			
						bricks[i][j]=bricks[i][j]-1;
						if(bricks[i][j]==0) {
							numberBricks--;
						}
						score+=10;
						if(pos_ballx+29<=x || pos_ballx+1 >= x + brickWidth) { //intersection from the side
							ball_x_dir=-ball_x_dir;
						}
						if (!velocityUpdate){
							ball_y_dir=-(ball_y_dir-0.5) ;
						}
						velocityUpdate=true;
					}
				}
				}
			}
			
	}
	
	public void drawbricks(Graphics2D g) {
		for (int i = 0; i<row;i++) {
			for(int j= 0; j<col; j++) {
				if(bricks[i][j]==1) {
					g.setColor(SEA);
					g.fillRect(30 + j*brickWidth,50+ i*brickHeight , brickWidth, brickHeight);
					g.setColor(ABYSS);
					g.setStroke(new BasicStroke(4));
					g.setColor(ABYSS);
					g.drawRect(30 + j*brickWidth,50+ i*brickHeight , brickWidth, brickHeight);
				}
				if(bricks[i][j]==2) {
					g.setColor(Color.white);
					g.fillRect(30 + j*brickWidth,50+ i*brickHeight , brickWidth, brickHeight);
					g.setColor(ABYSS);
					g.setStroke(new BasicStroke(4));
					g.setColor(ABYSS);
					g.drawRect(30 + j*brickWidth,50+ i*brickHeight , brickWidth, brickHeight);
				}
			}
		}
	}
	

	
}
