package database.controllers;

//@SuppressWarnings("all")
public class PrioriteAccess {

    private final DatabaseManager databaseManager;

    public PrioriteAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
//
//    public List<Priorite> get(String nomPriorite, Integer niveau) {
//        List<Priorite> prioriteList = null;
//
//        try {
//            Session session = databaseManager.openSession();
//
//            Criteria criteria = session.createCriteria(Priorite.class);
//
//            if (nomPriorite != null) {
//                criteria.add(Restrictions.eq("nomPriorite", nomPriorite.toLowerCase()));
//            }
//
//            if (niveau != null) {
//                criteria.add(Restrictions.eq("niveau", niveau));
//            }
//
//            prioriteList = criteria.list();
//
//            databaseManager.commit();
//        } catch (Exception ex) {
//            databaseManager.rollback(ex);
//        } finally {
//            databaseManager.close();
//        }
//
//        return prioriteList;
//    }
//
//    public void save(String nomPriorite, int numero) {
//        DatabaseAccess.save(new Priorite(nomPriorite, numero));
//    }
//
//    public void update(int idPriorite, String nomPriorite) {
//        Priorite priorite = DatabaseAccess.get(Priorite.class, idPriorite);
//        priorite.setNomPriorite(nomPriorite);
//        DatabaseAccess.update(priorite);
//    }
//
//    public void update(int idPriorite, int numero) {
//        Priorite priorite = DatabaseAccess.get(Priorite.class, idPriorite);
//        priorite.setNiveau(numero);
//        DatabaseAccess.update(priorite);
//    }
//
//    public void update(int idPriorite, String nomPriorite, int numero) {
//        Priorite priorite = DatabaseAccess.get(Priorite.class, idPriorite);
//        priorite.setNomPriorite(nomPriorite);
//        priorite.setNiveau(numero);
//        DatabaseAccess.update(priorite);
//    }
//
//    public void update(String oldNomPriorite, String newNomPriorite) {
//        List<Priorite> prioriteList = get(oldNomPriorite);
//
//        for (Priorite priorite : prioriteList) {
//            priorite.setNomPriorite(newNomPriorite);
//        }
//
//        DatabaseAccess.update(prioriteList);
//    }
//
//    public void delete(String nomPriorite) {
//        DatabaseAccess.delete(get(nomPriorite));
//    }
}
