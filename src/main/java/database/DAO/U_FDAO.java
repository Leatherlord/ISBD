package database.DAO;

import database.Relations.U_F;
import database.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class U_FDAO {

    public void save(U_F relation) {
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
}
