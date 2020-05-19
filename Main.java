package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Set;

public final class Main {

    private Main() { }

    private static final class GameInputLoader {
        private final String mInputPath;

        private GameInputLoader(final String path) {
            mInputPath = path;
        }

        public GameInput load() {
            List<Integer> assetsIds = new ArrayList<>();
            List<String> playerOrder = new ArrayList<>();

            try {
                BufferedReader inStream = new BufferedReader(new FileReader(mInputPath));
                String assetIdsLine = inStream.readLine().replaceAll("[\\[\\] ']", "");
                String playerOrderLine = inStream.readLine().replaceAll("[\\[\\] ']", "");

                for (String strAssetId : assetIdsLine.split(",")) {
                    assetsIds.add(Integer.parseInt(strAssetId));
                }

                for (String strPlayer : playerOrderLine.split(",")) {
                    playerOrder.add(strPlayer);
                }
                inStream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GameInput(assetsIds, playerOrder);
        }
    }

    // Functie care sorteaza elementele unui hash, in functie de valoare
    private static Map<String, Integer> sortByValue(final Map<String, Integer> unsortMap) {

        // Se converteste Map-ul in Lista de Map
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Se sorteaza lista folosind Collections.sort()
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(final Map.Entry<String, Integer> o1,
                              final Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // Se parcurge lista, formandu-se noul HashMap (ordonat)
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    // Functie pentru afisarea clasamentului
    public static void printLeaderboard(final Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey()
                    + ": " + entry.getValue());
        }
    }

    // Functie care determina clasamentul
    public static Map<String, Integer> getLeaderboard(final List<BasicPlayer> players,
                                                      final List<String> typePlayer) {
        final int three = 3;
        final int four = 4;
        final int five = 5;
        final int ten = 10;
        final int eleven = 11;
        final int twelve = 12;
        final int fifteen = 15;
        final int twenty = 20;

        // Numarul de jucatori
        int n = players.size();

        // Lista bunurilor pe care le detine fiecare jucator la finalul joculul
        List<Map<Integer, Integer>> assets = new ArrayList<>(n);
        // Lista care contine jucatorii si scorul obtinut de fiecare dintre acestia
        Map<String, Integer> leaderboard = new LinkedHashMap<>();
        for (BasicPlayer p : players) {
            int totalCoins = 0;

            // Se stabilesc bunurile obtinute de fiecare jucator
            Map<Integer, Integer> good = new LinkedHashMap<>();
            for (Asset a : p.getAssetOnMerchantStand()) {
                String type = a.getType();
                int id = a.getId();

                // Daca bunul este legal, acesta se adauga pe stand
                if (type.equals("Legal")) {
                    good.put(id, good.getOrDefault(id, 0) + 1);
                } else {
                    // Daca bunul este ilegal
                    // se stabileste cate bunuri legale aduce acesta pe stand
                    if (id == ten) {
                        good.put(1, good.getOrDefault(1, 0) + three);
                    }

                    if (id == eleven) {
                        good.put(three, good.getOrDefault(three, 0) + 2);
                    }

                    if (id == twelve) {
                        good.put(2, good.getOrDefault(2, 0) + 2);
                    }
                }

                // Se calculeaza punctajul adus de fiecare bun
                totalCoins += a.getProfit() + a.getBonus();
            }

            // Se aduna punctajul la suma de bani ramasa
            totalCoins += p.getCoins();

            // Se adauga jucatorul si punctajul sau in clasament
            leaderboard.put(p.getType(), leaderboard.getOrDefault(p.getType(), 0) + totalCoins);

            // Se actualizeaza lista de bunuri a jucatorului
            assets.add(good);
        }

        // Se calculeaza kingBonusul si queenBonusul
        // g = lista care asociaza fiecarui bun numarul de bunuri de tipul k
        // pe care le detine jucatorul l
        List<List<Integer>> g = new ArrayList<List<Integer>>(four);
        List<Integer> p = Arrays.asList(new Integer[n]);

        for (int k = 0; k < four; ++k) {
            for (int l = 0; l < n; ++l) {
                p.set(l, 0);
            }
            g.add(p);

            // Pentru fiecare jucator
            // se contorizeaza cate bunuri de tipul k contine
            for (int l = 0; l < n; ++l) {
                Set<Integer> set = assets.get(l).keySet();
                for (Integer key : set) {
                    if (key == k) {
                        Integer value = assets.get(l).get(k);
                        p.set(l, value);
                        g.set(k, p);
                    }
                }
            }

            // Se stabilesc relege si regina
            int king = 0, queen = 0;
            for (int l = 0; l < n; ++l) {
                int val = g.get(k).get(l);
                if (val > king) {
                    queen = king;
                    king = val;
                } else {
                    if (val > queen) {
                        queen = val;
                    }
                }
            }

            // Se distribuie premiile pentru rege si regina
            for (int l = 0; l < n; ++l) {
                String tmp = typePlayer.get(l).toUpperCase();
                int val = g.get(k).get(l);
                if (val == king) {
                    if (k == 0) {
                        leaderboard.put(tmp, leaderboard.getOrDefault(tmp, 0) + twenty);
                    }

                    if (k == 1 || k == 2) {
                        leaderboard.put(tmp, leaderboard.getOrDefault(tmp, 0) + fifteen);
                    }

                    if (k == three) {
                        leaderboard.put(tmp, leaderboard.getOrDefault(tmp, 0) + ten);
                    }
                }

                if ((king != queen && val == queen)
                        || (king == queen && val != king && val != queen)) {
                    if (k == 0 || k == 1 || k == 2) {
                        leaderboard.put(tmp, leaderboard.getOrDefault(tmp, 0) + ten);
                    }

                    if (k == three) {
                        leaderboard.put(tmp, leaderboard.getOrDefault(tmp, 0) + five);
                    }
                }

            }
        }

        // Clasamentul este gata pentru a fi afisat :)
        return leaderboard;
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0]);
        GameInput gameInput = gameInputLoader.load();

        List<Integer> ids = gameInput.getAssetIds();
        List<String> typePlayer  = gameInput.getPlayerNames();
        final int three = 3;
        final int four = 4;
        final int six = 6;
        final int seven = 7;
        final int eight = 8;
        final int nine = 9;
        final int silkId = 10;
        final int pepperId = 11;
        final int barrelId = 12;

        // Se parcurge lista de id-uri ale cartilor de joc
        // Se creaza o lista de bunuri
        List<Asset> goods = new ArrayList<>();
        for (int i : ids) {
            if (i == 0) {
                goods.add(new Asset(0, 2));
            }

            if (i == 1) {
                goods.add(new Asset(1, three));
            }

            if (i == 2) {
                goods.add(new Asset(2, four));
            }

            if (i == three) {
                goods.add(new Asset(three,  four));
            }

            if (i == silkId) {
                goods.add(new Asset(silkId, nine, three, "Cheese"));
            }

            if (i == pepperId) {
                goods.add(new Asset(pepperId, eight, 2, "Chicken"));
            }

            if (i == barrelId) {
                goods.add(new Asset(barrelId, seven, 2, "Bread"));
            }
        }

        // Se parcurge lista de strategii
        // Se creaza lista de jucatori
        int n = typePlayer.size();
        List<BasicPlayer> players = new ArrayList<>(n);
        for (int k = 0; k < n; ++k) {
            if (typePlayer.get(k).equals("basic")) {
                players.add(new BasicPlayer());
            }

            if (typePlayer.get(k).equals("greedy")) {
                players.add(new GreedyPlayer());
            }

            if (typePlayer.get(k).equals("bribed")) {
                players.add(new BribePlayer());
            }
        }

        // Se calculeaza numarul de runde, in functie de numarul de jucatori
        // Contorul i parcurge lista de bunuri
        int numberOfRounds = 2 * n;
        int round = 0;
        int i = 0;
        while (round < numberOfRounds) {
            // Completarea bunurilor din mana
            for (BasicPlayer p : players) {
                while (p.getAssetInHand().size() < six) {
                    p.addAssetInHand(goods.get(i));
                    ++i;
                }
            }

            // Distribuirea rolurilor
            int boss = round % n;
            for (int p = 0; p < n; ++p) {
                if (p != boss) {
                    // Crearea sacului si declarararea bunurilor
                    players.get(p).merchant();
                    // Inspectia sacului
                    players.get(boss).sheriff(players.get(p));
                    // Golirea sacului la finalul rund
                    players.get(p).clearBag();
                }
            }

            ++round;
        }

        // Determinarea si afisarea clasamentului final
        Map<String, Integer> leaderboard = getLeaderboard(players, typePlayer);
        printLeaderboard(sortByValue(leaderboard));
    }
}
