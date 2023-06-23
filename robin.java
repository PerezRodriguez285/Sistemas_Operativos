import javax.swing.*;
import java.awt.event.*;

public class robin extends JFrame implements ActionListener{
	//texto 
	private JLabel robin0;
    private JLabel robin2, robin3, robin4, robin5, robin6, robin7; 
    private JLabel variable1;
    private JLabel variable2;
    private JLabel variable3;
     	
	//botones
	private JButton boton1;
	private JButton boton2;
	private JButton boton3;
	private JButton boton4;
	private JButton boton5;
	private JButton boton6, boton7;
	
	//cuadro de texto
	private JTextField texto1;
	private JTextField texto2;
	private JTextField texto3;
	
	//text area
	private JTextArea area1;
	private JTextArea area2;
	private JTextArea area3;
	private JTextArea area4;
	
	//scrollpane
	private JScrollPane paen1;
	private JScrollPane paen2;
	private JScrollPane paen3;
	private JScrollPane paen4;
	
	//variable de texto
	String text ="";
	String text1 = "";
	String text2 = "";
	int resultado=0, q=0, p=0, tp=0;
	
	//constructor
	public robin(){
	//texto
	setLayout(null);
	robin0 = new JLabel("ROUND ROBIN");
	robin0.setBounds(500,0,300,30);
	add(robin0);
	
	robin2 = new JLabel("V 0.1");
	robin2.setBounds(1000,630,100,30);
	add(robin2);
	
	variable1 = new JLabel("Q");
	variable1.setBounds(50,5,30,150);
	add(variable1);
	
	variable2 = new JLabel("P");
	variable2.setBounds(50,20,100,180);
	add(variable2);
	
	variable3 = new JLabel("PT");
	variable3.setBounds(50,35,170,210);
	add(variable3);
	
	robin3 = new JLabel("Quantum");
	robin3.setBounds(30,320, 100,30);
	add(robin3);
	
	robin4 = new JLabel("N-Procesos");
	robin4.setBounds(120,320,100,30);
	add(robin4);
	
	robin5 = new JLabel("T.Procesos");
	robin5.setBounds(210,320,100,30);
	add(robin5);
	
	//botones
	boton1 = new JButton("SALIR");
	boton1.setBounds(950,600,100,30);
	add(boton1);
	//evento de boton1
	boton1.addActionListener(this);
	
	//boton2
	boton2= new JButton("int.valor Q");
	boton2.setBounds(360,65,100,30);
	add(boton2);
	boton2.addActionListener(this);
	
	//boton3
	boton3= new JButton("cant.P");
	boton3.setBounds(360,95,100,30);
	add(boton3);
	boton3.addActionListener(this);
	
	//boton4
	boton4= new JButton("tP");
	boton4.setBounds(360,125,100,30);
	add(boton4);
	boton4.addActionListener(this);
	
	//boton5
	boton5= new JButton("borrar");
	boton5.setBounds(310,360,100,30);
	add(boton5);
	boton5.addActionListener(this);
	
	//boton6
	boton6 = new JButton("EJECUTAR");
	boton6.setBounds(500,600,100,30);
	add(boton6);
	boton6.addActionListener(this);
	
	//boton7
	boton7 = new JButton("borrar");
	boton7.setBounds(940,460,100,30);
	add(boton7);
	boton7.addActionListener(this);
	
	//cuadro de texto
	texto1 = new JTextField("0");
	texto1.setBounds(100,60,150,30);
	add(texto1);
	
	texto2 = new JTextField("0");
	texto2.setBounds(100,90,150,30);
	add(texto2);
	
	texto3 = new JTextField("0");
	texto3.setBounds(100,120,150,30);
	add(texto3);
	
	//text area
	area1 = new JTextArea();
	paen1 = new JScrollPane(area1);
	paen1.setBounds(30,350,90,300);
	add(paen1);
	
	area2 = new JTextArea();
	paen2 = new JScrollPane(area2);
	paen2 .setBounds(120,350,90,300);
	add(paen2);
	
	area3 = new JTextArea();
	paen3 = new JScrollPane(area3);
	paen3 .setBounds(210,350,90,300);
	add(paen3);
	
	area4 = new JTextArea();
	paen4 = new JScrollPane(area4);
	paen4.setBounds(700,80,340,370);
	add(paen4);
	
	}
	
	//evento accion de botones
	public void actionPerformed(ActionEvent s){
		if(s.getSource() == boton1){
			System.exit(0);			
		}
        if(s.getSource() == boton2){
			//parceo
			q = Integer.parseInt(texto1.getText());
			//imprime los datos en forma texto
			text += texto1.getText()+"\n";
			area1.setText(text);
			texto1.setText("");
						 
		}
        if(s.getSource() == boton3){
			//parceo
			p= Integer.parseInt(texto2.getText());
			//imprime los datos en forma de texto
			text1 += texto2.getText()+"\n";
			area2.setText(text1);
			texto2.setText("");
			 			
		}
        if(s.getSource() == boton4){
			//parceo
			tp = Integer.parseInt(texto3.getText()); 
			//imprime los datos en forma de texto
			text2 += texto3.getText()+"\n";
			area3.setText(text2);
			texto3.setText("");
			 			
		}
        if(s.getSource() == boton5){
			area1.setText("");
            area2.setText("");
            area3.setText("");
            int q =0, tp =0, p=0;			
		}
        
        if(s.getSource() == boton6){
			//aqui ba todo el proceso del programa
					/*ejemplo de una suma */
					// asta riba declara las variables faltantes junto con los strings e int por la linea 42
			resultado = q + p + tp;
			
            //esta linea de codigo es para que se emprima el resultado
			area4.setText("resultado: "+ resultado);//aqui va la variable con el resultado puede usar canetacion			
			
		}	

        if(s.getSource() == boton7){
			int q=0,p=0, tp=0;
			area4.setText("");
			
		}		
	}

//metodo main	
	
   public static void main(String args[]){
	//apartado grafico ventana    
		robin robin1 = new robin(); //objeto ventana
	    robin1.setBounds(0,0,1080,720);
	    robin1.setVisible(true);
	    robin1.setLocationRelativeTo(null);
        robin1.setResizable(true);
      
        	
	   
   }
}