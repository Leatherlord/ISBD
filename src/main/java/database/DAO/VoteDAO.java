package database.DAO;

import database.Relations.Vote;
import database.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class VoteDAO {

    public void save(Vote relation) {
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
