import database.DAO.*;
import database.Relations.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@WebServlet(name = "CountServlet", value = "/count")
public class CountServlet extends HttpServlet {

    private VoteDAO voteDAO;
    private ApplicationDAO applicationDAO;
    private FractionDAO fractionDAO;
    private Voting_IterationDAO votingIterationDAO;
    private U_FDAO u_fdao;
    private U_PDAO u_pdao;

    @Override
    public void init() {
        voteDAO = new VoteDAO();
        applicationDAO = new ApplicationDAO();
        fractionDAO = new FractionDAO();
        votingIterationDAO = new Voting_IterationDAO();
        u_fdao = new U_FDAO();
        u_pdao = new U_PDAO();
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

        Voting_Iteration currentIteration = (Voting_Iteration) request.getSession().getAttribute("currentIteration");
        if (currentIteration == null) {
            error(request, response, "Cannot count votes when voting is closed!");
            return;
        }

        Voting_Iteration lastNotNull = votingIterationDAO.getLastFinished();
        List<Application> validApplications = lastNotNull == null ? applicationDAO.getAll()
                : applicationDAO.getAllAfter(lastNotNull.getUntil());

        List<Fraction> allFractions = fractionDAO.getAll();
        HashMap<Long, Integer> users = new HashMap<>();
        for (Fraction fraction : allFractions) {
            users.put(fraction.getId(), fraction.getU_fSet().size());
        }
        for (Application application : validApplications) {
            if (application.getVotes().size() > users.get(application.getFraction().getId()) / 2) {
                if (application.getPosition().getId() == 1) {

                    U_F u_f = new U_F();
                    u_f.setFraction(application.getFraction());
                    u_f.setUser(application.getUser());
                    u_f.setSince(new Date(System.currentTimeMillis()));
                    application.getFraction().getU_fSet().add(u_f);
                    u_fdao.save(u_f);

                    U_P u_p = new U_P();
                    u_p.setFraction(application.getFraction());
                    u_p.setUser(application.getUser());
                    u_p.setSince(new Date(System.currentTimeMillis()));
                    u_p.setPosition(application.getPosition());
                    application.getUser().getU_pSet().add(u_p);
                    application.getFraction().getU_pSet().add(u_p);
                    application.getPosition().getU_pSet().add(u_p);
                    u_pdao.save(u_p);

                } else {

                    for (U_P u_p : application.getUser().getU_pSet()) {
                        if (u_p.getFraction().getId().longValue() == application.getFraction().getId()) {
                            u_p.setPosition(application.getPosition());
                            u_pdao.update(u_p);
                            break;
                        }
                    }

                }
            }
        }

        votingIterationDAO.update(currentIteration);
        request.getSession().setAttribute("currentIteration", null);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void error(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
