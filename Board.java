import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.Random;

public class Board extends JPanel implements ActionListener{

	public enum Direction {
		Right, Left, Up, Down;		
	}
	
	private static final int Largura = 600;
	private static final int Altura = 800;
	private int z = 0;
	
	private int x_Lula = 100;
	private int y_Lula = 100;
	private int x_Propina;
	private int y_Propina;
	
	
	private int x_Japa = 500;
	private int y_Japa = 500;
	
	
	private Direction Direcao_Lula;
	private Direction Direcao_Japa;
	
	private Timer timer;
	private Image PF;
	private Image Lula;
	private Image Propina;
	
	String str1, str2, str3, str4, str5, str6, str7, str8;
	
	
	private Controlador Controle = new Controlador();
	Random randomGenerator = new Random();
	
	public Placar Placar_Jogo = new Placar();

	InputStream StreamFromClient;
	OutputStream StreamToClient;
	
	ServerSocket ss;
	
	Socket client;
	
	DataFromClient Thread_Stream;
		
	public void Conectar (int Port)
	{
		try
		{
			ss = new ServerSocket (Port);
			client = ss.accept();
			System.out.println("Conectou!");
			StreamFromClient = client.getInputStream();
			StreamToClient = client.getOutputStream();
			client.setSoTimeout(10);
		}
		catch (IOException e)
		{
			System.out.println("Exceção:" + e);
		}
	}
	
	public Board (int Port)
	{
		setBackground(Color.black);
		setPreferredSize(new Dimension(Largura, Altura));
		
		Conectar(Port);
		
		timer = new Timer (10, this);
		timer.start();
		
		ImageIcon Imagem_PF = new ImageIcon("PF.png");
		PF = Imagem_PF.getImage();
		
		ImageIcon Imagem_Lula = new ImageIcon("Lula.png");
		Lula = Imagem_Lula.getImage();
		
		ImageIcon Imagem_Propina = new ImageIcon("Propina.png");
		Propina = Imagem_Propina.getImage();

		
		Direcao_Lula = Direction.Up;
		Direcao_Japa = Direction.Up;
		
		y_Lula = 100;
		x_Lula = 100;
		
		x_Japa = 500;
		y_Japa = 500;
			
		x_Propina = randomGenerator.nextInt(Largura);
		y_Propina = randomGenerator.nextInt(Altura);
		
		addKeyListener(new Controlador());
		setFocusable(true);
		
		System.out.println("Estou aqui!");
	
	
		Thread_Stream = new DataFromClient (this, StreamFromClient);
		Thread_Stream.start();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent (g);
		g.drawImage(PF, x_Japa, y_Japa, this);
		g.drawImage(Lula, x_Lula, y_Lula, this);
		g.drawImage(Propina, x_Propina, y_Propina, this);
        Toolkit.getDefaultToolkit().sync();
	}
	
	public void actionPerformed (ActionEvent e)
	{
		move();
		
		if (ganhou_Propina())
		{
			System.out.println("PROPINA!");
			set_Propina();
			Placar_Jogo.Acrescentar_Propina();
		}
		if (Prendeu())
		{
			System.out.println("PF!");
			set_Lula();
			Placar_Jogo.Acrescentar_Prisao();
		}
		
		str1 = Integer.toString(x_Lula);
		str2 = Integer.toString(y_Lula);
		str3 = Integer.toString(x_Japa);
		str4 = Integer.toString(y_Japa);
		str5 = Integer.toString(x_Propina);
		str6 = Integer.toString(y_Propina);
		str7 = Integer.toString(Placar_Jogo.get_n_Prisoes());
		str8 = Integer.toString(Placar_Jogo.get_n_Propina());
		
		String mensagem = new String (str1 + "," + str2 + "," + str3 + "," + str4 + "," + str5 + "," + str6 + "," + str7 + "," + str8 + ",");
		
		System.out.println(mensagem);
		
		try
		{
			StreamToClient.write(mensagem.getBytes());
		}
		catch(IOException evt)
		{
			System.out.println("Exceção:" + evt);
		}
		repaint();
	}
	
	private void set_Lula ()
	{
		x_Lula= randomGenerator.nextInt(Largura);
		y_Lula = randomGenerator.nextInt(Altura);
	}
	
	private boolean Prendeu()
	{
		return (Math.abs (x_Lula - x_Japa) < 20 && Math.abs(y_Lula - y_Japa) < 20); 
	}
	
	private void set_Propina ()
	{
		x_Propina = randomGenerator.nextInt(Largura);
		y_Propina = randomGenerator.nextInt(Altura);	
	}
	
	private void move ()
	{
		if (Direcao_Lula == Direction.Right)
			if (x_Lula < Largura)
				x_Lula++;
			else x_Lula = 0;
		else if (Direcao_Lula == Direction.Left)
			if (x_Lula == 0)
				x_Lula = Largura;
			else x_Lula--; 
		else if (Direcao_Lula == Direction.Down)
			if (y_Lula < Altura)
				y_Lula++;
			else y_Lula = 0;
		else if (Direcao_Lula == Direction.Up)
			if (y_Lula == 0)
				y_Lula = Altura;
			else y_Lula--;

		if (Direcao_Japa == Direction.Right)
			if (x_Japa < Largura)
				x_Japa++;
			else x_Japa = 0;
		else if (Direcao_Japa == Direction.Left)
			if (x_Japa == 0)
				x_Japa = Largura;
			else x_Japa--; 
		else if (Direcao_Japa == Direction.Down)
			if (y_Japa < Altura)
				y_Japa++;
			else y_Japa = 0;
		else if (Direcao_Japa == Direction.Up)
			if (y_Japa == 0)
				y_Japa = Altura;
			else y_Japa--;
	}
	
	private boolean ganhou_Propina ()
	{
		return (Math.abs (x_Lula - x_Propina) < 20 && Math.abs(y_Lula - y_Propina) < 20); 
	}
	
	public void TurnLeft ()
	{
		Direcao_Japa = Direction.Left;
	}
	
	public void TurnRight ()
	{
		Direcao_Japa = Direction.Right;
	}
	
	public void TurnUp ()
	{
		Direcao_Japa = Direction.Up;
	}
	
	public void TurnDown ()
	{
		Direcao_Japa = Direction.Down;
	}
	
	private class Controlador extends KeyAdapter
	{
		@Override
		public void keyPressed (KeyEvent e)
		{
			int key = e.getKeyCode();
			//String Message = new String("");
			
			switch (key)
			{
				case KeyEvent.VK_RIGHT:
					//Message = new String ("R");
					//StreamToClient.write(Message.getBytes());
					Direcao_Lula = Direction.Right;
					break;
				case KeyEvent.VK_LEFT:
					//Message = new String ("L");
					//StreamToClient.write(Message.getBytes());
					Direcao_Lula = Direction.Left;
					break;					
				case KeyEvent.VK_UP:
					//Message = new String ("U");
					//StreamToClient.write(Message.getBytes());
					Direcao_Lula = Direction.Up;
					break;
				case KeyEvent.VK_DOWN:
					//Message = new String ("D");
					//StreamToClient.write(Message.getBytes());
					Direcao_Lula = Direction.Down;
					break;
			}
			
		}
	}   
	
}
