import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

enum Color{
    DEFAULT("\033[0m"),
    BLACK("\033[0;30m"),    // BLACK
    RED("\033[0;31m"),      // RED
    GREEN("\033[0;32m"),    // GREEN
    YELLOW("\033[0;33m"),   // YELLOW
    BLUE("\033[0;34m"),     // BLUE
    MAGENTA("\033[0;35m"),  // MAGENTA
    CYAN("\033[0;36m"),     // CYAN
    WHITE("\033[0;37m"),    // WHITE

    BLACK_BOLD("\033[1;30m"),   // BLACK
    RED_BOLD("\033[1;31m"),     // RED
    GREEN_BOLD("\033[1;32m"),   // GREEN
    YELLOW_BOLD("\033[1;33m"),  // YELLOW
    BLUE_BOLD("\033[1;34m"),    // BLUE
    MAGENTA_BOLD("\033[1;35m"), // MAGENTA
    CYAN_BOLD("\033[1;36m"),    // CYAN
    WHITE_BOLD("\033[1;37m");   // WHITE


    private final String code;
    Color(String code){
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}


class Board {
    int[][] board = new int[5][5];
    int[] checked = new int[25];
    int c = 0, i = 0, no, q, r;

    void cls() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int getinput() {
        try {
            Scanner sc = new Scanner(System.in);
            return sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invlid option entered. Please enter a valid option");
            return getinput();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    String geString(){
        try {
            Scanner sc = new Scanner(System.in);
            return sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invlid option entered. Please enter a valid option");
            return geString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Main board
    void random() {
        for (i = 1; i <= 25; i++) {
            while (c < 5) {
                no = randpos();
                // System.out.println(no+" rand");
                if (board[no / 5][no % 5] == 0) {
                    board[no / 5][no % 5] = i;
                    break;
                } else {
                    c++;
                }
            }
            if (c == 5) {
                // System.out.println(i+"=i");
                if (passEmpty(i))
                    c = 0;
                else {
                    System.out.println("Error found in passing to empty spot.!No empty spot found.");
                }
            }
        }
        cls();
        System.out.println("\n\nYour board is generated randomly below.\n\n");
        printBoard();
    }

    boolean passEmpty(int val) {
        int x, y;
        for (x = 0; x < 5; x++)
            for (y = 0; y < 5; y++)
                if (board[x][y] == 0) {
                    board[x][y] = val;
                    return true;
                }
        return false;
    }

    int randpos() {
        // [loc/5][(loc%5)-1]
        return new Random().nextInt(0, 25);
    }

    boolean check(int val) {
        for (int j = 0; j < 5; j++)
            for (int k = 0; k < 5; k++)
                if (board[j][k] == val)
                    return true;
        return false;
    }

    void custom() {
        boolean done;
        int temp;
        System.out.println("Enter the values for your board starting from the first position. The range is 1-25.\n");
        for (int i = 0; i < 25; i++) {
            done = false;
            while (!done) {
                System.out.println("Enter value for position " + (i + 1) + ": ");
                temp = getinput();
                if (temp < 1 || temp > 25)
                    System.out.println("Enter a value which is in the range 1-25 only.");
                else if (!check(temp)) {
                    board[i / 5][i % 5] = temp;
                    done = true;
                } else
                    System.out.println("Enter a new value which is not already in use.");
            }
        }
        cls();
        System.out.println("\n\nYour board is generated randomly below.\n\n");
        printBoard();
    }

    void printBoard() {
        for (i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++)
                System.out.print(String.format("%5d ", board[i][j]));
            System.out.println();
        }
    }

    void inPlayPrint(String player){
        System.out.println("\n\nYou are "+Color.RED_BOLD+"Player "+player+Color.DEFAULT+".");
        System.out.println("Color Support:\n"+Color.CYAN+"> 0"+Color.DEFAULT+" - Already marked.\n\n");
        printBoard();
        System.out.println();
    }

    int markDone(int n){
        for(int i=0;i<25;i++)
        if(checked[i] == 0){
        return checked[i] = n;
        }
        return 1;
    }
    
    boolean alreadyDone(int n){
        for(int i=0;i<25;i++){
            if(checked[i] == n){
                return true;
            }
        }
        markDone(n);
        return false;
    }

    String getColor(int n){
        for(int i=0;i<25;i++)
        if(checked[i] == n) return String.format(Color.CYAN+"%5d "+Color.DEFAULT, n);
        return String.format(Color.DEFAULT+"%5d "+Color.DEFAULT, n);
    }

    void midGameBoard(String player){
        cls();
        System.out.println("\n\nYou are "+Color.RED_BOLD+"Player "+player+Color.DEFAULT+".");
        System.out.println("Color Support:\n"+Color.CYAN+"> 0"+Color.DEFAULT+" - Already marked.\n\n");
        for (i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++)
                System.out.print(getColor(board[i][j]));
            System.out.println();
            System.out.println();
        }
    }
}


class Misc extends Thread {
    public void run() {
        System.out.println();
        while (true) {
            System.out.print("Waiting for connetion");
            for (int ll = 0; ll < 5; ll++) {
                System.out.print(".");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    return;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            System.out.print("\r                                          \r");
        }
    }
}


class Start extends Board {
    String intro = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n\n";
    int option = 0;

    boolean start() {
        System.out.println(Color.GREEN + intro + Color.RED);
        System.out.println("----     ----     ----      --     ----          ----");
        System.out.println("--  --    --      -- --     --    --  --       --    --");
        System.out.println("--  --    --      --  --    --   --            --    --");
        System.out.println("----      --      --   --   --   --   -----    --    --");
        System.out.println("--  --    --      --    --  --   --   ----     --    --");
        System.out.println("--  --    --      --     -- --    --  --       --    --");
        System.out.println("----     ----     --      ----     ----          ----\n\n");
        System.out.print(Color.GREEN + intro + Color.DEFAULT);
        System.out.println("Enter one of the follwing options:\n\n1. Generate a random board.\n2. Enter your own values.\n3. Exit\n");

        option = getinput();
        while (true)
            switch (option) {
                case 1:
                    random();
                    return true;
                case 2:
                    custom();
                    return true;
                case 3:
                    System.out.println("Ended.");
                    return false;
                default:
                    System.out.println("Invalid option entered.\nEnter a valid option:");
                    option = getinput();
            }
    }

}

class NewConnection {
    Start start = new Start();
    NewConnection(Start s){
        this.start = s;
    }
    boolean client(Misc mm) {
        try {
            Socket sock = new Socket("localhost", 5000);
            DataInputStream din = new DataInputStream(sock.getInputStream());
            DataOutputStream dout = new DataOutputStream(sock.getOutputStream());
            Scanner sc = new Scanner(System.in);
            mm.interrupt();
            start.cls();
            start.inPlayPrint("1");
            int out;
            String in;
            while(true){
                System.out.println("Enter a number to scratch out: ");
                while(true){
                    out = start.getinput();
                    if(out==0 || out > 25) System.out.println("Enter a number in the given range only 1-25 :");
                    else if(start.alreadyDone(out)) System.out.println(out + " is marked already. Please enter another number which is not marked:");
                    else break;
                }
                dout.writeUTF(String.valueOf(out));
                dout.flush();
                start.markDone(out);
                start.midGameBoard("1");
                in = din.readUTF();
                if(in=="end")break;
                out = Integer.parseInt(in);
                start.markDone(out);
                start.midGameBoard("1");
            }
            dout.close();
            sock.close();
            return true;
        }catch(ConnectException e){
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    void server(Misc mm){
        try{
            ServerSocket ss = new ServerSocket(5000);
            Socket s = ss.accept();
            mm.interrupt();
            start.cls();
            start.inPlayPrint("2");
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            Scanner sc = new Scanner(System.in);
            int number;
            String in;
            while(true){
                in = din.readUTF();
                number = Integer.parseInt(in);
                System.out.println("Client: "+number);
                start.markDone(number);
                start.midGameBoard("2");
                System.out.println("Enter a number to scratch out: ");
                while(true){
                    number = start.getinput();
                    if(number==0 || number > 25) System.out.println("Enter a number in the given range only 1-25 :");
                    else if(start.alreadyDone(number)) System.out.println(number + " is marked already. Please enter another number which is not marked:");
                    else break;
                }
                if(in=="!!")break;
                //number = sc.nextInt();
                dout.writeUTF(String.valueOf(number));
                dout.flush();
                start.midGameBoard("2");
            }
            try{
                din.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            s.close();
            ss.close();
            dout.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class Main {
    public static void main(String a[]) {
        Start s = new Start();
        s.cls();
        if (!s.start())
            return;

        Misc mm = new Misc();
        mm.start();
        NewConnection nc = new NewConnection(s);
        if(!nc.client(mm)){
            nc.server(mm);
        }
        

    }
}