package database.DAO;

import database.Relations.User;
import database.Util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserDAO {

    public void save(User relation) {
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

    public User getByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        ParameterExpression<String> nameParam = builder.parameter(String.class);
        criteria.select(root).where(builder.equal(root.get("name"), nameParam));
        Query<User> query = session.createQuery(criteria);
        query.setParameter(nameParam, name);
        return query.getResultList().stream().findFirst().orElse(null);
    }

}
