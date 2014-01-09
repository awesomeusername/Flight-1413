import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class AirplaneGame {
	
	JFrame frame;
	
	public static void main(String[] args){
		AirplaneGame ag = new AirplaneGame();
	}
	
	public AirplaneGame(){
		frame = new JFrame("Flight 1413");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AirplaneGameGUI gui = new AirplaneGameGUI();
		frame.getContentPane().add(gui.display());
		frame.pack();
		frame.show();
	}
	
	protected class AirplaneGameGUI{
		
		JLayeredPane panel;
		int phase;
		// 0 - menu
		// 1 - instructions
		// 2 - intro to play
		// 3 - play, full tank
		// 4 - on fire; you've lost
		// 5 - showing score
		// 6 - sure you want to delete highscore?
		// 7 - highscore deleted
		JLabel title;
		JLabel playOption, instrOption, resetScoresOption;
		JLabel message;
		JLabel ground;
		Can airplane;
		JLabel fuelBarBack;
		JLabel fuelBarFront;
		JLabel countdown;
		boolean up, down, left, right;
		int fuel;
		double score;
		int hiscore;
		JLabel scoreboard;
		JLabel hiscoreboard;
		double speed;
		ArrayList<Can> cans;
		ArrayList<Can> decor;
		ImageIcon airplaneImg, burningImg, canImg;
		ImageIcon[] cloud;
		
		public AirplaneGameGUI(){
			panel = new JLayeredPane();
			panel.setLayout(null);
			panel.setPreferredSize(new Dimension(800,600));
			panel.setBackground(new Color(200,228,255));
			panel.setOpaque(true);
			title = new JLabel("FLIGHT 1413");
			panel.add(title,3);
			playOption = new JLabel("PLAY");
			panel.add(playOption,3);
			instrOption = new JLabel("INSTRUCTIONS");
			panel.add(instrOption,3);
			resetScoresOption = new JLabel("RESET HIGH SCORE");
			panel.add(resetScoresOption,3);
			message = new JLabel("WASD/Arrow keys and enter to select.");
			panel.add(message,3);
			ground = new JLabel();
			ground.setLocation(0,550);
			ground.setSize(800,50);
			ground.setBackground(new Color(16,160,0));
			ground.setOpaque(true);
			panel.add(ground,0);
			airplane = new Can(4);
			airplane.setLocation(-10,-10);
			airplane.setSize(0,0);
			airplaneImg = new ImageIcon(getClass().getResource("airplane.png"));
			burningImg = new ImageIcon(getClass().getResource("burning.png"));
			canImg = new ImageIcon(getClass().getResource("fuel.png"));
			airplane.setIcon(airplaneImg);
			cloud = new ImageIcon[4];
			for (int i = 0; i < 4; i += 1){
				cloud[i] = new ImageIcon(getClass().getResource("cloud" + (i+1) + ".png"));
			}
			fuelBarBack = new JLabel();
			fuelBarBack.setSize(0,0);
			fuelBarBack.setLocation(-10,-10);
			fuelBarBack.setBackground(Color.black);
			fuelBarBack.setOpaque(true);
			panel.add(fuelBarBack,1);
			fuelBarFront = new JLabel();
			fuelBarFront.setSize(0,0);
			fuelBarFront.setLocation(-10,-10);
			fuelBarFront.setBackground(new Color(0,128,0));
			fuelBarFront.setOpaque(true);
			panel.add(fuelBarFront,2);
			countdown = new JLabel();
			countdown.setSize(0,0);
			countdown.setLocation(-10,-10);
			panel.add(countdown,3);
			scoreboard = new JLabel();
			scoreboard.setSize(0,0);
			scoreboard.setLocation(-10,-10);
			panel.add(scoreboard,3);
			hiscoreboard = new JLabel();
			hiscoreboard.setSize(0,0);
			hiscoreboard.setLocation(-10,-10);
			panel.add(hiscoreboard,3);
			hiscore = 0;
			speed = 2.0;
			cans = new ArrayList<Can>();
			decor = new ArrayList<Can>();
			AirplaneGameKeyListener agkl = new AirplaneGameKeyListener();
			frame.addKeyListener(agkl);
			java.util.Timer timer = new java.util.Timer();
			timer.scheduleAtFixedRate(new AirplaneTask(), 0, 50);
			setupMenu();
		}
		
		public void setupMenu(){
			phase = 0;
			title.setLocation(10,10);
			title.setSize(780,260);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setFont(new Font("Futura", Font.PLAIN, 72));
			playOption.setLocation(250,220);
			playOption.setSize(400,75);
			playOption.setHorizontalAlignment(SwingConstants.LEFT);
			playOption.setFont(new Font("Futura", Font.PLAIN, 42));
			playOption.setText("=> PLAY");
			instrOption.setLocation(250,295);
			instrOption.setSize(400,75);
			instrOption.setHorizontalAlignment(SwingConstants.LEFT);
			instrOption.setFont(new Font("Futura", Font.PLAIN, 42));
			instrOption.setText("INSTRUCTIONS");
			resetScoresOption.setLocation(250,370);
			resetScoresOption.setSize(500,75);
			resetScoresOption.setHorizontalAlignment(SwingConstants.LEFT);
			resetScoresOption.setFont(new Font("Futura", Font.PLAIN, 42));
			resetScoresOption.setText("RESET HIGH SCORE");
			message.setLocation(10,550);
			message.setSize(780,50);
			message.setHorizontalAlignment(SwingConstants.CENTER);
			message.setVerticalAlignment(SwingConstants.CENTER);
			message.setFont(new Font("Futura", Font.PLAIN, 18));
			message.setText("WASD/Arrow keys and enter to select.");
		}
		
		public void takedownMenu(){
			title.setSize(0,0);
			title.setLocation(-10,-10);
			playOption.setSize(0,0);
			playOption.setLocation(-10,-10);
			instrOption.setSize(0,0);
			instrOption.setLocation(-10,-10);
			resetScoresOption.setSize(0,0);
			resetScoresOption.setLocation(-10,-10);
			message.setSize(0,0);
			message.setLocation(-10,-10);
		}
		
		public void setupInstr(){
			phase = 1;
			message.setLocation(25,25);
			message.setSize(750,550);
			message.setHorizontalAlignment(SwingConstants.CENTER);
			message.setVerticalAlignment(SwingConstants.TOP);
			message.setFont(new Font("Futura", Font.PLAIN, 24));
			message.setText("<HTML>You are a pilot for Awesome Airlines.  You're in the middle of your first flight of today, Flight 1413.  Everything is going well.  You sit back and are about to take a sip of coffee when the plane lurches, causing you to spill all over yourself.  You check the dials and find that you're running out of gas!  <BR><BR>You can move your plane using the arrow keys or WASD.  The space bar also moves your plane upward.  You can move your plane side to side or down without any cost, but it takes up gas to move up.<BR><BR>Fortunately, there are tanks of fuel randomly floating in the sky, so you can refill.  But be careful!  YOU ONLY GET ONE tank of fuel at a time.  If you try to collect fuel when you still have some fuel left in your tank, it will ignite and your plane will explode.  Try to fly as far as you can!</HTML>");
			playOption.setText("=> BACK TO MENU");
			playOption.setLocation(200,520);
			playOption.setSize(400,100);
			playOption.setHorizontalAlignment(SwingConstants.CENTER);
			playOption.setFont(new Font("Futura", Font.PLAIN, 36));
		}
		
		public void setupPlay(){
			phase = 2;
			airplane.setSize(80,35);
			airplane.setLocation(-100,50);
			airplane.setOpaque(false);
			airplane.setIcon(airplaneImg);
			fuelBarBack.setSize(604,20);
			fuelBarBack.setLocation(0,0);
			fuelBarFront.setSize(600,16);
			fuelBarFront.setLocation(2,2);
			fuelBarFront.setBackground(new Color(0,128,0));
			countdown.setFont(new Font("Futura", Font.PLAIN, 288));
			countdown.setSize(780,580);
			countdown.setLocation(10,10);
			countdown.setHorizontalAlignment(SwingConstants.CENTER);
			countdown.setText("3");
			scoreboard.setSize(196,25);
			scoreboard.setLocation(604,0);
			scoreboard.setFont(new Font("Futura", Font.PLAIN, 18));
			scoreboard.setText("0 m");
			scoreboard.setHorizontalAlignment(SwingConstants.RIGHT);
			hiscoreboard.setSize(196,25);
			hiscoreboard.setLocation(604,30);
			hiscoreboard.setFont(new Font("Futura", Font.PLAIN, 18));
			hiscoreboard.setText("Best: " + hiscore + " m");
			hiscoreboard.setHorizontalAlignment(SwingConstants.RIGHT);
			up = false;
			down = false;
			left = false;
			right = false;
			fuel = 100;
			score = 0;
			speed = 2.0;
		}
		
		public void removeFuelBar(){
			fuelBarFront.setSize(0,0);
			fuelBarBack.setSize(0,0);
			fuelBarFront.setLocation(-10,-10);
			fuelBarBack.setLocation(-10,-10);
		}
		
		public void showScore(){
			scoreboard.setSize(780,380);
			scoreboard.setLocation(10,10);
			scoreboard.setHorizontalAlignment(SwingConstants.CENTER);
			scoreboard.setFont(new Font("Futura", Font.PLAIN, 144));
			hiscoreboard.setSize(780,100);
			hiscoreboard.setLocation(10,290);
			hiscoreboard.setHorizontalAlignment(SwingConstants.CENTER);
			hiscoreboard.setFont(new Font("Futura", Font.PLAIN, 30));
		}
		
		public void setupSubmenu(){
			message.setText("Are you sure you want to delete your awesome high score of");
			message.setLocation(10,10);
			message.setSize(780,180);
			message.setHorizontalAlignment(SwingConstants.CENTER);
			message.setFont(new Font("Futura", Font.PLAIN, 28));
			hiscoreboard.setText(hiscore + " m?");
			hiscoreboard.setFont(new Font("Futura", Font.PLAIN, 144));
			hiscoreboard.setLocation(10,80);
			hiscoreboard.setSize(780,300);
			hiscoreboard.setHorizontalAlignment(SwingConstants.CENTER);
			playOption.setText("=> BACK TO MENU");
			playOption.setLocation(225,340);
			playOption.setSize(350,50);
			playOption.setHorizontalAlignment(SwingConstants.LEFT);
			playOption.setFont(new Font("Futura", Font.PLAIN, 36));
			instrOption.setText("DELETE");
			instrOption.setLocation(225,400);
			instrOption.setSize(350,50);
			instrOption.setHorizontalAlignment(SwingConstants.LEFT);
			instrOption.setFont(new Font("Futura", Font.PLAIN, 36));
		}
		
		public void takedownSubmenu(){
			message.setLocation(-10,-10);
			message.setSize(0,0);
			hiscoreboard.setLocation(-10,-10);
			hiscoreboard.setSize(0,0);
			playOption.setLocation(-10,-10);
			playOption.setSize(0,0);
			instrOption.setLocation(-10,-10);
			instrOption.setSize(0,0);
		}
		
		public JLayeredPane display(){
			return panel;
		}
		
		protected class Can extends JLabel{
			
			double drift;
			
			public Can(int layer){
				super();
				drift = 0.0;
				setLocation(-60,-60);
				setSize(50,50);
				setIcon(canImg);
				panel.add(this, layer);
			}
			
			public boolean collide(){
				if (getX() + 50 < airplane.getX() || getX() > airplane.getX() + 80){
					return false;
				}
				if (getY() + 50 < airplane.getY() || getY() > airplane.getY() + 35){
					return false;
				}
				return true;
			}
		}
		
		protected class AirplaneGameKeyListener implements KeyListener{
			public void keyReleased(KeyEvent e){
				if (phase == 3){
					if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
						left = false;
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
						right = false;
					} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_SPACE){
						up = false;
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN  || e.getKeyCode() == KeyEvent.VK_S){
						down = false;
					}
				}
			}
			public void keyTyped(KeyEvent e){}
			public void keyPressed(KeyEvent e){
				if (phase == 0){
					if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
						if (playOption.getText().charAt(0) == '='){
							playOption.setText("PLAY");
							resetScoresOption.setText("=> RESET HIGH SCORE");
						} else if (instrOption.getText().charAt(0) == '='){
							instrOption.setText("INSTRUCTIONS");
							playOption.setText("=> PLAY");
						} else if (resetScoresOption.getText().charAt(0) == '='){
							resetScoresOption.setText("RESET HIGH SCORE");
							instrOption.setText("=> INSTRUCTIONS");
						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
						if (playOption.getText().charAt(0) == '='){
							playOption.setText("PLAY");
							instrOption.setText("=> INSTRUCTIONS");
						} else if (instrOption.getText().charAt(0) == '='){
							instrOption.setText("INSTRUCTIONS");
							resetScoresOption.setText("=> RESET HIGH SCORE");
						} else if (resetScoresOption.getText().charAt(0) == '='){
							resetScoresOption.setText("RESET HIGH SCORE");
							playOption.setText("=> PLAY");
						}
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER){
						if (playOption.getText().charAt(0) == '='){
							takedownMenu();
							setupPlay();
						} else if (instrOption.getText().charAt(0) == '='){
							takedownMenu();
							setupInstr();
						} else if (resetScoresOption.getText().charAt(0) == '='){
							takedownMenu();
							setupSubmenu();
							phase = 6;
						}
					}
				} else if (phase == 1){
					if (e.getKeyCode() == KeyEvent.VK_ENTER){
						setupMenu();
					}
				} else if (phase == 3){
					if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
						left = true;
						right = false;
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
						right = true;
						left = false;
					} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_SPACE){
						up = true;
						down = false;
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN  || e.getKeyCode() == KeyEvent.VK_S){
						down = true;
						up = false;
					}
				} else if (phase == 5 && playOption.getText().equals("=> CONTINUE")){
					if (e.getKeyCode() == KeyEvent.VK_ENTER){
						phase = 1;
						scoreboard.setLocation(-10,-10);
						scoreboard.setSize(0,0);
						hiscoreboard.setLocation(-10,-10);
						hiscoreboard.setSize(0,0);
						setupMenu();
					}
				} else if (phase == 6){
					if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
						if (playOption.getText().charAt(0) == '='){
							playOption.setText("BACK TO MENU");
							instrOption.setText("=> DELETE");
						} else {
							playOption.setText("=> BACK TO MENU");
							instrOption.setText("DELETE");
						}
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER){
						if (playOption.getText().charAt(0) == '='){
							takedownSubmenu();
							setupMenu();
							phase = 0;
						} else {
							hiscore = 0;
							message.setText("Score deleted.");
							message.setFont(new Font("Futura",Font.PLAIN,48));
							hiscoreboard.setLocation(-10,-10);
							hiscoreboard.setSize(0,0);
							phase = 7;
							playOption.setText("");
							instrOption.setText("=> BACK TO MENU");
						}
					}
				} else if (phase == 7){
					if (e.getKeyCode() == KeyEvent.VK_ENTER){
						takedownSubmenu();
						setupMenu();
						phase = 0;
					}
				}
			}
		}
		
		protected class AirplaneTask extends TimerTask{
			
			int count;
			
			public void run(){
				if (phase == 0){
					count = 0;
				}
				if (phase == 2){
					count += 1;
					airplane.setLocation(airplane.getX() + 2, airplane.getY());
					if (count == 25){
						countdown.setText("2");
					} else if (count == 50){
						countdown.setText("1");
					}
					if (airplane.getX() >= 50){
						phase = 3;
						countdown.setText("GO");
					}
				}
				if (phase == 3){
					speed = 2.0 + 0.1 * (int) (0.01*(score - airplane.getX()));
					count += 1;
					score += speed;
					if (count == 100){
						countdown.setText("");
						countdown.setSize(0,0);
						countdown.setSize(-10,-10);
					}
					if (up && fuel > 0){
						airplane.setLocation(airplane.getX(), airplane.getY() - 5);
						fuel -= 1;
						fuelBarFront.setSize(6*fuel, 16);
						if (fuel == 50){
							fuelBarFront.setBackground(new Color(128,128,0));
						} else if (fuel < 50){
							fuelBarFront.setBackground(new Color(128,128*fuel/50,0));
						} else {
							fuelBarFront.setBackground(new Color(128*(100-fuel)/50,128,0));
						}
						if (airplane.getY() < 0){
							airplane.setLocation(airplane.getX(), 0);
						}
					} else if (down){
						airplane.setLocation(airplane.getX(), airplane.getY() + 13);
					} else {
						airplane.setLocation(airplane.getX(), airplane.getY() + 4);
					}
					if (airplane.getY() > 515){
						phase = 5;
						removeFuelBar();
						showScore();
						airplane.setIcon(burningImg);
						decor.add(airplane);
					}
					if (left){
						airplane.setLocation(airplane.getX() - 8, airplane.getY());
						score -= 8;
						if (airplane.getX() < 0){
							score -= airplane.getX();
							airplane.setLocation(0, airplane.getY());
						}
					} else if (right){
						airplane.setLocation(airplane.getX() + 8, airplane.getY());
						score += 8;
						if (airplane.getX() > 720){
							score -= airplane.getX() - 720;
							airplane.setLocation(720, airplane.getY());
						}
					}
					airplane.drift = 800 - airplane.getX();
					scoreboard.setText((int) (score) + " m");
					if (Math.random() < Math.max(0.5/(cans.size() * cans.size() * cans.size() * cans.size() + 1), speed/150)){
						Can can = new Can(4);
						cans.add(can);
						can.setLocation(800,(int) (500*Math.random()));
					}
				} else if (phase == 4){
					airplane.setLocation(airplane.getX(), airplane.getY() + 4);
					if (airplane.getY() > 520){
						phase = 5;
						decor.add(airplane);
					}
					score += speed;
					scoreboard.setText((int) (score) + " m");
				} else if (phase == 5){
					showScore();
					if (hiscore < score){
						hiscore = (int) score;
						hiscoreboard.setText("New high score!");
					} else {
						hiscoreboard.setText("High score: "+ hiscore + " m");
					}
					if (cans.isEmpty() && !decor.contains(airplane)){
						playOption.setLocation(10,400);
						playOption.setSize(780,200);
						playOption.setText("=> CONTINUE");
						playOption.setHorizontalAlignment(SwingConstants.CENTER);
					}
				}
				ArrayList<Can> removeMe = new ArrayList<Can>();
				for (Can can: cans){
					can.drift += speed;
					can.setLocation((int) (800-can.drift), can.getY());
					if (can.getX() < -50){
						can.setLocation(-60,-60);
						can.setSize(0,0);
						panel.remove(can);
						removeMe.add(can);
					}
					if (can.collide() && phase == 3){
						can.setLocation(-60,-60);
						can.setSize(0,0);
						panel.remove(can);
						removeMe.add(can);
						if (fuel == 0){
							fuel = 100;
							fuelBarFront.setSize(600,16);
							fuelBarFront.setBackground(new Color(0,128,0));
						} else {
							phase = 4;
							airplane.setIcon(burningImg);
							removeFuelBar();
						}
					}
				}
				for (Can can: removeMe){
					cans.remove(can);
				}
				ArrayList<Can> removeMeDecor = new ArrayList<Can>();
				for (Can d: decor){
					panel.setLayer(d,0);
					d.drift += speed;
					d.setLocation((int) (800-d.drift), d.getY());
					if (d.getX() < -125){
						if (d == airplane){
							removeMeDecor.add(d);
							d.setLocation(-130,-130);
							d.setSize(0,0);
						} else {
							d.setLocation(-60,-60);
							d.setSize(0,0);
							panel.remove(d);
							removeMeDecor.add(d);
						}
					}
				}
				for (Can d: removeMeDecor){
					decor.remove(d);
				}
				if (Math.random() < 0.01 || decor.isEmpty()){
					Can d = new Can(0);
					decor.add(d);
					d.drift -= 0.5;
					d.setSize(120,60);
					d.setIcon(cloud[(int) (Math.random() * 4)]);
					d.setLocation(800, (int) (Math.random() * 300));
				}
				resetLayers();
			}
			
			public void resetLayers(){
				panel.setLayer(title,3);
				panel.setLayer(playOption,3);
				panel.setLayer(instrOption,3);
				panel.setLayer(resetScoresOption,3);
				panel.setLayer(message,3);
				panel.setLayer(ground, 0);
				panel.setLayer(airplane,4);
				panel.setLayer(fuelBarBack,1);
				panel.setLayer(fuelBarFront,2);
				panel.setLayer(countdown,3);
				panel.setLayer(scoreboard,3);
				panel.setLayer(hiscoreboard,3);
				for (Can c: cans){
					panel.setLayer(c,4);
				}
				for (Can d: decor){
					if (d != airplane){
						panel.setLayer(d,0);
					}
				}
			}
		}
	}
}
