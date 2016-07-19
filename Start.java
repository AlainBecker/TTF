import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Start extends JFrame{
	
	private JPanel cp1 = null,cp2 = null,cp3 = null;
	public JLabel lab1=null,lab2=null,lab3=null,lab4=null,lab5=null,lab6=null,lab7=null;
	private JButton b1 = null,b2= null;
	private JComboBox cb1=null,cb2=null;
	private String[] dimensions ={"3","4","5","6","7","8","9"};
	public JLabel spacer=new JLabel(" ");
	
	public Start() throws IOException{super("1024");
		cp1 = new JPanel(new BorderLayout(0,0));
		cp2 = new JPanel(new FlowLayout());
		cp3= new JPanel();
		cp3.setLayout(new BoxLayout(cp3, BoxLayout.PAGE_AXIS));
		
		URL url = null;
		url = new URL("http://i.imgur.com/8wyLqTp.png?1");
		Image image = ImageIO.read(url);
		lab1= new JLabel(new ImageIcon(image));
		
		lab2=new JLabel ("Welcome to 1024!",SwingConstants.CENTER);
		lab3=new JLabel ("Choose Your Preferred Game Board Size:",SwingConstants.CENTER);
		lab4=new JLabel ("Slide tiles by using WASD or Arrow Keys.",SwingConstants.CENTER);
		lab1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lab2.setAlignmentX(Component.CENTER_ALIGNMENT);
		lab3.setAlignmentX(Component.CENTER_ALIGNMENT);
		lab4.setAlignmentX(Component.CENTER_ALIGNMENT);

		cp3.add(lab1);cp3.add(lab2);cp3.add(lab4);cp3.add(lab3);
		
		cb1=new JComboBox(dimensions);
		cb1.setSelectedIndex(0);
		cb2=new JComboBox(dimensions);
		cb2.setSize(12, 3);
		cb2.setSelectedIndex(0);
		
		lab6=new JLabel ("Rows: ",SwingConstants.CENTER);
		lab5=new JLabel ("X",SwingConstants.CENTER);
		lab7=new JLabel (" :Columns",SwingConstants.CENTER);
		
		cp2.add(lab6);cp2.add(cb1);cp2.add(lab5);cp2.add(cb2);cp2.add(lab7);
		
		b1=new JButton("Play!");
		
		cp1.add(cp3, BorderLayout.NORTH);
		cp1.add(cp2, BorderLayout.CENTER);
		cp1.add(b1, BorderLayout.SOUTH);
		
		this.setContentPane(cp1);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		b1.addActionListener(new eh());
	}
	
	private class eh implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
			int r= Integer.valueOf((String) cb1.getSelectedItem());
			int c= Integer.valueOf((String) cb2.getSelectedItem());
			TTF.rows = r;
			TTF.columns= c;
			TTF JZ = new TTF();
			JZ.setVisible(true);
			
		}
	}
	public static void main(String[] args) throws IOException {
		Start eh = new Start();
		eh.setVisible(true);
		eh.pack();
	}

}
