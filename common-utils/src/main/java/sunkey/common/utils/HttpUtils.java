package sunkey.common.utils;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.*;
import okio.BufferedSink;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-03-18 09:48
 **/
public class HttpUtils {

    private static final OkHttpClient client = new OkHttpClient.Builder().build();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @SneakyThrows
    public static String execute(Request request) {
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        }
        return null;
    }

    public static String post(String url, Map<String, ?> form,
                              String partName, String fileName, InputStream in) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (!CollectionUtils.isEmpty(form)) {
            for (Map.Entry<String, ?> entry : form.entrySet()) {
                String val = StringUtils.def(entry.getValue(), null);
                if (StringUtils.hasText(entry.getKey()) && val != null)
                    builder.addFormDataPart(entry.getKey(), val);
            }
        }
        MediaType mediaType = MediaType.parse("file/*");
        builder.addFormDataPart(partName, fileName,
                new InputStreamRequestBody(mediaType, in)).build();
        return execute(new Request.Builder()
                .url(url)
                .post(builder.build())
                .build());
    }

    public static String post(String url, Map<String, ?> form) {
        FormBody.Builder builder = new FormBody.Builder();
        if (!CollectionUtils.isEmpty(form)) {
            for (Map.Entry<String, ?> entry : form.entrySet()) {
                String val = StringUtils.def(entry.getValue(), null);
                if (StringUtils.hasText(entry.getKey()) && val != null)
                    builder.add(entry.getKey(), val);
            }
        }
        return execute(new Request.Builder()
                .url(url)
                .post(builder.build())
                .build());
    }

    public static String postJson(String url, String json) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, json))
                .build();
        return execute(request);
    }

    public static String get(String url) {
        return execute(new Request.Builder()
                .url(url)
                .get().build());
    }

    public static String get(String url, Map<String, ?> queryParams) {
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        if (!CollectionUtils.isEmpty(queryParams)) {
            for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
                if (StringUtils.hasText(entry.getKey()))
                    builder.addQueryParameter(entry.getKey(), StringUtils.def(entry.getValue(), null));
            }
        }
        return execute(new Request.Builder()
                .url(builder.build())
                .get().build());
    }

    @AllArgsConstructor
    public static class InputStreamRequestBody extends RequestBody {

        @NonNull
        private final MediaType mediaType;
        @NonNull
        private final InputStream in;

        @Override
        public MediaType contentType() {
            return mediaType;
        }

        @Override
        public void writeTo(BufferedSink out) throws IOException {
            byte[] buf = new byte[256];
            for (int res = in.read(buf); res > -1; res = in.read(buf)) {
                out.write(buf, 0, res);
            }
        }
    }

}
