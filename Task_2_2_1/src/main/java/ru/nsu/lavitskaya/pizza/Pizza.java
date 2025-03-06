package ru.nsu.lavitskaya.pizza;

public class Pizza {
    private final int id;
    private String status;

    public Pizza(int id) {
        this.id = id;
        this.status = "init";
    }
    public void changeStatus(String newStatus){
        this.status = newStatus;
    }
    public int getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
