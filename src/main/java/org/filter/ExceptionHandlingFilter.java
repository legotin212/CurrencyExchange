package org.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.exception.EntityAlreadyExistsException;
import org.exception.NotFoundException;
import org.exception.WrongArgumentsException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);

        } catch (EntityAlreadyExistsException entityAlreadyExistsException) {

            ErrorResponse errorResponse = new ErrorResponse(entityAlreadyExistsException.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            httpResponse.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().flush();
        }
        catch (WrongArgumentsException exception){
            ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().flush();
        }
        catch (NotFoundException exception){
            ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().flush();
        }
        catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }

    }
}