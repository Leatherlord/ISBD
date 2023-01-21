<%@ page import="java.io.IOException" %>
<%@ page import="java.util.Set" %>
<%@ page import="database.Relations.*" %>
<%@ page import="database.DAO.PositionDAO" %>
<%@ page import="database.DAO.FractionDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="database.DAO.ApplicationDAO" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%!
    private final FractionDAO fractionDAO = new FractionDAO();
    private final PositionDAO positionDAO = new PositionDAO();
    private final ApplicationDAO applicationDAO = new ApplicationDAO();

    private void error(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
%>
<%
    if (request.getSession().getAttribute("user") == null) {
        error(request, response, "Unauthorized access!");
        return;
    }
    User user = (User) request.getSession().getAttribute("user");
    List<Fraction> allFractions = fractionDAO.getAll();
    List<Position> allPositions = positionDAO.getAll();
    List<Application> allApplications = applicationDAO.getAll();
%>
<html>
<head>
    <title>Main Page</title>
    <link rel="stylesheet" href="index.css">
</head>
<body>
<div class="header-container">
    <div class="header">
        <h2>Hello World!</h2>
        <h2>And especially you, <%=user.getName()%>!</h2>
        <form class="form" method="post" action="${pageContext.request.contextPath}/logout">
            <label>Return to login page:
                <button class="ui-button" name="logout_button" type="submit">Log out</button>
            </label>
        </form>
    </div>
</div>
<div class="container">
    <div class="positions">
        <h2>Your positions:</h2>
        <%
            Set<U_P> positions = user.getU_pSet();
            boolean isElder = false;
            for (U_P pos : positions) {
                if (pos.getPosition().getId() == 3) isElder = true;
                out.print("<h2>Position: ");
                out.print(pos.getPosition().getName());
                out.print(", Fraction: ");
                out.print(pos.getFraction().getName());
                out.print(", Since: ");
                out.print(pos.getSince());
                out.print(", Until: ");
                out.print(pos.getUntil() == null ? "Now" : pos.getUntil());
                out.print("</h2>");
            }
        %>
    </div>
    <div class="applications">
        <h2>Apply for a position:</h2>
        <form class="form" method="post" action="${pageContext.request.contextPath}/apply">
            <label>Select Fraction:
                <select name="fraction" multiple="multiple" size="<%=allFractions.size()%>">
                    <%
                        for (Fraction fraction : allFractions) {
                            out.println("<option value=\"" + fraction.getId() + "\">" + fraction.getName() + "</option>");
                        }
                    %>
                </select>
            </label>
            <br>
            <label>Select Position:
                <select name="position" multiple="multiple" size="<%=positions.size()%>">
                    <%
                        for (Position position : allPositions) {
                            out.println("<option value=\"" + position.getId() + "\">" + position.getName() + "</option>");
                        }
                    %>
                </select>
            </label>
            <button class="ui-button" name="apply" type="submit">Apply!</button>
        </form>
        <h2>Your applications:</h2>
        <%
            Set<Application> applications = user.getApplications();
            for (Application app : applications) {
                out.print("<h2>Application id: ");
                out.print(app.getId());
                out.print(", Position: ");
                out.print(app.getPosition().getName());
                out.print(", Fraction: ");
                out.print(app.getFraction().getName());
                out.print(", Date: ");
                out.print(app.getDate().toString());
                out.print("</h2>");
            }
        %>
    </div>
    <div class="votes">
        <h2>Your votes:</h2>
        <%
            Set<Vote> votes = user.getVotes();
            for (Vote vote : votes) {
                out.print("<h2>Vote id: ");
                out.print(vote.getId());
                out.print(", User: ");
                out.print(vote.getApplication().getUser().getName());
                out.print(", Fraction: ");
                out.print(vote.getApplication().getFraction().getName());
                out.print(", Position: ");
                out.print(vote.getApplication().getPosition().getName());
                out.print(", Date: ");
                out.print(vote.getDate().toString());
                out.print("</h2>");
            }
        %>
    </div>
    <div class="voting">
        <h2>Current voting iteration info:</h2>
        <%
            Voting_Iteration currentIteration = (Voting_Iteration) request.getSession().getAttribute("currentIteration");
            if (currentIteration == null) {
                out.println("<h2>Voting is closed!</h2>");
            } else {
                out.println("<h2>Started: " + currentIteration.getSince().toString() + "</h2>");
            }
            if (isElder) {
                if (currentIteration == null) {
                    out.println("<form class=\"form\" method=\"post\" action=\"/Elections/voting\">");
                    out.println("    <label>Start new voting iteration!:");
                    out.println("        <button class=\"ui-button\" name=\"start_voting\" type=\"submit\">Start!</button>");
                    out.println("    </label>");
                    out.println("    <input type=\"hidden\" name=\"iter\" value=\"start\">");
                    out.println("</form>");
                } else {
                    out.println("<form class=\"form\" method=\"post\" action=\"/Elections/voting\">");
                    out.println("    <label>Stop voting iteration!:");
                    out.println("        <button class=\"ui-button\" name=\"stop_voting\" type=\"submit\">Stop!</button>");
                    out.println("    </label>");
                    out.println("    <input type=\"hidden\" name=\"iter\" value=\"stop\">");
                    out.println("</form>");
                }
            }
            if (currentIteration != null) {
                out.println("Available voting options for you:");
                for (Application app : allApplications) {
                    boolean toShow = false;
                    for (U_P pos : positions) {
                        if (pos.getFraction().getId().longValue() != app.getFraction().getId().longValue()) continue;
                        boolean isVoted = false;
                        for (Vote vote : votes) {
                            if (vote.getApplication().getId().longValue() == app.getId().longValue()) {
                                isVoted = true;
                                break;
                            }
                        }
                        if (isVoted) continue;
                        if (app.getUser().getId().longValue() == user.getId().longValue()) continue;
//                        if (app.getDate().before(currentIteration.getSince())) continue;
                        if (app.getPosition().getId() == 1) {
                            toShow = true;
                            break;
                        }
                        if (app.getPosition().getId() == pos.getPosition().getId() + 1) {
                            toShow = true;
                            break;
                        }
                    }
                    if (toShow) {
                        out.println("<form class=\"form\" method=\"post\" action=\"/Elections/vote\">");
                        out.println("    <label>User: " + app.getUser().getName() + ", Fraction: " + app.getFraction().getName() + ", Position: " + app.getPosition().getName() + ":");
                        out.println("        <button class=\"ui-button\" name=\"stop_voting\" type=\"submit\">Vote!</button>");
                        out.println("    </label>");
                        out.println("    <input type=\"hidden\" name=\"vote\" value=\"" + app.getId() + "\">");
                        out.println("</form>");
                    }
                }
            }
        %>
    </div>
</div>

</body>

</html>
