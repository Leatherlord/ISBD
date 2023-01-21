package database.DAO;

import database.Relations.Fraction;
import database.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class FractionDAO {

    public Fraction get(long id) {
        Transaction transaction = null;
        Fraction relation = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            relation = session.get(Fraction.class, id);
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
    public List<Fraction> getAll() {
        Transaction transaction = null;
        List<Fraction> listOfAll = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            listOfAll = session.createQuery("from Fraction").getResultList();
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
