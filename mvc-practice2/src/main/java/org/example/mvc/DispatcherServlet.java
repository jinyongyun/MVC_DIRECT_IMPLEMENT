package org.example.mvc;

import org.example.mvc.controller.RequestMethod;
import org.example.mvc.view.JSPViewResolver;
import org.example.mvc.view.ModelAndView;
import org.example.mvc.view.View;
import org.example.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;


@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;

    private List<HandlerAdapter> handlerAdapters;

    private List<ViewResolver> viewResolvers;

    @Override
    public void init() throws ServletException {
        RequestMappingHandlerMapping rmhm = new RequestMappingHandlerMapping();
        rmhm.init();

        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("org.example");
        ahm.initialize();

        handlerMappings = List.of(rmhm, ahm);
        handlerAdapters = List.of(new SimpleControllerHandlerAdapter(), new AnnotationHandlerAdapter());
        viewResolvers = Collections.singletonList(new JSPViewResolver());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        log.info("[DispatcherServlet] #service started");
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        try {
            Object handler = handlerMappings.stream()
                            .filter(hm -> hm.findHandler(new HandlerKey(requestMethod, requestURI)) != null)
                            .map(hm -> hm.findHandler(new HandlerKey(requestMethod, requestURI)))
                            .findFirst()
                            .orElseThrow(() -> new ServletException("No handler for [" + requestMethod + ", " + requestURI + "]"));

            HandlerAdapter handlerAdapter = handlerAdapters.stream()
                    .filter(ha -> ha.supports(handler))
                    .findFirst()
                    .orElseThrow(() -> new ServletException("No adapter for handler [" + handler + "]"));

            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

            for (ViewResolver viewResolver : viewResolvers) {
               View view = viewResolver.resolveView(modelAndView.getViewName());
                view.render(modelAndView.getModel(), request, response);
            }


          /** RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
           requestDispatcher.forward(request, response);*/
        } catch (Exception e) {
            log.error("exception occur: [{}]", e.getMessage(), e);
            throw new ServletException(e);
        }
    }
}
