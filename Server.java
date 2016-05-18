import java.awt.BorderLayout;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
    
    public class Server extends JFrame
    {
    	public Server ()
    	{
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
