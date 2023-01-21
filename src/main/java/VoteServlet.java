import database.DAO.ApplicationDAO;
import database.DAO.VoteDAO;
import database.Relations.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "VoteServlet", value = "/vote")
public class VoteServlet extends HttpServlet {

    private ApplicationDAO applicationDAO;
    private VoteDAO voteDAO;

    @Override
    public void init() {
        applicationDAO = new ApplicationDAO();
        voteDAO = new VoteDAO();
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
        if (request.getParameter("vote") == null) {
            error(request, response, "Undefined behaviour");
            return;
        }
        if (request.getSession().getAttribute("currentIteration") == null) {
            error(request, response, "No voting is allowed now!");
            return;
        }
        Vote vote = new Vote();
        Voting_Iteration currentIteration = (Voting_Iteration) request.getSession().getAttribute("currentIteration");
        vote.setVotingIteration(currentIteration);
        vote.setDate(new Date(System.currentTimeMillis()));
        vote.setUser(user);
        Application application = applicationDAO.get(Long.parseLong(request.getParameter("vote")));
        vote.setApplication(application);
        for (Vote v : user.getVotes()) {
            if (v.getApplication().getId().longValue() == vote.getApplication().getId().longValue()) {
                error(request, response, "You have already voted for this application!");
                return;
            }
        }
        currentIteration.getVotes().add(vote);
        user.getVotes().add(vote);
        application.getVotes().add(vote);
        voteDAO.save(vote);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void error(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
