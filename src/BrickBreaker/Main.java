package BrickBreaker;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame object=new JFrame();
		Game game=new Game();
		object.setTitle("Brick break game");
		object.setBounds(10,10,700,700);
		object.setResizable(false);
		object.setVisible(true);
		object.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		object.add(game);
	}

}
