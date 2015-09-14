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
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import javajo.sample.codic.model.Translation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class Codic {

    private static final String PROPERTY_FILE = "codic.properties";

    private static final String PROPERTY_KEY = "codic.access.token";

    private static final String ENVIRONMENT_KEY = "CODIC_ACCESS_TOKEN";

    private static final String API_ENTRY_URL = "https://api.codic.jp/v1/engine/translate.json";

    private static final String CODIC_HTTP_AUTH_HEADER = "Authorization";

    private final String accessToken;

    public Codic() {
        String key = System.getenv(ENVIRONMENT_KEY);
        if (key != null) {
            accessToken = key;
        } else {
            ClassLoader loader = getClass().getClassLoader();
            try(InputStream is = getCodicProperties(loader)) {
                Properties properties = new Properties();
                properties.load(is);
                if (!properties.containsKey(PROPERTY_KEY)) {
                    throw new CodicException("Exception in initialization. Property key[codic.access.token] is missing.");
                }
                accessToken = properties.getProperty(PROPERTY_KEY);
            } catch (IOException e) {
                throw new CodicException(e);
            }
        }
    }

    public Codic(String accessToken) {
        this.accessToken = accessToken;
    }

    private static InputStream getCodicProperties(ClassLoader cl) throws CodicException {
        URL resource = cl.getResource(PROPERTY_FILE);
        boolean inResource = resource != null;
        File propertyFile = new File(PROPERTY_FILE);
        if (inResource) {
            return cl.getResourceAsStream(PROPERTY_FILE);
        } else if (propertyFile.exists()) {
            try {
                return new FileInputStream(propertyFile);
            } catch (FileNotFoundException e) {
                throw new CodicException("Exception in initialization. codic.properties is missing.", e);
            }
        } else {
            throw new CodicException("Exception in initialization. Environment variable [CODIC_ACCESS_TOKEN] or codic.properties is missing.");
        }
    }

    public List<Translation> translate(String text) {
        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.codic.jp")
                .addPathSegment("v1")
                .addPathSegment("engine")
                .addPathSegment("translate.json")
                .addQueryParameter("text", text)
                .build()
                .url();
        Request request = new Builder()
                .url(url)
                .addHeader(CODIC_HTTP_AUTH_HEADER, "Bearer " + accessToken)
                .get()
                .build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            int code = response.code();
            if (code != 200) {
                throw new CodicException("Exception in calling API[" + API_ENTRY_URL + "] with status " + code + ".");
            }
            Genson genson = new GensonBuilder().useMethods(true).create();
            String string = response.body().string();
            return genson.deserialize(string, new GenericType<List<Translation>>() {});
        } catch (IOException e) {
            throw new CodicException("Exception in calling API[" + API_ENTRY_URL + "].", e);
        }
    }
}
