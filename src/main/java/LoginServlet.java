import database.DAO.Voting_IterationDAO;
import database.Relations.User;
import database.DAO.UserDAO;
import database.Relations.Voting_Iteration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;
    private Voting_IterationDAO votingIterationDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
        votingIterationDAO = new Voting_IterationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        error(request, response, "Trying to use GET while only POST supported here!");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("login") != null && request.getParameter("password") != null) {
            User user = userDAO.getByName(request.getParameter("login"));
            if (user == null || !user.getPassword().equals(request.getParameter("password"))) {
                error(request, response, "Wrong login and(or) password!");
                return;
            }
            request.getSession().setAttribute("user", user);
            List<Voting_Iteration> votingIterations = votingIterationDAO.getAll();
            Voting_Iteration currentIteration = null;
            for (Voting_Iteration iteration : votingIterations) {
                if (iteration.getUntil() == null) {
                    if (currentIteration == null) currentIteration = iteration;
                    else {
                        if (currentIteration.getSince().before(iteration.getSince())) {
                            currentIteration.setUntil(iteration.getSince());
                            votingIterationDAO.update(currentIteration);
                            currentIteration = iteration;
                        } else {
                            iteration.setUntil(currentIteration.getSince());
                            votingIterationDAO.update(iteration);
                        }
                    }
                }
            }
            request.getSession().setAttribute("currentIteration", currentIteration);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        error(request, response, "No login and(or) password provided!");
    }

    private void error(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
