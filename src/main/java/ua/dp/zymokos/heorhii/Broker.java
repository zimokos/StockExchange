package ua.dp.zymokos.heorhii;

import java.util.ArrayList;

public class Broker {
    private String name;
    private ArrayList<Target> target;
    private ArrayList<Shares> purchasedShares;

    public Broker(String name, ArrayList<Target> target) {
        this.name = name;
        this.target = target;
        this.purchasedShares = new ArrayList<>();
    }

    public Broker() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Target> getTarget() {
        return target;
    }

    public void setTarget(ArrayList<Target> target) {
        this.target = target;
    }

    public ArrayList<Shares> getPurchasedShares() {
        return purchasedShares;
    }

    public void setPurchasedShares(ArrayList<Shares> purchasedShares) {
        this.purchasedShares = purchasedShares;
    }
}
