package model;

import java.util.List;
import java.util.ArrayList;

public class Login {
    private final List<Patron> patrons;

    public Login() {
        this.patrons = new ArrayList<Patron>();
    }

    public void addPatron(String username, String password) {
        for (Patron patron : this.patrons) {
            if (patron.getUsername().equals(username))
                System.out.println("This Username already exsists.");
            else
                patrons.add(new Patron(username, password));
        }
     

    }

    public Patron getPatron(String username) {
        for (Patron patron : patrons) {
            if (patron.getUsername().equals(username)) {
                return patron;
            }
        }
        return null;
    }
    /*
    public ArrayList getPatrons() {
        return patrons;
    }
    */

    public void removePatron(Patron patron) {
        this.patrons.remove(patron);
    }


}
