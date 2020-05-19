package main;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

public class BasicPlayer {
    private String type;
    private List<Asset> assetInHand;
    private List<Asset> assetOnMerchantStand;
    private int coins;
    private List<Asset> assetInBag;
    private int bribe;
    private int declaredType;
    private List<Asset> confiscated;

    public BasicPlayer() {
        final int six = 6;
        final int fifty = 50;
        type = "BASIC";
        assetInHand = new ArrayList<>(six);
        assetInBag = new ArrayList<>();
        assetOnMerchantStand = new ArrayList<>();
        coins = fifty;
        bribe = 0;
        declaredType = 0;
        confiscated = new ArrayList<>();
    }

    /**/
    public void merchant() {
        final int four = 4;

        // Se contorizeaza bunurile din mana
        Map<Integer, Integer> countAssets = new LinkedHashMap<Integer, Integer>();
        for (Asset asset : assetInHand) {
            int id = asset.getId();
            countAssets.put(id, countAssets.getOrDefault(id, 0) + 1);
        }

        // Se verifica daca exista bunuri legale
        boolean ok = false;

        for (Asset asset : assetInHand) {
            if (asset.getType().equals("Legal")) {
                ok = true;
                break;
            }
        }

        // Crearea sacului
        int freq = 0;
        Asset temp = null;
        // Daca nu exista bunuri legale
        if (!ok) {
            // Se alege bunul ilegal care are cel mai mare profit
            for (Asset asset : assetInHand) {
                if (asset.getProfit() > freq) {
                    freq = asset.getProfit();
                    temp = asset;
                }
            }

            // Se declara ca fiind bun legal, de tip apple, si se adauga in sac
            declaredType = 0;
            assetInBag.add((temp));
        } else {
            // Se retine frecventa bunurilor care apar de cele mai multe ori
            for (Integer key : countAssets.keySet()) {
                if (key < four) {
                    if (countAssets.get(key) > freq) {
                        freq = countAssets.get(key);
                    }
                }
            }

            // Se retin cartile care apar de cele mai multe ori
            List<Integer> idsFreq = new ArrayList<>();
            for (Integer key : countAssets.keySet()) {
                if (countAssets.get(key) == freq && key < four) {
                    idsFreq.add(key);
                }
            }

            // Se creaza o lista cu bunurile care apar de cele mai multe ori
            List<Asset> goodsFreq = new ArrayList<>();
            for (int i : idsFreq) {
                for (Asset a : assetInHand) {
                    if (a.getId() == i) {
                        goodsFreq.add(a);
                        break;
                    }
                }
            }

            // Se parcurge lista cu bunuri frecvente
            // Se cauta bunul care va aduce profit maxim
            int id = -1;
            int maxProfit = 0;
            for (Asset s : goodsFreq) {
                if (s.getProfit() > maxProfit) {
                    maxProfit = s.getProfit();
                    id = s.getId();
                }
            }

            // In sac, se adauga toate cartile de acelasi tip, care maximizeaza profitul
            // Cartile se elimina din mana jucatorului
            int cnt = 0;
            while (cnt < assetInHand.size()) {
                if (assetInHand.get(cnt).getId() == id) {
                    temp = assetInHand.remove(cnt);
                    cnt = 0;
                } else {
                    ++cnt;
                }
            }

            for (int i = 0; i < freq; ++i) {
                assetInBag.add(temp);
            }

            declaredType = id;
            bribe = 0;
        }
    }

    /**/
    public void sheriff(final BasicPlayer p) {
        // Se verifica bunurile din sacul jucatorului p
        List<Asset> tmp = new ArrayList<>(p.getAssetInBag());
        for (Asset a : tmp) {
            // Daca jucatorul a mintit in privinta continutului sacului
            if (isLiar(tmp)) {
                if (a.getType().equals("Illegal")) {
                    // comerciantul este penalizat pentru fiecare bun ilegal
                    this.coins += a.getPenalty();
                    int c = p.getCoins() - a.getPenalty();
                    p.setCoins(c);

                    // Bunul ilegal se confisca
                    confiscated.add(a);
                }

                // Se adauga, totusi, bunurile de tip legal
                if (a.getType().equals("Legal")) {
                    p.assetOnMerchantStand.add(a);
                }
            } else {
                // Daca jucatorul este onest
                // seriful se scuza cu bani pentru ca l-a verificat pe nedreot
                this.coins -= a.getPenalty();
                int c = p.getCoins() + a.getPenalty();
                p.setCoins(c);

                p.assetOnMerchantStand.add(a);
            }

//            p.assetInBag.remove(a);
        }
    }

    // Functie care verifica daca jucatorul este onest
    public final boolean isLiar(final List<Asset> b) {
        for (Asset a : b) {
            if (a.getType().equals("Illegal")) {
                return true;
            }
        }

        return false;
    }

    public final void setType(final String type) {
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    public final void setCoins(final int coins) {
        this.coins = coins;
    }

    public final int getCoins() {
        return coins;
    }

    public final void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    public final int getBribe() {
        return bribe;
    }

    public final void setDeclaredType(final int declaredType) {
        this.declaredType = declaredType;
    }

    public final int getDeclaredType() {
        return declaredType;
    }

    public final void clearBag() {
        this.assetInBag.clear();
    }
    public final void setAssetInBag(final List<Asset> assetInBag) {
        this.assetInBag = assetInBag;
    }

    public final List<Asset> getAssetInBag() {
        return assetInBag;
    }

    public final void setAssetInHand(final List<Asset> assetInHand) {
        this.assetInHand = assetInHand;
    }

    public final void addAssetInHand(final Asset a) {
        this.assetInHand.add(a);
    }

    public final List<Asset> getAssetInHand() {
        return assetInHand;
    }

    public final void setAssetOnMerchantStand(final List<Asset> assetOnMerchantStand) {
        this.assetOnMerchantStand = assetOnMerchantStand;
    }

    public final List<Asset> getAssetOnMerchantStand() {
        return assetOnMerchantStand;
    }

    public final List<Asset> getConfiscated() {
        return confiscated;
    }

    public final void setConfiscated(final List<Asset> confiscated) {
        this.confiscated = confiscated;
    }
}
