package com.lim.bookopenapi.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lim.bookopenapi.domain.Book;
import com.lim.bookopenapi.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BookService {

    private final BookRepository bookRepository;

    @Value("${kakao.openapi.authorization}")
    private String kakaoAuthKey;

    public void getBookByQuery(String searchBy) {

        String title = "";
        String authors = "";
        String authorsArray = "";
        String contents = "";
        String datetime = "";
        String isbn = "";
        String status = "";
        String thumbnail = "";
        String bookUrl = "";
        String reqUrl = "https://dapi.kakao.com/v3/search/book?target=title";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "KakaoAK " + kakaoAuthKey);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("query=" + searchBy);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("responseCode: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body: " + result);

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(result);

            JsonElement documents = jsonElement.getAsJsonObject().get("documents");

            int size = jsonElement.getAsJsonObject().get("documents").getAsJsonArray().size();

            for (int i = 0; i < size; i++) {
                title = documents.getAsJsonArray().get(i).getAsJsonObject().get("title").getAsString();
                if (documents.getAsJsonArray().get(i).getAsJsonObject().get("authors").getAsJsonArray().size() != 1) {
                    int authorSize = documents.getAsJsonArray().get(i).getAsJsonObject().get("authors").getAsJsonArray().size();
                    for (int j = 0; j < authorSize; j++) {
                        authorsArray += documents.getAsJsonArray().get(i).getAsJsonObject().get("authors").getAsJsonArray().get(j)+"/";
                    }
                    authors = authorsArray;
                } else {
                    authors = documents.getAsJsonArray().get(i).getAsJsonObject().get("authors").getAsString();
                }
                contents = documents.getAsJsonArray().get(i).getAsJsonObject().get("contents").getAsString();
                datetime = documents.getAsJsonArray().get(i).getAsJsonObject().get("datetime").getAsString();
                isbn = documents.getAsJsonArray().get(i).getAsJsonObject().get("isbn").getAsString();
                status = documents.getAsJsonArray().get(i).getAsJsonObject().get("status").getAsString();
                thumbnail = documents.getAsJsonArray().get(i).getAsJsonObject().get("thumbnail").getAsString();
                bookUrl = documents.getAsJsonArray().get(i).getAsJsonObject().get("url").getAsString();

                log.info("----------------------------");
                log.info(i + "번째 책 파싱 시작");
                log.info(title);
                log.info(authors);
                log.info(contents);
                log.info(datetime);
                log.info(isbn);
                log.info(status);
                log.info(thumbnail);
                log.info(bookUrl);
                log.info(i + "번째 책 파싱 완료");
                log.info("----------------------------");

                Book book = Book.builder()
                        .title(title)
                        .authors(authors)
                        .contents(contents)
                        .datetime(datetime)
                        .isbn(isbn)
                        .status(status)
                        .thumbnail(thumbnail)
                        .bookUrl(bookUrl)
                        .build();

                bookRepository.save(book);
            }

            br.close();
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
