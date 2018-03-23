package hut34.wallet.controller;

import hut34.wallet.controller.dto.IndexMeta;
import hut34.wallet.testinfra.BaseControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest extends BaseControllerTest {

    @Override
    protected Object controller() {
        return new IndexController("APP NAME");
    }

    @Test
    public void indexPage() throws Exception {
        indexPageSuccess("/path");
    }

    @Test
    public void indexPage_willMatchSlash() throws Exception {
        indexPageSuccess("/");
    }

    @Test
    public void indexPage_willMatchMultiLevel() throws Exception {
        indexPageSuccess("/path/to/some/thing/");
    }

    @Test
    public void indexPage_willNotMatchStatic() throws Exception {
        mvc.perform(get("/static").contentType(MediaType.TEXT_HTML))
            .andExpect(status().isNotFound());
    }

    @Test
    public void indexPage_willNotMatchStatic_withSuffix() throws Exception {
        mvc.perform(get("/staticabc").contentType(MediaType.TEXT_HTML))
            .andExpect(status().isNotFound());
    }

    @Test
    public void indexPage_willNotMatchStatic_withSubPath() throws Exception {
        mvc.perform(get("/static/to/some/thing").contentType(MediaType.TEXT_HTML))
            .andExpect(status().isNotFound());
    }

    private void indexPageSuccess(String url) throws Exception {
        IndexMeta expected = new IndexMeta()
            .setTitle("APP NAME")
            .setUrl(url)
            .setDescription("Making Ethereum wallets easier for AI, bots, and people")
            .setImage("https://avatars3.githubusercontent.com/u/29348141?s=200&v=4");


        mvc.perform(get(url).contentType(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().size(1))
            .andExpect(model().attribute("meta", equalTo(expected)));
    }


}
