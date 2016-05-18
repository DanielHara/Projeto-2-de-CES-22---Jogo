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

	//Declaração de variáveis

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
	
	
	//Esta função inicia um ServerSocket, que está disposto a iniciar o jogo com o cliente, através do comando ss.accept();	
	public void Conectar (int Port)
	{
		try
		{
			ss = new ServerSocket (Port);
			client = ss.accept();
			System.out.println("Conectou!");
			StreamFromClient = client.getInputStream();	//Obter as Input e Output Streams
			StreamToClient = client.getOutputStream();
			client.setSoTimeout(10);
		}
		catch (IOException e)
		{
			System.out.println("Exceção:" + e);
		}
	}
	
	//Construtor
	public Board (int Port)
	{
		setBackground(Color.black);
		setPreferredSize(new Dimension(Largura, Altura));
		
		Conectar(Port);
		
		timer = new Timer (10, this);	//Cronômetro, para atualizar a tela
		timer.start();
		
		//Carregar as imagens
		
		ImageIcon Imagem_PF = new ImageIcon("PF.png");
		PF = Imagem_PF.getImage();
		
		ImageIcon Imagem_Lula = new ImageIcon("Lula.png");
		Lula = Imagem_Lula.getImage();
		
		ImageIcon Imagem_Propina = new ImageIcon("Propina.png");
		Propina = Imagem_Propina.getImage();

		//Setar as posições dos personagens no início da execução do jogo.
		Direcao_Lula = Direction.Up;
		Direcao_Japa = Direction.Up;
		
		y_Lula = 100;
		x_Lula = 100;
		
		x_Japa = 500;
		y_Japa = 500;
			
		x_Propina = randomGenerator.nextInt(Largura);
		y_Propina = randomGenerator.nextInt(Altura);
		
		addKeyListener(new Controlador());    //Adicionar o controle por teclas.
		setFocusable(true);
		
		System.out.println("Estou aqui!");
	
	
		Thread_Stream = new DataFromClient (this, StreamFromClient);
		Thread_Stream.start();    //Iniciar a Thread, que é a responsável pela leitura de dados vindos do Cliente.
	}
	
	
	//Esta função serve para desenhar a Tela.
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent (g);
		g.drawImage(PF, x_Japa, y_Japa, this);
		g.drawImage(Lula, x_Lula, y_Lula, this);
		g.drawImage(Propina, x_Propina, y_Propina, this);
        Toolkit.getDefaultToolkit().sync();
	}
	
	//Sempre que o cronômetro registrar que se passaram 10ms, este código é executado.
	
	public void actionPerformed (ActionEvent e)
	{
		move();
		//Se o Lula conseguiu pegar a propina, uma nova propina é colocada em um ponto aleatório e se acrescenta 1 propina no placar.	
		if (ganhou_Propina())
		{
			System.out.println("PROPINA!");
			set_Propina();
			Placar_Jogo.Acrescentar_Propina();
		}
		//Se o Lula e o Japa se encontrarem, o Lula é colocado em um local aleatório e se acrescenta 1 prisão.
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
		//Esta String chamada mensagem é passada ao cliente, para que ele possa localizar e sincronizar a tela.	
		System.out.println(mensagem);
		
		try
		{
			StreamToClient.write(mensagem.getBytes());	//Passamos ao cliente as informações de posição.
		}
		catch(IOException evt)
		{
			System.out.println("Exceção:" + evt);
		}
		repaint();
	}
	
	private void set_Lula ()
	{
		x_Lula= randomGenerator.nextInt(Largura);	//Quando o Lula encontrar o Japa, sua nova posição deve ser aleatória.
		y_Lula = randomGenerator.nextInt(Altura);
	}
	
	private boolean Prendeu()
	{
		return (Math.abs (x_Lula - x_Japa) < 20 && Math.abs(y_Lula - y_Japa) < 20);  //Lula é considerado preso quando chegar suficientemente perto do Japa.
	}
	
	private void set_Propina ()
	{
		x_Propina = randomGenerator.nextInt(Largura);	//Quando Lula embolsa a propina, outra propina deve aparecer em local aleatório.
		y_Propina = randomGenerator.nextInt(Altura);	
	}
	
	//Esta função move os personagens de acordo com a direção em que ele viajam.
	
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
		return (Math.abs (x_Lula - x_Propina) < 20 && Math.abs(y_Lula - y_Propina) < 20); //Considera-se que Lula embolsa a propina quando chegar suficientemente perto dela.
	}
	
	//Virar o Japa para a esquerda.
	public void TurnLeft ()
	{
		Direcao_Japa = Direction.Left;
	}
	
	//Virar o Japa para a direita.
	public void TurnRight ()
	{
		Direcao_Japa = Direction.Right;
	}
	
	//Virar o Japa para cima.
	public void TurnUp ()
	{
		Direcao_Japa = Direction.Up;
	}
	
	//Virar o Japa para baixo.
	public void TurnDown ()
	{
		Direcao_Japa = Direction.Down;
	}
	
	//Esta função permite que se controle o Lula por meio das setas do teclado.
	private class Controlador extends KeyAdapter
	{
		@Override
		public void keyPressed (KeyEvent e)
		{
			int key = e.getKeyCode();
			
			switch (key)
			{
				case KeyEvent.VK_RIGHT:
					Direcao_Lula = Direction.Right;
					break;
				case KeyEvent.VK_LEFT:
					Direcao_Lula = Direction.Left;
					break;					
				case KeyEvent.VK_UP:
					Direcao_Lula = Direction.Up;
					break;
				case KeyEvent.VK_DOWN:
					Direcao_Lula = Direction.Down;
					break;
			}
			
		}
	}   
	
}
