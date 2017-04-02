import controlers.ORMAccess;
import models.Npa;

import java.util.Calendar;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            ORMAccess ormAccess = new ORMAccess();

            Npa npa = ormAccess.getNpa(1);
            System.out.println(npa);

            List<Npa> npaList = ormAccess.getNpaList("1004");
            System.out.println(npaList);

            ormAccess.saveNpa(new Npa(0, "1111", Calendar.getInstance()));

            npaList = ormAccess.getNpaList();
            System.out.println(npaList);

            npaList.get(0).setNumeroNpa("6666");
            ormAccess.updateNpaList(npaList);

            npaList = ormAccess.getNpaList();
            System.out.println(npaList);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ORMAccess.terminate();
        }
    }
}
