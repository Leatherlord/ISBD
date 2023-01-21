package database.DAO;

import database.Relations.Application;
import database.Util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;

public class ApplicationDAO {
    public void save(Application relation) {
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

    public Application get(long id) {
        Transaction transaction = null;
        Application relation = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            relation = session.get(Application.class, id);
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
    public List<Application> getAll() {
        Transaction transaction = null;
        List<Application> listOfAll = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            listOfAll = session.createQuery("from Application").getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return listOfAll;
    }

    public List<Application> getAllAfter(Date date) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Application> criteria = builder.createQuery(Application.class);
        Root<Application> root = criteria.from(Application.class);
        ParameterExpression<Date> dateParam = builder.parameter(Date.class);
        criteria.select(root).where(builder.greaterThan(root.get("date"), dateParam));
        Query<Application> query = session.createQuery(criteria);
        query.setParameter(dateParam, date);
        return query.getResultList();
    }

}
