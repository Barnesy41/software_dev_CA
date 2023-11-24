public class Card implements Runnable {
    private final int value;

    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            break;
        }
    }

    public Card (int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
