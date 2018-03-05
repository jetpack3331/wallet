package hut34.wallet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import hut34.wallet.controller.dto.IndexMeta;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final String applicationName;

    public IndexController(@Value("${app.name}") String applicationName) {
        this.applicationName = applicationName;
    }


    // The RequestMapping for the indexPage() method will not match "/" so easiest fix was to create another method with this mapping.
    @RequestMapping("/")
    public String rootIndexPage(HttpServletRequest request, Model model) {
        return indexPage(request, model);
    }

    @GetMapping(
        value = "/**",
        produces = MediaType.TEXT_HTML_VALUE,
        headers = {"!X-Appengine-Cron"})
    public String indexPage(HttpServletRequest request, Model model) {
        return getIndex(request, model);
    }

    private String getIndex(HttpServletRequest request, Model model) {
        logger.info("Loading index.html");
        IndexMeta meta = new IndexMeta()
            .setTitle(applicationName)
            .setUrl(request.getRequestURI())
            .setDescription(String.format("Try %s today!", applicationName))
            .setImage("https://lh3.googleusercontent.com/sm_h6TfeNXSFRKr3hB8C9Ir8lcWa4PYf56OwLeOieU3Y9G1HYiy-N0AvZzAN2dgJBnwWq-HKM5Bo9atsos8_FnXfHOJlXgLjjB_ZKaNfBt8rZTIOQad2x0YEbiSLOjj99sHRmbH_");

        model.addAttribute("meta", meta);

        // Generated index.html thymeleaf template from webpack
        return "index";
    }
}
