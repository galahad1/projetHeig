package controllers;

import models.Npa;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Calendar;
import java.util.List;

/**
 * @author Lassalle Loan
 * @since 02/04/2017
 */
public class DatabaseAccess {

    private static SessionFactory sessionFactory;

    public DatabaseAccess() {
        sessionFactory = new Configuration().configure("ressources/hibernate/hibernate.cfg.xml").buildSessionFactory();
    }

    public static void terminate() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public Npa getNpa(int idNpa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Npa npa = null;

        try {

            tx = session.beginTransaction();

            npa = session.get(Npa.class, idNpa);

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }

        return npa;
    }

    public List<Npa> getNpa(String numeroNpa) throws Exception {
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        List<Npa> npaList = null;

        try {
            tx = session.beginTransaction();

            npaList = session.createNamedQuery("Npa.findByNumeroNpa", Npa.class)
                    .setParameter("numeroNpa", numeroNpa).list();

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }

        return npaList;
    }

    public List<Npa> getNpa() throws Exception {
        Session session = sessionFactory.openSession();

        Transaction tx = null;
        List<Npa> npaList = null;

        try {

            tx = session.beginTransaction();

            npaList = session.createNamedQuery("Npa.findAll", Npa.class).list();

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }

        return npaList;
    }

    public void saveNpa(String numeroNpa, Calendar derniereMiseAJour) throws Exception {
        saveNpa(new Npa(0, numeroNpa, derniereMiseAJour));
    }

    public void saveNpa(Npa npa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.save(npa);

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    public void saveNpa(List<Npa> npaList) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            for (Npa npa : npaList) {
                session.save(npa);
            }

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    public void updateNpa(int idNpa, String numeroNpa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Npa npa = null;

        try {
            tx = session.beginTransaction();

            npa = session.get(Npa.class, idNpa);
            npa.setNumeroNpa(numeroNpa);
            session.update(npa);

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    public void updateNpa(String oldNumeroNpa, String newNumeroNpa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Npa> npaList = null;

        try {
            tx = session.beginTransaction();

            npaList = session.createNamedQuery("Npa.findByNumeroNpa", Npa.class)
                    .setParameter("numeroNpa", oldNumeroNpa).list();

            if (npaList.size() == 1) {
                npaList.get(0).setNumeroNpa(newNumeroNpa);
                session.update(npaList.get(0));
            }

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    public void updateNpa(Npa npa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.update(npa);

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    public void updateNpa(List<Npa> npaList) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            for (Npa npa : npaList) {
                session.update(npa);
            }

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    public void deleteNpa(int idNpa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Npa npa = null;

        try {
            tx = session.beginTransaction();

            npa = session.get(Npa.class, idNpa);
            session.delete(npa);

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    public void deleteNpa(String numeroNpa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Npa> npaList = null;

        try {
            tx = session.beginTransaction();

            npaList = session.createNamedQuery("Npa.findByNumeroNpa", Npa.class)
                    .setParameter("numeroNpa", numeroNpa).list();

            for (Npa npa : npaList) {
                session.delete(npa);
            }

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    public void deleteNpa(Npa npa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.delete(npa);

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    public void deleteNpa(List<Npa> npaList) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            for (Npa npa : npaList) {
                session.delete(npa);
            }

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }
    }

    private void catchException(Exception ex, Transaction tx) throws Exception {
        if (tx != null) {
            try {
                tx.rollback();
                System.out.println(ex.toString());
                throw ex;
            } catch (HibernateException hex) {
                System.out.println(hex.toString());
                throw hex;
            }
        }
    }

    private void closeSession(Session session) {
        try {
            session.close();
        } catch (HibernateException hex) {
            System.out.println(hex.toString());
            throw hex;
        }
    }
}
