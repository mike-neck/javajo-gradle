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
package javajo.sample.model;

import com.owlike.genson.annotation.JsonProperty;
import net.vvakame.util.jsonpullparser.annotation.JsonKey;
import net.vvakame.util.jsonpullparser.annotation.JsonModel;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@JsonModel
public final class Translation {

    @JsonKey
    private boolean successful;

    @JsonKey
    private String text;

    @JsonKey("translated_text")
    @JsonProperty("translated_text")
    private String translatedText;

    @JsonKey
    private List<Word> words;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Translation)) return false;
        Translation that = (Translation) o;
        return Objects.equals(successful, that.successful) &&
                Objects.equals(text, that.text) &&
                Objects.equals(translatedText, that.translatedText) &&
                Objects.equals(words, that.words);
    }

    @Override
    public int hashCode() {
        return Objects.hash(successful, text, translatedText, words);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Translation:[", "]")
                .add("successful: [" + (successful) + "]")
                .add("text: [" + (text == null ? "null" : text) + "]")
                .add("translatedText: [" + (translatedText == null ? "null" : translatedText) + "]")
                .add("words: [" + (words == null ? "null" : words) + "]")
                .toString();
    }
}
