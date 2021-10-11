package Imitator;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;







public class ImitatorBrain2 {
	
	
	//Arduino config
	public static final int DATA_RATE = 9600;
	public static final int TIMEOUT = 2000;
	//kasnjenje [ms]
	private static final long DELAY = 10;
	//broj ledica
	public static final int LEDS_NUM = 30;
	//broj ledica po sekciji
	public static final int LEDS_PER_SECTION = 3;
	public static final int SECTIONS = LEDS_NUM / LEDS_PER_SECTION;
	//automatsko prepoznavanje rezolucije
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public int X_RES = (int)screenSize.getWidth();
	public int Y_RES = (int)screenSize.getHeight();
	//sirina i visina sekcija
	public int SECT_WIDTH = X_RES / SECTIONS;
	public int SECT_HEIGHT = Y_RES;
	//preskakanje svakih 10 piksela
	public static final int SECT_SKIP = 10;
	// robot ya citanje ekrana
	private Robot robot;
	// arduino communication
	private SerialPort serial;
	private OutputStream output;

	/**
	 * init arduino communication
	 */
	private void initSerial() {
	CommPortIdentifier serialPortId = null;
	Enumeration enumComm = CommPortIdentifier.getPortIdentifiers();
	while (enumComm.hasMoreElements() && serialPortId == null) {
	serialPortId = (CommPortIdentifier) enumComm.nextElement();
	}
	try {
	serial = (SerialPort) serialPortId.open(this.getClass().getName(),
	TIMEOUT);
	serial.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
	SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	} catch (PortInUseException | UnsupportedCommOperationException e) {
	e.printStackTrace();
	}
	}
	
	/**
	 * init the robot
	 */
	private void initRobot() {
	try {
	robot = new Robot();
	} catch (AWTException e) {
	e.printStackTrace();
	}
	}
	/**
	 * init arduino output
	 */
	private void initOutputStream() {
	try {
	output = serial.getOutputStream();
	} catch (IOException e) {
	e.printStackTrace();
	}
	}
	/**
	 * read colors from the screen
	 * 
	 * @return array with colors that will be send to arduino
	 * @throws InterruptedException 
	 */
	
	 //screen je cijeli ekran/rezolucija
	public volatile Color[] leds = new Color[SECTIONS]; // leds je POLJE od 10 radi SECTIONA tipa podatka Color - tu se spremaju svih 10 prosjecnih boja
	
	
	
	public Color[] getColors(){
	BufferedImage screen = robot.createScreenCapture(new Rectangle(new Dimension(X_RES, Y_RES)));
	
	class dretva implements Runnable{
		
		public void run(){
			for (int led = 0; led < (SECTIONS/2); led++) {
				BufferedImage sections = screen.getSubimage(led * SECT_WIDTH, 0, SECT_WIDTH, SECT_HEIGHT);
				Color sectionAvgColor = getAvgColor(sections);
				leds[led] = sectionAvgColor;
				}
				}
				
		}
		
	dretva posao = new dretva();
	Thread thread = new Thread(posao); 
	thread.start();
	
	for (int led = (SECTIONS/2); led < SECTIONS; led++) {
	BufferedImage section = screen.getSubimage(led * SECT_WIDTH, 0, SECT_WIDTH, SECT_HEIGHT);
	Color sectionAvgColor = getAvgColor(section);
	leds[led] = sectionAvgColor;
	}
	try {
		thread.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return leds;
	}
	
	/**
	 * calculate average color for section
	 * @throws InterruptedException 
	 */
	
	
	
	private Color getAvgColor(BufferedImage imgSection){ 
	int width = imgSection.getWidth(); //sekcija sirine
	int height = imgSection.getHeight(); //sekcija visine
	int r = 0, g = 0, b = 0; //rgb boje
	int loops = 0;
	for (int x = 0; x < width; x += SECT_SKIP) { //brojac od nultog pixela do pola sirine
	for (int y = 0; y < height; y += SECT_SKIP) { //brojac do visine u istoj petlji
	int rgb = imgSection.getRGB(x, y);
	Color color = new Color(rgb);
	r += color.getRed();
	g += color.getGreen();
	b += color.getBlue();
	loops++;
	}
	}
	r = r / loops;
	g = g / loops;
	b = b / loops;
	
	return new Color(r, g, b);
	}
	
	/**
	 * Send the data to Arduino
	 */
	public void sendColors(Color[] leds) {
	try {
	output.write(0xff);
	for (int i = 0; i < SECTIONS; i++) {
	output.write(leds[i].getRed());
	output.write(leds[i].getGreen());
	output.write(leds[i].getBlue());
	}
	} catch (IOException e) {
	e.printStackTrace();
	}
	}
	
	
	
	//Main Loop
	private void loop(){
	while (true) {
	Color[] leds = getColors();
	sendColors(leds);
	try {
	Thread.sleep(DELAY);
	} catch (InterruptedException e) {
	e.printStackTrace();
	}
	}
	}
	
	
	public static void main(String[] args){
	ImitatorBrain2 imitator = new ImitatorBrain2();
	imitator.initRobot();
	imitator.initSerial();
	imitator.initOutputStream();
	imitator.loop();
	}

}