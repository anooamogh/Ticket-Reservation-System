//Amogh Anoo ASA210011
public class Node {
    private Node Next;
    private Node Down;
    private Seat Payload;

    public Node getNext() {
        return Next;
    }

    public Node getDown() {
        return Down;
    }

    public Seat getPayload() {
        return Payload;
    }

    public void setDown(Node down) {
        Down = down;
    }

    public void setNext(Node next) {
        Next = next;
    }

    public void setPayload(Seat payload) {
        Payload = payload;
    }
}
