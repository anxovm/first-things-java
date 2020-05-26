package juegoS;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;


public class Snake extends JFrame{
	
	//Variables para las dimensiones de la ventana
	private int width = 640;
	private int height = 480;
	
	boolean gameOver = false;
	//direccion por defecto
	int direccion = KeyEvent.VK_LEFT;
	long frecuencia = 40;
	
	private int widthPoint = 10;
	private int heightPoint = 10;
	
	ArrayList<Point> lista = new ArrayList<Point>();
	
	public Point snake;
	public Point comida;
	
	ImagenSnake imagenSnake;
	public Snake() {
		//Ponemos un titulo a la ventana
		setTitle("Snake");
		
		//Dimensiones de la ventana
		setSize(width,height);
		
		/*Con esto hacemos que al ejecutar el programa, la ventana
		se situe en el centro de la pantalla*/
		Dimension din = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(din.width/2-width/2, din.height/2-height/2);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Teclas teclas = new Teclas();
		this.addKeyListener(teclas);
		
		startGame();
		
		imagenSnake = new ImagenSnake();
		this.getContentPane().add(imagenSnake);
		
		setVisible(true);
		
		//Aqui instanciamos el objeto momento que es el que maneja el hilo del programa
		Momento momento = new Momento();
		Thread trid = new Thread(momento);
		trid.start();
	
	}
	
	public void startGame() {
		comida = new Point(200,200);
		snake = new Point(width/2,height/2);
		
		lista = new ArrayList<Point>();
		lista.add(snake);
		
		crearComida();
	}
	
	public void crearComida() {
		Random rnd = new Random();
		
		comida.x = (rnd.nextInt(width))+5;
		if((comida.x % 5)>0) {
			comida.x = comida.x - (comida.x % 5);
		}
		
		if(comida.x < 5) {
			comida.x = comida.x + 10;
		}
		if(comida.x > width) {
			comida.x = comida.x - 10;
		}
		comida.y = (rnd.nextInt(height))+5;
		if((comida.y % 5)>0) {
			comida.y = comida.y - (comida.y % 5);
		}
		
		if(comida.y < 0) {
			comida.y = comida.y + 10;
		}
		if(comida.y > height) {
			comida.y = comida.y - 10;
		}
	}
	
	//Funcion main en la que se ejecutara el programa
	public static void main(String[] args) {
		Snake s = new Snake();
	}
	
	public void actualizar() {
		imagenSnake.repaint();
		
		lista.add(0, new Point(snake.x, snake.y));
		lista.remove((lista.size() - 1));		
		
		for(int i = 1; i<lista.size();i++) {
			Point punto = lista.get(i);
			if(snake.x == punto.x && snake.y == punto.y) {
				gameOver = true;
			}
		}
		if((snake.x > (comida.x-10) ) && snake.x < (comida.x +10) && snake.y > (comida.y - 10) && snake.y < (comida.y + 10)) {
			lista.add(0, new Point(snake.x,snake.y));
			crearComida();
		}
	}
	
	//Funcion para hacer la serpiente
	public class ImagenSnake extends JPanel{
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setColor(new Color(0,0,255));
			g.fillRect(snake.x, snake.y, widthPoint, heightPoint);
			for(int i=0;i<lista.size();i++) {
				Point p = (Point)lista.get(i);
				g.fillRect(p.x, p.y, widthPoint, heightPoint);
			}
			
			
			
			g.setColor(new Color(255,0,0));
			g.fillRect(comida.x, comida.y, widthPoint, heightPoint);
			if(gameOver) {
				g.drawString("Game Over", 200,320);
			}
		}
	}
	
	public class Teclas extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			//Si presionamos la tecla escape, salimas del programa
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}else if(e.getKeyCode() == KeyEvent.VK_UP) {
				/*La tecla por defecto es left
				 * Aqui controlamos que si controlamos que si precionamos
				 * la tecla arriba, no funcione la tecla abajo
				 * Esto mismo se aplica a todas las direcciones siguientes*/
				if(direccion != KeyEvent.VK_DOWN) {
					direccion = KeyEvent.VK_UP;
				}
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(direccion != KeyEvent.VK_UP) {
					direccion = KeyEvent.VK_DOWN;
				}
				
			}else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if(direccion != KeyEvent.VK_LEFT) {
					direccion = KeyEvent.VK_RIGHT;
				}
				
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				if(direccion != KeyEvent.VK_RIGHT) {
					direccion = KeyEvent.VK_LEFT;
				}	
			}
		}
	}
	
	
	public class Momento extends Thread{
		long last = 0;
		public void run() {
			while(true) {
				//bucle infinito donde hacemos que el juego se refresque cada 20 milisegundos
				if((java.lang.System.currentTimeMillis() - last) > frecuencia) {
					/*Si la direccion es hacia arriba, el rectangula se movera en la direccion y
					 * Esto se aplica a todas las direcciones*/
					if(!gameOver) {
						if(direccion == KeyEvent.VK_UP) {
							snake.y = snake.y - heightPoint;
							//Si se pasa del borde, aparecera abajo
							if(snake.y > height) {
								snake.y = 0;
							}
							//sino, que siga subiendo
							if(snake.y < 0) {
								snake.y = height - heightPoint;
							}
						} else if(direccion == KeyEvent.VK_DOWN) {
							snake.y = snake.y + heightPoint;
							if(snake.y > height) {
								snake.y = 0;
							}
							if(snake.y < 0) {
								snake.y = height - heightPoint;
							}
						} else if(direccion == KeyEvent.VK_RIGHT) {
							snake.x = snake.x + widthPoint;
							if(snake.x > width) {
								snake.x = 0;
							}
							if(snake.x < 0) {
								snake.x = width - widthPoint;
							}
						} else if(direccion == KeyEvent.VK_LEFT) {
							snake.x = snake.x - heightPoint;
							if(snake.y > width) {
								snake.x = 0;
							}
							if(snake.x < 0) {
								snake.x = width - widthPoint;
							}
						}
					}
					//Funcion que vuelve a dibujar a snake, con lo que cada 20 milisegundos se vuelve a dibujar
					actualizar();
					last = java.lang.System.currentTimeMillis();
				}
			}
		}
	}

}
