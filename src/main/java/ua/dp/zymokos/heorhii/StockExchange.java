package ua.dp.zymokos.heorhii;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StockExchange {
    public static void main(String[] args) throws InterruptedException {
        Shares aapl = new Shares("AAPL", 100, 141.00);
        Shares coke = new Shares("COKE", 1000, 387.00);
        Shares ibm = new Shares(" IBM", 200, 137.00);

        ArrayList<Target> aliceTargets = new ArrayList<>();
        Target aliceTarget1 = new Target("AAPL", 10, 100.00);
        Target aliceTarget2 = new Target("COKE", 20, 390.00);
        aliceTargets.add(aliceTarget1);
        aliceTargets.add(aliceTarget2);
        Broker alice = new Broker("Alice", aliceTargets);
        ArrayList<Target> bobTargets = new ArrayList<>();
        Target bobTarget1 = new Target("AAPL", 10, 140.00);
        Target bobTarget2 = new Target(" IBM", 20, 135.00);
        bobTargets.add(bobTarget1);
        bobTargets.add(bobTarget2);
        Broker bob = new Broker("Bob", bobTargets);
        ArrayList<Target> charlieTargets = new ArrayList<>();
        Target charlieTarget1 = new Target("COKE", 300, 370.00);
        charlieTargets.add(charlieTarget1);
        Broker charlie = new Broker("Charlie", charlieTargets);

        System.out.println("Начало биржевой сессии: " + LocalDate.now() + " " + LocalTime.now());
        changeSharesPrice("          <AAPL thread>", aapl);
        changeSharesPrice("          <COKE thread>", coke);
        changeSharesPrice("           <IBM thread>", ibm);
        createBrokerThread("         <ALICE thread>", alice, aapl, coke);
        createBrokerThread("           <BOB thread>", bob, aapl, ibm);
        createBrokerThread("       <CHARLIE thread>", charlie, coke);
        Thread.currentThread().sleep(30000);
        System.out.println(" Конец биржевой сессии: " + LocalDate.now() + " " + LocalTime.now());

        System.out.println();
        System.out.println("Результаты биржевой сессии:");
        printSharesResults(aapl, coke, ibm);
        printBrokerResults(alice, bob, charlie);
        System.exit(0);
    }

    public static void changeSharesPrice(String threadName, Shares shares) {
        Runnable runnable = () -> {
            while (true) {
                Double nextPrice = shares.getPrice() + Math.random() * (shares.getPrice() * 0.03)
                        * (Math.random() > 0.5 ? 1 : -1);
                System.out.printf(threadName + " " + LocalDate.now() + " " + LocalTime.now() + " Цена акции компании "
                        + shares.getName() + " изменилась. Текущая стоимость: %.2f. Доступно " + shares.getAmount()
                        + " акций.", nextPrice);
                System.out.println();
                shares.setPrice(nextPrice);
                try {
                    Thread.currentThread().sleep(5000);
//                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable, threadName);
        thread.start();
    }

    public static void createBrokerThread(String threadName, Broker broker, Shares... shares) {
        Runnable runnable = () -> {
            while (true) {
                for (Shares share : shares) {
                    tryBuyShares(threadName, broker, share);
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable, threadName);
        thread.start();
    }

    public static void tryBuyShares(String threadName, Broker broker, Shares shares) {
        synchronized (shares) {
            broker.getTarget().forEach(target -> {
                if (target.getTargetName().equals(shares.getName()) && target.getTargetPrice() >= shares.getPrice()
                        && target.getTargetAmount() <= shares.getAmount()) {
                    System.out.println(threadName + " " + LocalDate.now() + " " + LocalTime.now()
                            + " Попытка покупки акций " + target.getTargetName() + " для " + broker.getName()
                            + " успешна. Куплено " + target.getTargetAmount() + " акций.");
                    broker.getPurchasedShares().add(new Shares(target.getTargetName(), target.getTargetAmount()
                            , shares.getPrice()));
                    shares.setAmount(shares.getAmount() - target.getTargetAmount());
                } else {
                    System.out.println(threadName + " " + LocalDate.now() + " " + LocalTime.now()
                            + " Попытка покупки акций " + target.getTargetName() + " для " + broker.getName()
                            + " не успешна.");
                }
            });
        }
    }

    public static void printSharesResults(Shares... shares) {
        for (Shares share : shares) {
            System.out.printf("Цена акции компании " + share.getName() + " остановилась на %.2f. Осталось " + share.getAmount() + " акций.", share.getPrice());
            System.out.println();
        }
    }

    public static void printBrokerResults(Broker... brokers) {
        for (Broker broker : brokers) {
            Integer aaplSum = 0;
            Integer cokeSum = 0;
            Integer ibmSum = 0;
            for (int i = 0; i < broker.getPurchasedShares().size(); i++) {
                if (broker.getPurchasedShares().get(i).getName().equals("AAPL")) {
                    aaplSum = aaplSum + broker.getPurchasedShares().get(i).getAmount();
                } else if (broker.getPurchasedShares().get(i).getName().equals("COKE")) {
                    cokeSum = cokeSum + broker.getPurchasedShares().get(i).getAmount();
                } else if (broker.getPurchasedShares().get(i).getName().equals(" IBM")) {
                    ibmSum = ibmSum + broker.getPurchasedShares().get(i).getAmount();
                }
            }
            System.out.println("Брокер " + broker.getName() + " купил " + aaplSum + " акций компании AAPL, " + cokeSum + " акций компании COKE, " + ibmSum + " акций компании IBM.");
        }
    }

    public static String priceDifference(double price1, double price2) {
        double changePrice = price2 - price1;
//        if (changePrice > 0) {
//            return "+" + changePrice;
//        } else {
//            return "" + changePrice;
//        }
        return changePrice > 0 ? "+" + changePrice : "" + changePrice;
    }
}
