import javax.swing.*;     
import java.awt.*; 


public class Placar extends JPanel{

	private JTextArea textArea = new JTextArea(2,30);
	private int n_Propina;
	private int n_Prisoes;
	
	
	public Placar ()
	{
		textArea.setEditable(false);
		n_Propina = 0;
		n_Prisoes = 0;
		add(textArea);
		Show_Placar();
	}
	
	private void Show_Placar ()
	{
		textArea.setText("");
		textArea.append("Propina: R$" + n_Propina + "Bilhões \nPrisões: " + n_Prisoes);
	}
	
	public void Acrescentar_Prisao ()
	{
		n_Prisoes++;
		Show_Placar();
	}
	
	public void Acrescentar_Propina ()
	{
		n_Propina++;
		Show_Placar();
	}
	
	public int get_n_Prisoes ()
	{
		return n_Prisoes;
	}
	
	public int get_n_Propina ()
	{
		return n_Propina;
	}
	
}
