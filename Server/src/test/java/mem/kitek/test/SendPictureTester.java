package mem.kitek.test;

import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.List;

/**
 * Created by RINES on 20.10.17.
 */
public class SendPictureTester {

    private final static List<File> files = Lists.newArrayList(
            new File("/Users/RinesThaix/Desktop/test/coding.jpg")
    );

    public static void main(String[] args) throws Exception {
        files.forEach(SendPictureTester::send);
    }

    public static void send(File file) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpPost httppost = new HttpPost("http://78.155.192.82:8228/api/sendPicture?cam_id=1");

                FileBody bin = new FileBody(file);

                HttpEntity reqEntity = MultipartEntityBuilder.create()
                        .addPart("image", bin)
                        .build();


                httppost.setEntity(reqEntity);

                System.out.println("executing request " + httppost.getRequestLine());
                CloseableHttpResponse response = httpclient.execute(httppost);
                try {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        System.out.println("Response content length: " +    resEntity.getContentLength());
                    }
                    EntityUtils.consume(resEntity);
                } finally {
                    response.close();
                }
            } finally {
                httpclient.close();
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
