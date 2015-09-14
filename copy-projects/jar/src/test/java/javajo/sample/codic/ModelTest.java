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
package javajo.sample.codic;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import javajo.sample.codic.model.Candidates;
import javajo.sample.codic.model.Translation;
import javajo.sample.codic.model.Word;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelTest {

    private final ClassLoader loader = getClass().getClassLoader();

    @Test
    public void parseJson() {
        Genson genson = new GensonBuilder().useMethods(true).create();
        try(InputStream is = loader.getResourceAsStream("sample-response.json")) {
            List<Translation> list = genson.deserialize(is, new GenericType<List<Translation>>() {});
            assertThat(list).hasSize(1);

            Translation trans = list.get(0);
            assertThat(trans.isSuccessful())
                    .isTrue();
            assertThat(trans.getText())
                    .matches("取得");
            assertThat(trans.getWords()).hasSize(1);

            List<Word> words = trans.getWords();
            Word word = words.get(0);
            assertThat(word.isSuccessful()).isTrue();
            assertThat(word.getText()).matches("取得");
            assertThat(word.getTranslatedText()).matches("acquisition");
            assertThat(word.getCandidates())
                    .contains(new Candidates("acquisition"), new Candidates("get"), new Candidates("acquired"), new Candidates("fetch"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
