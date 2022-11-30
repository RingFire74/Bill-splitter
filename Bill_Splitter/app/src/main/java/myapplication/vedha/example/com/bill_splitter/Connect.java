package myapplication.vedha.example.com.bill_splitter;

import java.io.Serializable;
import java.util.ArrayList;

public class Connect implements Serializable {
    ArrayList<String> list;
    public void putList(ArrayList<String> list){
        this.list=list;
    }
    public ArrayList<String> getList(){
        return  list;
    }
}
