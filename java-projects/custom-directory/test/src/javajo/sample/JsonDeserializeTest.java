/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javajo.sample;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import javajo.sample.model.Translation;
import javajo.sample.model.TranslationJppAdapter;
import net.vvakame.util.jsonpullparser.JsonFormatException;
import net.vvakame.util.jsonpullparser.JsonPullParser;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonDeserializeTest {

    private final ClassLoader loader = getClass().getClassLoader();

    public static final String JSON = "sample-response.json";

    private String json;

    private Genson genson;

    private JsonPullParser parser;

    @Before
    public void setup() {
        try (Stream<String> st = loadResource(loader.getResourceAsStream(JSON))) {
            json = st.collect(joining());
        }
        genson = new GensonBuilder()
                .useMethods(true)
                .create();
        parser = JsonPullParser.newParser(json);
    }

    private static Stream<String> loadResource(InputStream input) {
        return new BufferedReader(new InputStreamReader(input)).lines();
    }

    @Test
    public void jsonPullParser() throws IOException, JsonFormatException {
        List<Translation> list = TranslationJppAdapter.getList(parser);
        assertThat(list)
                .hasSize(1);
        Translation translation = list.get(0);
        assertThat(translation.getTranslatedText())
                .isEqualTo("acquisition");
    }

    @Test
    public void genson() {
        List<Translation> list = genson.deserialize(json, new GenericType<List<Translation>>() {
        });
        assertThat(list)
                .hasSize(1);
        Translation translation = list.get(0);
        assertThat(translation.getTranslatedText())
                .isEqualTo("acquisition");
    }

    @Test
    public void resultShouldBeSame() throws IOException, JsonFormatException {
        List<Translation> byGenson = genson.deserialize(json, new GenericType<List<Translation>>() {});
        List<Translation> byJpp = TranslationJppAdapter.getList(parser);

        assertThat(byGenson)
                .isEqualTo(byJpp);
    }
}
