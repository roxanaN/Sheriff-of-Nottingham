package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BribePlayer extends BasicPlayer {
    public BribePlayer() {
        super.setType("BRIBED");
    }

    public final void merchant() {
        final int five = 5;
        final int ten = 10;
        List<Asset> hand = new ArrayList<>(getAssetInHand());
        List<Asset> bag = new ArrayList<>(getAssetInBag());
        boolean illegal = false, legal = false;

        // Se verifica ce fel de bunuri detine jucatorul in mana
        for (Asset asset : hand) {
            if (asset.getType().equals("Illegal")) {
                illegal = true;
                continue;
            }

            if (asset.getType().equals("Legal")) {
                legal = true;
            }
        }

        // Daca nu are bani sau nu are bunuri ilegale
        // comerciantul va fi onest
        if (getCoins() == 0 || !illegal) {
            super.merchant();
        } else {
            // Daca are doar bunuri ilegale
            // Se adauga primele 5 bunuri care maximizeaza profitul
            if (!legal) {
                Collections.sort(hand, Collections.reverseOrder());

                for (int i = 0; i < five; ++i) {
                    bag.add(hand.remove(i));
                }

                setBribe(ten);
            } else {
                // Daca are bunuri legale
                // Se adauga toate bunurile ilegale existente
                int nrIllegal = 0, cnt = 0;
                while (cnt < hand.size()) {
                    if (hand.get(cnt).getType().equals("Illegal")) {
                        bag.add(hand.remove(cnt));
                        ++nrIllegal;
                    } else {
                        ++cnt;
                    }
                }

                // In functie de numarul bunurilor ilegale adaugate
                // se stabileste mita
                if (nrIllegal > 2) {
                    setBribe(ten);
                } else {
                    setBribe(five);
                }
            }

            // Se creaza sacul, se actualizeaza cartile ramase
            // Bunurile din sac e declara ca fiind legale
            setAssetInBag(bag);
            setAssetInHand(hand);
            setDeclaredType(0);
        }
    }

    public final void sheriff(final BasicPlayer p) {
        super.sheriff(p);
    }
}
