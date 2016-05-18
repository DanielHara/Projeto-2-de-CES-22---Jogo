import javax.swing.*;     
import java.awt.*; 

Esta classe controla o Placar.

public class Placar extends JPanel{

	private JTextArea textArea = new JTextArea(2,30);
	private int n_Propina;
	private int n_Prisoes;
	
	//Construtor
	public Placar ()
	{
		textArea.setEditable(false);
		n_Propina = 0;
		n_Prisoes = 0;
		add(textArea);
		Show_Placar();
	}
	
	//Esta função mostra o placar.
	private void Show_Placar ()
	{
		textArea.setText("");
		textArea.append("Propina: R$" + n_Propina + "Bilhões \nPrisões: " + n_Prisoes);
	}
	
	//Esta função acrescenta 1 prisão
	public void Acrescentar_Prisao ()
	{
		n_Prisoes++;
		Show_Placar();
	}
	
	//Esta função acrescenta 1 propina
	public void Acrescentar_Propina ()
	{
		n_Propina++;
		Show_Placar();
	}
	
	//Esta função retorna o número de prisões.
	public int get_n_Prisoes ()
	{
		return n_Prisoes;
	}
	
	//Esta função retorna o número de propinas.
	public int get_n_Propina ()
	{
		return n_Propina;
	}
	
}
