package controlers;

import models.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Lassalle Loan
 * @since 02/04/2017
 */
public class ORMAccess {

    private static SessionFactory sessionFactory;

    public ORMAccess() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
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

    @SuppressWarnings("deprecation")
    public Npa getNpa(int idNpa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Npa npa = null;

        try {

            tx = session.beginTransaction();

            npa = (Npa)session.createCriteria(Npa.class).add(Restrictions.eq("idNpa", idNpa));

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }

        return npa;
    }

    @SuppressWarnings("deprecation")
    public Npa getNpa(String numeroNpa) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Npa npa = null;

        try {

            tx = session.beginTransaction();

            npa = (Npa)session.createCriteria(Npa.class).add(Restrictions.eq("numeroNpa", numeroNpa));

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }

        return npa;
    }

    @SuppressWarnings("unchecked")
    public List<Npa> getNpaList() throws Exception {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Npa> npaList = null;

        try {

            tx = session.beginTransaction();

            @SuppressWarnings("deprecation")
            Query query = session.createQuery("from Npa");
            npaList = query.list();

            tx.commit();
        } catch (Exception e) {
            catchException(e, tx);
        } finally {
            closeSession(session);
        }

        return npaList;
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
        }
    }
}
