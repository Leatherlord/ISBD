package database.DAO;

import database.Relations.Position;
import database.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PositionDAO {

    public Position get(long id) {
        Transaction transaction = null;
        Position relation = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            relation = session.get(Position.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return relation;
    }

    @SuppressWarnings("unchecked")
    public List<Position> getAll() {
        Transaction transaction = null;
        List<Position> listOfAll = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            listOfAll = session.createQuery("from Position").getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return listOfAll;
    }

}
