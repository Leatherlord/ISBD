import database.DAO.Voting_IterationDAO;
import database.Relations.U_P;
import database.Relations.User;
import database.Relations.Voting_Iteration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Set;

@WebServlet(name = "VotingIterationsServlet", value = "/voting")
public class VotingIterationsServlet extends HttpServlet {

    private Voting_IterationDAO votingIterationDAO;

    @Override
    public void init() {
        votingIterationDAO = new Voting_IterationDAO();
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
        User user = (User) request.getSession().getAttribute("user");
        Set<U_P> positions = user.getU_pSet();
        boolean isElder = false;
        for (U_P pos : positions) {
            if (pos.getPosition().getId() == 3) {
                isElder = true;
                break;
            }
        }
        if (!isElder) {
            error(request, response, "Unauthorized access!");
            return;
        }
        if (request.getParameter("iter") == null) {
            error(request, response, "Undefined behaviour");
        }
        Voting_Iteration currentIteration = (Voting_Iteration) request.getSession().getAttribute("currentIteration");
        if ((request.getParameter("iter").equals("start") && currentIteration != null) ||
                (request.getParameter("iter").equals("stop") && currentIteration == null)) {
            error(request, response, "Wrong iteration signal!");
            return;
        }
        if (currentIteration == null) {
            currentIteration = new Voting_Iteration();
            currentIteration.setSince(new Date(System.currentTimeMillis()));
            votingIterationDAO.save(currentIteration);
            request.getSession().setAttribute("currentIteration", currentIteration);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        currentIteration.setUntil(new Date(System.currentTimeMillis()));
        request.getRequestDispatcher("/count").forward(request, response);
    }

    private void error(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
