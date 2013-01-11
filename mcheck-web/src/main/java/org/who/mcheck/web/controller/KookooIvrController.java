package org.who.mcheck.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static java.lang.String.format;

@Controller
@RequestMapping("/kookoo")
public class KookooIvrController {

    public static final String DECISIONTREE_URL = "/decisiontree/node";

    public KookooIvrController() {
    }

    @RequestMapping("/ivr")
    public String handleRequest(HttpServletRequest request) {
        String treeName = request.getParameter("tree");
        String event = request.getParameter("event");
        String sessionId = request.getParameter("sid");
        String transitionKey = null;
        if ("GotDTMF".equals(event)) {
            transitionKey = request.getParameter("data");
        } else if("Hangup".equals(event) || "Disconnect".equals(event)) {
            transitionKey = "hangup";
        }
        String treePath = request.getParameter("trP");
        String language = request.getParameter("ln");
        return redirectToDecisionTree(treeName, transitionKey, treePath, language, sessionId, request.getServletPath());
    }

    private String redirectToDecisionTree(String treeName, String digits, String treePath, String language, String sessionId, String servletPath) {
        final String transitionKey = digits == null ? "" : "&trK=" + digits;
        return format("forward:%s%s?provider=kookoo&tree=%s&trP=%s&ln=%s&flowSessionId=%s%s", servletPath, DECISIONTREE_URL, treeName, treePath, language, sessionId, transitionKey).replaceAll("//", "/");
    }
}
