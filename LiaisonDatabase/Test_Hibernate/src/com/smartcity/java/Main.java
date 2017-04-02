import controlers.ORMAccess;
import models.Npa;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ORMAccess ormAccess = new ORMAccess();

        try {
            List<Npa> npaList = ormAccess.getNpaList();
            System.out.println(npaList);

            Npa npa = ormAccess.getNpa(1);
            System.out.println(npa);

            npa = ormAccess.getNpa("1011");
            System.out.println(npa);

            npa = ormAccess.getNpa("5000");
            System.out.println(npa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
