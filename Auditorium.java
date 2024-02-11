//Amogh Anoo ASA210011

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

public class Auditorium {
    private Node First;
    private int numRows;
    private int numColumns;

    public Auditorium() {

    }

    public Auditorium(String fileName) throws FileNotFoundException {
        numRows = 0;
        String tempStr = "";
        Scanner scnr = new Scanner(new File(fileName));
        First = new Node();
        Node head = First;


        while (scnr.hasNextLine()) {
            char columnLetter = 'A'; // Initialize columnLetter to 'A'
            tempStr = scnr.nextLine();
            numRows++;
            numColumns = tempStr.length();

            for (int i = 0; i < tempStr.length(); i++) {
                // Assuming your append method is correct, call it like this:
                append(head, numRows, columnLetter, tempStr.charAt(i));
                columnLetter++; // Increment columnLetter to move to the next column
            }

            head.setDown(new Node());
            head = head.getDown();
        }
    }

    public void append(Node H, int rowNum, char column, char type) {
        Node current = H;

        // Check if the current node's payload is null
        if (current.getPayload() == null) {
            Seat newSeat = new Seat(rowNum, column, type);
            current.setPayload(newSeat);
        } else {
            // Traverse the linked list to find the last node
            while (current.getNext() != null) {
                current = current.getNext();
            }

            // Create a new node and set its payload to a new Seat
            current.setNext(new Node());
            Seat newSeat = new Seat(rowNum, column, type);
            current.getNext().setPayload(newSeat);
        }

    }

    public void displayAuditorium() {
        Node currentRow = First;
        boolean hasNextRow = true;

        System.out.print(" ");
        for (int i = 0; i < numColumns; i++) {
            System.out.print((char) (i + 65));
        }
        System.out.println();
        int rowOutput = 1;
        while (currentRow != null) {

            if (rowOutput <= numRows) {
                System.out.print(rowOutput);
            }

            rowOutput++;
            Node currentSeat = currentRow;

            while (currentSeat != null) {
                Seat seat = currentSeat.getPayload();
                if (seat != null) {
                    // Print the seat information based on its type
                    char seatType = seat.getTicketType();
                    switch (seatType) {
                        case 'A':
                            System.out.print("#");
                            break;
                        case 'C':
                            System.out.print("#");
                            break;
                        case 'S':
                            System.out.print("#");
                            break;
                        default:
                            System.out.print("."); // Empty seat
                            break;
                    }
                }
                currentSeat = currentSeat.getNext();

            }

            // Check if there's another row to print
            if (currentRow.getNext() != null) {
                System.out.println(); // Move to the next row
                currentRow = currentRow.getDown();
            } else {
                hasNextRow = false; // No more rows to print
                break;
            }
        }

        if (hasNextRow) {
            System.out.println(); // Print a newline if there's another row
        }
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getNumRows() {
        return numRows;
    }

    public boolean checkAvailability(int r, char seat, int total) {
        Node cur = First;
        int seatNum = (seat - 'A');
        //go to the seat wanting to be checked
        for (int i = 1; i < r; i++) {
            cur = cur.getDown();
        }
        //traverse right
        for (int i = 0; i < seatNum; i++) {
            cur = cur.getNext();
        }
        //check for the total number of the seats wanted if they are fre
        for (int i = 0; i < total; i++) {
            if (cur.getPayload().getTicketType() != '.') {
                return false;
            }
            cur = cur.getNext();
        }
        return true;
    }
    public char remove_reservation(int r, char seat){
        //traverse to the seat
        Node cur = First;
        int seatNum = (seat - 'A');
        //go to the seat wanting to be checked
        for (int i = 1; i < r; i++) {
            cur = cur.getDown();
        }
        //traverse right
        for (int i = 0; i < seatNum; i++) {
            cur = cur.getNext();
        }
        //hold the type fo ticket and then return it so it can be taken away from totals
        char tempChar = cur.getPayload().getTicketType();
        cur.getPayload().setTicketType('.');
        return tempChar;
    }

    public Node getFirst() {
        return First;
    }

    public void exitProgram(String filename) throws FileNotFoundException {
        FileOutputStream os = new FileOutputStream(filename);
        PrintWriter outScan = new PrintWriter(os);

        Node currentRow = First;
        boolean hasNextRow = true;

        //loop throuhg the linked lists and get the information about each of the nodes
        while (currentRow != null) {
            Node currentSeat = currentRow;

            while (currentSeat != null) {
                Seat seat = currentSeat.getPayload();
                if (seat != null) {
                    // Print the seat information based on its type
                    char seatType = seat.getTicketType();
                    switch (seatType) {
                        case 'A':
                            outScan.print("A");
                            break;
                        case 'C':
                            outScan.print("C");
                            break;
                        case 'S':
                            outScan.print("S");
                            break;
                        default:
                            outScan.print("."); // Empty seat
                            break;
                    }
                }
                currentSeat = currentSeat.getNext();

            }

            // Check if there's another row to print
            if (currentRow.getDown() != null) {
                outScan.println(); // Move to the next row
                currentRow = currentRow.getDown();
            } else {
                hasNextRow = false; // No more rows to print
                break;
            }
        }

        if (hasNextRow) {
            outScan.println(); // Print a newline if there's another row
        }
        outScan.close();

    }

    public Seat bestAvailable(int numSeats) {
        //declaring variables
        double centerRow = (numRows + 1) / 2.0;
        double distance = 10000.0;
        double distCheck = 0;
        boolean isFree = true;
        Node middleSelection = new Node();
        Seat bestSeat = new Seat(-1, 'Z', ' ');
        Node head = First;
        Node cur = head;
        Node checker = cur;

        //calculate the center row
        //calculate the column in the center
        double centerColumn = (numColumns + 1) / 2.0;

        //out loop for the rows
        for (int i = 0; i < numRows; i++) {
            //inner loop for each seat in the row
            cur = head;
            for (int j = 0; j < numColumns - numSeats + 1; j++) {
                checker = cur;
                //check the seats wanted in front of the current seat
                for (int u = 0; u < numSeats; u++) {
                    //if any are not free
                    if (checker.getPayload().getTicketType() != '.') {
                        isFree = false;
                    }
                    checker = checker.getNext();
                }
                //if all of them are free
                if (isFree) {
                    //check the distance using the middle of the row
                    double centerSeat = (cur.getPayload().getSeat() - 65) + ((numSeats+1)/2.0);
                    distCheck = Math.sqrt((centerSeat - centerColumn) * (centerSeat - centerColumn)*(1.0) +
                            (cur.getPayload().getRow() - centerRow) * (cur.getPayload().getRow() - centerRow)*(1.0));
                    //if the distance is less, use new seat

                    //if they are equal, check for row closest to the center

                    if (distCheck == distance) {
                        //if the current row is smaller, use it
                        if (Math.abs(cur.getPayload().getRow() - centerRow) < Math.abs(bestSeat.getRow() - centerRow)) {
                            bestSeat = cur.getPayload();
                        }
                        //if they are equally distant
                        else if (cur.getPayload().getRow() < bestSeat.getRow()) {
                            bestSeat = cur.getPayload();

                        }
                    }
                    //if the distance checked is less than the current lowest
                    else if (distCheck < distance) {
                        distance = distCheck;
                        bestSeat = cur.getPayload();
                    }


                }
                //reset value of the boolean and move forward.
                isFree = true;
                cur = cur.getNext();
            }
            head = head.getDown();
        }
        return bestSeat;
    }

    public double adminReport(int [] array){
        int numAdults = 0;
        int numSeniors = 0;
        int numChild = 0;
        int numOpen = 0;
        int numReserved = 0;
        Node currentRow = First;
        boolean hasNextRow = true;
        int rowOutput = 1;
        while (currentRow != null) {
            Node currentSeat = currentRow;
            while (currentSeat != null) {
                Seat seat = currentSeat.getPayload();
                if (seat != null) {
                    // Print the seat information based on its type
                    char seatType = seat.getTicketType();
                    switch (seatType) {
                        case 'A':
                            array[2]++;;
                            numAdults++;
                            numReserved++;
                           array[1]++;;
                            break;
                        case 'C':
                           array[3]++;;
                            numChild++;
                            numReserved++;
                           array[1]++;;
                            break;
                        case 'S':
                           array[4]++;;
                            numSeniors++;
                           array[1]++;;
                            numReserved++;
                            break;
                        default:
                           array[0]++;; // Empty seat
                            numOpen++;
                            break;
                    }
                }
                currentSeat = currentSeat.getNext();
            }
            // Check if there's another row to print
            if (currentRow.getNext() != null) {
                currentRow = currentRow.getDown();
            } else {
                hasNextRow = false; // No more rows to print
                break;
            }
        }
        System.out.print(numOpen + "\t" + numReserved + "\t" + numAdults + "\t" + numChild + "\t" + numSeniors + "\t" + "$");
        double total = (numAdults*10) + (numSeniors * 7.5) + (numChild*5);
        System.out.printf("%.2f", total);
        System.out.println();
        return total;
    }
}
