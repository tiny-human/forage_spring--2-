package exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

// Intercepte les exceptions de tous les controllers.
// Retourne une vue HTML pour les requêtes classiques et du JSON pour les appels API.
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException ex, Model model, HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String uri = request.getRequestURI() == null ? "" : request.getRequestURI();

        boolean wantsJson = (accept != null && accept.contains("application/json")) || uri.startsWith("/api/");

        if (wantsJson) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("ok", false);
            body.put("message", ex.getMessage());
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(body);
        }

        model.addAttribute("errorMessage", ex.getMessage());
        return "error"; // → /WEB-INF/views/error.jsp
    }
}
