//Amogh Anoo ASA210011

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        int numRows = 0;
        int numColumns = 0;
        int userInput = 0;
        int rowNumber = 0;
        char startingSeatLetter = 'a';
        int adultTickets = -1;
        int childTickets = -1;
        int seniorTickets = -1;
        char userChoice = '1';
        int auditoriumSelection = 0;

        Auditorium[] auditoriums = new Auditorium[3];
        try{
            auditoriums[0] = new Auditorium("A1.txt");
            auditoriums[1] = new Auditorium("A2.txt");
            auditoriums[2] = new Auditorium("A3.txt");
        }
        catch(FileNotFoundException e) {
            System.out.println("Did not find input files");
        }

        //open the auditorium and populate it.
        Scanner input = new Scanner(System.in);
        HashMap<String, Reservation> reservations = new HashMap<String, Reservation>();
        Scanner users = null;
        try {
            users = new Scanner(new File("userdb.dat"));
        }
        catch (FileNotFoundException e){
            System.out.println("Did not find user file");
        }

        //grab the line and split it into username and password
        while (users.hasNextLine()) {
            String temp = users.nextLine();
            String[] stuff = temp.split(" ");
            reservations.put(stuff[0], new Reservation(stuff[0], stuff[1]));
        }

        boolean exitProgram = false;
        //while not exiting the program
        while (!exitProgram) {

            int maxAttempts = 3;
            //take in the username and extract the reservation from the HashMap
            System.out.println("Enter a username:");
            String usernameInput = input.nextLine();
            Reservation curReservation = reservations.get(usernameInput);
            int incorrectPasswordAttempts = 0;
            //asks for incorrect passwords and stops at 3, then reprompts the username
            while (incorrectPasswordAttempts < maxAttempts) {
                System.out.println("Enter a password:");
                String passwordInput = input.nextLine();
                //if the 2 are not equal, then reprompt
                if (!Objects.equals(passwordInput, curReservation.password)) {
                    System.out.println("Invalid password");
                    incorrectPasswordAttempts++;
                    //if it hits 3
                    if (incorrectPasswordAttempts == maxAttempts) {
                        System.out.println("Please enter a new username:");
                        usernameInput = input.nextLine();
                        curReservation = reservations.get(usernameInput);
                        incorrectPasswordAttempts = 0; // Reset the counter
                    }
                } else {
                    // Correct password
                    break;
                }
            }
            //now the correct username and password has been entered
            if (Objects.equals(usernameInput, "admin")) {
                boolean loggedIn = true;
                while (loggedIn) {
                    System.out.println("1. Print Report");
                    System.out.println("2. Logout");
                    System.out.println("3. Exit");
                    //take in the user's choice
                    try {
                        userInput = input.nextInt();
                        if (userInput < 1 || userInput > 3) {
                            throw new InputMismatchException();
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Please input a valid integer.");
                        input.nextLine();
                    }
                    if (userInput == 1) {
                        //0 open, 1 reserved, 2 adults, 3 child, 4 senior
                        int[] array = new int[5];
                        double totalCost = 0;
                        for (int i = 0; i < 3; i++) {
                            System.out.print("Auditorium " + (i + 1) + "\t");
                            //load in the admin report for the auditoriums and print it
                            totalCost += auditoriums[i].adminReport(array);
                        }
                        //output the final admin table at the very bottom
                        System.out.print("Total\t");
                        System.out.print(array[0] + "\t" + array[1] + "\t" + array[2] + "\t" + array[3] + "\t" + array[4] + "\t" + "$");
                        System.out.printf("%.2f", totalCost);
                        System.out.println();
                        //if logging out
                    } else if (userInput == 2) {
                        loggedIn = false;
                        input.nextLine();
                        //if ending the program
                    } else {
                        auditoriums[0].exitProgram("A1Final.txt");
                        auditoriums[1].exitProgram("A2Final.txt");
                        auditoriums[2].exitProgram("A3Final.txt");
                        return;
                    }
                }

            } else {
                // While not quit
                boolean isAvailable = false;
                userInput = 0;
                while (userInput != 5) {
                    System.out.println("1. Reserve Seats");
                    System.out.println("2. View Orders");
                    System.out.println("3. Update Order");
                    System.out.println("4. Display Receipt");
                    System.out.println("5. Log Out");

                    //get an integer
                    try {
                        userInput = input.nextInt();
                        if (userInput < 1 || userInput > 5) {
                            throw new InputMismatchException();
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Please input a valid integer.");
                        input.nextLine();
                    }

                    //if wanting to reserve seats
                    if (userInput == 1) {
                        System.out.println("1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3");
                        boolean getAuditoriumNumber = false;
                        while (!getAuditoriumNumber) {
                            try {
                                auditoriumSelection = input.nextInt();
                                if (auditoriumSelection < 1 || auditoriumSelection > 3) {
                                    throw new InputMismatchException();
                                }
                                getAuditoriumNumber = true;
                            } catch (Exception e) {
                                System.out.println("Please enter a valid auditorium number");
                                input.nextLine();
                            }
                        }
                        //use this auditorium for all the work
                        Auditorium a = auditoriums[auditoriumSelection - 1];
                        numColumns = a.getNumColumns();
                        numRows = a.getNumRows();
                        a.displayAuditorium();
                        // Get information about the reservation
                        rowNumber = 0;
                        //collect the row that they want to reserve seats from
                        while (rowNumber < 1 || rowNumber > numRows) {
                            try {
                                System.out.println("Row Number");
                                rowNumber = input.nextInt();
                                if (rowNumber < 1 || rowNumber > numRows) {
                                    throw new InputMismatchException("Error: Please enter a valid row.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Error: Please enter a valid integer.");
                                input.nextLine();
                            }

                        }
                        //collect user data about their reservation
                        String temp = null;
                        startingSeatLetter = 'a';
                        //collect the letter of the seat
                        while (startingSeatLetter < 'A' || startingSeatLetter > 'Z') {
                            try {
                                System.out.println("Seat Letter");
                                startingSeatLetter = input.next().charAt(0);
                            } catch (InputMismatchException e) {
                                System.out.println("Error: Please enter a valid character");
                                input.nextLine();
                            }

                        }
                        //collect number of adult tickets
                        adultTickets = -1;
                        while (adultTickets < 0) {
                            try {
                                System.out.println("Adult Tickets");
                                adultTickets = input.nextInt();
                                if (adultTickets < 0) {
                                    throw new InputMismatchException();
                                }
                            } catch (InputMismatchException | NumberFormatException e) {
                                System.out.println("Error: Please enter a valid integer.");
                                input.nextLine();
                            }

                        }
                        //collect number of child tickets
                        childTickets = -1;
                        while (childTickets < 0) {
                            try {
                                System.out.println("Child Tickets");
                                childTickets = input.nextInt();
                                if (childTickets < 0) {
                                    throw new InputMismatchException();
                                }
                            } catch (InputMismatchException | NumberFormatException e) {
                                System.out.println("Error: Please enter a valid integer.");
                                input.nextLine();
                            }
                        }
                        seniorTickets = -1;
                        while (seniorTickets < 0) {
                            try {
                                System.out.println("Senior Tickets");
                                seniorTickets = input.nextInt();
                                if (seniorTickets < 0) {
                                    throw new InputMismatchException();
                                }
                            } catch (InputMismatchException | NumberFormatException e) {
                                System.out.println("Error: Please enter a valid integer.");
                                input.nextLine();
                            }
                        }


                        // Check if the seats are available
                        isAvailable = a.checkAvailability(rowNumber, startingSeatLetter, (adultTickets + childTickets + seniorTickets));

                        if (isAvailable) {
                            // Reserve if seats are not filled
                            reserveSeats(rowNumber, startingSeatLetter, adultTickets, seniorTickets, childTickets, a, curReservation, auditoriumSelection);
                            System.out.println("Reserved");
                        } else {
                            //find the best starting seat
                            Seat newBest = new Seat(-1, 'a', 'a');
                            newBest = a.bestAvailable((adultTickets + seniorTickets + childTickets));
                            userChoice = 'a';
                            //if no new best seat is found
                            if (newBest.getRow() != -1) {
                                System.out.println("" + newBest.getRow() + (char) newBest.getSeat() + " - " + newBest.getRow() + (char) (newBest.getSeat() + (adultTickets + childTickets + seniorTickets - 1)));
                                //looping until we get good responses
                                while (userChoice != 'Y' && userChoice != 'N') {
                                    try {
                                        System.out.println("Do you want to reserve these seats?");

                                        userChoice = input.next().charAt(0);
                                        if (userChoice == 'Y') {
                                            //if best seats are wanted, reserve them
                                            reserveSeats(newBest.getRow(), newBest.getSeat(), adultTickets, seniorTickets, childTickets, a, curReservation, auditoriumSelection);
                                            System.out.println("reserved");
                                        } else if (userChoice == 'N') {
                                            //do nothing
                                            continue;
                                        }
                                        //exception for if the input does not match
                                        if (userChoice != 'Y' && userChoice != 'N') {
                                            throw new InputMismatchException();
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Error: Please enter a valid character");
                                        input.nextLine();
                                    }

                                }
                            } else {
                                System.out.println("no seats available");
                            }

                        }
                    }
                    //if they want to see their orders
                    else if (userInput == 2) {
                        if (!curReservation.orders.isEmpty()) {
                            for (int i = 0; i < curReservation.orders.size(); i++) {
                                //sort alphabetically, he print
                                Collections.sort(curReservation.orders.get(i).seats);
                                System.out.println(curReservation.orders.get(i).toString());
                                System.out.println();
                            }
                            //if the orders linked list is empty
                        } else {
                            System.out.println("No orders");
                        }
                    }
                    //if they want to update their order
                    else if (userInput == 3) {
                        //print it if it is not empty
                        if (!curReservation.orders.isEmpty()) {
                            for (int i = 0; i < curReservation.orders.size(); i++) {
                                System.out.print("" + (i + 1) + ". ");
                                System.out.println(curReservation.orders.get(i).toString());
                            }
                            //find the order number that needs to be changed
                            int userOrderNum = getOrderToBeChanged(input, curReservation);
                            int updateUserSelection = 0;
                            boolean hasChange = false;
                            //until the user successfully updates their order, display this menu
                            while (!hasChange) {
                                printUpdateMenu();
                                boolean user_update_choice = false;
                                //take in their choice
                                while (!user_update_choice) {
                                    try {
                                        updateUserSelection = input.nextInt();
                                        if (updateUserSelection < 1 || updateUserSelection > 3) {
                                            throw new InputMismatchException();
                                        }
                                        user_update_choice = true;
                                    } catch (InputMismatchException e) {
                                        input.nextLine();
                                        System.out.println("Please enter a valid choice");
                                    }
                                }
                                //switch for determining which action to take
                                switch (updateUserSelection) {
                                    case 1:
                                        //adding the seat to the order and to the auditorium
                                        hasChange = addSeat(curReservation.orders.get(userOrderNum - 1), input, auditoriums, curReservation);
                                        break;
                                    case 2:
                                        //deleting seat from the auditorium and the order
                                        hasChange = deleteSeat(curReservation.orders.get(userOrderNum - 1), input, auditoriums, curReservation);
                                        break;
                                    case 3:
                                        //wipe the set of seats in that order, as well as deleting the order and decrmenting values used for calculation
                                        hasChange = true;
                                        setAllSeatsToAvailable(curReservation.orders.get(userOrderNum - 1).seats);
                                        curReservation.numSenior -= curReservation.orders.get(userOrderNum - 1).numSeniorSeats;
                                        curReservation.numChildren -= curReservation.orders.get(userOrderNum - 1).numChildSeats;
                                        curReservation.numAdults -= curReservation.orders.get(userOrderNum - 1).numAdultSeats;
                                        curReservation.orders.remove(curReservation.orders.get(userOrderNum - 1));
                                        checkEmptyOrder(curReservation);
                                        System.out.println("Wiped");
                                        break;
                                    default:
                                        hasChange = false;
                                        break;
                                }
                            }
                        }
                    }
                    //else if they want to see their receipts
                    else if (userInput == 4) {
                        //loop throug the orders and calculate their revenue, and display it
                        for (int i = 0; i < curReservation.orders.size(); i++) {
                            if (curReservation.orders.get(i) != null){
                                Collections.sort(curReservation.orders.get(i).seats);
                                System.out.println(curReservation.orders.get(i).toString());
                                System.out.print("Order Total: $");
                                //calculate function
                                double orderTotal = calculate_order_total(curReservation.orders.get(i));
                                System.out.printf("%.2f", orderTotal);
                                System.out.println();
                                System.out.println();
                            }

                        } //output the totals for the all the auditoriums
                        System.out.println();
                        System.out.print("Customer Total: $");
                        double rTotal = calculate_reservation_total(curReservation);
                        System.out.printf("%.2f", rTotal);
                        System.out.println();
                    }
                    else {
                        System.out.println("Logged out");
                        input.nextLine();
                    }
                }
            }

        }
    }

    public static void reserveSeats(int row, char seatNum, int adult, int senior, int child, Auditorium auditorium, Reservation reservation, int AuditoriumNumber) {
        //update reservation numbers for the calculation functions
        reservation.setNumAdults(reservation.getNumAdults() + adult);
        reservation.setNumChildren(reservation.getNumChildren() + child);
        reservation.setNumSenior(reservation.getNumSenior() + senior);
        //create a new order
        order newOrder = new order(adult, senior, child, AuditoriumNumber);
        reservation.orders.add(newOrder);
        int total = adult + senior + child;
        Node cur = auditorium.getFirst();
        int seatNumber = (seatNum - 'A');

        //go down for the number of rows
        for (int i = 1; i < row; i++) {
            cur = cur.getDown();
        }
        //go right for the cur node
        for (int i = 0; i < seatNumber; i++) {
            cur = cur.getNext();
        }
        //distribute the ticket types in descending order
        for (int i = 0; i < total; i++) {
            if (adult > 0) {
                cur.getPayload().setTicketType('A');
                adult--;
            } else if (child > 0) {
                cur.getPayload().setTicketType('C');
                child--;
            } else if (senior > 0) {
                cur.getPayload().setTicketType('S');
                senior--;
            }
            //add the seat to the liked list
            newOrder.seats.add(cur.getPayload());
            if (cur.getNext() != null) {
                cur = cur.getNext();
            }
        }
    }

    //overridden function that is used when adding to an order, so that I don't accidentally create a new order
    public static void reserveSeats(int row, char seatNum, int adult, int senior, int child, Auditorium auditorium, Reservation reservation, int AuditoriumNumber, order order) {
        int total = adult + senior + child;
        Node cur = auditorium.getFirst();
        int seatNumber = (seatNum - 'A');

        //go down for the number of rows
        for (int i = 1; i < row; i++) {
            cur = cur.getDown();
        }
        //go right for the cur node
        for (int i = 0; i < seatNumber; i++) {
            cur = cur.getNext();
        }
        //distribute the ticket types in descending order
        for (int i = 0; i < total; i++) {
            if (adult > 0) {
                cur.getPayload().setTicketType('A');
                adult--;
                order.numAdultSeats++;
                reservation.numAdults++;
            } else if (child > 0) {
                cur.getPayload().setTicketType('C');
                child--;
                order.numChildSeats++;
                reservation.numChildren++;
            } else if (senior > 0) {
                cur.getPayload().setTicketType('S');
                senior--;
                order.numSeniorSeats++;
                reservation.numSenior++;
            }
            order.seats.add(cur.getPayload());
            if (cur.getNext() != null) {
                cur = cur.getNext();
            }
        }
    }

    public static int getOrderToBeChanged(Scanner input, Reservation r) {
        //function exists only so that this ugly piece of code doesn't show up in my main function
        //it's already too indented
        System.out.println("Please choose an order to change");
        boolean isNum = false;
        int userNum = 0;
        while (!isNum) {
            try {
                userNum = input.nextInt();
                if (userNum < 1 || userNum > r.orders.size()) {
                    throw new InputMismatchException();
                }
                isNum = true;
            } catch (InputMismatchException e) {
                System.out.println("Please pick a valid order option");
                input.nextLine();
            }
        }

        return userNum;
    }

    public static void printUpdateMenu() {
        //this is the same as the last one
        System.out.println("1. Add tickets to order");
        System.out.println("2. Delete tickets from order");
        System.out.println("3. Cancel Order");
    }

    public static void setAllSeatsToAvailable(LinkedList<Seat> orders) {
        //when wiping an order, seat all the seats in the order to . so that they are available since they
        //are all pointers to seats in the auditorium anyway
        for (int i = 0; i < orders.size(); i++) {
            orders.get(i).setTicketType('.');
        }
    }

    public static boolean deleteSeat(order order, Scanner input, Auditorium auditoriums[], Reservation reservation) {
        //open necessary auditorium
        Auditorium a = auditoriums[order.getAuditoriumNumber() - 1];
        System.out.println(order.toString());
        System.out.println("Which seat would you like to delete?");
        //take in which seat they want to delete
        String temp = null;
        int rowNum = 0;
        char seatLetter = 'a';
        int rowDelete = 0;
        char columnDelete = 'a';
        try {
            rowNum = input.nextInt();
            seatLetter = input.next().charAt(0);
        } catch (InputMismatchException e) {
            System.out.println("Valid Seat Not Selected");
        } catch (Exception e) {
            //input.nextLine();
            System.out.println("Invalid Input");
        }

        //if the seat is inside the linked list
        boolean is_there = order.seats.contains(new Seat(rowNum, seatLetter, '.'));
        if (is_there) {
            //unreserve the seat and update the totals for the reservation and the order
            char ticket_type = a.remove_reservation(rowNum, seatLetter);
            switch (ticket_type) {
                case 'A':
                    order.numAdultSeats--;
                    reservation.numAdults--;
                    break;
                case 'C':
                    order.numChildSeats--;
                    reservation.numChildren--;
                    break;
                case 'S':
                    order.numSeniorSeats--;
                    reservation.numSenior--;
                    break;
                default:
                    break;
            }
            //remove from the linked list
            order.seats.remove(new Seat(rowNum, seatLetter, '.'));
            checkEmptyOrder(reservation);
            return true;
        } else {
            //if the seat is not found
            System.out.println("Seat not found");
            return false;
        }

    }
    public static void checkEmptyOrder(Reservation r){
        //if the order is empty, then remove all of them from the linked list
        for (int i = 0; i < r.orders.size(); i++) {
            if (r.orders.get(i).seats.isEmpty()){
                r.orders.remove(i);
                return;
            }
        }
    }
    public static boolean addSeat(order order, Scanner input, Auditorium[] array, Reservation curReservation) {
        int numRows = 0;
        int numColumns = 0;
        int userInput = 0;
        int rowNumber = 0;
        char startingSeatLetter = 'a';
        int adultTickets = -1;
        int childTickets = -1;
        int seniorTickets = -1;
        char userChoice = '1';
        boolean isAvailable = false;
        Auditorium a = array[order.auditoriumNumber - 1];
        numColumns = a.getNumColumns();
        numRows = a.getNumRows();
        a.displayAuditorium();
        // Get information about the reservation
        rowNumber = 0;
        //collect the row that they want to reserve seats from
        while (rowNumber < 1 || rowNumber > numRows) {
            try {
                System.out.println("Row Number");
                rowNumber = input.nextInt();
                if (rowNumber < 1 || rowNumber > numRows) {
                    throw new InputMismatchException("Error: Please enter a valid row.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer.");
                input.nextLine();
            }

        }

        String temp = null;
        startingSeatLetter = 'a';
        //collect the letter of the seat
        while (startingSeatLetter < 'A' || startingSeatLetter > 'Z') {
            try {
                System.out.println("Seat Letter");
                startingSeatLetter = input.next().charAt(0);
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid character");
                input.nextLine();
            }

        }
        //collect number of adult tickets
        adultTickets = -1;
        while (adultTickets < 0) {
            try {
                System.out.println("Adult Tickets");
                adultTickets = input.nextInt();
                if (adultTickets < 0) {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer.");
                input.nextLine();
            }

        }
        //collect number of child tickets
        childTickets = -1;
        while (childTickets < 0) {
            try {
                System.out.println("Child Tickets");
                childTickets = input.nextInt();
                if (childTickets < 0) {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer.");
                input.nextLine();
            }
        }
        seniorTickets = -1;
        while (seniorTickets < 0) {
            try {
                System.out.println("Senior Tickets");
                seniorTickets = input.nextInt();
                if (seniorTickets < 0) {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer.");
                input.nextLine();
            }
        }

        // Check if the seats are available
        isAvailable = a.checkAvailability(rowNumber, startingSeatLetter, (adultTickets + childTickets + seniorTickets));

        if (isAvailable) {
            // Reserve if seats are not filled
            reserveSeats(rowNumber, startingSeatLetter, adultTickets, seniorTickets, childTickets, a, curReservation, order.auditoriumNumber, order);
            System.out.println("Reserved");
            return true;
        } else {
            System.out.println("Could not reserve seats");
            return false;
        }
    }

    public static double calculate_order_total(order order) {
        //count up all the people and multiply them by the amount of money they bring in
        double total = 0;
        for (int i = 0; i < order.seats.size(); i++) {
            switch (order.seats.get(i).getTicketType()) {
                case 'A':
                    total += 10;
                    break;
                case 'C':
                    total += 5;
                    break;
                case 'S':
                    total += 7.5;
                    break;
            }
        }
        return total;
    }

    public static double calculate_reservation_total(Reservation reservation) {
        //this is on a reservation level
        double total = 0;
        for (int i = 0; i < reservation.orders.size(); i++) {
            total += calculate_order_total(reservation.orders.get(i));
        }
        return total;
    }

    public static class Reservation {
        // Linked list to store orders
        public LinkedList<order> orders;
        // User-specific information
        private String username;
        private String password;
        // Count of different ticket types
        public int numAdults;
        public int numSenior;
        public int numChildren;

        // Default constructor
        public Reservation() {
        }

        // Parameterized constructor
        public Reservation(String u, String p) {
            this.username = u;
            this.password = p;
            orders = new LinkedList<order>();
        }

        public int getNumAdults() {
            return numAdults;
        }

        public void setNumAdults(int numAdults) {
            this.numAdults = numAdults;
        }

        public int getNumSenior() {
            return numSenior;
        }

        public void setNumSenior(int numSenior) {
            this.numSenior = numSenior;
        }

        public int getNumChildren() {
            return numChildren;
        }

        public void setNumChildren(int numChildren) {
            this.numChildren = numChildren;
        }

    }

    public static class order {
        public LinkedList<Seat> seats;
        public int numAdultSeats;
        public int numSeniorSeats;
        public int numChildSeats;
        private int auditoriumNumber;

        public order(int numAdultSeats, int numSeniorSeats, int numChildSeats, int auditoriumNumber) {
            //default values
            this.numAdultSeats = numAdultSeats;
            this.numSeniorSeats = numSeniorSeats;
            this.numChildSeats = numChildSeats;
            this.auditoriumNumber = auditoriumNumber;
            this.seats = new LinkedList<Seat>();
        }

        public int getAuditoriumNumber() {
            return auditoriumNumber;
        }

        public void setAuditoriumNumber(int auditoriumNumber) {
            this.auditoriumNumber = auditoriumNumber;
        }

        public LinkedList<Seat> getSeats() {
            return seats;
        }

        public void setSeats(LinkedList<Seat> seats) {
            this.seats = seats;
        }

        @Override
        public String toString() {
            //toString for a reservation will go through all the
            String hold = "Auditorium " + auditoriumNumber + ", ";
            int i = 0;
            while (i < seats.size()) {
                hold = hold.concat(seats.get(i).toString());
                i++;
                try {
                    seats.get(i);
                    hold = hold.concat(",");
                } catch (IndexOutOfBoundsException e) {
                    hold = hold.concat("\n");
                }
            }
            hold = hold + "" + numAdultSeats + " adult, " + numChildSeats + " child, " + numSeniorSeats + " senior";
            return hold;

        }

        public void setNumAdultSeats(int numAdultSeats) {
            this.numAdultSeats = numAdultSeats;
        }

        public void setNumSeniorSeats(int numSeniorSeats) {
            this.numSeniorSeats = numSeniorSeats;
        }

        public void setNumChildSeats(int numChildSeats) {
            this.numChildSeats = numChildSeats;
        }

        public int getNumAdultSeats() {
            return numAdultSeats;
        }

        public int getNumSeniorSeats() {
            return numSeniorSeats;
        }

        public int getNumChildSeats() {
            return numChildSeats;
        }
    }
}