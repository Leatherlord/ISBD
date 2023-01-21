package database.DAO;

import database.Relations.Voting_Iteration;
import database.Util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class Voting_IterationDAO {

    public void save(Voting_Iteration relation) {
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

    public void update(Voting_Iteration relation) {
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

    public Voting_Iteration getLastFinished() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Voting_Iteration> criteria = builder.createQuery(Voting_Iteration.class);
        Root<Voting_Iteration> root = criteria.from(Voting_Iteration.class);
        criteria.select(root).where(builder.isNotNull(root.get("until"))).orderBy(builder.desc(root.get("until")));
        Query<Voting_Iteration> query = session.createQuery(criteria);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public List<Voting_Iteration> getAll() {
        Transaction transaction = null;
        List<Voting_Iteration> listOfAll = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            listOfAll = session.createQuery("from Voting_Iteration").getResultList();
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
