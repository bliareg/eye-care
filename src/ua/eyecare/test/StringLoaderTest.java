package ua.eyecare.test;

import org.junit.Before;
import org.junit.Test;
import ua.eyecare.tools.StringLoader;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by bliareg on 09.07.15.
 */
public class StringLoaderTest {

    StringLoader loader;
    @Before
    @Test
    public void connecting() throws IOException {
//        loader = new StringLoader("resources/string-res/localization_data_");
    }

    @Test
    public void testResource(){
    assertEquals("test", loader.getString("test"));

    }






}