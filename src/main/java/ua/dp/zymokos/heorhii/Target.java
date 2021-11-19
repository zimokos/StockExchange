package ua.dp.zymokos.heorhii;

public class Target {
    private String targetName;
    private Integer targetAmount;
    private Double targetPrice;

    public Target(String targetName, Integer targetAmount, Double targetPrice) {
        this.targetName = targetName;
        this.targetAmount = targetAmount;
        this.targetPrice = targetPrice;
    }

    public Target() {
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Integer getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Integer targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Double targetPrice) {
        this.targetPrice = targetPrice;
    }
}
