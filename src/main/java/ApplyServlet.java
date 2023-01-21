import database.DAO.ApplicationDAO;
import database.DAO.FractionDAO;
import database.DAO.PositionDAO;
import database.DAO.UserDAO;
import database.Relations.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Set;

@WebServlet(name = "ApplyServlet", value = "/apply")
public class ApplyServlet extends HttpServlet {
    private ApplicationDAO applicationDAO;
    private FractionDAO fractionDAO;
    private PositionDAO positionDAO;

    @Override
    public void init() {
        applicationDAO = new ApplicationDAO();
        fractionDAO = new FractionDAO();
        positionDAO = new PositionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        error(request, response, "Trying to use GET while only POST supported here!");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            error(request, response, "Unauthorized access!");
            return;
        }
        if (request.getParameter("fraction") == null || request.getParameter("position") == null) {
            error(request, response, "No position specified!");
            return;
        }
        User user = (User)request.getSession().getAttribute("user");
        request.getSession().setAttribute("user", user);
        Fraction fraction = fractionDAO.get(Long.parseLong(request.getParameter("fraction")));
        Position position = positionDAO.get(Long.parseLong(request.getParameter("position")));
        Set<Application> applications = user.getApplications();
        for (Application application : applications) {
            if (application.getFraction().getId().longValue() == fraction.getId().longValue() &&
                    application.getPosition().getId().longValue() == position.getId().longValue()) {
                error(request, response, "You have already applied for this position!");
                return;
            }
        }
        boolean userInFraction = false;
        long posId = 0;
        Set<U_P> u_pSet = user.getU_pSet();
        for (U_P up : u_pSet) {
            if (up.getFraction().getId().longValue() == fraction.getId().longValue()) {
                userInFraction = true;
                posId = up.getPosition().getId();
            }
        }
        if (!userInFraction && position.getId() != 1) {
            error(request, response, "You are not allowed to apply for this position!");
            return;
        }
        if (userInFraction && (posId + 1 < position.getId() || posId >= position.getId())) {
            error(request, response, "You are not allowed to apply for this position!");
            return;
        }
        Application application = new Application();
        application.setPosition(positionDAO.get(position.getId()));
        application.setFraction(fraction);
        application.setDate(new Date(System.currentTimeMillis()));
        application.setUser(user);
        applicationDAO.save(application);
        user.getApplications().add(application);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void error(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
