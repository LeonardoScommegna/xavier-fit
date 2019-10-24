package it.fed03;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class NumberOfRetrievedUsers implements Serializable {
    private static final long serialVersionUID = 42L;

    private int number = 0;

    public int getNumber() {
        return number;
    }

    public void addRetrievedCount(int count) {
        number += count;
    }
}
