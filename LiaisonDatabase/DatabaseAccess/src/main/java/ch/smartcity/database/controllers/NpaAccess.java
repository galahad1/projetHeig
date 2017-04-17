package controllers;

import models.Npa;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NpaAccess {

    private final static Logger LOGGER = Logger
            .getLogger(NpaAccess.class.getName());

    private final static String MESSAGE_MORE_THAN_1_RESULT =
            "More than 1 result";
    private final static String PARAMETER_NUMERO_NPA = "numeroNpa";
    private final static String NAMED_QUERY_FIND_BY_NUMERO_NPA =
            "Npa.findByNumeroNpa";
    private final static String NAMED_QUERY_FIND_ALL = "Npa.findAll";

    private static SessionFactory _sessionFactory;

    public NpaAccess(SessionFactory sessionFactory) {
        _sessionFactory = sessionFactory;
    }

    public Npa getNpa(int idNpa) {
        Npa npa = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = _sessionFactory.openSession();
            transaction = session.beginTransaction();

            npa = session.get(Npa.class, idNpa);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return npa;
    }

    public List<Npa> getNpa(String numeroNpa) {
        List<Npa> npaList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = _sessionFactory.openSession();
            transaction = session.beginTransaction();

            npaList = session
                    .createNamedQuery(NAMED_QUERY_FIND_BY_NUMERO_NPA, Npa.class)
                    .setParameter(PARAMETER_NUMERO_NPA, numeroNpa).list();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return npaList;
    }

    public List<Npa> getNpa() {
        List<Npa> npaList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = _sessionFactory.openSession();
            transaction = session.beginTransaction();

            npaList = session
                    .createNamedQuery(NAMED_QUERY_FIND_ALL, Npa.class)
                    .list();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return npaList;
    }

    public void saveNpa(String numeroNpa, Calendar derniereMiseAJour) {
        saveNpa(new Npa(0, numeroNpa, derniereMiseAJour));
    }

    public void saveNpa(Npa npa) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = _sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.save(npa);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }
    }

    public void saveNpa(List<Npa> npaList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = _sessionFactory.openSession();
            transaction = session.beginTransaction();

            for (Npa npa : npaList) {
                session.save(npa);
            }

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }
    }

    public void updateNpa(int idNpa, String numeroNpa) {
        Npa npa = getNpa(idNpa);
        npa.setNumeroNpa(numeroNpa);
        updateNpa(npa);
    }

    public void updateNpa(String oldNumeroNpa, String newNumeroNpa) {
        List<Npa> npaList = getNpa(oldNumeroNpa);

        if (npaList.size() == 1) {
            npaList.get(0).setNumeroNpa(newNumeroNpa);
            updateNpa(npaList.get(0));
        } else {
            LOGGER.log(Level.WARNING, MESSAGE_MORE_THAN_1_RESULT);
        }
    }

    public void updateNpa(Npa npa) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = _sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.update(npa);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }
    }

    public void updateNpa(List<Npa> npaList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = _sessionFactory.openSession();
            transaction = session.beginTransaction();

            for (Npa npa : npaList) {
                session.update(npa);
            }

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }
    }

    public void deleteNpa(int idNpa) {
        deleteNpa(getNpa(idNpa));
    }

    public void deleteNpa(String numeroNpa) {
        deleteNpa(getNpa(numeroNpa));
    }

    public void deleteNpa(Npa npa) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = _sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.delete(npa);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }
    }

    public void deleteNpa(List<Npa> npaList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = _sessionFactory.openSession();
            transaction = session.beginTransaction();

            for (Npa npa : npaList) {
                session.delete(npa);
            }

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }
    }

    private void rollback(Exception ex, Transaction transaction) {
        try {
            transaction.rollback();
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (Exception _ex) {
            LOGGER.log(Level.SEVERE, _ex.getMessage(), _ex);
        }
    }

    private void close(Session session) {
        try {
            session.close();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
