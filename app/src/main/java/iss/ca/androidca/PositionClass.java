package iss.ca.androidca;

public class PositionClass {
    int id;
    int select;

    public PositionClass(int id){
        this.id=id;
        select = 0;
    }

    public int getId() {
        return id;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }
}
