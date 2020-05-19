package main;

public final class Asset {
    private int id;
    private String type;
    private int profit;
    private int penalty;
    private int bonus;

    // In functie de tipul cartii de joc
    // se creaza un bun (legal sau ilegal)
    public Asset(final int id, final int profit) {
        this.id = id;
        this.type = "Legal";
        this.profit = profit;
        this.penalty = 2;
        this.bonus = 0;
    }

    public Asset(final int id, final int profit, final int nr, final String plus) {
        // nr = numarul de carti de tipul plus
        // pe care jucatorul le aduce in plus, prin intermediul bunului ilegal
        final int four = 4;
        final int three = 3;

        this.id = id;
        this.type = "Illegal";
        this.profit = profit;
        this.penalty = four;

        if (plus.equals("Cheese")) {
            this.bonus = nr * three;
        }

        if (plus.equals("Chicken") || plus.equals("Bread")) {
            this.bonus = nr * four;
        }
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setProfit(final int profit) {
        this.profit = profit;
    }

    public int getProfit() {
        return profit;
    }

    public void setPenalty(final int penalty) {
        this.penalty = penalty;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setBonus(final int bonus) {
        this.bonus = bonus;
    }

    public int getBonus() {
        return bonus;
    }
}
