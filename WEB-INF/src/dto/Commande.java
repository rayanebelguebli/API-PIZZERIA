package dto;

import java.util.ArrayList;
import java.util.Date;

public class Commande {
    private int id;
    private String name;
    private Date date;
    private ArrayList<Pizza> list;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public ArrayList<Pizza> getList() {
        return list;
    }
    public void setList(ArrayList<Pizza> list) {
        this.list = list;
    }

    
}
