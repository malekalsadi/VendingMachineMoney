package com.progressoft.samples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Money {

    private final double value;
    private int count = 0;

    public static final Money Zero = new Money(0.00);
    public static final Money OnePiaster = new Money(0.01);
    public static final Money FivePiasters = new Money(0.05);
    public static final Money TenPiasters = new Money(0.10);
    public static final Money TwentyFivePiasters = new Money(0.25);
    public static final Money FiftyPiasters = new Money(0.50);
    public static final Money OneDinar =  new Money(1.00);
    public static final Money FiveDinars = new Money(5.00);
    public static final Money TenDinars = new Money(10.00);
    public static final Money TwentyDinars = new Money(20.00);
    public static final Money FiftyDinars = new Money(50.00);

    static {
        Zero.AvailableMoney.add(Zero);
        OnePiaster.AvailableMoney.add(OnePiaster);
        FivePiasters.AvailableMoney.add(FivePiasters);
        TenPiasters.AvailableMoney.add(TenPiasters);
        TwentyFivePiasters.AvailableMoney.add(TwentyFivePiasters);
        FiftyPiasters.AvailableMoney.add(FiftyPiasters);
        OneDinar.AvailableMoney.add(OneDinar);
        FiveDinars.AvailableMoney.add(FiveDinars);
        TenDinars.AvailableMoney.add(TenDinars);
        TwentyDinars.AvailableMoney.add(TwentyDinars);
        FiftyDinars.AvailableMoney.add(FiftyDinars);
    }

    public List<Money> AvailableMoney = new ArrayList<>();

    public Money(double value) {
        this.value = value;
        this.count = 1;
    }

    public Money(double value, int count) {
        this.value = value;
        this.count = count;
    }

    public double amount() {
        return this.value;
    }

    public Money times(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }

        Money money = new Money(this.value * count);
        money.AvailableMoney.add(new Money(this.value, count));
        return money;
    }

    public static Money sum(Money... items) {
        double total = 0;
        for (Money item : items) {
            total += item.amount();
        }
        return new Money(total);
    }

    public Money plus(Money other) {
        Money money = new Money(this.value + other.value);
        money.AvailableMoney = new ArrayList<>(this.AvailableMoney);
        money.AvailableMoney.addAll(other.AvailableMoney);
        return money;
    }

    public Money minus(Money other) {
        double result = this.value - other.value;

        if (result < 0) {
            throw new IllegalArgumentException("Resulting money cannot be negative");
        }

        if(AvailableMoney.size() > 1) {
            AvailableMoney.sort(new Comparator<Money>() {
                @Override
                public int compare(Money m1, Money m2) {
                    return Double.compare(m2.amount(), m1.amount());
                }
            });
        }

        double ans = result;
        for(Money money: AvailableMoney) {
            if (ans >= money.amount()) {
                while(money.count > 0 && ans >= money.amount()) { // Decrement the count of each available money
                    money.count--;
                    ans -= money.amount();
                }
            }
        }

        if(ans != 0) {
            throw new IllegalArgumentException("Available bills or coins are not enough for the change");
        }

        return new Money(result);
    }


    @Override
    public String toString() {
        return String.format("%.2f", this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        if(AvailableMoney.isEmpty())
            return Double.compare(money.value, value) == 0;


        return Double.compare(money.amount(), amount()) == 0;
    }

}

