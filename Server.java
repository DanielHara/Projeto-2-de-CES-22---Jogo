import java.awt.BorderLayout;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
    
    
    //Implementação da classe principal e da função main.
    
    public class Server extends JFrame
    {
    	public Server ()
    	{
    		//Perguntar o número da Porta.
    		
    		String Porta = null;
    		int Port = 0;
    		while (Porta == null || Porta.equals(""))
    			Porta = JOptionPane.showInputDialog("Digite a porta:");
    		Port = Integer.parseInt(Porta);
    		
    		Board Tela = new Board(Port);
    		add(Tela, BorderLayout.WEST);
    		add(Tela.Placar_Jogo, BorderLayout.EAST);
    		pack();
    		setVisible(true);
    		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	}
    	
    	public static void main (String[] args)
    	{
    		Server Jogo = new Server ();
    	}
    	
    }
