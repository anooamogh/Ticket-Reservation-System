//Amogh Anoo ASA210011
public class Seat implements Comparable<Seat>{

    private int row;
    private char Seat;
    private char ticketType;

    public Seat() {
        this(0,'0','0');
    }
    public Seat(int r, char seat, char type){
        row = r;
        Seat = seat;
        ticketType = type;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setSeat(char seat) {
        Seat = seat;
    }

    public void setTicketType(char ticketType) {
        this.ticketType = ticketType;
    }

    public char getSeat() {
        return Seat;
    }

    public int getRow() {
        return row;
    }

    public char getTicketType() {
        return ticketType;
    }
    //equals overridden in order to use the LinkedList remove and add functions. This way I don't have to guess when removing them from the list
    public boolean equals(Object o){
        Seat object = (Seat) o;
        if (this.getRow() == object.getRow() && this.getSeat() == object.getSeat()){
            return true;
        }
        return false;
    }
    //to string function
    public String toString() {
        return "" + row + Seat;
    }
    //compares them by first their row, then seat location because then I can sort the list
    @Override
    public int compareTo(Seat s){
        String one = this.toString();
        String two = s.toString();
        return one.compareTo(two);
    }
}

