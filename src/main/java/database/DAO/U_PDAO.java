package database.DAO;

import database.Relations.U_P;
import database.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class U_PDAO {

    public void save(U_P relation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(relation);
            transaction.commit();
            session.refresh(relation);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(U_P relation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(relation);
            transaction.commit();
            session.refresh(relation);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
