package main;

import java.util.ArrayList;
import java.util.List;

public class GreedyPlayer extends BasicPlayer {
    private int round = 1;

    public GreedyPlayer() {
        super.setType("GREEDY");
    }

    /**/
    public final void merchant() {
        super.merchant();

        // In rundele pare, jucatorul greedy introduce in sac un bun ilegal
        if (round % 2 == 0) {
            doIllegal();
        }

        ++round;
    }

    private void doIllegal() {
        final int five = 5;
        List<Asset> bag = new ArrayList<>(getAssetInBag());
        List<Asset> hand = new ArrayList<>(getAssetInHand());

        // Daca se mai pot adauga bunuri in sac
        // Se alege bunul care maximizeaza profitul
        if (bag.size() < five) {
            int max = 0;
            Asset temp = null;

            for (Asset a : hand) {
                if (a.getType().equals("Illegal") && a.getProfit() > max) {
                    max = a.getProfit();
                    temp = a;
                }
            }

            if (temp != null) {
                bag.add(temp);
                hand.remove(temp);
            }

            // Se actualizeaza sacul si cartile din mana
            setAssetInBag(bag);
            setAssetInHand(hand);
        }
    }

    public final void sheriff(final BasicPlayer p) {
        // Daca jucatorul nu ofera mita
        // Seriful aplica strategia de baza
        if (p.getBribe() == 0) {
            super.sheriff(p);
        } else {
            // Daca se ofera mita, se actualizeaza banii fiecarui jucator
            this.setCoins(this.getCoins() + p.getBribe());
            p.setCoins(p.getCoins() - p.getBribe());

            // Toate bunurile se adauga pe stand
            List<Asset> stand = new ArrayList<>(p.getAssetOnMerchantStand());
            stand.addAll(p.getAssetInBag());
            p.setAssetOnMerchantStand(stand);
        }
    }
}
