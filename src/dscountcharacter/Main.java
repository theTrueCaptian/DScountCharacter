package dscountcharacter;
// GUI-related imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// File-related imports
import java.io.*; 		// both needed
import java.util.*;

/**
 * Maeda Hanafi  2/10/10
 */

public class Main extends Frame implements ActionListener{
    // File Parameters
    String dataFilePath = null;
    File dataFileName = null;
    int[] count = new int[256];
    int word = 0;
    int[] CharArray = new int[256];
    JTextArea textArea;

    // Retrieved command code
    String command = "";

    public static void main(String[] args){
        Frame frame = new Main();

        frame.setResizable(true);
        frame.setSize(1000,700);
        frame.setVisible(true);
    }

    public Main(){
        setTitle("Text File Processing");

        // Create Menu
        MenuBar mb = new MenuBar();
        setMenuBar(mb);

        Menu menu = new Menu("File");
        mb.add(menu);

        MenuItem miColor = new MenuItem("Open");
        miColor.addActionListener(this);
        menu.add(miColor);

        MenuItem miLine = new MenuItem("Process");
        miLine.addActionListener(this);
        menu.add(miLine);

        MenuItem wordCounter = new MenuItem("Word Count");
        wordCounter.addActionListener(this);
        menu.add(wordCounter);

        MenuItem miExit = new MenuItem("Exit");
        miExit.addActionListener(this);
        menu.add(miExit);

        // End program when window is closed
        WindowListener l = new WindowAdapter(){
            public void windowClosing(WindowEvent ev){
                System.exit(0);
            }
            public void windowActivated(WindowEvent ev){
                repaint();
            }
            public void windowStateChanged(WindowEvent ev){
                repaint();
            }
        };

        ComponentListener k = new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                repaint();
            }
        };

        // register listeners
        this.addWindowListener(l);
        this.addComponentListener(k);

        //to display result
        textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.PLAIN, 16));
        textArea.setEditable(false);
        this.add(textArea);
        this.pack();
        this.setVisible(true);

        //scollbar
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.CENTER);

        

    }

//******************************************************************************
//  called by windows manager whenever the application window performs an action
//  (select a menu item, close, resize, ....
//******************************************************************************

    public void actionPerformed (ActionEvent ev){
        // figure out which command was issued
        command = ev.getActionCommand();

        // take action accordingly
        if("Open".equals(command)){
            dataFilePath = null;
            dataFileName = null;

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogType(JFileChooser.OPEN_DIALOG );
            chooser.setDialogTitle("Open Data File");

            int returnVal = chooser.showOpenDialog(null);
            if( returnVal == JFileChooser.APPROVE_OPTION){
                  dataFilePath = chooser.getSelectedFile().getPath();
                  dataFileName = chooser.getSelectedFile();
            }
          
            repaint();
        }else if("Process".equals(command)){
            Char(dataFileName);
            //display results         
            repaint();
        }else if("Word Count".equals(command)){
               countWord(dataFileName);
               repaint();
        }else{
            if("Exit".equals(command)){
                System.exit(0);
            }
        }
    }
//********************************************************
// called by repaint() to redraw the screen
//********************************************************

    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        if("Open".equals(command)){
            // Acknowledge that file was opened
            String result;
            if (dataFileName != null){
                result = "File --  "+ dataFileName +"  -- was successfully opened";
            }else{
                result = "NO File is Open";
            }
            textArea.setText(result);
            return;
        }else if("Process".equals(command)){
            // Display the results
            String result = "Index\tCharacter \t Frequency\n";
            
            for(int k=0; k<=255; k++){
            	if(k>31 && k!=127){  //display only printable characters
                    result += k + "\t" + (char)k + "\t\t" + CharArray[k] + "\n";
            	}else{
                    result += k + "\t" + " " + "\t\t" + CharArray[k] + "\n";
                }
            }
            //display
            textArea.setText(result);
            //send to text file Result
            dumpToResultFile(result);
            return;
        }else if("Word Count".equals(command)){
            // Display the results
            String result = "The number of words is " + word;
            textArea.setText(result);
            return;

        }

    }

    public void Char(File inFileBuffer){
        StringBuffer theContents = new StringBuffer();
        String myBuffer = theContents.toString();
        Scanner reader;
        try {
            reader = new Scanner(inFileBuffer);
            while(reader.hasNext()){
                myBuffer = myBuffer + reader.next();
            }
            reader.close();
        } catch (FileNotFoundException ex) {
        }
        for (int i=0; i < myBuffer.length(); i++) {
            char thisChar = myBuffer.charAt(i);
            collectChar(thisChar);

        }
    }

    public void collectChar(char inChar){
        int index = (int)inChar;
        CharArray[index] = CharArray[index] +1;
    }


    public void countWord(File inFileBuffer){
        Scanner reader;
        try {
            reader = new Scanner(inFileBuffer);
            while(reader.hasNext()){
                word++;
                System.out.println(reader.next() + " ===> " + word);
            }
            reader.close();
        } catch (FileNotFoundException ex) {
        }

    }
   
    public void dumpToResultFile(String stringResult){
    	PrintWriter output = null;
        try {
            File file = new File("F:\\DScountCharacter\\RESULTS.txt");
            output = new PrintWriter(file);
        } catch (FileNotFoundException ex) {
        }
        output.print(stringResult);
        output.close();
    }

    
}