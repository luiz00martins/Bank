package BankClient;

import java.util.concurrent.atomic.AtomicInteger;

public class Account {

    private AtomicInteger dollars;
    private AtomicInteger cents;

    public Account(){
        dollars = new AtomicInteger();
        cents = new AtomicInteger();

        dollars.set(0);
        cents.set(0);
    }

    public void deposit(int valueDollars, int valueCents){
        dollars.addAndGet(valueDollars);
        cents.addAndGet(valueCents);
    }

    public boolean withdraw(int valueDollars, int valueCents){

        if (dollars.get() >= valueDollars && cents.get() >= valueCents) {
            dollars.addAndGet(-valueDollars);
            cents.addAndGet(-valueCents);

            return true;
        }
        else {
            return false;
        }
    }

}
